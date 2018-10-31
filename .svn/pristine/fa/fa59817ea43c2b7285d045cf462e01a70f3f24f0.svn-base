package com.hnjk.edu.teaching.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Edumanager;

/**
 * 毕业论文批次与指导老师关系维护表
 * <code>GraduateMentor</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午02:30:00
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_GRADUATEMENTOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduateMentor extends BaseModel {

	private static final long serialVersionUID = 6610329636634442924L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GUIDTEACHERID")
	private Edumanager edumanager;// 老师

	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMSUBID")
	private ExamSub examSub;// 毕业批次

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "graduateMentor")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy(value = "resourceid")
	@Where(clause = "isDeleted=0")
	private Set<GraduateMentorDetails> graduateMentorDetails = new LinkedHashSet<GraduateMentorDetails>();// 指导老师所分配的学生

	@Transient
	private String examSubId;
	
	@Transient
	private String guidTeacherId;

	@Transient
	private String studentDetail;

	public Set<GraduateMentorDetails> getGraduateMentorDetails() {
		return graduateMentorDetails;
	}

	public void setGraduateMentorDetails(
			Set<GraduateMentorDetails> graduateMentorDetails) {
		this.graduateMentorDetails = graduateMentorDetails;
	}

	public Edumanager getEdumanager() {
		return edumanager;
	}

	public void setEdumanager(Edumanager edumanager) {
		this.edumanager = edumanager;
	}

	public ExamSub getExamSub() {
		return examSub;
	}

	public void setExamSub(ExamSub examSub) {
		this.examSub = examSub;
	}

	public String getGuidTeacherId() {
		return guidTeacherId;
	}

	public void setGuidTeacherId(String guidTeacherId) {
		this.guidTeacherId = guidTeacherId;
	}

	public String getStudentDetail() {
		StringBuffer sb = new StringBuffer();
		if (!getGraduateMentorDetails().isEmpty()) {
			for (GraduateMentorDetails detail : getGraduateMentorDetails()) {
				sb.append(detail.getStudentInfo().getStudentName());
				sb.append(",");
			}
		}
		studentDetail = sb.toString();
		return studentDetail;
	}

	public void setStudentDetail(String studentDetail) {
		this.studentDetail = studentDetail;
	}

	public String getExamSubId() {
		return examSubId;
	}

	public void setExamSubId(String examSubId) {
		this.examSubId = examSubId;
	}

}
