package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
/**
 * 课程 - 随堂练习-学生得分累积情况
 * <code>MateResource</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2013-12-24 上午11：55：00
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_STUCOURSEEXAMCOUNT")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StuActiveCourseExamCount extends BaseModel{

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学生学籍信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")	
	@Where(clause="isDeleted=0")
	private YearInfo yearInfo;// 所属年度
	
	@Column(name="TERM")
	private String term;//学期
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//课程
	
	@Column(name="CORRECTCOUNT")
	private Integer correctCount=0;//答对题目数
	
	@Column(name="COUNTS")
	private Integer counts=0;//总题数
	
	@Column(name="CORRECTPER")
	private Double correctper=0.0;//答对率
	
	@Column(name="STATUS")
	private String status=Constants.EXAMRESULT_ABNORAMITY_0;//提交状态 0--全部未提交，1-部分提交，2-全部提交
	
	@Column(name="REFRESHDATE")
	private Date refreshDate;//最新刷新时间
	
}
