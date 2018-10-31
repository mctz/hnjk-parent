package com.hnjk.edu.teaching.vo;

import com.hnjk.platform.taglib.JstlCustomFunction;

/**
 * 导出实考与报考人数
 * 序号，考试批次，教学方式（统考/非统考），课程名称，报考人数，实考人数，备注
 * @fw
 */
public class EnterExamToTalVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private String examSubName;		//考试批次
	private String teachTypeName;	//教学方式（统考/非统考）
	private String courseid;
	private String courseName;		//课程名称
	private String enterTotal;		//报考人数
	private String realTotal;		//实考人数
	private String meto;			//备注
	
	public String getExamSubName() {
		return examSubName;
	}
	public void setExamSubName(String examSubName) {
		this.examSubName = examSubName;
	}
	public String getTeachTypeName() {
//		return teachTypeName;
		return JstlCustomFunction.dictionaryCode2Value("teachType",teachTypeName);
	}
	public void setTeachTypeName(String teachTypeName) {
		this.teachTypeName = teachTypeName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getEnterTotal() {
		return enterTotal;
	}
	public void setEnterTotal(String enterTotal) {
		this.enterTotal = enterTotal;
	}
	public String getRealTotal() {
		return realTotal;
	}
	public void setRealTotal(String realTotal) {
		this.realTotal = realTotal;
	}
	public String getMeto() {
		return meto;
	}
	public void setMeto(String meto) {
		this.meto = meto;
	}
	
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	
}
