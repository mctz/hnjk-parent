package com.hnjk.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.vo.CheckFileVO;
import com.hnjk.platform.system.cache.CacheAppManager;

/** 
 * 通联对账文件定时器
 * <p>
 * 		每天早上9点半，通过接口获取前一天的对账文件信息
 *     注：目前只应用于广东医，
 * </p>
 * TODO:等订单处理通知接口上线，这个定时器有可能就会关闭
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2017年2月21日 上午9:29:12 
 * 
 */
public class CheckFileScheduler {
	protected static Logger logger = LoggerFactory.getLogger(CheckFileScheduler.class);
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService; 
	
	/**
	 * 获取前一天的对账文件，然后根据教育订单号update对应的
	 * 缴费明细中的支付时间，付款方式，手续费，支付流水号
	 */
	@SuppressWarnings("unchecked")
	public void handlerCheckFile(){
		try{
			do{
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				if(ExStringUtils.isEmpty(schoolCode) || !"10571".equals(schoolCode)){
					continue;
				}
				// 交易日期
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -1);
				Date previousDay = calendar.getTime();
				String tradeDate = ExDateUtils.formatDateStr(previousDay, ExDateUtils.PATTREN_DATE);
				
				String merNo = CacheAppManager.getSysConfigurationByCode("tlmerchantNO").getParamValue();// 商户标识
				if(ExStringUtils.isEmpty(merNo)){
					logger.info("商户标识为空");
					continue;
				}
				ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
				String key = property.getProperty("TLKey");
				if(ExStringUtils.isEmpty(key)){
					logger.info("秘钥为空");
					continue;
				}
				String TLCheckUrl = property.getProperty("TLCheckUrl");
				if(ExStringUtils.isEmpty(TLCheckUrl)){
					logger.info("获取对账文件URL为空");
					continue;
				}
				Map<String, Object> statemenData = studentPaymentService.getStatementByDate(TLCheckUrl,tradeDate,merNo,key);
				int statusCode = (Integer)statemenData.get("statusCode");
				if(statusCode == 300){
					logger.info((String)statemenData.get("msg"));
					continue;
				}
				// 账单详细
				List<CheckFileVO> checkFileVOList = (List<CheckFileVO>)statemenData.get("checkFileList");
				List<StudentPaymentDetails> spdList = null;
				if(ExCollectionUtils.isNotEmpty(checkFileVOList)){
					spdList = new ArrayList<StudentPaymentDetails>();
					StudentPaymentDetails spd = null;
					for(CheckFileVO cf : checkFileVOList){
						spd = studentPaymentDetailsService.findUniqueByProperty("eduOrederNo", cf.getEduOrederNo());
						if(spd!=null){
							spd.setPaymentMethod(cf.getPayMethod());
							spd.setOperateDate(ExDateUtils.convertToDateTime(cf.getPayTime()));
							spd.setChargeMoney(cf.getFee());
							spd.setPayNo(cf.getPayNo());
							spdList.add(spd);
						}
					}
					studentPaymentDetailsService.batchSaveOrUpdate(spdList);
				}
			} while(false);
		} catch(Exception e){
			logger.error("定时器处理对账文件与系统同步出错",e);
		} 
	}
}
