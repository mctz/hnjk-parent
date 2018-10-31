package com.hnjk.platform.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;

/**
 * 字典select标签
 * <code>CustomSelectTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-17 上午11:47:59
 * @see 
 * @version 1.0
 */
public class CustomSelectTag extends BaseTagSupport {

	private static final long serialVersionUID = 5140979292750090983L;

	private String id;

	private String name;

	private String classCss; // 样式相当html中class

	private String style;

	private String disabled;

	private String onblur;

	private String onchange;

	private String onclick;

	private String ondblclick;

	private String onfocus;
	
	private String dictionaryCode; // 字典编码

	private String value;
	
	private String validate; //验证类型
	
	private String mes; //验证信息
	
	private String choose="Y";
	
	private String size="";
	
	private String multiple;//多选列表
	
	private String filtrationStr;  //保留值 以逗号","分隔

	private String isSubOptionText;//是否需要截去一部分长度显示option的值
	
	//自定义select
	private String isUseCustom;//是否用自定义 Y为启用
	private String customValues; //自定义值
	private String customNames;//自定义显示值
	
	private String excludeValue;//不包含的值，以逗号","分隔
	
	private String orderType;//排序方式 （根据序号）asc|desc
	
	
	@Override
	public int doEndTag() throws JspException {
		int selectorSize = 50;//默认值，最多显示50个
		if(ExStringUtils.isNotBlank(size)) {
			selectorSize = Integer.parseInt(size);
		}
		StringBuilder sb = new StringBuilder("<select");
		if(name!=null && !"".equals(name)){
			if (ExStringUtils.isEmpty(id)) {
				sb.append(" id='" + name + "'");
			} else {
				sb.append(" id='" + id + "'");
			}
			sb.append(" name='"+name+"'");
		}
		if(classCss!=null && !"".equals(classCss)){
			sb.append(" class='"+classCss+"'");
		}
		if(style!=null && !"".equals(style)){
			sb.append(" style='"+style+"'");
		}
		if(disabled!=null && !"".equals(disabled) && ("true".equals(disabled) || "disabled".equals(disabled))){
			sb.append(" disabled='"+disabled+"'");
		}
		if(onblur!=null && !"".equals(onblur)){
			sb.append(" onblur='"+onblur+"'");
		}
		if(onchange!=null && !"".equals(onchange)){
			sb.append(" onchange='"+onchange+"'");
		}
		if(onclick!=null && !"".equals(onclick)){
			sb.append(" onclick='"+onclick+"'");
		}
		if(ondblclick!=null && !"".equals(ondblclick)){
			sb.append(" ondblclick='"+ondblclick+"'");
		}
		if(onfocus!=null && !"".equals(onfocus)){
			sb.append(" onfocus='"+onfocus+"'");
		}
		if (validate != null && !"".equals(validate)) {
			sb.append(" validate='" + validate + "'");
		}
		if (mes != null && !"".equals(mes)) {
			sb.append(" mes='" + mes + "'");
		}
//		if (ExStringUtils.isNotEmpty(size)) {
//			sb.append(" size='" + size + "'");
//		}
		if (multiple != null && !"".equals(multiple)) {
			sb.append(" multiple='" + multiple + "'");
		}
		sb.append(" >");
		if("Y".equals(choose)) {
			sb.append("<option value=''>请选择</option>");
		}
		
		if("Y".equals(isUseCustom)){//用自定义生成select
			System.out.println("customNames==="+customNames+"|customValues===="+customValues);
			if(ExStringUtils.isNotBlank(customNames) && ExStringUtils.isNotBlank(customValues)){
				String[] cst_values = customValues.split(",");
				String[] cst_names = customNames.split(",");
				if(cst_names.length == cst_values.length){
					try {
						for(int i = 0 ; i < cst_values.length && i<selectorSize; i++){
							sb.append("<option value='"+cst_values[i]+"'");
							if(value!=null && !"".equals(value)){ //初始化默认值
								if(value.equals(cst_values[i])) {
									sb.append(" selected='selected'");
								}
							}
							sb.append(">");
							//sb.append(childDict.getDictName());
							if("Y".equals(isSubOptionText)&&cst_names[i].length()>10) {
								sb.append(cst_names[i].substring(0, 9)+"...");
							}else if("value-code".equals(isSubOptionText)){
								sb.append(cst_names[i]+"-"+cst_values[i]);
							}else{
								sb.append(cst_names[i]);
							}	
							sb.append("</option>");
						}
					} catch (Exception e) {
						logger.error("自定义生成select列表出错：{}",e.fillInStackTrace());
					}
				}
			}
		}else{
			if(dictionaryCode!=null && !"".equals(dictionaryCode)){
				//从缓存中获取字典对象
				List<Dictionary> children  = CacheAppManager.getChildren(dictionaryCode);
				if(children != null){
					List<String> excludeValueList = new ArrayList<String>();
					List<String> filtrationStrList = new ArrayList<String>();
					if(ExStringUtils.isNotBlank(filtrationStr)){
						filtrationStrList = Arrays.asList(filtrationStr.split(","));
					}
					if(ExStringUtils.isNotBlank(excludeValue)){
						excludeValueList = Arrays.asList(excludeValue.split(","));
					}
					if(orderType!=null && "desc".equals(orderType)){
						for (int i = children.size()-1,j=0; i >=0 && j<selectorSize; i--,j++) {
							Dictionary childDict = children.get(i);
							//保留值
							/*if(ExStringUtils.isNotBlank(filtrationStr) && filtrationStr.indexOf(childDict.getDictValue())<0){							
								continue;							
							}*/
							if(filtrationStrList.size()>0 && !filtrationStrList.contains(childDict.getDictValue())){
								continue;							
							}
							//不包含的值(以不包含的值为准)
							if(excludeValueList.size()>0 && excludeValueList.contains(childDict.getDictValue())){
								continue;							
							}
							if(Constants.BOOLEAN_YES.equals(childDict.getIsUsed())){ //启用						
								sb.append("<option value='"+ExStringUtils.stripToEmpty(childDict.getDictValue())+"'");
								if(value!=null && !"".equals(value)){ //初始化默认值
									if(value.equals(childDict.getDictValue())) {
										sb.append(" selected='selected'");
									}
								}else if(ExStringUtils.isNotEmpty(childDict.getIsDefault()) && Constants.BOOLEAN_YES.equals(childDict.getIsDefault())){
									sb.append(" selected='selected'");						
								}
								sb.append(">");
								//sb.append(childDict.getDictName());
								if("Y".equals(isSubOptionText)&&childDict.getDictName().length()>10) {
									sb.append(childDict.getDictName().substring(0, 9)+"...");
								}else if("value-code".equals(isSubOptionText)){
									sb.append(childDict.getDictName()+"-"+childDict.getDictValue());
								}else{
									sb.append(childDict.getDictName());
								}	
								sb.append("</option>");
							}
						}
					}else {
						for(int i=0;i<children.size() && i<selectorSize;i++){
							Dictionary childDict = children.get(i);
							//保留值
							/*if(ExStringUtils.isNotBlank(filtrationStr) && filtrationStr.indexOf(childDict.getDictValue())<0){							
								continue;							
							}*/
							if(filtrationStrList.size()>0 && !filtrationStrList.contains(childDict.getDictValue())){
								continue;							
							}
							//不包含的值(以不包含的值为准)
							if(excludeValueList.size()>0 && excludeValueList.contains(childDict.getDictValue())){
								continue;							
							}
							if(Constants.BOOLEAN_YES.equals(childDict.getIsUsed())){ //启用						
								sb.append("<option value='"+ExStringUtils.stripToEmpty(childDict.getDictValue())+"'");
								if(value!=null && !"".equals(value)){ //初始化默认值
									if(value.equals(childDict.getDictValue())) {
										sb.append(" selected='selected'");
									}
								}else if(ExStringUtils.isNotEmpty(childDict.getIsDefault()) && Constants.BOOLEAN_YES.equals(childDict.getIsDefault())){
									sb.append(" selected='selected'");						
								}
								sb.append(">");
								//sb.append(childDict.getDictName());
								if("Y".equals(isSubOptionText)&&childDict.getDictName().length()>10) {
									sb.append(childDict.getDictName().substring(0, 9)+"...");
								}else if("value-code".equals(isSubOptionText)){
									sb.append(childDict.getDictName()+"-"+childDict.getDictValue());
								}else{
									sb.append(childDict.getDictName());
								}	
								sb.append("</option>");
							}
						}
					}
					
				}			
			}
		}
		
		sb.append("</select>");
		JspWriter writer = this.pageContext.getOut();
		try {
			writer.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
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

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getOnblur() {
		return onblur;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public String getOnfocus() {
		return onfocus;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDictionaryCode() {
		return dictionaryCode;
	}

	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	
	public String getIsSubOptionText() {
		return isSubOptionText;
	}

	public void setIsSubOptionText(String isSubOptionText) {
		this.isSubOptionText = isSubOptionText;
	}
	public String getFiltrationStr() {
		return filtrationStr;
	}

	public void setFiltrationStr(String filtrationStr) {
		this.filtrationStr = filtrationStr;
	}

	public String getCustomValues() {
		return customValues;
	}

	public void setCustomValues(String customValues) {
		this.customValues = customValues;
	}

	public String getCustomNames() {
		return customNames;
	}

	public void setCustomNames(String customNames) {
		this.customNames = customNames;
	}

	public String getIsUseCustom() {
		return isUseCustom;
	}

	public void setIsUseCustom(String isUseCustom) {
		this.isUseCustom = isUseCustom;
	}

	public String getExcludeValue() {
		return excludeValue;
	}

	public void setExcludeValue(String excludeValue) {
		this.excludeValue = excludeValue;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
}
