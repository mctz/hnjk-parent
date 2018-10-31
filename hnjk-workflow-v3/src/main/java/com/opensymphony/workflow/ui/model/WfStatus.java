package com.opensymphony.workflow.ui.model;

import java.util.Map;

/**
 * 流程图状态.
 * <code>WfStatus</code><p>
 * 流程图的模型：<p>
 * 视图：<p>
 *  - states <<流程节点>><p>
 *  	- rect <<节点描述>><p>
 *  - paths	<<流程路径>><p>
 *  	- path	<<路径描述>><p>
 *  - props	<<流程属性>><p>
 *  	- prop <<属性描述>><p>
 *  活动视图：<p>
 *  - activeRects	<<活动节点>><p>
 *  	- rect	<<活动节点描述>><p>
 *  - historyRects	<<历史节点>><p>
 *  	- rect	<<历史节点描述>><p>
 *  
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-3-30 上午10:34:52
 * @see 
 * @version 1.0
 */
public class WfStatus  implements java.io.Serializable{

	private static final long serialVersionUID = 4240311044825508515L;

	private Map<String,WfStatusRect> states;
	
	private Map<String,WfStatusPath> paths;
	
	private Map<String,WfStatusProps> props;

	
	/**
	 * @return the states
	 */
	public Map<String, WfStatusRect> getStates() {
		return states;
	}

	/**
	 * @param states the states to set
	 */
	public void setStates(Map<String, WfStatusRect> states) {
		this.states = states;
	}

	/**
	 * @return the paths
	 */
	public Map<String, WfStatusPath> getPaths() {
		return paths;
	}

	/**
	 * @param paths the paths to set
	 */
	public void setPaths(Map<String, WfStatusPath> paths) {
		this.paths = paths;
	}

	/**
	 * @return the props
	 */
	public Map<String, WfStatusProps> getProps() {
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(Map<String, WfStatusProps> props) {
		this.props = props;
	}

	
	
	
}
