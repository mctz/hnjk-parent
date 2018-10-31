package com.hnjk.edu.portal.model;

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
import org.jfree.ui.Size2D;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.security.model.OrgUnit;

/**
 * 门户文章表<code>Article</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-10 下午04:27:28
 * @see 
 * @version 1.0
*/
@Entity
@Table(name = "EDU_PORTAL_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "edu_portal_articles")
public class Article extends BaseModel {
	private static final long serialVersionUID = -3107987203312429330L;
	
	/**审核通过*/
	public final static int AUDIT_STATUS_PASS = 1;
	
	/**审核不通过*/
	public final static int AUDIT_STATUS_NOTPASS = 0;
	
	/**文章表单类型*/
	public final static String ARTICLE_ATTACHS_TYPE = "ARTICLE";
	
	
	@Column(name="ARTITYPE")
	private String artitype;//所属文章类型

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDITDATE")
	private Date auditDate;//审核时间

	@Column(name = "AUDITMAN")
	private String auditMan;//审核人

	@Column(name = "AUDITMANID", length = 1000)
	private String auditManId;//审核人ID

	@Column(name = "AUDITSTATUS", precision = 22, scale = 0)
	private Integer auditStatus = AUDIT_STATUS_NOTPASS;//审核状态 0-未审核;1-已审核发布

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHANNELID")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	private Channel channel;//所属栏目

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", nullable = false)
	@Field(name = "content", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String content;//文章内容

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILLINDATE")
	private Date fillinDate = new Date();//创建时间

	@Column(name = "FILLINMAN", length = 50)
	@Field(name = "fillinMan", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String fillinMan;//创建人

	@Column(name = "FILLINMANID", length = 32)
	private String fillinManId;//创建人ID

	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "ORGUNITID") 
	@Where(clause="isDeleted=0")
	private OrgUnit orgUnit;//所属单位
	
	@Column(name = "IMGPATH")
	private String imgPath;//封面文章图片路径
	
	//@Column(name = "ISATTACH", length = 1)
	//private String isAttach = Constants.BOOLEAN_NO;//是否有附件
	
	@Column(name = "ISDRAFT", length = 1)
	private String isDraft = Constants.BOOLEAN_NO;//是否为草稿
	
	@Column(name = "ISPHOTONEWS", length = 1)
	private String isPhotoNews = Constants.BOOLEAN_NO;//是否为图片新闻

	@Column(name = "SOURCE")	
	private String source;//文章来源

	@Column(name = "SUMMARY", length = 500)	
	private String summary;//文章摘要

	@Column(name = "TAGS")
	@Field(name = "tags", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String tags;//关键字

	@Column(name = "TITLE", nullable = false)
	@Field(name = "title", index = Index.TOKENIZED,analyzer = @Analyzer(impl = ChineseAnalyzer.class),store = Store.YES)
	private String title;//文章标题

	@Column(name = "TOPLEVEL", precision = 22, scale = 0)
	private Integer topLevel = 0;//置顶级别

	@Column(name="UPDATEDATE")
    private Date updateDate;//更新时间
	
	@Column(name="FILESIZE")
	private Long fileSize;
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();

	@Transient
	private String isNew = Constants.BOOLEAN_NO;//判断文章是否满足新的条件 
	
	public Article() {
	}
		
	public String getIsNew() {
		try {
			if(ExDateUtils.getDateDiffNum(ExDateUtils.DATE_FORMAT.format(fillinDate), ExDateUtils.DATE_FORMAT.format(new Date())) < 15){				
				isNew = Constants.BOOLEAN_YES;
			}
		} catch (Exception e) {
			
		}		
		return isNew;
	}

	public List<Attachs> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}

	public OrgUnit getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(OrgUnit orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getArtitype() {
		return artitype;
	}

	public void setArtitype(String artitype) {
		this.artitype = artitype;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public String getAuditManId() {
		return auditManId;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getContent() {
		return content;
	}

	public Date getFillinDate() {
		return fillinDate;
	}

	public String getFillinMan() {
		return fillinMan;
	}

	public String getFillinManId() {
		return fillinManId;
	}

	public String getImgPath() {
		return imgPath;
	}

	public String getSource() {
		return source;
	}

	public String getSummary() {
		return summary;
	}

	public String getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}

	public Integer getTopLevel() {
		return topLevel;
	}

//	public String getIsAttach() {
//		return isAttach;
//	}
//
//	public void setIsAttach(String isAttach) {
//		this.isAttach = isAttach;
//	}

	public String getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(String isDraft) {
		this.isDraft = isDraft;
	}

	public String getIsPhotoNews() {
		return isPhotoNews;
	}

	public void setIsPhotoNews(String isPhotoNews) {
		this.isPhotoNews = isPhotoNews;
	}

	//public void setArtitype(Artitype artitype) {
	//	this.artitype = artitype;
	//}


	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public void setAuditManId(String auditManId) {
		this.auditManId = auditManId;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}


	public void setSource(String source) {
		this.source = source;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTopLevel(Integer topLevel) {
		this.topLevel = topLevel;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
}
