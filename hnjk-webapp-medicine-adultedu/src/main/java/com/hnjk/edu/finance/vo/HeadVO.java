package com.hnjk.edu.finance.vo;

import com.hnjk.platform.system.cache.CacheAppManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 8, 2016 2:06:54 PM 
 * 
 */

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class HeadVO implements Serializable {
	
	private String serviceCode;// 服务接口代码，必填
	private String visitorId;// 接入方商户号，必填
	private String sendTime;// 请求发送时间，必填
	private String receiveUrl;// 处理结果通知url
	
	public HeadVO(){}
	
	public HeadVO(String serviceCode, String sendTime,String receiveUrl) {
		super();
		this.serviceCode = serviceCode;
		this.visitorId = CacheAppManager.getSysConfigurationByCode("tlmerchantNO").getParamValue();
		this.sendTime = sendTime;
		this.receiveUrl = receiveUrl;
	}
	
}
