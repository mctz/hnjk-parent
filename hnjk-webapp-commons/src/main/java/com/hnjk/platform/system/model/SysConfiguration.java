package com.hnjk.platform.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 系统配置参数表.
 * <code>SysConfiguration</code><p>
 * 系统在启动会加载并影射成全局Map.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-26 上午10:46:50
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "HNJK_SYS_CONFIG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SysConfiguration extends BaseModel{

	private static final long serialVersionUID = -4032812730103413771L;

	@Column(name="PARAMNAME",nullable=false)
	private String paramName;//参数名称
	
	@Column(name="PARAMCODE",unique=true,nullable=false)
	private  String paramCode;//参数编码	
	
	@Column(name="PARAMVALUE",nullable=false)
	private String paramValue;//参数值
	
	@Column(name="MEMO")
	private String memo;//参数说明

	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	
}
