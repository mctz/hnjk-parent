package com.hnjk.edu.netty.vo;

import com.hnjk.edu.netty.common.StringUtil;
/**
 * 缴费响应
 * @author Administrator
 *
 */
public class PaymentResp extends ResponseBase{
	
	public final static int dealSerialLen = 20;
	
	public final static int dealSerial4BankLen = 20;
	
	public final static int currentFeeLen = 10;
	
	public final static int deductionFeeLen = 10;
	
	public final static int preStoreLen = 10;

	private String dealSerial="";//交易流水
	
	private String dealSerial4Bank="";//银行交易流水
	
	private String currentFee="";//本次缴费金额
	
	private String deductionFee="";//本次缴费抵扣金额
	
	private String preStore="";//本次缴费后预存金额
	
	public PaymentResp(){
	}

	public String getPayment(){		
		
		return getResponseBase()+getDealSerial()+getDealSerial4Bank()+getCurrentFee() +getDeductionFee()+getPreStore();
	}
	
	public String getDealSerial() {
		if(StringUtil.strCnlength(dealSerial)<dealSerialLen){
			dealSerial=StringUtil.fillWithSpace(dealSerial, dealSerialLen);
		}
		return dealSerial;
	}

	public void setDealSerial(String dealSerial) {
		
		this.dealSerial = dealSerial;
	}

	public String getDealSerial4Bank() {
		if(StringUtil.strCnlength(dealSerial4Bank)<dealSerial4BankLen){
			dealSerial4Bank=StringUtil.fillWithSpace(dealSerial4Bank, dealSerial4BankLen);
		}
		return dealSerial4Bank;
	}

	public void setDealSerial4Bank(String dealSerial4Bank) {
		
		this.dealSerial4Bank = dealSerial4Bank;
	}

	public String getCurrentFee() {
		if(StringUtil.strCnlength(currentFee)<currentFeeLen){
			currentFee=StringUtil.fillWithSpace(currentFee, currentFeeLen);
		}
		return currentFee;
	}

	public void setCurrentFee(String currentFee) {
		
		this.currentFee = currentFee;
	}

	public String getDeductionFee() {
		if(StringUtil.strCnlength(deductionFee)<deductionFeeLen){
			deductionFee=StringUtil.fillWithSpace(deductionFee, deductionFeeLen);
		}
		return deductionFee;
	}

	public void setDeductionFee(String deductionFee) {
		
		this.deductionFee = deductionFee;
	}

	public String getPreStore() {
		if(StringUtil.strCnlength(preStore)<preStoreLen){
			preStore=StringUtil.fillWithSpace(preStore, preStoreLen);
		}
		return preStore;
	}

	public void setPreStore(String preStore) {
		
		this.preStore = preStore;
	}
	
	
	
}
