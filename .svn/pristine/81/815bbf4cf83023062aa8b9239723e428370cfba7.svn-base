package com.hnjk.edu.basedata.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;
/**
 * 
  * @description:班级设置
  * @author xiangy  
  * @date 2013-12-4 上午09:50:02 
  * @version V1.0
 */
@Entity
@Table(name="EDU_BASE_CLASS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassInfo extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="CLASSNAME",nullable=false,unique=true)
	private String className;    //班级名称
	
	@Column(name="STATUS",nullable=false)
	private String status;     //状态       1.启用，2.停用
	
	@Column(name="MEMO")
	private String memo;     //备注
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRANCHSCHOOLID")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private OrgUnit branchSchool;//所属校外学习中心

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public OrgUnit getBranchSchool() {
		return branchSchool;
	}

	public void setBranchSchool(OrgUnit branchSchool) {
		this.branchSchool = branchSchool;
	}
	
	
	

}
