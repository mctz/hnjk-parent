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

/**
 * 消息阅读情况表.
 * <code>MessageStat</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-16 上午10:59:59
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_PORTAL_MSGSTAT")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageStat extends BaseModel{

	private static final long serialVersionUID = 678790436255385620L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MSGID")
	private Message message;//消息
	
	@Column(name="RECEIVERID")
	private String receiverId;//阅读人ID
	
	@Column(name="READTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date readTime;//阅读时间

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	
}
