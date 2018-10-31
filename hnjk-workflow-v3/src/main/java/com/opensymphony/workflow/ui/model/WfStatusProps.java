package com.opensymphony.workflow.ui.model;

import java.util.Map;

/**
 * 流程图 - 状态属性
 * <code>StatusProps</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-3-23 下午04:35:29
 * @see 
 * @version 1.0
 */
public class WfStatusProps implements java.io.Serializable{

	private static final long serialVersionUID = 6658989529756845304L;
	
	//props:{text:{value:'任务1'},assignee:{value:''},form:{value:''},desc:{value:''}}}
	
	private Map<String, Object> text;
	
	private Map<String, Object> assignee;
	
	private Map<String, Object> form;
	
	private Map<String, Object> desc;
	
	private Map<String, Object> name;
	
	private Map<String, Object> key;

	/**
	 * @return the text
	 */
	public Map<String, Object> getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Map<String, Object> text) {
		this.text = text;
	}

	/**
	 * @return the assignee
	 */
	public Map<String, Object> getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(Map<String, Object> assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the form
	 */
	public Map<String, Object> getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(Map<String, Object> form) {
		this.form = form;
	}

	/**
	 * @return the desc
	 */
	public Map<String, Object> getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(Map<String, Object> desc) {
		this.desc = desc;
	}

	/**
	 * @return the name
	 */
	public Map<String, Object> getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(Map<String, Object> name) {
		this.name = name;
	}

	/**
	 * @return the key
	 */
	public Map<String, Object> getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Map<String, Object> key) {
		this.key = key;
	}
	
	
}
