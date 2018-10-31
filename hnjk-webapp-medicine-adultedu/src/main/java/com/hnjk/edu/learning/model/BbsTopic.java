package com.hnjk.edu.learning.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.platform.system.model.Attachs;

/**
 * 论坛帖子表
 * <code>BbsTopic</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-9-6 下午03:26:47
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BBS_TOPIC")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "edu_bbs_topic")
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsTopic extends BaseModel{

	/**
	 * 关联课程ID
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;

	/**
	 * 关联知识节点ID
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSID")
	private Syllabus syllabus;

	/**
	 * 标题
	 */
	@Column(name="TITLE",nullable=false)
	@Field(name = "title", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String title;

	/**
	 * 内容
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", nullable = false)
	@Field(name = "content", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String content;

	/**
	 * 帖子类型：字典CodeBbsTopicType：普通帖/提问/活动
	 */
	@Column(name="TOPICTYPE",nullable=false)
	@Field(name = "topicType", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String topicType;

	/**
	 * 发帖人
	 */
	@Column(name="FILLINMAN")
	@Field(name = "fillinMan", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String fillinMan;

	/**
	 * 附件
	 */
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();

	/**
	 * 发帖时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FILLINDATE")
	private Date fillinDate = new Date();

	/**
	 * 帖子状态 字典CodeBbsTopicStatus：0-普通/1-精华/-1锁定
	 */
	@Column(name="STATUS")
	private Integer status = 0;

	/**
	 * 帖子置顶数 数字越大，越靠上
	 */
	@Column(name="TOPLEVEL")
	private Integer topLevel=0;

	/**
	 * 回复数
	 */
	@Column(name="REPLYCOUNT")
	private Integer replyCount = 0;

	/**
	 * 点击量
	 */
	@Column(name="CLICKCOUNT")
	private Integer clickCount = 1;

	/**
	 * 最后回复人
	 */
	@Column(name="LASTREPLYMAN")
	private String lastReplyMan;
	

	/**
	 * 最后回复人对象
	 */
	//TODO:this field is string ,why not class
	@Column(name = "LASTREPLYMANID")
	private String lastReplyManId;

	/**
	 * 第一回复人
	 */
	@Column(name="FIRSTREPLYMAN")
	private String firstReplyMan;

	/**
	 * 第一回复人
	 */
	@Column(name="FIRSTREPLYACCOUNT")
	private String firstReplyAccount;

	/**
	 * 第一回复人对象
	 */
	@Column(name = "FIRSTREPLYMANID")
	private String firstReplyManId;

	/**
	 * 首次回复日期
	 */
	@Column(name="HOWLONGREPLY")
	private BigDecimal howLongReply;

	/**
	 * 最后回复日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LASTREPLYDATE")
	private Date lastReplyDate;

	/**
	 * 所属论坛版块
	 */
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "SECTIONID") 
	@Where(clause="isDeleted=0")
	private BbsSection bbsSection;

	/**
	 * 回复贴
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bbsTopic")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause="isDeleted=0")
	@DeleteChild(deleteable=true)
	private Set<BbsReply> bbsReplys = new HashSet<BbsReply>(0);
	
	/*3.0.8 新增小组讨论相关字段*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private BbsTopic parenTopic;

	/**
	 * 讨论话题权限，取自字典CodeViewPermiss：小组成员可见/全部可见
	 */
	@Column(name="VIEWPERMISS")
	private String viewPermiss;

	/**
	 * 讨论截止时间
	 */
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parenTopic")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	@Where(clause="isDeleted=0")
	@OrderBy("fillinDate DESC")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@DeleteChild(deleteable=true)
	private Set<BbsTopic> childs = new HashSet<BbsTopic>(0);
	
	/**3.0.9新增是否应答字段，用来作为随堂问答的条件*/
	@Column(name="ISANSWERED")
	private Integer isAnswered = Constants.BOOLEAN_FALSE;//是否应答 0 - 未应答 1- 应答
	
	/**
	 * 3.0.9 调整
	 * 发帖人对象
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "FILLINMANID")
	private BbsUserInfo bbsUserInfo;

	/**
	 * 是否有附件
	 */
	@Column(name="ISATTACHS",length=1)
	private String isAttachs = Constants.BOOLEAN_NO;

	/**
	 * 小组讨论话题关联的小组
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID")
	private BbsGroup bbsGroup;
	
	/**新增是否优秀贴，用来作为学生网络辅导分的依据*/
	@Column(name="ISBEST",length=1)
	private String isBest = Constants.BOOLEAN_NO;//是否优秀贴，用来作为学生网络辅导分的依据
	
	/**
	 * 3.3.3 新增帖子评分属性
	 * 帖子评分属性:无效贴/有效贴/良好贴/优秀贴
	 */
	@Column(name="SCORETYPE")
	private String scoreType = "0";

	/**
	 * 是否有操作权限
	 */
	@Transient
	private boolean hasCheckAuthority;


	/**3.1.7 新增标签，用来归类一些特殊类型的帖子，如FAQ ...*/
	@Column(name="TAGS",length=255)
	private String tags;//标签，用来归类一些特殊类型的帖子
	
	/**
	 * 3.1.8 新增针对学生反馈的字段
	 * 字典CodeFacebookType，预约学习/预约考试/预约毕业论文/课件学习/随堂练习/课堂作业/期末考试/统考/学籍异动/毕业/学位/其他
	 */
	@Column(name="FACEBOOKTYPE")
	private String facebookType;

	/**3.1.9 新增关键字*/
	@Column(name="KEYWORDS",length=255)
	@Field(name = "keywords", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String keywords;
	
	/**3.1.11 新增年度学期，为区别学生提问所在年度学期*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;

	/**
	 * 学期
	 */
	@Column(name="TERM")
	private String term;
	
	@Transient
	private Classes classes;
	
	@Transient
	private String unitName;

	/**
	 * 某门课程的总帖数
	 */
	@Transient
	private Integer totalTopicNum;

	/**
	 * 某门课程的有效贴数
	 */
	@Transient
	private Integer validTopicNum;

	/**
	 * 回复
	 */
	@Transient
	private List<BbsReply> replyList = new ArrayList<BbsReply>();

	/**
	 * 是否已回复
	 */
	@Transient
	private String isReply=Constants.BOOLEAN_NO;

}
