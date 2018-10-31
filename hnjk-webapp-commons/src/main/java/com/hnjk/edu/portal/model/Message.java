package com.hnjk.edu.portal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**
 * 系统消息表.
 * <code>Message</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-16 上午10:09:48
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_PORTAL_MSG")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "edu_portal_msg")
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Message extends BaseModel implements Serializable {
	
	@Column(name="MSGTITLE",length=255,nullable=false)
	@Field(name = "msgTitle", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String msgTitle;//消息标题
	
	@Column(name="MSGTYPE",nullable=false)
	private String msgType;// 消息类型 取自字典CodeMsgType: tips - 温馨提示 / sysmsg - 系统消息/ usermsg - 用户消息/announcement-公告通知
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", nullable = false)
	@Field(name = "content", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String content;//消息内容
	
	@Column(name="SENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime;//发送时间
	
	@Column(name="TOTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date toTime;//倒计时时间
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Message parent;//父
	
	@Column(name="SENDTYPE")
	private String sendType;//附加发送选项 portal - 发送到门户 / email - 发送到邮件 / sms - 发送到手机
	
	@Column(name="ISDRAFT")
	private String isDraft = Constants.BOOLEAN_NO;//是否草稿
	
	@Column(name="ISREPLY")
	private String isReply = Constants.BOOLEAN_NO;//是否允许回复,非用户消息都不能回复
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@Where(clause="isDeleted=0")
	@OrderBy("sendTime DESC")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Message> childs = new HashSet<Message>(0);

	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();
		
	@Transient
	private boolean readed;//是否已读	

	/**3.2.4 新增表单ID及类型*/
	@Column(name="FORMID")
	private String formId;//表单ID，消息与业务关联的表单ID
	
	@Column(name="FORMTYPE")
	private String formType;//表单类型，如bbsreply,exicerreply等等，查询时用来缩小范围
	
	/**
	 * 将发送人信息移到消息主体 2016-1-21
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDERID")
	private User sender;// 发送人
	
	@Column(name="SENDERNAME")
	private String senderName;// 发送人姓名
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGUNITID")
	private OrgUnit orgUnit;// 发送人所在单位
	
	@Column(name="HASATTACHS")
	private String hasAttachs = Constants.BOOLEAN_NO;// 是否有附件
	
	@Column(name="STATUS")
	private String status = Constants.BOOLEAN_NO;
	
	@Transient
	private String title;

	@Transient
	private String studyNo;

}
