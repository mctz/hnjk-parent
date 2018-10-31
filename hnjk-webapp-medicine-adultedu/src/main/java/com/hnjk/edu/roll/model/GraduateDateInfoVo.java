package com.hnjk.edu.roll.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 毕业信息模版
 * @author Administrator
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class GraduateDateInfoVo {

	/**
	 * 学号
	 */
	private String studyNo;

	/**
	 * 学生姓名
	 */
	private String studentName;

	/**
	 * 身份证号
	 */
	private String certNum;

	/**
	 * 年级
	 */
	private String gradeName;

	/**
	 * 层次名称
	 */
	private String classicName;

	/**
	 * 专业名称
	 */
	private String majorName;

	/**
	 * 班级名称
	 */
	private String className;

	/**
	 * 毕业证号
	 */
	private String diplomaNum;

	/**
	 * 学位名称
	 */
	private String degreeName;

	/**
	 * 学位证号
	 */
	private String degreeNum;

	/**
	 * 导入失败信息
	 */
	private String message;
}
