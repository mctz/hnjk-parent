package com.hnjk.extend.taglib.tree;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.util.StringUtils;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 普通数据树标签
 * <code>Tree</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-7 下午04:37:25
 * @see 
 * @version 1.0
 */
public class TreeTag extends TagSupport{
	
	
	private static final long serialVersionUID = 7252377804904677598L;

	private String defaultTree;

	private List nodes; //节点列表
	
	private String isCheckbox;//是否加入checkBox	
	
	private String id;//树的id
	
	private String isExpand;//是否展开数
	
	private String oncheck;//点击checkbox时触发的事件
	

	/**
	 * 生成标签的html代码
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
		
			TreeNode.setListHasChild(nodes);
			StringBuffer sb = new StringBuffer();
			int oldleave = -1;
		
			sb.append("<ul id=\""+id+"\" class=\"tree treeFolder");
			if("Y".equals(isCheckbox)){
				sb.append(" treeCheck ");
			}
			
			if("Y".equals(isExpand)){
				sb.append("expand\"");
			}else {
				sb.append("collapse\"");
			}
			
			if(!ExStringUtils.isEmpty(oncheck)){
				sb.append(" oncheck ='"+oncheck+"'");
			}
			sb.append(">");
			for(int i=0;i<nodes.size();i++){
				TreeNode node = (TreeNode)nodes.get(i);
				// 当新节点的层次小于或者等于上一个节点层次时，补回树节点的结束标签（</ul></li>）
				if(oldleave>=node.getLevelposition()){
					int dif =  oldleave -node.getLevelposition();
					sb.append("</ul>\n");
					sb.append("</li>\n");
					for(int j=0;j<dif;j++){
						sb.append("</ul>\n");
						sb.append("</li>\n");
					}
				}
				//设置要展开的层次：如果当前的节点的层次小于或等于要展开的层次，便设置为展开，否则便设置为收缩
				
					sb.append("<li>");
				
			
				if(StringUtils.hasText(node.getHref())){
					sb.append("<a href='#' onclick=\""+node.getHref()+"\"  tname='resourceid' tvalue=\""+node.getNodeId()+"\">"+node.getNodeName()+"</a>");
				}else{
					sb.append(node.getNodeName());
				}
			
				sb.append("<ul>\n");
				oldleave=node.getLevelposition();
			}
			sb.append("</ul>\n");
			sb.append("</li>\n");
			if(oldleave>0){
				for(int i=0;i<oldleave;i++){
					sb.append("</ul>\n");
					sb.append("</li>\n");
				}
			}
			sb.append("</ul>");
			//sb.append("</div>");
			super.pageContext.getOut().print(sb.toString().replaceAll("<ul>\n</ul>\n", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}
	

	public void setIsCheckbox(String isCheckbox) {
		this.isCheckbox = isCheckbox;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}



	public String getDefaultTree() {
		return defaultTree;
	}

	public void setDefaultTree(String defaultTree) {
		this.defaultTree = defaultTree;
	}

	public List getNodes() {
		return nodes;
	}

	public void setIsExpand(String isExpand) {
		this.isExpand = isExpand;
	}

	public void setOncheck(String oncheck) {
		this.oncheck = oncheck;
	}
	
	
}
