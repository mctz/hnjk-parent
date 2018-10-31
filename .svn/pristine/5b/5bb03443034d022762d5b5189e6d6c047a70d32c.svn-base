package com.hnjk.edu.arrange.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.security.model.OrgUnit;

/**
 *教学班
 */
@Entity
@Table(name="EDU_ARRANGE_TEACHCOURSE")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachCourse extends SuperBaseModel{

	private static final long serialVersionUID = 1L;

	@Column(name="teachingCode")
	private String teachingCode;//	教学班编码	
	
	@Column(name="teachingClassname")
	private String teachingClassname;//	教学班名	
	
	@JoinColumn(name="unitid",nullable=false)
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private OrgUnit unit;//	教学点
	
	@JoinColumn(name="yearid",nullable=false)
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private YearInfo yearInfo;//	年度
	
	@Column(name="term")
	private String term;//	学期	(？)
	
	@Column(name="openTerm")
	private String openTerm;//	开课学期	
	
	@Column(name="studentNumbers")
	private Integer studentNumbers;//	学生人数	
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name="classicid",nullable=false)
	private Classic classic;//	层次	
	
	@Column(name="teachingtype")
	private String teachingtype;//	学习形式	
	
	@Column(name="teachtype")
	private String teachtype;//	教学类型	
	
	@JoinColumn(name="courseid")
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private Course course;//	课程
	
	@Column(name="courseName")
	private String courseName;//	课程名字	
	
	@Column(name="studyHour")
	private Integer studyHour;//	学时	
	
	@Column(name="examClassType")
	private String examClassType;//	考试类别	数据字典：CodeCourseExamType
	
	@Column(name="willTeacherName")
	private String willTeacherName;//	意愿老师	
	
	@Column(name="teacherNames")
	private String teacherNames;//	主讲老师名	
	
	@Column(name="status")
	private Integer status;
	/*CodeTeachClassesStatus
	教学班合班状态：0、一个教学班对应一个行政班；
	1、一个教学班对应多个行政班（合班）；
	2、多个教学班对应一个行政班（拆班）；*/
	@Column(name="generateStatus")
	private Integer generateStatus;//	生成状态 CodeGenerateStatus：0、未生成；1：已生成
	
	@Column(name="publishStatus")
	private Integer publishStatus;//	发布状态 CodePublishStatus：0、未发布；1：已发布；2：未生成
	
	@Column(name="selectedStatus")
	private Integer selectedStatus;//	选课状态	CodeSelectedStatus：0-为选课；1-已选课
	
	@Column(name="arrangeStatus")
	private Integer arrangeStatus;//	排课状态	CodeCourseArrangeStatus：0-未排课,1-已排课
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teachCourse")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	@OrderBy(clause="days")
	private Set<TeachCourseDetail> teachCourseDetails = new HashSet<TeachCourseDetail>(0);//排课详情

	@Column(name="classNames")
	private String classNames;// 班级信息
	
	@Column(name="memo")
	private String memo;
	
	@Transient
	private Integer arrangeStudyHour;//	已排课学时	
	
	@Transient
	private String recordScorerName;//登分老师
	
	public String getTeachingCode() {
		return teachingCode;
	}
	public void setTeachingCode(String teachingCode) {
		this.teachingCode = teachingCode;
	}
	public String getTeachingClassname() {
		return teachingClassname;
	}
	public void setTeachingClassname(String teachingClassname) {
		this.teachingClassname = teachingClassname;
	}
	public OrgUnit getUnit() {
		return unit;
	}
	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}
	public YearInfo getYearInfo() {
		return yearInfo;
	}
	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getOpenTerm() {
		return openTerm;
	}
	public void setOpenTerm(String openTerm) {
		this.openTerm = openTerm;
	}
	public Integer getStudentNumbers() {
		return studentNumbers;
	}
	public void setStudentNumbers(Integer studentNumbers) {
		this.studentNumbers = studentNumbers;
	}
	public Classic getClassic() {
		return classic;
	}
	public void setClassic(Classic classic) {
		this.classic = classic;
	}
	public String getTeachingtype() {
		return teachingtype;
	}
	public void setTeachingtype(String teachingtype) {
		this.teachingtype = teachingtype;
	}
	public String getTeachtype() {
		return teachtype;
	}
	public void setTeachtype(String teachtype) {
		this.teachtype = teachtype;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Integer getStudyHour() {
		return studyHour;
	}
	public void setStudyHour(Integer studyHour) {
		this.studyHour = studyHour;
	}
	public String getExamClassType() {
		return examClassType;
	}
	public void setExamClassType(String examClassType) {
		this.examClassType = examClassType;
	}
	public String getWillTeacherName() {
		return willTeacherName;
	}
	public void setWillTeacherName(String willTeacherName) {
		this.willTeacherName = willTeacherName;
	}
	public String getTeacherNames() {
		return teacherNames;
	}
	public void setTeacherNames(String teacherNames) {
		this.teacherNames = teacherNames;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getGenerateStatus() {
		return generateStatus;
	}
	public void setGenerateStatus(Integer generateStatus) {
		this.generateStatus = generateStatus;
	}
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public Integer getSelectedStatus() {
		return selectedStatus;
	}
	public void setSelectedStatus(Integer selectedStatus) {
		this.selectedStatus = selectedStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	public Integer getArrangeStatus() {
		return arrangeStatus;
	}
	public void setArrangeStatus(Integer arrangeStatus) {
		this.arrangeStatus = arrangeStatus;
	}
	public Set<TeachCourseDetail> getTeachCourseDetails() {
		return teachCourseDetails;
	}
	public void setTeachCourseDetails(Set<TeachCourseDetail> teachCourseDetails) {
		this.teachCourseDetails = teachCourseDetails;
	}
	public String getClassNames() {
		return classNames;
	}
	public void setClassNames(String classNames) {
		this.classNames = classNames;
	}
	public Integer getArrangeStudyHour() {
		return arrangeStudyHour;
	}
	public void setArrangeStudyHour(Integer arrangeStudyHour) {
		this.arrangeStudyHour = arrangeStudyHour;
	}
	public String getRecordScorerName() {
		return recordScorerName;
	}
	public void setRecordScorerName(String recordScorerName) {
		this.recordScorerName = recordScorerName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
