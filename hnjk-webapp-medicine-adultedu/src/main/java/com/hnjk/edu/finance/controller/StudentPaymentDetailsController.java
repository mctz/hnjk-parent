/**
 * <code>StuPerpayfeeController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:22:25
 * @see 
 * @version 1.0
 */

package com.hnjk.edu.finance.controller;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.ServerException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.service.IReturnPremiumService;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.vo.ChargeSummaryVo;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.StudentPaymentDetailsExportVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.taglib.function.JstlCustFunction;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**
 * 学生缴费明细管理
 * <code>StudentPaymentDetailsController</code>
 * 
 *@author：  zik, 广东学苑教育发展有限公司
 * @since： 2015-11-16 16:36
 * @see 
 * @version 1.0
 */
@Controller
public class StudentPaymentDetailsController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 1802218469458763531L;
	
	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService; 
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("returnPremiumService")
	private IReturnPremiumService returnPremiumService;
	
	/**
	 * 获取某个学生缴费明细列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/single-list.html")
	public String listStudentPaymentDetails(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		try {
			objPage.setOrderBy(" spd.studentInfo.studyNo,spd.operateDate ");
			objPage.setOrder(Page.DESC);
			// 处理参数
			String studentInfoId = request.getParameter("studentInfoId");//学籍ID
			String stuPaymentId = request.getParameter("stuPaymentId");//缴费ID
			String payType = request.getParameter("payType");// 缴费类型
			
			if(ExStringUtils.isNotEmpty(studentInfoId)){
				condition.put("studentInfoId", studentInfoId);
			}
			if(ExStringUtils.isNotEmpty(payType)){
				condition.put("payType", payType);
			}

			StudentPayment studentPayment = studentPaymentService.get(stuPaymentId);
			
			Page page = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(objPage,condition);
//			model.addAttribute("schoolCode",CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
			model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
			model.addAttribute("studentPaymentDetailsList", page);
			model.addAttribute("studentPaymentInfo", studentPayment);
			model.addAttribute("condition", condition);
		} catch (ServiceException e) {
			logger.error("获取学生缴费明细列表出错", e);
		}
		
		return "/edu3/finance/studentPaymentDetails/single-studentPaymentDetails-list";
	}
	
	@RequestMapping("/edu3/finance/studentpayment/pay_input.html")
	public String payInput(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		model.addAttribute("studentPaymentId", ExStringUtils.defaultIfEmpty(request.getParameter("studentPaymentId"), ""));
		model.addAttribute("pageNum", ExStringUtils.defaultIfEmpty(request.getParameter("pageNum"), "1"));
//		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
//		model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType"));
		return "/edu3/finance/studentPaymentDetails/pay-form";
	}
	
	/**
	 * 获取学生缴费明细记录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/list.html")
	public String getStudentPaymentDetails(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
//			objPage.setOrderBy(" spd.studentInfo.studyNo,spd.operateDate ");
//			objPage.setOrder(Page.DESC);
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				String brSchoolId = cureentUser.getOrgUnit().getResourceid();
				condition.put("brSchool", brSchoolId);//判断用户是否为校外学习中心
				model.addAttribute("linkageQuerySchoolId", brSchoolId);
			}
			
			objPage = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(objPage,condition);
			model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
			model.addAttribute("studentPaymentDetailsList", objPage);
		//	model.addAttribute("studentPaymentInfo", studentPayment);
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取学生缴费明细列表出错", e);
		} 
		
		return "/edu3/finance/studentPaymentDetails/studentpaymentdetails-list";
	}
	
	
	/**
	 * 打印前返回查询所得的数量,向用户获取收据号
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/quaryPrint.html")
	public String queryForPrint(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
//			objPage.setOrderBy(" spd.studentInfo.studyNo,spd.operateDate ");
//			objPage.setOrder(Page.DESC);
			// 处理参数
			condition.put("print", "Y");
			List<StudentPaymentDetails> spdList = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
//			objPage = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(objPage,condition);
		//	model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
//			model.addAttribute("total", objPage.getTotalCount());
		//	model.addAttribute("studentPaymentInfo", studentPayment);
			model.addAttribute("total", spdList.size());
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取学生缴费明细列表出错", e);
		} 
		
		return "/edu3/finance/studentPaymentDetails/studentpaymentdetails-count";
	}
	
	/**
	 * 打印前返回查询所得的数量,向用户获取收据号
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/finance/studentPaymentDetails/printListView.html"})
	public String printStudentPaymentDetailsView(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		try {
			Condition2SQLHelper.addMapFromResquestByIterator(request, model);
			String drawer = (String)model.get("drawer");
			if(ExStringUtils.isNotBlank(drawer)){
				model.addAttribute("drawer",URLEncoder.encode(drawer, "UTF-8"));
			}
		} catch (Exception e) {
			logger.error("打印学生缴费明细", e);
		} 
		
		return "/edu3/finance/studentPaymentDetails/studentpaymentdetails-printview";
	}
	
	/**
	 * 学生发票签领表预览
	 * @param request
	 * @param response
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/finance/studentPaymentDetails/printBillView.html"})
	public String printBillView(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		try {
			Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		} catch (Exception e) {
			logger.error("打印学生缴费明细", e);
		} 
		model.addAttribute("url", "/edu3/finance/studentPaymentDetails/printBill.html");
		return "/edu3/finance/studentPaymentDetails/printBill-view";
	}
	
	/**
	 * 检查收据号重复
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/checkReceiptNumber.html")
	public void checkReceiptNumber(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String receiptNumber_begin = request.getParameter("receiptNumber_begin");// 收据号开始
			String receiptNumber_end = request.getParameter("receiptNumber_end");// 收据号结尾
			if(ExStringUtils.isNotEmpty(receiptNumber_begin)) {
				condition.put("receiptNumber_begin", receiptNumber_begin);
			}
			if(ExStringUtils.isNotEmpty(receiptNumber_end)) {
				condition.put("receiptNumber_end", receiptNumber_end);
			}else if(ExStringUtils.isNotEmpty(receiptNumber_begin)){
				condition.put("receiptNumber_end", receiptNumber_begin);
			}
			
			// 增加检验票据号是否重复
			List<StudentPaymentDetails> list = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
			if(list!=null && list.size()>0){
				StringBuffer message = new StringBuffer();
				for(StudentPaymentDetails details : list){
					message.append("已经存在票据号:"+details.getReceiptNumber()+"</br>");
				}
				map.put("statusCode", 300);
				map.put("message", message.toString());
			}else{
				map.put("statusCode", 200);
			}
		}catch (Exception e) {
			logger.error("检查票据号重复出错:{}",e.fillInStackTrace());		
			map.put("statusCode", 300);
			map.put("message", "检查票据号重复出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	
	/**
	 * 打印学生缴费明细列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/printList.html")
	public void printStudentPaymentDetails(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
//			objPage.setOrderBy(" spd.studentInfo.studyNo,spd.operateDate ");
//			objPage.setOrder(Page.DESC);
			
			// 处理参数
			condition.put("print", "Y");
			String _drawerString = (String) condition.get("drawer");
			String _receiptNumber_begin = (String) condition.get("receiptNumber_begin");
			condition.remove("drawer");
			condition.remove("receiptNumber_begin");
			condition.remove("receiptNumber_end");
			List<StudentPaymentDetails> list = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
			
			int receiptNumber_begin = Integer.parseInt(_receiptNumber_begin);
			//中文出现乱码,解密
			condition.put("drawer",URLDecoder.decode(_drawerString,"UTF-8"));

//			SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
			//先修改,然后打印,增加修改开票人
			Date now = new Date();
			for(StudentPaymentDetails detail : list){
				detail.setDrawer(condition.get("drawer")!=null?(String)condition.get("drawer"):"");
				detail.setPrintDate(now);
				//detail.setReceiptNumber(config.getParamValue()+receiptNumber_begin);//保存不加前缀,用纯数字,然后去判断重复
				detail.setReceiptNumber(receiptNumber_begin+"");
				receiptNumber_begin++;
			}
			List<PayDetailsVO> payDetailsVOs = createPrintPayDetailsVO(list);
			
			//打印
			Map<String, Object> param = new HashMap<String, Object>();
			JasperPrint jasperPrint   = null;//输出的报表

			for (PayDetailsVO vo : payDetailsVOs) {
				List<String> temp = new ArrayList<String>();
				temp.add(vo.getCheckPayable());
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(temp);
				String reprotFile = "";
				reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						File.separator+"finance"+File.separator+"paymentDetails.jasper"),"utf-8");
				
				
				param.put("type","4");//默认类型为成人大学生,根据票据为1,2,3,4,5,6,7
				param.put("payMethod",vo.getPrintPayMethod());
				param.put("payable",vo.getCheckPayable());			
				param.put("className",vo.getClassName());
				param.put("year",vo.getPrintYear());
				param.put("month",vo.getPrintMonth());
				param.put("day",vo.getPrintDay());
				//大写金额
				if(ExStringUtils.isNotBlank(vo.getPrintUpperMoney())){
					param.put("wan",vo.getPrintUpperMoney().substring(0, 1));
					param.put("qian",vo.getPrintUpperMoney().substring(1, 2));
					param.put("bai",vo.getPrintUpperMoney().substring(2, 3));
					param.put("shi",vo.getPrintUpperMoney().substring(3, 4));
					param.put("yuan",vo.getPrintUpperMoney().substring(4, 5));
					param.put("jiao",vo.getPrintUpperMoney().substring(5, 6));
					param.put("fen",vo.getPrintUpperMoney().substring(6, 7));
				}
				
				param.put("yearInfo",vo.getYear()+"学年");
				param.put("brschool",vo.getBrSchool());
				param.put("payAmount",vo.getPrintPayAmount());
				param.put("operatorName",vo.getOperatorName());
				param.put("drawer",condition.get("drawer")!=null?(String)condition.get("drawer"):"");
					
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, param,dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, param,dataSource); // 填充报表
				}
			}
			if (null!=jasperPrint) {
				studentPaymentDetailsService.batchSaveOrUpdate(list);
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
				
			
		} catch (Exception e) {
			logger.error("打印学生缴费明细出错", e);
		} 
		
	}
	
	/**
	 * 打印学生发票签领表
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/printBill.html")
	public void prinBill(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		JasperPrint jasperPrint = null;
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			List<StudentPaymentDetails> list = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
			int i = 0;
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			for (StudentPaymentDetails vo : list) {
				StudentInfo studentInfo = vo.getStudentInfo();
				if(studentInfo!=null){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("order", String.valueOf(++i));
					map.put("unitName", studentInfo.getBranchSchool().getUnitName());
					map.put("gradeName", studentInfo.getGrade().getYearInfo().getFirstYear().toString());
					map.put("studyNo", studentInfo.getStudyNo());
					map.put("stuName", studentInfo.getStudentName());
					map.put("majorName", studentInfo.getMajor().getMajorName());
					map.put("operateDate", ExDateUtils.formatDateStr(vo.getOperateDate(), 1));
					dataList.add(map);
				}
			}
			String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("title", schoolName+schoolConnectName+"学生发票签领表");
			String reportPath = File.separator+"reports"+File.separator+"finance"+File.separator+"receiptlist_student.jasper";
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			File reprot_file   = new File(reprotFile);
			JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataList);	
			jasperPrint = JasperFillManager.fillReport(reprot_file.getPath(),param,dataSource); // 填充报表
				
		} catch (Exception e) {
			logger.error("打印学生缴费明细出错", e);
		} 
		if (null!=jasperPrint) {
			renderStream(response, jasperPrint);
		}else {
			renderHtml(response,"缺少打印数据！");
		}
	}
	
	/**
	 * 创建打印缴费VO
	 * @param paymentDetails
	 * @return
	 */
	private List<PayDetailsVO> createPrintPayDetailsVO(List<StudentPaymentDetails> paymentDetails) {
		List<PayDetailsVO> payDetailsVOs = new ArrayList<PayDetailsVO>();
		for(StudentPaymentDetails paymentDetail : paymentDetails) {
			PayDetailsVO payDetailsVO = new PayDetailsVO();
//			payDetailsVO.setClassName(paymentDetail.getStudentInfo().getClasses()!=null?paymentDetail.getStudentInfo().getClasses().getClassname():"");
			payDetailsVO.setClassName(paymentDetail.getClassName()!=null?paymentDetail.getClassName():"");
			payDetailsVO.setPrintPayMethod("付款方式："+JstlCustomFunction.dictionaryCode2Value("CodePaymentMethod", paymentDetail.getPaymentMethod()));
			payDetailsVO.setPrintYear(JstlCustFunction.getTimeByPattern(paymentDetail.getOperateDate(), "yyyy"));
			payDetailsVO.setPrintMonth(JstlCustFunction.getTimeByPattern(paymentDetail.getOperateDate(), "MM"));
			payDetailsVO.setPrintDay(JstlCustFunction.getTimeByPattern(paymentDetail.getOperateDate(), "dd"));
			payDetailsVO.setPrintUpperMoney(JstlCustFunction.getCN(new BigDecimal(paymentDetail.getPayAmount())));
			payDetailsVO.setPrintPayAmount((new BigDecimal(paymentDetail.getPayAmount()).setScale(2,RoundingMode.HALF_UP)).toString());
			if(ExStringUtils.isNotBlank(paymentDetail.getCheckPayable())){
				payDetailsVO.setCheckPayable(paymentDetail.getCheckPayable());
			}else{
				payDetailsVO.setCheckPayable(paymentDetail.getStudentInfo().getStudentName()+"("+paymentDetail.getStudentInfo().getStudyNo()+")");
			}
			payDetailsVO.setYear(paymentDetail.getYear());
			payDetailsVO.setOperatorName(paymentDetail.getOperatorName());
			payDetailsVO.setBrSchool(paymentDetail.getStudentInfo().getBranchSchool().getUnitName());
			payDetailsVOs.add(payDetailsVO);
		}
		
		return payDetailsVOs;
	}
	
	/**
	 * 撤销票据号,开票人,打印时间
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/cancelPrint.html")
	public void cancelPrintStudentPaymentDetail(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			// 处理参数
			if(condition.containsKey("detailIds")){
				List<StudentPaymentDetails> list = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
				for(StudentPaymentDetails detail:list){
					detail.setReceiptNumber(null);
					detail.setDrawer(null);
					detail.setPrintDate(null);
				}
				studentPaymentDetailsService.batchSaveOrUpdate(list);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.REPEAL,"打印收据：撤销打印：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "撤销成功！");
			}	

		} catch (Exception e) {
			logger.error("撤销票据号,开票人,打印时间出错:{}",e.fillInStackTrace());		
			map.put("statusCode", 300);
			map.put("message", "撤销出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 获取学生缴费明细列表简明报表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/reportList.html")
	public String getStudentPaymentDetailReports(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			//objPage.setOrderBy(" spd.studentInfo.branchSchool.unitCode,spd.studentInfo.classic.classicCode,spd.studentInfo.major.majorCode ");
			//objPage.setOrder(Page.ASC);
			// 处理参数
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				String brSchoolId = cureentUser.getOrgUnit().getResourceid();
				condition.put("brSchool", brSchoolId);//判断用户是否为校外学习中心
				model.addAttribute("linkageQuerySchoolId", brSchoolId);
			}
			SysConfiguration config = CacheAppManager.getSysConfigurationByCode("feeReceiptNumberPrefix");
			
			objPage = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(objPage,condition);
			if(condition.containsKey("drawer")){
				condition.put("drawer",URLDecoder.decode((String) condition.get("drawer"),"UTF-8"));
			}
			model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
			model.addAttribute("studentPaymentDetailsList", objPage);
		//	model.addAttribute("studentPaymentInfo", studentPayment);
			model.addAttribute("prefix", "NULL".equals(config.getParamValue())?"":config.getParamValue());
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取学生缴费明细列表简明报表出错", e);
		} 
		
		return "/edu3/finance/studentPaymentDetails/studentpaymentdetails-reportList";
	}
	
	/**
	 * 导出收据打印明细
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/export.html")
	public void exportPrintReceipt(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			String type = request.getParameter("type");
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				String brSchoolId = cureentUser.getOrgUnit().getResourceid();
				condition.put("brSchool", brSchoolId);//判断用户是否为校外学习中心
				model.addAttribute("linkageQuerySchoolId", brSchoolId);
			}
			
			List<StudentPaymentDetails> list = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(condition);
			Double total = 0.0;//总共学费
			for(StudentPaymentDetails detail:list){
				total+=detail.getShouldPayAmount();
			}
			
			List<StudentPaymentDetailsExportVo> voList = covertToExportVO(list);	
			
			StudentPaymentDetailsExportVo end = new StudentPaymentDetailsExportVo();
			end.setUnitName("总计");
			end.setPayAmount(total);
			//voList.add(end);
			
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
	 	    GUIDUtils.init();
	 	    //导出
			File excelFile = null;
			String header = "";
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			if("record".equals(type)){
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeTeachingType");
				dictCodeList.add("CodePaymentMethod");
				dictCodeList.add("CodeStudentStatus");
				header = "学生缴费明细记录表";
				exportExcelService.initParamsByfile(disFile, "studentpaymentdetails_exprot_record", list, dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE), null	, null);
			}else {
				header = "打印收据明细表";
				exportExcelService.initParmasByfile(disFile, "studentpayment_details_exprot", voList,condition);
			}
			//初始化配置参数
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			exportExcelService.getModelToExcel().setHeader(header);
			//exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 7, templateMap);
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			
			//String downloadFileName = "收据打印信息表.xls";
			String downloadFilePath = excelFile.getAbsolutePath();
						
			downloadFile(response, header+".xls",downloadFilePath,true);	
	 	 			
		} catch (Exception e) {
			logger.error("收据打印信息表", e);
			renderHtml(response, "<script>alert('导出excel文件出错,请联系管理员')</script>");
		} 
	}
	
	/**
	 * 打印收据列表
	 * @param request
	 * @param response
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/finance/studentPaymentDetails/printReceiptList.html"})
	public String getPrintReceipt(HttpServletRequest request,HttpServletResponse response, Page objPage,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				String brSchoolId = cureentUser.getOrgUnit().getResourceid();
				condition.put("brSchool", brSchoolId);//判断用户是否为校外学习中心
				model.addAttribute("linkageQuerySchoolId", brSchoolId);
			}
			
			objPage = studentPaymentDetailsService.findStudentPaymentDetailsByCondition(objPage,condition);
			model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
			model.addAttribute("printReceiptList", objPage);
		//	model.addAttribute("studentPaymentInfo", studentPayment);
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取打印收据信息出错", e);
		} 
		
		return "/edu3/finance/studentPaymentDetails/printReceiptList";
	}
	
	/**
	 * 收费汇总表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/finance/studentPaymentDetails/summaryList.html"})
	public String getChargeSummaryList (HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				OrgUnit unit = cureentUser.getOrgUnit();
				String brSchoolId = unit.getResourceid();
				condition.put("brSchool", brSchoolId);//判断用户是否为校外学习中心
				model.addAttribute("isBrschool", true);
				model.addAttribute("brSchoolName", unit.getUnitCode()+"-"+unit.getUnitName());
				model.addAttribute("linkageQuerySchoolId", brSchoolId);
			}
			// 学费
			condition.put("chargingItems", "tuition");
			List<Map<String, Object>> list = studentPaymentDetailsService.findChargeSummayForMap(condition);
			List<ChargeSummaryVo> summaryList = getChargeSummaryVoList(list,model);
			model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
			model.addAttribute("summaryList", summaryList);
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取收费汇总表出错", e);
		} 
		
		return "/edu3/finance/studentPaymentDetails/chargeSummaryList";
	}
	
	/**
	 * 退费汇总表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/chargeBackList.html")
	public String getChargeBackFee (HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			String currentFeeStr = ExStringUtils.trim(request.getParameter("currentFee"));
			BigDecimal currentFee = null;
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				String brSchoolId = cureentUser.getOrgUnit().getResourceid();
				condition.put("brSchool", brSchoolId);
				//判断用户是否为校外学习中心
				model.addAttribute("isBrschool", true);
			}else {
				model.addAttribute("isBrschool", false);
			}
			condition.put("chargeBack", "Y");
			if(ExStringUtils.isNotBlank(currentFeeStr)){
				currentFee = BigDecimal.valueOf(Double.parseDouble(currentFeeStr));
				condition.put("currentFee", currentFee);
			}
			// 学费
			condition.put("chargingItems", "tuition");
			// 退费
			condition.put("processType", "returnPremium");
			List<Map<String, Object>> list = returnPremiumService.findReturnPremiumForMap(condition);
			List<ChargeSummaryVo> chargeBackList = getChargeSummaryVoList(list,model);
			model.addAttribute("payType", CacheAppManager.getSysConfigurationByCode("payment.payType").getParamValue());
			model.addAttribute("chargeBackList", chargeBackList);
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取退费汇总表出错", e);
		} 
		return "/edu3/finance/studentPaymentDetails/chargeBackList";
	}
	
	/**
	 * 将收费汇总表map对象转为vo对象
	 * @param list
	 * @return
	 * @throws ParseException 
	 */
	private List<ChargeSummaryVo> getChargeSummaryVoList(List<Map<String, Object>> list,ModelMap model) {
		List<ChargeSummaryVo> summaryList = new ArrayList<ChargeSummaryVo>();
		DecimalFormat formatDouble = new DecimalFormat("#,##0.00");
		String payType = "";
		ChargeSummaryVo summaryVo = null;
		int sumPaidCount = 0;//合计总笔数（总人数）
		int sumStuCount = 0;
		BigDecimal sumBKAmount = BigDecimal.valueOf(0);//合计本科学费
		BigDecimal sumZKAmount = BigDecimal.valueOf(0);//合计专科学费
		if(list!=null && list.size()>0){
			for (int i=0;i<list.size();i++) {
				Map<String, Object> map = list.get(i);
				String paymentMethod = map.get("paymentmethod")==null?"-1":map.get("paymentmethod").toString();
				String classicName = map.get("classicname").toString();
				String totalNum = map.get("paidcount").toString();
				String stuCount = map.get("stucount").toString();
				BigDecimal paidamount = (BigDecimal) map.get("paidamount");
				BigDecimal _beginNum = (BigDecimal) map.get("minnumber");
				BigDecimal _endNum = (BigDecimal) map.get("maxnumber");
				Date beginDate =  (Date) (map.get("mindate")==null?null:map.get("mindate"));
				Date endDate = (Date) (map.get("maxdate")==null?null:map.get("maxdate"));
				if(!payType.equals(paymentMethod)){
					if(i!=0){
						summaryList.add(summaryVo);
					}
					summaryVo = new ChargeSummaryVo();
					summaryVo.setPaymentMethod(paymentMethod);
					summaryVo.setBeginReceiptNum(_beginNum!=null?_beginNum.toString():"");
					summaryVo.setEndReceiptNum(_endNum!=null?_endNum.toString():"");
				}else{
					if(ExStringUtils.isNotEmpty(summaryVo.getBeginReceiptNum())&&_beginNum !=null &&new BigDecimal(summaryVo.getBeginReceiptNum()).compareTo(_beginNum)>0){
						summaryVo.setBeginReceiptNum(_beginNum!=null?_beginNum.toString():"");
					}
					if(ExStringUtils.isNotEmpty(summaryVo.getEndReceiptNum())&&_endNum !=null && new BigDecimal(summaryVo.getEndReceiptNum()).compareTo(_endNum)<0){
						summaryVo.setEndReceiptNum(_endNum!=null?_endNum.toString():"");
					}
				}
				summaryVo.setTotalNum(summaryVo.getTotalNum()+Integer.parseInt(totalNum));
				summaryVo.setStuCount(summaryVo.getStuCount()+Integer.parseInt(stuCount));
				if("本科".equals(classicName) || classicName.endsWith("本")){
					summaryVo.setUndergraduateFee(paidamount);
					sumBKAmount=sumBKAmount.add(paidamount);
				}else if("专科".equals(classicName) || classicName.endsWith("专")) {
					summaryVo.setEducationFee(paidamount);
					sumZKAmount=sumZKAmount.add(paidamount);
				}
				try {
					if(beginDate!=null){
						if(summaryVo.getBeginDate()!=null && summaryVo.getBeginDate().before(beginDate)){
						}else {
							summaryVo.setBeginDate(beginDate);
						}
					}
					if(endDate!=null){
						if(summaryVo.getEndDate()!=null && summaryVo.getEndDate().after(endDate)){
						}else {
							summaryVo.setEndDate(endDate);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("转换日期出错", e);
				}
				sumPaidCount+=Integer.valueOf(totalNum);
				sumStuCount+=Integer.valueOf(stuCount);
				payType=paymentMethod;
			}
			summaryList.add(summaryVo);
			model.addAttribute("sumPaidCount", sumPaidCount);
			model.addAttribute("sumStuCount", sumStuCount);
			model.addAttribute("sumBKAmount", sumBKAmount.equals(BigDecimal.valueOf(0)) ?"0":formatDouble.format(sumBKAmount));
			model.addAttribute("sumZKAmount", sumZKAmount.equals(BigDecimal.valueOf(0)) ?"0":formatDouble.format(sumZKAmount));
			model.addAttribute("sumAmount", sumBKAmount.equals(BigDecimal.valueOf(0)) ?"0":formatDouble.format(sumZKAmount.add(sumBKAmount)));
		}
		return summaryList;
	}

	/**
	 * 收费汇总表导出及打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/finance/studentPaymentDetails/summaryList/export.html","/edu3/finance/studentPaymentDetails/summaryList/print-view.html"})
	public String chargeSummaryExportAndPrint(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		// 参数处理
 		String flag = request.getParameter("flag");
 		String currentFeeStr = ExStringUtils.trim(request.getParameter("currentFee"));
		String chargeBack = ExStringUtils.trim(request.getParameter("chargeBack"));
		BigDecimal currentFee = null;
 		try {
	 		SysConfiguration sysConfiguration = sysConfigurationService.findOneByCode("graduateData.schoolName");
	 		String schoolName = sysConfiguration.getParamValue();
	 		
	 		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			/*if(ExStringUtils.isNotBlank(currentFeeStr) && !"undefined".equals(currentFeeStr)){
				currentFee = BigDecimal.valueOf(Double.parseDouble(currentFeeStr));
				condition.put("currentFee", currentFee);
				model.addAttribute("currentFee",currentFee);
			}*/
			if("Y".equals(chargeBack)){
				condition.put("chargeBack", chargeBack);
				model.addAttribute("chargeBack",chargeBack);
			}
			
	 		if("print".equals(flag)){
	 			Condition2SQLHelper.addMapFromResquestByIterator(request, model);
				return "/edu3/finance/studentPaymentDetails/printChargeSummary";
			}else if ("export".equals(flag)) {
				// 学费
				condition.put("chargingItems", "tuition");
				String fileName = null;
				List<Map<String, Object>> list = null;
				if("Y".equals(chargeBack)){
					// 退费
					condition.put("processType", "returnPremium");
					fileName = "退费汇总表";
					list = returnPremiumService.findReturnPremiumForMap(condition);
				}else {
					fileName = "收费汇总表";
					list = studentPaymentDetailsService.findChargeSummayForMap(condition);
				}
				fileName = URLEncoder.encode(schoolName+fileName+".xls", "UTF-8");
				List<ChargeSummaryVo> summaryList = getChargeSummaryVoList(list,model);
				request.getSession().setAttribute("summaryList", summaryList);
				response.setContentType("application/vnd.ms-excel");
				//attachment
			  	response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			  	return "/edu3/finance/studentPaymentDetails/chargeSummaryExcel";
			}
 		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 打印收费汇总表
	 * @param request
	 * @param response
	 * @param model
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 * @throws ServerException 
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/chargeSummary/print.html")
	public void printChargeSummary(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws UnsupportedEncodingException, ParseException, ServerException{
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String sumPaidcount = request.getParameter("sumPaidcount");
		String sumStuCount = request.getParameter("sumStuCount");
		String sumBKAmount = request.getParameter("sumBKAmount");
		String sumZKAmount = request.getParameter("sumZKAmount");
		String sumAmount = request.getParameter("sumAmount");
		String currentFeeStr = ExStringUtils.trim(request.getParameter("currentFee"));
		String chargeBack = ExStringUtils.trim(request.getParameter("chargeBack"));
		BigDecimal currentFee = null;
		if(ExStringUtils.isNotBlank(currentFeeStr)){
			currentFee = BigDecimal.valueOf(Double.parseDouble(currentFeeStr));
			condition.put("currentFee", currentFee);
		}
		// 学费
		condition.put("chargingItems", "tuition");
		List<Map<String, Object>> list = null;
		if("Y".equals(chargeBack)){
			// 退费
			condition.put("processType", "returnPremium");
			list = returnPremiumService.findReturnPremiumForMap(condition);
		}else {
			condition.remove("chargeBack");
			list = studentPaymentDetailsService.findChargeSummayForMap(condition);
		}
		
		List<ChargeSummaryVo> summaryList = getChargeSummaryVoList(list,model);
		List<Map<String, Object>> datelist = new ArrayList<Map<String,Object>>();
		DecimalFormat formatDouble = new DecimalFormat("#,###.00");
		for (ChargeSummaryVo vo : summaryList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("payMethod", vo.getPaymentMethod());
			map.put("stuCount", vo.getStuCount());
			map.put("totalNum", vo.getTotalNum());
			map.put("bkFee", vo.getUndergraduateFee().compareTo(BigDecimal.ZERO)==0?"0":formatDouble.format(vo.getUndergraduateFee()));
			map.put("zkFee", vo.getEducationFee().compareTo(BigDecimal.ZERO)==0?"0":formatDouble.format(vo.getEducationFee()));
			map.put("totalFee", vo.getTotalFee().compareTo(BigDecimal.ZERO)==0?"0":formatDouble.format(vo.getTotalFee()));
			map.put("beginNum", vo.getBeginReceiptNum());
			map.put("endNum", vo.getEndReceiptNum());
			map.put("beginDate", vo.getBeginDate());
			map.put("endDate", vo.getEndDate());
			datelist.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sumPaidcount", sumPaidcount);
		map.put("sumStuCount", sumStuCount);
		map.put("sumBKAmount", sumBKAmount);
		map.put("sumZKAmount", sumZKAmount);
		map.put("sumAmount", sumAmount);
		try {
			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(datelist);
			String jasperFile = null;
			jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"finance"+File.separator+"chargeSummary.jasper"),"utf-8");
			map.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/finance"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"<script>alert('缺少打印数据！')</script>");
			}
		} catch (Exception e) {
			logger.error("打印出错：{}"+e.fillInStackTrace());
			renderHtml(response,"<script>alert('打印出错："+e.getMessage()+"')</script>");
		}	
	}
	
	
	/*
	 * 转化成导出vo
	 */
	private List<StudentPaymentDetailsExportVo> covertToExportVO(List<StudentPaymentDetails> list) {
		List<StudentPaymentDetailsExportVo> returnList = new ArrayList<StudentPaymentDetailsExportVo>();
		int i = 1;
		for(StudentPaymentDetails detail : list){
			StudentPaymentDetailsExportVo vo = new StudentPaymentDetailsExportVo();
			
			vo.setCardType(JstlCustomFunction.dictionaryCode2Value("CodeCardType", detail.getCardType()));
			vo.setReceiptNumber(detail.getReceiptNumber());
			vo.setYear(detail.getYear());
			vo.setPaymentMethod(JstlCustomFunction.dictionaryCode2Value("CodePaymentMethod", detail.getPaymentMethod()));
			vo.setUnitName(detail.getStudentInfo().getBranchSchool().getUnitName());
			vo.setStudentNo(detail.getStudentInfo().getStudyNo());
			vo.setStudentName(detail.getStudentInfo().getStudentName());
			vo.setClassic(detail.getStudentInfo().getClassic().getClassicName());
			vo.setClasses(detail.getStudentInfo().getClasses()!=null?detail.getStudentInfo().getClasses().getClassname():"");
			vo.setDrawer(detail.getDrawer());
			vo.setOperateDate(detail.getOperateDate());
			vo.setPayAmount(detail.getPayAmount());
			vo.setPrintDate(detail.getPrintDate());
			vo.setChargeMoney(detail.getChargeMoney());
			vo.setExamCertificateNo(detail.getStudentInfo().getExamCertificateNo());
			vo.setShowOrder(i);
			i++;
			
			returnList.add(vo);
		}
		return returnList;
	}
	
	/**
	 * 跳转到下载对账文件页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentPaymentDetails/downloadCheckFile.html")
	public String downloadCheckFileForm(HttpServletRequest request,HttpServletResponse response) throws WebException{
		
		return "/edu3/finance/studentPaymentDetails/downloadCheckFile";
	}
}