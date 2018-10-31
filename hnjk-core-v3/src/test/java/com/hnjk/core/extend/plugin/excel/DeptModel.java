package com.hnjk.core.extend.plugin.excel;

/**
 * 用来导入导出的测试模型。
 * <code>DeptModel</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-7 上午09:36:57
 * @see 
 * @version 1.0
 */
public class DeptModel {
	
	private int id;
	private String deptName;
	private String deptCode;
	private String sendFileName;
	private String receiveFileName;
	private int deptNo;
	private String flag;
	private String message;
	
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReceiveFileName() {
		return receiveFileName;
	}
	public void setReceiveFileName(String receiveFileName) {
		this.receiveFileName = receiveFileName;
	}
	public String getSendFileName() {
		return sendFileName;
	}
	public void setSendFileName(String sendFileName) {
		this.sendFileName = sendFileName;
	}
	
	
}

