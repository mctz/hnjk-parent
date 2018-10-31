package com.hnjk.edu.teaching.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.platform.system.model.Attachs;

/**
 * 毕业导师公告表. <code>GraduPapersNotice</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午02:28:55
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_PAPERSNOTICE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduatePapersNotice extends BaseModel {

	private static final long serialVersionUID = -8750898731407126811L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMSUBID" ,unique = true)
	private ExamSub examSub;// 毕业论文批次表

	@Column(name = "GUIDTEACHERNAME")
	private String guidTeacherName;// 导师姓名

	@Column(name = "GUIDTEACHERID")
	private String guidTeacherId;// 导师ID

	@Column(name = "TITLE", nullable = false)
	private String title;// 标题

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", nullable = false)
	private String content;// 内容

	@Column(name = "PUBTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pubTime = new Date();// 发布时间

	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();// 附件
	
	
	public GraduatePapersNotice() {
	}
	
	public GraduatePapersNotice(String guidTeacherName, String guidTeacherId) {
		super();
		this.guidTeacherName = guidTeacherName;
		this.guidTeacherId = guidTeacherId;
	}



	public List<Attachs> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getGuidTeacherName() {
		return guidTeacherName;
	}

	public void setGuidTeacherName(String guidTeacherName) {
		this.guidTeacherName = guidTeacherName;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
