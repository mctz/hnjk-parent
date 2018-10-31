package com.hnjk.edu.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.model.FeeMajor;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.model.TextbookFee;
import com.hnjk.edu.finance.model.YearPaymentStandard;
import com.hnjk.edu.finance.model.YearPaymentStandardDetails;
import com.hnjk.edu.finance.service.IAnnualFeesService;
import com.hnjk.edu.finance.service.IFeeMajorService;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.service.ITextbookFeeService;
import com.hnjk.edu.finance.service.IYearPaymentStandardService;
import com.hnjk.edu.finance.util.TonlyPayUtil;
import com.hnjk.edu.finance.vo.HeadVO;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.PayOfflineVo;
import com.hnjk.edu.netty.common.NettyConstants;
import com.hnjk.edu.netty.common.StringUtil;
import com.hnjk.edu.netty.vo.PaymentReq;
import com.hnjk.edu.netty.vo.PaymentResp;
import com.hnjk.edu.netty.vo.QueryAssessmentReq;
import com.hnjk.edu.netty.vo.QueryAssessmentResp;
import com.hnjk.edu.netty.vo.RequestBase;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IReginfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 11, 2016 4:04:34 PM
 * 
 */
@Transactional
@Service("tempStudentFeeService")
public class TempStudentFeeServiceImpl extends BaseServiceImpl<TempStudentFee>
		implements ITempStudentFeeService {

	@Autowired
	@Qualifier("yearPaymentStandardService")
	private IYearPaymentStandardService yearPaymentStandardService;

	@Autowired
	@Qualifier("feeMajorService")
	private IFeeMajorService feeMajorService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;

	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;// 报名信息服务

	@Autowired
	@Qualifier("reginfoservice")
	private IReginfoService reginfoService;

	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService;

	@Autowired
	@Qualifier("annualFeesService")
	private IAnnualFeesService annualFeesService;

	@Autowired
	@Qualifier("textbookFeeService")
	private ITextbookFeeService textbookFeeService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 根据条件获取学生注册缴费信息列表
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findByContidion(Map<String, Object> condition, Page objPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = createSelectHQL(condition, params);

		if (objPage.isOrderBySetted()) {
			hql.append(" order by " + objPage.getOrderBy() + " "
					+ objPage.getOrder());
		}

		return findByHql(objPage, hql.toString(), params);
	}

	/**
	 * 查询列表HQL
	 * 
	 * @param condition
	 * @param params
	 * @return
	 */
	private StringBuffer createSelectHQL(Map<String, Object> condition,
			Map<String, Object> params) {
		StringBuffer hql = new StringBuffer("from "
				+ TempStudentFee.class.getSimpleName()
				+ " tsf where tsf.isDeleted=:isDeleted ");
		params.put("isDeleted", 0);
		if (condition.containsKey("studentName")) {
			hql.append(" and tsf.studentName like :studentName  ");
			params.put("studentName", "%" + condition.get("studentName") + "%");
		}
		if (condition.containsKey("gradeId")) {
			hql.append(" and tsf.grade.resourceid=:gradeId  ");
			params.put("gradeId", condition.get("gradeId"));
		}
		if (condition.containsKey("examCertificateNo")) {
			hql.append(" and tsf.examCertificateNo=:examCertificateNo  ");
			params.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		if (condition.containsKey("certNum")) {
			hql.append(" and tsf.certNum=:certNum  ");
			params.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("studyNoType")) {
			hql.append(" and tsf.studyNoType=:studyNoType  ");
			params.put("studyNoType", condition.get("studyNoType"));
		}
		if (condition.containsKey("isUploaded")) {
			hql.append(" and tsf.isUploaded=:isUploaded  ");
			params.put("isUploaded", condition.get("isUploaded"));
		}
		if (condition.containsKey("eduOrederNo")) {
			hql.append(" and tsf.eduOrderNo=:eduOrederNo  ");
			params.put("eduOrederNo", condition.get("eduOrederNo"));
		}
		if (condition.containsKey("batchNo")) {
			hql.append(" and tsf.batchNo=:batchNo  ");
			params.put("batchNo", condition.get("batchNo"));
		}
		if (condition.containsKey("unitId")) {
			hql.append(" and tsf.unit.resourceid=:unitId  ");
			params.put("unitId", condition.get("unitId"));
		}
		if (condition.containsKey("branchSchoolId")) {
			hql.append(" and tsf.unit.resourceid=:branchSchoolId  ");
			params.put("branchSchoolId", condition.get("branchSchoolId"));
		}
		if (condition.containsKey("payStatus")) {
			hql.append(" and tsf.payStatus=:payStatus  ");
			params.put("payStatus", condition.get("payStatus"));
		}
		if (condition.containsKey("hasStudentInfo")) {
			hql.append(" and tsf.hasStudentInfo=:hasStudentInfo  ");
			params.put("hasStudentInfo", condition.get("hasStudentInfo"));
		}
		if (condition.containsKey("majorId")) {
			hql.append(" and tsf.major.resourceid=:majorId ");
			params.put("majorId", condition.get("majorId"));
		}
		/*
		 * if(condition.containsKey("certNum")){
		 * hql.append(" and tsf.enrolleeInfo.studentBaseInfo.certNum=:certNum  "
		 * ); params.put("certNum", condition.get("certNum")); }
		 */
		if (condition.containsKey("isReconciliation")) {
			hql.append(" and tsf.isReconciliation=:isReconciliation  ");
			params.put("isReconciliation", condition.get("isReconciliation"));
		}
		if (condition.containsKey("handleStatus")) {
			hql.append(" and tsf.handleStatus=:handleStatus  ");
			params.put("handleStatus", condition.get("handleStatus"));
		}
		if (condition.containsKey("chargingItems")) {
			hql.append(" and tsf.chargingItems=:chargingItems  ");
			params.put("chargingItems", condition.get("chargingItems"));
		}
		if(condition.containsKey("yearId")){
			hql.append(" and tsf.yearInfo.resourceid=:yearId ");
			params.put("yearId", condition.get("yearId"));
		}
		return hql;
	}

	/**
	 * 根据条件获取学生注册缴费信息列表
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public List<TempStudentFee> findListByContidion(
			Map<String, Object> condition) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = createSelectHQL(condition, params);
		if (condition.containsKey("resourceids")) {
			hql.append(" and resourceid in(:resourceids)");
			params.put("resourceids", condition.get("resourceids"));
		}
		if (condition.containsKey("order")) {
			hql.append(" " + condition.get("order"));
		}

		return findByHql(hql.toString(), params);
	}

	/**
	 * 生成注册缴费标准（注册时使用）
	 * 
	 * @param enrolleeInfoList
	 * @param chargingItems
	 * @return
	 */
	@Override
	public Map<String, Object> createTempStuFee(
			List<EnrolleeInfo> enrolleeInfoList, String chargingItems) {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer message = new StringBuffer();
		Map<String, YearPaymentStandard> ypsMap = new HashMap<String, YearPaymentStandard>();
		Map<String, YearPaymentStandardDetails> ypsdMap = new HashMap<String, YearPaymentStandardDetails>();
		Map<String, FeeMajor> feeMajorMap = new HashMap<String, FeeMajor>();
		Map<String, String> messageMap = new HashMap<String, String>();
		String msg = "";
		try {
			if (ExCollectionUtils.isNotEmpty(enrolleeInfoList)) {
				String uniqueId = CacheAppManager.getSysConfigurationByCode(
						"exameeInfo.uniqueId").getParamValue();
				List<TempStudentFee> tempStuFeeList = new ArrayList<TempStudentFee>();
				TempStudentFee tsf = null;
				// 学校内部批次编号

				String batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
				// 当前年
				String currentYear = CacheAppManager.getSysConfigurationByCode("currentYear").getParamValue();
				YearInfo year = yearInfoService.getByFirstYear(Long.valueOf(currentYear));
				Double money =0d;

				String schoolOrderNo = null;
				Major major = null;
				for (EnrolleeInfo ei : enrolleeInfoList) {
					// TODO:已分配教学点的学生才同步？
					// 是否已生成
					String examCertificateNo = ei.getExamCertificateNo();
					if ("1".equals(uniqueId)) {
						examCertificateNo = ei.getEnrolleeCode();
					}
					TempStudentFee tempStuFee = getByExamNoAndBatchNo(
							examCertificateNo, batchNo, Constants.BOOLEAN_NO,
							chargingItems);
					// 已有记录不做处理
					if (tempStuFee != null) {
						continue;
					}
					RecruitMajor recruitMajor = ei.getRecruitMajor();// 招生专业
					major = recruitMajor.getMajor();// 基础专业
					Grade grade = ei.getGrade();// 年级
					YearInfo yearInfo = grade.getYearInfo();// 年度
					String tbfKey = null;

					if("textbookFee".equals(chargingItems)){// 教材费处理逻辑

						// 处理重复问题
						StringBuffer ym = new StringBuffer(200);
						ym.append(year.getResourceid()).append("_tbf_").append(major.getResourceid());
						tbfKey = ym.toString();
						if (messageMap.containsKey(tbfKey)) {
							continue;
						}
						// 新生的教材费的年度不能比录取年度早
						if(yearInfo.getFirstYear().compareTo(year.getFirstYear())>0){
							msg = "准考证号: "+examCertificateNo+"新生的教材费年度不能早于录取年度.<br>";
							message.append(msg);
							continue;
						}
						// 获取年教材费标准
						TextbookFee tbf = textbookFeeService.findByYearAndMajor(year.getResourceid(), major.getResourceid());
						if(tbf == null){
							msg = "专业: "+major.getMajorName()+"没有设置年教材费标准,无法生成教材费订单.<br>";
							messageMap.put(tbfKey, msg);
							message.append(msg);
							continue;
						}
						// 金额
						money = tbf.getMoney();
						// 学校内部订单编号,2开头代表教材费
						StringBuffer son = new StringBuffer(30);
						son.append("2").append(batchNo).append(examCertificateNo);
						schoolOrderNo = son.toString();

					} else {// 学费处理逻辑
						FeeMajor feeMajor = null;
						if (feeMajorMap.containsKey(major.getResourceid())) {
							feeMajor = feeMajorMap.get(major.getResourceid());
						} else {
							if (messageMap.containsKey(major.getResourceid())) {
								continue;
							}
							feeMajor = feeMajorService.get(major.getResourceid());
							if (feeMajor == null || ExStringUtils.isBlank(feeMajor.getPaymentType())) {
								msg = "专业: " + major.getMajorName()
										+ "没有设置缴费类别,无法生成学生缴费标准.<br>";
								messageMap.put(major.getResourceid(), msg);
								message.append(msg);
								continue;
							}
							feeMajorMap.put(major.getResourceid(), feeMajor);
						}
						String paymentType = feeMajor.getPaymentType();// 缴费类别
						String year_paymentType_key = yearInfo.getResourceid()
								+ "_" + paymentType;
						YearPaymentStandard yearPaymentStandard = null;
						if (ypsMap.containsKey(year_paymentType_key)) {
							yearPaymentStandard = ypsMap.get(year_paymentType_key);
						} else {
							if (messageMap.containsKey(year_paymentType_key)) {
								continue;
							}
							yearPaymentStandard = yearPaymentStandardService.getYearPaymentStandard(grade.getResourceid(), paymentType);// 年缴费标准
							if (yearPaymentStandard == null
									|| ExCollectionUtils.isEmpty(yearPaymentStandard.getYearPaymentStandardDetails())) {
								msg = grade.getGradeName()+ " "+ JstlCustomFunction.dictionaryCode2Value("CodePaymentType",paymentType)
										+ "的年缴费标准还未设置.<br>";
								messageMap.put(year_paymentType_key, msg);
								message.append(msg);
								continue;
							}
							ypsMap.put(year_paymentType_key,yearPaymentStandard);
						}
						// 学校内部订单编号,1开头代表学费
						StringBuffer son = new StringBuffer(30);
						son.append("1").append(batchNo).append(examCertificateNo);
						schoolOrderNo = son.toString();

						Date earlistDate = null;
						YearPaymentStandardDetails firstPayment = null;
						if (ypsdMap.containsKey(year_paymentType_key)) {
							firstPayment = ypsdMap.get(year_paymentType_key);
						} else {
							for (YearPaymentStandardDetails payment : yearPaymentStandard
									.getYearPaymentStandardDetails()) {
								// 获取第一年缴费标准
								if (earlistDate == null) {
									earlistDate = payment.getCreditEndDate();
									firstPayment = payment;
								} else {
									if (earlistDate.after(payment
											.getCreditEndDate())) {
										earlistDate = payment
												.getCreditEndDate();
										firstPayment = payment;
									}
								}
							}
							ypsdMap.put(year_paymentType_key, firstPayment);
						}
						// 金额
						money = firstPayment.getCreditFee();
					}

					// 生成预缴费订单信息

					tsf = createAdvanceOrder(chargingItems, batchNo, money, schoolOrderNo, major, ei, examCertificateNo, grade,null,Constants.BOOLEAN_NO,year);
					

					tempStuFeeList.add(tsf);
				}
				// 批量保存学生缴费标准
				batchSaveOrUpdate(tempStuFeeList);
			} else {
				message.append("没有要处理的数据");
			}
		} catch (Exception e) {
			logger.error("生成注册缴费标准出错", e);
			message.setLength(0);
			message.append("生成失败");
		} finally {
			// 有失败的记录
			if (message.length() > 0) {
				statusCode = 300;
			}
			map.put("statusCode", statusCode);
			map.put("message", message.toString());
		}

		return map;
	}

	/**
	 * 创建于缴费订单
	 * 
	 * @param chargingItems
	 * @param batchNo
	 * @param money
	 * @param schoolOrderNo
	 * @param major
	 * @param ei
	 * @param examCertificateNo
	 * @param grade
	 * @param si
	 * @param hasStudentInfo
	 * @param yearInfo
	 * @return
	 */

	private TempStudentFee createAdvanceOrder(String chargingItems, String batchNo,Double money, String schoolOrderNo,
			 Major major, EnrolleeInfo ei, String examCertificateNo, Grade grade,StudentInfo si,String hasStudentInfo,YearInfo yearInfo) {

		TempStudentFee tsf;
		tsf = new TempStudentFee();
		tsf.setGrade(grade);
		tsf.setEnrolleeInfo(ei);
		tsf.setExamCertificateNo(examCertificateNo);
		tsf.setAmount(money);
		tsf.setBatchNo(batchNo);
		tsf.setSchoolOrderNo(schoolOrderNo);
		tsf.setHasStudentInfo(hasStudentInfo);
		tsf.setMajor(major);
		tsf.setStudyNoType("2");// 18级以后都是使用身份证号作为通联支付对接的学号
		// 由于没有使用通联支付，所以目前写进行硬编码，以后如果使用多种就要做成全局参数
		tsf.setIsUploaded(Constants.BOOLEAN_YES);
		tsf.setChargingItems(chargingItems);
		tsf.setStudentInfo(si);
		if (hasStudentInfo.equals(Constants.BOOLEAN_YES)) {
			tsf.setUnit(si.getBranchSchool());
			tsf.setStudentName(si.getStudentBaseInfo().getName());
			tsf.setCertNum(si.getStudentBaseInfo().getCertNum());
		} else {
			tsf.setUnit(ei.getBranchSchool());
			tsf.setStudentName(ei.getStudentBaseInfo().getName());
			tsf.setCertNum(ei.getStudentBaseInfo().getCertNum());
		}

		tsf.setYearInfo(yearInfo);

		return tsf;
	}

	/**
	 * 处理新增订单返回的异步信息
	 * 
	 * @param xmlData
	 */
	@Override
	public void handleOrderResult(String xmlData) {
		try {
			Document returnDoc = XMLUtils.parseText(xmlData);
			Element root = returnDoc.getRootElement();
			Element body = XMLUtils.getChild(root, "BODY");
			List<Element> dataList = XMLUtils
					.getChildElements(body, "DATALIST");
			if (ExCollectionUtils.isNotEmpty(dataList)) {
				// String studentId = null;
				String eduOrederNo = null;
				String receiptCode = null;
				String schoolOrderNo = null;
				String batchNo = CacheAppManager.getSysConfigurationByCode(
						"payment_batchNo").getParamValue();
				List<TempStudentFee> saveList = new ArrayList<TempStudentFee>();
				for (Element data : dataList) {
					receiptCode = XMLUtils.getChild(data, "RECEIPT_CODE")
							.getText();
					if (Constants.RETURN_STATUS_SUCESS.equals(receiptCode)) {// 成功
					// studentId = XMLUtils.getChild(data,
					// "STUDENT_ID").getText();
						eduOrederNo = XMLUtils.getChild(data, "EDU_OREDER_NO")
								.getText();
						schoolOrderNo = XMLUtils.getChild(data, "ORDER_NO")
								.getText();
						TempStudentFee tempStuFee = findUniqueByProperty(
								"schoolOrderNo", schoolOrderNo);
						if (tempStuFee != null) {
							tempStuFee.setIsUploaded("Y");// 已同步到通联
							tempStuFee.setEduOrderNo(eduOrederNo);// 教育系统订单号
							tempStuFee.setBatchNo(batchNo);// 学校内部批次编号
							saveList.add(tempStuFee);
						}
					}
					// 失败不做处理
				}
				batchSaveOrUpdate(saveList);
			}
		} catch (DocumentException e) {
			logger.error("解析新增订单异步通知出错", e);
		} catch (Exception e) {
			logger.error("处理新增订单返回的异步信息出错", e);
		}

	}

	/**
	 * 生成学生缴费信息（供第三方使用）
	 * 
	 * @param studentPaymentList
	 * @return
	 */
	@Override
	public Map<String, Object> createPayTempStuFee(
			List<StudentPayment> studentPaymentList) {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer message = new StringBuffer();
		try {
			do {
				if (ExCollectionUtils.isEmpty(studentPaymentList)) {
					statusCode = 300;
					message.append("没有要处理的数据");
					continue;
				}
				TempStudentFee tsf = null;
				List<TempStudentFee> tempStuFeeList = new ArrayList<TempStudentFee>();
				String uniqueId = CacheAppManager.getSysConfigurationByCode(
						"exameeInfo.uniqueId").getParamValue();
				// 学校内部批次编号
				String batchNo = CacheAppManager.getSysConfigurationByCode(
						"payment_batchNo").getParamValue();
				// 获取当前年
				String currentYear = CacheAppManager.getSysConfigurationByCode("currentYear").getParamValue();
				YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(currentYear));
				// 收费项
				String chargingItems = "tuition";
				for (StudentPayment sp : studentPaymentList) {
					String examCertificateNo = sp.getStudentInfo()
							.getExamCertificateNo();
					if ("1".equals(uniqueId)) {
						examCertificateNo = sp.getStudentInfo()
								.getEnrolleeCode();
					}
					tsf = getByExamNoAndBatchNo(examCertificateNo, batchNo,
							Constants.BOOLEAN_YES, chargingItems);
					if (tsf != null) {
						continue;
					}

					double recpayFee = sp.getRecpayFee() == null ? 0 : sp
							.getRecpayFee();
					double feacepayFee = sp.getFacepayFee() == null ? 0 : sp
							.getFacepayFee();
					double amount = recpayFee - feacepayFee;
					if ("1".equals(sp.getChargeStatus()) || amount == 0) {
						continue;
					}
	
					tsf = createTempStudentFee(batchNo, sp, examCertificateNo,amount,yearInfo);
					

					tempStuFeeList.add(tsf);
				}
				if (ExCollectionUtils.isEmpty(tempStuFeeList)) {
					statusCode = 300;
					message.append("没有要处理的数据");
					continue;
				}
				// 批量保存学生缴费标准
				batchSaveOrUpdate(tempStuFeeList);
			} while (false);
		} catch (Exception e) {
			statusCode = 300;
			message.append("生成缴费临时记录失败");
			logger.error("处理生成缴费临时记录逻辑出错", e);
		} finally {
			// 有失败的记录
			if (message.length() > 0) {
				statusCode = 300;
			}
			map.put("statusCode", statusCode);
			map.put("message", message.toString());
		}

		return map;
	}

	/**
	 * 创建注册缴费信息记录
	 * 
	 * @param batchNo
	 * @param sp
	 * @param examCertificateNo
	 * @param amount
	 * @param yearInfo
	 * @return
	 */
	@Override

	public TempStudentFee createTempStudentFee(String batchNo,StudentPayment sp, String examCertificateNo, double amount,YearInfo yearInfo) {

		// 生成临时的注册缴费信息
		TempStudentFee tsf = new TempStudentFee();
		tsf.setGrade(sp.getGrade());
		tsf.setUnit(sp.getBranchSchool());
		tsf.setExamCertificateNo(examCertificateNo);
		tsf.setStudentName(sp.getStudentInfo().getStudentName());
		tsf.setAmount(amount);
		tsf.setBatchNo(batchNo);
		tsf.setSchoolOrderNo("1" + batchNo + examCertificateNo);
		// TODO: 使用通联支付接口要设置为N
		tsf.setIsUploaded(Constants.BOOLEAN_YES);
		// 添加学籍信息id
		tsf.setStudentInfo(sp.getStudentInfo());
		tsf.setHasStudentInfo(Constants.BOOLEAN_YES);
		tsf.setMajor(sp.getStudentInfo().getMajor());
		tsf.setCertNum(sp.getStudentInfo().getStudentBaseInfo().getCertNum());
		tsf.setStudyNoType(sp.getStudyNoType());
		tsf.setChargingItems("tuition");// 学费
		tsf.setYearInfo(yearInfo);
		return tsf;
	}

	/**
	 * 根据支付状态和教育系统订单号获取实体
	 * 
	 * @param payStatus
	 * @param eduOrderNo
	 * @return
	 */
	@Override
	public TempStudentFee findByPayStatusAndOrderNo(String payStatus,
			String eduOrderNo) {
		String hql = "from "
				+ TempStudentFee.class.getSimpleName()
				+ " tsf where tsf.isDeleted=0 and tsf.payStatus=? and tsf.eduOrderNo=?";
		return findUnique(hql, payStatus, eduOrderNo);
	}

	/**
	 * 补缴费记录
	 * 
	 * @param resourceid
	 * @param resourceid
	 * @return
	 */
	@Override
	public Map<String, Object> makeRecord(String resourceid, HeadVO headVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			do {
				TempStudentFee tempStudentFee = get(resourceid);
				if (tempStudentFee == null) {
					statusCode = 300;
					message = "补缴费记录失败";
					continue;
				}
				if (tempStudentFee.getEduOrderNo() != null
						&& "Y".equals(tempStudentFee.getIsUploaded())
						&& Constants.FEE_PAYSTATUS_UNPAY.equals(tempStudentFee
								.getPayStatus())) {
					Document returnDoc = TonlyPayUtil.queryPayment(headVO,
							tempStudentFee.getEduOrderNo());
					Element root = returnDoc.getRootElement();
					Element body = XMLUtils.getChild(root, "BODY");
					Element dataList = XMLUtils.getChild(body, "DATALIST");
					if (dataList != null) {
						// 支付状态
						Element payResultElement = XMLUtils.getChild(dataList,
								"PAY_RESULT");
						if (payResultElement == null) {
							statusCode = 300;
							message = XMLUtils.getChild(dataList, "RETURN_MSG")
									.getText();
							continue;
						}
						String payResult = payResultElement.getText();
						if (!Constants.RETURN_STATUS_SUCESS.equals(payResult)) {
							statusCode = 300;
							message = "缴费失败";
							continue;
						}
						// 考生号或准考证号，（长号）
						/*
						 * String examcertificateno =
						 * XMLUtils.getChild(dataList, "STUDENT_ID").getText();
						 * if(ExStringUtils.isEmpty(examcertificateno)){
						 * statusCode = 300; message = "学号为空"; continue; }
						 */

						// 支付流水号 2017-02-20 修改订单处理结果接口
						String payNo = XMLUtils.getChild(dataList, "PAY_NO")
								.getText();
						if (ExStringUtils.isEmpty(payNo)) {
							statusCode = 300;
							message = "支付流水号为空";
							continue;
						}
						// 支付时间
						String payTime = XMLUtils
								.getChild(dataList, "PAY_TIME").getText();
						if (ExStringUtils.isEmpty(payTime)) {
							statusCode = 300;
							message = "支付时间为空";
							continue;
						}
						Date _payTime = ExDateUtils
								.convertToDateTimeCombine(payTime);
						/**
						 * 支付类型 01-代收，09-网银支付，10-POS支付，11-现金支付，
						 * 12-移动支付，13-外卡支付，14-微信支付，15-通联钱包
						 */
						String payType = XMLUtils
								.getChild(dataList, "PAY_TYPE").getText();
						if (ExStringUtils.isEmpty(payType)) {
							statusCode = 300;
							message = "支付类型为空";
							continue;
						}
						// 支付金额
						String amount = XMLUtils.getChild(dataList, "AMT")
								.getText();
						if (ExStringUtils.isEmpty(amount)) {
							statusCode = 300;
							message = "金额为空";
							continue;
						}
						BigDecimal payAmount = (new BigDecimal(amount)
								.divide(BigDecimal.valueOf(100))).setScale(2);
						if (BigDecimal.ZERO.equals(payAmount)) {
							statusCode = 300;
							message = "金额为0";
							continue;
						}
						// 教育系统订单号
						String eduOrderNo = XMLUtils.getChild(dataList,
								"EDU_OREDER_NO").getText();
						if (ExStringUtils.isEmpty(eduOrderNo)) {
							statusCode = 300;
							message = "教育系统订单号为空";
							continue;
						}
						// 判断该订单是否已推送过，有则不作处理
						TempStudentFee _tempStudentFee = findUniqueByProperty(
								"eduOrderNo", eduOrderNo);
						if (_tempStudentFee == null
								|| (tempStudentFee != null && Constants.FEE_PAYSTATUS_PAYED
										.equals(_tempStudentFee.getPayStatus()))) {
							continue;
						}

						String examcertificateno = _tempStudentFee
								.getExamCertificateNo();
						// 处理注册并生成缴费信息
						// 获取唯一标识
						SysConfiguration config = CacheAppManager
								.getSysConfigurationByCode("exameeInfo.uniqueId");
						int uniqueId = 0;
						if (config != null
								&& "1".equals(config.getParamValue())) {
							uniqueId = 1;
						}
						// 网上缴费逻辑
						/*
						 * User user = SpringSecurityHelper.getCurrentUser();
						 * if(user==null){ user =
						 * userService.getUserByLoginId("hnjk"); }
						 */
						User user = userService.getUserByLoginId("hnjk");
						PayDetailsVO payDetailsVO = new PayDetailsVO();
						payDetailsVO.setPayAmount(payAmount.doubleValue());
						payDetailsVO.setEduOrederNo(eduOrderNo);
						// 2017-02-20 修改订单处理结果接口
						payDetailsVO.setPaymentMethod(TonlyPayUtil
								.convertPayMethod(payType));
						payDetailsVO.setPayNo(payNo);
						payDetailsVO.setPayTime(_payTime);

						// TODO:	到时由上面替换
//						payDetailsVO.setPaymentMethod(TonlyPayUtil.convertPayMethod("09"));
						// 收费项
						payDetailsVO.setChargingItems(tempStudentFee.getChargingItems());
						// 是否开单位发票
						payDetailsVO.setIsInvoicing(tempStudentFee.getIsInvoicing());
						// 单位名称
						payDetailsVO.setInvoiceTitle(tempStudentFee.getInvoiceTitle());
						
						Map<String, Object> resultMap = studentPaymentService.payOnline(examcertificateno, uniqueId,user, payDetailsVO,tempStudentFee);
						if((Integer)resultMap.get("statusCode")==300){

							statusCode = 300;
							message = (String) resultMap.get("message");
						} else {
							tempStudentFee
									.setHandleStatus(TempStudentFee.HANDLESTATUS_NONEED);
							saveOrUpdate(tempStudentFee);
						}
					}
				} else {
					statusCode = 300;
					message = "没有要操作的记录";
				}
			} while (false);
		} catch (Exception e) {
			statusCode = 300;
			message = "补缴费记录失败";
			logger.error("补缴费记录出错", e);
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}

		return map;
	}

	/**
	 * 根据长号（考生号或准考证号）和批次号获取实体类
	 * 
	 * @param examCertificateNo
	 * @param batchNo
	 * @param hasStudentInfo
	 * @param chargingItems
	 * @return
	 */
	@Override
	public TempStudentFee getByExamNoAndBatchNo(String examCertificateNo,
			String batchNo, String hasStudentInfo, String chargingItems) {
		String hql = "from "
				+ TempStudentFee.class.getSimpleName()
				+ " tsf where tsf.isDeleted=0 and tsf.examCertificateNo=? and tsf.batchNo=? and tsf.hasStudentInfo=? and tsf.chargingItems=?";
		return findUnique(hql, examCertificateNo, batchNo, hasStudentInfo,
				chargingItems);
	}

	/**
	 * 处理注销订单返回的异步信息
	 * 
	 * @param xmlData
	 */
	@Override
	public void handleDeleteResult(String xmlData) {
		try {
			Document returnDoc = XMLUtils.parseText(xmlData);
			Element root = returnDoc.getRootElement();
			Element body = XMLUtils.getChild(root, "BODY");
			List<Element> dataList = XMLUtils
					.getChildElements(body, "DATALIST");
			if (ExCollectionUtils.isNotEmpty(dataList)) {
				String eduOrederNo = null;
				String receiptCode = null;
				List<TempStudentFee> saveList = new ArrayList<TempStudentFee>();
				String remark = "注销订单" + ExDateUtils.getCurrentDate();
				for (Element data : dataList) {
					receiptCode = XMLUtils.getChild(data, "RECEIPT_CODE")
							.getText();
					eduOrederNo = XMLUtils.getChild(data, "EDU_OREDER_NO")
							.getText();
					if (Constants.RETURN_STATUS_SUCESS.equals(receiptCode)) {// 成功,删除学生注册缴费记录就行,不用级联学生缴费信息
						TempStudentFee tempStuFee = findByPayStatusAndOrderNo(
								"1", eduOrederNo);
						if (tempStuFee != null) {
							tempStuFee.setIsDeleted(1);// 设置删除状态
							tempStuFee.setRemark(remark);
							saveList.add(tempStuFee);
						}
					} else {// 失败打印日志
						logger.info("教育系统订单号:"
								+ eduOrederNo
								+ "注销订单信息失败--- "
								+ XMLUtils.getChild(data, "RECEIPT_MSG")
										.getText());
					}
				}
				batchSaveOrUpdate(saveList);
			}
		} catch (DocumentException e) {
			logger.error("解析注销订单异步通知出错", e);
		} catch (Exception e) {
			logger.error("处理注销订单返回的异步信息出错", e);
		}
	}

	/**
	 * 处理修改学生信息返回的异步信息
	 * 
	 * @param xmlData
	 */
	@Override
	public void handleEditStudentInfo(String xmlData) {
		try {
			Document returnDoc = XMLUtils.parseText(xmlData);
			Element root = returnDoc.getRootElement();
			Element body = XMLUtils.getChild(root, "BODY");
			List<Element> dataList = XMLUtils
					.getChildElements(body, "DATALIST");
			if (ExCollectionUtils.isNotEmpty(dataList)) {
				String receiptCode = null;
				for (Element data : dataList) {
					receiptCode = XMLUtils.getChild(data, "RECEIPT_CODE")
							.getText();
					if (Constants.RETURN_STATUS_SUCESS.equals(receiptCode)) {
						logger.info(XMLUtils.getChild(data, "STUDENT_ID")
								.getText() + ":修改学生信息成功");
					} else {// 失败打印日志
						logger.info(XMLUtils.getChild(data, "STUDENT_ID")
								.getText()
								+ "修改学生信息失败--- "
								+ XMLUtils.getChild(data, "RECEIPT_MSG")
										.getText());
					}
				}
			}
		} catch (DocumentException e) {
			logger.error("解析修改学生信息异步通知出错", e);
		} catch (Exception e) {
			logger.error("处理修改学生信息返回的异步信息出错", e);
		}
	}

	/**
	 * 申请第三方注销学生缴费信息的异步通知结果(第二个反馈,另一种方式支付了,将订单注销,设置管理系统记录为已付款)
	 * 
	 */
	@Override
	public void handleDeleteResultSecond(String xmlData) {
		try {
			Document returnDoc = XMLUtils.parseText(xmlData);
			Element root = returnDoc.getRootElement();
			Element body = XMLUtils.getChild(root, "BODY");
			List<Element> dataList = XMLUtils
					.getChildElements(body, "DATALIST");
			if (ExCollectionUtils.isNotEmpty(dataList)) {
				String eduOrederNo = null;
				String receiptCode = null;
				List<TempStudentFee> saveList = new ArrayList<TempStudentFee>();
				for (Element data : dataList) {
					receiptCode = XMLUtils.getChild(data, "RECEIPT_CODE")
							.getText();
					eduOrederNo = XMLUtils.getChild(data, "EDU_OREDER_NO")
							.getText();
					if (Constants.RETURN_STATUS_SUCESS.equals(receiptCode)) {// 成功,设置记录为已付款
						TempStudentFee tempStuFee = findByPayStatusAndOrderNo(
								"1", eduOrederNo);
						if (tempStuFee != null) {
							tempStuFee.setPayStatus("2");
							;// 设置已付款状态
							saveList.add(tempStuFee);
						}
					} else {// 失败打印日志
						logger.info("教育系统订单号:"
								+ eduOrederNo
								+ "注销订单信息失败--- "
								+ XMLUtils.getChild(data, "RECEIPT_MSG")
										.getText());
					}
				}
				batchSaveOrUpdate(saveList);
			}
		} catch (DocumentException e) {
			logger.error("解析注销订单异步通知出错", e);
		} catch (Exception e) {
			logger.error("处理注销订单返回的异步信息出错", e);
		}
	}

	/**
	 * 查询学生缴费信息
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public String queryAssessment(String request) {
		String respInfo = "";
		RequestBase reqb = new RequestBase(request);
		String dealCode = reqb.getDealCode();
		QueryAssessmentResp qa = new QueryAssessmentResp();

		try {
			if (dealCode.equalsIgnoreCase(NettyConstants.DEAL_CODE_520)) {
				qa.setDealCode(NettyConstants.DEAL_CODE_520);
				// 请求初始化
				QueryAssessmentReq qar = new QueryAssessmentReq(request);
				String certNum = qar.getUserCode();// 用户身份证号，存在字母，转换为大写
				// 学校内部批次编号
				String batchNo = CacheAppManager.getSysConfigurationByCode(
						"payment_batchNo").getParamValue();
				if (isArrearage(certNum, batchNo)) {// 查询用户是否存在未缴费用
					if (isPaid(certNum, batchNo)) {
						qa.setRespCode(NettyConstants.RESP_CODE_004);// 缴费号码不存在
						qa.setRespMsg("已缴费");
					} else {
						qa.setRespCode(NettyConstants.RESP_CODE_000);
						getPaymentDetails(certNum, qa, batchNo);
					}
				} else {// 不存在
					qa.setRespCode(NettyConstants.RESP_CODE_001);// 缴费号码不存在
					qa.setRespMsg("缴费号码不存在");
				}
			} else {// 其他错误
				qa.setDealCode(dealCode);
				qa.setRespCode(NettyConstants.RESP_CODE_022);
				qa.setRespMsg("数据包长度与交易码不匹配");
			}
		} catch (Exception e) {// 其他错误
			qa.setDealCode(dealCode);
			qa.setRespCode(NettyConstants.RESP_CODE_022);
			qa.setRespMsg("Server端处理业务逻辑出错，请联系Server端技术人员处理");
			e.printStackTrace();
		} finally {
			qa.setHeadCode(StringUtil.fillWithSpace(
					String.valueOf(NettyConstants.RESP_CODE_LENGTH_520), 4));
			respInfo = qa.getQueryAssessment();
		}
		return respInfo;
	}

	public boolean isArrearage(String certNum, String batchNo) {// 是否有未缴费的名单
		boolean result = true;
		// 因为只查询一张表，所以要确保这张表的数据是准确的
		// String hql =
		// " from "+TempStudentFee.class.getSimpleName()+" where isDeleted=0 and payStatus='1' and certNum=?";
		String hql = " from "
				+ TempStudentFee.class.getSimpleName()
				+ " where isDeleted=0 and certNum=? and batchNo=? and payStatus='1'";
		List<TempStudentFee> list = this.findByHql(hql, new Object[] { certNum,
				batchNo });
		if (list.size() > 0 && list != null) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean isPaid(String certNum, String batchNo) {// 是否有未缴费的名单
		boolean result = true;
		// 因为只查询一张表，所以要确保这张表的数据是准确的
		String hql = " from "
				+ TempStudentFee.class.getSimpleName()
				+ " where isDeleted=0 and payStatus='2' and certNum=? and batchNo=?";
		// String hql =
		// " from "+TempStudentFee.class.getSimpleName()+" where isDeleted=0 and certNum=?";
		List<TempStudentFee> list = this.findByHql(hql, new Object[] { certNum,
				batchNo });
		if (list.size() > 0 && list != null) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public void getPaymentDetails(String certNum, QueryAssessmentResp qa,
			String batchNo) {// 应缴费详情
		String hql = " from "
				+ TempStudentFee.class.getSimpleName()
				+ " where isDeleted=0 and payStatus='1' and certNum=? and batchNo=?";
		List<TempStudentFee> list = this.findByHql(hql, new Object[] { certNum,
				batchNo });
		String userCode = certNum;
		String userName = "";
		String userAddress = "广东医科大学继续教育学院";
		double totalAssessment = 0.00;
		String costInfo = "";
		if (list.size() > 0) {
			for (TempStudentFee fee : list) {
				userCode = fee.getCertNum();
				userName = fee.getStudentName();
				totalAssessment += fee.getAmount();
			}
			costInfo += "学生：" + userName + " 身份证号：" + certNum
					+ " 当前总共应缴费用为（￥）：" + totalAssessment + " 元";
			qa.setUserCode(userCode);
			qa.setUserName(userName);
			qa.setCostInfo(costInfo);
			qa.setTotalAssessment(String.valueOf(totalAssessment));
			qa.setUserAddress(userAddress);
		} else {
			costInfo += "学生：" + userName + " 身份证号：" + certNum
					+ " 当前总共应缴费用为（￥）：" + 0.00 + " 元";
			qa.setUserCode(userCode);
			qa.setUserName(userName);
			qa.setCostInfo(costInfo);
			qa.setTotalAssessment(String.valueOf(0.00));
			qa.setUserAddress(userAddress);
		}
	}

	/**
	 * 缴费
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public String paymentRequest(String request) {
		String respInfo = "";
		RequestBase reqb = new RequestBase(request);
		String dealCode = reqb.getDealCode();
		PaymentResp py = new PaymentResp();

		try {
			if (dealCode.equalsIgnoreCase(NettyConstants.DEAL_CODE_540)) {
				py.setRespCode(NettyConstants.RESP_CODE_000);
				// 请求初始化
				PaymentReq pr = new PaymentReq(request);
				// 学校内部批次编号
				String batchNo = CacheAppManager.getSysConfigurationByCode(
						"payment_batchNo").getParamValue();
				if (isArrearage(pr.getUserCode(), batchNo)) {// 查询用户是否存在未缴费用
					if (isPaid(pr.getUserCode(), batchNo)) {// 已缴费
						py.setRespCode(NettyConstants.RESP_CODE_004);
						py.setRespMsg("已缴费");
					} else {
						synchronized (this) {
							// 有四张表要更新：
							updatePaymentDetails(pr, py, batchNo);
						}
					}
				} else {
					py.setRespCode(NettyConstants.RESP_CODE_001);
					py.setRespMsg("缴费号码不存在");
				}
			}
		} catch (Exception e) {
			py.setRespCode(NettyConstants.RESP_CODE_022);
			py.setRespMsg("处理缴费业务时发生错误");
			e.printStackTrace();
		} finally {
			py.setDealCode(dealCode);
			py.setHeadCode(StringUtil.fillWithSpace(
					String.valueOf(NettyConstants.RESP_CODE_LENGTH_540), 4));
			respInfo = py.getPayment();
		}
		return respInfo;
	}
	@SuppressWarnings("unchecked")
	@Transactional

	public void updatePaymentDetails(PaymentReq pr,PaymentResp p,String batchNo){//应缴费详情
		String hql = " from "+TempStudentFee.class.getSimpleName()+" where isDeleted=0 and payStatus='1' and certNum=? and batchNo=?";
		List<TempStudentFee> list = this.findByHql(hql, new Object[]{pr.getUserCode(),batchNo});
//		String userCode=pr.getUserCode();
		String fee=pr.getFee();
//		String operator=pr.getOperator();
//		String payType = pr.getPayType();
//		String dealDate=pr.getDealDate();
//		String dealSerial = pr.getDealSerial();
//		String dealUnionSerial = pr.getDealUnionSerial();
		int statusCode=200;		
		if(list.size()>0 && list!=null){
			double tmpfee=list.get(0).getAmount();
			tmpfee=tmpfee*list.size();
			if(tmpfee==Double.valueOf(fee)){//金额正确
				for(TempStudentFee tsf:list){
					//1、学生是否有学籍，还没有学籍的则给学生进行注册
					String uniqueId = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();//0.ZKZH//准考证号 1.KSH//考生号
					String deflautPropertyName="examCertificateNo";//默认为准考证号
					if("1".equals(uniqueId)){
						deflautPropertyName="enrolleeCode";

					}
					StudentInfo studentInfo = studentInfoService
							.findUniqueByProperty(deflautPropertyName,
									tsf.getExamCertificateNo());
					StudentPayment payment = null;
					if (studentInfo == null) {// 学生未注册
						EnrolleeInfo ei = enrolleeInfoService
								.findUniqueByProperty(deflautPropertyName,
										tsf.getExamCertificateNo());
						String[] eiIds = new String[] { ei.getResourceid() };
						// 2、给学生注册
						List<StudentInfo> registeredStus = reginfoService
								.doRegister(eiIds);
						if (ExCollectionUtils.isNotEmpty(registeredStus)) {
							studentInfo = registeredStus.get(0);
							// 3、生成 缴费标准信息、年度缴费信息 这两张表的信息
							Map<String, Object> createPaymentMap = studentPaymentService
									.generateStudentFeeRecord(registeredStus);
							statusCode = (Integer) createPaymentMap
									.get("statusCode");
							if (statusCode == 300) {
								p.setRespCode(NettyConstants.RESP_CODE_022);
								p.setRespMsg((String) createPaymentMap
										.get("message"));
								continue;
							}
							// 获取学生缴费标准信息
							List<StudentPayment> paymentList = (List<StudentPayment>) createPaymentMap
									.get("paymentList");
							if (ExCollectionUtils.isNotEmpty(paymentList)) {
								payment = paymentList.get(0);// 由于这个是一个学生一个学生处理的，就这样获取，否则使用学生匹配获取
							}
						}
					} else {
						payment = studentPaymentService
								.findUnique(
										"from "
												+ StudentPayment.class
														.getSimpleName()
												+ " where isDeleted=0 and studentInfo.resourceid=?",
										studentInfo.getResourceid());
					}
					if (payment == null) {
						statusCode = 300;
						p.setRespCode(NettyConstants.RESP_CODE_022);
						p.setRespMsg(studentInfo.getStudyNo() + "该学生没有生成缴费记录");
						continue;
					}
					// 更新 缴费标准信息、年度缴费信息、学生缴费详情三张表
					Map<String, Object> returnMap = payFee(tsf, payment, pr);
					statusCode = (Integer) returnMap.get("statusCode");
					if (statusCode == 200) {
						// 更改缴费信息状态
						if (tsf != null) {
							tsf.setEduOrderNo(pr.getDealSerial());
							tsf.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
							tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_NONEED);
							this.saveOrUpdate(tsf);
						}
					} else {
						p.setRespCode(NettyConstants.RESP_CODE_022);
						p.setRespMsg((String) returnMap.get("message"));
					}

				}

			} else {// 金额不正确
				p.setRespCode(NettyConstants.RESP_CODE_005);
				p.setRespMsg("缴费金额不正确");
			}
		} else {
			p.setRespCode(NettyConstants.RESP_CODE_001);
			p.setRespMsg("缴费号码不存在");
		}
	}
	public Map<String, Object> payFee(TempStudentFee tsf,
			StudentPayment studentPayment, PaymentReq pr) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		// 获取缴费类型全局参数
		StudentPaymentDetails studentPaymentDetails;
		try {
			SysConfiguration payTypeConf = CacheAppManager
					.getSysConfigurationByCode("payment.payType");
			Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;

			if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
					&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){

				payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
			}

			Double _sysPaidFee = studentPayment.getFacepayFee() == null ? 0
					: studentPayment.getFacepayFee();
			Double recpayFee = studentPayment.getRecpayFee();// 应缴金额
			studentPayment.setFacepayFee(tsf.getAmount());// 实缴金额
			studentPayment.setChargeStatus("1");// 已缴费
			studentPayment.setEnableLogHistory(true);// 记录日志
			// 1、更新缴费记录
			studentPaymentService.update(studentPayment);
			// 新增缴费明细
			Date today = new Date();
			studentPaymentDetails = new StudentPaymentDetails();
			StudentInfo _stuInfo = studentPayment.getStudentInfo();
			studentPaymentDetails.setEduOrederNo(pr.getDealSerial());// 交易流水号
																		// 等同于
																		// 教育系统订单号
			studentPaymentDetails.setStudentInfo(_stuInfo);
			studentPaymentDetails.setShouldPayAmount(recpayFee);
			studentPaymentDetails.setPayType(payType);// 缴费类型
			studentPaymentDetails.setCreateDate(today);
			// studentPaymentDetails.setOperateDate(ExDateUtils.convertToDateEN(pr.getDealDate()));
			// 由于给过来的日期转换有问题，所以改成直接拿当前的时间为操作日期
			studentPaymentDetails.setOperateDate(today);
			studentPaymentDetails.setPaymentMethod("6");// 公众号 支付
			studentPaymentDetails.setPayNo(pr.getDealSerial());// 支付流水号
																// 也保存为交易流水号
			if (payType == 2) {// 叠加(广东医当前属于叠加)
				studentPaymentDetails.setPayAmount(tsf.getAmount());// 缴费金额
				studentPaymentDetails.setPaidAmount(_sysPaidFee
						+ tsf.getAmount());// 已缴金额
			} else {// 直接更新
				studentPaymentDetails.setPayAmount(tsf.getAmount());// 缴费金额
				studentPaymentDetails.setPaidAmount(tsf.getAmount());
			}
			int _year = studentPayment.getYearInfo().getFirstYear().intValue()
					+ studentPayment.getChargeTerm() - 1;
			studentPaymentDetails.setYear(_year + "");
			String defaultOperator = CacheAppManager.getSysConfigurationByCode(
					"payment.default.operator").getParamValue();
			User user = userService.findUniqueByProperty("username",
					defaultOperator);
			studentPaymentDetails.setOperatorName(user.getCnName());
			studentPaymentDetails.setOperatorId(user.getResourceid());
			studentPaymentDetails.setClassName(_stuInfo.getGrade()
					.getGradeName()
					+ _stuInfo.getMajor().getMajorName()
					+ _stuInfo.getClassic().getClassicName());
			String chargeRate = CacheAppManager.getSysConfigurationByCode(
					"Official.Accounts.ChargeRate").getParamValue();
			double charge = Double.valueOf(pr.getFee())
					* Double.valueOf(chargeRate);
			studentPaymentDetails.setChargeMoney(charge);
			// 2、更新缴费详情
			studentPaymentDetailsService.saveOrUpdate(studentPaymentDetails);
			returnMap.put("studentPaymentDetails", studentPaymentDetails);

			AnnualFees annualFees = annualFeesService
					.findUnique(
							"from "
									+ AnnualFees.class.getSimpleName()
									+ " af where af.isDeleted=0 and af.studentInfo.resourceid=? and af.chargeYear=?",
							_stuInfo.getResourceid(), _year);
			if (annualFees != null) {
				annualFees.setFacepayFee(tsf.getAmount());
				int chargeStatus = 0;
				if (annualFees.getRecpayFee().doubleValue() == tsf.getAmount()) {
					chargeStatus = 1;
				} else if (annualFees.getRecpayFee().doubleValue() > tsf
						.getAmount()) {
					chargeStatus = -1;
				}
				annualFees.setChargeStatus(chargeStatus);
				// 3、更新学生年度缴费信息
				annualFeesService.saveOrUpdate(annualFees);
			}
		} catch (NumberFormatException e) {
			statusCode = 300;
			// 更新 缴费标准信息、年度缴费信息、学生缴费详情 三张表时出错
			message = "Server端出错，请联系管理员";
			e.printStackTrace();
		} catch (ServiceException e) {
			statusCode = 300;
			message = "Server端出错，请联系管理员";
			e.printStackTrace();
		} finally {
			returnMap.put("message", message);
			returnMap.put("statusCode", statusCode);
		}
		return returnMap;
	}

	@Override
	public TempStudentFee isSync(String eduNum, String fee) {
		TempStudentFee tsf = this.findUniqueByProperty("eduOrderNo", eduNum);
		if (tsf == null) {
			return null;
		} else {
			double loaclFee = tsf.getAmount();
			double remoteFee = Double.valueOf(fee);
			if (loaclFee == remoteFee) {
				if (tsf.getPayStatus().equalsIgnoreCase(
						Constants.FEE_PAYSTATUS_PAYED)) {
					return tsf;
				} else {
					return null;
				}

			} else {
				return null;
			}
		}
	}

	/**
	 * 处理导入线下缴费逻辑
	 * 
	 * @param payOfflineVoList
	 * @return
	 */
	@Override
	public Map<String, Object> handlePayOfflineInfoImport(
			List<PayOfflineVo> payOfflineVoList) {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(1024); // 出错信息
		List<PayOfflineVo> failList = new ArrayList<PayOfflineVo>(100);
		try {
			do {
				if (ExCollectionUtils.isEmpty(payOfflineVoList)) {
					statusCode = 300;
					msg.append("没有线下缴费信息数据");
					continue;
				}

				List<String> tempList = new ArrayList<String>(1024);
				TempStudentFee tsf = null;
				List<TempStudentFee> tsfList = new ArrayList<TempStudentFee>(
						1024);
				String selhql = "from "
						+ TempStudentFee.class.getSimpleName()
						+ " tf where tf.isDeleted=0 and tf.examCertificateNo=? and tf.payStatus='1'";
				for (PayOfflineVo po : payOfflineVoList) {
					if (tempList.contains(po.getExamCertificateNo())) {
						msg.append(po.getExamCertificateNo()).append(
								"该学生的订单重复导入<br/>");
						po.setErrorMsg(po.getExamCertificateNo() + "该学生的订单重复导入");
						failList.add(po);
						continue;
					}
					tempList.add(po.getExamCertificateNo());

					// 根据准考证号查询预缴费订单
					tsf = findUnique(selhql, po.getExamCertificateNo());
					if (tsf == null) {
						msg.append(po.getExamCertificateNo()).append(
								"该学生不存在要缴费的订单<br/>");
						po.setErrorMsg(po.getExamCertificateNo()
								+ "该学生不存在要缴费的订单");
						failList.add(po);
						continue;
					}
					// 身份证号
					if (!po.getCertNum().equals(tsf.getCertNum())) {
						msg.append(po.getExamCertificateNo()).append(
								"该学生的身份证号不正确<br/>");
						po.setErrorMsg(po.getExamCertificateNo()
								+ "该学生的身份证号不正确");
						failList.add(po);
						continue;
					}
					// 学生姓名
					if (!po.getName().equals(tsf.getStudentName())) {
						msg.append(po.getExamCertificateNo()).append(
								"该学生的姓名不正确<br/>");
						po.setErrorMsg(po.getExamCertificateNo() + "该学生的姓名不正确");
						failList.add(po);
						continue;
					}
					// 缴费金额
					Double amount = Double.valueOf(po.getPayAmount());
					if (!amount.equals(tsf.getAmount())) {
						msg.append(po.getExamCertificateNo()).append(
								"该学生的缴费金额与系统不一致<br/>");
						po.setErrorMsg(po.getExamCertificateNo()
								+ "该学生的缴费金额与系统不一致");
						failList.add(po);
						continue;
					}
					tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_TOAUDIT);
					tsfList.add(tsf);
				}

				if (ExCollectionUtils.isNotEmpty(tsfList)) {
					batchSaveOrUpdate(tsfList);
				}
			} while (false);
			if (msg.length() > 0) {
				statusCode = 400;
			}
		} catch (Exception e) {
			logger.error("处理导入线下缴费信息出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("处理导入线下缴费信息失败！");
		} finally {
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
			retrunMap.put("failList", failList);
		}

		return retrunMap;
	}

	/**
	 * 根据准考证号删除未付款的预缴费订单
	 * 
	 * @param examCertificateNo
	 */
	@Override
	public void deleteByExamCertificateNo(String examCertificateNo) {
		try {
			StringBuilder delsql = new StringBuilder(200);
			delsql.append("update edu_fee_tempstudentfee set isdeleted=1 where paystatus='1' and examcertificateno=?");

			baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate()
					.update(delsql.toString(), examCertificateNo);
		} catch (Exception e) {
			logger.error("根据准考证号删除未付款的预缴费订单出错", e);
		}
	}

	/**
	 * 生成教材费订单
	 * 
	 * @param studentInfoList
	 * @return
	 */
	@Override
	public Map<String, Object> createTextbookFee(
			List<StudentInfo> studentInfoList) {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer message = new StringBuffer();
		Map<String, String> messageMap = new HashMap<String, String>();
		String msg = "";
		try {
			if (ExCollectionUtils.isNotEmpty(studentInfoList)) {
				String uniqueId = CacheAppManager.getSysConfigurationByCode(
						"exameeInfo.uniqueId").getParamValue();
				List<TempStudentFee> tempStuFeeList = new ArrayList<TempStudentFee>(150);
				List<AnnualFees> annualFeesList = new ArrayList<AnnualFees>(150);
				TempStudentFee tsf = null;
				// 学校内部批次编号
				String batchNo = CacheAppManager.getSysConfigurationByCode(
						"payment_batchNo").getParamValue();
				Double money = 0d;
				String schoolOrderNo = null;
				Major major = null;
				String chargingItems = "textbookFee";
				String tbfKey = null;

				// 获取当前年
				String currentYear = CacheAppManager.getSysConfigurationByCode("currentYear").getParamValue();
				YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(currentYear));
				
				for(StudentInfo si : studentInfoList){

					// 是否已生成
					String examCertificateNo = si.getExamCertificateNo();
					if ("1".equals(uniqueId)) {
						examCertificateNo = si.getEnrolleeCode();
					}
					TempStudentFee tempStuFee = getByExamNoAndBatchNo(
							examCertificateNo, batchNo, Constants.BOOLEAN_NO,
							chargingItems);
					// 已有记录不做处理
					if (tempStuFee != null) {
						continue;
					}

					major = si.getMajor();//基础专业

					Grade grade = si.getGrade();// 年级

					

					// 处理重复问题
					StringBuffer ym = new StringBuffer(200);
					ym.append(yearInfo.getResourceid()).append("_tbf_")
							.append(major.getResourceid());
					tbfKey = ym.toString();
					if (messageMap.containsKey(tbfKey)) {
						continue;
					}
					// 获取年教材费标准
					TextbookFee tbf = textbookFeeService.findByYearAndMajor(
							yearInfo.getResourceid(), major.getResourceid());
					if (tbf == null) {
						msg = "专业: " + major.getMajorName()
								+ "没有设置年教材费标准,无法生成教材费订单.<br>";
						messageMap.put(tbfKey, msg);
						message.append(msg);
						continue;
					}
					// 金额
					money = tbf.getMoney();
					// 学校内部订单编号,2开头代表教材费
					StringBuffer son = new StringBuffer(30);
					son.append("2").append(batchNo).append(examCertificateNo);
					schoolOrderNo = son.toString();

					// 检查年度教材费信息是否存在，没有则新增
					AnnualFees annualFees = annualFeesService.findUniqueByCondition(si.getResourceid(),yearInfo.getResourceid(),"textbookFee");
					if(annualFees==null){
						// 新增
						annualFees = new AnnualFees();
						annualFees.setChargeStatus(0);
						annualFees.setChargeYear(yearInfo.getFirstYear().intValue());
						annualFees.setChargingItems(chargingItems);
						annualFees.setDerateFee(0d);
						annualFees.setFacepayFee(0d);
						annualFees.setReturnPremiumFee(0d);
						annualFees.setPayAmount(0d);
						annualFees.setRecpayFee(tbf.getMoney());
						annualFees.setStudentInfo(si);
						annualFees.setStudyNo(si.getStudyNo());
						annualFees.setYearInfo(yearInfo);
						// 添加到列表
						annualFeesList.add(annualFees);
					}

					// 生成预缴费订单信息

					tsf = createAdvanceOrder(chargingItems, batchNo, money, schoolOrderNo, major, null, examCertificateNo, grade,si,Constants.BOOLEAN_YES,yearInfo);
					

					tempStuFeeList.add(tsf);
				}
				// 批量保存学生缴费标准
				batchSaveOrUpdate(tempStuFeeList);
				if(ExCollectionUtils.isNotEmpty(annualFeesList)){
					annualFeesService.batchSaveOrUpdate(annualFeesList);
				}
			}
		} catch (Exception e) {
			logger.error("生成教材费订单出错", e);
			message.setLength(0);
			message.append("生成失败");
		} finally {
			// 有失败的记录
			if (message.length() > 0) {
				statusCode = 300;
			}
			map.put("statusCode", statusCode);
			map.put("message", message.toString());
		}

		return map;
	}

	@Override
	public List<TempStudentFee> findByCertnum(String certNum) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		hql.append(" from "
				+ TempStudentFee.class.getSimpleName()
				+ " where isDeleted=0 and certNum=:certNum order by payStatus,batchNo desc");
		params.put("certNum", certNum);
		return findByHql(hql.toString(), params);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void updatePaymentDetails(TempStudentFee tsf) {// 应缴费详情
		int statusCode = 200;

		// 1、学生是否有学籍，还没有学籍的则给学生进行注册
		String uniqueId = CacheAppManager.getSysConfigurationByCode(
				"exameeInfo.uniqueId").getParamValue();// 0.ZKZH//准考证号
														// 1.KSH//考生号
		String deflautPropertyName = "examCertificateNo";// 默认为准考证号
		if (uniqueId.equals("1")) {
			deflautPropertyName = "enrolleeCode";
		}
		StudentInfo studentInfo = studentInfoService.findUniqueByProperty(
				deflautPropertyName, tsf.getExamCertificateNo());
		StudentPayment payment = null;
		if (studentInfo == null) {// 学生未注册
			EnrolleeInfo ei = enrolleeInfoService.findUniqueByProperty(
					deflautPropertyName, tsf.getExamCertificateNo());
			String[] eiIds = new String[] { ei.getResourceid() };
			// 2、给学生注册
			List<StudentInfo> registeredStus = reginfoService.doRegister(eiIds);
			if (ExCollectionUtils.isNotEmpty(registeredStus)) {
				studentInfo = registeredStus.get(0);
				// 3、生成 缴费标准信息、年度缴费信息 这两张表的信息
				Map<String, Object> createPaymentMap = studentPaymentService
						.generateStudentFeeRecord(registeredStus);
				statusCode = (Integer) createPaymentMap.get("statusCode");
				if (statusCode == 300) {
					logger.error("生成缴费标准信息失败：updatePaymentDetails(TempStudentFee tsf):resourceid:"+tsf.getResourceid());
				}
				// 获取学生缴费标准信息
				List<StudentPayment> paymentList = (List<StudentPayment>) createPaymentMap
						.get("paymentList");
				if (ExCollectionUtils.isNotEmpty(paymentList)) {
					payment = paymentList.get(0);// 由于这个是一个学生一个学生处理的，就这样获取，否则使用学生匹配获取
				}
			}
		} else {
			payment = studentPaymentService.findUnique("from "
					+ StudentPayment.class.getSimpleName()
					+ " where isDeleted=0 and studentInfo.resourceid=?",
					studentInfo.getResourceid());
		}
		if (payment == null) {
			statusCode = 300;
			logger.error("生成缴费标准信息失败：updatePaymentDetails(TempStudentFee tsf):resourceid:"+tsf.getResourceid());
		}
		// 更新 缴费标准信息、年度缴费信息、学生缴费详情三张表
		Map<String, Object> returnMap = payFee(tsf, payment);
		statusCode = (Integer) returnMap.get("statusCode");
		if (statusCode == 200) {
			// 更改缴费信息状态
			if (tsf != null) {
				// tsf.setEduOrderNo(pr.getDealSerial());
				tsf.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
				tsf.setHandleStatus(TempStudentFee.HANDLESTATUS_NONEED);
				this.saveOrUpdate(tsf);
			}
		}

	}

	public Map<String, Object> payFee(TempStudentFee tsf,
			StudentPayment studentPayment) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		// 获取缴费类型全局参数
		StudentPaymentDetails studentPaymentDetails;
		try {
			SysConfiguration payTypeConf = CacheAppManager
					.getSysConfigurationByCode("payment.payType");
			Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
			if (payTypeConf != null
					&& ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
					&& StudentPaymentDetails.PAYTYPE_OVERLAY == Integer
							.valueOf(payTypeConf.getParamValue())) {
				payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
			}

			Double _sysPaidFee = studentPayment.getFacepayFee() == null ? 0
					: studentPayment.getFacepayFee();
			Double recpayFee = studentPayment.getRecpayFee();// 应缴金额
			studentPayment.setFacepayFee(tsf.getAmount());// 实缴金额
			studentPayment.setChargeStatus("1");// 已缴费
			studentPayment.setEnableLogHistory(true);// 记录日志
			// 1、更新缴费记录
			studentPaymentService.update(studentPayment);
			// 新增缴费明细
			Date today = new Date();
			studentPaymentDetails = new StudentPaymentDetails();
			StudentInfo _stuInfo = studentPayment.getStudentInfo();
			studentPaymentDetails.setEduOrederNo(tsf.getEduOrderNo());// 交易流水号
																		// 等同于
																		// 教育系统订单号
			studentPaymentDetails.setStudentInfo(_stuInfo);
			studentPaymentDetails.setShouldPayAmount(recpayFee);
			studentPaymentDetails.setPayType(payType);// 缴费类型
			studentPaymentDetails.setCreateDate(today);
			// studentPaymentDetails.setOperateDate(ExDateUtils.convertToDateEN(pr.getDealDate()));
			// 由于给过来的日期转换有问题，所以改成直接拿当前的时间为操作日期
			studentPaymentDetails.setOperateDate(today);
			studentPaymentDetails.setPaymentMethod("8");// 平台 支付
			studentPaymentDetails.setPayNo(tsf.getEduOrderNo());// 支付流水号
																// 也保存为交易流水号
			if (payType == 2) {// 叠加(广东医当前属于叠加)
				studentPaymentDetails.setPayAmount(tsf.getAmount());// 缴费金额
				studentPaymentDetails.setPaidAmount(_sysPaidFee
						+ tsf.getAmount());// 已缴金额
			} else {// 直接更新
				studentPaymentDetails.setPayAmount(tsf.getAmount());// 缴费金额
				studentPaymentDetails.setPaidAmount(tsf.getAmount());
			}
			int _year = studentPayment.getYearInfo().getFirstYear().intValue()
					+ studentPayment.getChargeTerm() - 1;
			studentPaymentDetails.setYear(_year + "");
			String defaultOperator = CacheAppManager.getSysConfigurationByCode(
					"payment.default.operator").getParamValue();
			User user = userService.findUniqueByProperty("username",
					defaultOperator);
			studentPaymentDetails.setOperatorName(user.getCnName());
			studentPaymentDetails.setOperatorId(user.getResourceid());
			studentPaymentDetails.setClassName(_stuInfo.getGrade()
					.getGradeName()
					+ _stuInfo.getMajor().getMajorName()
					+ _stuInfo.getClassic().getClassicName());
			String chargeRate = CacheAppManager.getSysConfigurationByCode(
					"Official.Accounts.ChargeRate").getParamValue();
			double charge = tsf.getAmount() * Double.valueOf(chargeRate);
			studentPaymentDetails.setChargeMoney(charge);
			
			// 2、更新缴费详情
			studentPaymentDetailsService.saveOrUpdate(studentPaymentDetails);
			returnMap.put("studentPaymentDetails", studentPaymentDetails);

			AnnualFees annualFees = annualFeesService
					.findUnique(
							"from "
									+ AnnualFees.class.getSimpleName()
									+ " af where af.isDeleted=0 and af.studentInfo.resourceid=? and af.chargeYear=? and af.chargingItems=?",
							_stuInfo.getResourceid(), _year,"tuition");
			if (annualFees != null) {
				annualFees.setFacepayFee(tsf.getAmount());
				int chargeStatus = 0;
				if (annualFees.getRecpayFee().doubleValue() == tsf.getAmount()) {
					chargeStatus = 1;
				} else if (annualFees.getRecpayFee().doubleValue() > tsf
						.getAmount()) {
					chargeStatus = -1;
				}
				annualFees.setChargeStatus(chargeStatus);
				// 3、更新学生年度缴费信息
				annualFeesService.saveOrUpdate(annualFees);
			}
		} catch (NumberFormatException e) {
			statusCode = 300;
			// 更新 缴费标准信息、年度缴费信息、学生缴费详情 三张表时出错
			message = "Server端出错，请联系管理员";
			e.printStackTrace();
		} catch (ServiceException e) {
			statusCode = 300;
			message = "Server端出错，请联系管理员";
			e.printStackTrace();
		} finally {
			returnMap.put("message", message);
			returnMap.put("statusCode", statusCode);
		}
		return returnMap;
	}
}