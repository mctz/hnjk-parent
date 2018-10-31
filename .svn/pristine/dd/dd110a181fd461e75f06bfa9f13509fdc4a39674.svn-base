package com.hnjk.edu.netty.vo;
/**
 * 查询账单请求
 * @author Administrator
 *
 */
public class QueryAssessmentReq extends RequestBase{

	public final static int userCodeLen = 35;
	
	private String userCode;//客户代码

	public QueryAssessmentReq(){
		
	}
	
	public QueryAssessmentReq(String request){		
		this.userCode = request.substring(dealCodeLen).toUpperCase().trim();
	} 
	public String getUserCode() {
		return userCode.toUpperCase();
	}

	public void setUserCode(String userCode) {		;
		this.userCode = userCode.toUpperCase();
	}
}
