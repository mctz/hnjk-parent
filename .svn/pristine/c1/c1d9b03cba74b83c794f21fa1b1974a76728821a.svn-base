package com.hnjk.edu.recruit.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;

/** 
 * 招生范围
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午3:49:02 
 * 
 */
@Entity
@Table(name = "EDU_ENROLLMENTBOOK_SCOPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitmentScope  extends BaseModel{

	private static final long serialVersionUID = 127084254119644337L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID")
	private OrgUnit unit;// 教学点
	
	@Column(name="AREASCOPE")
	private String areaScope;// 招生范围，身份证号前六位，对应字典表 CodeRecruitmentScope

	@Transient
	private String unitId;
	
	public OrgUnit getUnit() {
		return unit;
	}

	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}

	public String getAreaScope() {
		return areaScope;
	}

	public void setAreaScope(String areaScope) {
		this.areaScope = areaScope;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
}
