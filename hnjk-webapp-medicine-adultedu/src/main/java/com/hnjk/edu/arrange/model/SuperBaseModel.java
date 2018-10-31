package com.hnjk.edu.arrange.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.User;

/** 
 * 提供一个超级基础Model,
 * 其他Model可以继承BaseModel，也可以继承SuperBaseModel（根据实际需求而定）
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Sep 28, 2016 11:12:47 AM 
 * 
 */
@MappedSuperclass
public class SuperBaseModel extends BaseModel {

	private static final long serialVersionUID = 7054381337068387202L;

	@Column(name="CREATEDATE", updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 更新时间（即操作时间）
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATORID", updatable = false)
	private User operator;// 操作人
	
	@Column(name="OPERATORNAME")
	private String operatorName;// 操作人名字

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
	
}
