package com.hnjk.platform.system.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.base.model.BaseModel;

/**
 * 审计日志
 * <code>HistoryModel</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-11-17 下午04:22:06
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "HNJK_SYS_HISTORY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HistoryModel extends BaseModel{

	private static final long serialVersionUID = 5118378904699162815L;

	@Column(name="ENTIRYID")
	private String entiryId;//审计实体(model)resourceid
	
	@Column(name="ENTIRYNAME")
	private String entiryName;//审计实体(model)
	
	@Column(name="HISTORYTYPE")
	@Enumerated(EnumType.STRING)
	private OperationType operatorType;//操作类型
	
	@Column(name="BEFOREVALUE")
	private String beforeValue;//操作前数据，json field:value
	
	@Column(name="AFTERVALUE")
	private String afterValue;//操作后数据, json field:value
	
	@Column(name="OPERATORMAN")
	private String operatorMan;//操作人
	
	@Column(name="OPERATORMANID")
	private String operatorManId;//操作人ID
	
	@Column(name="OPERATORTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date operatorTime = new Date();//操作时间
	
	@Column(name="OPERATORIP")
	private String operatorIp;//操作人IP

	@Column(name="REQUESTURL")
	private String requestUrl;//请求路径
	/**
	 * @return the entiryId
	 */
	public String getEntiryId() {
		return entiryId;
	}

	/**
	 * @param entiryId the entiryId to set
	 */
	public void setEntiryId(String entiryId) {
		this.entiryId = entiryId;
	}

	/**
	 * @return the entiryName
	 */
	public String getEntiryName() {
		return entiryName;
	}

	/**
	 * @param entiryName the entiryName to set
	 */
	public void setEntiryName(String entiryName) {
		this.entiryName = entiryName;
	}
	
	
	
	/**
	 * @return the operatorType
	 */
	public OperationType getOperatorType() {
		return operatorType;
	}

	/**
	 * @param operatorType the operatorType to set
	 */
	public void setOperatorType(OperationType operatorType) {
		this.operatorType = operatorType;
	}

	/**
	 * @return the beforeValue
	 */
	public String getBeforeValue() {
		return beforeValue;
	}

	/**
	 * @param beforeValue the beforeValue to set
	 */
	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}

	/**
	 * @return the afterValue
	 */
	public String getAfterValue() {
		return afterValue;
	}

	/**
	 * @param afterValue the afterValue to set
	 */
	public void setAfterValue(String afterValue) {
		this.afterValue = afterValue;
	}

	/**
	 * @return the operatorMan
	 */
	public String getOperatorMan() {
		return operatorMan;
	}

	/**
	 * @param operatorMan the operatorMan to set
	 */
	public void setOperatorMan(String operatorMan) {
		this.operatorMan = operatorMan;
	}

	/**
	 * @return the operatorManId
	 */
	public String getOperatorManId() {
		return operatorManId;
	}

	/**
	 * @param operatorManId the operatorManId to set
	 */
	public void setOperatorManId(String operatorManId) {
		this.operatorManId = operatorManId;
	}

	/**
	 * @return the operatorTime
	 */
	public Date getOperatorTime() {
		return operatorTime;
	}

	/**
	 * @param operatorTime the operatorTime to set
	 */
	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}

	/**
	 * @return the operatorIp
	 */
	public String getOperatorIp() {
		return operatorIp;
	}

	/**
	 * @param operatorIp the operatorIp to set
	 */
	public void setOperatorIp(String operatorIp) {
		this.operatorIp = operatorIp;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	
}
