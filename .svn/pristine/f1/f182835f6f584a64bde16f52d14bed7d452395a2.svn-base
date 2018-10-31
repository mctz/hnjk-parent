package com.hnjk.edu.finance.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.model.ReturnPremium;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IAnnualFeesService;
import com.hnjk.edu.finance.service.IReturnPremiumService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;

/**
 * 学生年度缴费管理.
 * <code>AnnualFeesController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @version 1.0
 */
@Controller
public class AnnualFeesController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -6216762678106007125L;

	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("returnPremiumService")
	private IReturnPremiumService returnPremiumService;
	
	@Autowired
	@Qualifier("annualFeesService")
	private IAnnualFeesService annualFeesService;	
	
	/**
	 * 获取学生年度缴费记录list
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/annualFees/list.html")
	public String getList(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page page) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		try {
			condition=getAnnualFeesCondition(request);
			
			page = annualFeesService.findAnnualFeesByCondition(condition,page);					
			model.addAttribute("annualFeesList", page);
			
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("获取学生年度缴费记录出错", e);
		}
		
		return "/edu3/finance/annualFees/list";
	}
	
	/**
	 * 获取request里的参数转换成condition
	 * @param request
	 * @return
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 */
	private Map<String, Object> getAnnualFeesCondition(HttpServletRequest request) throws ParseException, UnsupportedEncodingException {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		String brSchool = request.getParameter("brSchool");
		String majorid = request.getParameter("majorid");
		String classicid = request.getParameter("classicid");
		String gradeid = request.getParameter("gradeid");
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));		
		String classesId = request.getParameter("classesId");
		String studentStatus = request.getParameter("studentStatus");//学籍状态
		String yearId = request.getParameter("yearId");//学年
		String chargeStatus = request.getParameter("chargeStatus");
		String chargingItems = request.getParameter("chargingItems");
		
		if(ExStringUtils.isNotEmpty(chargeStatus)){
			condition.put("chargeStatus", chargeStatus);
		}
		if(ExStringUtils.isNotEmpty(yearId)){
			condition.put("yearId", yearId);
		}
		if(ExStringUtils.isNotEmpty(brSchool)){
			condition.put("brSchool", brSchool);
		}
		if(ExStringUtils.isNotEmpty(majorid)){
			condition.put("majorid", majorid);
		}
		if(ExStringUtils.isNotEmpty(classicid)){
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotEmpty(gradeid)){
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(name)){
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)){
			condition.put("studentStatus", studentStatus);
		}
		if(ExStringUtils.isNotEmpty(chargingItems)){
			condition.put("chargingItems", chargingItems);
		}
		
		return condition;
	}
	
	/**
	 * 跳转至修改应缴金额
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/annualFees/prepareUpdate.html")
	public String prepareUpdate(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer("");
		try {
			String resourceid = request.getParameter("annualFeesId");
			String type = request.getParameter("type");//修改的类型,退费,修改应缴金额
			if(ExStringUtils.isBlank(resourceid) || ExStringUtils.isBlank(type)){
				logger.error("参数为空");
				message.append("功能错误,请联系管理员");
			}else{
				condition.put("resourceid", resourceid);
				if("refund".equals(type)){
					condition.put("studentStatus", "13");//退学的才给退费
				}
				List<AnnualFees> list = annualFeesService.findAnnualFeesByCondition(condition);
				if(list!=null && list.size()>0){
					model.addAttribute("annualFees", list.get(0));
				}else if("refund".equals(type)){
					message.append("只有退学的学生可以退费");
				}
			}
			
			model.addAttribute("message", message.toString());
			condition.put("type", type);
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			logger.error("退费出错", e);
		}
		return "/edu3/finance/annualFees/updateRecpayFee";
	}
	
	/**
	 * 确认修改应缴金额,退费操作
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/annualFees/comfirmUpdate.html")
	public void refundComfirm(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		String type = request.getParameter("type");
		if(ExStringUtils.isBlank(type)){
			logger.error("修改出错,type参数错误");
			map.put("statusCode", 300);
			map.put("message", "修改失败:<br/>请联系管理员");
		}else if("update".equals(type)){//修改应缴金额
			try {
				String resourceid = request.getParameter("resourceid");
				String recpayFee = request.getParameter("recpayFee");
				if(ExStringUtils.isBlank(resourceid) || ExStringUtils.isBlank(recpayFee)){
					logger.error("参数错误");
					map.put("statusCode", 300);
					map.put("message", "修改失败:参数错误<br/>请联系管理员");
				}else{
					condition.put("resourceid", resourceid);
					List<AnnualFees> list = annualFeesService.findAnnualFeesByCondition(condition);
					if(list!=null&&list.size()>0){
						AnnualFees annualFees = list.get(0);
						if(annualFees.getFacepayFee()>Double.parseDouble(recpayFee)){
							map.put("statusCode", 300);
							map.put("message", "修改失败:应缴金额小于已缴金额<br/>请联系管理员");
						}else{
							condition.clear();
							condition.put("studyNo", annualFees.getStudentInfo().getStudyNo());
							List<StudentPayment> paymentList = studentPaymentService.findStudentPaymentByCondition(condition);
							if(paymentList!=null&&paymentList.size()>0){
								StudentPayment studentPayment = paymentList.get(0);
								studentPayment.setRecpayFee(studentPayment.getRecpayFee()+Double.parseDouble(recpayFee)-annualFees.getRecpayFee());
								//根据修改的修改缴费状态
								if(studentPayment.getFacepayFee()!=0){
									if(studentPayment.getFacepayFee()>=studentPayment.getRecpayFee()){
										studentPayment.setChargeStatus("1");
									}else{
										studentPayment.setChargeStatus("-1");
									}
								}
								
								annualFees.setRecpayFee(Double.parseDouble(recpayFee));
								
								if(annualFees.getFacepayFee()!=0){
									if(annualFees.getFacepayFee()>=annualFees.getRecpayFee()){
										annualFees.setChargeStatus(1);
									}else{
										annualFees.setChargeStatus(-1);
									}
								}
								
								studentPaymentService.saveOrUpdate(studentPayment);
								annualFeesService.saveOrUpdate(annualFees);
								UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.UPDATE,"学生年度缴费管理--修改应缴金额：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
								map.put("statusCode", 200);
								map.put("message", "修改成功:<br/>");
								map.put("reloadUrl", request.getContextPath()+"/edu3/finance/annualFees/list.html");
							}else{
								map.put("statusCode", 300);
								map.put("message", "修改失败:查不到数据<br/>请联系管理员");
							}
						}
					}else{
						map.put("statusCode", 300);
						map.put("message", "修改失败:查不到数据<br/>请联系管理员");
					}
				}
			}catch (Exception e) {
				logger.error("修改出错", e);
				map.put("statusCode", 300);
				map.put("message", "修改失败:<br/>"+e.getLocalizedMessage()+"请联系管理员");
			}
		}else if("refund".equals(type)){//退费操作
			try {
				String resourceid = request.getParameter("resourceid");
				String recpayFee = request.getParameter("recpayFee");
				String refund = request.getParameter("refund");
				String facepayFee = request.getParameter("facepayFee");
				String paymentMethod = request.getParameter("paymentMethod");
				
				if(ExStringUtils.isBlank(resourceid) || ExStringUtils.isBlank(refund) || ExStringUtils.isBlank(recpayFee)|| ExStringUtils.isBlank(paymentMethod)){
					logger.error("参数错误");
					map.put("statusCode", 300);
					map.put("message", "退费失败:参数错误<br/>请联系管理员");
				}else{
					condition.put("resourceid", resourceid);
					List<AnnualFees> list = annualFeesService.findAnnualFeesByCondition(condition);
					if(list!=null&&list.size()>0){
						AnnualFees annualFees = list.get(0);
						if(annualFees.getRecpayFee()<Double.parseDouble(recpayFee)){
							map.put("statusCode", 300);
							map.put("message", "退费失败:应缴金额小于原应缴金额<br/>请联系管理员");
						}else{
							condition.clear();
							condition.put("studyNo", annualFees.getStudentInfo().getStudyNo());
							List<StudentPayment> paymentList = studentPaymentService.findStudentPaymentByCondition(condition);
							if(paymentList!=null&&paymentList.size()>0){
								//设置总的缴费
								StudentPayment studentPayment = paymentList.get(0);
								studentPayment.setReturnPremiumFee(Double.parseDouble(refund)+(studentPayment.getReturnPremiumFee()!=null?studentPayment.getReturnPremiumFee():0.0));
								studentPayment.setRecpayFee(studentPayment.getRecpayFee()-annualFees.getRecpayFee()+Double.parseDouble(recpayFee));
								//退费保留已缴金额studentPayment.setFacepayFee(studentPayment.getFacepayFee()-Double.parseDouble(refund));
								//设置年度缴费
								annualFees.setRecpayFee(Double.parseDouble(recpayFee));
								annualFees.setReturnPremiumFee(Double.parseDouble(refund)+(annualFees.getReturnPremiumFee()!=null?annualFees.getReturnPremiumFee():0.0));
								//退费保留已缴金额annualFees.setFacepayFee(annualFees.getFacepayFee()-Double.parseDouble(refund));
								
								//根据修改的修改缴费状态
								if(studentPayment.getFacepayFee()!=0){
									if((studentPayment.getFacepayFee()+studentPayment.getPayAmount())>=studentPayment.getRecpayFee()){
										studentPayment.setChargeStatus("1");
									}else{
										studentPayment.setChargeStatus("-1");
									}
								}
								if(annualFees.getFacepayFee()!=0){
									if(annualFees.getFacepayFee()>=annualFees.getRecpayFee()){
										annualFees.setChargeStatus(1);
									}else{
										annualFees.setChargeStatus(-1);
									}
								}
								//退费金额大于0才给记录退费,小于0就当做修改应缴金额
								if(Double.parseDouble(refund)>0.00){
									StudentInfo _stuInfo = annualFees.getStudentInfo();
									condition.clear();
									condition.put("studyNo", _stuInfo.getStudyNo());
									List<ReturnPremium> returnPremiumList = returnPremiumService.findReturnPremiumByCondition(condition);
									//增加退费记录
									Double facepayFeeD = Double.valueOf(facepayFee);
									Double refundD = Double.parseDouble(refund);
									int showOrder = returnPremiumList!=null?returnPremiumList.size()+1:0;
									ReturnPremium returnPremium = returnPremiumService.createReturnPremium(refundD, facepayFeeD, paymentMethod,
											annualFees.getRecpayFee(), _stuInfo,annualFees.getYearInfo(),showOrder,ReturnPremium.PROCESSTYPE_RETURNPREMIUM,null,"tuition");
									
									returnPremiumService.saveOrUpdate(returnPremium);
								}
								annualFeesService.saveOrUpdate(annualFees);
								studentPaymentService.saveOrUpdate(studentPayment);
								UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.UPDATE,"学生年度缴费管理--退费：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
								map.put("statusCode", 200);
								map.put("message", "退费成功:<br/>");
								map.put("reloadUrl", request.getContextPath()+"/edu3/finance/annualFees/list.html");
							}else{
								map.put("statusCode", 300);
								map.put("message", "退费失败:查不到数据<br/>请联系管理员");
							}
						}
					}else{
						map.put("statusCode", 300);
						map.put("message", "退费失败:查不到数据<br/>请联系管理员");
					}
				}
			} catch (Exception e) {
				logger.error("退费出错", e);
				map.put("statusCode", 300);
				map.put("message", "退费失败:<br/>"+e.getLocalizedMessage()+"请联系管理员");
			}
		}else{
			map.put("statusCode", 300);
			map.put("message", "退费失败:数据错误<br/>请联系管理员");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
