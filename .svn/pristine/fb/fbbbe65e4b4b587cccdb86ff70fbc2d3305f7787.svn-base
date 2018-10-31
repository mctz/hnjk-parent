package com.hnjk.extend.taglib.tree;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseTreeModel;


/**
 * 
 * <code>TreeBuilder</code><p>
 * 构建数据树.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-6-28 下午07:50:11
 * @see 
 * @version 1.0
 */
public class TreeBuilder {

	/**
	 * 构造数据树 (NodeId为resourceid)
	 * @param treeList 
	 * @param rootNodeCode
	 * @param checkedValues
	 * @return
	 */
	public static ZTreeNode buildTreeById(List<? extends BaseTreeModel> treeList,String rootNodeCode,String checkedValues){
		ZTreeNode zTreeNode = null;		
		ZTreeNode zTreeNode1 = null;	
		
		Map<String,ZTreeNode> map = new HashMap<String, ZTreeNode>();
		boolean checked = false;
		for(BaseTreeModel treeNode:treeList){	
			if(ExStringUtils.isNotEmpty(checkedValues) && checkedValues.indexOf(treeNode.getNodeId()) >=0){
				checked = true;
			}else{
				checked = false;
			}
			if(treeNode.getNodeCode().equals(rootNodeCode)){				
				zTreeNode =  new ZTreeNode(treeNode.getNodeName(),treeNode.getNodeId(),treeNode.getParentNodeId(),true,checked,treeNode.getNodeLevel(),new LinkedHashSet<ZTreeNode>());
				map.put(treeNode.getNodeId(), zTreeNode);
			}else {
				if(null!=map.get(treeNode.getParentNodeId())){
					ZTreeNode parentOrg = map.get(treeNode.getParentNodeId());
					zTreeNode1 =  new ZTreeNode(treeNode.getNodeName(),treeNode.getNodeId(),treeNode.getParentNodeId(),true,checked,treeNode.getNodeLevel(),new LinkedHashSet<ZTreeNode>());
					map.put(treeNode.getNodeId(), zTreeNode1);
					parentOrg.getNodes().add(zTreeNode1);				
				}
			}	
		}
		map = null;
		zTreeNode1 = null;
		return zTreeNode;
	}
	
	/**
	 * 构造数据树(NodeId 为编码)
	 * @param treeList
	 * @param rootNodeCode
	 * @param checkedCodes
	 * @return
	 */
	public static ZTreeNode buildTreeByCode(List<? extends BaseTreeModel> treeList,String rootNodeCode,String checkedCodes){
		ZTreeNode zTreeNode = null;		
		ZTreeNode zTreeNode1 = null;	
		
		Map<String,ZTreeNode> map = new HashMap<String, ZTreeNode>();
		boolean checked = false;
		for(BaseTreeModel treeNode : treeList){	
			if(ExStringUtils.isNotEmpty(checkedCodes) && checkedCodes.indexOf(treeNode.getNodeCode()) >=0){
				checked = true;
			}else{
				checked = false;
			}
			if(treeNode.getNodeCode().equals(rootNodeCode)){				
				zTreeNode =  new ZTreeNode(treeNode.getNodeName(),treeNode.getNodeCode(),treeNode.getParentNodeId(),true,checked,treeNode.getNodeLevel(),new LinkedHashSet<ZTreeNode>());
				map.put(treeNode.getNodeId(), zTreeNode);
			}else {
				if(null!=map.get(treeNode.getParentNodeId())){
					ZTreeNode parentOrg = map.get(treeNode.getParentNodeId());
					zTreeNode1 =  new ZTreeNode(treeNode.getNodeName(),treeNode.getNodeCode(),treeNode.getParentNodeId(),true,checked,treeNode.getNodeLevel(),new LinkedHashSet<ZTreeNode>());
					map.put(treeNode.getNodeId(), zTreeNode1);
					parentOrg.getNodes().add(zTreeNode1);				
				}
			}	
		}
		map = null;
		zTreeNode1 = null;
		return zTreeNode;
	}
}
