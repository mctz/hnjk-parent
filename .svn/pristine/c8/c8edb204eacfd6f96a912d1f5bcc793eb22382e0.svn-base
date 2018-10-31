package com.hnjk.edu.basedata.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;


/**
 *  <code>Building</code>基础数据-校外学习中心-教学楼.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午11:32:22
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BASE_BUILDING")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Building extends BaseModel {

	private static final long serialVersionUID = 5251181544120616380L;
	
	@Column(name="BUILDINGNAME", nullable=false)
	private String buildingName;//教学楼名称	
     
     @Column(name="MAXLAYERS",  nullable=false, precision=22, scale=0)
     private Long maxLayers;//最高楼层
     
     @Column(name="MAXUNITS",  nullable=false, precision=22, scale=0)
     private Long maxUnits;//每层最大单元数
     
     @Column(name="PHONE")
     private String phone;//电话
     
     @Column(name="MEMO")
     private String memo;//备注
     
     @Column(name="SHOWORDER")
     private Long showOrder = 0L;//排序号

     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "BRANCHSCHOOLID")
     @Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
     private OrgUnit branchSchool;//所属校外学习中心
     
     @Column(name="BUILDINGCODE")
     private String buildingCode;//楼号
     
    @Transient
    private String branchSchoolId;
     
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "building")
 	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL})
 	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
 	@OrderBy("showOrder ASC")	
 	@Where(clause="isDeleted=0")
    private Set<Classroom> classroom = new HashSet<Classroom>(0);//所属科室 n:1     
     
     
	public Set<Classroom> getClassroom() {
		return classroom;
	}
	
	public void addClassroom(Classroom classroom){
		getClassroom().add(classroom);
	}
	
	@SuppressWarnings("unused")
	public void setClassroom(Set<Classroom> classroom) {
		this.classroom = classroom;
	}


	public OrgUnit getBranchSchool() {
		return branchSchool;
	}

	public void setBranchSchool(OrgUnit branchSchool) {
		this.branchSchool = branchSchool;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Long getMaxLayers() {
		return maxLayers;
	}

	public void setMaxLayers(Long maxLayers) {
		this.maxLayers = maxLayers;
	}

	public Long getMaxUnits() {
		return maxUnits;
	}

	public void setMaxUnits(Long maxUnits) {
		this.maxUnits = maxUnits;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	@Override
	public String toString() {		
		return getBuildingName();
	}

    @Override
    public String getResourceid() {
    	return super.getResourceid();
    }

	public String getBranchSchoolId() {
		return branchSchoolId;
	}

	public void setBranchSchoolId(String branchSchoolId) {
		this.branchSchoolId = branchSchoolId;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
	
}