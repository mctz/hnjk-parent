package com.hnjk.edu.netty.vo;

import com.hnjk.edu.netty.common.StringUtil;
/**
 * 缴费请求
 * @author Administrator
 *
 */
public class PaymentReq extends RequestBase{
	
	public final static int userCodeLen = 35;
	
	public final static int feeLen = 10;
	
	public final static int operatorLen = 20;
	
	public final static int payTypeLen = 4;
	
	public final static int dealDateLen = 8;
	
	public final static int dealSerialLen = 20;
	
	public final static int dealUnionSerialLen = 20;
	
	private String userCode;//客户代码
	
	private String fee;//缴费金额
	
	private String operator;//操作员
	
	private String payType;//付款方式
	
	private String dealDate;//交易日期
	
	private String dealSerial;//交易流水  对应系统的  教育系统订单号
	
	private String dealUnionSerial;//关联交易流水

	public PaymentReq(){};
	
	public PaymentReq(String request){
		super(request);
		request= StringUtil.cnfillwithSpace(request);
		
		this.userCode = request.substring(dealCodeLen,dealCodeLen+userCodeLen).replaceAll(" ", "");
		this.fee = request.substring(dealCodeLen+userCodeLen,dealCodeLen+userCodeLen+feeLen).replaceAll(" ", "");
		this.operator = request.substring(dealCodeLen+userCodeLen+feeLen,dealCodeLen+userCodeLen+feeLen+operatorLen).replaceAll(" ", "");
		this.payType = request.substring(dealCodeLen+userCodeLen+feeLen+operatorLen,dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen).replaceAll(" ", "");
		this.dealDate = request.substring(dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen,
				dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen+dealDateLen).replaceAll(" ", "");
		this.dealSerial = request.substring(dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen+dealDateLen,
				dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen+dealDateLen+dealSerialLen).replaceAll(" ", "");		
		this.dealUnionSerial = request.substring(dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen+dealDateLen+dealSerialLen,
				dealCodeLen+userCodeLen+feeLen+operatorLen+payTypeLen+dealDateLen+dealSerialLen+dealUnionSerialLen).replaceAll(" ", "");
	};
	
	public String getUserCode() {
		return userCode.toUpperCase();
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode.toUpperCase();
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getDealSerial() {
		return dealSerial;
	}

	public void setDealSerial(String dealSerial) {
		this.dealSerial = dealSerial;
	}

	public String getDealUnionSerial() {
		return dealUnionSerial;
	}

	public void setDealUnionSerial(String dealUnionSerial) {
		this.dealUnionSerial = dealUnionSerial;
	}
	
	

}
