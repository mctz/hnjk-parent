package com.hnjk.security.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;

/**
 * 系统角色模型<code>Role</code>
 * <p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-10-30 上午10:25:30
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "hnjk_sys_roles")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends BaseModel implements Serializable {

	private static final long serialVersionUID = 2792629731265339761L;

	@Column(name = "roleCode", nullable = false, unique = true)
	private String roleCode;// 角色编码

	@Column(name = "roleName", nullable = false, unique = true)
	private String roleName;// 角色名称

	@ManyToOne(fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Role parent;// 父ID

	// 授权资源父子关系，形成树形结O
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder asc")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<Role> children = new LinkedHashSet<Role>(0);

	@Column(name = "roleDescript")
	private String roleDescript;// 角色描述

	// 角色与资源授权关系
	@ManyToMany(targetEntity = Resource.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE ,CascadeType.REFRESH}, fetch = FetchType.LAZY)	
	@JoinTable(name = "hnjk_sys_roleauthority", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "authorityId") })
	@OrderBy("showOrder asc")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@DeleteChild(deleteable=false)
	private Set<Resource> authoritys = new LinkedHashSet<Resource>(0);
	
	// 系统用户与角色关联 n:n
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hnjk_sys_roleusers", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "userId") })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@DeleteChild(deleteable=false)
	private Set<User> users = new LinkedHashSet<User>(0);

	@Column(name = "showOrder")
	private int showOrder = 0;// 排序号
	
	//3.1.2 新增角色模型，用来处理不同教学模式的情况
	@Column(name="ROLEMODULE")
	private String roleModule;
	
	/**3.1.6 新增角色帮助文档*/	
	@Transient	
	private String helpDocPath;
	
	@Transient	
	private String[] resourceFuncId;
	
	
	/**
	 * @return the helpDocPath
	 */
	public String getHelpDocPath() {
		return helpDocPath;
	}

	/**
	 * @param helpDocPath the helpDocPath to set
	 */
	public void setHelpDocPath(String helpDocPath) {
		this.helpDocPath = helpDocPath;
	}

	public String getRoleModule() {
		return roleModule;
	}

	public void setRoleModule(String roleModule) {
		this.roleModule = roleModule;
	}

	@Transient
	private String parentId;

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public Set<Resource> getAuthoritys() {
		return authoritys;
	}

	public void setAuthoritys(Set<Resource> authoritys) {
		this.authoritys = authoritys;
	}

	public Set<Role> getChildren() {
		return children;
	}

	public void setChildren(Set<Role> children) {
		this.children = children;
	}

	public Role getParent() {
		return parent;
	}

	public void setParent(Role parent) {
		this.parent = parent;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDescript() {
		return roleDescript;
	}

	public void setRoleDescript(String roleDescript) {
		this.roleDescript = roleDescript;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	@Override
	public String toString() {
		return this.getRoleName();
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String[] getResourceFuncId() {
		return resourceFuncId;
	}

	public void setResourceFuncId(String[] resourceFuncId) {
		this.resourceFuncId = resourceFuncId;
	}
		
}
