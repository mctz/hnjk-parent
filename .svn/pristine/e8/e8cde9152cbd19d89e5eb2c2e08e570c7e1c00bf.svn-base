package com.hnjk.edu.finance.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xalan.lib.ExsltStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.util.AddPaymentUtil;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.ReturnCode;
import com.hnjk.edu.portal.service.IArticleService;
import com.hnjk.edu.recruit.helper.UpdateIDNumber;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * 学费管理 <code>StudentFeeController</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-5-28 上午10:50:56
 * @see
 * @version 1.0
 */
@Controller
public class StudentFeeController extends BaseSupportController {

	private static final long serialVersionUID = 3339397528651542762L;

	@Autowired
	@Qualifier("yxtDataSource")
	private DataSource dataSource;

	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService iTempStudentFeeService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService iStudentInfoService;

	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService iStudentPaymentService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("articleService")
	private IArticleService articleService;
	
	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService;

	/**
	 * 学费查询
	 * 
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolfee/studentfee/list.html")
	public String exeList(String studentName, String studyNo,
			String schoolCenter, String classic, Page objPage, ModelMap model)
			throws WebException {
		try {

			Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
			if (ExStringUtils.isNotEmpty(studyNo))
				condition.put("studyNo", studyNo);
			if (ExStringUtils.isNotEmpty(studentName))
				condition.put("studentName", studentName);
			// if(ExStringUtils.isNotEmpty(schoolCenter))
			// condition.put("schoolCenter", schoolCenter);
			// if(ExStringUtils.isNotEmpty(classic)) condition.put("classic",
			// classic);

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			if (ExStringUtils.isNotEmpty(studyNo)
					|| ExStringUtils.isNotEmpty(studentName)) {
				int pagestart = 0;
				if (objPage.getPageNum() > 1) {
					pagestart = (objPage.getPageNum() - 1)
							* objPage.getPageSize();
				}

				StringBuffer sql1 = new StringBuffer(
						"select count(*) from V_BankWater where CollegeID!='033' and datalength(StudentID)>14 ");
				if (ExStringUtils.isNotEmpty(studyNo))
					sql1.append(" and StudentID='" + studyNo + "'");
				if (ExStringUtils.isNotEmpty(studentName))
					sql1.append(" and StudentName like '" + studentName + "%'");
				if (ExStringUtils.isNotEmpty(schoolCenter))
					condition.put("schoolCenter", schoolCenter);
				if (ExStringUtils.isNotEmpty(classic))
					condition.put("classic", classic);

				StringBuffer sql2 = new StringBuffer("select top ");
				sql2.append(objPage.getPageSize());
				sql2.append(" * from V_BankWater where CollegeID!='033' and datalength(StudentID)>14");

				if (ExStringUtils.isNotEmpty(studyNo)) {
					sql2.append(" and StudentID='" + studyNo + "'");
				}
				if (ExStringUtils.isNotEmpty(studentName)) {
					sql2.append(" and StudentName like '" + studentName + "%'");
				}

				User user = SpringSecurityHelper.getCurrentUser();
				// 校外学习中心人员
				if (CacheAppManager
						.getSysConfigurationByCode("orgunit.brschool.unittype")
						.getParamValue()
						.equals(user.getOrgUnit().getUnitType())) {
					String unitName = ExStringUtils.trimToEmpty(user
							.getOrgUnit().getUnitDescript());// 取组织描述字段匹配
					sql1.append(" and CollegeName='" + unitName + "' ");
					sql2.append(" and CollegeName='" + unitName + "' ");
				}
				sql2.append(" and StudentID > (select  isnull(max(StudentID),0)  from (select top ");
				sql2.append(pagestart);
				sql2.append(" StudentID from V_BankWater where CollegeID!='033' and datalength(StudentID)>14 ");

				if (ExStringUtils.isNotEmpty(studyNo)) {
					sql2.append(" and StudentID='" + studyNo + "'");
				}
				if (ExStringUtils.isNotEmpty(studentName)) {
					sql2.append(" and StudentName like '" + studentName + "%'");
				}

				sql2.append(" order  by StudentID) as  T )  order by StudentID");

				int totalCount = jdbcTemplate.queryForInt(sql1.toString());
				List list = jdbcTemplate.queryForList(sql2.toString());
				objPage.setTotalCount(totalCount);

				model.addAttribute("feeList", list);
				model.addAttribute("condition", condition);
				model.addAttribute("objPage", objPage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/edu3/finance/stufee/stufee-list";
	}

	/**
	 * 学费统计
	 * 
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolfee/studentfee/listsum.html")
	public String exeListSum(String year, Page objPage, ModelMap model)
			throws WebException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
			List list = new ArrayList();
			if (ExStringUtils.isNotEmpty(year)) {
				condition.put("year", year);

				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "select CollegeName,TuitionFee,'"
						+ year
						+ "'+EntranceFlagName EntranceFlagName,count(*) StuNum, count(*)*TuitionFee SumFee from V_BankWater where CollegeID!='033' and datalength(StudentID)>14 and ChargeYear="
						+ year + " ";
				User user = SpringSecurityHelper.getCurrentUser();
				if (CacheAppManager
						.getSysConfigurationByCode("orgunit.brschool.unittype")
						.getParamValue()
						.equals(user.getOrgUnit().getUnitType())) {
					sql += " and CollegeName='"
							+ ExStringUtils.trimToEmpty(user.getOrgUnit()
									.getUnitDescript()) + "' ";
				}
				sql += " group by CollegeName,TuitionFee,EntranceFlagName ";
				sql += " order by CollegeName,EntranceFlagName,TuitionFee ";
				list = jdbcTemplate.queryForList(sql);
			}

			model.addAttribute("feeList", list);
			model.addAttribute("condition", condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/edu3/finance/stufee/stufee-sum";
	}

	@RequestMapping("/tuitionFee.html")
	public String studentPayFeeIndex(Page objPage, ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);
		Map<String, Object> condition = new HashMap<String, Object>();
		model.addAttribute("condition", condition);
		// 只搜索出已经发布的信息
		condition.put("auditStatus", 1);
		// 写死了只会加载“缴费说明”里的文档
		condition.put("channelId", "40288194661f5f250166230d97480005");
		Page page = articleService.findArticleByCondition(condition, objPage);
		model.addAttribute("articleList", page);
		return "/edu3/finance/stupayfee/studentPayFeeIndex";
	}

	@RequestMapping("/tuitionFee/searchOrders.html")
	public void validateUserSubmit(String certNum, HttpServletRequest request,
			HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String message = "";
		int statusCode = 200;
		certNum = StringEscapeUtils.escapeSql(certNum);
		if (ExStringUtils.isNotBlank(certNum)) {
			if (!UpdateIDNumber.validate(certNum)) {
				message = "身份证号不合法!";
				statusCode = 300;
			}

		} else {
			message = "身份证号不能为空!";
			statusCode = 300;
		}
		map.put("message", message);
		map.put("statusCode", statusCode);
		map.put("certNum", certNum);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	@RequestMapping("/tuitionFee/displayOrders.html")
	public String searchOrders(Page objPage, String certNum,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws WebException {
		List<TempStudentFee> list = new ArrayList<TempStudentFee>();
		certNum = StringEscapeUtils.escapeSql(certNum);
		list = iTempStudentFeeService.findByCertnum(certNum);
		StudentInfo info = iStudentInfoService.findBycertNum(certNum);

		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);
		Map<String, Object> condition = new HashMap<String, Object>();
		model.addAttribute("condition", condition);
		// 只搜索出已经发布的信息
		condition.put("auditStatus", 1);
		// 写死了只会加载“缴费说明”里的文档
		condition.put("channelId", "40288194661f5f250166230d97480005");
		Page page = articleService.findArticleByCondition(condition, objPage);
		model.addAttribute("articleList", page);
		model.addAttribute("feeList", list);
		model.addAttribute("stuInfo", info);
		model.addAttribute("hasStuInfo", ExBeanUtils.isNullOfAll(info)?"N":"Y");
		ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
		String payUrl=cp.getProperty("payUrl");
		model.addAttribute("payUrl", payUrl+"?pwd=");
		return "/edu3/finance/stupayfee/studentPayFeeOrders";
	}

	@RequestMapping("/tuitionFee/payFee.html")
	public void payFee(String schoolOrderNo, HttpServletRequest request,
			HttpServletResponse response) throws WebException {
		int statusCode = 200;
		String message = "操作成功！";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String forWardUrl = "";
		schoolOrderNo = StringEscapeUtils.escapeSql(schoolOrderNo);
		// 此处未做签名校验（说明文档中没有要求）
		TempStudentFee fee = iTempStudentFeeService.findUniqueByProperty(
				"schoolOrderNo", schoolOrderNo);
		if(fee.getHasStudentInfo().equals("N") && !"tuition".equals(fee.getChargingItems())){
			statusCode = 300;
			message = "你当前还没有学籍信息，请先缴学费后再缴教材费用！";
			jsonMap.put("statusCode", statusCode);
			jsonMap.put("message", message);
			renderJson(response, JsonUtils.mapToJson(jsonMap));
		}else{

			String payPassword = fee.getPayPassword();
			String payStatus = fee.getPayStatus();
			ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
			String _sysCert = cp.getProperty("sysCert");
			String _sysId = cp.getProperty("sysId");
			String _itemId = cp.getProperty("itemId");
			String addPayUrl = cp.getProperty("addPayUrl");
			String _returnURL = cp.getProperty("returnURL");
			String payUrl = cp.getProperty("payUrl");
			if (ExStringUtils.isBlank(payPassword)) {
				try {
					URL url = new URL(addPayUrl);
					URLConnection conn = url.openConnection();
					conn.setDoInput(true);
					conn.setDoOutput(true);
					OutputStream out = conn.getOutputStream();
					StringBuffer params = new StringBuffer();
					String sysCert = _sysCert;
					String firstSC = sysCert.charAt(0) + "";
					String sysId = _sysId;
					String itemId = _itemId;
					String objId = schoolOrderNo; // 一般为订单号，会判断是否重复
					// String otherId = "";
					String objName = fee.getStudentName();
					String amount = fee.getAmount().toString();
					String specialValue = "";
					String autoSubmit = "";
					String returnURL = _returnURL;
					try {
						params.append("sign=").append(
								AddPaymentUtil
										.createSignString(new String[] { firstSC,
												sysId, firstSC, itemId, firstSC,
												objId, firstSC, firstSC, objName,
												firstSC, amount, firstSC, firstSC,
												"data", firstSC, specialValue,
												firstSC,returnURL,  sysCert }));

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					params.append("&sysId=").append(sysId);
					params.append("&itemId=").append(itemId);
					params.append("&objId=").append(objId);
					params.append("&objName=").append(
							URLEncoder.encode(objName, "UTF-8"));
					params.append("&returnURL=").append(
							URLEncoder.encode(returnURL, "UTF-8"));
					// params.append("&otherId=").append(
					// URLEncoder.encode(otherId, "UTF-8"));
					params.append("&specialValue=").append(
							URLEncoder.encode(specialValue, "UTF-8"));
					params.append("&autoSubmit=").append(
							URLEncoder.encode(autoSubmit, "UTF-8"));
					params.append("&amount=").append(amount);
					params.append("&returnType=").append("data");

					out.write(params.toString().getBytes("UTF-8"));
					// out.write(params.getBytes("UTF-8"));
					InputStream in = conn.getInputStream();
					InputStreamReader isr = new InputStreamReader(in, "UTF-8");
					char[] buf = new char[1024];
					int count = isr.read(buf);
					String result = new String(buf, 0, count);
					out.close();
					Map<String, Object> map = JsonUtils.jsonToMap(result);
					String returnCode = map.get("returnCode").toString();
					if (Integer.valueOf(returnCode) == ReturnCode.code00.getCode()) {
						payPassword = map.get("payPassword").toString();
						// 数据库中保存支付码
						fee.setPayPassword(payPassword);
						iTempStudentFeeService.saveOrUpdate(fee);
						forWardUrl = payUrl + "?pwd=" + payPassword;
						jsonMap.put("forWardUrl", forWardUrl);
					} else {
						statusCode = 300;
						message = ReturnCode.getReturnCode(Integer
								.valueOf(returnCode));
					}
				} catch (IOException e) {
					statusCode = 300;
					message = "连接缴费客户端失败，请稍后再试";
					e.printStackTrace();
				} catch (Exception e) {
					statusCode = 300;
					message = "系统业务处理出错，请联系管理员处理";
					e.printStackTrace();
				} finally {

					jsonMap.put("statusCode", statusCode);
					jsonMap.put("message", message);
					renderJson(response, JsonUtils.mapToJson(jsonMap));
				}
			} else {
				// 已有支付码，直接跳转到支付页面
				if (payStatus.equals(Constants.FEE_PAYSTATUS_PAYED)) {
					message = "系统检测到您当前的订单已支付，你稍后再查询订单状态";
					jsonMap.put("statusCode", statusCode);
					jsonMap.put("message", message);
				} else {

					forWardUrl = payUrl + "?pwd=" + payPassword;
					jsonMap.put("forWardUrl", forWardUrl);
					jsonMap.put("statusCode", statusCode);
					jsonMap.put("message", message);
				}

				renderJson(response, JsonUtils.mapToJson(jsonMap));
			}
		}

	}
	//约定的返回地址
	@RequestMapping("/tuitionFee/payResult.html")
	public void payResult(HttpServletRequest request,
			HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			ParseException {
		// Map<String, Object> condition = Condition2SQLHelper
		// .getConditionFromResquestByIterator(request);
		String sign = request.getParameter("sign");
		String version = request.getParameter("version");
		String sysId = request.getParameter("sysId");
		String itemId = request.getParameter("itemId");
		String objId = request.getParameter("objId");
		String otherId = request.getParameter("otherId");
		String objName = request.getParameter("objName");
		String amount = request.getParameter("amount");
		String paid = request.getParameter("paid");
		String refund = request.getParameter("refund");
		String overTime = request.getParameter("overTime");
		String status = request.getParameter("status");
		String projectId = request.getParameter("projectId");
		String payId = request.getParameter("payId");
		String payPassword = request.getParameter("payPassword");
		String specialValue = request.getParameter("specialValue");
		String payType = request.getParameter("payType");
		ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
		String sysCert = cp.getProperty("sysCert");
		String firstSC = sysCert.charAt(0) + "";
		String mySign = AddPaymentUtil.createSignString(new String[] { firstSC,
				version, firstSC, sysId, firstSC, itemId, firstSC, objId,
				firstSC, otherId, firstSC, objName, firstSC, amount, firstSC,
				paid, firstSC, refund, firstSC, overTime, firstSC, status,
				firstSC, projectId, firstSC, payId, firstSC, payPassword,
				firstSC, specialValue, firstSC, payType, sysCert });
		// 核对签名
		if (mySign.equals(sign)) {
			// 1 根据objId（订单号）进行查询
			TempStudentFee fee = iTempStudentFeeService.findUniqueByProperty(
					"schoolOrderNo", objId);
			if (ExBeanUtils.isNotNullOfAll(fee)) {
				// 核对金额
				if (fee.getAmount().compareTo(Double.valueOf(amount))==0) {
					if(Constants.FEE_PAYSTATUS_PAYED.equals(fee.getPayStatus())){
						logger.info("该订单已经在系统中已完成付款，订单ID:" + objId);
						renderText(response, "0");
					}else{
						String eduOrderNo = payId;
						fee.setEduOrderNo(eduOrderNo);
//						StudentPayment studentPayment = iStudentPaymentService
//								.findUnique(
//										"from StudentPayment s where s.isDeleted=0 and s.studentInfo.resourceid=?",
//										fee.getStudentInfo().getResourceid());
						User user = userService.getUserByLoginId("hnjk");
						PayDetailsVO payDetailsVO = new PayDetailsVO();
						payDetailsVO.setPayAmount(Double.valueOf(amount));
						payDetailsVO.setPaymentMethod("8");
						// TODO 以后如果有多个学校使用做成全局参数或数据字典
						payDetailsVO.setChargeMoney(0d);
						// 终端号加准考证号
						payDetailsVO.setEduOrederNo(fee.getSchoolOrderNo());
						// 收费项
						payDetailsVO.setChargingItems(fee.getChargingItems());
						// 是否开单位发票
						payDetailsVO.setIsInvoicing(fee.getIsInvoicing());
						// 单位名称
						payDetailsVO.setInvoiceTitle(fee.getInvoiceTitle());
						// 缴费时间
						String payTime = overTime;
						if (ExStringUtils.isNotEmpty(payTime)) {
							payDetailsVO.setPayTime(ExDateUtils.parseDate(payTime,
									ExDateUtils.PATTREN_DATE_TIME_COMBINE));
						}
						if (fee.getChargingItems().equalsIgnoreCase("tuition")) {// 学费
							iTempStudentFeeService.updatePaymentDetails(fee);
//							iStudentPaymentService.payFee(studentPayment,
//									payDetailsVO, user);
						} else if (fee.getChargingItems().equalsIgnoreCase(
								"textbookFee")) {
							iStudentPaymentService.payFeeForTextbook(fee,
									payDetailsVO, user);
						}
						logger.info("完成缴费订单，订单ID:" + objId);
						renderText(response, "0");
					}
					
				} else {
					logger.info("核对缴费订单，订单金额不匹配：" + fee.getAmount()
							+ " 系统金额：返回订单的金额：" + amount + " 订单ID:" + objId);
					renderText(response, "2");
				}
			}

		} else {
			logger.info("核对缴费订单，签名信息不匹配！ 返回签名：" + sign + " 系统签名:" + mySign);
			renderText(response, "3");
		}

	}
	@RequestMapping("/tuitionFee/returnUrl.html")
	public void handleReturnUrl(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		String sign = request.getParameter("sign");
		String version = request.getParameter("version");
		String sysId = request.getParameter("sysId");
		String itemId = request.getParameter("itemId");
		String objId = request.getParameter("objId");
		String otherId = request.getParameter("otherId");
		String objName = request.getParameter("objName");
		String amount = request.getParameter("amount");
		String paid = request.getParameter("paid");
		String refund = request.getParameter("refund");
		String overTime = request.getParameter("overTime");
		String status = request.getParameter("status");
		String projectId = request.getParameter("projectId");
		String payId = request.getParameter("payId");
		String payPassword = request.getParameter("payPassword");
		String specialValue = request.getParameter("specialValue");
		String payType = request.getParameter("payType");
		ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
		String sysCert = cp.getProperty("sysCert");
		String firstSC = sysCert.charAt(0) + "";
		String mySign = AddPaymentUtil.createSignString(new String[] { firstSC,
				version, firstSC, sysId, firstSC, itemId, firstSC, objId,
				firstSC, otherId, firstSC, objName, firstSC, amount, firstSC,
				paid, firstSC, refund, firstSC, overTime, firstSC, status,
				firstSC, projectId, firstSC, payId, firstSC, payPassword,
				firstSC, specialValue, firstSC, payType, sysCert });
		// 核对签名
		if (mySign.equals(sign)){
			TempStudentFee fee = iTempStudentFeeService.findUniqueByProperty(
					"schoolOrderNo", objId);
			if (ExBeanUtils.isNotNullOfAll(fee)) {
				if (fee.getAmount().compareTo(Double.valueOf(amount))==0) {
					StringBuffer html = new StringBuffer();
					String payUrl = cp.getProperty("payUrl");
					html.append("<script>window.location.href=\""+payUrl + "?pwd=" + payPassword+"\";</script>");
					logger.info(html.toString());
					renderHtml(response, html.toString());
				}
			}
		}
	}
	@RequestMapping("/tuitionFee/applyPrintInvoice.html")
	public void applyPrintInvoice(String schoolOrderNo,String invoiceTitle,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		int statusCode = 200;
		String message = "操作成功！";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			schoolOrderNo = StringEscapeUtils.escapeSql(schoolOrderNo);
			TempStudentFee fee = iTempStudentFeeService.findUniqueByProperty(
					"schoolOrderNo", schoolOrderNo);
//			invoiceTitle = ExStringUtils.getEncodeURIComponentByOne(invoiceTitle);
			if(ExBeanUtils.isNotNullOfAll(fee)){
				fee.setInvoiceTitle(invoiceTitle);
				fee.setIsInvoicing(Constants.BOOLEAN_YES);
				iTempStudentFeeService.saveOrUpdate(fee);
				String certNum = fee.getCertNum();
				jsonMap.put("certNum", certNum);
				String eduOrederNo = fee.getEduOrderNo();
				StudentPaymentDetails spd = studentPaymentDetailsService.findUniqueByProperty("eduOrederNo", eduOrederNo);
				if(ExBeanUtils.isNotNullOfAll(spd)){
					spd.setIsInvoicing(Constants.BOOLEAN_YES);
					spd.setInvoiceTitle(invoiceTitle);
					studentPaymentDetailsService.saveOrUpdate(spd);
				}
				
			}else{
				statusCode = 300;
				message = "根据订单号查找订单信息为空，请稍后再试！";
			}
			
		} catch (Exception e) {
			statusCode = 300;
			message = "操作失败！请稍后再试";
			e.printStackTrace();
		} finally{
			jsonMap.put("statusCode", statusCode);
			jsonMap.put("message", message);
			renderJson(response, JsonUtils.mapToJson(jsonMap));
		}

	}
	@RequestMapping("/tuitionFee/displayInvoice.html")
	public void displayInvoice(String schoolOrderNo,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		int statusCode = 200;
		String message = "操作成功！";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			schoolOrderNo = StringEscapeUtils.escapeSql(schoolOrderNo);
			TempStudentFee fee = iTempStudentFeeService.findUniqueByProperty(
					"schoolOrderNo", schoolOrderNo);
//			invoiceTitle = ExStringUtils.getEncodeURIComponentByOne(invoiceTitle);
			if(ExBeanUtils.isNotNullOfAll(fee)){
				if(Constants.BOOLEAN_YES.equals(fee.getIsInvoicing())){
					String invoiceTitle = fee.getInvoiceTitle();
					jsonMap.put("invoiceTitle", invoiceTitle);
				}
				
			}else{
				statusCode = 300;
				message = "根据订单号查找订单信息为空，请稍后再试！";
			}
			
		} catch (Exception e) {
			statusCode = 300;
			message = "加载单位名称失败！请稍后再试";
			e.printStackTrace();
		} finally{
			jsonMap.put("statusCode", statusCode);
			jsonMap.put("message", message);
			renderJson(response, JsonUtils.mapToJson(jsonMap));
		}

	}
}
