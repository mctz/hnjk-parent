package com.hnjk.edu.teaching.vo;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 
 * <code>未预约学习学生信息Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-7-27 下午02:14:03
 * @see 
 * @version 1.0
 */
public class NotCourseOrderStuInfoVo {
	
	private String SYSUSERID;     //系统用户ID
	private String USERNAME;      //系统用户名
	private String RESOURCEID;    //学籍ID
	private String NAME;          //姓名
	private String MOBILE;        //手机
	private String OFFICEPHONE;   //办公电话
	private String CONTACTPHONE;  //联系电话
	private String EMAIL;         //电子邮件
	private String GRADENAME;     //年级
	private String CLASSICNAME;   //层次
	private String MAJORNAME;     //专业
	private String UNITNAME;      //校外学习中心

	private String TEMPCONTACTPHONE="";//联系方式的临时变量 
	
	
	
	public String getSYSUSERID() {
		return SYSUSERID;
	}
	public void setSYSUSERID(String sYSUSERID) {
		SYSUSERID = sYSUSERID;
	}
	
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getRESOURCEID() {
		return RESOURCEID;
	}
	public void setRESOURCEID(String rESOURCEID) {
		RESOURCEID = rESOURCEID;
	}
	public String getOFFICEPHONE() {
		return OFFICEPHONE;
	}
	public void setOFFICEPHONE(String oFFICEPHONE) {
		OFFICEPHONE = oFFICEPHONE;
	}
	public String getCONTACTPHONE() {
		return CONTACTPHONE;
	}
	public void setCONTACTPHONE(String cONTACTPHONE) {
		CONTACTPHONE = cONTACTPHONE;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getTEMPCONTACTPHONE() {
		if (ExStringUtils.isNotEmpty(MOBILE)) {
			this.TEMPCONTACTPHONE+="＼"+MOBILE;
		}
		if (ExStringUtils.isNotEmpty(OFFICEPHONE)) {
			this.TEMPCONTACTPHONE+="＼"+OFFICEPHONE;
		}
		if (ExStringUtils.isNotEmpty(CONTACTPHONE)) {
			this.TEMPCONTACTPHONE+="＼"+CONTACTPHONE;
		}
		if (this.TEMPCONTACTPHONE.length()>0) {
			this.TEMPCONTACTPHONE = this.TEMPCONTACTPHONE.substring(1);
		}
		return this.TEMPCONTACTPHONE;
	}
	public void setTEMPCONTACTPHONE(String TEMPCONTACTPHONE) {
		this.TEMPCONTACTPHONE = TEMPCONTACTPHONE;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getGRADENAME() {
		return GRADENAME;
	}
	public void setGRADENAME(String gRADENAME) {
		GRADENAME = gRADENAME;
	}
	public String getCLASSICNAME() {
		return CLASSICNAME;
	}
	public void setCLASSICNAME(String cLASSICNAME) {
		CLASSICNAME = cLASSICNAME;
	}
	public String getMAJORNAME() {
		return MAJORNAME;
	}
	public void setMAJORNAME(String mAJORNAME) {
		MAJORNAME = mAJORNAME;
	}
	public String getUNITNAME() {
		return UNITNAME;
	}
	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}

	
	
}
