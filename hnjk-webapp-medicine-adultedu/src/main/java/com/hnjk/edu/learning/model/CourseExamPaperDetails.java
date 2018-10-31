package com.hnjk.edu.learning.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 考试试卷表 - 试卷内容
 * <code>CourseExamPaperDetails</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-4-13 上午10:22:49
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_EXPAPERDETAILS")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseExamPaperDetails extends BaseModel{

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMID")	
	private CourseExam courseExam;//试题
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAPERID")
	private CourseExamPapers courseExamPapers;//所属试卷
	
	@Column(name="SCORE")
	private Double score;//分值

	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号

}
