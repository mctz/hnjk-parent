package com.hnjk.edu.basedata.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;

/**
 * <code>BaseMajor</code>基础数据-专业信息表
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午11:05:01
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_MAJOR")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Major extends BaseModel implements Serializable{

	@Column(name = "MAJORCODE", unique = true)
	private String majorCode;// 专业编码

	@Column(name = "MAJORNAME", nullable = false, unique = true)
	private String majorName;// 专业名称

	@Column(name = "MAJORENNAME")
	private String majorEnName;// 专业英文名称


	@Column(name = "MAJORNATIONCODE")
	private String majorNationCode;// 专业国家编码
	
	@Column(name = "MAJORSCHOOLCODE")
	private String majorSchoolCode;// 专业学校编码

	@Column(name = "MAJORINTRODUCE")
	private String majorIntroduce;// 专业介绍

	@Column(name = "MAJORCLASS")
	private String majorClass; // 专业类别，字典表CodeMajorClass
	
	@Column(name = "PICTURE4COURSE")
	private String picture4Course; // 教学计划课程图片
	
	@Column(name = "PICTURE4TEXTBOOK")
	private String picture4TextBook; // 教材目录图片

	@Column(name = "ISADULT", length = 1)
	private String isAdult = Constants.BOOLEAN_NO;// 是否属于网络成人直属班,Y-是,N-否

	/**3.1.4 新增国家专业*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "NATIONMAJARID")	
	private NationMajor nationMajor;// 国家专业
	
	@Column(name = "MAJORCODE4STUDYNO")
	private String majorCode4StudyNo; // 广东医 ：专业编码，只用于编学号
	
	@Column(name="classicCode")
	private String classicCode;//层次代码
	
	@Column(name="isForeignLng")
	private String isForeignLng;// 是否是外语类

	@Transient
	private String classicName;//层次名称
		
	// 专业与选修课关联 n:n
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "edu_base_majorcourse", joinColumns = { @JoinColumn(name = "majorid") }, inverseJoinColumns = { @JoinColumn(name = "courseid") })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause="isDeleted=0")
	@DeleteChild(deleteable=false)
	private Set<Course> courses = new LinkedHashSet<Course>(0);
	
	@Transient
	private String majorCodeName;

	@Override
	public String toString() {
		return getMajorName();
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	public String getMajorCodeName() {
		return getMajorCode()+"-"+getMajorName();
	}
	
	public String getIsForeignLng(){
		if(ExStringUtils.isBlank(this.isForeignLng)){
			this.isForeignLng = "N";
		}
		
		return this.isForeignLng;
	}
	
}