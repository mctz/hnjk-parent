package com.hnjk.edu.roll.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;

/**
 * 班级表
 * @author hzg139
 *
 */
@Entity
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Table(name = "EDU_ROLL_CLASSES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Classes extends BaseModel{
	
	@Column(name="CLASSESNAME")
	private String classname ;//班级名称
	
	@OneToOne(optional =true,cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name="ORGUNITID")
	private OrgUnit brSchool;//关联学习中心
	
	@OneToOne(optional =true,cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name="GRADEID")
	private Grade grade;//关联年级
	
	@OneToOne(optional =true,cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name="CLASSICID")
	private Classic classic;//关联层次
	
	@OneToOne(optional =true,cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name="MAJORID")
	private Major major;//关联专业
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="TEACHINGTYPE")
	private String teachingType;//教学形式
	
	@Column(name="CLASSESMASTER")
	private String classesMaster;//班主任--教师/用户名
	
	@Column(name="CLASSESMASTERID")
	private String classesMasterId;//班主任--教师-用户id

	@Column(name="CLASSESLEADER")
	private String classesLeader;//班长--学生姓名

	@Column(name="CLASSESLEADERID")
	private String classesLeaderId;//班长--学籍id

	@Column(name="MASTERPHONE")
	private String classesMasterPhone;//班主任电话
	
//	@Formula("(select count(*) from edu_roll_studentinfo s where s.isdeleted=0 and s.classesid=resourceid and s.studentStatus = '11' )")
	@Transient
	private Integer studentNum;//班级学生人数
	
//	@Formula("(select count(*) from edu_roll_studentinfo s where s.isdeleted=0 and s.classesid=resourceid and s.studentStatus = '11' )")
//	private Integer allstudentNum;//全部状态下-班级学生人数
	
	@Column(name="classCode")
	private String classCode;//班号
	
	@Transient
	private String brSchoolid;
	@Transient
	private String gradeid;
	@Transient
	private String majorid;
	@Transient
	private String classicid;

	public Classes() {	}

	public Classes(String classname,OrgUnit brSchool, Grade grade, Major major, Classic classic,	String teachingType) {
		this.classname = classname;
		this.brSchool = brSchool;
		this.grade = grade;
		this.major = major;
		this.classic = classic;
		this.teachingType = teachingType;
	}
	
	@Override
	public String toString() {
		return classname;
	}

}
