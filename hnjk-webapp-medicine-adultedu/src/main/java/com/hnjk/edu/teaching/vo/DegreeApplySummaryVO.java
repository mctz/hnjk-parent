package com.hnjk.edu.teaching.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 学位申请汇总
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月8日 下午4:16:03 
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class DegreeApplySummaryVO {

	private Integer serialNumber;
	private String name;
	private String gender;
	private String majorType;
	private String majorName;
	private String candidateNo;
	private String graduateDate;
	private String languageCode;
	private String degreeNum;
	private String certNum;
	private String avgScore;
	private String thesisScore;
	private String phone;
	private String title;
	private String email;
	private String memo;

}
