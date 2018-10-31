package com.hnjk.edu.recruit.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月2日 上午10:36:27 
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class EnrollmentBookInfoVO {

	private String gradeName;
	private String unitName;
	private String classicName;
	private String majorName;
	private String studentName;
	private String certNum;
	private String phone;
	private String errorMsg;
	

	
}
