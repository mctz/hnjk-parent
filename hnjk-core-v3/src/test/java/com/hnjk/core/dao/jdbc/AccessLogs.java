package com.hnjk.core.dao.jdbc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.support.base.model.BaseModel;



public class AccessLogs extends BaseModel{

	private static final long serialVersionUID = 447325503612706289L;
		
	private String ipaddress;//客户端IP地址

	private Date accessTime;//访问时间
	
	private String protocol;//访问协议 HTTP1.0/HTTP1.1

	private Integer serverStatus;//F服务器返回状态 200 404 500
	
	private Long netFlow;//访问产生流量

	private Double runningTime;//服务器处理时间 毫秒
	
	private String url;//访问资源路径

	private String clientOs;//客户端操作系统
	
	private String clientBrowser;//客户端浏览器 

	private String username;//访问者账号
	
	//l.resourceid,l.ipaddress,l.accesstime,l.protocol,l.serverstatus,l.netflow,
	//l.runningtime,l.url,l.clientos,l.clientbrowser,l.isdeleted,l.version,l.username
	public Object[] getArray(){
		GUIDUtils.init();
		return new Object[]{GUIDUtils.buildMd5GUID(false),getIpaddress(),getAccessTime(),getProtocol(),getServerStatus(),getNetFlow(),
				getRunningTime(),getUrl(),getClientOs(),getClientBrowser(),getIsDeleted(),0,getUsername()};
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * @return the accessTime
	 */
	public Date getAccessTime() {
		return accessTime;
	}

	/**
	 * @param accessTime the accessTime to set
	 */
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the serverStatus
	 */
	public Integer getServerStatus() {
		return serverStatus;
	}

	/**
	 * @param serverStatus the serverStatus to set
	 */
	public void setServerStatus(Integer serverStatus) {
		this.serverStatus = serverStatus;
	}

	/**
	 * @return the netFlow
	 */
	public Long getNetFlow() {
		return netFlow;
	}

	/**
	 * @param netFlow the netFlow to set
	 */
	public void setNetFlow(Long netFlow) {
		this.netFlow = netFlow;
	}

	/**
	 * @return the runningTime
	 */
	public Double getRunningTime() {
		return runningTime;
	}

	/**
	 * @param runningTime the runningTime to set
	 */
	public void setRunningTime(Double runningTime) {
		this.runningTime = runningTime;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the clientOs
	 */
	public String getClientOs() {
		return clientOs;
	}

	/**
	 * @param clientOs the clientOs to set
	 */
	public void setClientOs(String clientOs) {
		this.clientOs = clientOs;
	}

	/**
	 * @return the clientBrowser
	 */
	public String getClientBrowser() {
		return clientBrowser;
	}

	/**
	 * @param clientBrowser the clientBrowser to set
	 */
	public void setClientBrowser(String clientBrowser) {
		this.clientBrowser = clientBrowser;
	}
	
	

}
