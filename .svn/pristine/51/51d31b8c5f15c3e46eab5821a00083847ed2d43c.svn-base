package com.hnjk.edu.netty.vo;

import com.hnjk.edu.netty.common.StringUtil;
/**
 * 响应基类
 * @author Administrator
 *
 */
public class ResponseBase {
	
	protected final static int dealCodeLen = 3;
	
	protected final static int headCodeLen = 4;
	
	public final static int respCodeLen = 3;
	
	public final static int respMsgLen = 60;
	
	private String headCode="";
	
	private String dealCode="000";//交易码
	
	private String respCode="022";//响应码
	
	private String respMsg="";//响应信息
	
	public ResponseBase() {
		
	}
	
	@SuppressWarnings("finally")
	public String getResponseBase(){
		String returnInfo="";
		try {
			returnInfo=this.headCode+this.dealCode+this.respCode+getRespMsg();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return returnInfo;
		}
	}

	public String getDealCode()  throws Exception{
		
		return dealCode;
	}

	public void setDealCode(String dealCode){
		
		this.dealCode = dealCode;
	}

	public String getRespCode()  throws Exception {		
		
		return respCode;
	}

	public void setRespCode(String respCode){
		
		this.respCode = respCode;
	}

	public String getRespMsg() {
		
		if(StringUtil.strCnlength(respMsg)<respMsgLen){
			respMsg=StringUtil.fillWithSpace(respMsg, respMsgLen);
		}
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		
		this.respMsg = respMsg;
	}

	public String getHeadCode() {
		return headCode;
	}

	public void setHeadCode(String headCode) {
		this.headCode = headCode;
	}
	
	

}
