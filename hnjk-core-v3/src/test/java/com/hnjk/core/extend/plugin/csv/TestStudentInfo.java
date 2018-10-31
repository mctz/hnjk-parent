package com.hnjk.core.extend.plugin.csv;

import java.io.Serializable;

import com.hnjk.core.support.base.model.BaseModel;


public class TestStudentInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8139205990153248238L;
	
	//姓名","学号","性别","身份证","民族","	年级","培养层次","专业","学习中心","学籍状态","帐号状态
		
	private String studyno;// 学生号

	private String studentname;// 学生姓名，冗余字段
	
	private String gender;
	
	private String certnum;
	
	private String nation;
	
	private String gradename;
	
	private String classicname;
	
	private String majorname;
	
	private String brschool;
		
	private String studentstatus;// 学籍状态 字典：在学 11/休学 12/退学 13/勒令退学 14/开除学籍 15/毕业 16/自动流失 17/学习期限已过 18
	
	private String accoutstatus;

	public String[] toArray(){
		return new String[]{getStudentname(),getStudyno(),
				getGender(),getCertnum(),getNation(),getGradename(),
				getClassicname(),getMajorname(),getBrschool(),
				getStudentstatus(),getAccoutstatus()};
	}
	
	
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the studyno
	 */
	public String getStudyno() {
		return studyno;
	}

	/**
	 * @param studyno the studyno to set
	 */
	public void setStudyno(String studyno) {
		this.studyno = studyno;
	}

	/**
	 * @return the studentname
	 */
	public String getStudentname() {
		return studentname;
	}

	/**
	 * @param studentname the studentname to set
	 */
	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}

	/**
	 * @return the certnum
	 */
	public String getCertnum() {
		return certnum;
	}

	/**
	 * @param certnum the certnum to set
	 */
	public void setCertnum(String certnum) {
		this.certnum = certnum;
	}

	/**
	 * @return the nation
	 */
	public String getNation() {
		return nation;
	}

	/**
	 * @param nation the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}

	/**
	 * @return the gradename
	 */
	public String getGradename() {
		return gradename;
	}

	/**
	 * @param gradename the gradename to set
	 */
	public void setGradename(String gradename) {
		this.gradename = gradename;
	}

	/**
	 * @return the classicname
	 */
	public String getClassicname() {
		return classicname;
	}

	/**
	 * @param classicname the classicname to set
	 */
	public void setClassicname(String classicname) {
		this.classicname = classicname;
	}

	/**
	 * @return the majorname
	 */
	public String getMajorname() {
		return majorname;
	}

	/**
	 * @param majorname the majorname to set
	 */
	public void setMajorname(String majorname) {
		this.majorname = majorname;
	}

	/**
	 * @return the brschool
	 */
	public String getBrschool() {
		return brschool;
	}

	/**
	 * @param brschool the brschool to set
	 */
	public void setBrschool(String brschool) {
		this.brschool = brschool;
	}

	/**
	 * @return the studentstatus
	 */
	public String getStudentstatus() {
		return studentstatus;
	}

	/**
	 * @param studentstatus the studentstatus to set
	 */
	public void setStudentstatus(String studentstatus) {
		this.studentstatus = studentstatus;
	}

	/**
	 * @return the accoutstatus
	 */
	public String getAccoutstatus() {
		return accoutstatus;
	}

	/**
	 * @param accoutstatus the accoutstatus to set
	 */
	public void setAccoutstatus(String accoutstatus) {
		this.accoutstatus = accoutstatus;
	}
	
	
	
	
	

}