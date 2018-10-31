package com.hnjk.edu.recruit.model;

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
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;

/**
 * 招生批次-招生专业设置表。
 * <code>RecruitMajorSetting</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-4-7 下午02:41:34
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_MAJORSET")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitMajorSetting extends BaseModel{

	private static final long serialVersionUID = -8577470621677656696L;
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "MAJORID") 
	private Major major;//专业
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "CLASSICID") 
	private Classic classic;//层次
	
	@Column(name="TEACHINGTYPE")
	private String teachingType;//办学模式 取字典
	
	/**
	 * @return the major
	 */
	public Major getMajor() {
		return major;
	}

	/**
	 * @param major the major to set
	 */
	public void setMajor(Major major) {
		this.major = major;
	}

	/**
	 * @return the classic
	 */
	public Classic getClassic() {
		return classic;
	}

	/**
	 * @param classic the classic to set
	 */
	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	/**
	 * @return the teachingType
	 */
	public String getTeachingType() {
		return teachingType;
	}

	/**
	 * @param teachingType the teachingType to set
	 */
	public void setTeachingType(String teachingType) {
		this.teachingType = teachingType;
	}
	
	

}
