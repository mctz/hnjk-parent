package com.hnjk.extend.taglib.tree;


import java.util.List;

/**
 * <code>TreeNode</code>树节点.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-24 下午05:06:18
 * @see 
 * @version 1.0
 */
public class TreeNode {
	
	/**节点ID*/
	private String nodeId;
	/**节点名字*/
	private String nodeName;
	/**父节点ID*/
	private String parentNodeId;
	/**所在层级 */
	private int levelposition;
	/**是否叶子 */
	private boolean isLeaf=false;
	/** 是否有子节点 */
	private boolean hasChild=false;
	/**是否打开*/
	private boolean isOpen = false;
	/**节点链接*/
	private String href;

	public TreeNode(){
		
	}
	
	public TreeNode(String nodeId,String nodeName,String parentNodeId,Integer levelposition){
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.parentNodeId=parentNodeId;
		this.levelposition=levelposition;
	}
	
	public TreeNode(String nodeId,String nodeName,String parentNodeId,Integer levelposition,boolean isLeaf){
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.parentNodeId=parentNodeId;
		this.levelposition=levelposition;
		this.isLeaf=isLeaf;
		
	}
	
	public TreeNode(String nodeId,String nodeName,String parentNodeId,Integer levelposition,boolean isLeaf,String href){
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.parentNodeId=parentNodeId;
		this.levelposition=levelposition;
		this.isLeaf=isLeaf;
		this.href=href;
	}
	
	public TreeNode(String nodeId,String nodeName,String href,boolean isLeaf,boolean isOpen){
		this.nodeId=nodeId;
		this.nodeName=nodeName;		
		this.href=href;
		this.isOpen=isOpen;
	}
	
	/**
	 * 根据树节点列表构建树节点的json结构模型
	 * @param treeNodes
	 * @return
	 */
	public static String genTreeJson(List<TreeNode> treeNodes){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i=0;i<treeNodes.size();i++) {
			if (i > 0) {
				sb.append(",");
			}
			TreeNode node = treeNodes.get(i);
			sb.append(TreeNode.buildNodeString(node));
		}
		sb.append("]");
		return sb.toString();
	}

	public static String genZTreeJson(List<TreeNode> treeNodes){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i=0;i<treeNodes.size();i++) {
			if (i > 0) {
				sb.append(",");
			}
			TreeNode node = treeNodes.get(i);
			sb.append(TreeNode.buildNodeZtreeString(node));
		}
		sb.append("]");
		return sb.toString();
	}

	
	/**
	 * 生成节点json
	 * @param item
	 * @return
	 */
	public static String buildNodeString(TreeNode node) {
		StringBuilder sb = new StringBuilder();
		sb.append(" {");
		sb.append("  \"text\": \"" + node.getNodeName() + "\"");
		try {
			sb.append(",  \"id\":\"" + node.getNodeId() + "\"");
			if(node.hasChild){
				sb.append(",  \"hasChildren\":true");
			}else{
				sb.append(",  \"hasChildren\":false");
			}
			if(node.isLeaf){
				sb.append(",\"classes\":\"file\"");
			}else if(!node.hasChild&&!node.isLeaf){
				sb.append(",\"classes\":\"closefolder\"");
			}else{
				sb.append(",\"classes\":\"folder\"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
		sb.append(" }");
		return sb.toString();
	}
	
	public static String buildNodeZtreeString(TreeNode node) {
		StringBuilder sb = new StringBuilder();
		sb.append(" {");
		sb.append("  \"name\": \"" + node.getNodeName() + "\"");
		try {
			sb.append(",  \"ename\":\"" + node.getNodeId() + "\"");
			if(node.isOpen){
				sb.append(",  \"open\":true");
			}else{
				sb.append(",  \"open\":false");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
		sb.append(" }");
		return sb.toString();
	}
	
	
	/**
	 * 设置树列表是否有子节点
	 * @author cp
	 * @since:2009-4-14下午04:49:41
	 * @param treeNodes
	 */
	public static void setListHasChild(List treeNodes){
		TreeNode oldNode = null;
		for(int i=0;i<treeNodes.size();i++){
			TreeNode node = (TreeNode)treeNodes.get(i);
			if(oldNode ==null){
				oldNode = node;
				continue;
			}
			if(node.getLevelposition()>oldNode.getLevelposition()){
				oldNode.setHasChild(true);
			}else{
				oldNode.setHasChild(false);
			}
			oldNode = node;
		}
		oldNode.setHasChild(false);
	}
	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getParentNodeId() {
		return parentNodeId;
	}
	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public int getLevelposition() {
		return levelposition;
	}

	public void setLevelposition(int levelposition) {
		this.levelposition = levelposition;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	
}
