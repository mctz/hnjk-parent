package com.hnjk.edu.portal.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;

/**
 * 消息发送者明细.
 * <code>MessageSender</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-16 上午10:35:21
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_PORTAL_SENDER")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageSender extends BaseModel{

	private static final long serialVersionUID = 8124049132348119583L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MSGID")
	private Message message;//消息
	
	@Column(name="SENDERID")
	private String senderId;//发送人ID
	
	@Column(name="SENDER")
	private String sender;//发送人姓名
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGUNITID")
	private OrgUnit orgUnit;//发送人所在单位

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public OrgUnit getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(OrgUnit orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	
	
}
