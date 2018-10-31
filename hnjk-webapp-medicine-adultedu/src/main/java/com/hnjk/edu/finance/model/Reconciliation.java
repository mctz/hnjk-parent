package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 对账文件
 */
@Entity
@Table(name = "EDU_FEE_RECONCILIATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Reconciliation extends BaseModel{
	
	public static final String status_default="N";
	
	public static final String status_yes="Y";
	
	public static final String status_wait="W";

	@Column(name="fileName")
	private String fileName;//文件名
	
	@Column(name="status")
	private String status=status_default;//对账状态
	
	@Column(name="uploadDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;//对账文件上传时间
	
	@Column(name="totalCount")
	private int totalCount;//总笔数
	
	@Column(name="totalFee",scale=2)
	private Double totalFee;//总金额
	
}
