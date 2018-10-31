package com.hnjk.edu.teaching.vo;

import java.io.Serializable;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeacherVo implements Serializable {

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 登录账号
	 */
	private String username;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 教学站
	 */
	private String unitName;

	/**
	 * 电话
	 */
	private String telphone;

	/**
	 * 是否任课老师
	 */
	private String isTeacher;

	/**
	 * 是否班主任
	 */
	private String isMaster;

	/**
	 * 专业学科类别
	 */
	private String specialty;

	/**
	 * 教师类型
	 */
	private String teacherType;

	/**
	 * 广外 聘用类型
	 */
	private String hireType;

	/**
	 * 担任课程层次
	 */
	private  String courseClassic;

	/**
	 * 授课班级
	 */
	private String courseClasses;

	/**
	 * 现工作单位
	 */
	private String workPlace;

	private String message;

	public void setIsTeacher(String isTeacher) {
		if (ExStringUtils.isBlank(isTeacher)) {
			isTeacher = "";
		}
		this.isTeacher = isTeacher;
	}

	public void setIsMaster(String isMaster) {
		if (ExStringUtils.isBlank(isMaster)) {
			isMaster = "";
		}
		this.isMaster = isMaster;
	}
	
}
