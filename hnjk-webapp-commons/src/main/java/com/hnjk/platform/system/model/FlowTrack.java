package com.hnjk.platform.system.model;

import java.util.Date;

/**
 * 流程跟踪模型. <code>FlowTrack</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-1-8 下午04:55:51
 * @see 
 * @version 1.0
 */
public class FlowTrack implements java.io.Serializable{
	
	private static final long serialVersionUID = -1903719097704785584L;
	
	private String currentStep;//当前位置
	private String histroySetp;//上一位置
	private String action;//动作
	private String status;//状态
	private String owner;//所有者
	private Date startDat;//开始时间
	private Date endDate;//结束时间
	private String caller;//处理者
	private String opinion;//处理意见
	private Date doneDate;//处理时间
	
	
	
	public Date getDoneDate() {
		return doneDate;
	}
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getHistroySetp() {
		return histroySetp;
	}
	public void setHistroySetp(String histroySetp) {
		this.histroySetp = histroySetp;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Date getStartDat() {
		return startDat;
	}
	public void setStartDat(Date startDat) {
		this.startDat = startDat;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
