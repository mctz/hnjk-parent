package com.hnjk.security.model;

import com.hnjk.core.support.base.model.BaseTreeModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 系统资源表，包含资源和菜单<code>AUTHORITY</code>
 * <p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-10-30 上午10:04:00
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "hnjk_sys_resources")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Resource extends BaseTreeModel {

	private static final long serialVersionUID = -5396060046559398409L;
	
	public static String RESOURCE_CODE = "resourceCode";

	/**
	 * 资源编码
	 */
	@Column(name = "resourceCode", nullable = false, unique = true)
	private String resourceCode;

	/**
	 * 资源类型
	 */
	@Column(name = "resourceType", nullable = false)
	private String resourceType;

	/**
	 * 资源名称
	 */
	@Column(name = "resourceName", nullable = false, unique = true)
	private String resourceName;

	/**
	 * 资源路径
	 */
	@Column(name = "resourcePath", nullable = false, unique = true)
	private String resourcePath;

	/**
	 * 资源描述
	 */
	@Column(name = "resourceDescript")
	private String resourceDescript;

	/**
	 * 资源层级
	 */
	@Column(name = "resourceLevel")
	private int resourceLevel = 0;

	/**
	 * 排序号
	 */
	@Column(name = "showOrder")
	private int showOrder;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Resource parent;// 父对象

	// 授权资源父子关系，形成树形结构
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder asc")
	@Where(clause="isDeleted=0")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Resource> children = new LinkedHashSet<Resource>(0);
	
	@Transient
	private String parentId; // 临时父对象id
	
	/**
	 *  class值 样式
	 */
	@Column(name="STYLE")
	private String style;

	/**
	 * 页面类型
	 */
	@Column(name="JUMPURL")
	private String pageType;

	/**
	 * js函数名称  例如: add()
	 */
	@Column(name="JSFUNCTION")
	private String jsFunction;

	/**
	 * 是否有子
	 */
	@Column(name="ISChILD")
	private String isChild;

	/**
	 * 第一人称资源名
	 */
	@Column(name="FIRSTPERSONNAME")
	private String firstPersonName;
	/*
	 * // 菜单与功能点，1:n @OneToMany(fetch = FetchType.LAZY, mappedBy = "resources")
	 * @org.hibernate.annotations.Cascade(value = {
	 * org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
	 * org.hibernate.annotations.CascadeType.ALL }) @Cache(usage =
	 * CacheConcurrencyStrategy.READ_WRITE) @OrderBy("showOrder ASC") private
	 * Set<Function> functions = new LinkedHashSet<Function>(0);//
	 */

	@Override
	public String toString() {
		return this.getResourceName();
	}

	/** (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeName()
	 */
	@Override
	public String getNodeName() {
		return getResourceName();
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/** (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeCode()
	 */
	@Override
	public String getNodeCode() {
		return getResourceCode();
	}

	/** (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getParentNodeId()
	 */
	@Override
	public String getParentNodeId() {
		if(null != getParent()){
			return getParent().getResourceid();
		}
		return getParentId();
	}

	public int getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(int resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/** (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeLevel()
	 */
	@Override
	public Integer getNodeLevel() {		
		return getResourceLevel();
	}

}