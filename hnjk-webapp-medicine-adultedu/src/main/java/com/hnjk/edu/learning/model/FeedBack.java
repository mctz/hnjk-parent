package com.hnjk.edu.learning.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Indexed;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.User;

/**
 * 反馈信息
 * @author zik, 广东学苑教育发展有限公司
 *
 */
@Entity
@Table(name="EDU_LEAR_FEEDBACK")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index="edu_lear_feedback")
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class FeedBack extends BaseModel {
	
	@Column(name="USERNAME")
	private String userName;// 反馈人姓名
	
	@Column(name="FBTITLE")
	private String title;// 标题
	
	@Column(name="FBTYPE")
	private String type;// 类型
	
	@Column(name="FBCONTENT")
	private String content;// 内容
	
	@Column(name="FBTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date   feedBackDate;// 反馈时间
	
	@Column(name="ISREPLY")
	private String isReply;// 是否回复
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private FeedBack parent;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID")
	private User feedBackMan;// 反馈人
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	@Where(clause="isDeleted=0")
	@OrderBy("feedBackDate DESC")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@DeleteChild(deleteable=true)
	private Set<FeedBack> feedBackSet = new HashSet<FeedBack>();
	
}
