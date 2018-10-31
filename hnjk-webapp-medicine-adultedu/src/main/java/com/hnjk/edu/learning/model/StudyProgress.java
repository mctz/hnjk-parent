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
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Jul 19, 2016 2:06:59 PM 
 * 
 */
@Entity
@Table(name="EDU_LEAR_STUDYPROGRESS")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudyProgress extends BaseModel {
	
	@Column(name="RESOURCETYPE")
	private String resourceType;// 资源类型,视频：video 目前只有视频以后，如果有其他类型，再存放在数据字典中
	
	@Column(name="LEARNEDTIME")
	private Integer learnedTime=0;// 视频：已学习时长（以秒为单位）
	
	@Column(name="TOTALTIME")
	private Integer totalTime=0;// 视频：总时长（以秒为单位）
	
	@Column(name="CREATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate=new Date();// 创建时间
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 更新时间
	
	@OneToOne(optional=true, cascade={CascadeType.MERGE,CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="STUDENTINFOID")
	private StudentInfo studentInfo;
	
	@OneToOne(optional=true, cascade ={CascadeType.MERGE,CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="COURSEID")
	private Course course;
	
	@OneToOne(optional=true, cascade ={CascadeType.MERGE,CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="MATEID")
	private MateResource mate;//资源
	
	@Column(name="CURRENTTIME")
	private Integer currentTime=0;// 视频：当前时长（以秒为单位）

}
