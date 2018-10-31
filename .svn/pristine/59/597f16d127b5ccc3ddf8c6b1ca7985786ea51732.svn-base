package com.hnjk.extend.taglib.tree;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用于生成ZTree数据树的节点模型
 * @author hzg
 *
 */
public class ZTreeNode {

	private String name;//节点名称
	
	private String id;//节点别名(用作ID)
	
	private boolean open;//是否打开
	
	private String parentId ;//父ID级别
	
	private boolean checked;//是否选择	
	
	private Integer level;//层级
	
	private Set<ZTreeNode> nodes = new LinkedHashSet<ZTreeNode>(0);//子节点
	
	private Map<String, Object> exAttribute = new HashMap<String, Object>();//扩展属性，key - value 形式
	

	public ZTreeNode(){}
	
	/**
	 * 常规（自己写回调）
	 * @param name
	 * @param id
	 * @param parentId
	 * @param open
	 * @param checked
	 * @param level
	 * @param nodes
	 */
	public ZTreeNode(String name,String id,String parentId,boolean open,boolean checked,int level,Set<ZTreeNode> nodes){
		this.name = name;	
		this.id = id;
		this.parentId = parentId;
		this.open = open;
		this.checked = checked;
		this.level = level;
		this.nodes = nodes;	
	}
	
	/**
	 * 常规（自己写回调）
	 * @param name
	 * @param id
	 * @param parentId
	 * @param open
	 * @param checked
	 * @param level
	 * @param nodes
	 * @param exAttribute 节点扩展属性
	 */
	public ZTreeNode(String name,String id,String parentId,boolean open,boolean checked,int level, 
			Set<ZTreeNode> nodes,Map<String, Object> exAttribute){
		this.name = name;	
		this.id = id;
		this.parentId = parentId;
		this.open = open;
		this.checked = checked;
		this.level = level;
		this.nodes = nodes;
		this.exAttribute = exAttribute;
	}
	
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	
	

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public Set<ZTreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(Set<ZTreeNode> nodes) {
		this.nodes = nodes;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the exAttribute
	 */
	public Map<String, Object> getExAttribute() {
		return exAttribute;
	}

	/**
	 * @param exAttribute the exAttribute to set
	 */
	public void setExAttribute(Map<String, Object> exAttribute) {
		this.exAttribute = exAttribute;
	}

	
	
}
