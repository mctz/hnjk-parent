package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.security.model.User;

@Entity
@Table(name="EDU_LEAR_STUDENTSYLLABUS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentSyllabus extends BaseModel{
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="STUDENTID")
	private StudentInfo stuInfo;
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="SYLLABUSID")
	private Syllabus syllabus;
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name="UPDATEMANID")
	private User updateMan;
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@Column(name = "EXERCISESUM")
	private int exercisesum;
	
	@Column(name = "REDOTIMES")
	private int redotimes = 0;
	
	@Column(name = "MEMO")
	private String memo ;
	
}
