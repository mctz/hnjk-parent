package com.hnjk.edu.netty.vo;

import com.hnjk.edu.netty.common.StringUtil;
/**
 * 查询账单响应基类
 * @author Administrator
 *
 */
public class QueryAssessmentResp extends ResponseBase{
	
	public final static int userCodeLen = 35;
	
	public final static int userNameLen = 100;
	
	public final static int userAddressLen = 100;
	
	public final static int totalAssessmentLen = 10;
	
	public final static int costInfoLen = 100;

	private String userCode="";//用户代码
	
	private String userName="";//用户名称
	
	private String userAddress="";//用户地址
	
	private String totalAssessment="";//应缴总额 （单位：元）
	
	private String costInfo="";//费用详情信息
	
	public QueryAssessmentResp(){
	}
	
	public String getQueryAssessment(){
		return getResponseBase()+getUserCode()+getUserName()+getUserAddress()+getTotalAssessment()+getCostInfo();
	}

	public String getUserCode() {
		if(StringUtil.strCnlength(userCode)<userCodeLen){
			userCode=StringUtil.fillWithSpace(userCode, userCodeLen);
		}
		return userCode;
	}

	public void setUserCode(String userCode) {
		
		this.userCode = userCode;
	}

	public String getUserName() {
		if(StringUtil.strCnlength(userName)<userNameLen){
			userName=StringUtil.fillWithSpace(userName, userNameLen);
		}
		return userName;
	}

	public void setUserName(String userName) {
		
		this.userName = userName;
	}

	public String getUserAddress() {
		if(StringUtil.strCnlength(userAddress)<userAddressLen){
			userAddress=StringUtil.fillWithSpace(userAddress, userAddressLen);
		}
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		
		this.userAddress = userAddress;
	}

	public String getTotalAssessment() {
		if(StringUtil.strCnlength(totalAssessment)<totalAssessmentLen){
			totalAssessment=StringUtil.fillWithSpace(totalAssessment, totalAssessmentLen);
		}
		return totalAssessment;
	}

	public void setTotalAssessment(String totalAssessment) {
		
		this.totalAssessment = totalAssessment;
	}

	public String getCostInfo() {
		if(StringUtil.strCnlength(costInfo)<costInfoLen){
			costInfo=StringUtil.fillWithSpace(costInfo, costInfoLen);
		}
		return costInfo;
	}

	public void setCostInfo(String costInfo) {
		
		this.costInfo = costInfo;
	}
	
}
