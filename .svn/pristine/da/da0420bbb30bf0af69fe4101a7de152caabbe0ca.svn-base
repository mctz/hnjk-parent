package com.hnjk.extend.taglib.tree;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.util.StringUtils;


/**
 *  <code>TreeViewTag</code>Tree view标签.<p>
 *  特点：可以记忆每次点击的状态.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-24 下午05:06:50
 * @see 
 * @version 1.0
 */
public class TreeViewTag extends TagSupport{
	
	@SuppressWarnings("unused")
	private static final String css_folder = "folder";
		
	private static final long serialVersionUID = -2205235159213413593L;
	
	private String defaultTree;

	private List nodes; //节点列表
	
	private String checkbox;//是否加入checkBox
	
	private String radio;//是否加入radio
	
	private String id;//树的id
	
	private String css;// 树的样式
	
	private int expandLevel=0; // 默认展开到几层
	

	/**
	 * 生成标签的html代码
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			//List<TreeNode> treeNodes=setIsLeafNode();
			TreeNode.setListHasChild(nodes);
			StringBuffer sb = new StringBuffer();
			int oldleave = -1;
			sb.append("<div class=\"treediv\">");
			sb.append("<ul id=\""+id+"\" class=\"filetree\">");
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
				if(node.getLevelposition()<=expandLevel){
					sb.append("<li class=\"collapsable\">");
				}else{
					sb.append("<li class=\"closed\">");
				}
				//设置样式
				if(css!=null&&css.equals(css_folder)){
					if(node.isLeaf()){						
					sb.append("<span class=\"file\">");
					}
					else if(node.isHasChild()){
						sb.append("<span class=\"folder\">");
					}
					else{
						sb.append("<span class=\"closefolder\">");
					}
				}else{
					sb.append("<span>");
				}
				// 设置是否有checkbox、radio
				if(StringUtils.hasText(checkbox))
				{
					sb.append("<input type=\"checkbox\" name=\""+checkbox+"\" value=\""+node.getNodeId()+"\""+"/>");
				}else if(StringUtils.hasText(radio)){
					sb.append("<input type=\"radio\" name=\""+radio+"\" value=\""+node.getNodeId()+"\""+"/>");
				}
				sb.append("<span "+"nodeId=\""+node.getNodeId()+"\""+" class=\"itemtext\">");
				//<a href='?"+node.getHref()+"'>"" node.getNodeName()+"</a>
				if(StringUtils.hasText(node.getHref())){
					sb.append("<a href='#"+node.getNodeId()+"' onclick=\""+node.getHref()+"\" title='"+node.getNodeName()+"'>"+node.getNodeName()+"</a>");
				}else{
					sb.append(node.getNodeName());
				}
				sb.append("</span></span>\n");
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
			sb.append("</div>");
			super.pageContext.getOut().print(sb.toString().replaceAll("<ul>\n</ul>\n", ""));
			
		} catch (IOException e) {			
			e.printStackTrace();
			
		}
		return super.doStartTag();
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}
	

	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public int getExpandLevel() {
		return expandLevel;
	}

	public void setExpandLevel(int expandLevel) {
		this.expandLevel = expandLevel;
	}

	public String getDefaultTree() {
		return defaultTree;
	}

	public void setDefaultTree(String defaultTree) {
		this.defaultTree = defaultTree;
	}

}
