package com.hnjk.edu.teaching.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 联动查询信息VO
 * 
 * @author zik, 广东学苑教育发展有限公司
 *
 */
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class LinkageQueryInfo implements Serializable {

	private String unitId;// 教学点ID
	private String unitCode;// 教学点编码
	private String unitName;// 教学点名称
	private String gradeId;// 年级ID
	private String gradeName;// 年级名称
	private String classicId;// 层次ID
	private String classicCode;// 层次编码
	private String classicName;// 层次名称
	private String teachingType;// 学习形式
	private String majorId;// 专业ID
	private String majorCode;// 专业编码
	private String majorName;// 专业名称
	private String classesId;// 班级ID
	private String classesCode;// 班级编号
	private String classesName; // 班级名称

}
