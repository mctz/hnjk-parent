package com.hnjk.edu.teaching.vo;

import com.hnjk.core.foundation.utils.ExStringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 
 * <code>成绩Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-10 下午05:27:03
 * @see 
 * @version 1.0
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ExamResultsVo implements Serializable {
	
	private String sort;//序号
	private String branchSchool;//教学中心
	private String major;//专业
	//新增班级列
	private String classes; //班级
	
	private String stuId;//学籍ID
	private String studyNo;//学号
	private String name;//姓名
	private String courseName;//课程名称
	private String writtenScore;//卷面成绩
	private String usuallyScore;//平时成绩
	private String onlineScore;//网上学习成绩
	private String integratedScore;//综合成绩
	private String examAbnormity;//成绩异常
	private String flag;//错误信息标识
	private String message;//错误消息
	private String courseId;//课程ID 
	private String examResultsResourceId;//成绩ID
	private String referenceUsuallyScore;//参考平时成绩
	private String examSubId;//考试批次ID
	private String checkStatus;
	private String studentstatus;//学籍状态
	private String grade;//导出的成绩单需有年级
	private String classic;//层次
	private String memo;//备注
	private String examroom;//试室号
	private String examseatno;//座位号
	private String examResultsChs;
    private String examInfoId;
    private String courseScoreType;

	public void setWrittenScore(String writtenScore) {
		this.writtenScore = ExStringUtils.trimToEmpty(writtenScore);
	}

}
