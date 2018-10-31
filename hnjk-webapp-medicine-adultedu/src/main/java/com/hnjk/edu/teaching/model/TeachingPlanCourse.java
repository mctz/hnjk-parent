package com.hnjk.edu.teaching.model;

// default package

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;

/**
 * 教学计划 课程表
 * <code>TeachingPlanCourse</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午02:01:57
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_PLANCOURSE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class TeachingPlanCourse extends BaseModel implements Serializable{

	/**
	 * 课程类型：CodeCourseType
	 * 11	必修课
	 * 22	选修课
	 * 33	限选课
	 * 44	任选课
	 * 55	通识课
	 * 66	实践环节
	 * thesis	毕业论文
	 */
	@Column(name = "COURSETYPE")
	private String courseType;

	@Column(name = "STYDYHOUR")
	private Long stydyHour;// 学时

	@Column(name = "CREDITHOUR",scale=2)
	private Double creditHour;// 学分

	@Column(name = "TARGETANDTASK")
	private String targetAndTask;// 课程目的与任务

	@Column(name = "TERM")
	private String term;// 所在学期

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "SHOWORDER")
	private Integer showOrder = 0;// 排序号

	@OneToOne(cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BEFORECOURSEID")
	private Course beforeCourse;// 关联的前置课程

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANID")
	private TeachingPlan teachingPlan;// 所属教学计划

	@OneToOne(cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 所属课程
	
	
	// @OneToOne(optional = true, cascade = {
	// CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	// @JoinColumn(name = "SYLLABUSID")
	// private Syllabus syllabus;//该课程的教学大纲

	/**3.0.8 新增是否主干课*/
	@Column(name="ISMAINCOURSE")
	private String isMainCourse = Constants.BOOLEAN_NO;//是否主干课
	
	/**
	 * 3.0.9新增课程类型
	 * 课程类别：courseNature
	 * 1100		公共课
	 * 1200		学科基础课
	 * 1300		专业领域课
	 * 1400		全校性通选课
	 * 1500		专业基础课
	 * 1600		专业课
	 * 1700		公共基础课
	 * 2010		选修课
	 * 2020		实践课
	 * 3001		毕业论文设计与撰写
	 * 3002		临床技能训练与考核
	 * 3003		毕业实习
	 */
	@Column(name="COURSENATURE")
	private String courseNature;
	
	/**3.0.9 从课程信息表中调整过来的字段*/	
	@Column(name="TEACHTYPE")
	private String teachType ="facestudy";//#教学方式 统考、非统考
	
	/**3.1.5 新增所关联的教程，改成1:n*/
//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "COURSEBOOKID")
//	private CourseBook courseBook;//关联的教程		
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teachingPlanCourse")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause = "isDeleted=0")
	private Set<TeachingPlanCourseBooks> teachingPlanCourseBooks = new LinkedHashSet<TeachingPlanCourseBooks>(0);//关联的教材 1:n
		
	@Transient
	private String teachingPlanCourseBook;
	
	/***冗余课程属性字段，用来拼装SQL返回的结果集*/
	@Transient
	private String courseId;//courseID

	@Transient
	private String courseCode;//课程编码

	@Transient
	private String courseName;//课程名称
	
	@Transient
	private Long examType;//课程考试形式
	
	@Transient
	private String isUniteExam;//是否统考
	
	@Transient
	private String isDegreeUnitExam;//是否学位统考
	
	/**3.1.0 新增面授学时，考试类别，是否学位课*/
	@Column(name="FACESTUDYHOUR")
	private Long faceStudyHour = 0L;//面授学时
	@Transient
	private Long selfStudyHour = 0L;//自学学时(总学时-面授学时-实验学时)

	/**
	 * 考试类别：CodeExamClassType
	 * 0	待定
	 * 1	考试
	 * 2	考查
	 * 3	统考
	 * 4	实践
	 * 5	英语统考
	 * 6	计算机统考
	 */
	@Column(name="EXAMCLASSTYPE")
	private String examClassType;
	
	@Column(name="ISDEGREECOURSE")
	private String isDegreeCourse = Constants.BOOLEAN_NO;//是否学位课

	@Column(name="TEACHMETHOD")
	private String teachMethod;
	
	/** 2015.11.07 添加实验学时 **/
	@Column(name="experimentperiod ")
	private Long experimentPeriod  = 0L;

	/**
	 * 成绩类型:CodeCourseScoreStyle 默认为考试信息成绩类型
	 * 10	数值型
	 * 11	百分制
	 * 12	150分制
	 * 20	字符型
	 * 22	二分制
	 * 23	三分制
	 * 24	四分制
	 * 25	五分制
	 * 30	等级制
	 */
	@Column(name="scoreStyle")
	private String scoreStyle;
	
	@Transient
	private String openTerm;//实际开课学期
	
	@Transient
	private String courseScoreType;


	/**
	 * @return the teachingPlanCourseBook
	 */
	public String getTeachingPlanCourseBook() {
		String books = "";
		if(null != teachingPlanCourseBooks){
			for(TeachingPlanCourseBooks book : teachingPlanCourseBooks){
				books += book.getCourseBook().getBookName()+",";
			}
		}
		return books;	
	}

	//自学学时=总学时-面授学时-实验学时
	public Long getSelfStudyHour() {
		if(this.stydyHour != null){
			if(this.faceStudyHour != null){
				this.selfStudyHour = this.stydyHour - this.faceStudyHour - this.experimentPeriod;
			} else {
				this.selfStudyHour = this.stydyHour - this.experimentPeriod;
			}
		}
		return selfStudyHour;
	}
	
}