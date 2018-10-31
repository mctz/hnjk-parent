package com.hnjk.edu.recruit.vo;

import com.hnjk.security.model.OrgUnit;

public class ExameeInfoVo {

	private String RECRUITPLANNAME;
	private String KSH;
	private String XM;
	private String SFZH;
	private String LQZYMC;
	private String ZKZH;
	private String XBDM;
	private String CSRQ;
	private String KSZT;
	private String UNITNAME;
	private String MATRICULATENOTICENO;
	private String LXDH;
	private String errorMsg;
	
	private String flag ;
	private String message;
	
	private OrgUnit orgUnit;
	
	public String getRECRUITPLANNAME() {
		return RECRUITPLANNAME;
	}
	public void setRECRUITPLANNAME(String rECRUITPLANNAME) {
		RECRUITPLANNAME = rECRUITPLANNAME;
	}
	public String getKSH() {
		return KSH;
	}
	public void setKSH(String kSH) {
		KSH = kSH;
	}
	public String getXM() {
		return XM;
	}
	public void setXM(String xM) {
		XM = xM;
	}
	public String getSFZH() {
		return SFZH;
	}
	public void setSFZH(String sFZH) {
		SFZH = sFZH;
	}
	public String getLQZYMC() {
		return LQZYMC;
	}
	public void setLQZYMC(String lQZYMC) {
		LQZYMC = lQZYMC;
	}
	public String getZKZH() {
		return ZKZH;
	}
	public void setZKZH(String zKZH) {
		ZKZH = zKZH;
	}
	public String getXBDM() {
		return XBDM;
	}
	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}
	public String getCSRQ() {
		return CSRQ;
	}
	public void setCSRQ(String cSRQ) {
		CSRQ = cSRQ;
	}
	public String getKSZT() {
		return KSZT;
	}
	public void setKSZT(String kSZT) {
		KSZT = kSZT;
	}
	public String getUNITNAME() {
		return UNITNAME;
	}
	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}
	public String getMATRICULATENOTICENO() {
		return MATRICULATENOTICENO;
	}
	public void setMATRICULATENOTICENO(String mATRICULATENOTICENO) {
		MATRICULATENOTICENO = mATRICULATENOTICENO;
	}
	public String getLXDH() {
		return LXDH;
	}
	public void setLXDH(String lXDH) {
		LXDH = lXDH;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public OrgUnit getOrgUnit() {
		return orgUnit;
	}
	public void setOrgUnit(OrgUnit orgUnit) {
		this.orgUnit = orgUnit;
	}
	
}
