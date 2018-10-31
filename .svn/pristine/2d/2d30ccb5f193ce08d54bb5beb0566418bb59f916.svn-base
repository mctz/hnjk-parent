package com.hnjk.edu.teaching.vo;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.platform.taglib.JstlCustomFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 补考学生VO
 * @author link
 *
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class FailExamStudentVo implements Serializable {
	private String courseName ;
	private String unitName ;
	private String majorName ;
	private String studyNo ;
	private String studentName ;
	private String className;
	/**
	 * 学生的resourceid
	 */
	private String resourceid;
	private String integratedscore;
	private String examabnormity;
	/**
	 * 补考成绩
	 */
	private String nonexamabnormity;
	private String isPass;
	private String gradename;
	private String classicname;
	private String examType;
	private String term;
	private String yearId;
	private String courseCode;
	private Date startTime;//考试预约时间
	private Date examStartTime;//考试开始时间
	private Date examinputStartTime;//成绩录入时间
	/**
	 * 学生签名一栏
	 */
	private String signer;
	private String lecturerName;
	private String nextScore;
	private String sortNum;
	private String firstyear;
	private String subyear;
	private String subterm;
	private String finalPass;
	/**
	 * 课程教学类型：面授、网络
	 */
	private String teachType;

	public String getIntegratedscore() {
		return this.integratedscore==null?JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examabnormity):ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.resourceid,this.integratedscore);
	}

	public String getNextScore() {
		if(this.nonexamabnormity==null){
			return this.nextScore="无";
		}
		return this.nextScore ==null?JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", nonexamabnormity):ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.resourceid,this.nextScore);
	}

	public Date getExamStartTime() {
		if (examStartTime == null) {
			return startTime;
		}
		return examStartTime;
	}

	public String getLecturerName() {
		return ExStringUtils.toString(lecturerName);
	}
}
