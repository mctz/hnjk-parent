package com.hnjk.edu.basedata.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;

/**
 * <code>Course</code>基础数据-课程<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午01:35:10
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BASE_COURSE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Course extends BaseModel {
	 private static final long serialVersionUID = -1991495113727768973L;
	
	 @Column(name="COURSECODE",nullable=false,unique=true)	
	 private String courseCode;//课程编码
	
	 @Column(name="COURSENAME",nullable=false)
     private String courseName;//课程名称
	 
	 @Column(name="COURSEENNAME")
     private String courseEnName;//课程英文名称
	 
	 @Column(name="COURSESHORTNAME")	 
     private String courseShortName;//课程简称
	 
	 /*学时、学分移到教学计划中 modify by hzg*/
	 //@Column(name="CREDITHOUR", nullable=true, precision=3, scale=1)
     //private Double creditHour;//所需学分
	 
	 //@Column(name="TEACHINGTIMETYPE")
     //private String teachingTimeType;//学时类型,从基础数据取
	 
	 //@Column(name="STYDYHOUR",nullable=true)
     //private Long stydyHour;//学时
	 
	 @Column(name="CHSINTRODUCTION")
     private String chsIntroduction;//中文简介
	 
	 @Column(name="ENINTRODUCTION")
     private String enIntroduction;//英文简介
	 
	 @Column(name="STATUS")
     private Long status;//状态，0-未提交;1-启用;2-停用
	 
	 @Column(name="ISUNITEEXAM")
     private String isUniteExam = Constants.BOOLEAN_NO;//是否统考课程
	 
	 @Column(name="ISDEGREEUNITEXAM")
	 private String isDegreeUnitExam = Constants.BOOLEAN_NO;//是否学位统考课程
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="STOPTIME")
     private Date stopTime;//停用时间
	 
	 @Column(name="EXAMTYPE")
     private Long examType = 0L;//课程考试形式
	 
	 @Column(name="EXAMCODE")
     private String examCode;//课程考试编码
	 
	 @Column(name="MEMO")
     private String memo;///备注
	 
	 /*
	  * 教材和课程改为单向n:1
	 @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	 @org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	 @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	 @OrderBy("showOrder ASC")	
	 @Where(clause="isDeleted=0")
	 private Set<CourseBook> courseBook = new HashSet<CourseBook>(0);//课程-课程教材 n:1
	 */
	 	 
	 /** 3.0.3 新增字段 3.0.9 调整到教学计划中*/
//	 @Column(name="DEFAULTTEACHERID")
//	 private String defaultTeacherId;//默认老师ID
//	 
//	 @Column(name="DEFAULTTEACHERNAME")
//	 private String defaultTeacherName;//默认老师姓名
	 
	 @Column(name="ISPRACTICE")
	 private String isPractice = Constants.BOOLEAN_NO;//是否实践课
	 
	 /**3.0.8 新增辅导老师字段 3.0.9 调整到教学计划中*/	
//	 @Column(name="ASSISTANTNAMES")
//	private String assistantNames;//辅导老师姓名
//		 
//	@Column(name="ASSISTANTIDS")
//	private String assistantIds;//辅导老师ID
	
	/**3.0.9 调整到教学计划中*/
//	private String teachType;//教学方式 网络学习/面授	
	
	/**3.1.6 新增计划外学时学分*/
	@Column(name="PLANOUTSTUDYHOUR")
	private Long planoutStudyHour;
	
	@Column(name="PLANOUTCREDITHOUR")
	private Double planoutCreditHour;
	 
	/**3.1.8 新增是否有课件资源*/
	@Column(name="HASRESOURCE",length=1)
	private String hasResource = Constants.BOOLEAN_NO;
	
	/**3.1.8 新增当为统考课程，导出统考成绩单模板时的排序号*/
	@Column(name="UNITEEXAMTEMPLATESORDER")
	private Long uniteExamTemplatesOrder;
	
	
	/**3.2.11 新增是否精品课程，用来支持精品课程资源*/
	@Column(name="ISQUALITYRESOURCE",length=1)
	private String isQualityResource = Constants.BOOLEAN_NO;//是否精品课程
	
	@Column(name="COVER")
	private String cover;// 增加课程封面
	
	@Column(name="courseType")
	private String courseType;//#CodeCourseType 课程类型  11-必修课，22-选修课，33-限选课，44-任选课，55-通识课，66-实践环节，thesis-毕业论文
	
	@Column(name="topicNum")
	private Integer topicNum;//有效帖总数，默认值为1，学生登录在线平台时显示随堂问答进度=有效帖/有效帖总数
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	@OrderBy("courseType, ASC")
	@Where(clause = "isDeleted=0")
	private Set<TextBook> textBooks = new  LinkedHashSet<TextBook>(0);//教材  1：N
	
	public Set<TextBook> getTextBooks() {
		return textBooks;
	}
	public void setTextBooks(Set<TextBook> textBooks) {
		this.textBooks = textBooks;
	}
	/**
	 * @return the isQualityResource
	 */
	public String getIsQualityResource() {
		return isQualityResource;
	}
	/**
	 * @param isQualityResource the isQualityResource to set
	 */
	public void setIsQualityResource(String isQualityResource) {
		this.isQualityResource = isQualityResource;
	}

	/**增加导出时的序号*/
	@Transient
	private Long showOrderNo;
	public Long getShowOrderNo(){
		return showOrderNo;
	}
	public void setShowOrderNo(Long showOrderNo){
		this.showOrderNo = showOrderNo;
	}
	
	public Long getUniteExamTemplatesOrder() {
		return uniteExamTemplatesOrder;
	}
	public void setUniteExamTemplatesOrder(Long uniteExamTemplatesOrder) {
		this.uniteExamTemplatesOrder = uniteExamTemplatesOrder;
	}
	/**
	 * @return the hasResource
	 */
	public String getHasResource() {
		return hasResource;
	}

	/**
	 * @param hasResource the hasResource to set
	 */
	public void setHasResource(String hasResource) {
		this.hasResource = hasResource;
	}

	/**
	 * @return the planoutStudyHour
	 */
	public Long getPlanoutStudyHour() {
		return planoutStudyHour;
	}

	/**
	 * @param planoutStudyHour the planoutStudyHour to set
	 */
	public void setPlanoutStudyHour(Long planoutStudyHour) {
		this.planoutStudyHour = planoutStudyHour;
	}
	
	public Double getPlanoutCreditHour() {
		return planoutCreditHour;
	}
	public void setPlanoutCreditHour(Double planoutCreditHour) {
		this.planoutCreditHour = planoutCreditHour;
	}
	public String getIsPractice() {
		return isPractice;
	}

	public void setIsPractice(String isPractice) {
		this.isPractice = isPractice;
	}

//	public Set<CourseBook> getCourseBook() {
//		return courseBook;
//	}
//	
//	public void addCourseBook(CourseBook courseBook){
//		getCourseBook().add(courseBook);
//	}
//	
//	@SuppressWarnings("unused")
//	public void setCourseBook(Set<CourseBook> courseBook) {
//		this.courseBook = courseBook;
//	}

	public String getChsIntroduction() {
		return chsIntroduction;
	}

	public void setChsIntroduction(String chsIntroduction) {
		this.chsIntroduction = chsIntroduction;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = ExStringUtils.trimToNull(courseCode);
	}

	public String getCourseEnName() {
		return courseEnName;
	}

	public void setCourseEnName(String courseEnName) {
		this.courseEnName = ExStringUtils.trimToNull(courseEnName);
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = ExStringUtils.trimToNull(courseName);
	}

	public String getCourseShortName() {
		return courseShortName;
	}

	public void setCourseShortName(String courseShortName) {
		this.courseShortName = ExStringUtils.trimToNull(courseShortName);
	}

	public String getEnIntroduction() {
		return enIntroduction;
	}

	public void setEnIntroduction(String enIntroduction) {
		this.enIntroduction = enIntroduction;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public Long getExamType() {
		return examType;
	}

	public void setExamType(Long examType) {
		this.examType = examType;
	}

	public String getIsUniteExam() {
		return isUniteExam;
	}

	public void setIsUniteExam(String isUniteExam) {
		this.isUniteExam = isUniteExam;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}


	public String getIsDegreeUnitExam() {
		return isDegreeUnitExam;
	}

	public void setIsDegreeUnitExam(String isDegreeUnitExam) {
		this.isDegreeUnitExam = isDegreeUnitExam;
	}
	
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	@Override
	public String toString() {		
		return getCourseName();
	}
	
	//是否该课程的老师（包括负责老师或辅导老师）
//	public boolean isCourseTeacher(String userId){
//		if(this.getDefaultTeacherId()!=null&&this.getDefaultTeacherId().equals(userId))
//			return true;
//		if(this.getAssistantIds()!=null&&this.getAssistantIds().contains(userId))
//			return true;
//		return false;
//	}
	
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public Integer getTopicNum() {
		return topicNum;
	}
	public void setTopicNum(Integer topicNum) {
		this.topicNum = topicNum;
	}
	
}