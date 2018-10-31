/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi.hibernate3;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.opensymphony.workflow.spi.WorkflowEntry;


/**
 * 流程实例表.
 * @author hzg
 *
 */
@Entity
@Table(name = "OS_WFENTRY")
@SequenceGenerator(name="seq_wfentry",sequenceName="seq_os_wfentry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateWorkflowEntry implements WorkflowEntry {

	private static final long serialVersionUID = -7148423381396314366L;
	
	@Id  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_wfentry")	
	private long id = -1;//流程实例ID
	
	@Column(name="NAME",nullable=false)
    private String workflowName;  //流程实例名称
   
	@Column(name="STATE")
    private Integer state;//流程实例状态
    
    @Version
    private Long version;//流程实例版本

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entry")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("startDate ASC")	
	private List<HibernateCurrentStep> currentSteps = new ArrayList<HibernateCurrentStep>();//对应当前步骤：1：n
	
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entry")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("startDate ASC")	
    private List<HibernateHistoryStep> historySteps = new ArrayList<HibernateHistoryStep>();//对应历史步骤:1:n

    /**3.0.3新增所属用户ID，所属单位ID*/
    @Column(name="USERID")
    private String userId;
    
    @Column(name="UNITID")
    private String  unitId;
    
    
    /**
	 * @return the userId
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the unitId
	 */
	@Override
	public String getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public void setCurrentSteps(List<HibernateCurrentStep> currentSteps) {
        this.currentSteps = currentSteps;
    }

    public List<HibernateCurrentStep> getCurrentSteps() {
        return currentSteps;
    }

    public void setHistorySteps(List<HibernateHistoryStep> historySteps) {
        this.historySteps = historySteps;
    }

    public List<HibernateHistoryStep> getHistorySteps() {
        return historySteps;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
	public long getId() {
        return id;
    }

    @Override
	public boolean isInitialized() {
        return state > 0;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
	public int getState() {
        return state;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    @Override
	public String getWorkflowName() {
        return workflowName;
    }

    public void removeCurrentSteps(HibernateCurrentStep step) {
        getCurrentSteps().remove(step);
    }

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

    
}
