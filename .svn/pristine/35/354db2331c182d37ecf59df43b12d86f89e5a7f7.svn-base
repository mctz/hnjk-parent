/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi.hibernate3;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.opensymphony.workflow.spi.Step;


/**
 * 流程步骤父类.可以扩展<p>
 * @see HibernateCurrentStep
 * @author hzg
 *
 */
@MappedSuperclass
public  class HibernateStep implements Step {
   
	private static final long serialVersionUID = -5436101457597551275L;
	
	@Id  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_steps")	
	private long id = -1;//ID
	
	@Column(name="DUE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;//过期时间
	
	@Column(name="FINISH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;//完成时间
    
	@Column(name="START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    private Date startDate;//开始时间
        
	@Column(name="CALLER")
    private String caller;//调用者
    
	@Column(name="OWNER")
    private String owner;//所有者
    
	@Column(name="STATUS")
    private String status;//状态
    
	@Column(name="ACTION_ID")
    private int actionId;//动作ID
    
	@Column(name="STEP_ID")
    private int stepId;//步骤ID
    
	@Column(name="OPINION")
    private String opinion;	// 处理意见
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTRY_ID")
    private HibernateWorkflowEntry entry;//流程实例
    
	@Transient
    private List<HibernateStep> previousSteps;//上下步骤集合
    
	@Transient
    private long[] previousStepIds;//上下步骤ID集合


	
    public HibernateStep() {
    }

    public HibernateStep(HibernateStep step) {
        this.actionId = step.getActionId();
        this.caller = step.getCaller();
        this.finishDate = step.getFinishDate();
        this.dueDate = step.getDueDate();
        this.startDate = step.getStartDate();
        //do not copy this value, it's for unsaved-value
        //this.id = step.getId();
        this.owner = step.getOwner();
        this.status = step.getStatus();
        this.stepId = step.getStepId();
        this.opinion = step.getOpinion();
        this.previousSteps = step.getPreviousSteps();
        this.entry = step.entry;   
    }


	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
    
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @Override
	public int getActionId() {
        return actionId;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    @Override
	public String getCaller() {
        return caller;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
	public Date getDueDate() {
        return dueDate;
    }

    public void setEntry(HibernateWorkflowEntry entry) {
        this.entry = entry;
    }

    public HibernateWorkflowEntry getEntry() {
        return entry;
    }

    @Override
	public long getEntryId() {
        return entry.getId();
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Override
	public Date getFinishDate() {
        return finishDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
	public long getId() {
        return id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
	public String getOwner() {
        return owner;
    }

    @Override
	public long[] getPreviousStepIds() {
        return previousStepIds;
    }

    public void setPreviousStepIds(long[] previousStepIds) {
        this.previousStepIds = previousStepIds;
    }

    public void setPreviousSteps(List<HibernateStep> previousSteps) {
        this.previousSteps = previousSteps;
    }

    public List<HibernateStep> getPreviousSteps() {
        return previousSteps;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
	public Date getStartDate() {
        return startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
	public String getStatus() {
        return status;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    @Override
	public int getStepId() {
        return stepId;
    }
}
