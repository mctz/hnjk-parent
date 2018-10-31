package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 学生基本信息
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 8, 2016 11:47:24 AM 
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentInfoVO implements Serializable {

	private String studentId;// 学号(或者其他唯一值)，必填
	private String studentName;// 学生姓名，必填
	private String idVerifyCode;// 身份验证码，必填
	private String college;// 学院
	private String major;// 专业
	private String classes;// 班级

}
