package com.hnjk.edu.teaching.model;
// default package

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseTreeModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;

/**
 * 课程大纲的 知识结构树表
 * <code>Syllabus</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午02:21:57
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_SYLLABUSTREE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Syllabus extends BaseTreeModel {

	private static final long serialVersionUID = -550787267155464942L;
	 
	@Column(name="NODENAME")
    private String syllabusName;//知识节点名称
    
	@ManyToOne(fetch = FetchType.LAZY)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Syllabus parent;//父节点
    
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder asc")
	@Where(clause="isDeleted=0")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Syllabus> children =  new HashSet<Syllabus>(0);//子节点
	
    @Column(name="ISCHILD",length=1)
    private String isChild = Constants.BOOLEAN_NO;//是否有子节点
    
    @Column(name="NODELEVEL")
    private Long syllabusLevel;//节点级别
    
    @Column(name="NODETYPE")
    private String syllabusType;//#节点类型 取自字典CodeTeachingNodeType 章/节/点
    
    @Column(name="NODECONTENT")
    private String syllabusContent;//节点内容
    
    /**~~~~~~~~~3.1.2 移动到指标库中*/
    //@Column(name="REQUIRED")
    //private String required;//#教学要求度 了解/掌握/熟练掌握
    
    //@Column(name="ABILITYTARGET")
    //private String abilityTarget;//#能力目标  容易/中等/难
    
    @Column(name="PROVIDESTYDYHOUR")
    private Double provideStydyHour;//学时分配
    
    @Column(name="MEMO")
    private String memo;//备注
    
    @Column(name="SHOWORDER")
    private Integer showOrder = 0;//排序号
    
    @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
    private Course course;//所属课程
    
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SYLLABUSID")
//    private Syllabus syllabus;//课程教学大纲
    
    @Transient
	private String parentId;
    @Transient
	private String courseId;
    
    @Transient
    private Map<String, Object> exMetaMap = new HashMap<String, Object>();;//扩展属性，用来保存该节点下是否有学习目标、随堂练习等素材
        

	/**
	 * @return the exMetaMap
	 */
	public Map<String, Object> getExMetaMap() {
		return exMetaMap;
	}

	/**
	 * @param exMetaMap the exMetaMap to set
	 */
	public void setExMetaMap(Map<String, Object> exMetaMap) {
		this.exMetaMap = exMetaMap;
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

	public Set<Syllabus> getChildren() {
		return children;
	}

	public void setChildren(Set<Syllabus> children) {
		this.children = children;
	}

	public String getIsChild() {
		return isChild;
	}

	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	

	/**
	 * @return the syllabusName
	 */
	public String getSyllabusName() {
		return syllabusName;
	}

	/**
	 * @param syllabusName the syllabusName to set
	 */
	public void setSyllabusName(String syllabusName) {
		this.syllabusName = syllabusName;
	}

	/**
	 * @return the syllabusLevel
	 */
	public Long getSyllabusLevel() {
		return syllabusLevel;
	}

	/**
	 * @param syllabusLevel the syllabusLevel to set
	 */
	public void setSyllabusLevel(Long syllabusLevel) {
		this.syllabusLevel = syllabusLevel;
	}

	/**
	 * @return the syllabusType
	 */
	public String getSyllabusType() {
		return syllabusType;
	}

	/**
	 * @param syllabusType the syllabusType to set
	 */
	public void setSyllabusType(String syllabusType) {
		this.syllabusType = syllabusType;
	}

	/**
	 * @return the syllabusContent
	 */
	public String getSyllabusContent() {
		return syllabusContent;
	}

	/**
	 * @param syllabusContent the syllabusContent to set
	 */
	public void setSyllabusContent(String syllabusContent) {
		this.syllabusContent = syllabusContent;
	}

	public Syllabus getParent() {
		return parent;
	}

	public void setParent(Syllabus parent) {
		this.parent = parent;
	}

	

	public Double getProvideStydyHour() {
		return provideStydyHour;
	}

	public void setProvideStydyHour(Double provideStydyHour) {
		this.provideStydyHour = provideStydyHour;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
	
	/**
	 * 获取层级名称，如：第一章·第一节·第一点
	 * @return
	 */
	public String getFullNodeName() {
		List<String> nodes = new ArrayList<String>();
		Syllabus t_syllabus = this;
		while(t_syllabus!=null&&t_syllabus.getParent()!=null){
			nodes.add(t_syllabus.getSyllabusName());
			t_syllabus = t_syllabus.getParent();
		}
		Collections.reverse(nodes);
		String title = "";
		for (String str : nodes) {
			title += str + "·";
		}
		if(title.endsWith("·")) {
			title = title.substring(0,title.length()-1);
		}
		return title;
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeName()
	 */
	@Override
	public String getNodeName() {	
		return getSyllabusName();
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
		return getSyllabusLevel().intValue();
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
    
}