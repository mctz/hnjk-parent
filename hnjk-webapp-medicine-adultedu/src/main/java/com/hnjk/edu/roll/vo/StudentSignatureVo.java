package com.hnjk.edu.roll.vo;

import java.util.ArrayList;
import java.util.List;

public class StudentSignatureVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4181120716845092323L;
	public StudentSignatureVo(){};
	public StudentSignatureVo(String studyNo,int sortNum,int row){
		this.sortNum1 =String.valueOf(sortNum);
		this.sortNum2=String.valueOf(sortNum+row);
		this.sortNum3=String.valueOf(sortNum+row*2);
		this.studentName1="";
		this.studentName2="";
		this.studentName3="";
		this.studyNo1 = "";
		this.studyNo2 = "";
		this.studyNo3 = "";
	}
	public StudentSignatureVo(String studyNo,String studentName){	
		this.studentName1="";
		this.studentName2="";		
		this.studyNo1 = "";
		this.studyNo2 = "";
		this.setEmptyCell("");
	}
	
	public List<StudentSignatureVo> tableInit(int row,int pageNum,int column){
		List<StudentSignatureVo> list = new ArrayList<StudentSignatureVo>();
		for(int i=0;i<row;i++){
			StudentSignatureVo tmp = new StudentSignatureVo("",i+1+row*column*(pageNum-1),row);
			list.add(tmp);
		}
		return list;
	}
	
	
	private String emptyCell;
	private String sortNum1;
	private String sortNum2;
	private String sortNum3;
	private String studyNo1;
	private String studyNo2;
	private String studyNo3;
	private String studentName1;
	private String studentName2;
	private String studentName3;
	
	public String getSortNum1() {
		return sortNum1;
	}
	public void setSortNum1(String sortNum1) {
		this.sortNum1 = sortNum1;
	}
	public String getSortNum2() {
		return sortNum2;
	}
	public void setSortNum2(String sortNum2) {
		this.sortNum2 = sortNum2;
	}
	public String getSortNum3() {
		return sortNum3;
	}
	public void setSortNum3(String sortNum3) {
		this.sortNum3 = sortNum3;
	}
	public String getStudyNo1() {
		return studyNo1;
	}
	public void setStudyNo1(String studyNo1) {
		this.studyNo1 = studyNo1;
	}
	public String getStudyNo2() {
		return studyNo2;
	}
	public void setStudyNo2(String studyNo2) {
		this.studyNo2 = studyNo2;
	}
	public String getStudyNo3() {
		return studyNo3;
	}
	public void setStudyNo3(String studyNo3) {
		this.studyNo3 = studyNo3;
	}
	public String getStudentName1() {
		return studentName1;
	}
	public void setStudentName1(String studentName1) {
		this.studentName1 = studentName1;
	}
	public String getStudentName2() {
		return studentName2;
	}
	public void setStudentName2(String studentName2) {
		this.studentName2 = studentName2;
	}
	public String getStudentName3() {
		return studentName3;
	}
	public void setStudentName3(String studentName3) {
		this.studentName3 = studentName3;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEmptyCell() {
		return emptyCell;
	}
	public void setEmptyCell(String emptyCell) {
		this.emptyCell = emptyCell;
	}

	
}
