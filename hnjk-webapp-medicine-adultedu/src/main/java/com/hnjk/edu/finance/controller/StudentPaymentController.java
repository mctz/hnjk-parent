package com.hnjk.edu.finance.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.commons.lang.ArrayUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.MD5CryptorUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.ReturnPremium;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.service.IYearPaymentStandardService;
import com.hnjk.edu.finance.util.TonlyPayUtil;
import com.hnjk.edu.finance.vo.CheckFileTotalVO;
import com.hnjk.edu.finance.vo.CheckFileVO;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.PayStudentInfoVO;
import com.hnjk.edu.finance.vo.StudentPaymentVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IStudentBaseInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.extend.taglib.function.JstlCustFunction;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SetTime;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.ISetTimeService;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

/**
 * 学生缴费标准管理.
 * <code>StudentPaymentController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-12 下午04:20:31
 * @see 
 * @version 1.0
 */
@Controller
public class StudentPaymentController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -5916501591304559296L;
	private static final String POS_TRADE_RSPONSE_SUCCESS = "00";// 成功
//	private static final String POS_TRADE_RSPONSE_ABNORMITY = "FF";// 异常
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;

	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService; 
	
	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService; 
	
	@Autowired
	@Qualifier("setTimeService")
	private ISetTimeService setTimeService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentBaseInfoService")
	private IStudentBaseInfoService studentBaseInfoService;
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;
	
	/*@Autowired
	@Qualifier("feeMajorService")
	private IFeeMajorService feeMajorService;*/
	
	@Autowired
	@Qualifier("yearPaymentStandardService")
	private IYearPaymentStandardService yearPaymentStandardService;
	
	/**
	 * 学生缴费标准列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/list.html")
	public String listStudentPayment(HttpServletRequest request, Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("branchSchool.unitCode,grade.gradeName ");	
		objPage.setOrder(Page.ASC);
		
		Map<String, Object> condition = getStudentPaymentCondition(request);
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		String branchSchool=cureentUser.getOrgUnit().getResourceid();
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0) {
			condition.put("brSchool", branchSchool);//判断用户是否为校外学习中心
		}
		
		Page page = studentPaymentService.findStudentPaymentByContidion(condition, objPage);	
		
		model.addAttribute("studentPaymentList", page);
		model.addAttribute("condition", condition);
		model.addAttribute("paymentMajorSelect",graduationQualifService.getGradeToMajor1((String)condition.get("gradeid"),(String)condition.get("majorid"),"studentPayment_majorid","majorid","searchPaymentMajorClick()",(String)condition.get("classicid")));
		model.addAttribute("paymentClassesSelect",graduationQualifService.getGradeToMajorToClasses1((String)condition.get("gradeid"),(String)condition.get("majorid"),(String)condition.get("classesId"),(String)condition.get("brSchool"),"studentPayment_classesid","classesId",(String)condition.get("classicid")));
//		return "/edu3/finance/studentpayment/studentpayment-list";
		return "/edu3/finance/studentpayment/studentpayment-list-new";
	}
	/**
	 * @param request
	 * @return
	 */
	private Map<String, Object> getStudentPaymentCondition(	HttpServletRequest request) {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		String brSchool = request.getParameter("brSchool");
		String majorid = request.getParameter("majorid");
		String classicid = request.getParameter("classicid");
		String gradeid = request.getParameter("gradeid");
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String yearInfoId = request.getParameter("yearInfoId");
		String qianfeipay = request.getParameter("qianfeipay");
		String term = request.getParameter("term");		
		String chargeStatus = request.getParameter("chargeStatus");
		String classesId = request.getParameter("classesId");
		String studentStatus = request.getParameter("studentStatus");
		if(ExStringUtils.isNotEmpty(brSchool)) {
			condition.put("brSchool", brSchool);
		}
		if(ExStringUtils.isNotEmpty(majorid)) {
			condition.put("majorid", majorid);
		}
		if(ExStringUtils.isNotEmpty(classicid)) {
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)) {
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(chargeStatus)) {
			condition.put("chargeStatus", chargeStatus);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus", studentStatus);
		}
		if(ExStringUtils.isNotEmpty(qianfeipay)) {
			condition.put("qianfeipay", Double.parseDouble(qianfeipay.toString()));
		}
		return condition;
	}
	
	/**
	 * 学生缴费情况
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	// TODO：新的收费流程这个功能已经不用 2015-11-24
	@RequestMapping("/edu3/finance/studentpaymentstu/liststu.html")
	public String studentPayment(HttpServletRequest request, Page objPage,ModelMap model) throws WebException{
//		objPage.setOrderBy("unit.unitCode");	
		objPage.setOrder(Page.ASC);
		
		Map<String, Object> condition = getStudentPaymentCondition(request);
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		String branchSchool=cureentUser.getOrgUnit().getResourceid();
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0) {
			condition.put("brSchool", branchSchool);//判断用户是否为校外学习中心
		}
		Page page = studentPaymentService.findStudentPaymentStuByContidion(condition, objPage);	
		model.addAttribute("studentPaymentListStu", page);
		model.addAttribute("condition", condition);
		return "/edu3/finance/studentpayment/studentpaymentstu-list";
	}
	/**
	 * 编辑学生缴费标准
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/input.html")
	public String editStudentPayment(String resourceid, String isDerate, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ 
			StudentPayment studentPayment = studentPaymentService.get(resourceid);	
			model.addAttribute("studentPayment", studentPayment);
		}
		model.addAttribute("isDerate", ExStringUtils.trimToEmpty(isDerate));//是否减免设置
		return "/edu3/finance/studentpayment/studentpayment-form";
	}
	/**
	 * 保存学生缴费标准
	 * @param studentPayment
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/save.html")
	public void saveFeeMajor(StudentPayment studentPayment, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String isDerate = ExStringUtils.trimToEmpty(request.getParameter("isDerate"));//是否减免设置
			if(ExStringUtils.isNotBlank(studentPayment.getResourceid())){
				StudentPayment payment = studentPaymentService.get(studentPayment.getResourceid());
				if("Y".equals(isDerate)){//减免
					payment.setDerateFee(studentPayment.getDerateFee());//减免费用
					payment.setDereteReason(studentPayment.getDereteReason());//减免理由
					
					Double facepayFee = payment.getFacepayFee();
					Double recpayFee = payment.getRecpayFee();
					Double derateFee = payment.getDerateFee();
					if((facepayFee==0&&recpayFee-facepayFee>derateFee)||facepayFee==null){
						payment.setChargeStatus("0");
					}else if(recpayFee-derateFee==facepayFee){
						payment.setChargeStatus("1");
					}else if(recpayFee<derateFee){
						throw new Exception("减免金额不能大于实际应缴金额") ;
					}
					
				} else {//编辑
					payment.setRecpayFee(studentPayment.getRecpayFee());
					payment.setChargeEndDate(studentPayment.getChargeEndDate());
					payment.setMemo(studentPayment.getMemo());
					
					Double facepayFee = payment.getFacepayFee();
					Double recpayFee = payment.getRecpayFee();
					Double derateFee = payment.getDerateFee();
					if((facepayFee==0&&recpayFee-facepayFee>derateFee)||facepayFee==null){
						payment.setChargeStatus("0");
					}else if(recpayFee-derateFee==facepayFee){
						payment.setChargeStatus("1");
					}else if(recpayFee<derateFee){
						throw new Exception("减免金额不能大于实际应缴金额") ;
					}
				}
				payment.setEnableLogHistory(true);//记录日志
				studentPaymentService.update(payment);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_FINANCE_STUDENTPAYMENT_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/finance/studentpayment/input.html?resourceid="+studentPayment.getResourceid()+("Y".equals(isDerate)?"&isDerate=Y":""));
		}catch (Exception e) {
			logger.error("保存缴费类别出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 设置缓缴期限
	 * @param resourceid
	 * @param deferEndDate
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/deferenddate/edit.html")
	public void saveStudentPaymentDeferDate(String resourceid, Date deferEndDate, String deferType, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){			
				StudentPayment payment = studentPaymentService.get(resourceid);
				if("0".equals(deferType)){//取消缓缴
					payment.setIsDefer(Constants.BOOLEAN_NO);//取消缓缴
					payment.setDeferEndDate(null);//缓缴期限
					payment.setEnableLogHistory(true);//记录日志
					studentPaymentService.update(payment);
					map.put("statusCode", 200);
					map.put("message", "取消缓缴成功！");	
				} else {
					if(deferEndDate != null){
						payment.setIsDefer(Constants.BOOLEAN_YES);//设置为缓缴
						payment.setDeferEndDate(deferEndDate);//缓缴期限
						payment.setEnableLogHistory(true);//记录日志
						studentPaymentService.update(payment);
						map.put("statusCode", 200);
						map.put("message", "设置缓缴期限成功！");	
					}
				}				
			} 
		} catch (Exception e) {
			logger.error("设置缓缴期限出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置缓缴期限失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	/**
	 * 缓缴记录管理
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/defere/list.html")
	public String listStudentPaymentDefer(String resourceid, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ 
			StudentPayment studentPayment = studentPaymentService.get(resourceid);	
			//查询缓缴记录
			List<StudentPayment> studentPaymentList = studentPaymentService.findDeferStudentPayment(studentPayment);
			model.addAttribute("studentPayment", studentPayment);
			model.addAttribute("studentPaymentList", studentPaymentList);
			model.addAttribute("beforeTotalFee", ExCollectionUtils.isNotEmpty(studentPaymentList)?studentPayment.getBeforeTotalFee():studentPayment.getRecpayFee());
		}
		return "/edu3/finance/studentpayment/studentpayment-defer-list";
	}
	/**
	 * 设置多次缓缴记录
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/defere/save.html")
	public void saveStudentPaymentDefer(String resourceid, HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			String[] deferid = request.getParameterValues("deferid");
			String[] deferFee = request.getParameterValues("deferFee");
			String[] deferEndDate = request.getParameterValues("deferEndDate");
			if(ExStringUtils.isNotBlank(resourceid)){
				List<StudentPayment> studentPaymentList = new ArrayList<StudentPayment>();
				StudentPayment studentPayment = studentPaymentService.get(resourceid);
				List<StudentPayment> childList = studentPaymentService.findDeferStudentPayment(studentPayment);
				
				Double beforeTotalFee = ExCollectionUtils.isNotEmpty(childList)?studentPayment.getBeforeTotalFee():studentPayment.getRecpayFee();
				if(ArrayUtils.isEmpty(deferid)){//缓缴设置为空
					if(ExCollectionUtils.isNotEmpty(childList)){
//						Double totalFee = 0.0;
						for (StudentPayment payment : childList) {
							payment.setIsDeleted(1);//标记为删除
							payment.setEnableLogHistory(true);
							studentPaymentList.add(payment);
//							totalFee=totalFee+payment.getFacepayFee();
						}
						studentPayment.setRecpayFee(beforeTotalFee);//还原为普通标准
						studentPayment.setBeforeTotalFee(null);
						studentPayment.setIsMultiDefer(Constants.BOOLEAN_NO);//还原
						studentPayment.setEnableLogHistory(true);//记录日志
//						studentPayment.setFacepayFee(totalFee);
//						if(totalFee==0||totalFee==null){
//							studentPayment.setChargeStatus("0");
//						}else if(totalFee<studentPayment.getRecpayFee()-studentPayment.getDerateFee()){
//							studentPayment.setChargeStatus("-1");
//						}else if(studentPayment.getRecpayFee()-studentPayment.getDerateFee()==totalFee){
//							studentPayment.setChargeStatus("1");
//						}else if(studentPayment.getRecpayFee()-studentPayment.getDerateFee()<totalFee){
//							throw new Exception("已缴金额不能大于实际应缴金额") ;
//						}
						studentPaymentList.add(studentPayment);
					}
				} else {
					//删除不存在的缴费记录
					for (StudentPayment payment : childList) {
						if(!ArrayUtils.contains(deferid, payment.getResourceid())){
							payment.setIsDeleted(1);//标记为删除
							payment.setEnableLogHistory(true);
							studentPaymentList.add(payment);
						}
					}
					childList.removeAll(studentPaymentList);//剔除以删除数据
					
					Double totalFee = 0.0;//总缓缴金额
					for (int i = 0; i < deferid.length; i++) {
						StudentPayment child = new StudentPayment();;
						if(ExStringUtils.isNotBlank(deferid[i])){
							for (StudentPayment payment : childList) {
								if(payment.getResourceid().equals(deferid[i])){
									child = payment;
									break;
								}
							}
							childList.remove(child);
						} else {
							//child = new StudentPayment();
							ExBeanUtils.copyProperties(child, studentPayment);
							child.setChargeEndDate(null);
							child.setBeforeTotalFee(null);
							child.setResourceid(null);
							child.setIsDefer(Constants.BOOLEAN_YES);
							child.setChargeStatus("0");			
							child.setDerateFee(0.0);	
							child.setDereteReason(null);
//							child.setFacepayFee(0.0);
						}
						child.setIsMultiDefer(Constants.BOOLEAN_YES);//多次缓缴记录
						child.setRecpayFee(Double.valueOf(ExStringUtils.trimToEmpty(deferFee[i])));
						child.setDeferEndDate(ExDateUtils.convertToDate(ExStringUtils.trimToEmpty(deferEndDate[i])));
						child.setEnableLogHistory(true);//记录日志
						totalFee += child.getRecpayFee();
						
						studentPaymentList.add(child);					
					}
					studentPayment.setBeforeTotalFee(beforeTotalFee);//缴费标准备份总缴费金额
					studentPayment.setRecpayFee(beforeTotalFee-totalFee);//设置剩余金额
					studentPayment.setIsMultiDefer(Constants.BOOLEAN_YES);//多次缓缴记录
					studentPayment.setEnableLogHistory(true);//记录日志
					
						studentPaymentList.add(studentPayment);
					
				}
				studentPaymentService.batchSaveOrUpdate(studentPaymentList);
				map.put("statusCode", 200);
				map.put("message", "设置成功");
				map.put("navTabId", "RES_FINANCE_STUDENTPAYMENT");
			} 
		} catch (Exception e) {
			logger.error("设置多次缓缴记录出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置多次缓缴记录失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	/**
	 * 导出学生缴费标准
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/defere/export.html")
	public void exportStudentPayment(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String, Object> condition = getStudentPayment(request);
		condition.put("orderby", "branchSchool.unitCode,grade.gradeName ");
		List<StudentPayment> list = studentPaymentService.findStudentPaymentByCondition(condition);		
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//文件输出服务器路径
		try{
			//导出
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");		
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeTeachingType");
			dictCodeList.add("yesOrNo");
			dictCodeList.add("CodeChargeStatus");
			dictCodeList.add("CodeStudentStatus");
			//模板文件路径
			String templateFilepathString = "studentPayment.xls";
			exportExcelService.initParamsByfile(disFile, "studentPayment", list, dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE), null	, null);		
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, null);
			disFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
			downloadFile(response, "学生缴费记录.xls", disFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出学生缴费标准失败\")</script>");
		}
	}
	
	private Map<String, Object> getStudentPayment(HttpServletRequest request) {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		String brSchool = request.getParameter("brSchool");
		String majorid = request.getParameter("majorid");
		String classicid = request.getParameter("classicid");
		String gradeid = request.getParameter("gradeid");
		String classesId = request.getParameter("classesId");
		String name = ExStringUtils.trimToEmpty(request.getParameter("name1"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String chargeStatus = request.getParameter("chargeStatus");
		String studentStatus = request.getParameter("studentStatus");
	
		if(ExStringUtils.isNotEmpty(brSchool)) {
			condition.put("brSchool", brSchool);
		}
		if(ExStringUtils.isNotEmpty(majorid)) {
			condition.put("majorid", majorid);
		}
		if(ExStringUtils.isNotEmpty(classicid)) {
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			name=ExStringUtils.getEncodeURIComponentByTwice(name);
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(chargeStatus)) {
			condition.put("chargeStatus", chargeStatus);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus", studentStatus);
		}
		
		return condition;
	}
	
	/**
	 * 上传缴费
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/finance/studentpayment/upload.html")
	public String uploadToReg(HttpServletRequest request,HttpServletResponse response,ModelMap model){		
		return "/edu3/finance/studentpayment/studentpaymentUpload-view";
	}
	
	/**
	 * 上传缴费(右江医)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/finance/studentpayment/upload1.html")
	public String uploadToReg1(HttpServletRequest request,HttpServletResponse response,ModelMap model){		
		return "/edu3/finance/studentpayment/studentpaymentUpload-view1";
	}
	
	/**
	 * 批量缴费导入(old)
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/Import.html")
	public void showExcelExportImport(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		Page objPage = new Page();
		objPage.setOrder(Page.ASC);
		try {			
			if( ExStringUtils.isNotBlank(attachId)){				
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
			
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeTeachingType");
				dictCodeList.add("yesOrNo");
				dictCodeList.add("CodeChargeStatus");
				//处理数据字典字段
				importExcelService.initParmas(excel, "studentPaymentImport", dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYNAME));
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				List<StudentPaymentVo> modelList = importExcelService.getModelList();
				StringBuffer msg = new StringBuffer(); 
				for (StudentPaymentVo s : modelList) {
					Map<String ,Object> condition = new HashMap<String, Object>();
						String studyNo= s.getStudyNo();
						String yearName= s.getYearName();
						String name= s.getName();
						//截止日期
						String chargeEndDate= s.getChargeEndDate();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date date;
						if(ExStringUtils.isNotBlank(chargeEndDate)){
							
							date = sdf.parse(chargeEndDate);
							condition.put("chargeEndDate", date); 
						}
						//缓缴日期
						String deferEndDate= s.getDeferEndDate();
						if(ExStringUtils.isNotBlank(deferEndDate)){
							 
							date = sdf.parse(deferEndDate);
							condition.put("deferEndDate", date); 
						}
						
						condition.put("yearName", yearName); 
						Page year1= yearInfoService.findYearInfoByCondition(condition, objPage);
						List<YearInfo> year2 = year1.getResult();
						condition.remove("yearName");
						
						condition.put("name", name);
						condition.put("studyNo", studyNo);
						if(year2.size()==1){
							condition.put("yearInfoId", year2.get(0).getResourceid());
							
							List<StudentPayment> studentPayments = studentPaymentService.findStudentPaymentByCondition(condition);
							if(studentPayments.size()==1){
								StudentPayment ss = studentPayments.get(0);
//								ss.setRecpayFee(s.getRecpayFee());
								ss.setFacepayFee(s.getFacepayFee());
								Double facepayFee = ss.getFacepayFee();
								Double recpayFee = ss.getRecpayFee();
								Double derateFee = ss.getDerateFee();
								if(facepayFee==0||facepayFee==null){
									ss.setChargeStatus("0");
								}else if(facepayFee<recpayFee-derateFee){
									ss.setChargeStatus("-1");
								}else if(recpayFee-derateFee==facepayFee){
									ss.setChargeStatus("1");
								}else if(recpayFee-derateFee<facepayFee){
									throw new Exception("已缴金额不能大于实际应缴金额") ;
								}
//								ss.setChargeStatus(s.getChargeStatus());
								ss.setEnableLogHistory(true);
								studentPaymentService.saveOrUpdate(ss);
							}
						}
				
				}
			
				map.put("message","导入成功");
				map.put("statusCode", 200);				
				map.put("reloadUrl", request.getContextPath()+"/edu3/finance/studentpayment/list.html");
				map.put("navTabId", "RES_SCHOOL_REGISTER_ENROLL");
			} else {
				map.put("statusCode", 300);				
				map.put("message", "请上传附件.");
			}
		} catch (Exception e) {
			logger.error("导入出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入失败:<br/>"+e.getMessage());
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 缴费情况统计
	 * @param year
	 * @param currentIndex
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/finance/studentpayment/stat/list.html","/edu3/finance/studentpayment/stat/export.html"})
	public String listStudentPaymentStat(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
		String yearInfo = request.getParameter("yearInfoId");
		String branchSchool = request.getParameter("branchSchool");
		String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
		String flag = request.getParameter("flag");
		boolean isBrschool = false;
		
		YearInfo year=null;
		if(ExStringUtils.isNotBlank(yearInfo)){
			condition.put("yearInfoId", yearInfo);
			year = yearInfoService.get(yearInfo);
		}else {
			Calendar c = Calendar.getInstance();
			year=yearInfoService.getByFirstYear(Long.valueOf(c.get(Calendar.YEAR)));
			String yearInfoId="";
			if(year != null){
				yearInfoId=year.getResourceid();
			}
			condition.put("yearInfoId",yearInfoId );
		}
		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			isBrschool = true;
		}
		if(ExStringUtils.isNotBlank(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		condition.put("currentIndex", currentIndex);
		
		Map<String, Object> map = new HashMap<String, Object>();
		String[] statTypes = {"teachingType","major","classic","brSchool"};
		for (String statType : statTypes) {
			List<Map<String, Object>> list = studentPaymentService.statStudentPayment(condition, statType);
			calculateTotal(list);//加入总计
			map.put(statType, list);
		}		
		model.addAttribute("statListMap", map);
		model.addAttribute("statTypes", statTypes);
		model.addAttribute("statType", statTypes[Integer.parseInt(currentIndex)]);
		model.addAttribute("isBrschool", isBrschool);
		model.addAttribute("condition", condition);
		if("export".equals(flag)){
			String fileName = "";
			try {
				fileName = URLEncoder.encode(year+"年缴费情况统计.xls", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getSession().setAttribute("statListMap", map);
			response.setContentType("application/vnd.ms-excel");
			//attachment
		  	response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		  	return "/edu3/finance/studentpayment/studentpayment-export";
		}
		return "/edu3/finance/studentpayment/studentpayment-stat";
	}
	
	/**
	 * 计算各项总计
	 * @param list
	 */
	private void calculateTotal(List<Map<String, Object>> list) {
		if(ExCollectionUtils.isNotEmpty(list)){
			BigDecimal recpayfeeCount = new BigDecimal(0);
			BigDecimal recpayfee = new BigDecimal(0);
			BigDecimal fullfeeCount = new BigDecimal(0);
			BigDecimal fullfeeSum = new BigDecimal(0);
			BigDecimal deratefeeCount = new BigDecimal(0);
			BigDecimal deratefeeSum = new BigDecimal(0);
			BigDecimal owefeeCount = new BigDecimal(0);
			BigDecimal owefeeSum = new BigDecimal(0);
			BigDecimal partfeeCount = new BigDecimal(0);
			BigDecimal partfeeSum = new BigDecimal(0);
			for (Map<String, Object> obj : list) {
				BigDecimal objFullfeeCount = (BigDecimal)obj.get("FULLFEECOUNT");
				BigDecimal objAllfeeCount = (BigDecimal)obj.get("ALLFEECOUNT");
				recpayfeeCount = recpayfeeCount.add((BigDecimal)obj.get("ALLFEECOUNT"));
				recpayfee = recpayfee.add((BigDecimal)obj.get("RECPAYFEE"));
				fullfeeCount = fullfeeCount.add((BigDecimal)obj.get("FULLFEECOUNT"));
				fullfeeSum = fullfeeSum.add((BigDecimal)obj.get("FULLFEESUM"));
				deratefeeCount = deratefeeCount.add((BigDecimal)obj.get("DERATEFEECOUNT"));
				deratefeeSum = deratefeeSum.add((BigDecimal)obj.get("DERATEFEESUM"));
				owefeeCount = owefeeCount.add((BigDecimal)obj.get("OWEFEECOUNT"));
				owefeeSum = owefeeSum.add((BigDecimal)obj.get("OWEFEESUM"));
				partfeeCount = partfeeCount.add((BigDecimal)obj.get("PARTFEECOUNT"));
				partfeeSum = partfeeSum.add((BigDecimal)obj.get("PARTFEESUM"));
				BigDecimal fullfeePer = new BigDecimal(0);
				if(objAllfeeCount.longValue()>0){
					fullfeePer = objFullfeeCount.divide(objAllfeeCount, 3, BigDecimal.ROUND_HALF_UP);
				}
				obj.put("fullfeePer", fullfeePer);
				obj.put("fullfeePerStr", fullfeePer.multiply(new BigDecimal(100)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_HALF_UP)+"%");
			}
			Map<String, Object> total = new HashMap<String, Object>();
			total.put("statType", "总：");
			total.put("allfeeCount", recpayfeeCount);
			total.put("recpayfee", recpayfee);
			total.put("fullfeeCount", fullfeeCount);
			total.put("fullfeeSum", fullfeeSum);
			total.put("deratefeeCount", deratefeeCount);
			total.put("deratefeeSum", deratefeeSum);
			total.put("owefeeCount", owefeeCount);
			total.put("owefeeSum", owefeeSum);
			total.put("partfeeCount", partfeeCount);
			total.put("partfeeSum", partfeeSum);
			BigDecimal totalFullfeePer = new BigDecimal(0);
			if(recpayfeeCount.longValue()>0){
				totalFullfeePer = fullfeeCount.divide(recpayfeeCount, 3, BigDecimal.ROUND_HALF_UP);
			}
			total.put("fullfeePer", totalFullfeePer);
			total.put("fullfeePerStr", totalFullfeePer.multiply(new BigDecimal(100)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_HALF_UP)+"%");
			list.add(total);
		}		
	}
	
	/**旧方法，不用
	 * 导出缴费情况统计
	 * @param year
	 * @param type
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	/*@RequestMapping("/edu3/finance/studentpayment/stat/export.html")
	public void exportStudentFactFeeStat(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
		String yearInfo = request.getParameter("yearInfoId");
		String branchSchool = request.getParameter("branchSchool");
		String currentIndex = request.getParameter("currentIndex");
		
		YearInfo year=null;
		if(ExStringUtils.isNotBlank(yearInfo)){
			condition.put("yearInfo", yearInfo);
			year = yearInfoService.get(yearInfo);
		}else {
			Calendar c = Calendar.getInstance();
			year=yearInfoService.getByFirstYear(Long.valueOf(c.get(Calendar.YEAR)));
			condition.put("yearInfo", year.getResourceid());
		}
		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotBlank(branchSchool)){
			condition.put("brSchool", branchSchool);
		}
		condition.put("currentIndex", ExStringUtils.defaultIfEmpty(currentIndex, "0"));
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板	
			templateMap.put("yearInfo", ("0".equals(year)?"":(year+"年")));
			//初始化配置参数
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeLearningStyle");
			dictCodeList.add("terminalType");
			dictCodeList.add("yesOrNo");
			Map<String , Object> mapdict = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			String[] filterColunms1 = {"firstyear","statType","teachingType","allfeeCount","recpayfee","fullfeeCount","fullfeeSum","deratefeeCount","deratefeeSum","partfeeCount","partfeeSum","owefeeCount","owefeeSum","fullfeePerStr","memo"};
			String[] filterColunms2 = {"firstyear","statType","allfeeCount","recpayfee","fullfeeCount","fullfeeSum","deratefeeCount","deratefeeSum","partfeeCount","partfeeSum","owefeeCount","owefeeSum","fullfeePerStr","memo"}; 
			List<String> filterColumnList = Arrays.asList(filterColunms1);//定义过滤列
			List<MergeCellsParam> mergeCellsParams = new ArrayList<MergeCellsParam>();
			
			String statType = "";
			String templateFileName = "studentFactFeeStat.xls";
			if("1".equals(currentIndex)) {
				templateMap.put("statTypeName", "专业");
				statType = "major";
				filterColumnList = Arrays.asList(filterColunms2);
			} else if("2".equals(currentIndex)) {
				templateMap.put("statTypeName", "层次");
				statType = "classic";
				filterColumnList = Arrays.asList(filterColunms2);
			} else if("3".equals(currentIndex)) {
				templateMap.put("statTypeName", "学习中心");
				statType = "brSchool";
				filterColumnList = Arrays.asList(filterColunms2);
			} else {
				templateFileName = "studentFactFeeStat1.xls";
				templateMap.put("statTypeName", "办学模式");
				statType = "teachingType";
				filterColumnList = Arrays.asList(filterColunms1);
			}
			//模板文件路径
			String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+templateFileName;
			list = studentPaymentService.statStudentPayment(condition, statType);
			if(ExCollectionUtils.isNotEmpty(list)){					
				getMergeCellsParam(list, mergeCellsParams, statType);//获取合并单元格
				if("teachingType".equals(statType)){
					//getMergeCellsParam(list, mergeCellsParams, "");//合并年度
				}
				calculateTotal(list);//加入总计
			}
			exportExcelService.initParamsByfile(disFile, "studentFactFeeStat", list, null, filterColumnList, mergeCellsParams);		
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
			disFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
			downloadFile(response, ("0".equals(year)?"":(year+"年"))+"缴费情况统计.xls", disFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出缴费情况统计失败:"+e.getMessage()+"\")</script>");
		}
	}
*/
	
	private void getMergeCellsParam(List<Map<String, Object>> list, List<MergeCellsParam> mergeCellsParams, String statType) {
		int start = 0;
		String tempStr = null;					
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> obj = list.get(i);
			String str = obj.get("firstyear").toString();
			if("teachingType".equals(statType)){
				str = obj.get("firstyear").toString()+(obj.get("statType")!=null?obj.get("statType").toString():"");
			}					
			if(!ExStringUtils.equals(tempStr, str) && tempStr!=null){
				if(i-start>1){					
					if("teachingType".equals(statType)){
						mergeCellsParams.add(new MergeCellsParam(1,start+4,1,i+4-1));
					} else {
						mergeCellsParams.add(new MergeCellsParam(0,start+4,0,i+4-1));
					}
				} 							
				start = i;
			}	
			if(i==list.size()-1 && i-start>0){				
				if("teachingType".equals(statType)){					
					mergeCellsParams.add(new MergeCellsParam(1,start+4,1,i+4));
				} else {
					mergeCellsParams.add(new MergeCellsParam(0,start+4,0,i+4));
				}
			}
			tempStr = str;
		}
	}
	
	/**
	 * 单独缴费
	 * @param resourceid
	 * @param deferEndDate
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/pay/edit.html")
	public void saveStudentPayEdit(HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "缴费成功！";
		String studentPaymentId = request.getParameter("studentPaymentId");
		String payAmount = request.getParameter("payAmount");
		String posSerialNumber = request.getParameter("posSerialNumber");
		String receiptNumber = request.getParameter("receiptNumber");
		String carrTermNum = request.getParameter("carrTermNum");
		String carrCardNo = request.getParameter("carrCardNo");
		String paymentMethod = request.getParameter("paymentMethod");
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			do{
				Double facepay = Double.parseDouble(payAmount);
				StudentPayment payment = studentPaymentService.get(studentPaymentId);
				if(payment == null){
					statusCode = 300;
					message = "所传参数有误！";
					continue;
				}
				PayDetailsVO payDetailsVO = new PayDetailsVO();
				payDetailsVO.setPayAmount(facepay);
				payDetailsVO.setPaymentMethod(paymentMethod);
				payDetailsVO.setPosSerialNumber(posSerialNumber);
				payDetailsVO.setCarrTermNum(carrTermNum);
				payDetailsVO.setCarrCardNo(carrCardNo);
				payDetailsVO.setReceiptNumber(receiptNumber);
				payDetailsVO.setChargingItems("tuition");
				Map<String, Object> returnMap = studentPaymentService.payFee(payment,payDetailsVO, user);
				if(returnMap!=null && returnMap.size() >0){
					statusCode = (Integer)returnMap.get("statusCode");
					if(statusCode==300){
						message = (String)returnMap.get("message");
					}
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.INSERT,"保存导入成绩：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				}
			} while(false);
		} catch (NumberFormatException e) {
			statusCode = 400;
			message = "请填写数字！";
			map.put("value", payAmount);
		}catch (Exception e) {
			logger.error("单独缴费出错:",e);
			statusCode = 300;
			message = "缴费失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	
	
	/**
	 * 导出学生缴费情况
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpaymentstu/liststu-export.html")
	public void exportStudentPaymentStu(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String, Object> condition = getStudentPaymentStu(request);
		List<Map<String,Object>> list = studentPaymentService.findStudentPaymentStu1ByContidion(condition);		
		List<StudentPaymentVo>  list1 = setStudentPaymentVo(list);
		try{
			//导出
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		 	GUIDUtils.init();
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//初始化配置参数
			List<String> dictCodeList = new ArrayList<String>();
			
			dictCodeList.add("CodeChargeStatus");
			exportExcelService.initParmasByfile(disFile, "studentPaymentVo", list1, dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE), null);		
			
			
			exportExcelService.getModelToExcel().setHeader("学生缴费情况");
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
				
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			String downloadFileName = "学生缴费情况.xls";
			String downloadFilePath = excelFile.getAbsolutePath();
			downloadFile(response, downloadFileName,downloadFilePath,true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出失败:"+e.getMessage()+"\")</script>");
		}
	}
	

	private Map<String, Object> getStudentPaymentStu(HttpServletRequest request) {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		String brSchool = request.getParameter("branchSchool");
		String gradeid = request.getParameter("grade");
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String chargeStatus = request.getParameter("chargeStatus");
		String studynos = ExStringUtils.trimToEmpty(request.getParameter("studynos"));
		if(ExStringUtils.isNotEmpty(brSchool)) {
			condition.put("brSchool", brSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		
		String qianfeipay = request.getParameter("qianfeipay");
		
		if(ExStringUtils.isNotEmpty(qianfeipay)) {
			condition.put("qianfeipay", Double.parseDouble(qianfeipay.toString()));
		}
		
		if(ExStringUtils.isNotEmpty(name)) {
			try {
				name=new String(name.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(chargeStatus)) {
			condition.put("chargeStatus", chargeStatus);
		}
//		String studynos1  = "";
		if(ExStringUtils.isNotEmpty(studynos)){
//			String[] ids = studynos.split(",");
//			for (int i = 0; i < ids.length; i++) {
//				if(i==ids.length-1){
//					studynos1+=ids[i];
//				}else{
//					studynos1+=ids[i]+",";
//				}
//			}
			condition.put("studynos", studynos);
		}
//		if(!"".equals(studynos1)){
//			condition.put("studynos", studynos);
//		}
		return condition;
	}
	
	private List<StudentPaymentVo> setStudentPaymentVo(
			List<Map<String, Object>> list) {
		List<StudentPaymentVo>  rlist = new ArrayList<StudentPaymentVo>();
		for(Map<String, Object> map : list){
			StudentPaymentVo vo = new StudentPaymentVo();
			vo.setStudyNo(map.get("StudyNo")!=null?map.get("StudyNo").toString():" ");
			vo.setName(map.get("name")!=null?map.get("name").toString():" ");
			vo.setGradeName(map.get("gradename")!=null?map.get("gradename").toString():" ");
			vo.setUnitName(map.get("unitname")!=null?map.get("unitname").toString():" ");
			vo.setRecpayFee(map.get("recpayfee")!=null?Double.parseDouble(map.get("recpayfee").toString()):0);
			vo.setDeratefee(map.get("deratefee")!=null?Double.parseDouble(map.get("deratefee").toString()):0);
			vo.setFacepayFee(map.get("facepayFee")!=null?Double.parseDouble(map.get("facepayFee").toString()):0);
			vo.setChargeStatus(map.get("chargeStatus")!=null?map.get("chargeStatus").toString():"");
			vo.setMobile(map.get("mobile")!=null?map.get("mobile").toString():" ");
			vo.setPays(map.get("pays")!=null?Double.parseDouble(map.get("pays").toString()):0);
			
			vo.setRecpayfeeall(map.get("recpayfeeall")!=null?Double.parseDouble(map.get("recpayfeeall").toString()):0);
			rlist.add(vo);
		}
		return rlist;
	}
	
	/**
	 * 批量缴费导入(new)
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/import-new.html")
	public void importStudentPaymentInfo(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		long st0 = System.currentTimeMillis();
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "导入学生缴费信息成功！";
		Page objPage = new Page();
		objPage.setOrder(Page.ASC);
//		request.getSession(true).setMaxInactiveInterval(60*90);
		try {			
			if( ExStringUtils.isNotBlank(attachId)){			
				User user = SpringSecurityHelper.getCurrentUser();
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				//处理数据字典字段
				importExcelService.initParmas(excel, "studentPaymentImportNew", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				List<StudentPaymentVo> modelList = importExcelService.getModelList();
				if(ExCollectionUtils.isNotEmpty(modelList)){
//					Map<String, Object> returnMap = studentPaymentService.handleStudentPaymentImport(modelList, user);
					Map<String, Object> returnMap = studentPaymentService.handleStudentPaymentImport_new(modelList, user);
					if(returnMap!=null &&returnMap.size()>0 ){
						statusCode = (Integer)returnMap.get("statusCode");
						if(statusCode==300) {
							message = (String)returnMap.get("message");
						}else{
							message+=(String)returnMap.get("message");
						}
						List<StudentPaymentVo> failList = new ArrayList<StudentPaymentVo>();
						if(returnMap.containsKey("failRecord")){
							File excelFile = null;
							String url= "/edu3/finance/studentpayment/studentPaymentImportFail.html";
							failList =(List<StudentPaymentVo>) returnMap.get("failRecord");
							setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
							//导出
							GUIDUtils.init();
							String fileName = GUIDUtils.buildMd5GUID(false);
							File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
							//模板文件路径
							String templateFilepathString = "studentPaymentImportFail.xls";
							//初始化配置参数
							exportExcelService.initParmasByfile(disFile,"studentPaymentImportFail", failList,null);
							exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, null);
//							exportExcelService.getModelToExcel().setRowHeight(400);
								
							excelFile = exportExcelService.getExcelFile();//获取导出的文件
							logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
							map.put("failDLUrl", url+"?excelFile="+fileName);
						}
					}
				} else {
					statusCode = 300;				
					message = "没有数据";
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.IMPORT,"导入缴费信息：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			} else {
				statusCode = 300;				
				message = "请上传附件.";
			}
		} catch (Exception e) {
			logger.error("导入出错:{}",e.fillInStackTrace());
			statusCode = 300;
			message = "导入学生缴费信息失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);			
			map.put("reloadUrl", request.getContextPath()+"/edu3/finance/studentpayment/list.html");
			map.put("navTabId", "RES_SCHOOL_REGISTER_ENROLL");
		}
		long et0 = System.currentTimeMillis();
		logger.info("此次导入缴费数据总共消耗时间："+(et0-st0)/1000f+" 秒");
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出学生缴费模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/studentpayment/paymentTemplate.html")
	public void downloadPayTemplate(HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			String templateFilepathString = "studentPaymentImport.xls";
			downloadFile(response, "学生缴费模板.xls", templateFilepathString,false);
		}catch(Exception e){
			logger.error("导出学生缴费模板出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出学生缴费模板失败\");</script>");
		}
	}
	/**
	 * 导出学生导入缴费失败记录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/studentpayment/studentPaymentImportFail.html")
	public void downloadStudentPaymentImportFail(String excelFile,HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
			downloadFile(response, "学生导入缴费失败记录.xls", disFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出学生导入缴费失败记录出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出学生导入缴费失败记录出错\");</script>");
		}
	}
	/**
	 * 学生收费管理页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/pos_payment_form.html")
	public String listStudentPaymentDetails(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer();
		String showType = "T";// T-显示TempStudentFee，P-显示StudentPayment，M-多条记录
		try {
			// 处理参数
			String studentNo = ExStringUtils.trimToEmpty(request.getParameter("studentNo"));
			String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
			String enrolleecode = ExStringUtils.trimToEmpty(request.getParameter("enrolleecode"));
			String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
			String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
			String uniqueValue = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			model.addAttribute("uniqueValue", uniqueValue);
			
			String branchSchoolId = null;
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0) {
				condition.put("branchSchoolId", cureentUser.getOrgUnit().getResourceid());//判断用户是否为校外学习中心
			}
			
			if(ExStringUtils.isNotEmpty(studentNo)||ExStringUtils.isNotEmpty(studentName)
					||ExStringUtils.isNotEmpty(enrolleecode)||ExStringUtils.isNotEmpty(examCertificateNo)||ExStringUtils.isNotEmpty(certNum)){
				message.append("200");
				if(ExStringUtils.isNotEmpty(studentNo)){
					condition.put("studyNo", studentNo);
				}
				if(ExStringUtils.isNotEmpty(studentName)){
					condition.put("name", studentName);
					condition.put("studentName", studentName);
				}
				if(ExStringUtils.isNotEmpty(enrolleecode)){
					condition.put("enrolleecode", enrolleecode);
				}
				if(ExStringUtils.isNotEmpty(examCertificateNo)){
					condition.put("examCertificateNo", examCertificateNo);
				}
				if(ExStringUtils.isNotEmpty(branchSchoolId)){
					condition.put("branchSchoolId", branchSchoolId);
				}
				if(ExStringUtils.isNotEmpty(certNum)){
					condition.put("certNum", certNum.toUpperCase());
				}
				// 判断该学生是否已经有学籍
				condition.put("studentInfoStatus", "'11','25'");
				List<StudentInfo> studentInfoList = studentInfoService.findByCondition(condition);
				List<StudentPayment> studentPaymentList = new ArrayList<StudentPayment>();
				List<StudentPaymentDetails> stuPaymentDetailList = new ArrayList<StudentPaymentDetails>();
				List<PayStudentInfoVO> psiList = new ArrayList<PayStudentInfoVO>();
				
				if(ExCollectionUtils.isEmpty(studentInfoList)){
					// 新生
					// 缴费注册信息
					String batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
					condition.put("hasStudentInfo", Constants.BOOLEAN_NO);
					condition.put("batchNo", batchNo);
					List<TempStudentFee> tempStudentFeeList = tempStudentFeeService.findListByContidion(condition);
					if(ExCollectionUtils.isEmpty(tempStudentFeeList)){
						message.setLength(0);
						message.append("不存在该学生或没有生成注册缴费记录<br/>");
					}else if(tempStudentFeeList.size()==1){
						TempStudentFee tempStudentFee = tempStudentFeeList.get(0);
						model.addAttribute("tempStudentFee", tempStudentFee);
					}else{
						PayStudentInfoVO psi = null;
						for(TempStudentFee tsf : tempStudentFeeList){
							psi = new PayStudentInfoVO(tsf.getStudentName(),null,tsf.getExamCertificateNo());
							psiList.add(psi);
						}
						model.addAttribute("psiList", psiList);
						showType = "M";
					}
				}else if(studentInfoList.size()==1){
					showType = "P";
					StudentInfo studentInfo = studentInfoList.get(0);
					condition.put("studentInfoId", studentInfo.getResourceid());
					
					studentPaymentList = studentPaymentService.findStudentPaymentByCondition(condition);
					if(ExCollectionUtils.isNotEmpty(studentPaymentList)){
						StudentPayment studentPayment = studentPaymentList.get(0);
						condition.put("studentInfoId", studentInfo.getResourceid());
						stuPaymentDetailList = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
//						model.addAttribute("schoolCode",CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
						model.addAttribute("studentPaymentInfo",studentPayment );
						model.addAttribute("studentPaymentDetailsList", stuPaymentDetailList);
					}else{
						message.setLength(0);
						message.append("该学生没有生成缴费记录<br/>");
					}
					
				}else {// 有多个学生
					message.setLength(0);
					message.append("存在多个学生，请细化查询条件！<br/>");
					PayStudentInfoVO psi = null;
					for(StudentInfo so : studentInfoList){
						psi = new PayStudentInfoVO(so.getStudentName(),so.getStudyNo(),so.getExamCertificateNo());
						psiList.add(psi);
					}
					model.addAttribute("psiList", psiList);
					showType = "M";
				}
				
				model.addAttribute("condition", condition);
			} 
		/*	String receiptPrefix = "";
			SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
			if(config != null){
				if(!ExStringUtils.trim("NULL").equals(config.getParamValue())){
					receiptPrefix = config.getParamValue();
				}
			}
			model.addAttribute("receiptPrefix", receiptPrefix);*/
			model.addAttribute("message", message);
			model.addAttribute("showType", showType);
			
		} catch (ServiceException e) {
			logger.error("学生收费管理页面出错", e);
		}
		
		return "/edu3/finance/studentpayment/pos_payment_form";
	}

	/**
	 * 验证缴费信息
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/validData.html")
	public void validPosData(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "验证通过！";
		String carrTradeSum = "";// 交易金额
		String money = request.getParameter("money");
		String stuPaymentId = request.getParameter("stuPaymentId");
		String showType = request.getParameter("showType");
		String tempStudentFeeId = request.getParameter("tempStudentFeeId");
//		String uniqueValue = request.getParameter("uniqueValue");
//		String examCertificateNo = request.getParameter("examCertificateNo");
//		String receiptNumber = request.getParameter("receiptNumber");
		try {
			do{
				SetTime payTime = setTimeService.getSetTimeByBusinessType("payTime");
				if(payTime==null){
					statusCode = 400;
					message = "请先设置缴费时间！";
					continue;
				}
				Date today = new Date();
				if(today.before(payTime.getBeginDate())){
					statusCode = 400;
					message = "还未到缴费时间，不能进行缴费操作！";
					continue;
				}
				if(today.after(payTime.getEndDate())){
					statusCode = 400;
					message = "缴费时间已结束，不能进行缴费操作！";
					continue;
				}
				//检查票据号是否重复
				/*Map<String, Object> condition = new HashMap<String, Object>();
				String receiptPrefix = "";
				SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
				if(config != null){
					if(!ExStringUtils.trim("NULL").equals(config.getParamValue())){
						receiptPrefix = config.getParamValue();
					}
				}*/
				// TODO:打印收据时再设置
				/*condition.put("receiptNumber", receiptPrefix+receiptNumber);
				List<StudentPaymentDetails> payList = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
				if(ExCollectionUtils.isNotEmpty(payList)){
					statusCode = 400;
					message = "该票据号已存在！";
					continue;
				}*/
				// 获取学生缴费情况
				StudentPayment studentPayment = null;
				// 教学点收费形式
				String payForm = "singlePremium";
				double payAmount = 0;
				if(ExStringUtils.isNotEmpty(showType) && "T".equals(showType)){// 首次缴费
					TempStudentFee tsf = tempStudentFeeService.get(tempStudentFeeId);
					if(tsf==null){
						statusCode = 400;
						message = "该学生的缴费信息不存在！";
						continue;
					}
					payAmount = tsf.getAmount();
					// 首次缴费（即缴费注册）一定要缴清第一年的学费，第二年开始可以分多次缴xx
					/*if(payAmount>Double.valueOf(money)){
						statusCode = 400;
						message = "请一次缴清所欠费的金额！";
						continue;
					}*/
				}else {
					studentPayment = studentPaymentService.get(stuPaymentId);
					if(studentPayment == null){
						statusCode = 400;
						message = "该学生的缴费信息不存在！";
						continue;
					}
					payForm = studentPayment.getStudentInfo().getBranchSchool().getPayForm();
					payAmount = new BigDecimal(studentPayment.getRecpayFee() - studentPayment.getFacepayFee()).setScale(2, RoundingMode.FLOOR).doubleValue();
				}
				
				if(ExStringUtils.isEmpty(money)){
					statusCode = 400;
					message = "缴费金额不能为空！";
					continue;
				}
				if(payAmount<Double.valueOf(money)){
					statusCode = 400;
					message = "输入金额大于应缴金额，请检查！";
					continue;
				}
				// 判断教学点收费形式xx
				if("singlePremium".equals(payForm)){// 一次缴清
					if(Double.valueOf(money).compareTo(payAmount)<0){
						statusCode = 400;
						message = "请一次缴清所欠费的金额:"+payAmount;
						continue;
					}
				}
				
				// 转换
				try {
					BigDecimal _money = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP);
					if(_money.compareTo(BigDecimal.ZERO)<=0){
						statusCode = 400;
						message = "缴费金额不能小于或等于0！";
						continue;
					}
					// 已经有学籍的学生才进行检查
					if("P".equals(showType)){
						Map<String, Object> validateMap = studentPaymentService.validateFacepay(_money.doubleValue(), studentPayment);
						statusCode = (Integer)validateMap.get("statusCode");
						if(statusCode==300){
							message = (String)validateMap.get("message");
							continue;
						}
					}
				
					carrTradeSum = ExStringUtils.suppleFront('0',_money.toPlainString().replace(".", ""), 12);
				} catch (Exception e) {
					statusCode = 400;
					message = "输入的金额格式不对！";
					continue;
				}
				String timestamp = String.valueOf(System.currentTimeMillis());
				ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
				String key = property.getProperty("TLKey");
				StringBuilder signInfo = new StringBuilder();
				signInfo.append(carrTradeSum).append(timestamp).append(key);
				map.put("timestamp", timestamp);
				map.put("sign", MD5CryptorUtils.getMD5ofStr(signInfo.toString()));
			} while(false);
		} catch (Exception e) {
			logger.error("验证缴费信息", e);
			statusCode = 300;
			message = "验证缴费信息失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("moneyStr", carrTradeSum);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 确认pos缴费
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/affirmPayByPos.html")
	public void affirmPayByPos(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "缴费成功！";
		String returnData = request.getParameter("returnData");// POS返回的数据
		String paymentId = request.getParameter("paymentId");
		String paymentMethod = request.getParameter("paymentMethod");// 付款方式
//		String receiptNumber = request.getParameter("receiptNumber");// 票据号
		String checkPayable = request.getParameter("checkPayable");// 票据抬头
		String memo = request.getParameter("memo");// 备注
		String moneyStr = request.getParameter("moneyStr");// 提交时候的金额
		String showType = request.getParameter("showType");// 处理类型
		String examCertificateNo = request.getParameter("examCertificateNo");// 长号（准考证号或考生号）
		String uniqueValue = request.getParameter("uniqueValue");// 唯一标识
		String tempStudentFeeId = request.getParameter("tempStudentFeeId");
		String timestamp = request.getParameter("timestamp");
		String sign = request.getParameter("sign");
		try {
			do{
				if(ExStringUtils.isEmpty(returnData)){
					logger.info("examCertificateNo="+examCertificateNo+",returnData="+returnData);
					statusCode = 300;
					message = "缴费失败！";
					continue;
				}
				// 验证数据的真实性
				ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
				String key = property.getProperty("TLKey");
				StringBuilder signInfo = new StringBuilder();
				signInfo.append(moneyStr).append(timestamp).append(key);
				if(ExStringUtils.isEmpty(sign) || !sign.equals(MD5CryptorUtils.getMD5ofStr(signInfo.toString()))){
					statusCode = 300;
					message = "签名无效！";
					continue;
				}
				
				/*
				 * 建设银行
				 * String[] _returnData = returnData.split("_");
				String carrRsp = _returnData[1];
				
				if(POS_TRADE_RSPONSE_ABNORMITY.equals(carrRsp)) {
					statusCode = 300;
					message = "交易失败，请检查串口接线是否松动或是否已经正确取消了交易！";	
					continue;
				} else if(!POS_TRADE_RSPONSE_SUCCESS.equals(carrRsp)) {
					statusCode = 300;
					message = "缴费失败！";	
					continue;
				}*/
				// 光大银行
				// 响应码
				String carrRsp = returnData.substring(12, 14);
				if(!POS_TRADE_RSPONSE_SUCCESS.equals(carrRsp)) {
					logger.info("examCertificateNo="+examCertificateNo+",carrRsp="+carrRsp);
					statusCode = 300;
					message = "缴费失败！";	
					continue;
				}
				
				StudentPayment studentPayment = null;
				TempStudentFee tsf = null;
				if("P".equals(showType)){// 已经存在学籍的学生缴费
					// 获取学生缴费情况
					studentPayment = studentPaymentService.get(paymentId);
					if(studentPayment == null){
						statusCode = 400;
						message = "该学生的缴费信息不存在！";
						continue;
					}
					String batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
					List<TempStudentFee>  tsfList = tempStudentFeeService.findByHql("from "+TempStudentFee.class.getSimpleName()+" tsf where tsf.isDeleted=0 and tsf.studentInfo.resourceid=? and tsf.payStatus='1'  and tsf.batchNo=?" , 
							studentPayment.getStudentInfo().getResourceid(),batchNo);
					if(ExCollectionUtils.isNotEmpty(tsfList)){
						tsf = tsfList.get(0);
					}
				
				}else {
//					TempStudentFee tsf = tempStudentFeeService.findUniqueByProperty("examCertificateNo", examCertificateNo);
					tsf = tempStudentFeeService.get(tempStudentFeeId);
				}
				
				if(tsf == null){
					statusCode = 400;
					message = "该学生的缴费信息不存在！";
					continue;
				}
				// POS支付成功处理的逻辑
				
				/*建设银行
				 * String carrActualSum = _returnData[9];// 实际扣款金额
//					System.out.println(carrActualSum);
				String carrTermNum = _returnData[2];// 终端号
				String carrTermSN = _returnData[4];// 终端流水号
				String carrCardNo = _returnData[6];// 银行卡号
*/				
				// 光大银行
				// 银行代码和银行名称
//				String bankName = returnData.substring(67, 75);
				String carrActualSum = returnData.substring(75, 87);// 实际扣款金额
				String carrTermNum = returnData.substring(14, 22);// 终端号
				String carrTermSN = returnData.substring(37, 43);// 终端流水号
				String carrCardNo = returnData.substring(47, 67);// 银行卡号
				if(ExStringUtils.isEmpty(carrActualSum)){
					statusCode = 400;
					message = "金额不能为空！";
					continue;
				}
				Double _actualSum= transformStrToDouble(carrActualSum);
				Double _money = transformStrToDouble(moneyStr);
				if(_actualSum != _money){
					logger.info("实际扣款返回的金额："+carrActualSum+"页面输入的金额："+moneyStr);
					_actualSum = _money;
				}
				// 2017-12-19 收款人统一
//				User user = SpringSecurityHelper.getCurrentUser();
				User user =  userService.getUserByLoginId("hnjk");
				// 处理缴费信息
				// 收据号
			/*	String receiptPrefix = "";
				SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
				if(config != null){
					if(!ExStringUtils.trim("NULL").equals(config.getParamValue())){
						receiptPrefix = config.getParamValue();
					}
				}*/
				PayDetailsVO payDetailsVO = new PayDetailsVO();
				payDetailsVO.setPayAmount(_actualSum);
				payDetailsVO.setPaymentMethod(paymentMethod);
				payDetailsVO.setPosSerialNumber(carrTermSN);
				payDetailsVO.setCarrTermNum(carrTermNum);
				payDetailsVO.setCarrCardNo(carrCardNo);
//				payDetailsVO.setReceiptNumber(receiptPrefix+receiptNumber);
				payDetailsVO.setCheckPayable(checkPayable);
				payDetailsVO.setMemo(memo);
				// TODO 以后如果有多个学校使用做成全局参数或数据字典
				payDetailsVO.setChargeMoney(0d);
				// 终端号加准考证号
				payDetailsVO.setEduOrederNo(carrTermNum+tsf.getExamCertificateNo());
				// 收费项
				payDetailsVO.setChargingItems(tsf.getChargingItems());
				// 是否开单位发票
				payDetailsVO.setIsInvoicing(tsf.getIsInvoicing());
				// 单位名称
				payDetailsVO.setInvoiceTitle(tsf.getInvoiceTitle());
				
				Map<String, Object> returnMap = null;
				if("P".equals(showType)){// POS机已有学籍缴费
					returnMap = studentPaymentService.payFee(studentPayment, payDetailsVO, user);
				} else {// POS机首次缴费注册
					returnMap = studentPaymentService.payOnline(examCertificateNo,Integer.valueOf(uniqueValue), user,payDetailsVO,tsf);
				}
				
				if(returnMap!=null && returnMap.size() >0){
					statusCode = (Integer)returnMap.get("statusCode");
					if(statusCode==300){
						message = (String)returnMap.get("message");
					}else if(statusCode==200){
//							request.getSession().setAttribute("_receiptNumber", Integer.valueOf(receiptNumber)+1);
						message = "缴费成功,实际缴费金额为"+_actualSum+"！";
						// 是一次缴清的
						tsf.setIsReconciliation(Constants.BOOLEAN_YES);
						tsf.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
						tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_NONEED);
						tsf.setEduOrderNo(payDetailsVO.getEduOrederNo());
						tempStudentFeeService.saveOrUpdate(tsf);
						
						/*	if(tsf.getAmount().compareTo(_actualSum)==0){//已缴清
							//缴费成功后申请撤销学生注册缴费的第三方记录
							
							 * 这是通联支付的逻辑先撤销
							 * try{
								if(tsf!=null){
									Date today = new Date();
									String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
									// 接收异步通知接口
									String receiveUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/order/deleteResultSecond.html";
									HeadVO headVO = new HeadVO(Constants.EDU_ORDER_INFO_CLOSE,dateTime,receiveUrl);
									int faileSize = 0;// 失败数量
									StringBuffer msg = new StringBuffer();
									List<TempStudentFee> tempList = new ArrayList<TempStudentFee>();
									String _msg = null;
									tempList.add(tsf);
									Document returnDoc =TonlyPayUtil.cancelOrder(headVO, tempList);
									msg = getReturnInfo(returnDoc, msg,faileSize);
									_msg = msg.toString();
									int sizeIndex = _msg.lastIndexOf("-");
									faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
									msg.setLength(0);
									msg.append(_msg.substring(0, sizeIndex));
								}
							}catch(Exception e){
								logger.error("申请撤销订单信息第三方接口出错" + e);
							}
							
							tsf.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
							tempStudentFeeService.saveOrUpdate(tsf);
						}*/
						
						// 打印
//							StudentPaymentDetails studentPaymentDetails = (StudentPaymentDetails)returnMap.get("studentPaymentDetails");
//							PayDetailsVO printPayDetailsVO = createPrintPayDetailsVO(studentPaymentDetails);
//							map.put("paymentDetail", printPayDetailsVO);
					}
				}
				
			} while(false);
		} catch (Exception e) {
			logger.error("确认pos缴费", e);
			statusCode = 300;
			message = "缴费失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			logger.info(paymentId+message);
//			map.put("dialogUrl",  request.getContextPath()+"/edu3/finance/studentpayment/pos_payment_form.html?stuPaymentId="+paymentId);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 将字符串转换为Double(按照一定的规则)
	 * @param data
	 * @return
	 * 例子：000000001005 -> 10.05
	 */
	private Double transformStrToDouble(String data){
		Double returnData = 0d;
		if(ExStringUtils.isNotEmpty(data)){
			String _decimal = data.substring(data.length()-2);
    		String _integer = data.substring(0, data.length()-2);
    		returnData = Double.valueOf(Integer.parseInt(_integer)+"."+_decimal);
		}
		
		return returnData;
	}
	
	/**
	 * 教学站收费形式列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/unitPayForm-list.html")
	public String listUnitPayForm(HttpServletRequest request, HttpServletResponse response, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy(" unitCode ");
		objPage.setOrder(Page.ASC);
		Map<String, Object> condition = new 	HashMap<String, Object>();
		String unitName = request.getParameter("unitName");
		String payForm = request.getParameter("payForm");

		try {
			if(ExStringUtils.isNotEmpty(unitName)){
				condition.put("unitName", unitName);
			}
			if(ExStringUtils.isNotEmpty(payForm)){
				condition.put("payForm", payForm);
			}
			condition.put("unitType", "brSchool");// 
			
			Page unitPayFormPage = orgUnitService.findOrgByConditionByHql(condition, objPage);
			model.addAttribute("schoolCode",CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
			model.addAttribute("batchNo", CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue());
			model.addAttribute("projectNo", CacheAppManager.getSysConfigurationByCode("payment_projectNo").getParamValue());
			model.addAttribute("condition", condition);
			model.addAttribute("unitPayFormList", unitPayFormPage);
		} catch (ServiceException e) {
			logger.error("教学站收费形式列表出错", e);
		}
		
		return "/edu3/finance/studentpayment/unitPayForm-list";
	}
	
	/**
	 * 选择教学点收费形式
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/unitPayForm-select.html")
	public String selectUnitPayForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String unitIds = request.getParameter("unitIds");
		model.addAttribute("unitIds", unitIds);
		
		return "/edu3/finance/studentpayment/select-unitPayForm";
	}
	
	/**
	 * 设置教学点收费形式
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setUnitPayForm.html")
	public void setUnitPayForm(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String unitIds = request.getParameter("unitIds");
		String unitPayForm = request.getParameter("unitPayForm");
		int returnCode = 200;
		String message = "设置教学点收费形式成功";
		try {
			do {
				if (ExStringUtils.isEmpty(unitIds)) {
					returnCode = 300;
					message = "请重新选择教学点记录！";
					continue;
				}
				if (ExStringUtils.isEmpty(unitPayForm)) {
					returnCode = 300;
					message = "请重新教学点收费形式！";
					continue;
				}
				// 设置教学点收费形式逻辑
				orgUnitService.setUnitPayForm(unitPayForm, unitIds);
				
			} while (false);
		} catch (Exception e) {
			logger.error("设置教学点收费形式出错", e);
			returnCode = 300;
			message = "设置教学点收费形式失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 跳转到设置收据号前缀页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setReceiptNOPrefixForm.html")
	public String setReceiptNOPrefixForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String receiptPrefix = "";
		SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
		if(config != null){
			receiptPrefix = config.getParamValue();
		}
		
		model.addAttribute("receiptPrefix", receiptPrefix);
		return "/edu3/finance/studentpayment/setReceiptNOPrefixForm";
	}
	
	/**
	 * 设置收据号前缀
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setReceiptNOPrefix.html")
	public void setReceiptNOPrefix(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String receiptPrefix = request.getParameter("receiptPrefix");
		int returnCode = 200;
		String message = "设置收据号前缀成功";
		try {
			do {
				if (ExStringUtils.isEmpty(receiptPrefix)) {
					returnCode = 300;
					message = "请填写收据号前缀！";
					continue;
				}
				// 设置收据号前缀
				SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
				if(config == null){
					returnCode = 300;
					message = "请联系管理员创建收据号前缀全局参数！";
				}else {
					config.setParamValue(ExStringUtils.trim(receiptPrefix));
					sysConfigurationService.updateSysConfiguration(config);
				}
			} while (false);
		} catch (Exception e) {
			logger.error("设置收据号前缀出错", e);
			returnCode = 300;
			message = "设置收据号前缀失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 预览收据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/previewReceipt.html")
	public String previewReceipt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String paymentDetailId = request.getParameter("resourceid");
		StudentPaymentDetails paymentDetail = studentPaymentDetailsService.get(paymentDetailId);
		if(paymentDetail != null){
			paymentDetail.setPrintPayAmount(new BigDecimal(paymentDetail.getPayAmount()).setScale(2,RoundingMode.HALF_UP));
		}
		model.addAttribute("paymentDetail", paymentDetail);
		
		return "/edu3/finance/studentpayment/previewReceipt";
	}
	
	/**
	 * 打印收据
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/printReceipt.html")
	public void printReceipt(HttpServletRequest request, HttpServletResponse response) throws WebException {
		int statusCode = 200;
		String mesage = "";
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			do {
				String paymentDetailId = request.getParameter("paymentDetailId");
				StudentPaymentDetails paymentDetail = studentPaymentDetailsService.get(paymentDetailId);
				if (paymentDetail == null) {
					statusCode =  300;
					mesage = "没有数据！";
					continue;
				}
				PayDetailsVO payDetailsVO = createPrintPayDetailsVO(paymentDetail);
				map.put("paymentDetail", payDetailsVO);
			} while (false);
		} catch (Exception e) {
			logger.error("打印收据出错", e);
			statusCode =  300;
			mesage = "打印收据失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("mesage", mesage);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 创建打印缴费VO
	 * @param paymentDetail
	 * @return
	 */
	private PayDetailsVO createPrintPayDetailsVO(StudentPaymentDetails paymentDetail) {
		PayDetailsVO payDetailsVO = new PayDetailsVO();
//		payDetailsVO.setClassName(paymentDetail.getStudentInfo().getClasses().getClassname());
		payDetailsVO.setClassName(paymentDetail.getClassName()!=null?paymentDetail.getClassName():"");
		payDetailsVO.setPrintPayMethod("付款方式："+JstlCustomFunction.dictionaryCode2Value("CodePaymentMethod", paymentDetail.getPaymentMethod()));
		payDetailsVO.setPrintYear(JstlCustFunction.getTimeByPattern(paymentDetail.getOperateDate(), "yyyy"));
		payDetailsVO.setPrintMonth(JstlCustFunction.getTimeByPattern(paymentDetail.getOperateDate(), "MM"));
		payDetailsVO.setPrintDay(JstlCustFunction.getTimeByPattern(paymentDetail.getOperateDate(), "dd"));
		payDetailsVO.setPrintUpperMoney(JstlCustFunction.getCN(new BigDecimal(paymentDetail.getPayAmount())));
		payDetailsVO.setPrintPayAmount((new BigDecimal(paymentDetail.getPayAmount()).setScale(2,RoundingMode.HALF_UP)).toString());
		payDetailsVO.setCheckPayable(paymentDetail.getCheckPayable());
		payDetailsVO.setOperatorName(paymentDetail.getOperatorName());
		return payDetailsVO;
	}
	
	/**
	 * 跳转到设置分成比例页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setRoyaltyRateForm.html")
	public String setRoyaltyRateForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		model.addAttribute("unitIds", request.getParameter("unitIds"));
		model.addAttribute("schoolCode",CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		return "/edu3/finance/studentpayment/setRoyaltyRateForm";
	}
	
	/**
	 * 设置分成比例
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setRoyaltyRate.html")
	public void setRoyaltyRate(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String royaltyRate = request.getParameter("royaltyRate");
		String royaltyRate2 = request.getParameter("royaltyRate2");
		String reserveRatio = request.getParameter("reserveRatio");
		String unitIds = request.getParameter("unitIds");
		int returnCode = 200;
		String message = "设置分成比例成功";
		//String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		try {
			do {
				if (ExStringUtils.isBlank(royaltyRate,royaltyRate2,reserveRatio)) {
					returnCode = 300;
					message = "请填写分成比例！";
					continue;
				}
				BigDecimal royaltyRateBG = new BigDecimal(royaltyRate).setScale(14,RoundingMode.HALF_UP);
				double royaltyRate_db = royaltyRateBG.doubleValue();
				BigDecimal royaltyRate2BG = new BigDecimal(royaltyRate2).setScale(14,RoundingMode.HALF_UP);
				double royaltyRate2_db = royaltyRate2BG.doubleValue();
				BigDecimal reserveRatioBG = new BigDecimal(reserveRatio).setScale(14,RoundingMode.HALF_UP);
				double reserveRatio_db = reserveRatioBG.doubleValue();
				if(0 > royaltyRate_db && royaltyRate_db > 100 || 0 > royaltyRate2_db && royaltyRate2_db > 100 || 0 > reserveRatio_db && reserveRatio_db > 100){
					returnCode = 300;
					message = "分成比例请填写0到100的数字！";
					continue;
				}
				
				orgUnitService.setRoyaltyRate(royaltyRateBG,royaltyRate2BG,reserveRatioBG, unitIds);
			} while (false);
		} catch (Exception e) {
			logger.error("设置分成比例出错", e);
			returnCode = 300;
			message = "设置分成比例失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 通联支付学生缴费接口（对外接口，获取缴费返回的结果）
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/pay/result.html",method=RequestMethod.POST)
	public void receivePayResult(HttpServletRequest request, HttpServletResponse response){
		String statusCode ="OK";
		String message = "";
		Document returnDoc = DocumentHelper.createDocument();
		Element root = returnDoc.addElement("RESULT");
		try {
			do {
				String data = request.getParameter("data");
				if (ExStringUtils.isEmpty(data)) {
					statusCode = "FAIL";
					message = "参数为空";
					continue;
				}
				Document resultDoc = XMLUtils.parseText(data);
				 Element body = XMLUtils.getChild(resultDoc.getRootElement(), "BODY");
				 Element dataList = XMLUtils.getChild(body, "DATALIST");
				 if(dataList != null){
					 //支付状态
					 String payResult = XMLUtils.getChild(dataList, "PAY_RESULT").getText();
					 if(!Constants.RETURN_STATUS_SUCESS.equals(payResult)){
						 statusCode = "FAIL";
						 message = "缴费失败";
						 continue;
					 }
					 // 考生号或准考证号，（长号）
					/* 2017-02-20 修改订单处理结果接口
					 * String examcertificateno = XMLUtils.getChild(dataList, "STUDENT_ID").getText();
					 if(ExStringUtils.isEmpty(examcertificateno)){
						 statusCode = "FAIL";
						 message = "学号为空";
						 continue;
					 }*/
					 // 支付流水号
					 String payNo = XMLUtils.getChild(dataList, "PAY_NO").getText();
					 if(ExStringUtils.isEmpty(payNo)){
						 statusCode = "FAIL";
						 message = "支付流水号为空";
						 continue;
					 }
					 // 支付时间
					 String payTime = XMLUtils.getChild(dataList, "PAY_TIME").getText();
					 if(ExStringUtils.isEmpty(payTime)){
						 statusCode = "FAIL";
						 message = "支付时间为空";
						 continue;
					 }
				     Date _payTime = ExDateUtils.convertToDateTimeCombine(payTime);
					 /**
					  * 支付类型
					  * 01-代收，09-网银支付，10-POS支付，11-现金支付，
					  * 12-移动支付，13-外卡支付，14-微信支付，15-通联钱包
					  */
					 String payType = XMLUtils.getChild(dataList, "PAY_TYPE").getText();
					 if(ExStringUtils.isEmpty(payType)){
						 statusCode = "FAIL";
						 message = "支付类型为空";
						 continue;
					 }
					 
					 // 支付金额
					 String amount = XMLUtils.getChild(dataList, "AMT").getText();
					 if(ExStringUtils.isEmpty(amount)){
						 statusCode = "FAIL";
						 message = "金额为空";
						 continue;
					 }
					 BigDecimal payAmount = (new BigDecimal(amount).divide(BigDecimal.valueOf(100))).setScale(2);
					 if(BigDecimal.ZERO.equals(payAmount)){
						 statusCode = "FAIL";
						 message = "金额为0";
						 continue;
					 }
					 // 教育系统订单号EDU_ORDER_NO
					 String eduOrderNo = XMLUtils.getChild(dataList, "EDU_OREDER_NO").getText();
					 if(ExStringUtils.isEmpty(eduOrderNo)){
						 statusCode = "FAIL";
						 message = "教育系统订单号为空";
						 continue;
					 }
					 // 判断该订单是否已推送过，有则不作处理
					 TempStudentFee tempStudentFee = tempStudentFeeService.findUniqueByProperty("eduOrderNo", eduOrderNo);
					 if(tempStudentFee==null  || (tempStudentFee!=null && Constants.FEE_PAYSTATUS_PAYED.equals(tempStudentFee.getPayStatus()))){
						 continue;
					 }
					 String examcertificateno = tempStudentFee.getExamCertificateNo();
					 // 处理注册并生成缴费信息
					 // 获取唯一标识
					 SysConfiguration config = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId");
					 int uniqueId = 0;
					 if(config!=null &&  "1".equals(config.getParamValue())){
						 uniqueId = 1;
					 }
					 // 网上缴费逻辑
					/* User user = SpringSecurityHelper.getCurrentUser();
					 if(user==null){
						 user = userService.getUserByLoginId("hnjk");
					 }*/
					User user =  userService.getUserByLoginId("hnjk");
					PayDetailsVO payDetailsVO = new PayDetailsVO();
					payDetailsVO.setPayAmount(payAmount.doubleValue());
					payDetailsVO.setEduOrederNo(eduOrderNo);
//					2017-02-20 修改订单处理结果接口
					payDetailsVO.setPaymentMethod(TonlyPayUtil.convertPayMethod(payType));
					payDetailsVO.setPayNo(payNo);
					payDetailsVO.setPayTime(_payTime);
					// TODO:	到时由上面替换
//					payDetailsVO.setPaymentMethod(TonlyPayUtil.convertPayMethod("09"));
					// 收费项
					payDetailsVO.setChargingItems(tempStudentFee.getChargingItems());
					// 是否开单位发票
					payDetailsVO.setIsInvoicing(tempStudentFee.getIsInvoicing());
					// 单位名称
					payDetailsVO.setInvoiceTitle(tempStudentFee.getInvoiceTitle());
					
					synchronized (tempStudentFee) {
						Map<String, Object> resultMap = studentPaymentService.payOnline(examcertificateno, uniqueId,user, payDetailsVO,tempStudentFee);
						if((Integer)resultMap.get("statusCode")==300){
							statusCode = "FAIL";
							message = (String)resultMap.get("message");
//							logger.info(examcertificateno+" : "+message);
							logger.info(eduOrderNo+" : "+message);
						} else {
							tempStudentFee.setHandleStatus(TempStudentFee.HANDLESTATUS_NONEED);
							tempStudentFeeService.saveOrUpdate(tempStudentFee);
						}
					}
				 }
			} while (false);
		} catch(DocumentException e){
			logger.error("支付学生缴费接口出错", e);
			statusCode = "FAIL";
			message = "报文解析错误";
		}catch (Exception e) {
			logger.error("支付学生缴费接口出错", e);
			statusCode = "FAIL";
			message = "系统出错";
		} finally {
			XMLUtils.addElement(root, "CODE", statusCode);
			XMLUtils.addElement(root, "MSG", message);
		}
		renderXml(response, returnDoc.asXML());
	}
	
	/**
	 * 生成缴费临时记录（供同步到第三方系统使用）
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/createPayTempStuFee.html")
	public void createPayTempStuFee(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int returnCode = 200;
		String message = "成功生成";
		try {
			do {
				Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
				String brSchool = request.getParameter("brSchool");
				String majorId = request.getParameter("majorId");
				String classicId = request.getParameter("classicId");
				String gradeId = request.getParameter("gradeId");
				String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
				String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
				String chargeStatus = request.getParameter("chargeStatus");
				String classesId = request.getParameter("classesId");
				String studentStatus = request.getParameter("studentStatus");
				
				if(ExStringUtils.isNotEmpty(brSchool)){
					condition.put("brSchool", brSchool);
				}
				if(ExStringUtils.isNotEmpty(majorId)){
					condition.put("majorid", majorId);
				}
				if(ExStringUtils.isNotEmpty(classicId)){
					condition.put("classicid", classicId);
				}
				if(ExStringUtils.isNotEmpty(gradeId)){
					condition.put("gradeid", gradeId);
				}
				if(ExStringUtils.isNotEmpty(name)){
					condition.put("name", name);
				}
				if(ExStringUtils.isNotEmpty(studyNo)){
					condition.put("studyNo", studyNo);
				}
				if(ExStringUtils.isNotEmpty(chargeStatus)){
					condition.put("chargeStatus", chargeStatus);
				}
				if(ExStringUtils.isNotEmpty(classesId)){
					condition.put("classesId", classesId);
				}
				if(ExStringUtils.isNotEmpty(studentStatus)){
					condition.put("studentStatus", studentStatus);
				}
				// 只为正常注册和缓毕业的学生生成
//				condition.put("studentStatus", "11");
				condition.put("studentStatus_in", "11','25");
				List<StudentPayment> paymentList = studentPaymentService.findStudentPaymentByCondition(condition);
				Map<String, Object> returnMap = tempStudentFeeService.createPayTempStuFee(paymentList);
				returnCode = (Integer)returnMap.get("statusCode");
				if(returnCode==300){
					message = (String)returnMap.get("message");
				}
			} while (false);
		} catch (Exception e) {
			logger.error("生成缴费临时记录出错", e);
			returnCode = 300;
			message = "生成缴费临时记录失败";
		} finally {
			map.put("statusCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 跳转到设置学校内部批次编号页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setBatchNoForm.html")
	public String setBarchNoForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		return "/edu3/finance/studentpayment/setBatchNoForm";
	}
	
	/**
	 * 设置学校内部批次编号
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setBatchNo.html")
	public void setBarchNo(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String batchNo = request.getParameter("batchNo");
		int returnCode = 200;
		String message = "设置学校内部批次编号成功";
		try {
			do {
				if (ExStringUtils.isEmpty(batchNo)) {
					returnCode = 300;
					message = "设置学校内部批次编号编号！";
					continue;
				}
				SysConfiguration no = CacheAppManager.getSysConfigurationByCode("payment_batchNo");
				no.setParamValue(batchNo);
				sysConfigurationService.update(no);
			} while (false);
		} catch (Exception e) {
			logger.error("设置学校内部批次编号出错", e);
			returnCode = 300;
			message = "设置学校内部批次编号失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 跳转到设置缴费项目编号页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setProjectNoForm.html")
	public String setProjectNoForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		return "/edu3/finance/studentpayment/setProjectNoForm";
	}
	
	/**
	 * 设置缴费项目编号
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/setProjectNo.html")
	public void setProjectNo(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String projectNo = request.getParameter("projectNo");
		int returnCode = 200;
		String message = "设置缴费项目编号成功";
		try {
			do {
				if (ExStringUtils.isEmpty(projectNo)) {
					returnCode = 300;
					message = "请填写缴费项目编号！";
					continue;
				}
				SysConfiguration no = CacheAppManager.getSysConfigurationByCode("payment_projectNo");
				no.setParamValue(projectNo);	
				sysConfigurationService.update(no);
			} while (false);
		} catch (Exception e) {
			logger.error("设置缴费项目编号出错", e);
			returnCode = 300;
			message = "设置缴费项目编号失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/*
	 * 第三方接口的同步返回
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
	 * 批量缴费导入(右江医)
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/import-new1.html")
	public void importStudentPaymentInfo1(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "导入学生缴费信息完成";
		int successNum; //成功条数
		int falseNum;//失败条数
		int countNum;//总条数
		String upLoadurl = "/edu3/finance/studentpayment/import-new1-false.html";
		List<StudentPaymentVo> falseList=new ArrayList<StudentPaymentVo>(); //错误的学生信息
		Page objPage = new Page();
		objPage.setOrder(Page.ASC);
		try {			
			if( ExStringUtils.isNotBlank(attachId)){			
				User user = SpringSecurityHelper.getCurrentUser();
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				//处理数据字典字段
				importExcelService.initParmas(excel, "studentPaymentImportNew1", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				List<StudentPaymentVo> modelList = importExcelService.getModelList();
				if(ExCollectionUtils.isNotEmpty(modelList)){
					countNum=modelList.size();
					Map<String, Object> returnMap = studentPaymentService.handleStudentPaymentImport1(modelList, user);
					if(returnMap!=null &&returnMap.size()>0 ){
						statusCode = (Integer)returnMap.get("statusCode");
						if(statusCode==300) {
							message = (String)returnMap.get("message");
						}else{
							
							falseList=(List<StudentPaymentVo>) returnMap.get("falseList");
							
							//缴费信息导入失败的信息以及原因
							if(falseList!=null&&falseList.size()>0){
								falseNum=falseList.size();
								setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
//								exportExcelService.getModelToExcel().setRowHeight(400);
								
								String fileName = GUIDUtils.buildMd5GUID(false);
								File disFile 	= new File(getDistfilepath()+ File.separator + fileName + ".xls");
								
								List<String> dictCodeList = new ArrayList<String>();
								dictCodeList.add("CodeSex");
								dictCodeList.add("CodeNation");
								dictCodeList.add("CodeStudentStatus");
								Map<String , Object> map1 = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
								
	//							CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
								
								exportExcelService.initParmasByfile(disFile, "studentPaymentImportNew1Error", falseList ,map1);
								exportExcelService.getModelToExcel().setHeader("缴费信息表");//设置大标题
								exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
								WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
								font.setBoldStyle(WritableFont.BOLD);
						        WritableCellFormat format = new WritableCellFormat(font);
								format.setAlignment(Alignment.CENTRE);
						        format.setBackground(Colour.YELLOW);
						        format.setBorder(Border.ALL, BorderLineStyle.THIN);
								exportExcelService.getModelToExcel().setTitleCellFormat(format);
								exportExcelService.getModelToExcel().setRowHeight(300);
								File excelFile = exportExcelService.getExcelFile();//获取导出的文件
								
								logger.info("获取导出分班失败记录的excel文件:{}",excelFile.getAbsoluteFile());
								
	//							downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
								message = "导入成功"+ (countNum-falseNum)
								+"条 | 导入失败"+ (falseNum)
								+"条 ";
								map.put("errorMessageList", true);
								map.put("forwardUrl", upLoadurl+"?excelFile="+fileName);
								
							}else{
								message = "导入成功"+ (countNum)+"条";
							}
						}
					}
				} else {
					statusCode = 300;				
					message = "没有数据";
				}
			} else {
				statusCode = 300;				
				message = "请上传附件.";
			}
		} catch (Exception e) {
			logger.error("导入出错:{}",e.fillInStackTrace());
			statusCode = 300;
			message = "导入学生缴费信息失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("reloadUrl", request.getContextPath()+"/edu3/finance/studentpayment/list.html");
			map.put("navTabId", "RES_SCHOOL_REGISTER_ENROLL");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导入学生缴费失败信息
	 * @param year
	 * @param type
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/import-new1-false.html")
	public void uploadFalseToImport(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入学生缴费信息失败表.xls", disFile.getAbsolutePath(),false);
	}
	
	
	
	
	@RequestMapping("/edu3/finance/studentPayment/studentPayment-refund-printListView.html")
	public String printStudentPaymentDetailsView(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			//condition = getReturnPremiumCondition(request);
			if(condition.containsKey("drawer")){
				condition.put("drawer", URLEncoder.encode((String)condition.get("drawer"), "UTF-8"));
			}
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("打印学生退费明细", e);
		} 
		
		return "/edu3/finance/studentpayment/studentpayment-refund-printview";
	}
	
	
	/**
	 * 创建打印缴费VO
	 * @param paymentDetail
	 * @return
	 */
	private List<PayDetailsVO> createPrintPayDetailsVO(List<ReturnPremium> returnPremiums) {
		List<PayDetailsVO> payDetailsVOs = new ArrayList<PayDetailsVO>();
		for(ReturnPremium returnPremium : returnPremiums) {
			PayDetailsVO payDetailsVO = new PayDetailsVO();
//			payDetailsVO.setClassName(returnPremium.getStudentInfo().getClasses()!=null?returnPremium.getStudentInfo().getClasses().getClassname():"");
			payDetailsVO.setClassName(returnPremium.getClassName()!=null?returnPremium.getClassName():"");
			payDetailsVO.setPrintPayMethod("付款方式："+JstlCustomFunction.dictionaryCode2Value("CodePaymentMethod", returnPremium.getPaymentMethod()));
			payDetailsVO.setPrintYear(JstlCustFunction.getTimeByPattern(returnPremium.getCreateDate(), "yyyy"));
			payDetailsVO.setPrintMonth(JstlCustFunction.getTimeByPattern(returnPremium.getCreateDate(), "MM"));
			payDetailsVO.setPrintDay(JstlCustFunction.getTimeByPattern(returnPremium.getCreateDate(), "dd"));
			payDetailsVO.setPrintUpperMoney(JstlCustFunction.getCN(new BigDecimal(returnPremium.getReturnPremiumFee())));
			payDetailsVO.setPrintPayAmount((new BigDecimal(returnPremium.getReturnPremiumFee()*-1).setScale(2,RoundingMode.HALF_UP)).toString());
			payDetailsVO.setCheckPayable(returnPremium.getStudentInfo().getStudentName()+"("+returnPremium.getStudyNo()+")");
			
			payDetailsVO.setYear(String.valueOf(returnPremium.getYearInfo().getFirstYear()));
			payDetailsVO.setOperatorName(returnPremium.getOperatorName());
			payDetailsVO.setBrSchool(returnPremium.getStudentInfo().getBranchSchool().getUnitName());
			payDetailsVOs.add(payDetailsVO);
		}
		return payDetailsVOs;
	}
	
	/**
	 * 获取对账单（通过通联接口）
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/pay/statement.html")
	public void getStatement(HttpServletRequest request, HttpServletResponse response){
//		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		String tradeDate = request.getParameter("tradeDate");// 交易日期
		try{
			do{
				if(ExStringUtils.isEmpty(tradeDate)){
					statusCode = 300;
					message = "交易日期不能空";
					continue;
				}
				String merNo = CacheAppManager.getSysConfigurationByCode("tlmerchantNO").getParamValue();// 商户标识
				if(ExStringUtils.isEmpty(merNo)){
					statusCode = 300;
					message = "商户标识不能空";
					continue;
				}
				ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
				String key = property.getProperty("TLKey");
				if(ExStringUtils.isEmpty(key)){
					statusCode = 300;
					message = "秘钥不能空";
					continue;
				}
				String TLCheckUrl = property.getProperty("TLCheckUrl");
				if(ExStringUtils.isEmpty(TLCheckUrl)){
					statusCode = 300;
					message = "获取对账文件URL不能为空";
					continue;
				}
				Map<String, Object> statemenData = studentPaymentService.getStatementByDate(TLCheckUrl,tradeDate,merNo,key);
				statusCode = (Integer)statemenData.get("statusCode");
				if(statusCode == 300){
					message = (String)statemenData.get("msg");
					continue;
				}
				// 账单总计
				CheckFileTotalVO totalVO = (CheckFileTotalVO)statemenData.get("checkFileTotal");
				// 账单详细
				List<CheckFileVO> checkFileVOList = (List<CheckFileVO>)statemenData.get("checkFileList");
				// 下载
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		 	    GUIDUtils.init();
		 	    //导出
				File excelFile = null;
				File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				//模板文件路径
				String temFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
	   					   + File.separator+"excel"+File.separator+"checkFile_tl.xls";
				
				Map<String,Object> templateMap = new HashMap<String, Object>();// 模板参数
				templateMap.put("tradeDate", totalVO.getTradeDate() );
				templateMap.put("tradeItems", totalVO.getTradeItems());
				templateMap.put("totalFee",totalVO.getTotalFee());
				templateMap.put("totalAmount", totalVO.getTotalAmount());
				// 字典表值转换
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodePaymentMethod");
				Map<String , Object> ditionaryMap = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "check_file_tl", checkFileVOList,ditionaryMap);
				exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
				exportExcelService.getModelToExcel().setTemplateParam(temFilepathString, 3, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:",excelFile.getAbsoluteFile());
				String downloadFileName = totalVO.getTradeDate()+"对账文件.xls";
				String downloadFilePath = excelFile.getAbsolutePath();
							
				downloadFile(response, downloadFileName,downloadFilePath,true);	
			} while(false);
			if(statusCode!=200){
				renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\""+message+"\")</script>");
			}
		} catch(Exception e){
			logger.error("获取对账单出错",e);
			statusCode = 300;
			message = "获取对账单失败";
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\""+message+"\")</script>");
		} 
	}
	
	
	/**
	 * 解决POS机缴费成功，但系统收不到返回信息的情况
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/replenishByPos.html")
	public void replenishByPos(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "POST补缴费记录成功！";
		String paymentMethod = "4";// 付款方式
		String memo = ExStringUtils.trimToEmpty(request.getParameter("memo"));// 备注
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));// 学号
		String money = ExStringUtils.trimToEmpty(request.getParameter("money"));// 提交时候的金额
		String showType = request.getParameter("showType");// 处理类型
		String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));// 长号（准考证号或考生号）
		String uniqueValue = request.getParameter("uniqueValue");// 唯一标识
		String carrTermNum = ExStringUtils.trimToEmpty(request.getParameter("carrTermNum"));// 终端号
		String carrTermSN = ExStringUtils.trimToEmpty(request.getParameter("carrTermSN"));// 终端流水号
		String carrCardNo = ExStringUtils.trimToEmpty(request.getParameter("carrCardNo"));// 银行卡号
		String payTime = request.getParameter("payTime");// 缴费时间,格式：yyyy-MM-dd hh:mm:ss
		try {
			do{
				// 光大银行
				// 银行代码和银行名称
				if(ExStringUtils.isEmpty(money)){
					statusCode = 400;
					message = "金额不能为空！";
					continue;
				}
				
				StudentPayment studentPayment = null;
				TempStudentFee tsf = null;
				// 缴费注册信息
				String batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
				List<TempStudentFee>  tsfList = null;
				if("P".equals(showType)){// 已经存在学籍的学生缴费
					// 获取学生缴费情况
					studentPayment = studentPaymentService.findUnique("from StudentPayment s where s.isDeleted=0 and s.studentInfo.studyNo=?", studyNo);
					if(studentPayment == null){
						statusCode = 400;
						message = "该学生的缴费信息不存在！";
						continue;
					}
					tsfList = tempStudentFeeService.findByHql("from "+TempStudentFee.class.getSimpleName()+" tsf where tsf.isDeleted=0 and tsf.studentInfo.resourceid=? and tsf.payStatus='1'  and tsf.batchNo=?" , 
							studentPayment.getStudentInfo().getResourceid(),batchNo);
				} else {
					// 新生
					Map<String,Object> condition = new HashMap<String, Object>();
					condition.put("batchNo", batchNo);
					condition.put("examCertificateNo", examCertificateNo);
					tsfList = tempStudentFeeService.findListByContidion(condition);
				}
				
				if(ExCollectionUtils.isNotEmpty(tsfList)){
					tsf = tsfList.get(0);
				}
				
				if(tsf == null){
					statusCode = 400;
					message = "该学生的缴费信息不存在！";
					continue;
				}
				
				Double _actualSum= new BigDecimal(money).setScale(2, RoundingMode.FLOOR).doubleValue();
				// 2017-12-19 收款人统一
				User user =  userService.getUserByLoginId("hnjk");
				PayDetailsVO payDetailsVO = new PayDetailsVO();
				payDetailsVO.setPayAmount(_actualSum);
				payDetailsVO.setPaymentMethod(paymentMethod);
				payDetailsVO.setPosSerialNumber(carrTermSN);
				payDetailsVO.setCarrTermNum(carrTermNum);
				payDetailsVO.setCarrCardNo(carrCardNo);
				payDetailsVO.setMemo(memo);
				// TODO 以后如果有多个学校使用做成全局参数或数据字典
				payDetailsVO.setChargeMoney(0d);
				// 终端号加准考证号
				payDetailsVO.setEduOrederNo(carrTermNum+tsf.getExamCertificateNo());
				// 收费项
				payDetailsVO.setChargingItems(tsf.getChargingItems());
				// 是否开单位发票
				payDetailsVO.setIsInvoicing(tsf.getIsInvoicing());
				// 单位名称
				payDetailsVO.setInvoiceTitle(tsf.getInvoiceTitle());
				// 缴费时间
				if(ExStringUtils.isNotEmpty(payTime)){
					payDetailsVO.setPayTime(ExDateUtils.parseDate(payTime, ExDateUtils.PATTREN_DATE_TIME));
				}
				
				Map<String, Object> returnMap = null;
				if("P".equals(showType)){// POS机已有学籍缴费
					payDetailsVO.setCheckPayable(studentPayment.getStudentInfo().getStudentName()+"(学号"+studyNo+")");
					returnMap = studentPaymentService.payFee(studentPayment, payDetailsVO, user);
				} else {// POS机首次缴费注册
					returnMap = studentPaymentService.payOnline(examCertificateNo,Integer.valueOf(uniqueValue), user,payDetailsVO,tsf);
				}
				
				if(returnMap!=null && returnMap.size() >0){
					statusCode = (Integer)returnMap.get("statusCode");
					if(statusCode==300){
						message = (String)returnMap.get("message");
					}else if(statusCode==200){
						message = "缴费成功,实际缴费金额为"+_actualSum+"！";
						// 是一次缴清的
						tsf.setIsReconciliation(Constants.BOOLEAN_YES);
						tsf.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
						tsf.setEduOrderNo(payDetailsVO.getEduOrederNo());
						tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_NONEED);
						tempStudentFeeService.saveOrUpdate(tsf);
					}
				}
				
			} while(false);
		} catch (Exception e) {
			logger.error("POST补缴费记录失败！", e);
			statusCode = 300;
			message = "POST补缴费记录失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 进入缴费异常表单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPayment/payAbnormal.html")
	public String payAbnormal(HttpServletRequest request,HttpServletResponse response) throws WebException{
		
		return "/edu3/finance/studentpayment/payAbnormal";
	}
	
	/**
	 * 根据时间段批量获取缴费记录-广外
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/queryBatchByTimePeriod.html")
	public void queryBatchByTimePeriod(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "根据时间段批量获取缴费记录成功！";
		
		try {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			studentPaymentService.queryBatchForGW(startDate, endDate);
			
		} catch (Exception e) {
			logger.error("根据时间段批量获取缴费记录出错", e);
			statusCode = 300;
			message = "根据时间段批量获取缴费记录失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 根据身份证号获取缴费记录-广外
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/queryByCertNum.html")
	public void queryByCertNum(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "根据身份证号获取缴费记录成功！";
		
		try {
			String certNum = request.getParameter("certNum");
			studentPaymentService.queryForGW(certNum);
		} catch (Exception e) {
			logger.error("根据身份证号获取缴费记录出错", e);
			statusCode = 300;
			message = "根据身份证号获取缴费记录失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * TODO:提交代码时要删掉
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/testTextbookFee.html") 
	public void testTextbookFee(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String orderNo = request.getParameter("orderNo");
		String money = request.getParameter("money");
		Double actualSum= new BigDecimal(money).setScale(2, RoundingMode.FLOOR).doubleValue();
		TempStudentFee tsf = tempStudentFeeService.findUnique("from TempStudentFee tsf where tsf.isDeleted=0 and tsf.schoolOrderNo=?", orderNo);
		if(tsf!=null && tsf.getChargingItems().equals("textbookFee")){
			User user =  userService.getUserByLoginId("hnjk");
			PayDetailsVO payDetailsVO = new PayDetailsVO();
			payDetailsVO.setPayAmount(actualSum);
			payDetailsVO.setPaymentMethod("4");
			// TODO 以后如果有多个学校使用做成全局参数或数据字典
			payDetailsVO.setChargeMoney(0d);
			// 终端号加准考证号
			payDetailsVO.setEduOrederNo(tsf.getSchoolOrderNo());
			// 收费项
			payDetailsVO.setChargingItems(tsf.getChargingItems());
			// 是否开单位发票
			payDetailsVO.setIsInvoicing(tsf.getIsInvoicing());
			// 单位名称
			payDetailsVO.setInvoiceTitle(tsf.getInvoiceTitle());
			// 缴费时间
			String payTime = "2018-09-04 18:00:00";
			if(ExStringUtils.isNotEmpty(payTime)){
				payDetailsVO.setPayTime(ExDateUtils.parseDate(payTime, ExDateUtils.PATTREN_DATE_TIME));
			}
			studentPaymentService.payFeeForTextbook(tsf, payDetailsVO, user);
		}
	}
}
