package com.hnjk.edu.teaching.model;

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

/**
 * 学生考试座位安排表.
 * <code>ExamDistribu</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-21 上午09:46:29
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMDISTRIBU")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamDistribu extends BaseModel{

	private static final long serialVersionUID = -2196382930086529262L;
	
//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "EXAMSUBID")
//	private ExamSub examSub;//考试批次
//	
//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "STUDENTID")
//	private StudentInfo studentInfo;//学生学籍
//	
//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "EXAMROOMID")
//	private Examroom examroom;//考场
//	
//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "COURSEID")
//	private Course course;
	
	/**3.0.9 修改*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMRESULTSID")
	private ExamResults examResults;
	
	@Column(name="EXAMSEATNUM")
	private String examSeatNum;//座位号

	
	
	public ExamResults getExamResults() {
		return examResults;
	}

	public void setExamResults(ExamResults examResults) {
		this.examResults = examResults;
	}

	public String getExamSeatNum() {
		return examSeatNum;
	}

	public void setExamSeatNum(String examSeatNum) {
		this.examSeatNum = examSeatNum;
	}

	

}
