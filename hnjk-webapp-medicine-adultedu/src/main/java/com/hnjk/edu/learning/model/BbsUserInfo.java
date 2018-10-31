package com.hnjk.edu.learning.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.security.model.User;

/**
 * 论坛用户信息表，作为系统用户表的扩展.
 * <code>BbsUserInfo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-9-6 下午03:37:32
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BBS_USERINFO")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "edu_bbs_reply")
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsUserInfo extends BaseModel{

	/**
	 * 所属系统用户对象
	 */
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID")	
	private User sysUser;

	/**
	 * 用户真实姓名或昵称
	 */
	@Column(name="USERNAME")
	private String userName;

	/**
	 * 用户级别
	 */
	@Column(name="USERLEVEL")
	private Integer userLevel = 0;

	/**
	 * 发帖总数
	 */
	@Column(name="TOPICCOUNT")
	private Integer topicCount = 0;

	/**
	 * 用户图像
	 */
	@Transient
	private String userface;

	/**
	 * 积分
	 */
	@Column(name="SCORE")
	private Long score = 0L;

	/**
	 * 经验值
	 */
	@Column(name="EXPERVALUE")
	private Long experValue = 0L;

	/**
	 * 个性签名
	 */
	@Column(name="DESINGER")
	private String desinger;
	
	@Transient
	private StudentInfo studentInfo;
	
}
