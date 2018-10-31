package com.hnjk.edu.learning.vo;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 
 * <code>用于VO </code><p>
 * 
 * @author：广东学苑教育发展有限公司
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseExamExcelVo implements Serializable {
	
	private String courseName;	
	private String examType;
	private String question;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String optionE;
	private String optionF;
	private String answer;
	private String parser;
	private String difficult;
	
}
