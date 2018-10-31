package com.hnjk.edu.netty.vo;

import com.hnjk.edu.netty.common.StringUtil;

/**
 * 对账文件请求
 * @author Administrator
 *
 */
public class ReconciliationReq extends RequestBase{

	public final static int totalCountLen = 10;
	
	public final static int totalfeeLen = 10;
	
	public final static int dateLen = 8;
	
	public final static int fileNameLen = 30;
	
	private String totalCount;//总笔数
	
	private String totalfee;//总金额
	
	private String date;//对账日期
	
	private String fileName;//文件名称
	
	public ReconciliationReq(){
		
	}
	
	public ReconciliationReq(String request){
		super(request);
		request= StringUtil.cnfillwithSpace(request);
		this.totalCount = request.substring(dealCodeLen,dealCodeLen+totalCountLen).replaceAll(" ", "");
		this.totalfee = request.substring(dealCodeLen+totalCountLen,dealCodeLen+totalCountLen+totalfeeLen).replaceAll(" ", "");
		this.date = request.substring(dealCodeLen+totalCountLen+totalfeeLen,dealCodeLen+totalCountLen+totalfeeLen+dateLen).replaceAll(" ", "");
		this.fileName = request.substring(dealCodeLen+totalCountLen+totalfeeLen+dateLen,
				dealCodeLen+totalCountLen+totalfeeLen+dateLen+fileNameLen).replaceAll(" ", "");
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
