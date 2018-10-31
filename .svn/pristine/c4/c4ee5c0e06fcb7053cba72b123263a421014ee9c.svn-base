package com.opensymphony.workflow.ui.model;

import java.util.Map;

/**
 * 流程图-状态模型
 * <code>StatusRect</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-3-23 下午04:28:01
 * @see 
 * @version 1.0
 */
public class WfStatusRect implements java.io.Serializable{

	private static final long serialVersionUID = 2395808223807896882L;

	public static String STATUSRECT_TYPE_START = "start";//开始
	public static String STATUSRECT_TYPE_TASK = "task";//step
	public static String STATUSRECT_TYPE_STATE = "state";//状态
	public static String STATUSRECT_TYPE_FORK = "fork";//分支
	public static String STATUSRECT_TYPE_JOIN = "join";//合并
	public static String STATUSRECT_TYPE_END = "end";;//结束
	
	
	//"rect4:{type:'start',text:{text:'开始'}, attr:{ x:409, y:10, width:50, height:50}, 
	//props:{text:{value:'开始'},temp1:{value:''},temp2:{value:''}}}
	private String type;//类型
	
	private Map<String,Object> text;//文本
	
	private Map<String, Object> attr;//属性
	
	private WfStatusProps props;//props

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

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
	 * @return the attr
	 */
	public Map<String, Object> getAttr() {
		return attr;
	}

	/**
	 * @param attr the attr to set
	 */
	public void setAttr(Map<String, Object> attr) {
		this.attr = attr;
	}

	/**
	 * @return the props
	 */
	public WfStatusProps getProps() {
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(WfStatusProps props) {
		this.props = props;
	}
	
	
}
