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
 * 敏感词
 * @author Zik, 广东学苑教育发展有限公司.
 *
 */
@Entity
@Table(name="EDU_SYS_SENSITIVEWORD")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class SensitiveWord extends BaseModel {

	private static final long serialVersionUID = 6408548727142216145L;
	
	@Column(name="WORD")
	private String word;// 敏感词
	
	@Column(name="CREATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 修改时间

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
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
