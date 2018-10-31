package com.hnjk.edu.finance.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExFileUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.finance.service.IFeeCommissionInfoService;
import com.hnjk.edu.finance.vo.FeeCommissionInfoVo;
import com.hnjk.edu.finance.vo.FeeCommissionJuniorInfoVo;
import com.hnjk.edu.finance.vo.FeeCommissionTypeInfoVo;
import com.hnjk.edu.finance.vo.FeeCommissionUniversityInfoVo;
import com.hnjk.edu.finance.vo.GradeNumVo;
import com.hnjk.edu.finance.vo.TransferSchoolFeeVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/** 
 * 学费提成信息
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Aug 22, 2016 4:25:11 PM 
 * 
 */
@Controller
public class FeeCommissionInfoController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 4973133077865312214L;
	
	@Autowired
	@Qualifier("feeCommissionInfoService")
	private IFeeCommissionInfoService feeCommissionInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	/**
	 * 学费提成明细-分页列表
	 * @param request
	 * @param model
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/feeCommissionInfo.html")
	public String exeFeeCommissionInfoList(HttpServletRequest request, ModelMap model) throws WebException {
		// 参数处理
		String brSchool = request.getParameter("brSchool");
 		String operatedate = request.getParameter("operatedate");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

		String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
	    int year = 0;
	    int month = 0;
	    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日"); 
		String date = df.format(new Date()).toString();
	    //String title = "";
	    String isBrschool = "N";
	    String userBrSchool = "";
	    Map<String,Object> condition = new HashMap<String, Object>(0);
		User curUser          = SpringSecurityHelper.getCurrentUser();
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchool = curUser.getOrgUnit().getResourceid();
			userBrSchool = curUser.getOrgUnit().getUnitName();
			isBrschool = "Y";
		}

		if(ExStringUtils.isNotEmpty(brSchool)){
			condition.put("brSchool", brSchool);
		}

		if(ExStringUtils.isNotBlank(operatedate)){
			condition.put("operatedate",operatedate);
		}
		
		String returnPath = null;
		List<Map<String, Object>> list = null;
		Map<String, BigDecimal> schoolFeeMap = null;
		if ("11846".equals(schoolCode)) {//广外
			if(ExStringUtils.isEmpty(beginDate)){
				beginDate = ExDateUtils.getCurrentYear()-1 + "-12-01";
			}
			if(ExStringUtils.isEmpty(endDate)){
				endDate = ExDateUtils.getCurrentDate();
			}
			returnPath = "/edu3/finance/feeCommissionInfo/feeCommissionInfo_gdwy_list";
			condition.put("beginDate",beginDate);
			condition.put("endDate",endDate);
			// 获取学费提成信息
			list = feeCommissionInfoService.findFeeCommissionExt(condition);
			String separateDate = CacheAppManager.getSysConfigurationByCode("fee.separateDate").getParamValue();
			schoolFeeMap = getFeeForSchool(beginDate, endDate, separateDate);
		} else {//广东医
			if(ExStringUtils.isEmpty(operatedate)){
				operatedate = ExDateUtils.getCurrentYear() + "-" + ExDateUtils.getCurrentMonth();
			}
			year = Integer.parseInt(operatedate.substring(0, 4));
			month = Integer.parseInt(operatedate.substring(operatedate.lastIndexOf("-")+1));
			returnPath = "/edu3/finance/feeCommissionInfo/feeCommissionInfo-list";
			condition.put("operatedate", operatedate);
			// 获取学费提成信息
			list = feeCommissionInfoService.findFeeCommissionInfoByJDBC(condition);
		}
		
		List<FeeCommissionInfoVo> newlist = getFeeCommissionInfoVoList(list, year,model,isBrschool,schoolFeeMap);

		model.addAttribute("condition", condition);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);
		model.addAttribute("isBrschool", isBrschool);
		model.addAttribute("userBrSchool", userBrSchool);
		model.addAttribute("feeCommissionInfo", newlist);
		return returnPath;
	}

	/**
	 * 查看未缴费的学生信息
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/finance/feeCommissionInfo/viewNotPay.html")
	public String viewNotPay(HttpServletRequest request,HttpServletResponse response, ModelMap model,Page objPage){
		String brSchool = request.getParameter("brSchool");
 		String operatedate = request.getParameter("operatedate");
 		String flag =  request.getParameter("flag");
 		String stuids = request.getParameter("stuids");
	    int year = 0;  
	    int month = 0;
	    Map<String,Object> condition = new HashMap<String, Object>(0);
		User curUser          = SpringSecurityHelper.getCurrentUser();
		String url = "/edu3/finance/feeCommissionInfo/viewNotPay";
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchool = curUser.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotEmpty(brSchool)){
			condition.put("brSchool", brSchool);
		}
		if(ExStringUtils.isEmpty(operatedate)){
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH)+1;
			operatedate = year+"-"+month;
		}else {
			year = Integer.parseInt(operatedate.substring(0, 4));
			month = Integer.parseInt(operatedate.substring(operatedate.lastIndexOf("-")+1));
		}
		if(ExStringUtils.isNotBlank(stuids)){
			condition.put("stuids", stuids);
		}
		condition.put("operatedate", year+"-"+month);
		
		if("export".equals(flag)){
			List<Map<String, Object>> list = feeCommissionInfoService.findNotPayStudentFee(condition);
			String fileName = null;
			try {
				fileName = new String("未缴费学生名单".getBytes("GBK"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			request.getSession().setAttribute("list", list);
			response.setCharacterEncoding("GB2312");
			response.setContentType("application/vnd.ms-excel");
		  	response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
			url = "/edu3/finance/feeCommissionInfo/exportNotPay";
		}else {
			Page page = feeCommissionInfoService.findNotPayStudentFee(condition,objPage);
			model.addAttribute("objPage", page);
			model.addAttribute("condition", condition);
		}
		return url;
	}

	/**
	 * 导出提成明细表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value={"/edu3/finance/feeCommissionInfo/export.html","/edu3/finance/feeCommissionInfo/print-view.html"})
	public String doExcelExport(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		// 参数处理
		String brSchool = request.getParameter("brSchool");
		String isBrschool = request.getParameter("isBrschool");
 		String operatedate = request.getParameter("operatedate");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
 		String flag = request.getParameter("flag");
 		String title = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
 		String userBrSchool = "";
 		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日"); 
		String date = df.format(new Date()).toString(); 
 		int year = 0;
		if (ExStringUtils.isNotEmpty(request.getParameter("year"))) {
			year = Integer.parseInt(request.getParameter("year"));
		}
		int month = 0;
		if (ExStringUtils.isNotEmpty(request.getParameter("month"))) {
			month = Integer.parseInt(request.getParameter("month"));
		}
		String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if("Y".equals(isBrschool)){
			userBrSchool = orgUnitService.findUniqueByProperty("resourceid", brSchool).getUnitName();
		}
		Map<String,Object> condition = new HashMap<String, Object>(0);

		if(ExStringUtils.isNotEmpty(brSchool)){
			condition.put("brSchool", brSchool);
		}

		if(ExStringUtils.isNotBlank(operatedate)){ condition.put("operatedate",operatedate);}
		condition.put("beginDate",beginDate);
		condition.put("endDate",endDate);


		String returnPath = "/edu3/finance/feeCommissionInfo/feeCommissionInfoExcel";

		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);

		model.addAttribute("brSchool", brSchool);
		model.addAttribute("isBrschool", isBrschool);
		model.addAttribute("userBrSchool", userBrSchool);
		model.addAttribute("operatedate", operatedate);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);

		List<Map<String, Object>> list = null;
		Map<String, BigDecimal> schoolFeeMap = null;
		if("print".equals(flag)){
			return "/edu3/finance/feeCommissionInfo/printFeeCommissionInfo";
		}else if ("export".equals(flag)) {
			if ("11846".equals(schoolCode)) {
				title = "各成教教学点学费分成明细表";
				returnPath = "/edu3/finance/feeCommissionInfo/feeCommissionInfoExcel_gdwy";
				list = feeCommissionInfoService.findFeeCommissionExt(condition);
				String separateDate = CacheAppManager.getSysConfigurationByCode("fee.separateDate").getParamValue();
				schoolFeeMap = getFeeForSchool(beginDate, endDate, separateDate);
			} else {
				title += year+"年成人高等教育校外点学费收入提成明细表";
				list = feeCommissionInfoService.findFeeCommissionInfoByJDBC(condition);
			}
			String fileName = null;
			try {
				fileName = new String("学费收入提成明细表".getBytes("GBK"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			List<FeeCommissionInfoVo> newlist = getFeeCommissionInfoVoList(list, year,model,isBrschool,schoolFeeMap);
			request.getSession().setAttribute("feeCommissionInfo", newlist);
			response.setCharacterEncoding("GB2312");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
		}
		model.addAttribute("title", title);
	    return returnPath;
	}

	/**
	 * 将查询结果转为VO对象
	 * @param list
	 * @param year
	 * @param map
	 * @param isBrschool
	 * @param schoolFeeMap
	 * @return
	 */
	private List<FeeCommissionInfoVo> getFeeCommissionInfoVoList(List<Map<String, Object>> list,int year,ModelMap map,String isBrschool,Map<String, BigDecimal> schoolFeeMap) {
		DecimalFormat formatInt = new DecimalFormat("#,###");
		List<FeeCommissionInfoVo> newlist = new ArrayList<FeeCommissionInfoVo>(0);
//		DecimalFormat formatDouble = new DecimalFormat("#,###.00");

		try {
			if(list!=null && 0<list.size()){
				String unitId = "";
				//String classicName = "";
				FeeCommissionInfoVo feeInfo = new FeeCommissionInfoVo();
				FeeCommissionJuniorInfoVo juniorInfo = new FeeCommissionJuniorInfoVo();
				FeeCommissionUniversityInfoVo universityInfo = new FeeCommissionUniversityInfoVo();
				FeeCommissionTypeInfoVo typeInfoVo = new FeeCommissionTypeInfoVo();
				int ordinal = 1;
				int index = 0; //当前集合的元素位置
				for (Map<String, Object> m : list) {
					index++;//位置标识，用来判断是否为最后一条记录
					try {
						if(m.get("unitshortname")==null){
							continue;
						}
						//添加一个教学点的记录
						if(!m.get("unitid").equals(unitId)){ //如果当前教学点和上一条记录的教学点不一样
							// 处理异动的教学点提成信息
							if(ExStringUtils.isNotBlank(unitId) && schoolFeeMap != null){
								BigDecimal monY = schoolFeeMap.get(unitId+"_Y");
								BigDecimal monN = schoolFeeMap.get(unitId+"_N");
								if(monY!=null){
									//外语类分成金额
									typeInfoVo.setRoyaltyRate2Pay(typeInfoVo.getRoyaltyRate2Pay().add(monY));
									// 实收学费
									feeInfo.setRealFullFees_tuition(feeInfo.getRealFullFees_tuition().add(monY));
								}
								if(monN!=null){
									//非外语类分成金额
									typeInfoVo.setRoyaltyRatePay(typeInfoVo.getRoyaltyRatePay().add(monN));
									// 实收学费
									feeInfo.setRealFullFees_tuition(feeInfo.getRealFullFees_tuition().add(monN));
								}
							}
							if(index!=1){//第一条不保存
								feeInfo.setUniversityInfo(universityInfo);
								feeInfo.setJuniorInfo(juniorInfo);
								feeInfo.setTypeInfoVo(typeInfoVo);
								newlist.add(feeInfo);
								ordinal++;
								feeInfo = new FeeCommissionInfoVo();
								juniorInfo = new FeeCommissionJuniorInfoVo();
								universityInfo = new FeeCommissionUniversityInfoVo();
								typeInfoVo = new FeeCommissionTypeInfoVo();
							}
							//保存当前vo的信息
							feeInfo.setOrdinal(ordinal);
							if (m.get("unitshortname").toString().length() < m.get("unitname").toString().length()) {
								feeInfo.setUnitName(m.get("unitshortname").toString());
							} else {
								feeInfo.setUnitName(m.get("unitname").toString());
							}

							feeInfo.setUnitid(m.get("unitid").toString());
							feeInfo.setProportion(ExNumberUtils.toDoubleDefault0(m.get("royaltyrate")));
							feeInfo.setMemo("Y".equals(m.get("islocal"))?"是":"否");
							feeInfo.setFirstReturnPay(ExNumberUtils.toBigDecimalDefault0(m.get("firstReturnPay")));
							feeInfo.setSecondReturnPay(ExNumberUtils.toBigDecimalDefault0(m.get("secondReturnPay")));
							//typeInfoVo
							typeInfoVo.setRoyaltyRate(feeInfo.getProportion());
							typeInfoVo.setRoyaltyRate2(ExNumberUtils.toDoubleDefault0(m.get("royaltyRate2")));
							typeInfoVo.setReserveRatio(ExNumberUtils.toDoubleDefault0(m.get("reserveRatio")));

						}
						//非外语类分成金额
						typeInfoVo.setRoyaltyRatePay(typeInfoVo.getRoyaltyRatePay().add(ExNumberUtils.toBigDecimalDefault0(m.get("royaltyRatePay"))));
						//外语类分成金额
						typeInfoVo.setRoyaltyRate2Pay(typeInfoVo.getRoyaltyRate2Pay().add(ExNumberUtils.toBigDecimalDefault0(m.get("royaltyRate2Pay"))));
						// 实收学费
						feeInfo.setRealFullFees_tuition(feeInfo.getRealFullFees_tuition().add(ExNumberUtils.toBigDecimalDefault0(m.get("spdamount_tuition"))));
						// 实收学费除外的其他费用-不需要
//						feeInfo.setRealFullFees_others(feeInfo.getRealFullFees_others().add(ExNumberUtils.toBigDecimalDefault0(m.get("spdamount_others"))));

						if (m.containsKey("gradename")) {
							// 广东医-实收净收入学费
							double _spdamount = Double.valueOf(m.get("spdamount").toString());//实际缴费金额（已缴金额-退费金额）
							GradeNumVo gradeNumVo = new GradeNumVo();
							gradeNumVo.setGrade(m.get("gradename").toString());
							gradeNumVo.setStudentNum((BigDecimal) (m.get("studentNum")!=null?m.get("studentNum"):0));
							gradeNumVo.setPayNum(new BigDecimal(m.get("paynum").toString()));
							// 已经减去退费金额
							gradeNumVo.setFees(BigDecimal.valueOf(_spdamount));
							if (m.containsKey("classicname")) {
								if ("专科".equals(m.get("classicname"))) {
									juniorInfo.setStudentFeeRule((BigDecimal) (m.get("creditfee")!=null?m.get("creditfee"):new BigDecimal(3250)));// 广东医专科学费默认3250
									if(m.get("gradename").toString().contains(String.valueOf(year))) {
										juniorInfo.setGradeNumVo3(gradeNumVo);
									}else if (m.get("gradename").toString().contains(String.valueOf(year-1))) {
										juniorInfo.setGradeNumVo2(gradeNumVo);
									}else if (m.get("gradename").toString().contains(String.valueOf(year-2))) {
										juniorInfo.setGradeNumVo1(gradeNumVo);
									}
								}else if("本科".equals(m.get("classicname"))) {
									universityInfo.setStudentFeeRule((BigDecimal) (m.get("creditfee")!=null?m.get("creditfee"):new BigDecimal(3510)));// 广东医本科学费默认3510
									if(m.get("gradename").toString().contains(String.valueOf(year))){
										universityInfo.setGradeNumVo3(gradeNumVo);
									}else if (m.get("gradename").toString().contains(String.valueOf(year-1))) {
										universityInfo.setGradeNumVo2(gradeNumVo);
									}else if (m.get("gradename").toString().contains(String.valueOf(year-2))) {
										universityInfo.setGradeNumVo1(gradeNumVo);
									}
								}
							}
						}
						unitId = m.get("unitid").toString(); //获取当前记录的教学点
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//保存最后一条
				feeInfo.setUniversityInfo(universityInfo);
				feeInfo.setJuniorInfo(juniorInfo);
				feeInfo.setTypeInfoVo(typeInfoVo);
				newlist.add(feeInfo);
			}
			if("N".equals(isBrschool) && map!=null){
				//广东医
				BigDecimal studentNum1 = BigDecimal.valueOf(0);//年级人数1
				BigDecimal studentNum2 = BigDecimal.valueOf(0);//年级人数2
				BigDecimal studentNum3 = BigDecimal.valueOf(0);//年级人数3
				BigDecimal sumStudentFullNum = BigDecimal.valueOf(0);//年级人数合计
				BigDecimal sumShouldFullFees = BigDecimal.valueOf(0);//应收学费合计
				BigDecimal sumRealFullFees = BigDecimal.valueOf(0);//实收学费合计
				BigDecimal sumNotPayFullNum = BigDecimal.valueOf(0);//未交人数合计
				BigDecimal sumNotFullFees = BigDecimal.valueOf(0);//未交金额合计
				BigDecimal sumProportionPay = BigDecimal.valueOf(0);//分成金额合计
				//广外
				BigDecimal sumRealFullFees_tuition = BigDecimal.valueOf(0);//学费实缴金额（不包括教材费等）
				BigDecimal sumRoyaltyRatePay = BigDecimal.valueOf(0);//非外语类
				BigDecimal sumRoyaltyRate2Pay = BigDecimal.valueOf(0);//外语类
				BigDecimal sumShouldReturnPay = BigDecimal.valueOf(0);//应返学费
				BigDecimal sumReserveRatioPay = BigDecimal.valueOf(0);//预留金额
				BigDecimal sumFirstReturnPay = BigDecimal.valueOf(0);//一返学费金额
				BigDecimal sumSecondReturnPay = BigDecimal.valueOf(0);//二返学费金额
				BigDecimal sumShouldReturnFees = BigDecimal.valueOf(0);//本次应返金额
				// 处理实际记录
				for (FeeCommissionInfoVo feeInfoVo : newlist) {
					studentNum1 = studentNum1.add(feeInfoVo.getJuniorInfo().getGradeNumVo1().getStudentNum()).add(feeInfoVo.getUniversityInfo().getGradeNumVo1().getStudentNum());
					studentNum2 = studentNum2.add(feeInfoVo.getJuniorInfo().getGradeNumVo2().getStudentNum()).add(feeInfoVo.getUniversityInfo().getGradeNumVo2().getStudentNum());
					studentNum3 = studentNum3.add(feeInfoVo.getJuniorInfo().getGradeNumVo3().getStudentNum()).add(feeInfoVo.getUniversityInfo().getGradeNumVo3().getStudentNum());
					sumShouldFullFees = sumShouldFullFees.add(feeInfoVo.getShouldFullFees());
					sumRealFullFees = sumRealFullFees.add(feeInfoVo.getRealFullFees());
					sumNotPayFullNum = sumNotPayFullNum.add(feeInfoVo.getNotPayFullNum());
					sumNotFullFees = sumNotFullFees.add(feeInfoVo.getNotFullFees());
					sumProportionPay = sumProportionPay.add(feeInfoVo.getProportionPay());

					sumRealFullFees_tuition = sumRealFullFees_tuition.add(feeInfoVo.getRealFullFees_tuition());
					sumRoyaltyRatePay = sumRoyaltyRatePay.add(feeInfoVo.getTypeInfoVo().getRoyaltyRatePay());
					sumRoyaltyRate2Pay = sumRoyaltyRate2Pay.add(feeInfoVo.getTypeInfoVo().getRoyaltyRate2Pay());
					sumShouldReturnPay = sumShouldReturnPay.add(feeInfoVo.getTypeInfoVo().getShouldReturnPay());
					sumReserveRatioPay = sumReserveRatioPay.add(feeInfoVo.getTypeInfoVo().getReserveRatioPay());
					sumFirstReturnPay = sumFirstReturnPay.add(feeInfoVo.getFirstReturnPay());
					sumSecondReturnPay = sumSecondReturnPay.add(feeInfoVo.getSecondReturnPay());
					sumShouldReturnFees = sumShouldReturnFees.add(feeInfoVo.getShouldReturnFees());
				}
				sumStudentFullNum = studentNum1.add(studentNum2).add(studentNum3);
				map.addAttribute("studentNum1", formatInt.format(studentNum1));
				map.addAttribute("studentNum2", formatInt.format(studentNum2));
				map.addAttribute("studentNum3", formatInt.format(studentNum3));
				map.addAttribute("sumStudentFullNum", formatInt.format(sumStudentFullNum));
				map.addAttribute("sumShouldFullFees", formatInt.format(sumShouldFullFees));
				map.addAttribute("sumRealFullFees", formatInt.format(sumRealFullFees));
				map.addAttribute("sumNotPayFullNum", formatInt.format(sumNotPayFullNum));
				map.addAttribute("sumNotFullFees", formatInt.format(sumNotFullFees));
				map.addAttribute("sumSchoolProportionPay", formatInt.format(sumRealFullFees.subtract(sumProportionPay)));
				map.addAttribute("sumProportionPay", formatInt.format(sumProportionPay));

				map.addAttribute("sumRealFullFees_tuition",formatInt.format(sumRealFullFees_tuition));
				map.addAttribute("sumRealFullFees_others",formatInt.format(sumRealFullFees.subtract(sumRealFullFees_tuition)));
				map.addAttribute("sumRoyaltyRatePay", formatInt.format(sumRoyaltyRatePay));
				map.addAttribute("sumRoyaltyRate2Pay", formatInt.format(sumRoyaltyRate2Pay));
				map.addAttribute("sumShouldReturnPay", formatInt.format(sumShouldReturnPay));
				map.addAttribute("sumReserveRatioPay", formatInt.format(sumReserveRatioPay));
				map.addAttribute("sumFirstReturnPay", formatInt.format(sumFirstReturnPay));
				map.addAttribute("sumSecondReturnPay", formatInt.format(sumSecondReturnPay));
				map.addAttribute("sumShouldReturnFees", formatInt.format(sumShouldReturnFees));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newlist;
	}
	
	/**
	 * 提成明细打印
	 * @param request
	 * @param response
	 * @param model
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/edu3/finance/feeCommissionInfo/print.html")
	public void feeCommissionReportPrint(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws UnsupportedEncodingException{
		// 参数处理
		String brSchool = request.getParameter("brSchool");
		String isBrschool = request.getParameter("isBrschool");
 		String operatedate = request.getParameter("operatedate");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

 		//String flag = request.getParameter("flag");
 		String title = "";
 		String userBrSchool = "";
 		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日"); 
		String date = df.format(new Date()).toString(); 

		//DecimalFormat formatDouble = new DecimalFormat("#,###.00");
 		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if("Y".equals(isBrschool)){
			userBrSchool = orgUnitService.findUniqueByProperty("resourceid", brSchool).getUnitName();
		}
		Map<String,Object> condition = new HashMap<String, Object>(0);
		if (ExStringUtils.isNotBlank(operatedate)) {
			condition.put("operatedate", operatedate);
		} else {
			condition.put("beginDate",beginDate);
			condition.put("endDate",endDate);
		}
		if (ExStringUtils.isNotBlank(brSchool)) {
			condition.put("brSchool", brSchool);
		}
		List<Map<String, Object>> list = null;
		List<FeeCommissionInfoVo> newlist = null;
		List<Map<String, Object>> datelist;
		Map<String, Object> map = new HashMap<String, Object>(50);
		map.putAll(model);
		String jasperFile = "";
		if ("11846".equals(schoolCode)) {//广外
			list = feeCommissionInfoService.findFeeCommissionExt(condition);
			String separateDate = CacheAppManager.getSysConfigurationByCode("fee.separateDate").getParamValue();
			Map<String, BigDecimal> schoolFeeMap = getFeeForSchool(beginDate, endDate, separateDate);
			newlist = getFeeCommissionInfoVoList(list, year,model,isBrschool,schoolFeeMap);
			datelist = getDateListByFeeCommissionInfoVo_gdwy(newlist);
			title = "各成教教学点学费分成明细表";
			jasperFile = "finance"+File.separator+"feeCommissionInfoPrint_gdwy.jasper";
		} else {//广东医
			list = feeCommissionInfoService.findFeeCommissionInfoByJDBC(condition);
			newlist = getFeeCommissionInfoVoList(list, year,model,isBrschool,null);
			datelist = getDateListByFeeCommissionInfoVo_gdy(newlist);
			//添加表尾
			map.put("userBrSchool", userBrSchool);

			//添加title
			map.put("grade1", year-2+"级");
			map.put("grade2", year-1+"级");
			map.put("grade3", year+"级");
			map.put("gradeStuNumTitle", year+"年"+month+"月成人高等教育在校学生数(正常注册)（人）");
			map.put("shouldFeeTitle", year+"年学生应收学费金额(元)");
			map.put("realFeeTitle", year+"年学生实收学费金额(元)");
			map.put("proportionPayTitle", year+"年教学点分成学费金额(元)");
			map.put("month", month+"");
			if("N".equals(isBrschool)){
				jasperFile = "finance"+File.separator+"feeCommissionInfoPrint1.jasper";
			}else if("Y".equals(isBrschool)) {
				jasperFile = "finance"+File.separator+"feeCommissionInfoPrint2.jasper";
			}
			title = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			title += year+"年成人高等教育校外点学费收入提成明细表";
		}

		map.put("date", date);
		map.put("title", title);
		map.put("logoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
		try {
			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(datelist);

			jasperFile = ExFileUtils.getRealPathForReports(request,jasperFile);
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

	private List<Map<String, Object>> getDateListByFeeCommissionInfoVo_gdwy(List<FeeCommissionInfoVo> infoVoList) {
		List<Map<String, Object>> datelist = new ArrayList<Map<String, Object>>();
		DecimalFormat formatInt = new DecimalFormat("#,###");
		Map<String, Object> map;
		for (FeeCommissionInfoVo fee : infoVoList) {
			map = new HashMap<String, Object>();
			map.put("ordinal", fee.getOrdinal());
			map.put("unitName", fee.getUnitName());
			map.put("realFullFees_tuition",formatInt.format(fee.getRealFullFees_tuition()));
			map.put("royaltyRatePay",formatInt.format(fee.getTypeInfoVo().getRoyaltyRatePay()));
			map.put("royaltyRate",fee.getTypeInfoVo().getRoyaltyRate()+"%");
			map.put("royaltyRate2Pay",formatInt.format(fee.getTypeInfoVo().getRoyaltyRate2Pay()));
			map.put("royaltyRate2",fee.getTypeInfoVo().getRoyaltyRate2()+"%");
			map.put("shouldReturnPay",formatInt.format(fee.getTypeInfoVo().getShouldReturnPay()));
			map.put("reserveRatio",fee.getTypeInfoVo().getReserveRatio()+"%");
			map.put("reserveRatioPay",formatInt.format(fee.getTypeInfoVo().getReserveRatioPay()));
			map.put("firstReturnPay",formatInt.format(fee.getFirstReturnPay()));
			map.put("secondReturnPay",formatInt.format(fee.getSecondReturnPay()));
			map.put("shouldReturnFees",formatInt.format(fee.getShouldReturnFees()));
			map.put("memo",fee.getMemo());
			datelist.add(map);
		}
		return  datelist;
	}

	private List<Map<String, Object>> getDateListByFeeCommissionInfoVo_gdy(List<FeeCommissionInfoVo> infoVoList) {
		List<Map<String, Object>> datelist = new ArrayList<Map<String, Object>>();
		DecimalFormat formatInt = new DecimalFormat("#,###");
		Map<String, Object> map;
		for (FeeCommissionInfoVo fee : infoVoList) {
			map =  new HashMap<String, Object>();
			//第一行专科及合并行数据
			map.put("ordinal", fee.getOrdinal());
			map.put("unitName", fee.getUnitName());
			map.put("zkClassicName", fee.getJuniorInfo().getClassicName());
			map.put("zkGrade1StuNum", formatInt.format(fee.getJuniorInfo().getGradeNumVo1().getStudentNum()));
			map.put("zkGrade2StuNum", formatInt.format(fee.getJuniorInfo().getGradeNumVo2().getStudentNum()));
			map.put("zkGrade3StuNum", formatInt.format(fee.getJuniorInfo().getGradeNumVo3().getStudentNum()));
			map.put("zkGradeSubtotal", formatInt.format(fee.getJuniorInfo().getStudentNumSubtotal()));
			map.put("studentFullNum", formatInt.format(fee.getStudentFullNum()).toString());
			map.put("zkFeeRule", formatInt.format(fee.getJuniorInfo().getStudentFeeRule()));
			map.put("zkShouldFees", formatInt.format(fee.getJuniorInfo().getShouldFees()));
			map.put("shouldFullFees", formatInt.format(fee.getShouldFullFees()));
			map.put("zkRealFees", formatInt.format(fee.getJuniorInfo().getRealFees()));
			map.put("realFullFees", formatInt.format(fee.getRealFullFees()));
			map.put("zknotPayNum", fee.getJuniorInfo().getNotPayNum().toString());
			map.put("notPayFullNum", fee.getNotPayFullNum().toString());
			map.put("zkNotFees", formatInt.format(fee.getJuniorInfo().getNotFees()));
			map.put("notFullFees", formatInt.format(fee.getNotFullFees()));
			map.put("schoolProportion", formatInt.format(fee.getSchoolProportion()));
			map.put("schoolProportionPay", formatInt.format(fee.getSchoolProportionPay()));
			map.put("proportion", formatInt.format(fee.getProportion()));
			map.put("proportionPay", formatInt.format(fee.getProportionPay()));
			//第二行本科
			map.put("bkClassicName", fee.getUniversityInfo().getClassicName());
			map.put("bkGrade1StuNum", formatInt.format(fee.getUniversityInfo().getGradeNumVo1().getStudentNum()));
			map.put("bkGrade2StuNum", formatInt.format(fee.getUniversityInfo().getGradeNumVo2().getStudentNum()));
			map.put("bkGrade3StuNum", formatInt.format(fee.getUniversityInfo().getGradeNumVo3().getStudentNum()));
			map.put("bkGradeSubtotal", formatInt.format(fee.getUniversityInfo().getStudentNumSubtotal()));
			map.put("bkFeeRule", formatInt.format(fee.getUniversityInfo().getStudentFeeRule()));
			map.put("bkShouldFees",formatInt.format(fee.getUniversityInfo().getShouldFees()));
			map.put("bkRealFees", formatInt.format(fee.getUniversityInfo().getRealFees()));
			map.put("bknotPayNum", formatInt.format(fee.getUniversityInfo().getNotPayNum()));
			map.put("bkNotFees", formatInt.format(fee.getUniversityInfo().getNotFees()));

			datelist.add(map);
		}
		return datelist;
	}
	
	/**
	 * 获取转教学点分成信息
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param separateDate
	 * @return
	 */
	private Map<String, BigDecimal> getFeeForSchool(String beginDate,String endDate, String separateDate) {
		Map<String, BigDecimal> schoolFeeMap = null;
		try {
			List<TransferSchoolFeeVo> schoolFeeList = feeCommissionInfoService.findChangeUnitFeeInfo(beginDate, endDate, separateDate);
			if(ExCollectionUtils.isNotEmpty(schoolFeeList)){
				schoolFeeMap = new HashMap<String, BigDecimal>(50);
				for(TransferSchoolFeeVo ts : schoolFeeList){
					// 50%的学费
					BigDecimal money = ts.getMoney();
					// 异动前的教学点
					BigDecimal bfm =(schoolFeeMap.get(ts.getBeforeSchoolId())==null?BigDecimal.ZERO:schoolFeeMap.get(ts.getBeforeSchoolId()));
					bfm.add(money);
					schoolFeeMap.put(ts.getBeforeSchoolId(), bfm);
					// 异动后的教学点
					BigDecimal m =(schoolFeeMap.get(ts.getSchoolId())==null?BigDecimal.ZERO:schoolFeeMap.get(ts.getSchoolId()));
					m.subtract(money);
					schoolFeeMap.put(ts.getSchoolId(), m);
				}
			}
		} catch (Exception e) {
			logger.error("获取转教学点分成信息出错", e);
		}
		
		return schoolFeeMap;
	}
}
