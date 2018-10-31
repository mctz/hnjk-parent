package com.hnjk.edu.portal.model;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseTreeModel;
import com.hnjk.core.support.context.Constants;

/**
 * 帮助文档栏目
 * <code>HelpChannel</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-9-9 上午11:08:38
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_HELP_CHANNEL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HelpChannel extends BaseTreeModel{

	private static final long serialVersionUID = 4993169009752259072L;
	
	
	@Column(name="CHANNELNAME",nullable=false)
	 private String channelName;//栏目名称	
	
	@Column(name="ISCHILD")
	 private String isChild =  Constants.BOOLEAN_NO;//是否叶子栏目
	
	 @Column(name="CHANNELLEVEL")
	 private int channelLevel ;//栏目级别
	 
	 @Column(name="FILLINMANID")
	 private String fillinManId;//创建人ID
	 
	 @Column(name="FILLINMAN")
	 private String fillinMan;//创建人
	 
	 @Column(name="FILLINDATE")
	 private Date fillinDate;//创建日期 
	 
	 @Column(name="SHOWORDER")
	 private Integer showOrder;//排序号
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private HelpChannel parent;//父子关系
	

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	@OrderBy("showOrder asc")
	@Where(clause="isDeleted=0")
    private Set<HelpChannel> children = new HashSet<HelpChannel>(0);//父子关系
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "channel")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("fillinDate desc")
	@Where(clause="isDeleted=0")
    private Set<HelpArticle> atricle = new HashSet<HelpArticle>(0);//栏目：文章 1：n

    /**3.1.10 新增关联角色*/
    @Column(name="RELATEROLES")
    private String relateRoles;//关联角色 编码，多个角色用,分割
    
    @Transient
	private String parentId;
    
    
    
    
    /**
	 * @return the relateRoles
	 */
	public String getRelateRoles() {
		return relateRoles;
	}
	/**
	 * @param relateRoles the relateRoles to set
	 */
	public void setRelateRoles(String relateRoles) {
		this.relateRoles = relateRoles;
	}
	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @param channelName the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * @return the isChild
	 */
	public String getIsChild() {
		return isChild;
	}

	/**
	 * @param isChild the isChild to set
	 */
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}

	/**
	 * @return the channelLevel
	 */
	public int getChannelLevel() {
		return channelLevel;
	}

	/**
	 * @param channelLevel the channelLevel to set
	 */
	public void setChannelLevel(int channelLevel) {
		this.channelLevel = channelLevel;
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
	 * @return the parent
	 */
	public HelpChannel getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(HelpChannel parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public Set<HelpChannel> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<HelpChannel> children) {
		this.children = children;
	}

	/**
	 * @return the atricle
	 */
	public Set<HelpArticle> getAtricle() {
		return atricle;
	}

	/**
	 * @param atricle the atricle to set
	 */
	public void setAtricle(Set<HelpArticle> atricle) {
		this.atricle = atricle;
	}
    public String getNodecode(){
    	return getResourceid();
    }

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return getChannelName();
	}

	@Override
	public String getNodeCode() {
		// TODO Auto-generated method stub
		return getResourceid();
	}

	@Override
	public String getParentNodeId() {
		// TODO Auto-generated method stub
		return getParentId();
	}

	@Override
	public Integer getNodeLevel() {
		// TODO Auto-generated method stub
		return getChannelLevel();
	}
    
}
