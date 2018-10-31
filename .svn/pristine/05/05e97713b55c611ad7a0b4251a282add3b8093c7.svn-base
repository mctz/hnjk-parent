package com.hnjk.edu.teaching.vo;

import java.util.Date;

import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 *  统考成绩VO
 * <code>UniversalExamVO</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-7-9 上午 10:55:20
 * @see
 * @version 1.0
 */

@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class UniversalExamVO {

	private String UEid;
	private String studyNo;
	private String studentName;
	private String courseName;
	private String unitName;
	private String gradeName;
	private String classicName;
	private String majorName;
	private String classesName;
	private String score;
	private String currentScore;
	private String checkStatus; 
	private String certificateNo;
	private Date examDate;
	private String whichTime;
	private String examClassType;
	private String operatorName;
	private String studentId;
	private String isPass;

	public String getCurrentScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId, this.currentScore);
	}

	public String getIsPass() {
		isPass=Double.valueOf(this.score)>=60?"Y":"N";
		return isPass;
	}
	
}
