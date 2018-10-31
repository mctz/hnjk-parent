package com.hnjk.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.model.FeeMajor;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.YearPaymentStandard;
import com.hnjk.edu.finance.model.YearPaymentStandardDetails;
import com.hnjk.edu.finance.service.IAnnualFeesService;
import com.hnjk.edu.finance.service.IFeeMajorService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.IYearPaymentStandardService;
import com.hnjk.edu.finance.vo.StudentPaymentInfoVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.cache.CacheAppManager;

/**
 * 处理定时任务
 * @author zik, 广东学苑教育发展有限公司
 *
 */
public class Scheduler {

	protected static Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	@Qualifier("yearPaymentStandardService")
	private IYearPaymentStandardService yearPaymentStandardService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("feeMajorService")
	private IFeeMajorService feeMajorService;
	
	@Autowired
	@Qualifier("annualFeesService")
	private IAnnualFeesService annualFeesService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 *  定时任务（每天凌晨1点执行）
	 *  描述：扫描缴费标准，如果开始缴费日期与当前时间相等并且缴费期数与(缴费记录中的期数+1)相等，
	 *  则将该缴费标准的应缴金额与缴费记录的缴费金额相加的结果作为缴费记录的新缴费金额，修改缴费的截止日期，
	 *  缴费期数，缴费状态。
	 */
	public void scanPaymentStandar() {
		try {
			// 获取缴费即缴费标准信息
			List<StudentPaymentInfoVo> paymentInfoList = yearPaymentStandardService.findPaymentInfoByBeginDate(new Date());
			List<StudentPayment> updateList = new ArrayList<StudentPayment>();
			List<AnnualFees> insertList = new ArrayList<AnnualFees>();//增加年度缴费
			if(ExCollectionUtils.isNotEmpty(paymentInfoList)){
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				StudentInfo si = null;
				for(StudentPaymentInfoVo paymentInfo : paymentInfoList) {
					StudentPayment studentPayment = studentPaymentService.get(paymentInfo.getStudentPaymentId());
					if(studentPayment != null){ 
						si = studentPayment.getStudentInfo();
						Double creditFee = paymentInfo.getCreditFee()==null?0:paymentInfo.getCreditFee();
						// 原始的年级跟现在的年级不相同并且原始的专业与现在的专业相同(针对休学->复学和退学->复学)
						if(!paymentInfo.getGradeId().equals(paymentInfo.getOriginalGrade())
								&& paymentInfo.getMajorId().equals(paymentInfo.getOriginalMajor())){
							StudentPaymentInfoVo _paymentInfo = yearPaymentStandardService.findByCondition(paymentInfo.getOriginalGrade(),
																							paymentInfo.getOriginalMajor(), paymentInfo.getFeeTerm());
							if(_paymentInfo != null) {// 如果之前的缴费标准没有，则按现在的缴费标准走
								creditFee = _paymentInfo.getCreditFee()==null?0:_paymentInfo.getCreditFee();// 原来缴费标准的应缴金额
							}
						}
						Double recpayFee = paymentInfo.getRecpayFee()==null?0:paymentInfo.getRecpayFee();
						Double facepayFee = paymentInfo.getFacepayFee()==null?0:paymentInfo.getFacepayFee();
						Double derateFee = paymentInfo.getDerateFee()==null?0:paymentInfo.getDerateFee();
						Double _facepayFee = facepayFee + derateFee;
						Double _recpayFee = recpayFee + creditFee;
						studentPayment.setChargeTime(paymentInfo.getCreditEndDate());
						studentPayment.setChargeTerm(paymentInfo.getFeeTerm());
						studentPayment.setRecpayFee(_recpayFee);// 应缴金额
						if(_facepayFee==0){
							studentPayment.setChargeStatus("0");
						} else if(_facepayFee>0&& _facepayFee < _recpayFee){
							studentPayment.setChargeStatus("-1");
						} else if (_facepayFee.equals(_recpayFee)){
							studentPayment.setChargeStatus("1");
						}
						updateList.add(studentPayment);
						
						String teachingType = "2";
						if(schoolCode.equals("10598")){// 广西
							teachingType = si.getTeachingType();
						}
						
//						FeeMajor feeMajor  = feeMajorService.findByProperty("resourceid",studentPayment.getStudentInfo().getMajor().getResourceid());
						FeeMajor feeMajor  = feeMajorService.findUniqueByCondition(si.getMajor().getResourceid(), teachingType);
						if(feeMajor == null || ExStringUtils.isBlank(feeMajor.getPaymentType())){
							logger.error("学号为:"+si.getStudyNo()+"没有专业缴费信息");
//							continue;//没有专业缴费信息或者类别
						}
						
						YearPaymentStandard yearPaymentStandard = getYearPaymentStandard(si.getGrade().getResourceid(),feeMajor.getPaymentType());//年缴费标准
						if(yearPaymentStandard == null){
							logger.error("学号为:"+si.getStudyNo()+"没有年级缴费标准");
//							continue;
						}
						
						//这里增加年度缴费记录
						for (YearPaymentStandardDetails payment : yearPaymentStandard.getYearPaymentStandardDetails()) {
							Map<String, Object> annualFeesCondition = new HashMap<String, Object>();
							annualFeesCondition.put("year",(si.getGrade().getYearInfo().getFirstYear()-1+payment.getFeeTerm()));
							annualFeesCondition.put("studentInfoId", si.getResourceid());
							List<AnnualFees> list = annualFeesService.findAnnualFeesByCondition(annualFeesCondition );
							if(list==null || list.size()==0){
								//生成学生年度缴费记录
								AnnualFees annualFees = new AnnualFees();
								annualFees.setChargeStatus(0);
								annualFees.setChargeYear( (int) (si.getGrade().getYearInfo().getFirstYear()-1+payment.getFeeTerm()));
								annualFees.setDerateFee(0.0);
								annualFees.setFacepayFee(0.0);
								annualFees.setMemo("生成缴费信息生成");
								annualFees.setRecpayFee(payment.getCreditFee());
								annualFees.setReturnPremiumFee(0.0);
								annualFees.setStudentInfo(si);
								annualFees.setStudyNo(si.getStudyNo());
								annualFees.setChargingItems("tuition");
								annualFees.setPayAmount(0d);
								
								YearInfo year = yearInfoService.findUniqueByProperty("firstYear", si.getGrade().getYearInfo().getFirstYear()-1+payment.getFeeTerm());
								if(year==null){//还没有学年
//									continue;
								}
								annualFees.setYearInfo(year);//第一学年
								
								insertList.add(annualFees);//加入更新列表
							}
						}
						updateList.add(studentPayment);
					}
				}
				// 批量更新学生缴费记录
				studentPaymentService.batchSaveOrUpdate(updateList);
				annualFeesService.batchSaveOrUpdate(insertList);
			}
		} catch (ServiceException e) {
			logger.error("定时任务(扫描缴费标准)出错", e);
		}
	}
	
	
	public YearPaymentStandard getYearPaymentStandard(String gradeId, String paymentType) throws ServiceException {
		List<YearPaymentStandard> yearPaymentStandardList = yearPaymentStandardService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("grade.resourceid", gradeId),Restrictions.eq("paymentType", paymentType));
		return ExCollectionUtils.isNotEmpty(yearPaymentStandardList) ? yearPaymentStandardList.get(0) : null;
	}
}
