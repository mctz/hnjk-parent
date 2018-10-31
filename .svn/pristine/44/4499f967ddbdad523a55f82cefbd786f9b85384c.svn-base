package com.hnjk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 用户扩展表.用来扩展用户的参数配置等信息.
 * @author hzg
 *
 */
@Entity
@Table(name = "hnjk_sys_usersextend")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserExtends extends BaseModel{

	private static final long serialVersionUID = -163966667675516489L;
	
	/**用户扩展编码 -  所在城市*/
	public final static String USER_EXTENDCODE_CITY = "city";
	
	/**用户扩展编码 - 用户头像*/
	public final static String USER_EXTENDCODE_FACE = "userface";
	
	/**用户扩展编码 - 用户菜单风格*/
	public final static String USER_EXTENDCODE_MENUSTYLE = "menustyle";
	
	/**用户扩展编码 - 默认学籍ID*/
	public final static String USER_EXTENDCODE_DEFAULTROLLID = "defalutrollid";
	/**
	 * 用户扩展编码 - 学生默认基础信息ID
	 */
	public final static String USER_EXTENDCODE_DEFAULTBASEINFOID = "defalutbaseinfoid";
	/**
	 * 用户扩展编码 - 学籍卡的提交时间
	 */
	public final static String USER_EXTENDCODE_ROLLCARDSUBMITTIME= "rollcardsubmittime";
	/** 用户扩展编码 - 系统用户联系人姓名 */
	public final static String USER_EXTENDCODE_REALNAME= "realname";
	
	public UserExtends(){}
	
	public UserExtends(String exCode,String exValue,User user){
		this.exCode = exCode;
		this.exValue = exValue;
		this.user = user;
	}
	
	@Column(name="EXCODE")
	private String exCode;//扩展键
	
	@Column(name="EXVALUE")
	private String exValue;//扩展值
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SYSUSERID")
	private User user;//系统用户

	public String getExCode() {
		return exCode;
	}

	public void setExCode(String exCode) {
		this.exCode = exCode;
	}

	public String getExValue() {
		return exValue;
	}

	public void setExValue(String exValue) {
		this.exValue = exValue;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
}
