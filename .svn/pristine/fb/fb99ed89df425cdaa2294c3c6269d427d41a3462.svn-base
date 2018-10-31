package com.hnjk.platform.system.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.security.model.User;

/**
 * 系统版本. <p>
 * 
 * @author：Zik, 广东学苑教育发展有限公司.
 * @since： 2016-3-6 上午11:36:49
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_SYS_VERSION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Version extends BaseModel {

	private static final long serialVersionUID = 4677383348950251237L;
	
	@Column(name = "VESIONNO")
	private Integer versionNo;// 版本号
	
	@Column(name = "VERSIONNAME")
	private String versionName;//版本号名称
	
	@Column(name = "MEMO")
	private String memo;// 版本说明
	
	@Column(name = "PREAPPURL")
	private String preAppUrl;// 上个版本URL
	
	@Column(name = "APPURL")
	private String appUrl;// 本版本URL
	
	@Column(name = "BACKENDURL")
	private String backendUrl;// 后端系统URL
	
	@Column(name = "ISFORCEDUPDATE")
	private String isForcedUpdate = Constants.BOOLEAN_NO;// 是否强制更新
	
	@Column(name="ISPUBLISH")
	private String isPublish = Constants.BOOLEAN_YES;// 是否发布
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATORID")
	private User operator;// 操作人
	
	@Column(name = "OPERATORNAME")
	private String operatorName; // 操作人名称
	
	@Column(name = "CREATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	
	@Column(name = "UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 更新时间
	
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPreAppUrl() {
		return preAppUrl;
	}
	public void setPreAppUrl(String preAppUrl) {
		this.preAppUrl = preAppUrl;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getBackendUrl() {
		return backendUrl;
	}
	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}
	public String getIsForcedUpdate() {
		return isForcedUpdate;
	}
	public void setIsForcedUpdate(String isForcedUpdate) {
		this.isForcedUpdate = isForcedUpdate;
	}
	public String getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
