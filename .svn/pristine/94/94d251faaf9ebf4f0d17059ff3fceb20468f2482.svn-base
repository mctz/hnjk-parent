package com.hnjk.core.dao.model;

import java.util.LinkedHashSet;
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
@Table(name = "t_hnjk_sys_resources")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resource extends BaseTreeModel {

	private static final long serialVersionUID = -5396060046559398409L;
	
	public static String RESOURCE_CODE = "resourceCode";

	@Column(name = "resourceCode", nullable = false, unique = true)
	private String resourceCode;// 资源编码

	@Column(name = "resourceType", nullable = false)
	private String resourceType;// 资源类型

	@Column(name = "resourceName", nullable = false, unique = true)
	private String resourceName;// 资源名称

	@Column(name = "resourcePath", nullable = false, unique = true)
	private String resourcePath;// 资源路径	
	
	@Column(name = "resourceDescript")
	private String resourceDescript;// 资源描述

	@Column(name = "resourceLevel")
	private int resourceLevel = 0;// 资源层级
	
	@Column(name = "showOrder")
	private int showOrder;// 排序号

	@Transient
	private String parentId; // 临时父对象id
	
	/** hnjk core 1.0.2 新增 */
	@Column(name="STYLE")
	private String style;		// class值 样式
	
	@Column(name="JUMPURL")
	private String pageType;		// 页面类型
	
	@Column(name="JSFUNCTION")
	private String jsFunction;	// js函数名称  例如: add()

	@Column(name="ISChILD")
	private String isChild;//是否有子

		
	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceDescript() {
		return resourceDescript;
	}

	public void setResourceDescript(String resourceDescript) {
		this.resourceDescript = resourceDescript;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	
	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public int getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(int resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return this.getResourceName();
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	public String getJsFunction() {
		return jsFunction;
	}

	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}


	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getIsChild() {
		return isChild;
	}

	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeName()
	 */
	@Override
	public String getNodeName() {		
		return getResourceName();
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.BaseTreeModel#getNodeCode()
	 */
	@Override
	public String getNodeCode() {		
		return getResourceCode();
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
		return getResourceLevel();
	}

	
	
}