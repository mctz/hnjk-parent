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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseTreeModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;

/**
 * 门户栏目表<code>Channel</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-10 下午04:26:50
 * @see 
 * @version 1.0
*/
@Entity
@Table(name = "EDU_PORTAL_CHANNEL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Channel extends BaseTreeModel {
    private static final long serialVersionUID = -5857349141935760806L;
    
    /**栏目的根，不要修改这个值*/
    public final static String CHANNEL_ROOT = "0";
    
    @Column(name = "CHANNELCONTENT", length = 500)
    private String channelContent;//栏目内容
    
    @Column(name = "CHANNELHREF")
    private String channelHref;//外部链接

    @Column(name = "CHANNELNAME",nullable=false)
    private String channelName;//栏目名称

    @Column(name = "CHANNELPOSITION", length = 20)
    private String channelPosition;//栏目位置

    @Column(name = "CHANNELSTATUS", precision = 22, scale = 0)
    private Integer channelStatus = 0;//栏目状态

    @Column(name = "CHANNELTYPE")
    private String channelType ;//栏目类型

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILLINDATE", length = 7)
    private Date fillinDate = new Date();//创建日期

    @Column(name = "FILLINMAN", length = 50)
    private String fillinMan;//创建人

    @Column(name = "FILLINMANID", length = 32)
    private String fillinManId;//创建人ID
   
    @Column(name = "ISCHILD", length = 1)
    private String isChild =  Constants.BOOLEAN_NO;//是否叶子栏目
    
    @Column(name="CHANNELLEVEL")
    private int channelLevel ;//栏目级别

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Channel parent;//父子关系
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	@OrderBy("showOrder asc")
	@Where(clause="isDeleted=0")
    private Set<Channel> children = new HashSet<Channel>(0);//父子关系    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "channel")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("fillinDate desc")
	@Where(clause="isDeleted=0")
    private Set<Article> atricle = new HashSet<Article>(0);//栏目：文章 1：n
    
    @Column(name = "SHOWORDER", precision = 6, scale = 0)
    private int showOrder;//排序号
    
    @Column(name="ISOPENBRSCHOOL")
    private String isOpenBrSchool = Constants.BOOLEAN_NO;//是否学校中心栏目

    /**3.2.11 新增关联课程*/
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "COURSEID") 
    private Course course;//所关联的课程
    
    @Transient
	private String parentId;
    
    public Channel() {
    }
       
    
    
    /**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}



	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
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



	public String getIsOpenBrSchool() {
		return isOpenBrSchool;
	}



	public void setIsOpenBrSchool(String isOpenBrSchool) {
		this.isOpenBrSchool = isOpenBrSchool;
	}



	public int getChannelLevel() {
		return channelLevel;
	}



	public void setChannelLevel(int channelLevel) {
		this.channelLevel = channelLevel;
	}



	public Set<Article> getAtricle() {
		return atricle;
	}
    
    public void addArticle(Article article){
    	getAtricle().add(article);
    }
    
	public void setAtricle(Set<Article> atricle) {
		this.atricle = atricle;
	}

	public void addChild(Channel channel) {
	children.add(channel);
    }

    public String getChannelContent() {
        return channelContent;
    }

    public String getChannelHref() {
        return channelHref;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelPosition() {
        return channelPosition;
    }

     

    public Integer getChannelStatus() {
		return channelStatus;
	}



	public void setChannelStatus(Integer channelStatus) {
		this.channelStatus = channelStatus;
	}

	public Set<Channel> getChildren() {
	return children;
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

    public Channel getParent() {
        return parent;
    }

    public int getShowOrder() {
        return showOrder;
    }


    public void setChannelContent(String channelContent) {
        this.channelContent = channelContent;
    }

    public void setChannelHref(String channelHref) {
        this.channelHref = channelHref;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setChannelPosition(String channelPosition) {
        this.channelPosition = channelPosition;
    }

   
    public String getChannelType() {
		return channelType;
	}


	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	


    public String getIsChild() {
		return isChild;
	}


	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}


	public void setChildren(Set<Channel> children) {
        this.children = children;
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

    public void setParent(Channel parent) {
        this.parent = parent;
    }

   

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }



	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeName()
	 */
	@Override
	public String getNodeName() {	
		return getChannelName();
	}



	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeCode()
	 */
	@Override
	public String getNodeCode() {		
		return getResourceid();
	}



	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getParentNodeId()
	 */
	@Override
	public String getParentNodeId() {	
		return getParentId();
	}



	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeLevel()
	 */
	@Override
	public Integer getNodeLevel() {	
		return getChannelLevel();
	}
    
    
  
}
