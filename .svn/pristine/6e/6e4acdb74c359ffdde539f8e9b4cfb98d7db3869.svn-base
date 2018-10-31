package com.hnjk.edu.learning.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.platform.system.model.Attachs;

/**
 * 课程 - 课后作业主干表
 * <code>MateResource</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:22:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_EXERCISE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Exercise extends BaseModel{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLSID")
	private ExerciseBatch exerciseBatch;//所属作业批次
	
	//@Column(name="EXERCISETYPE")
	//private String exerciseType;//题型：单选/多选/判断/填空/论述
	
	//@Lob
	//@Basic(fetch = FetchType.LAZY)
	//@Column(name="QUESTION" ,nullable=false)
	//private String question;//问题内容
	

	
	//@Column(name="DIFFICULT")
	//private String difficult;//难易度：容易/中等/难
	
	//@Column(name="KEYWORDS")
	//private String keywords;//关键字
	
	//@Lob
	//@Basic(fetch = FetchType.LAZY)
	//@Column(name="PARSER")
	//private String parser;//作业评析
	
	
	
	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "exercise")
	//@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	//@OrderBy("showOrder ASC")
	//@Where(clause="isDeleted=0")
	//@DeleteChild(deleteable=true)
	//private Set<ExerciseAnswer> answers = new LinkedHashSet<ExerciseAnswer>(0);
	
	@Column(name="SCORE")
	private Double score = 0d;//分值
	
	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号

	/**3.1.2 新增题库试题*/	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMID")
	private CourseExam courseExam;//试题
	
	@Transient
	private StudentExercise studentExercise;
		
	/**3.1.11 新增知识结构树，用来显示属于哪个章节的作业（冗余）*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSTREEID")
	private Syllabus syllabus;//知识结构树
	
	@Column(name="LIMITNUM")
	private Integer limitNum = 0;//字数限制
	
	@Column(name="HASATTACHS")
	private String hasAttachs = Constants.BOOLEAN_NO;// 是否有附件
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();//离线作业

	
}
