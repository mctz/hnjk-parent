package com.hnjk.core.support.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 基础的业务模型. <p>
 * 业务模型可以继承此模型.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-5-26下午05:00:06
 * @see 
 * @version 1.0
 */
@MappedSuperclass
public class BaseBizModel extends BaseModel{

	private static final long serialVersionUID = -3422659044645976260L;
	
	@Column(name="FILLINMANID")
	private String fillinmanId;//填写人ID
	
	@Column(name="FILLINMAN")
	private String fillinman;//填写人名
	
	@Temporal(TemporalType.DATE)
	@Column(name="FILLINDATE")
	private Date fillinDate;//填写时间

	public Date getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getFillinman() {
		return fillinman;
	}

	public void setFillinman(String fillinman) {
		this.fillinman = fillinman;
	}

	public String getFillinmanId() {
		return fillinmanId;
	}

	public void setFillinmanId(String fillinmanId) {
		this.fillinmanId = fillinmanId;
	}
	
	
	
}
