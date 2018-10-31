package com.hnjk.edu.learning.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Attachs;

/**
 * 论坛帖子回复表
 * <code>BbsReply</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-9-6 下午03:27:15
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BBS_REPLY")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "edu_bbs_reply")
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsReply  extends BaseModel{

	/**
	 * 回复内容
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "REPLYCONTENT", nullable = false)
	@Field(name = "replyContent", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String replyContent;

	/**
	 * 回复人
	 */
	@Column(name="REPLYMAN")
	@Field(name = "replyMan", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String replyMan;
	
	/**
	 * 3.3.3 新增帖子评分属性
	 * 帖子评分属性:无效贴/有效贴/良好贴/优秀贴
	 */
	@Column(name="SCORETYPE")
	private String scoreType = "0";

	/**
	 * 附件
	 */
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();

	/**
	 * 回复日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPLYDATE")
	private Date replyDate;

	/**
	 * 排序号
	 */
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;

	/**
	 * 所属帖子
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TOPICID")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	private BbsTopic bbsTopic;
	
	/**
	 * 3.0.9 调整
	 * 回复人ID
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "REPLYMANID")
	private BbsUserInfo bbsUserInfo;

	/**
	 * 是否有附件
	 */
	@Column(name="ISATTACHS",length=1)
	private String isAttachs = Constants.BOOLEAN_NO;
	
	/**
	 * 新增是否优秀贴，用来作为学生网络辅导分的依据
	 * 是否优秀贴，用来作为学生网络辅导分的依据
	 */
	@Column(name="ISBEST",length=1)
	private String isBest = Constants.BOOLEAN_NO;
	
	@Transient
	private boolean isPersisted = Boolean.FALSE;

	/**
	 * 是否我的回复
	 */
	@Transient
	private String isMine;

	public boolean getIsPersisted() {
		return this.isPersisted;
	}

	public void setIsPersisted(boolean isPersisted) {
		this.isPersisted = isPersisted;
	}
}
