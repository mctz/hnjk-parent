package com.hnjk.edu.recruit.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 入学考试学生作答情况表.
 * <code>RecruitExamStudentAnswer</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-4-25 下午07:31:16
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_STUANSWER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitExamStudentAnswer extends BaseModel{

	private static final long serialVersionUID = 5783282984804244485L;

	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "ENROLLEEINFOID")
	private EnrolleeInfo enrolleeInfo;//学生报名信息
	
//	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
//	@JoinColumn(name = "EXAMPLANID")
//	private RecruitExamPlan recruitExamPlan;//考试场次
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "EXAMPAPERID")
	private CourseExamPapers courseExamPapers;//考试试卷
	
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "EXAMID")
	private CourseExamPaperDetails courseExamPaperDetails;//考试试题
	
	@Column(name="ANSWER")
	private String answer;//学生答案
	
	@Column(name="RESULT", scale=2)
	private Double result;//得分
	
	@Column(name="ISCORRECT")
	private String isCorrect;//是否正确

	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号
	
	/**3.1.10 新增学生学籍ID*/
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;
	
	
	
	/**
	 * @return the studentInfo
	 */
	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	/**
	 * @param studentInfo the studentInfo to set
	 */
	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	/**
	 * @return the showOrder
	 */
	public Integer getShowOrder() {
		return showOrder;
	}

	/**
	 * @param showOrder the showOrder to set
	 */
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	/**
	 * @return the enrolleeInfo
	 */
	public EnrolleeInfo getEnrolleeInfo() {
		return enrolleeInfo;
	}

	/**
	 * @param enrolleeInfo the enrolleeInfo to set
	 */
	public void setEnrolleeInfo(EnrolleeInfo enrolleeInfo) {
		this.enrolleeInfo = enrolleeInfo;
	}


	/**
	 * @return the courseExamPapers
	 */
	public CourseExamPapers getCourseExamPapers() {
		return courseExamPapers;
	}

	/**
	 * @param courseExamPapers the courseExamPapers to set
	 */
	public void setCourseExamPapers(CourseExamPapers courseExamPapers) {
		this.courseExamPapers = courseExamPapers;
	}

	/**
	 * @return the courseExamPaperDetails
	 */
	public CourseExamPaperDetails getCourseExamPaperDetails() {
		return courseExamPaperDetails;
	}

	/**
	 * @param courseExamPaperDetails the courseExamPaperDetails to set
	 */
	public void setCourseExamPaperDetails(
			CourseExamPaperDetails courseExamPaperDetails) {
		this.courseExamPaperDetails = courseExamPaperDetails;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return the result
	 */
	public Double getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Double result) {
		this.result = result;
	}

	/**
	 * @return the isCorrect
	 */
	public String getIsCorrect() {
		return isCorrect;
	}

	/**
	 * @param isCorrect the isCorrect to set
	 */
	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	
}
