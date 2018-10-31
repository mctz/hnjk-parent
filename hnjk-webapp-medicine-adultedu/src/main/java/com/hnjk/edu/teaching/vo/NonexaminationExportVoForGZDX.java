package com.hnjk.edu.teaching.vo;

import com.hnjk.platform.taglib.JstlCustomFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class NonexaminationExportVoForGZDX implements Serializable {

	private String sortNum;//序号
	private String gradeName;//年级
	private String classesName;//班级名称
	private String unitName;//教学点名称
	private String studentName;//学生姓名
	private String classesSum;//班级人数
	private String studentSum;//学生人数
	private String studyNo;//学号
	private String courseName;//补考课程
	private String courseCode;//课程编码
	private String examclasstype;//考核形式 CodeExamClassType
	private String examType;//考试类型
	private String term;//上课学期 CodeCourseTermType
	private String adviseTerm;//建议学期
	private String stuSignature;//学生签名
	private String classicName;//层次
	private String classesmaster;//班主任
	private String masterPhone;//班主任电话
	private String teacherName;//任课老师
	private String teacherPhone;//任课老师电话
	private String recordteacher;//登分老师
	private String recordteacherPhone;//登分老师电话
	private String unitid;//教学点id
	private String stuid;//学生id
	private String stuNames;//学生姓名

	private String courseid;//课程id
	private String classesid;//班级id
	private String stuplanid;//教学计划id
	private String plancourseid;//教学计划课程id
	private String teachingtype;//学习形式 CodeTeachingType
	private String major;//专业
	private String flag;
	private String message;

	public void setTeachingtype(String teachingtype) {
		teachingtype=JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", teachingtype);
		this.teachingtype = teachingtype;
	}

	public void setExamclasstype(String examclasstype) {
		examclasstype= JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", examclasstype);
		this.examclasstype = examclasstype;
	}

	public void setExamType(String examType) {
		examType = JstlCustomFunction.dictionaryCode2Value("ExamResult", examType);
		this.examType = examType;
	}

	public void setTerm(String term) {
		term=JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", term);
		this.term = term;
	}
	
}
