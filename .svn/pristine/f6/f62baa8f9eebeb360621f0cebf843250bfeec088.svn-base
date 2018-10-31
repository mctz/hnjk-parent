package com.hnjk.platform.system.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 图片轮播
 */
@Entity
@Table(name="EDU_SYS_PICTURECAROUSEL")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class PictureCarousel extends BaseModel {

	private static final long serialVersionUID = 6378559551644217048L;

	@Column(name="PICNAME")
	private String picName;// 图片名称
	
	@Column(name="PICURL")
	private String picUrl;// 图片地址
	
	@Column(name="ISSHOW")
	private String isShow;// 是否显示
	
	@Column(name="SHOWORDER")
	private Integer showOrder;// 显示顺序
	
	@Column(name="MEMO")
	private String memo;// 描述
	
	@Column(name="OPERATORID")
	private String operatprId;// 操作人
	
	@Column(name="CREATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 修改时间

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperatprId() {
		return operatprId;
	}

	public void setOperatprId(String operatprId) {
		this.operatprId = operatprId;
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
