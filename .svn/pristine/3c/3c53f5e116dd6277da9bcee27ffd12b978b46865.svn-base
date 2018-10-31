package com.hnjk.edu.netty.vo;
/**
 * 请求基类
 * @author Administrator
 *
 */
public class RequestBase {

	public final static int dealCodeLen=3;
	
	public final static int headCodeLen=4;
	
	private String headCode;//数据包长描述 4位
	
	private String dealCode;//交易码

	public RequestBase(){};
	
	public RequestBase(String request){
		this.dealCode = request.substring(0, dealCodeLen);
	};
	public String getDealCode() {
		return dealCode;
	}

	/**
	 * 交易码 固定是最前面三位
	 * @param request
	 */
	public void setDealCode(String dealCode) {		
		this.dealCode = dealCode;
	}

	public String getHeadCode() {
		return headCode;
	}

	public void setHeadCode(String headCode) {
		this.headCode = headCode;
	}
	
}
