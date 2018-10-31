package com.hnjk.edu.roll.vo;

/**
 * 成绩信息VO
 * @author zik, 广东学苑教育文化有限公司
 *
 */
public class ExamResultsInfoVO {

	private String courseName_1;// 第一门课程信息
	private String examResult_1;// 第一门课程成绩
	private String courseName_2;// 第二门课程信息
	private String examResult_2;// 第二门课程成绩
	private String courseName_3;// 第三门课程信息
	private String examResult_3;// 第三门课程成绩
	
    public ExamResultsInfoVO(){
    	
    }
    
 public ExamResultsInfoVO(String courseName_1,String examResult_1,String courseName_2,String examResult_2,
		 String courseName_3,String examResult_3){
    	this.courseName_1=courseName_1;
    	this.examResult_1=examResult_1;
    	this.courseName_2=courseName_2;
    	this.examResult_2=examResult_2;
    	this.courseName_3=courseName_3;
    	this.examResult_3=examResult_3;
    }

	public String getCourseName_1() {
		return courseName_1;
	}

	public void setCourseName_1(String courseName_1) {
		this.courseName_1 = courseName_1;
	}

	public String getExamResult_1() {
		return examResult_1;
	}

	public void setExamResult_1(String examResult_1) {
		this.examResult_1 = examResult_1;
	}

	public String getCourseName_2() {
		return courseName_2;
	}

	public void setCourseName_2(String courseName_2) {
		this.courseName_2 = courseName_2;
	}

	public String getExamResult_2() {
		return examResult_2;
	}

	public void setExamResult_2(String examResult_2) {
		this.examResult_2 = examResult_2;
	}

	public String getCourseName_3() {
		return courseName_3;
	}

	public void setCourseName_3(String courseName_3) {
		this.courseName_3 = courseName_3;
	}

	public String getExamResult_3() {
		return examResult_3;
	}

	public void setExamResult_3(String examResult_3) {
		this.examResult_3 = examResult_3;
	}
    
}
