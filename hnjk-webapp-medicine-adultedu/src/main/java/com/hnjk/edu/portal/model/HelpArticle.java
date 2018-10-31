package com.hnjk.edu.portal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Attachs;

/**
 * 在线帮助文章.
 * <code>HelpArticle</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-9-9 上午11:09:04
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_HELP_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HelpArticle extends BaseModel{

	private static final long serialVersionUID = 7517321806322107100L;
	
	@Column(name = "TITLE", nullable = false)
	@Field(name = "title", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String title;//标题
	
	@Column(name = "TAGS")
	@Field(name = "tags", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String tags;//标签，关键字
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", nullable = false)
	@Field(name = "content", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String content;//内容
	
	@Column(name="FILLINMAN")
	private String fillinMan;//撰稿人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//撰稿人ID
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILLINDATE")
	private Date fillinDate;//撰稿时间
	
	@Column(name="ISPUBLISH")
	private String isPublish = Constants.BOOLEAN_NO;//是否发布
	
	@Column(name="RESOLVED")
	private Integer resolved;//用户已解决数量
	
	@Column(name="UNRESOLVED")
	private Integer unresolved;//未解决的数量

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHANNELID")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	private HelpChannel channel;//所属栏目
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();

	@Column(name="SHOWORDER")
	private Integer showOrder;
	
	
	
	/**
	 * @return the showOrder
	 */
	public Integer getShowOrder() {
		return showOrder;
	}

	/**
	 * @param showOrder the showOrder to set
	 */
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	/**
	 * @return the resolved
	 */
	public Integer getResolved() {
		return resolved;
	}

	/**
	 * @param resolved the resolved to set
	 */
	public void setResolved(Integer resolved) {
		this.resolved = resolved;
	}

	/**
	 * @return the unresolved
	 */
	public Integer getUnresolved() {
		return unresolved;
	}

	/**
	 * @param unresolved the unresolved to set
	 */
	public void setUnresolved(Integer unresolved) {
		this.unresolved = unresolved;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the fillinMan
	 */
	public String getFillinMan() {
		return fillinMan;
	}

	/**
	 * @param fillinMan the fillinMan to set
	 */
	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	/**
	 * @return the fillinManId
	 */
	public String getFillinManId() {
		return fillinManId;
	}

	/**
	 * @param fillinManId the fillinManId to set
	 */
	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	/**
	 * @return the fillinDate
	 */
	public Date getFillinDate() {
		return fillinDate;
	}

	/**
	 * @param fillinDate the fillinDate to set
	 */
	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	/**
	 * @return the isPublish
	 */
	public String getIsPublish() {
		return isPublish;
	}

	/**
	 * @param isPublish the isPublish to set
	 */
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}


	/**
	 * @return the channel
	 */
	public HelpChannel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(HelpChannel channel) {
		this.channel = channel;
	}

	/**
	 * @return the attachs
	 */
	public List<Attachs> getAttachs() {
		return attachs;
	}

	/**
	 * @param attachs the attachs to set
	 */
	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}
	
	

}
