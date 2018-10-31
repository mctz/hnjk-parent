package com.hnjk.edu.learning.model;

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
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.platform.system.model.Attachs;

/**
 * 课程通知表
 * <code>CourseNotice</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-24 上午09:32:02
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_COURSENOTICE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseNotice extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程
	
	
	@Column(name="NOTICETITLE",nullable=false)
	private String noticeTitle;//通知标题
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="NOTICECONTENT",nullable=false)
	private String noticeContent;//通知内容
	
	@Column(name="FILLINMAN")
	private String fillinMan;//填写人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//填写人ID
	
	@Column(name="FILLINDATE")
	private Date fillinDate;//填写日期

	/**3.1.4 新增年度、学期*/
	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private String term;//学期 字典值
	
	@ManyToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSESID")
	private Classes classes;// 班级
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();//附件
	
	@Transient
	private boolean isPersisted = Boolean.FALSE;

	public boolean getIsPersisted() {
		return this.isPersisted;
	}

	public void setIsPersisted(boolean isPersisted) {
		this.isPersisted = isPersisted;
	}
}
