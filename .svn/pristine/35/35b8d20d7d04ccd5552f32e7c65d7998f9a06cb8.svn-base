package com.hnjk.edu.teaching.model;

// default package

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;

/**
 * 教学任务书明细
 * <code>TeachTaskDetails</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午02:46:19
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_TEACHTASKDETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachTaskDetails  extends BaseModel implements Serializable{
     
	private static final long serialVersionUID = 3981714791335679723L;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="STARTTIME")
	private Date startTime;//开始时间
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENDTIME")
    private Date endTime;//结束时间
     
	@Column(name="TASKTYPE")
    private String taskType;//#教学活动,由教学管理人员制定 取自字典 学习指导,学习材料,随堂问答,作业布置与批改,网络辅导,考试辅导,期末评卷 
     
	@Column(name="TASKCONTENT")
    private String taskContent;//内容
    
	@Column(name="OWNERID")
    private String ownerId;//所有者
    
	@Column(name="OWNERNAME")
    private String ownerName;//所有者姓名
    
	@Column(name="PARTICIPANTSID")
    private String participantsId;//参与者ID
	
	@Column(name="PARTICIPANTSNAME")
    private String participantsName;//参与者
	
	@Column(name="STATUS")
    private String status;//状态	 
	
	@Column(name="SHOWORDER") 
    private Long showOrder;//排序号
     
	
	//TODO:table does not exist
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEACHTASKID")
    private TeachTask teachTask;//所属教师教学任务书
   
    @Column(name="EVALUATE")
    private Integer evaluate;//评价 -1 - 不满意,1-满意，2-非常满意
    
    /**3.1.9 新增是否修改、预警时间*/
    @Column(name="ISALLOWMODIFY")
    private String isAllowModify = Constants.BOOLEAN_YES;//是否允许修改
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="WARNINGTIME")
    private Date warningTime;//预警时间
    
    @Column(name="ISCUSTOMTASK")
    private String isCustomTask = Constants.BOOLEAN_NO;//是否为自定义任务，自定义任务是指，在任务书外，教师自己添加的任务，可以自行删除
    
    
        
	/**
	 * @return the isAllowModify
	 */
	public String getIsAllowModify() {
		return isAllowModify;
	}

	/**
	 * @param isAllowModify the isAllowModify to set
	 */
	public void setIsAllowModify(String isAllowModify) {
		this.isAllowModify = isAllowModify;
	}

	/**
	 * @return the warningTime
	 */
	public Date getWarningTime() {
		return warningTime;
	}

	/**
	 * @param warningTime the warningTime to set
	 */
	public void setWarningTime(Date warningTime) {
		this.warningTime = warningTime;
	}

	/**
	 * @return the isCustomTask
	 */
	public String getIsCustomTask() {
		return isCustomTask;
	}

	/**
	 * @param isCustomTask the isCustomTask to set
	 */
	public void setIsCustomTask(String isCustomTask) {
		this.isCustomTask = isCustomTask;
	}

	public Integer getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}

//	public String getTaskAdvise() {
//		return taskAdvise;
//	}
//
//	public void setTaskAdvise(String taskAdvise) {
//		this.taskAdvise = taskAdvise;
//	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getParticipantsId() {
		return participantsId;
	}

	public void setParticipantsId(String participantsId) {
		this.participantsId = participantsId;
	}

	public String getParticipantsName() {
		return participantsName;
	}

	public void setParticipantsName(String participantsName) {
		this.participantsName = participantsName;
	}

	public Long getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public TeachTask getTeachTask() {
		return teachTask;
	}

	public void setTeachTask(TeachTask teachTask) {
		this.teachTask = teachTask;
	}


    

}