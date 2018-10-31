package com.hnjk.edu.teaching.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class UniversalExamCountVO {

	private static final String ZERO_FLAG = "-";
	private String courseid;
	private String courseName;
	private String term;
	private String stuCount;//总人数
	private String examType;
	private String aeCount=ZERO_FLAG;//缓考
	private String neCount=ZERO_FLAG;//免考
	private String wCount=ZERO_FLAG;//待考：总人数-缓考-免考
	private String gradeName;
	private String classicName;
	private String unitName;
	private String majorName;

	public String getwCount() {
		return wCount;
	}
	public void setwCount(String wCount) {
		this.wCount = wCount;
	}
}
