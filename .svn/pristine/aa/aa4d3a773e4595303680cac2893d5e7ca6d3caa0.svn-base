package com.hnjk.edu.teaching.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 毕业指导老师分配的学生明细表
 * <code>GraduateMentorDetails</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午04:41:50
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_MENTORDETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduateMentorDetails extends BaseModel{

	private static final long serialVersionUID = -2806132554762572824L;

	@OneToOne(optional = true)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;// 学生学籍
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENTORID")
	private GraduateMentor graduateMentor;//指导老师表

	public GraduateMentor getGraduateMentor() {
		return graduateMentor;
	}

	public void setGraduateMentor(GraduateMentor graduateMentor) {
		this.graduateMentor = graduateMentor;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}
	
}
