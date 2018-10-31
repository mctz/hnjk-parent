package com.hnjk.edu.portal.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.User;

/**
 * 消息接受者与用户信息关联表
 * <code>MessageReceriverUser</code><p>
 * 
 * @author：zik, 广东学苑教育发展有限公司
 * @since： 2014-6-9 14:34:35
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_PORTAL_RECEIVEUSER")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageReceiverUser  extends BaseModel {
	
	private static final long serialVersionUID = 5131460311847700922L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "RECEIVERID")
	private MessageReceiver messageReceiver;//消息
	
	/**
	 * 接收人，用户中文姓名，阅读状态，消息状态，阅读时间 2016-1-25
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID")
	private User receiver;// 接收人
	
	@Column(name="USERCNNAME")
	private String userCNName;// 用户中文姓名
	
	@Column(name="USERNAME")
	private String userName;// 用户名（即账号）
	
	@Column(name="READSTATUS")
	private String readStatus="unRead";// 阅读状态，字典CodeReadStatus,unRead-未读，readed-已读
	
	@Column(name="MSGSTATUS")
	private String msgStatus;// 消息状态, 字典CodeMsgStatus,unSend-待发送，sended-已发送
	
	@Column(name="READTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date readTime;// 阅读时间
	
	public MessageReceiver getMessageReceiver() {
		return messageReceiver;
	}

	public void setMessageReceiver(MessageReceiver messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getUserCNName() {
		return userCNName;
	}

	public void setUserCNName(String userCNName) {
		this.userCNName = userCNName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

}
