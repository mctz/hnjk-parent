package com.opensymphony.workflow.util.taglib;

import com.hnjk.extend.taglib.BaseTagSupport;

public abstract class AbstractWfTag extends BaseTagSupport {

	private static final long serialVersionUID = 3234261027096355720L;

	protected String appFrom = "DB";
	protected String appType = "";
	protected String appWfId = "";
	
	public String getAppFrom() {
		return appFrom;
	}
	public void setAppFrom(String appFrom) {
		this.appFrom = appFrom;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getAppWfId() {
		return appWfId;
	}
	public void setAppWfId(String appWfId) {
		this.appWfId = appWfId;
	}
}
