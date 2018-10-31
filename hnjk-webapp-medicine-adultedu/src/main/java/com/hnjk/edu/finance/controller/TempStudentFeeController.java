package com.hnjk.edu.finance.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.FtpUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.finance.model.Reconciliation;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IReconciliationService;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.util.TonlyPayUtil;
import com.hnjk.edu.finance.vo.HeadVO;
import com.hnjk.edu.finance.vo.OrderInfoVO;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.PayOfflineVo;
import com.hnjk.edu.finance.vo.TempStudentFeeExportVO;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.util.BarcodeUtil;
import com.hnjk.edu.recruit.vo.EnrolleeInfoExportVO;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;
import com.ibm.icu.math.BigDecimal;

import edu.emory.mathcs.backport.java.util.Arrays;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 11, 2016 4:06:22 PM 
 * 
 */
@Controller
public class TempStudentFeeController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 3929729719304620256L;

	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService; 
	
	@Autowired
	@Qualifier("iReconciliationService")
	private IReconciliationService iReconciliationService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	/**
	 * 学生注册缴费信息列表
	 * 
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/list.html")
	public String listStudentPayment(HttpServletRequest request, Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("batchNo,examCertificateNo ");	
		objPage.setOrder(Page.DESC);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String gradeId = request.getParameter("gradeId");
		String isUploaded = request.getParameter("isUploaded");
		String eduOrederNo = ExStringUtils.trimToEmpty(request.getParameter("eduOrederNo"));
		String batchNo = ExStringUtils.trimToEmpty(request.getParameter("batchNo"));
		String unitId = request.getParameter("unitId");
		String payStatus = request.getParameter("payStatus");
		String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String majorId = request.getParameter("majorId");
		String studyNoType = request.getParameter("studyNoType");
		String isReconciliation = request.getParameter("isReconciliation");
		String handleStatus = request.getParameter("handleStatus");
		String chargingItems = request.getParameter("chargingItems");
		
		String isManger = "Y";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0) {
			unitId = cureentUser.getOrgUnit().getResourceid();
			isManger = "N";
			condition.put("unitId", unitId);
			model.addAttribute("linkageQuerySchoolId", unitId);
			condition.put("unitName", cureentUser.getOrgUnit().getUnitName());// 教学点名称
		}
		
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(isUploaded)){
			condition.put("isUploaded", isUploaded);
		}
		if(ExStringUtils.isNotEmpty(eduOrederNo)){
			condition.put("eduOrederNo", eduOrederNo);
		}
		if(ExStringUtils.isNotEmpty(batchNo)){
			condition.put("batchNo", batchNo);
		}
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(payStatus)){
			condition.put("payStatus", payStatus);
		}
		if(ExStringUtils.isNotEmpty(examCertificateNo)){
			condition.put("examCertificateNo", examCertificateNo);
		}
		if(ExStringUtils.isNotEmpty(certNum)){
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}
		if(ExStringUtils.isNotEmpty(studyNoType)){
			condition.put("studyNoType", studyNoType);
		}
		if(ExStringUtils.isNotEmpty(isReconciliation)){
			condition.put("isReconciliation", isReconciliation);
		}
		if(ExStringUtils.isNotEmpty(handleStatus)){
			condition.put("handleStatus", handleStatus);
		}
		if(ExStringUtils.isNotEmpty(chargingItems)){
			condition.put("chargingItems", chargingItems);
		}
		
		
		Page page = tempStudentFeeService.findByContidion(condition, objPage);	
		
		model.addAttribute("uniqueId",CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue());
		model.addAttribute("studentFeeList", page);
		model.addAttribute("isManger", isManger);
		model.addAttribute("condition", condition);
		return "/edu3/finance/tempStudentFee/list";
	}
	
	/**
	 * 同步学生缴费信息
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/order/syncTempFee.html")
	public void syncTempStrudentFee(HttpServletRequest request, HttpServletResponse response) throws WebException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "同步成功";
		String studentName = request.getParameter("studentName");
		String gradeId = request.getParameter("gradeId");
		String unitId = request.getParameter("unitId");
		String eduOrederNo = request.getParameter("eduOrederNo");
		String batchNo = request.getParameter("batchNo");
		String resourceids = request.getParameter("resourceIds");
		Map<String, Object> condition = new HashMap<String, Object>();
		StringBuffer msg = new StringBuffer();
		
		try {
			if(ExStringUtils.isNotEmpty(resourceids)){
				condition.put("resourceids", Arrays.asList(resourceids.split(",")));
			}else{
				if(ExStringUtils.isNotEmpty(studentName)){
					condition.put("studentName", studentName);
				}
				if(ExStringUtils.isNotEmpty(gradeId)){
					condition.put("gradeId", gradeId);
				}
				if(ExStringUtils.isNotEmpty(unitId)){
					condition.put("unitId", unitId);
				}
				if(ExStringUtils.isNotEmpty(eduOrederNo)){
					condition.put("eduOrederNo", eduOrederNo);
				}
				if(ExStringUtils.isNotEmpty(batchNo)){
					condition.put("batchNo", batchNo);
				}
			}
			condition.put("isUploaded", "N");
			List<TempStudentFee> studentFeeList = tempStudentFeeService.findListByContidion(condition);
			int totalSize = 0;
			int faileSize = 0;// 失败数量
			if(ExCollectionUtils.isNotEmpty(studentFeeList)){
				// 同步缴费信息到通联支付
				Date today = new Date();
				String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
				// 接收异步通知接口
				String receiveUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/order/result.html";
				// 缴费项目编号
				String projectNo = CacheAppManager.getSysConfigurationByCode("payment_projectNo").getParamValue();
				// 学校内部批次编号
				String _batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
				HeadVO headVO = new HeadVO(Constants.EDU_ORDER_INFO_ADD,dateTime,receiveUrl);
				int i=1;
				List<OrderInfoVO> orderInfoList = new ArrayList<OrderInfoVO>();
				OrderInfoVO orderInfoVO = new OrderInfoVO();
				totalSize = studentFeeList.size();
				String _msg = null;
				String studentId = null;
				for(TempStudentFee tsf: studentFeeList){
					orderInfoVO = new OrderInfoVO();
					orderInfoVO.setOrderNo(tsf.getSchoolOrderNo());// 学校订单号
					if("1".equals(tsf.getStudyNoType())){// 准考证号
						studentId = tsf.getExamCertificateNo();
					}else{
						studentId = tsf.getCertNum();
					}
					orderInfoVO.setStudentId(studentId);
					orderInfoVO.setAmount(String.valueOf((BigDecimal.valueOf(tsf.getAmount()).multiply(BigDecimal.valueOf(100))).intValue()));
					orderInfoList.add(orderInfoVO);
					if(i%300 == 0){
						Document returnDoc =TonlyPayUtil.operateOrderInfo(headVO, projectNo, _batchNo, orderInfoList);
						msg = getReturnInfo(returnDoc, msg,faileSize);
						_msg = msg.toString();
						int sizeIndex = _msg.lastIndexOf("-");
						faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
						msg.setLength(0);
						msg.append(_msg.substring(0, sizeIndex));
						orderInfoList.clear();
					}
					i++;
				}
				if(ExCollectionUtils.isNotEmpty(orderInfoList)){// 同步剩下的
					Document returnDoc =TonlyPayUtil.operateOrderInfo(headVO, projectNo, _batchNo, orderInfoList);
					msg = getReturnInfo(returnDoc, msg,faileSize);
					_msg = msg.toString();
					int sizeIndex = _msg.lastIndexOf("-");
					faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
					msg.setLength(0);
					msg.append(_msg.substring(0, sizeIndex));
				}
				msg.insert(0, "<br>成功："+(totalSize-faileSize)+" 条，失败："+faileSize+" 条<br>");
			} else {
				message = "没有数据要同步";
			}
			if(msg.length()!=0){
				if(totalSize!=0 && faileSize==0){
					statusCode = 200;
				}else{
					statusCode = 300;
				}
				message = msg.toString();
			}
			
		} catch (Exception e) {
			statusCode = 300;
			message = "同步学生缴费信息失败";
			logger.error("同步学生缴费信息失败", e);
		}finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	
	/**
	 * 生成学生注册缴费信息
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/finance/tempStudentFee/create.html")
	public void createTempStuFee(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "生成成功";
		
		// 收费项
		String chargingItems = request.getParameter("chargingItems");
		String branchSchool = request.getParameter("branchSchool");
		String recruitPlanId = request.getParameter("recruitPlanId");
		String classcicId = request.getParameter("classcicId");
		String majorId = request.getParameter("majorId");
		String name = request.getParameter("name");
		String enrolleeCode = request.getParameter("enrolleeCode");
		String certNum = request.getParameter("certNum");
		String resourceIds = request.getParameter("resourceIds");
		
		certNum  = ExStringUtils.trimToEmpty(certNum);
		if(ExStringUtils.isNotEmpty(resourceIds)){
			condition.put("resourceIds", Arrays.asList(resourceIds.split(",")));
		}else {
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);
			}
			if(ExStringUtils.isNotEmpty(classcicId)){
				condition.put("classic", classcicId);
			}
			if(ExStringUtils.isNotEmpty(majorId)){
				condition.put("major", majorId);
			}
			if(ExStringUtils.isNotEmpty(name)) {
//				name=ExStringUtils.getEncodeURIComponentByTwice(name);
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(enrolleeCode)){
				condition.put("enrolleeCode", enrolleeCode);
			}
			if(ExStringUtils.isNotEmpty(certNum)){
				condition.put("certNum", certNum);
			}
		}
		
		condition.put("registorFlag1", Constants.BOOLEAN_NO);
		try {
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				branchSchool=cureentUser.getOrgUnit().getResourceid();
				condition.put("branchSchool", branchSchool);//判断用户是否为校外学习中心
			}
			synchronized (this) {
				List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findListByCondition(condition);
				Map<String, Object> returnMap = tempStudentFeeService.createTempStuFee(enrolleeInfoList,chargingItems);
				if((Integer)returnMap.get("statusCode")==300) {
					statusCode = (Integer)returnMap.get("statusCode");
					message = (String)returnMap.get("message");	
				}
			}
			
		} catch (Exception e) {
			logger.error("生成学生注册缴费信息出错", e);
			statusCode = 300;
			message = "生成失败";		
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	
	/**
	 * 接收同步缴费信息的异步通知结果
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/order/result.html")
	public void getOrderResult(HttpServletRequest request, HttpServletResponse response) throws WebException {
		String returnData = request.getParameter("data");
//		System.out.println("====同步缴费信息的异步通知结果===="+returnData); 
		if(ExStringUtils.isNotEmpty(returnData)){
			// 处理新增订单异步通知
			tempStudentFeeService.handleOrderResult(returnData);
		}else{
			logger.info("接口/order/result.html->同步缴费信息的异步通知结果为空");
		}
		
	}
	
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
	 * 补缴费记录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/makeRecord.html")
	public void makeRecord(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "补缴费记录成功";
		
		String resourceid = request.getParameter("resourceId");
		try{
			do{
				if(ExStringUtils.isEmpty(resourceid)){
					statusCode = 300;
					message = "补缴费记录失败";		
					continue;
				}
				
				// 同步缴费信息到通联支付
				Date today = new Date();
				String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
				// 接收异步通知接口
				HeadVO headVO = new HeadVO(Constants.EDU_PAYMENT_QUERY,dateTime,"");
				Map<String, Object> returnMap = tempStudentFeeService.makeRecord(resourceid,headVO);
				statusCode = (Integer)returnMap.get("statusCode");
				if(statusCode==300){
					message = (String)returnMap.get("message");
				}
			}while(false);
		} catch (Exception e) {
			logger.error("补缴费记录出错", e);
			statusCode = 300;
			message = "补缴费记录失败";		
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 条形码打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/barcodePrintview.html")
	public String printviewBarcode(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String studentName = request.getParameter("studentName");
		String gradeId = request.getParameter("gradeId");
		String unitId = request.getParameter("unitId");
		String eduOrederNo = request.getParameter("eduOrederNo");
		String batchNo = request.getParameter("batchNo");
		String resourceids = request.getParameter("resourceIds");
		String majorId = request.getParameter("majorId");
		//条件
		model.addAttribute("studentName",ExStringUtils.trimToEmpty(studentName));
		model.addAttribute("gradeId",ExStringUtils.trimToEmpty(gradeId));
		model.addAttribute("unitId",ExStringUtils.trimToEmpty(unitId));
		model.addAttribute("eduOrederNo",ExStringUtils.trimToEmpty(eduOrederNo));
		model.addAttribute("batchNo",ExStringUtils.trimToEmpty(batchNo));
		model.addAttribute("resourceids",ExStringUtils.trimToEmpty(resourceids));
		model.addAttribute("majorId",ExStringUtils.trimToEmpty(majorId));
		
		return "/edu3/recruit/exameeinfo/exameeinfo-barcode-printview";
	}
	
	/**
	 * 打印学生缴费信息条形码
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/printBarcode.html")
	public void printBarcode(HttpServletRequest request, HttpServletResponse response) throws WebException{
		
		ArrayList<Map<String,Object>> maps = null;
		
		String studentName = request.getParameter("studentName");
		String gradeId = request.getParameter("gradeId");
		String unitId = request.getParameter("unitId");
		String eduOrederNo = request.getParameter("eduOrederNo");
		String batchNo = request.getParameter("batchNo");
		String resourceids = request.getParameter("resourceids");
		String majorId = request.getParameter("majorId");
		Map<String, Object> condition = new HashMap<String, Object>();
		
		try {
			if(ExStringUtils.isNotEmpty(resourceids)){
				condition.put("resourceids", Arrays.asList(resourceids.split(",")));
			}else{
				if(ExStringUtils.isNotEmpty(studentName)){
					condition.put("studentName", studentName);
				}
				if(ExStringUtils.isNotEmpty(gradeId)){
					condition.put("gradeId", gradeId);
				}
				if(ExStringUtils.isNotEmpty(unitId)){
					condition.put("unitId", unitId);
				}
				if(ExStringUtils.isNotEmpty(eduOrederNo)){
					condition.put("eduOrederNo", eduOrederNo);
				}
				if(ExStringUtils.isNotEmpty(batchNo)){
					condition.put("batchNo", batchNo);
				}
				if(ExStringUtils.isNotEmpty(majorId)){
					condition.put("majorId", majorId);
				}
			}
			condition.put("isUploaded", "Y");//必须已同步
			condition.put("payStatus", "1");//必须未付款
			List<TempStudentFee> studentFeeList = tempStudentFeeService.findListByContidion(condition);
			if(studentFeeList==null || studentFeeList.size()==0){
				renderHtml(response,"缺少打印数据！打印的数据必须是已同步,且未付款的");
			}else{
				List<EnrolleeInfoExportVO> list = null;
				
				list = covertToVo(studentFeeList);
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
			}		
		} catch (Exception e) {		
			renderHtml(response,"打印数据出错,联系管理员！");
			e.printStackTrace();
		}finally{
			if(maps!=null){
				for(Map<String,Object> tem: maps){
					 try {
						 if(tem.get("barcode")!=null){
							 ((InputStream)(tem.get("barcode"))).close();
						 }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			}	 
		}
		
		
	}
	
	/**
	 * 查询条形码信息
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/queryBarcode.html")
	public String queryBarcode(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String, Object> condition = new HashMap<String, Object>();
		String uniqueValue="";
		String showType = "";
		try {
			TempStudentFee tempStudentFee = tempStudentFeeService.get(resourceid);
			//条形码
			/*List<TempStudentFee> tsfFees = new ArrayList<TempStudentFee>();
			tsfFees.add(tempStudentFee);
			List<EnrolleeInfoExportVO> eInfoExportVOs = covertToVo(tsfFees);
			addBarcode(eInfoExportVOs);
			List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
			for(EnrolleeInfoExportVO vo : eInfoExportVOs){
				maps.add(convertToMapByEnrolleeInfoExportVO(vo));
			}
			model.addAttribute("barcode", maps.get(0).get("barcode")==null?"123":maps.get(0).get("barcode"));*/
		/*	
		 * if(tempStudentFee.getEduOrderNo()!=null){
				GUIDUtils.init();
				String path = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"barcode"+File.separator+tempStudentFee.getExamCertificateNo()+".jpeg";
				BarcodeUtil.encode2Image(tempStudentFee.getEduOrderNo(), 420, 80, path);
				model.addAttribute("rootUrl", CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
				model.addAttribute("barCodePath", tempStudentFee.getExamCertificateNo()+".jpeg");
			}*/
			uniqueValue = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			
			//学籍信息
			if("Y".equals(tempStudentFee.getHasStudentInfo())){
				String sql = " from "+StudentInfo.class.getSimpleName()+" where studentName=? and "; 
				if("0".equals(uniqueValue)){//准考证号
					sql += "examCertificateNo=?";
				}else {
					sql += "enrolleeCode=?";
				}
				StudentInfo studentInfo = studentInfoService.findUnique(sql ,tempStudentFee.getStudentName(),tempStudentFee.getExamCertificateNo());
				if(studentInfo!=null){
					showType = "P";
					model.addAttribute("studentInfo",studentInfo);
					
					List<StudentPayment> studentPaymentList = new ArrayList<StudentPayment>();
					List<StudentPaymentDetails> stuPaymentDetailList = new ArrayList<StudentPaymentDetails>();
					condition.put("studyNo", studentInfo.getStudyNo());
					studentPaymentList = studentPaymentService.findStudentPaymentByCondition(condition);
					if(ExCollectionUtils.isNotEmpty(studentPaymentList)){
						StudentPayment studentPayment = studentPaymentList.get(0);
						condition.put("studentInfoId", studentInfo.getResourceid());
						stuPaymentDetailList = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
						model.addAttribute("studentPaymentInfo",studentPayment );
						model.addAttribute("studentPaymentDetailsList", stuPaymentDetailList);
					}
				}
			}
			if(tempStudentFee!=null){
				model.addAttribute("tempStudentFee",tempStudentFee);
			}
			model.addAttribute("uniqueValue", uniqueValue);
			model.addAttribute("showType", showType);
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return "/edu3/finance/tempStudentFee/barcodeQuery";
	}
	
	/**
	 * 将临时学生缴费信息转换成打印VO,用于打印条形码
	 * @param studentFeeList<TempStudentFee>
	 * @return 
	 */
	private List<EnrolleeInfoExportVO> covertToVo(List<TempStudentFee> studentFeeList) {
		List<EnrolleeInfoExportVO> returnList = new ArrayList<EnrolleeInfoExportVO>();
		for(TempStudentFee temp:studentFeeList){
			EnrolleeInfoExportVO vo = new EnrolleeInfoExportVO();
			vo.setCertNum(temp.getCertNum());// 身份证号
			vo.setEnrolleeNo(temp.getExamCertificateNo());
			vo.setEnrolleeName(temp.getStudentName());
			vo.setEduOrderNo(temp.getEduOrderNo());
			returnList.add(vo);
		}
		return returnList;
	}
	
	/**
	 * 增加条形码流
	 * @param list<EnrolleeInfoExportVO>
	 * @return 
	 */
	private void addBarcode(List<EnrolleeInfoExportVO> list) {
		for(EnrolleeInfoExportVO vo :list){
//			vo.setBarcode(BarcodeUtil.encode(vo.getEnrolleeNo(),200,36));
			vo.setBarcode(BarcodeUtil.encode(vo.getEduOrderNo(),200,36));
		}
	}
	
	/*
	 * 转换成Map
	 */
	/**
	 * 考生注销信息转为map
	 * @param enrolleeInfoExportVO
	 * @return
	 */
	public Map<String, Object> convertToMapByEnrolleeInfoExportVO(EnrolleeInfoExportVO enrolleeInfoExportVO){
		Map<String, Object> map = ExBeanUtils.convertBeanToMap(enrolleeInfoExportVO);
		
		map.put("enrolleeNo", enrolleeInfoExportVO.getEnrolleeNo()); // 学号
		map.put("enrolleeName", enrolleeInfoExportVO.getEnrolleeName()); // 姓名	
		map.put("barcode", enrolleeInfoExportVO.getBarcode()); // 条形码流
		
		return map;
	}
	
	/**
	 * 导出学生信息
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/export-stu.html")
	public void exportListStu(HttpServletRequest request,HttpServletResponse response) throws WebException{					
		Map<String, Object> condition = new HashMap<String, Object>();
		String studentName = request.getParameter("studentName");
		String gradeId = request.getParameter("gradeId");
		String isUploaded = request.getParameter("isUploaded");
		String eduOrederNo = request.getParameter("eduOrederNo");
		String batchNo = request.getParameter("batchNo");
		String unitId = request.getParameter("unitId");
		String payStatus = request.getParameter("payStatus");
		String examCertificateNo = request.getParameter("examCertificateNo");
		String resourceids = request.getParameter("resourceIds");
		String majorId = request.getParameter("majorId");
		
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(isUploaded)){
			condition.put("isUploaded", isUploaded);
		}
		if(ExStringUtils.isNotEmpty(eduOrederNo)){
			condition.put("eduOrederNo", eduOrederNo);
		}
		if(ExStringUtils.isNotEmpty(batchNo)){
			condition.put("batchNo", batchNo);
		}
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(payStatus)){
			condition.put("payStatus", payStatus);
		}
		if(ExStringUtils.isNotEmpty(examCertificateNo)){
			condition.put("examCertificateNo", examCertificateNo);
		}
		if(ExStringUtils.isNotEmpty(resourceids)){
			condition.put("resourceids", Arrays.asList(resourceids.split(",")));
		}
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}
		condition.put("order", " order by examCertificateNo,unit.unitCode ");
		
		List<TempStudentFee> list = tempStudentFeeService.findListByContidion(condition);
		if(list.size()>8000){
			renderHtml(response, "<script>alert('导出的数据量"+list.size()+"太大,请控制在8000以内')</script>");
		}
		try{		
			//String flag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			List<EnrolleeInfoExportVO> voList = covertToExportVO(list);	
			
			//增加最后一个end_point
			EnrolleeInfoExportVO end = new EnrolleeInfoExportVO();
			end.setEnrolleeNo("END_POINT");
			voList.add(end);
					
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
			templateMap.put("school", CacheAppManager.getSysConfigurationByCode("tlschoolName").getParamValue());//学校,学校代码,商业名称为空,都用全局参数
			templateMap.put("schoolCode",CacheAppManager.getSysConfigurationByCode("tlschoolNo").getParamValue());
			templateMap.put("businessName", CacheAppManager.getSysConfigurationByCode("tlmerchantName").getParamValue());
			templateMap.put("total", String.valueOf(voList.size()-1));

			templateFilepathString = "exameeinfo_exprot_stu.xls";
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "exameeinfo_exprot_stu", voList,condition);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			
			String downloadFileName = "录取学生信息.xls";
			String downloadFilePath = excelFile.getAbsolutePath();
						
			downloadFile(response, downloadFileName,downloadFilePath,true);	
	 	 
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错,请联系管理员')</script>");
		 }
	}
	
	
	/**
	 * 将查询的学生临时缴费信息转换成导出学生信息vo
	 * @param list<TempStudentFee>
	 * @return List<EnrolleeInfoExportVO>
	 */
	private List<EnrolleeInfoExportVO> covertToExportVO(List<TempStudentFee> list) {
		
		List<EnrolleeInfoExportVO> returnList = new ArrayList<EnrolleeInfoExportVO>();
//		Map<String,Object> condition = new HashMap<String, Object>();
		if(ExCollectionUtils.isNotEmpty(list)){
			String certNum = null;
			String enrolleeNo = null;
			for(TempStudentFee tem:list){
//				condition.put("name", tem.getStudentName());
//				List<Map<String,Object>> exameeInfoList = exameeInfoService.findExameeInfoByConditionWithJDBC(condition);
				EnrolleeInfoExportVO tempVo = new EnrolleeInfoExportVO();
				if("1".equals(tem.getStudyNoType())){
					enrolleeNo = tem.getExamCertificateNo();
				}else {
					enrolleeNo = tem.getCertNum();
				}
				tempVo.setEnrolleeNo(enrolleeNo);
				tempVo.setEnrolleeBrSchool(tem.getUnit().getUnitName());
				tempVo.setEnrolleeClass("");
//				tempVo.setEnrolleeMajor((String) exameeInfoList.get(0).get("LQZYMC"));
				if(tem.getMajor()!=null){
					tempVo.setEnrolleeMajor(tem.getMajor().getMajorName());	
				}
				tempVo.setEnrolleeName(tem.getStudentName());
				
			/*	if("Y".equals(tem.getHasStudentInfo())){
					certNum = tem.getStudentInfo().getStudentBaseInfo().getCertNum();
				}else{
					certNum = tem.getEnrolleeInfo().getStudentBaseInfo().getCertNum();
				}*/
				certNum = tem.getCertNum().replace("(", "").replace(")", "").replace("（", "").replace("）", "");
				tempVo.setCertNum(certNum.substring(certNum.length()-6));
				tempVo.setMoney(tem.getAmount());
				
				returnList.add(tempVo);
			}
		}
			
		return returnList;
	}
	
	/**
	 * 申请注销第三方学生缴费信息
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/order/deleteTempFee.html")
	public void deleteTempStrudentFee(HttpServletRequest request, HttpServletResponse response) throws WebException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "操作成功";
		
		StringBuffer msg = new StringBuffer();
		
		String operate = request.getParameter("operate");// 操作类型，delete为逻辑删除，backout为物理删除
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String gradeId = request.getParameter("gradeId");
		String unitId = request.getParameter("unitId");
		String eduOrederNo = ExStringUtils.trimToEmpty(request.getParameter("eduOrederNo"));
		String batchNo = ExStringUtils.trimToEmpty(request.getParameter("batchNo"));
		String resourceids = request.getParameter("resourceids");
		String majorId = request.getParameter("majorId");
		Map<String, Object> condition = new HashMap<String, Object>();
		
		try {
			if(ExStringUtils.isNotEmpty(resourceids)){
				condition.put("resourceids", Arrays.asList(resourceids.split(",")));
			}else{
				if(ExStringUtils.isNotEmpty(studentName)){
					condition.put("studentName", studentName);
				}
				if(ExStringUtils.isNotEmpty(gradeId)){
					condition.put("gradeId", gradeId);
				}
				if(ExStringUtils.isNotEmpty(unitId)){
					condition.put("unitId", unitId);
				}
				if(ExStringUtils.isNotEmpty(eduOrederNo)){
					condition.put("eduOrederNo", eduOrederNo);
				}
				if(ExStringUtils.isNotEmpty(batchNo)){
					condition.put("batchNo", batchNo);
				}
				if(ExStringUtils.isNotEmpty(majorId)){
					condition.put("majorId", majorId);
				}
			}
			condition.put("isUploaded", "Y");//必须已同步
			condition.put("payStatus", "1");//必须未付款
			List<TempStudentFee> studentFeeList = tempStudentFeeService.findListByContidion(condition);
			if(ExCollectionUtils.isEmpty(studentFeeList)){
				statusCode = 300;
				message = "没有需要处理的数据(只有已同步且未付款的记录才可操作)";
			} else {
				if("backout".equals(operate)){// 撤销，物理删除
					for(TempStudentFee tsf: studentFeeList){
						tempStudentFeeService.truncateByProperty(TempStudentFee.class,"resourceid",tsf.getResourceid());
					}
				} else {
					int totalSize = 0;
					int faileSize = 0;// 失败数量
					if(ExCollectionUtils.isNotEmpty(studentFeeList)){
						Date today = new Date();
						String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
						// 接收异步通知接口
						String receiveUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/order/deleteResult.html";
						HeadVO headVO = new HeadVO(Constants.EDU_ORDER_INFO_CLOSE,dateTime,receiveUrl);
						
						totalSize = studentFeeList.size();
						//50一批处理
						int i=1;
						List<TempStudentFee> tempList = new ArrayList<TempStudentFee>();
						String _msg = null;
						for(TempStudentFee tsf: studentFeeList){
							tempList.add(tsf);
							if(i%50 == 0){
								Document returnDoc =TonlyPayUtil.cancelOrder(headVO, tempList);
								msg = getReturnInfo(returnDoc, msg,faileSize);
								_msg = msg.toString();
								int sizeIndex = _msg.lastIndexOf("-");
								faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
								msg.setLength(0);
								msg.append(_msg.substring(0, sizeIndex));
								tempList.clear();
							}
							i++;
						}
						if(ExCollectionUtils.isNotEmpty(tempList)){// 注销剩下的
							Document returnDoc =TonlyPayUtil.cancelOrder(headVO, tempList);
							msg = getReturnInfo(returnDoc, msg,faileSize);
							_msg = msg.toString();
							int sizeIndex = _msg.lastIndexOf("-");
							faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
							msg.setLength(0);
							msg.append(_msg.substring(0, sizeIndex));
						}
						msg.insert(0, "<br>申请成功："+(totalSize-faileSize)+" 条，申请失败："+faileSize+" 条<br>");
					}
					
					if(msg.length()!=0){
						if(totalSize!=0 && faileSize==0){
							statusCode = 200;
						}else{
							statusCode = 300;
						}
						message = msg.toString();
					}
				}
			}
		} catch (Exception e) {
			statusCode = 300;
			message = "操作失败";
			logger.error("删除或撤销学生缴费信息失败", e);
		}finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
		}	
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 申请第三方注销学生缴费信息的异步通知结果
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/order/deleteResult.html")
	public void getDeleteResult(HttpServletRequest request, HttpServletResponse response) throws WebException{
		String returnData = request.getParameter("data");
		if(ExStringUtils.isNotEmpty(returnData)){
			// 处理注销订单异步通知
			tempStudentFeeService.handleDeleteResult(returnData);
		}else{
			logger.info("接口/order/deleteResult.html->注销订单信息的异步通知结果为空");
		}
	}
	
	/**
	 * 导出缴费信息
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/export.html")
	public void exportTempFeeList(HttpServletRequest request,HttpServletResponse response) throws WebException{					
		Map<String, Object> condition = new HashMap<String, Object>();
		String studentName = request.getParameter("studentName");
		String gradeId = request.getParameter("gradeId");
		String isUploaded = request.getParameter("isUploaded");
		String eduOrederNo = request.getParameter("eduOrederNo");
		String batchNo = request.getParameter("batchNo");
		String unitId = request.getParameter("unitId");
		String payStatus = request.getParameter("payStatus");
		String examCertificateNo = request.getParameter("examCertificateNo");
		String resourceids = request.getParameter("resourceIds");
		String majorId = request.getParameter("majorId");
		
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(isUploaded)){
			condition.put("isUploaded", isUploaded);
		}
		if(ExStringUtils.isNotEmpty(eduOrederNo)){
			condition.put("eduOrederNo", eduOrederNo);
		}
		if(ExStringUtils.isNotEmpty(batchNo)){
			condition.put("batchNo", batchNo);
		}
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(payStatus)){
			condition.put("payStatus", payStatus);
		}
		if(ExStringUtils.isNotEmpty(examCertificateNo)){
			condition.put("examCertificateNo", examCertificateNo);
		}
		if(ExStringUtils.isNotEmpty(resourceids)){
			condition.put("resourceids", Arrays.asList(resourceids.split(",")));
		}
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}
		condition.put("order", " order by examCertificateNo,unit.unitCode ");
		
		List<TempStudentFee> list = tempStudentFeeService.findListByContidion(condition);

		try{
			List<TempStudentFeeExportVO> listVo = covertToTempStudentFeeExportVO(list);
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
	 	    GUIDUtils.init();
	 	    //导出
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "exameeinfo_exprot_tempfee", listVo,condition);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			
			String downloadFileName = "学生注册缴费信息.xls";
			String downloadFilePath = excelFile.getAbsolutePath();
						
			downloadFile(response, downloadFileName,downloadFilePath,true);	
	 	 
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错,请联系管理员')</script>");
		 }
	}
	
	/**
	 * 将查询的学生临时缴费信息转换成导出学生临时缴费信息vo
	 * @param list<TempStudentFee>
	 * @return List<TempStudentFeeExportVO>
	 */
	private List<TempStudentFeeExportVO> covertToTempStudentFeeExportVO(List<TempStudentFee> list) {
		
		List<TempStudentFeeExportVO> returnList = new ArrayList<TempStudentFeeExportVO>();
		if(ExCollectionUtils.isNotEmpty(list)){
//			String studentId = null;
			for(TempStudentFee tem:list){
				TempStudentFeeExportVO tempVo = new TempStudentFeeExportVO();
				tempVo.setAmount(tem.getAmount());
				tempVo.setBatchNo(tem.getBatchNo());
				tempVo.setEduOrderNo(tem.getEduOrderNo());
				/*if("1".equals(tem.getStudyNoType())){
					studentId = tem.getExamCertificateNo();
				}else{
					studentId = tem.getCertNum();
				}*/
				tempVo.setCertNum(tem.getCertNum());
				tempVo.setExamCertificateNo(tem.getExamCertificateNo());
				tempVo.setGradeName(tem.getGrade().getGradeName());
				tempVo.setIsUploaded(JstlCustomFunction.dictionaryCode2Value("yesOrNo", tem.getIsUploaded()));
				if(tem.getMajor()!=null){
					tempVo.setMajorName(tem.getMajor().getMajorName());	
				}
				tempVo.setPayStatus(JstlCustomFunction.dictionaryCode2Value("CodePayStatus", tem.getPayStatus()));
				tempVo.setSchoolOrderNo(tem.getSchoolOrderNo());
				tempVo.setStudentName(tem.getStudentName());
				tempVo.setUnitName(tem.getUnit().getUnitName());
				returnList.add(tempVo);
			}
		}		
		return returnList;
	}
	
	/**
	 * 导出缴费信息
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/export-fee.html")
	public void exportListStuFee(HttpServletRequest request,HttpServletResponse response) throws WebException{					
		Map<String, Object> condition = new HashMap<String, Object>();
		String studentName = request.getParameter("studentName");
		String gradeId = request.getParameter("gradeId");
		String isUploaded = request.getParameter("isUploaded");
		// 只导出未同步的缴费信息
		isUploaded = Constants.BOOLEAN_NO;
		String eduOrederNo = request.getParameter("eduOrederNo");
		String batchNo = request.getParameter("batchNo");
		String unitId = request.getParameter("unitId");
		String payStatus = request.getParameter("payStatus");
		String examCertificateNo = request.getParameter("examCertificateNo");
		String resourceids = request.getParameter("resourceIds");
		String majorId = request.getParameter("majorId");
		//根据全局参数查询
		batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
		
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(isUploaded)){
			condition.put("isUploaded", isUploaded);
		}
		if(ExStringUtils.isNotEmpty(eduOrederNo)){
			condition.put("eduOrederNo", eduOrederNo);
		}
		if(ExStringUtils.isNotEmpty(batchNo)){
			condition.put("batchNo", batchNo);
		}
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(payStatus)){
			condition.put("payStatus", payStatus);
		}
		if(ExStringUtils.isNotEmpty(examCertificateNo)){
			condition.put("examCertificateNo", examCertificateNo);
		}
		if(ExStringUtils.isNotEmpty(resourceids)){
			condition.put("resourceids", Arrays.asList(resourceids.split(",")));
		}
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}
		
		condition.put("order", " order by examCertificateNo,unit.unitCode ");
		
		List<TempStudentFee> list = tempStudentFeeService.findListByContidion(condition);
		
		try{		
			//String flag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			List<EnrolleeInfoExportVO> voList = covertToExportVO(list);	
			
			//增加最后一个end_point
			EnrolleeInfoExportVO end = new EnrolleeInfoExportVO();
			end.setEnrolleeNo("END_POINT");
			voList.add(end);
					
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
	 	    GUIDUtils.init();
	 	    //导出
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			
			//模板文件路径
			String templateFilepathString = "";
			
			
			templateMap.put("batchNo", batchNo);//批次号
			templateMap.put("projectNo", CacheAppManager.getSysConfigurationByCode("payment_projectNo").getParamValue());//项目编号
			templateMap.put("schoolCode",CacheAppManager.getSysConfigurationByCode("tlschoolNo").getParamValue());//学校编号
			templateMap.put("businessNo", CacheAppManager.getSysConfigurationByCode("tlmerchantNO").getParamValue());//商户编号
			templateMap.put("total", String.valueOf(voList.size()-1));
			
			templateFilepathString = "exameeinfo_exprot_stufee.xls";
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "exameeinfo_exprot_stufee", voList,condition);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 7, templateMap);
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			
			String downloadFileName = batchNo+"-学生缴费信息.xls";
			String downloadFilePath = excelFile.getAbsolutePath();
						
			downloadFile(response, downloadFileName,downloadFilePath,true);	
	 	 
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错,请联系管理员')</script>");
		 }
	}
	
	/**
	 * 申请第三方修改学生信息的异步通知结果
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/tempStudentFee/editInfo.html")
	public void getEditStudentInfoResult(HttpServletRequest request, HttpServletResponse response) throws WebException{
		String returnData = request.getParameter("data");
		if(ExStringUtils.isNotEmpty(returnData)){
			// 处理新增订单异步通知
			tempStudentFeeService.handleEditStudentInfo(returnData);
		}else{
			logger.info("接口/order/deleteResult.html->注销订单信息的异步通知结果为空");
		}
	}
	
	/**
	 * 申请第三方注销学生缴费信息的异步通知结果(第二个反馈,另一种方式支付了,将订单注销,设置管理系统记录为已付款)
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/order/deleteResultSecond.html")
	public void getDeleteResultSecond(HttpServletRequest request, HttpServletResponse response) throws WebException{
		String returnData = request.getParameter("data");
		if(ExStringUtils.isNotEmpty(returnData)){
			// 处理注销订单异步通知
			tempStudentFeeService.handleDeleteResultSecond(returnData);
		}else{
			logger.info("接口/order/deleteResult.html->注销订单信息的异步通知结果为空");
		}
	}
	/**
	 * 返回对账列表的list
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/reconciliation/list.html")
	public String getReconciliationList(Page objPage,ModelMap model) throws Exception{
		Map<String,Object> condition = new HashMap<String,Object>();
		objPage.setOrder(Page.DESC);
		Page page = iReconciliationService.getReconciliationPage(condition, objPage);
		
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		return "/edu3/finance/reconciliation/reconciliation-list";
	}
	/**
	 * 执行对账功能
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/reconciliation/progress.html")
	public void ReconciliationProgress(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode=200;
		String message="对账成功";
		try {
			do {
				String resourceid = request.getParameter("resourceid");
				Reconciliation re = iReconciliationService.get(resourceid);
				if(re!=null){
					String downloadDir=iReconciliationService.initFtpServer();
					String fileName=downloadDir+re.getFileName();			
					int isExist = FtpUtils.download("", re.getFileName(), fileName);
					if(isExist==0){//下载文件成功，表示文件存在
						List<TempStudentFee> list = new ArrayList<TempStudentFee>();
						String info = FileUtils.readFile(fileName);
						String [] strArray = info.split("\n");
						String  headline = strArray[0];
						//第一行：0-8位是日期，9-17位是总笔数，剩余为总金额
						int totalCount = Integer.valueOf(headline.substring(8, 17).trim());
						int tmpCount = iReconciliationService.saveReconciliation(list, strArray);
						
						if(totalCount==tmpCount){//当文件中的对账数等于系统中更新的对账数时，更新对账文件的标识为已对账
							re.setStatus(Reconciliation.status_yes);
							iReconciliationService.saveOrUpdate(re);
							tempStudentFeeService.batchSaveOrUpdate(list);
						}else{
							statusCode = 300;
							message="对账失败！<br>对账文件:"+re.getFileName()+"<br>首行总笔数为："+totalCount+" <br>根据文件中的交易流水号在系统中查询到的总笔数："+tmpCount;
						}
						
					}else{
						statusCode=300;
						message="下载文件失败，请检查FTP服务器上该文件是否存在！";
					}
					
				}
			} while (false);
			
		} catch (Exception e) {
			logger.error("保存回复帖出错：{}",e.fillInStackTrace());
			statusCode = 300;
			message = "保存失败！";
		}finally{
			map.put("statusCode",statusCode);
			map.put("message",message);
			FtpUtils.close();
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除对账文件内容
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/reconciliation/delete.html")
	public void deleteReconciliation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		boolean isSuccess = iReconciliationService.deleteReconciliation(resourceids);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "删除成功！");
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.DELETE,"保存导入成绩：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}else{
			map.put("statusCode", 300);
			map.put("message", "删除失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 下载导入线下缴费信息模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/payOffLineTemplate.html")
	public void downloadPayOffLineTemplate(HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			String templateFilepathString = "payOfflineImport.xls";
			downloadFile(response, "线下缴费信息模板.xls", templateFilepathString,false);
		}catch(Exception e){
			logger.error("下载导入线下缴费信息模板出错：",e);
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载线下缴费信息模板失败\");</script>");
		}
	}
	
	/**
	 * 进入导入线下缴费页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/importPayOfflineForm.html")
	public String importPayOffLineForm(HttpServletRequest request,HttpServletResponse response) throws WebException{
		
		return "/edu3/finance/tempStudentFee/importPayOffline";
	}
	
	/**
	 * 导入线下缴费信息逻辑处理
	 * 
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/finance/tempStudentFee/importPayOffline.html")
	public void importPayOfflineInfo(String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer message = new StringBuffer(500);
		try {			
			List<PayOfflineVo> failList = null;
			if( ExStringUtils.isNotBlank(attachId)){			
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				importExcelService.initParmas(excel, "importPayOffline", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				importExcelService.getExcelToModel().setStartTitleRow(2);
				List<PayOfflineVo> modelList = importExcelService.getModelList();
				// 总条数
				int totalNum = 0;
				// 失败条数
				int failNum = 0;
				if(ExCollectionUtils.isNotEmpty(modelList)){
					totalNum = modelList.size();
					Map<String, Object> returnMap = tempStudentFeeService.handlePayOfflineInfoImport(modelList);
					if(returnMap!=null &&returnMap.size()>0 ){
						statusCode = (Integer)returnMap.get("statusCode");
						if(statusCode!=200) {
							message.append("导入失败的记录：<br/>").append((String)returnMap.get("message"));
							if(statusCode==400){
								failList = (List<PayOfflineVo>)returnMap.get("failList");
							}
						}
					}
					
					//导出线下缴费信息中的失败的记录
					if(ExCollectionUtils.isNotEmpty(failList)){
						failNum = failList.size();
						setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
						//导出
						GUIDUtils.init();
						String fileName = GUIDUtils.buildMd5GUID(false);
						File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
							
						//模板文件路径
						String templateFilepathString = "payOfflineImportError.xls";
						//初始化配置参数
						exportExcelService.initParmasByfile(disFile,"exportPayOfflineError", failList,null);
						exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, null);
						exportExcelService.getModelToExcel().setRowHeight(400);
						File excelFile = exportExcelService.getExcelFile();//获取导出的文件(将导出文件写到硬盘对应的目录中)
						logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());	
						String upLoadurl = "/edu3/finance/tempStudentFee/exportPayOfflineInfoError.html?excelFile="+fileName;
						map.put("exportErrorPayOffline", upLoadurl);
					}
					
					message.insert(0, "成功导入："+(totalNum-failNum)+"条<br/>");
				} else {
					statusCode = 300;				
					message.append("没有数据");
				}
			} else {
				statusCode = 300;				
				message.append("请上传附件");
			}
		} catch (Exception e) {
			logger.error("导入线下缴费信息出错:{}",e);
			statusCode = 300;
			message.append("导入线下缴费信息失败！");
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message.toString());
			map.put("reloadUrl", request.getContextPath()+"/edu3/finance/tempStudentFee/list.html");
			map.put("navTabId", "RES_FINANCE_TEMPSTUDENTFEE");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/exportPayOfflineInfoError.html")
	public void exportUserOrderInfoerror(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入线下缴费信息失败记录.xls", disFile.getAbsolutePath(),true);
	}	
	
	/**
	 * 审核线下缴费订单
	 * 
	 * <p>
	 *   通过该功能处理教学点直接汇款到成教院的情况
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/tempStudentFee/payOffLine.html")
	public void payOffLine(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer message = new StringBuffer(200);
		// 付款方式：汇款缴费
		String paymentMethod = "7";
		try {
			do{
				String tempStudentFeeIds = request.getParameter("resourceid");
				// 审核结果
				String auditResult = request.getParameter("auditResult");
				if(ExStringUtils.isBlank(tempStudentFeeIds)){
					statusCode = 300;
					message.append("没有需要处理的数据");
					break;
				}
				if(ExStringUtils.isBlank(auditResult)){
					statusCode = 300;
					message.append("审核结果不能为空！");
					break;
				}
				String[] studentFeeIds = tempStudentFeeIds.split(",");
				User user =  userService.getUserByLoginId("hnjk");
				SysConfiguration config = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId");
				 int uniqueValue = 0;
				 if(config!=null &&  "1".equals(config.getParamValue())){
					 uniqueValue = 1;
				 }
				TempStudentFee tsf = null;
				PayDetailsVO payDetailsVO = null;
				Map<String, Object> returnMap = null;
				String examCertificateNo = null;
				List<TempStudentFee> tempStudentFeeList = new ArrayList<TempStudentFee>(100);
				int failNum = 0;
				for(int i=0;i<studentFeeIds.length;i++){
					tsf = tempStudentFeeService.findUniqueByProperty("resourceid", studentFeeIds[i]);
					if (tsf==null) {
						message.append("该订单不符合审核条件<br/>");
						failNum++;
						continue;
					}
					if(tsf==null || (tsf.getIsDeleted()==0 && !TempStudentFee.HANDLESTATUS_TOAUDIT.equals(tsf.getHandleStatus()))){
						message.append(tsf.getSchoolOrderNo()).append("该订单不符合审核条件<br/>");
						failNum++;
						continue;
					}
					// 审核不通过
					if("noPass".equals(auditResult)){
						tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_NOPASS);
						tempStudentFeeList.add(tsf);
						continue;
					}
					examCertificateNo = tsf.getExamCertificateNo();
					payDetailsVO = new PayDetailsVO();
					payDetailsVO.setPayAmount(tsf.getAmount());
					payDetailsVO.setPaymentMethod(paymentMethod);
					payDetailsVO.setChargeMoney(0d);
					// 导入的线下缴费订单与学校的一样
					payDetailsVO.setEduOrederNo(tsf.getSchoolOrderNo());
					// 缴费时间为当前时间
					payDetailsVO.setPayTime(new Date());
					// 收费项
					payDetailsVO.setChargingItems(tsf.getChargingItems());
					// 是否开单位发票
					payDetailsVO.setIsInvoicing(tsf.getIsInvoicing());
					// 单位名称
					payDetailsVO.setInvoiceTitle(tsf.getInvoiceTitle());
					
					// 处理缴费逻辑
					returnMap = studentPaymentService.payOnline(examCertificateNo,Integer.valueOf(uniqueValue), user,payDetailsVO,tsf);
					
					if(returnMap!=null && returnMap.size() >0){
						statusCode = (Integer)returnMap.get("statusCode");
						if(statusCode==300){
							message.append((String)returnMap.get("message")).append("<br/>");
							failNum++;
						}else if(statusCode==200){
							// 是一次缴清的
							tsf.setIsReconciliation(Constants.BOOLEAN_YES);
							tsf.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
							tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_PASS);
							tsf.setEduOrderNo(payDetailsVO.getEduOrederNo());
							tempStudentFeeList.add(tsf);
						}
					}
				}
				
				if(message.length()>0){
					statusCode = 300;
					message.insert(0, "审核失败的订单如下:<br/>");
				}else{
					statusCode = 200;
				}
				message.insert(0, "成功审核："+(studentFeeIds.length-failNum)+"条<br/>");
				if(ExCollectionUtils.isNotEmpty(tempStudentFeeList)){
					tempStudentFeeService.batchSaveOrUpdate(tempStudentFeeList);
				}
			} while(false);
		} catch (Exception e) {
			logger.error("审核失败", e);
			statusCode = 300;
			message.setLength(0);
			message.append("审核失败！");
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
}
