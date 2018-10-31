package com.hnjk.edu.teaching.vo;

import lombok.ToString;

/**
 * 学位申请表中课程成绩VO
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月6日 上午11:46:57 
 * 
 */
@ToString
public class DegreeCourseExamVO {

	private String title;
	private Integer serialNumber;
	private String courseName;
	private String creditHour;
	private String scoreOne;
	private String scoreTwo;
	private String scoreThree;
	private String scoreFour;
	private String scoreFive;
	private String scoreSix;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCreditHour() {
		return creditHour;
	}
	public void setCreditHour(String creditHour) {
		this.creditHour = creditHour;
	}
	public String getScoreOne() {
		return scoreOne;
	}
	public void setScoreOne(String scoreOne) {
		this.scoreOne = scoreOne;
	}
	public String getScoreTwo() {
		return scoreTwo;
	}
	public void setScoreTwo(String scoreTwo) {
		this.scoreTwo = scoreTwo;
	}
	public String getScoreThree() {
		return scoreThree;
	}
	public void setScoreThree(String scoreThree) {
		this.scoreThree = scoreThree;
	}
	public String getScoreFour() {
		return scoreFour;
	}
	public void setScoreFour(String scoreFour) {
		this.scoreFour = scoreFour;
	}
	public String getScoreFive() {
		return scoreFive;
	}
	public void setScoreFive(String scoreFive) {
		this.scoreFive = scoreFive;
	}
	public String getScoreSix() {
		return scoreSix;
	}
	public void setScoreSix(String scoreSix) {
		this.scoreSix = scoreSix;
	}
	
}
