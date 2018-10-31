package com.hnjk.platform.system.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统访问日志表
 * @author Marco.hu
 *
 */
@Entity
@Table(name = "HNJK_SYS_ACCESSLOGS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class AccessLogs extends BaseModel implements Serializable{

	private static final long serialVersionUID = 447325503612706289L;

	/**
	 * 客户端IP地址
	 */
	@Column(name="IPADDRESS")
	private String ipaddress;

	/**
	 * 访问时间
	 */
	@Column(name="ACCESSTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accessTime;

	/**
	 * 访问协议 HTTP1.0/HTTP1.1
	 */
	@Column(name="PROTOCOL")
	private String protocol;

	/**
	 * F服务器返回状态 200 404 500
	 */
	@Column(name="SERVERSTATUS")
	private Integer serverStatus;

	/**
	 * 访问产生流量
	 */
	@Column(name="NETFLOW")
	private Long netFlow;

	/**
	 * 服务器处理时间 毫秒
	 */
	@Column(name="RUNNINGTIME")
	private Double runningTime;

	/**
	 * 访问资源路径
	 */
	@Column(name="URL")
	private String url;

	/**
	 * 客户端操作系统
	 */
	@Column(name="CLIENTOS")
	private String clientOs;

	/**
	 * 客户端浏览器
	 */
	@Column(name="CLIENTBROWSER")
	private String clientBrowser;

	/**
	 * 访问者账号
	 */
	@Column(name="USERNAME")
	private String username;
	
}
