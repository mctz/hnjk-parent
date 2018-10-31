package com.hnjk.job;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpUrlConnectionUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.util.SignUtils;
import com.hnjk.edu.finance.vo.EnquiryPaymentDetailsVO;
import com.hnjk.edu.finance.vo.PaymentRerultVO;
import com.hnjk.edu.finance.vo.ResultVO;
import com.hnjk.platform.system.cache.CacheAppManager;

/** 
 * 查询缴费情况定时器
 * 目前供广外使用
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年1月19日 下午2:28:44 
 * 
 */
public class EnquiryPaymentScheduler {

	protected static Logger logger = LoggerFactory.getLogger(EnquiryPaymentScheduler.class);
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	/**
	 * 获取前一天的对账文件，然后根据教育订单号update对应的
	 * 插入学生缴费详细情况，更新缴费标准信息
	 */
	public void enquiryPayment() {
		try{
			do{
				// 学校代码
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				// 目前只针对广外处理
				if(ExStringUtils.isEmpty(schoolCode) || !"11846".equals(schoolCode)){
					continue;
				}
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				String startDate = ExDateUtils.formatDateStr(calendar.getTime(), ExDateUtils.PATTREN_DATE_EN);
				String endDate = ExDateUtils.formatDateStr(calendar.getTime(), ExDateUtils.PATTREN_DATE_EN);
				// TODO:加入时间的判断，开放查询的时间段
				studentPaymentService.queryBatchForGW(startDate,endDate);
			} while(false);
		} catch(Exception e){
			logger.error("定时器查询学生缴费情况出错",e);
		} 
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
		test();
	}

	private static void test() throws UnsupportedEncodingException {
		// 接口基本信息
		String serverUrl = "http://gkxy.gdufs.edu.cn";
		String queryBatch = "/third/queryBatch";
		String sysId = "100000";
		String sysCert = "bc0d3888fa8811e7a11b8c89a51ce8c7";
		// 缴费批次编号（年份）
		String year = "2018";
		String startDate = "20180406";
		String endDate = "20180406";
		StringBuffer serverUrlAndParam = new StringBuffer(500);
		serverUrlAndParam.append(serverUrl).append(queryBatch).append("?");
		Map<String, Object> queryParamMap = new HashMap<String, Object>(10);
		queryParamMap.put("sysId", sysId);
		queryParamMap.put("year", year);
		queryParamMap.put("sysCert", sysCert);
		queryParamMap.put("startDate", startDate);
		queryParamMap.put("endDate", endDate);
		queryParamMap.put("sign", SignUtils.signByMD5(queryParamMap).toLowerCase());
		// 去除密钥
		queryParamMap.remove("sysCert");
		String result = HttpUrlConnectionUtils.getRequest(serverUrlAndParam.append(SignUtils.createQueryString(queryParamMap)).toString());
		ResultVO resultVO = JsonUtils.jsonToBean(result, ResultVO.class);
		if(!"0".equals(resultVO.getCode())){
			logger.info(resultVO.getMsg());
		}
		List<PaymentRerultVO> paymentRerultVOList = resultVO.getData();
		String certNum = null;
		for(PaymentRerultVO pvo : paymentRerultVOList){
			// 获取身份证号
			certNum = pvo.getIdCard();
			if(ExStringUtils.isBlank(certNum)){
				continue;
			}
			List<EnquiryPaymentDetailsVO> epaymentDetailsList = pvo.getRecords();
			if(CollectionUtils.isEmpty(epaymentDetailsList)){
				continue;
			}
		}
	}
	
}
