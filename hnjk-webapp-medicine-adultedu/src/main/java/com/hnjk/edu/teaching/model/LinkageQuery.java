package com.hnjk.edu.teaching.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;
/**
 * 联动查询表
 * 
 * @author zik, 广东学苑教育发展有限公司
 *
 */
@Entity
@Table(name="EDU_TEACH_LINKAGEQUERY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LinkageQuery extends BaseModel {

	private static final long serialVersionUID = 99674891618940868L;
	public static final String OPERATE_BEGIN = "begin";
	public static final String OPERATE_UNIT = "unit";
	public static final String OPERATE_GRADE = "grade";
	public static final String OPERATE_CLASSIC = "classic";
	public static final String OPERATE_TEACHINGTYPE = "teachingType";
	public static final String OPERATE_MAJOR = "major";
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOOLID")     
	private OrgUnit unit;// 教学点
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")     
	private Grade grade;// 年级
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")     
	private Classic classic;// 层次
	
	@Column(name="TEACHINGTYPE")
	private String teachingType;// 学习形式
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")     
	private Major major;// 专业
	
	public OrgUnit getUnit() {
		return unit;
	}

	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Classic getClassic() {
		return classic;
	}

	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	public String getTeachingType() {
		return teachingType;
	}

	public void setTeachingType(String teachingType) {
		this.teachingType = teachingType;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

}
