package com.hnjk.edu.learning.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;

/**
 * 题库试题表(单选,多选,通过excel导入)
 * 
 * @author：广东学苑教育发展有限公司.
 */
@Entity
@Table(name="EDU_LEAR_COURSEEXERCISE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseExercises extends BaseModel{
	
	/** 单选题*/
	public static final String SINGLESELECTION = "1";
	/** 多选题*/
	public static final String MUTILPCHIOCE = "2";
	/** 判断题*/
	public static final String CHECKING = "3";
	/** 填空题*/
	public static final String COMPLETION = "4";
	/** 论述题*/
	public static final String ESSAYS = "5";
	/** 材料题*/
	public static final String COMPREHENSION = "6";
	
	
	@Column(name="EXAMTYPE",nullable=false)
	private String examType;//题型，取自字典CodeExamType
	
	@Column(name="DIFFICULT")
	private String difficult;//难度:容易/较难/难   取自字典	

	@Column(name="REQUIREMENT")
	private String requirement;//考试要求:与教学要求度相同，取自字典 :掌握/理解/应用/熟练应用
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")	
	private Course course;//所属课程
		
	@Column(name="KEYWORDS")	
	private String keywords;//关键字，使用,隔开
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="QUESTION",nullable=false)
	private String question;//问题
	
	@Column(name="OPTIONA")
	private String optionA;//选项A
	
	@Column(name="OPTIONB")
	private String optionB;//选项B
	
	@Column(name="OPTIONC")
	private String optionC;//选项C
	
	@Column(name="OPTIOND")
	private String optionD;//选项D
	
	@Column(name="OPTIONE")
	private String optionE;//选项E
	
	@Column(name="OPTIONF")
	private String optionF;//选项F
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="ANSWER")
	private String answer;//答案
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="PARSER")
	private String parser;//解析
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate;//入库时间
	
	@Column(name="FILLINMAN")
	private String fillinMan;//填写人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//填写人ID

	/**3.1.4 新增入学考试的情况*/
	@Column(name="ISENROLEXAM")
	private String isEnrolExam = Constants.BOOLEAN_NO;//是否入学考试
	
	@Column(name="COURSENAME")
	private String courseName;//入学考试科目，从字典取
	
	
	/**3.1.6新增排序号*/
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号
	
	/**3.1.7 新增父ID*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private CourseExercises parent;
	
	@Column(name="EXAMNODETYPE")
	private String examNodeType;//入学考试知识点类型
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder asc")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@DeleteChild(deleteable=true)
	private Set<CourseExercises> childs = new LinkedHashSet<CourseExercises>(0);

	/**3.1.10 新增考试形式*/
	@Column(name="EXAMFORM")
	private String examform;//考试形式，如随堂练习/入学考试/在线考试/期末考试 等
	
	/**3.1.11 新增审计信息*/
	@Column(name="MODIFYMAN",length=50)
	private String modifyMan;//修改人账号
	
	@Column(name="MODIFYDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;//修改时间
	
	/**3.2.5 新增题目选项数，用于超多的选择题*/
	
	@Column(name="ANSWEROPTIONNUM")
	private Integer answerOptionNum = 5;
	
	/**3.2.11 新增是否在线答题，用于主观题的在线做题*/
	@Column(name="ISONLINEANSWER",length=1)
	private String isOnlineAnswer;//是否在线做题
	
	public CourseExercises() {
		super();
	}

	
	public static String covertToCorrectAnswer(String answer){
		if("T".equalsIgnoreCase(answer) || "对".equalsIgnoreCase(answer) || "√".equalsIgnoreCase(answer)){
			return "T";
		} else if("F".equalsIgnoreCase(answer) || "错".equalsIgnoreCase(answer) || "×".equalsIgnoreCase(answer)){
			return "F";
		}
		return answer;
	}

}
