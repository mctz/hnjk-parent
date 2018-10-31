package com.hnjk.edu.netty.vo;

public class ReconciliationInfo {
	
	public final static int feeLen = 10;
	
	public final static int userCodeLen = 35;
	
	public final static int dealSerialLen = 20;
	
	private String userCode;//客户代码
	
	private String dealSerial;//银行交易流水
	
	private String fee;//缴费金额
	
	public ReconciliationInfo(String line){
		this.userCode = line.substring(0,userCodeLen).toUpperCase().trim();
		this.dealSerial = line.substring(userCodeLen,userCodeLen+dealSerialLen).trim();
		this.fee = line.substring(userCodeLen+dealSerialLen,userCodeLen+dealSerialLen+feeLen).trim();
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDealSerial() {
		return dealSerial;
	}

	public void setDealSerial(String dealSerial) {
		this.dealSerial = dealSerial;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	
	
}
