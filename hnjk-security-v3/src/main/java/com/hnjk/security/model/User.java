package com.hnjk.security.model;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * 系统用户模型. <code>User</code>
 * <p>; 实现了spring security UserDetails
 * {@link org.springframework.security.userdetails.UserDetails} 接口，便于将用户交个spring
 * security管理.
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-10-30 上午10:36:27
 * @see
 * @version 1.0
 */

@Entity
@Table(name = "hnjk_sys_users")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class User extends BaseModel implements UserDetails {
   
	protected static Logger logger = LoggerFactory.getLogger(User.class);

	private static final int DATE_SECONDS = 24*60*60;
	private static final int HOUR_SECONDS = 60*60;

	/**
	 * 用户组织ID
	 */
	@Column(name = "unitId", insertable = false, updatable = false)
	private String unitId;

	/**
	 * 用户登录账号
	 */
	@Column(name = "username", nullable = false, unique = true,length=50)
	private String username;

	/**
	 * 用户中文名
	 */
	@Column(name = "cnname",length=50)
	private String cnName;

	/**
	 * 用户密码
	 */
	@Column(name = "userPassword")
	private String password;

	/**
	 * 是否允许
	 */
	@Column(name = "ENABLED")
	private boolean enabled = true;

	@Transient
	private String enabledChar;

	/**
	 * 账号是否过期
	 */
	@Column(name = "ACCOUTEXPIRED")
	private boolean accountNonExpired = true;

	/**
	 * 账号是否锁定
	 */
	@Column(name = "ACCOUTLOCKED")
	private boolean accountNonLocked = true;

	/**
	 * 账号证书是否过期
	 */
	@Column(name = "CREDENTIALSEXPIRED")
	private boolean credentialsNonExpired = true;

	/**
	 * 最后登录时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastLoginTime")
	private Date lastLoginTime = new Date();

	/**
	 * 登录IP
	 */
	@Column(name = "loginIp")
	private String loginIp;

	/**
	 * 排序号
	 */
	@Column(name = "showOrder")
	private int showOrder = 0;

	/**
	 * 用户类型
	 * 如学生:student 教务人员: edumanager 配置在全局参数中
	 */
	@Column(name = "USERTYPE")
	private String userType;

	/**3.0.9新增网络来源*/
	@Column(name="FROMNET")
	private String fromNet = Constants.FROM_PUB;//网络来源 edu-教育网,pub-公网
	 /*
     * 新增重置学生重置密码的问题和答案属性
     */
    @Column(name="PASSWORDQUESTION")
    private String passwordQuestion; //重置密码问题
    
    @Column(name="PASSWORDANSWER")
    private String passwordAnswer;  //重置密码答案

	/**
	 * 用户权限
	 */
	@Transient
	private GrantedAuthority[] authorities;

	/**
	 * 用户所在组织单位
 	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	@JoinColumn(name = "unitId")
	@Where(clause="isDeleted=0")
	private OrgUnit orgUnit;

	/**
	 * 系统用户与角色关联 n:n
 	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "hnjk_sys_roleusers", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "roleId") })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause="isDeleted=0")
	@DeleteChild(deleteable=false)
	private Set<Role> roles = new LinkedHashSet<Role>(0);

	/**
	 * 用户扩展信息
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "user")
	@MapKey(name = "exCode")
	@Where(clause="isDeleted=0")
	private Map<String, UserExtends> userExtends = new LinkedHashMap<String, UserExtends>(0);
	
	/**3.0.9 新增自定义用户登录账号字段*/
	@Column(name="CUSTOMUSERNAME",length=50,unique=true)
	private String customUsername;//自定义登录账号

	/**
	 * 用户左侧菜单
	 */
	@Transient
	private String userTopMenuIds;

	/**
	 * 用户权限ID集合
	 */
	@Transient
	private String userRightsIds;

	/**
	 * APP识别ID（每台机都唯一），用于处理单用户登录
	 */
	@Column(name = "APPMAC")
	private String appMac;

	/**
	 * 在线时长，主要用于计算
	 */
	@Column(name="LOGINLONG")
	private Integer loginLong = 0;

	/**
	 * 在线时长，主要用于前端显示
	 */
	@Transient
	private String loginLongStr;

	/**
	 * 开始计算在线时长时间
	 */
	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/**
	 * 结束计算在线时长时间
	 */
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 终端类型
	 */
	@Column(name="TERMINALTYPE")
	private String terminalType;

	/**
	 * 登陆版本
	 */
	@Column(name="LOGINVERSION")
	private String loginVersion;

	/**
	 * 是否使用移动端
	 */
	@Column(name="ISUSEMOBILETERMINAL")
	private String isUsemobileTerminal;
	
	@Transient
	private Date startTimeTemp;

	public UserExtends getPropertys(String key) {
		UserExtends property = userExtends.get(key);
		return property;
	}
	
	@Override
	public String toString() {
		return this.getUsername();
	}

	public String getEnabledChar() {
		if(isEnabled()) {
			enabledChar="Y";
		} else {
			enabledChar="N";
		}
		return enabledChar;
	}

	public void setEnabledChar(String enabledChar) {
		this.enabledChar = enabledChar;
		if("Y".equalsIgnoreCase(this.enabledChar)) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isContainRole(String roleCode) {
		Set<Role> roles = getRoles();
		if (!roles.isEmpty()) {
			for (Role role : roles) {
				if (role.getRoleCode().equals(roleCode)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getLoginLongStr() {
		StringBuilder loginLongSB = new StringBuilder();
		if(this.loginLong==null){
			this.loginLong = 0;
		}
		int dateNum = Integer.valueOf(this.loginLong/DATE_SECONDS);
		int hourNum = Integer.valueOf((this.loginLong%DATE_SECONDS)/HOUR_SECONDS);
	    int minuteNum = ((this.loginLong%DATE_SECONDS)%HOUR_SECONDS)/60;
	    int sencondNum =((this.loginLong%DATE_SECONDS)%HOUR_SECONDS)%60;
	    if(dateNum!=0){
	    	loginLongSB.append(dateNum+"天"+hourNum+"小时"+minuteNum+"分钟"+sencondNum+"秒");
	    }else if(hourNum!=0){
	    	loginLongSB.append(hourNum+"小时"+minuteNum+"分钟"+sencondNum+"秒");
	    }else if(minuteNum!=0){
	    	loginLongSB.append(minuteNum+"分钟"+sencondNum+"秒");
	    }else if(sencondNum!=0){
	    	loginLongSB.append(sencondNum+"秒");
	    }else {
	    	loginLongSB.append("");
	    }
		return loginLongSB.toString();
	}

}