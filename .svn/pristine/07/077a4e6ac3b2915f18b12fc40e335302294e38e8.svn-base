package com.hnjk.platform.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.ReflectionUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.taglib.BaseTagSupport;
/**
 * 通过model指定属性生成带checkbox的select列表
 * <code>MultipleSelectModelTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-5-10 下午06:02:09
 * @see 
 * @version 1.0
 */
public class CheckboxSelectModelTag extends BaseTagSupport {
	
	private static final long serialVersionUID = 3930254130655318675L;

	private String id;
	private String name;
	private String classCss; // 样式相当html中class
	private String style;
	private String bindValue; // 绑定的值
	private String displayValue; // 显示的值
	private String modelClass; // 类
	private String condition; // 条件	
	private String orderBy; //排序字符串
	private String value;

	@Override
	public int doEndTag() throws JspException {			
		String zNodes = "";//ztree数据
		String names = "";
		List<String> nameList = new ArrayList<String>();//已选择名称
		List<String> valueList = ExStringUtils.isNotBlank(value)? Arrays.asList(value.split("\\,")):new ArrayList<String>();//已选择值
		
		SessionFactory sf = (SessionFactory) SpringContextHolder.getBean("sessionFactory");
		Session session = sf.openSession();
		try {
			Object objClass = ReflectionUtils.newInstance(modelClass); //初始化对象
			StringBuilder hql = new StringBuilder("from " + objClass.getClass().getName() + " where isDeleted=0");
			if(null!= condition && !"".equals(condition) && condition.length()>0){ //条件
				String[] conditions = condition.split(",");
				for(String str : conditions){
					String[] ss1 = str.split("<>");
					String[] ss2 = str.split("=");

					if(ss1.length>1 && !"''".equals(ss1[1])){
						hql.append(" and "+str); 
					} else if(ss2.length>1 && !"''".equals(ss2[1])){
						hql.append(" and "+str); 
					}else if(ss1.length==1 && ss2.length==1){
						hql.append(" and "+str);
					}
				}
			}
			if(orderBy!=null && orderBy.length()>0){ //排序
				hql.append(" order by ");
				hql.append(orderBy);
			}else{
				hql.append(" order by resourceid asc");				
			}
			Query query = session.createQuery(hql.toString()); //HQL查询
			List list = query.list();
			if(!list.isEmpty()){
				List<String> zNodesList = new ArrayList<String>();
				boolean isCheckAll = valueList.size()==list.size();
				zNodesList.add("{id:\"\","+(isCheckAll?"checked:true,":"")+"name:\"全部\"}");
				for(int index=0;index<list.size();index++){
					Object obj = list.get(index);
					//String bindObj = ReflectionUtils.invokeGetMethod(obj, bindValue).toString(); //反射取属性GET方法中值,如取父类中属性值，请覆盖父类属性中GET方法
					//String displayObj = ReflectionUtils.invokeGetMethod(obj, displayValue).toString();					
					//改用BeanWrapper方式取属性值,可直接取到父类的属性值
					BeanWrapper wrapper = new BeanWrapperImpl(obj);
					String bindObj = wrapper.getPropertyValue(bindValue).toString();
					String displayObj = wrapper.getPropertyValue(displayValue).toString();
					if(valueList.contains(bindObj)){
						nameList.add(displayObj);
						zNodesList.add("{id:\""+bindObj+"\",checked:true,name:\""+displayObj+"\"}");
					} else {
						zNodesList.add("{id:\""+bindObj+"\",name:\""+displayObj+"\"}");
					}
				}
				if(isCheckAll){
					nameList.clear();
					nameList.add("全部");
				}
				if(ExCollectionUtils.isNotEmpty(nameList)){
					names = ExStringUtils.join(nameList,",");
				}
				if(ExCollectionUtils.isNotEmpty(zNodesList)){
					zNodes = ExStringUtils.join(zNodesList,",");
				}
				zNodes = "["+zNodes+"]";
			}
		} catch (Exception e1) {
			logger.error("生成mutiple select列表出错：{}",e1.fillInStackTrace());
		}finally{
			session.close();
		}

		JspWriter writer = this.pageContext.getOut();
		try {
			writer.append(printMutipleSelect(zNodes,names));
		} catch (IOException e) {
			logger.error("生成mutiple select列表出错:{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}

	private String printMutipleSelect(String zNodes, String names) {
		StringBuffer sb = new StringBuffer();
		sb.append("<input id=\"_"+id+"_names\" name=\""+name+"Names\" type=\"text\" readonly=\"readonly\" ");
		sb.append(" value=\""+ExStringUtils.trimToEmpty(names)+"\" title=\""+ExStringUtils.trimToEmpty(names)+"\" ");
		if(ExStringUtils.isNotBlank(style)){
			sb.append(" style=\""+style+"\" ");
		}
		if(ExStringUtils.isNotBlank(classCss)){
			sb.append(" class=\""+classCss+"\" ");
		}
		sb.append(" />\n");		
		sb.append("<input id=\""+id+"\" name=\""+name+"\" type=\"hidden\" value=\""+ExStringUtils.trimToEmpty(value)+"\" />\n");
		
		sb.append(getScriptTemplate().replace("#id#", id).replace("#zNodes#", zNodes));	
		return sb.toString();
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassCss() {
		return classCss;
	}

	public void setClassCss(String classCss) {
		this.classCss = classCss;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getBindValue() {
		return bindValue;
	}

	public void setBindValue(String bindValue) {
		this.bindValue = bindValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	private static String getScriptTemplate(){
		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\">	 \n");		
		sb.append(" 	$(document).ready(function(){ \n");
		sb.append(" 		var _#id#_zNodes =#zNodes#; \n");
		sb.append(" 		var _#id#_setting; \n");
		sb.append(" 		_#id#_setting = {isSimpleData: true,treeNodeKey: \"id\",treeNodeParentKey: \"pId\", showLine: false, checkable: true, callback: { beforeClick: _#id#_zTreeBeforeClick, change: _#id#_zTreeChange } }; \n");
		sb.append(" 		$(\"#_#id#_bg\").remove(); \n");
		sb.append(" 		$(\"<div id=\\\"_#id#_bg\\\" class=\\\"checkboxselect\\\" ><ul id=\\\"_#id#_tree\\\" class=\\\"ztree\\\"></ul></div>\").appendTo(\"body\"); \n");
		sb.append(" 		var _#id#_zTree = $(\"#_#id#_tree\").zTree(_#id#_setting, _#id#_zNodes); \n");
		sb.append("         $(\"#_#id#_names\").bind(\"click\",_#id#_showTree); \n");
		sb.append(" 		function _#id#_zTreeBeforeClick(treeId, treeNode){ \n");
		sb.append(" 			$(\"#\"+treeNode.tId + \"_check\").click(); \n");
		sb.append(" 			return false; \n");
		sb.append(" 		} \n");
		sb.append(" 		function _#id#_zTreeChange(event, treeId, treeNode){ \n");
		sb.append(" 			var names = [], ids = []; \n");
		sb.append("             var node = _#id#_zTree.getNodeByParam('id', '');\n");
		sb.append(" 			if(treeNode.id==''){ \n");
		sb.append(" 				_#id#_zTree.checkAllNodes(treeNode.checked);\n");
		sb.append(" 			} else { \n");		
		sb.append("                 if(node.checked) {\n");
		sb.append("                 	node.checked = false;\n");
		sb.append("                		_#id#_zTree.updateNode(node);\n");
		sb.append("            		}\n");
		sb.append("             }\n");
		sb.append(" 			var treeNodes = _#id#_zTree.getCheckedNodes(true); \n");		
		sb.append(" 			for (var i = 0; i < treeNodes.length; i++) { \n");
		sb.append(" 				if(treeNodes[i].id!=''){  \n");
		sb.append(" 					names.push(treeNodes[i].name);  \n");
		sb.append(" 					ids.push(treeNodes[i].id); \n");
		sb.append(" 				}  \n");
		sb.append(" 			} \n");
		sb.append("				if(ids.length ==_#id#_zNodes.length-1){ \n");
		sb.append(" 			   	 node.checked = true;names = [];names.push(node.name);\n");
		sb.append(" 			   	 _#id#_zTree.updateNode(node);\n");
		sb.append(" 			} \n");
		sb.append(" 			$(\"#_#id#_names\").val(names.length>0?names.join(','):''); \n");
		sb.append(" 			$(\"#_#id#_names\").attr('title',names.length>0?names.join(','):''); \n");
		sb.append(" 			$(\"##id#\").val(ids.length>0?ids.join(','):''); \n");
		sb.append(" 		} \n");
		sb.append("     	function _#id#_showTree() { \n");
		sb.append(" 			var _#id#_Obj = $(\"#_#id#_names\"); \n");
		sb.append(" 			var _#id#_Offset = $(\"#_#id#_names\").offset(); \n");
		sb.append(" 			$(\"#_#id#_bg\").css({left:_#id#_Offset.left + \"px\", top:_#id#_Offset.top + _#id#_Obj.outerHeight() + \"px\",width:_#id#_Obj.outerWidth()+\"px\"}).show(); \n");
		sb.append(" 			$(\"body\").bind(\"mousedown\", _#id#_BodyDown); \n");
		sb.append(" 		} \n");
		sb.append("     	function _#id#_hideTree() { \n");
		sb.append(" 			$(\"#_#id#_bg\").hide(); \n");
		sb.append(" 			$(\"body\").unbind(\"mousedown\", _#id#_BodyDown); \n");
		sb.append(" 		} \n");
		sb.append("     	function _#id#_BodyDown(event) { \n");
		sb.append(" 			if (!(event.target.id == \"_#id#_names\" || event.target.id == \"_#id#_bg\" || $(event.target).parents(\"#_#id#_bg\").length>0)) { \n");
		sb.append(" 				_#id#_hideTree(); \n");
		sb.append(" 			} \n");
		sb.append(" 		} \n");
		sb.append(" 	}); \n");
		sb.append(" </script>");
		return sb.toString();
	}
}
