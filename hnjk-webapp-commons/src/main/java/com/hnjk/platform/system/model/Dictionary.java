package com.hnjk.platform.system.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;

/**
 * 数据字典 <code>Dictionary</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-15 下午05:38:41
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "HNJK_SYS_DICT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dictionary extends BaseModel {

	private static final long serialVersionUID = -8507918351462665252L;

	@Column(name = "DICTCODE", unique = true, nullable = false)
	private String dictCode; // 字典编码

	@Column(name = "DICTNAME", nullable = false)
	private String dictName; // 字典名

	@Column(name = "DICTVALUE")
	private String dictValue; // 字典值

	@Column(name = "MODULE")
	private String module; // 所属模块

	@Column(name = "ISUSED")
	private String isUsed = Constants.BOOLEAN_YES; // 是否启用

	@Column(name = "ISDEFAULT")
	private String isDefault = Constants.BOOLEAN_YES;// 是否默认值

	@Column(name = "ISLOADCACHE")
	private String isLoadCache = Constants.BOOLEAN_YES; // 是否载入内存

	@Column(name = "SHOWORDER")
	private Integer showOrder; // 排序号

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Dictionary parentDict;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentDict")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder,dictValue asc")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Dictionary> dictSets = new HashSet<Dictionary>(0);

	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();
	
	@Transient
	private String fileName;
	
	public Dictionary() {
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public Dictionary getParentDict() {
		return parentDict;
	}

	public void setParentDict(Dictionary parentDict) {
		this.parentDict = parentDict;
	}

	public Set<Dictionary> getDictSets() {
		return dictSets;
	}

	public void setDictSets(Set<Dictionary> dictSets) {
		this.dictSets = dictSets;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getIsLoadCache() {
		return isLoadCache;
	}

	public void setIsLoadCache(String isLoadCache) {
		this.isLoadCache = isLoadCache;
	}

	public List<Attachs> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
