package com.hnjk.edu.finance.service.impl;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.model.Refundback;
import com.hnjk.edu.finance.model.ReturnPremium;
import com.hnjk.edu.finance.service.IAnnualFeesService;
import com.hnjk.edu.finance.service.IRefundbackService;
import com.hnjk.edu.finance.service.IReturnPremiumService;
import com.hnjk.edu.finance.vo.MajorFeeInfoVO;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;

/**
 * 退费标准服务.
 * <code>ReturnPremiumServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @version 1.0
 */
@Transactional
@Service("returnPremiumService")
public class ReturnPremiumServiceImpl extends BaseServiceImpl<ReturnPremium> implements IReturnPremiumService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("annualFeesService")
	private IAnnualFeesService annualFeesService;	
	
	@Autowired
	@Qualifier("refundbackService")
	private IRefundbackService refundbackService;	
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 根据条件查询退费记录
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ReturnPremium> findReturnPremiumByCondition(Map<String, Object> condition) {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findReturnPremiumByConditionHql(condition, values);
		return findByHql(hql.toString(), values);
	}
	
	/**
	 * 根据条件查询退费记录HQL
	 * @param condition
	 * @return
	 */
	private StringBuffer findReturnPremiumByConditionHql(Map<String, Object> condition,Map<String, Object> param) {
		StringBuffer hql = new StringBuffer();
		hql.append("from "+ReturnPremium.class.getSimpleName() +" rp where rp.isDeleted=:isDeleted ");
		param.put("isDeleted", 0);
		if(condition.containsKey("detailIds")){//勾选id
			String[] ids = ((String)condition.get("detailIds")).split(",");
			StringBuffer detailId = new StringBuffer();
			for(String id : ids){
				detailId.append("'"+id+"',");
			}
			detailId.deleteCharAt(detailId.length()-1);
			hql.append(" and rp.resourceid in "+"("+detailId.toString()+")");
		}else{
			if(condition.containsKey("studentInfoId")){//勾选id
				hql.append(" and rp.studentInfo.resourceid=:studentInfoId");
				param.put("studentInfoId", condition.get("studentInfoId"));
			}
			if(condition.containsKey("year")){//学年名
				hql.append(" and rp.yearInfo.firstYear=:year");
				param.put("year", Long.parseLong((String)condition.get("year")));
			}	
			if(condition.containsKey("print")){//打印专用
				hql.append(" and rp.receiptNumber is null");
			}
			if(condition.containsKey("brSchool")){
				hql.append(" and rp.studentInfo.branchSchool.resourceid=:brSchool");
				param.put("brSchool", condition.get("brSchool"));
			}
			if(condition.containsKey("yearId")){
				hql.append(" and rp.yearInfo.resourceid=:yearId");
				param.put("yearId", condition.get("yearId"));
			}
			if(condition.containsKey("gradeid")){
				hql.append(" and rp.studentInfo.grade.resourceid=:gradeid");
				param.put("gradeid", condition.get("gradeid"));
			}else if (condition.containsKey("gradeId")) {
				hql.append(" and rp.studentInfo.grade.resourceid=:gradeid");
				param.put("gradeid", condition.get("gradeId"));
			}
			if(condition.containsKey("classicid")){
				hql.append(" and rp.studentInfo.classic.resourceid=:classicid");
				param.put("classicid", condition.get("classicid"));
			}else if (condition.containsKey("classicId")) {
				hql.append(" and rp.studentInfo.classic.resourceid=:classicid");
				param.put("classicid", condition.get("classicId"));
			}
			if(condition.containsKey("majorid")){
				hql.append(" and rp.studentInfo.major.resourceid=:majorid");
				param.put("majorid", condition.get("majorid"));
			}else if (condition.containsKey("majorId")) {
				hql.append(" and rp.studentInfo.major.resourceid=:majorid");
				param.put("majorid", condition.get("majorId"));
			}
			if(condition.containsKey("classesId")){
				hql.append(" and rp.studentInfo.Classes.resourceid=:classesId");
				param.put("classesId", condition.get("classesId"));
			}
			if(condition.containsKey("name")){
				hql.append(" and rp.studentInfo.studentName=:name");
				param.put("name", condition.get("name"));
			}
			if(condition.containsKey("studyNo")){
				hql.append(" and rp.studentInfo.studyNo=:studyNo");
				param.put("studyNo", condition.get("studyNo"));
			}
			if(condition.containsKey("paymentMethod")){//学年名
				hql.append(" and rp.paymentMethod=:paymentMethod");
				param.put("paymentMethod",condition.get("paymentMethod"));
			}	
			if(condition.containsKey("receiptNumber_begin") && StringUtils.isNumeric((String) condition.get("receiptNumber_begin"))){//票据号开始
				hql.append(" and to_number(rp.receiptNumber)>=:receiptNumber_begin ");
				param.put("receiptNumber_begin", condition.get("receiptNumber_begin"));
			}
			if(condition.containsKey("receiptNumber_end") && StringUtils.isNumeric((String) condition.get("receiptNumber_end"))){//票据号结束
				hql.append(" and to_number(rp.receiptNumber)<=:receiptNumber_end ");
				param.put("receiptNumber_end", condition.get("receiptNumber_end"));
			}
			if(condition.containsKey("beginDate")){//开始日期
				hql.append(" and to_char(rp.createDate,'yyyy-mm-dd')>=:beginDate ");
				param.put("beginDate", condition.get("beginDate"));
			}
			if(condition.containsKey("endDate")){//结束日期
				hql.append(" and to_char(rp.createDate,'yyyy-mm-dd')<=:endDate ");
				param.put("endDate", condition.get("endDate"));
			}
			if(condition.containsKey("beginPrintDate")){//打印开始日期
				hql.append(" and to_char(rp.printDate,'yyyy-mm-dd')>=:beginPrintDate ");
				param.put("beginPrintDate", condition.get("beginPrintDate"));
			}
			if(condition.containsKey("endPrintDate")){//打印结束日期
				hql.append(" and to_char(rp.printDate,'yyyy-mm-dd')<=:endPrintDate ");
				param.put("endPrintDate", condition.get("endPrintDate"));
			}
			if(condition.containsKey("chargingItems")){// 收费项
				hql.append(" and rp.chargingItems=:chargingItems ");
				param.put("chargingItems", condition.get("chargingItems"));
			}
			if(condition.containsKey("processType")){// 处理类型
				hql.append(" and rp.processType=:processType ");
				param.put("processType", condition.get("processType"));
			}
		}
		if(condition.containsKey("print")){//打印专用
			hql.append(" and rp.receiptNumber is null");
		}
		if(condition.containsKey("isPrint")){//是否打印
			if("Y".equals(condition.get("isPrint"))){
				hql.append(" and rp.receiptNumber is not null");
			}else if("N".equals(condition.get("isPrint"))){
				hql.append(" and rp.receiptNumber is null");
			}		
		}
		hql.append(" order by rp.studyNo,rp.yearInfo.firstYear,rp.createDate asc");
		return hql;
	}
	
	/**
	 * 根据条件查询退费分页记录
	 */
	@Override
	public Page findReturnPremiumByCondition(Map<String, Object> condition, Page page) {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findReturnPremiumByConditionHql(condition, values);

		return findByHql(page,hql.toString(), values);
	}
	
	@Override
	public List<Map<String, Object>> findReturnPremiumForMap(Map<String, Object> condition) throws ServerException {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> summarytList = null;
		sql.append("select rp.paymentmethod method,d.dictname paymentmethod,ci.classicname,count(rp.resourceid) paidcount,count(distinct rp.studentinfoid) stucount,sum(rp.returnPremiumFee) paidamount,");
		sql.append("min(to_number(rp.receiptnumber)) minnumber,max(to_number(rp.receiptnumber)) maxnumber,");
		sql.append("min(rp.printdate) mindate,max(rp.printdate) maxdate");
		sql.append(" from edu_fee_returnpremium rp");
		sql.append(" join edu_roll_studentinfo si on si.resourceid=rp.studentinfoid");
		sql.append(" join edu_base_classic ci on ci.resourceid = si.classicid");
		sql.append(" left join hnjk_sys_dict d on d.dictcode like 'CodePaymentMethod_%' and d.dictvalue=rp.paymentMethod");
		sql.append(" where rp.isdeleted=0");
		if(condition.containsKey("brSchool")){
			sql.append(" and si.branchschoolid=:brSchool");
			param.put("brSchool", condition.get("brSchool"));
		}
		if(condition.containsKey("majorid")){
			sql.append(" and si.majorid=:majorid");
			param.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("classicid")){
			sql.append(" and si.classicid=:classicid");
		}
		if(condition.containsKey("gradeid")){
			sql.append(" and si.gradeid=:gradeid");
			param.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("studentStatus")){//学籍状态
			sql.append(" and si.studentStatus=:studentStatus ");
			param.put("studentStatus", condition.get("studentStatus"));
		}
		if(condition.containsKey("paymentMethod")){//付款方式
			sql.append(" and rp.paymentMethod=:paymentMethod ");
			param.put("paymentMethod", condition.get("paymentMethod"));
		}
		if(condition.containsKey("beginDate")){
			sql.append(" and to_char(rp.createdate ,'yyyy-mm-dd')>=:beginDate");
			param.put("beginDate", condition.get("beginDate"));
		}
		if(condition.containsKey("endDate")){
			sql.append(" and to_char(rp.createdate,'yyyy-mm-dd')<=:endDate");
			param.put("endDate", condition.get("endDate"));
		}
		
		if(condition.containsKey("receiptNumber_begin") && StringUtils.isNumeric((String) condition.get("receiptNumber_begin"))){//票据号开始
			sql.append(" and to_number(rp.receiptNumber)>=:receiptNumber_begin ");
			param.put("receiptNumber_begin", condition.get("receiptNumber_begin"));
		}
		if(condition.containsKey("receiptNumber_end") && StringUtils.isNumeric((String) condition.get("receiptNumber_end"))){//票据号结束
			sql.append(" and to_number(rp.receiptNumber)<=:receiptNumber_end ");
			param.put("receiptNumber_end", condition.get("receiptNumber_end"));
		}
		if(condition.containsKey("stuPaymentId")){
			sql.append(" and rp.resourceid=:stuPaymentId ");
			param.put("stuPaymentId", condition.get("stuPaymentId"));
		}
		if(condition.containsKey("chargingItems")){// 收费项
			sql.append(" and rp.chargingItems=:chargingItems ");
			param.put("chargingItems", condition.get("chargingItems"));
		}
		if(condition.containsKey("processType")){// 处理类型
			sql.append(" and rp.processType=:processType ");
			param.put("processType", condition.get("processType"));
		}
		sql.append(" group by rp.paymentmethod,d.dictname,ci.classicname order by rp.paymentmethod,ci.classicname");
		try {
			summarytList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summarytList;
	}

	/**
	 * 创建退缴费记录
	 * 
	 * @param refund
	 * @param facepayFee
	 * @param payAmount
	 * @param paymentMethod
	 * @param recpayFee
	 * @param _stuInfo
	 * @param yearInfo
	 * @param showOrder
	 * @param processType
	 * @param chargingItems
	 * @return
	 */
	@Override
	public ReturnPremium createReturnPremium(Double money, Double facepayFee,String paymentMethod, Double recpayFee, 
			StudentInfo _stuInfo,YearInfo yearInfo, Integer showOrder,String processType,String orderNo,String chargingItems) {
		ReturnPremium returnPremium = new ReturnPremium();
		Date n = new Date();
		returnPremium.setCreateDate(n);
		returnPremium.setFacepayFee(facepayFee);
		returnPremium.setOperator(SpringSecurityHelper.getCurrentUser());
		returnPremium.setOperatorName(SpringSecurityHelper.getCurrentUser().getCnName());
		returnPremium.setRecpayFee(recpayFee);
		returnPremium.setStudentInfo(_stuInfo);
		returnPremium.setStudyNo(_stuInfo.getStudyNo());
		returnPremium.setPaymentMethod(paymentMethod);
		returnPremium.setYearInfo(yearInfo);
		returnPremium.setShowOrder(showOrder);
		returnPremium.setClassName(_stuInfo.getGrade().getGradeName()+_stuInfo.getMajor().getMajorName()+_stuInfo.getClassic().getClassicName());
		returnPremium.setProcessType(processType);
		if(ReturnPremium.PROCESSTYPE_RETURNPREMIUM.equals(processType)){
			returnPremium.setReturnPremiumFee(money);
		} else if(ReturnPremium.PROCESSTYPE_AFTERPAYMENT.equals(processType)){
			returnPremium.setPayAmount(money);
		}
		if(ExStringUtils.isBlank(orderNo)){
			orderNo = _stuInfo.getStudyNo()+n.getTime();
		}
		returnPremium.setOrderNo(orderNo);
		returnPremium.setChargingItems(chargingItems);
		
		return returnPremium;
	}

	/**
	 * 学籍异动审批成功后处理学费退费补交逻辑
	 * 注：申请学籍异动一定要在新的一年缴费之前
	 * @param changeInfo
	 */
	@Override
	public Map<String, Object> handleTuitionForStuChange(StuChangeInfo changeInfo) {
		Map<String, Object> returnMap = new HashMap<String, Object>(10);
		int statusCode = 200;
		StringBuffer message = new StringBuffer(500);
		try {
			do {
				StudentInfo studentInfo = changeInfo.getStudentInfo();
				Grade grade = studentInfo.getGrade();
				YearInfo year = grade.getYearInfo();
				String[] majorIds = new String[2];
				Major major = changeInfo.getChangeTeachingGuidePlan().getTeachingPlan().getMajor();
				majorIds[0] = major.getResourceid();
				String changeType = changeInfo.getChangeType();
				// 涉及转专业
				if (changeType.equals("23") || changeType.equals("81")) {
					Major beforeMajor = changeInfo.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getMajor();
					majorIds[1] = beforeMajor.getResourceid();
					// 判断专业是否一样
					if (majorIds[0].equals(majorIds[1])) {
						message.append("专业一样无需处理");
						continue;
					}
				}
				// 获取当前年
				String currentYear = CacheAppManager.getSysConfigurationByCode("currentYear").getParamValue();
				Long feeTerm = Long.valueOf(currentYear)-year.getFirstYear()+1;
				// 根据专业获取缴费信息
				Map<String, MajorFeeInfoVO> majorFeeInfoMap = getMajorFeeInfoMap(grade, majorIds, feeTerm);
				if(majorFeeInfoMap == null){
					message.append("没有设置专业缴费类别");
					continue;
				}
				
				MajorFeeInfoVO mfInfo = majorFeeInfoMap.get(majorIds[0] );
				MajorFeeInfoVO beforeMfInfo =null;
				BigDecimal money = BigDecimal.ZERO;
				// 处理类型
				String processType = "returnPremium";
				// 收费项
				String chargingItems = "tuition";
				// 获取缴费年度
				YearInfo feeYear = yearInfoService.getByFirstYear(year.getFirstYear()+feeTerm-1);
				
				if (changeType.equals("23") || changeType.equals("81")) {
					beforeMfInfo = majorFeeInfoMap.get(majorIds[1] );
					// 判断学费是否一样
					BigDecimal creditFee = mfInfo.getCreditFee();
					BigDecimal beforeCreditFee = beforeMfInfo.getCreditFee();
					// 学费一样
					if(creditFee.compareTo(beforeCreditFee) == 0){
						message.append("学费一样无需处理");
						continue;
					}
					// 高转低
					if(creditFee.compareTo(beforeCreditFee)<0){
						processType = "returnPremium";
						money = beforeCreditFee.subtract(creditFee);
					} else {
						// 低转高
						processType = "afterPayment";
						money = creditFee.subtract(beforeCreditFee);
					}
				} else if(changeType.equals("13") || changeType.equals("11")){
					// 退学或休学
					/** 退费办法：
					*1、学生报到注册日前（不含报到日）退费的，应退回所缴学费的90%。
					*2、学生在校提前结业、经批准转学、中途死亡或因病休学、退学的，
					*     所缴学费按学生在校时间计算退费。退费标准=每年学费10个月
					*     （10-学生实际在校月数），在校时间未满一个月按一个月计算。
					**/
					// 报到注册日
					Date registrationDate = feeYear.getRegistrationDate();
					if(registrationDate == null){
						message.append(feeYear.getFirstYear()).append("年度还没有设置报到注册日");
						logger.info(feeYear.getFirstYear()+"年度还没有设置报到注册日");
						continue;
					}
					registrationDate = ExDateUtils.formatDate(registrationDate, ExDateUtils.PATTREN_DATE);
					// 异动申请时间
					Date applyDate = ExDateUtils.formatDate(changeInfo.getApplicationDate(), ExDateUtils.PATTREN_DATE);
					// 第1学期开学日期
					Date firstTermBeginDate = ExDateUtils.formatDate(feeYear.getFirstMondayOffirstTerm(), ExDateUtils.PATTREN_DATE);
					// 学生报到注册日前（不含报到日）
					if(applyDate.before(registrationDate) || applyDate.before(firstTermBeginDate)){
						money = mfInfo.getCreditFee().multiply(BigDecimal.valueOf(0.9));
					} else {
						// 退费标准=每年学费10个月（10-学生实际在校月数）
						long monthNum = 0l;
						// 第1学期结束日期
						Date firstTermEndDate = ExDateUtils.getEndDate(firstTermBeginDate, feeYear.getFirstTermWeekNum().intValue());
						firstTermEndDate = ExDateUtils.formatDate(firstTermEndDate, ExDateUtils.PATTREN_DATE);
						// 第2学期开学日期
						Date secondTermBeginDate = ExDateUtils.formatDate(feeYear.getFirstMondayOfSecondTerm(), ExDateUtils.PATTREN_DATE);
						// 第2学期结束日期
						Date secondTermEndDate = ExDateUtils.getEndDate(secondTermBeginDate, feeYear.getSecondTermWeekNum().intValue());
						secondTermEndDate = ExDateUtils.formatDate(secondTermEndDate, ExDateUtils.PATTREN_DATE);
						// 在第1学期在校期间
						if(applyDate.after(firstTermBeginDate) && applyDate.before(firstTermEndDate)){
							monthNum = ExDateUtils.dateDiff("month", applyDate, firstTermBeginDate)+1;
						} else if(applyDate.before(secondTermBeginDate)){
							// 暑假期间
							monthNum = 5l;
						} else if(applyDate.after(secondTermBeginDate) && applyDate.before(secondTermEndDate)){
							// 在第2学期在校期间
							monthNum = 5 + ExDateUtils.dateDiff("month", applyDate, secondTermBeginDate)+1;
						} else {
							// 寒假
							monthNum = 10l;
						}
						// 计算退学学费
						long diff = 10-monthNum;
						if(diff<=0){
							continue;
						}
						money = mfInfo.getCreditFee().divide(BigDecimal.valueOf(10)).multiply(BigDecimal.valueOf(diff)).setScale(2, BigDecimal.ROUND_HALF_UP);
					}
				}
				
				// 检查该条异动记录是否生成过退补学费订单
				String rbsql = "from Refundback where isDeleted=0 and yearInfo.resourceid=? and chargingItems=? and changeInfo.resourceid=?";
				Refundback rb = refundbackService.findUnique(rbsql, feeYear.getResourceid(),chargingItems,changeInfo.getResourceid());
				if(rb != null){
					message.append("该学籍异动已生成预退费补交教材费订单！");
					logger.info(changeInfo.getResourceid()+"该学籍异动已生成预退费补交教材费订单！");;
					continue;
				}
				// 检查该年度是否缴过费
				AnnualFees annualFees = annualFeesService.findUniqueByCondition(studentInfo.getResourceid(), feeYear.getResourceid(), chargingItems);
				if(annualFees == null){
					message.append("请先联系管理员生成年度缴费标准！");
					logger.info("请先联系管理员生成年度缴费标准！");
					continue;
				}
				
				// 创建
				refundbackService.createRefundback(changeInfo, chargingItems, studentInfo, processType, feeYear, money.doubleValue());
				
			} while (false);
			if(message.length()>0){
				statusCode = 300;
			} else {
				message.append("处理成功");
			}
		} catch (Exception e) {
			statusCode = 300;
			message.append("处理失败");
			logger.error("学籍异动审批成功后处理学费退费补交逻辑出错", e);
		} finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message.toString());
		}
		
		return returnMap;
	}

	/**
	 * 获取专业缴费信息map
	 * 
	 * @param grade
	 * @param majorIds
	 * @param feeTerm
	 * @return
	 * @throws Exception
	 */
	private Map<String, MajorFeeInfoVO> getMajorFeeInfoMap(Grade grade,String[] majorIds, Long feeTerm) throws Exception {
		Map<String, MajorFeeInfoVO> majorFeeInfoMap = null;
		List<MajorFeeInfoVO> majorFeeInfoList = findMajorFeeInfo(grade.getResourceid(), majorIds, feeTerm.intValue());
		if(ExCollectionUtils.isNotEmpty(majorFeeInfoList)){
			majorFeeInfoMap = new HashMap<String, MajorFeeInfoVO>(10);
			for(MajorFeeInfoVO mf : majorFeeInfoList){
				majorFeeInfoMap.put(mf.getMajorId(), mf);
			}
		}
		return majorFeeInfoMap;
	}

	/**
	 * 获取某个年某个专业第几年的缴费标准信息
	 * 
	 * @param gradeId
	 * @param majorIds
	 * @param feeTerm
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MajorFeeInfoVO> findMajorFeeInfo(String gradeId, String[] majorIds, int feeTerm) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(10);
		StringBuffer sql = new StringBuffer(500);
		sql.append("select m.basemajorid majorId,m.paymenttype,pd.creditfee,m.teachingType from edu_fee_major m "); 
		sql.append("left join edu_fee_payyear py on py.isdeleted=0 and py.paymenttype=m.paymenttype and py.gradeid=:gradeId "); 
		sql.append("left join edu_fee_payyeardetails pd on pd.isdeleted=0 and pd.payyearid=py.resourceid and pd.feeterm=:feeTerm "); 
		sql.append("where m.isDeleted=0 and m.basemajorid in (:majorIds) "); 
		sql.append("group by m.basemajorid,m.paymenttype,pd.creditfee,m.teachingType ");
		params.put("gradeId", gradeId);
		params.put("feeTerm", feeTerm);
		params.put("majorIds", CollectionUtils.arrayToList(majorIds));
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), MajorFeeInfoVO.class, params);
	}
	
}
