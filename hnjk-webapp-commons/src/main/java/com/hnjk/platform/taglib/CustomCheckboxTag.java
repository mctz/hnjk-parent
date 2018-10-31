package com.hnjk.platform.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;

/**
 * 自定义CheckBox
 * <code>CustomCheckboxTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-14 下午03:38:11
 * @see 
 * @version 1.0
 */
public class CustomCheckboxTag extends BaseTagSupport {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String classCss; // 样式相当html中class

	private String disabled;

	private String readonly;

	private String style;

	private String onblur;

	private String onchange;

	private String onclick;

	private String ondblclick;

	private String onfocus;

	private String dictionaryCode; // 字典编码

	private String value;

	private String filtrationStr;  //需要过滤的值选项 以逗号","分隔
	
	private String validate; // 验证类型

	private String mes; // 验证信息
	
	private String inputType = "checkbox";
	
	private String displayDirection="horizontal";//横向: horizontal 竖向：vertical

	@Override
	public int doEndTag() throws JspException {
		
		StringBuilder sb = new StringBuilder();
		
		if(dictionaryCode!=null && !"".equals(dictionaryCode)){
			//从缓存中获取字典对象
			List<Dictionary> children  = CacheAppManager.getChildren(dictionaryCode);
			if(children != null){
				int index = 0;
				for(Dictionary childDict : children){
					//保留值
					if(ExStringUtils.isNotBlank(filtrationStr) && filtrationStr.indexOf(childDict.getDictValue())<0){						
						continue;						
					}
					if(childDict.getIsUsed().equals(Constants.BOOLEAN_YES)){ //是否启用
						
						sb.append("<input type='"+inputType+"'");
						if(ExStringUtils.isNotEmpty(id)) {
							sb.append(" id='"+id+"'");
						}
						if(ExStringUtils.isNotEmpty(name)) {
							sb.append(" name='"+name+"'");
						}
						
						sb.append(" value='"+ExStringUtils.stripToEmpty(childDict.getDictValue())+"'");						
						if(ExStringUtils.isNotEmpty(value)){ //初始化默认值
							String[] values = value.split(",");
							if(values.length > 1){
								for(String str : values){
									if(childDict.getDictValue().equals(str)) {
										sb.append(" checked='checked'");
									}
								}
							}else{
								if(childDict.getDictValue().equals(value)) {
									sb.append(" checked='checked'");
								}
							}
						}else if(ExStringUtils.isNotEmpty(childDict.getIsDefault()) && childDict.getIsDefault().equals(Constants.BOOLEAN_YES)){
							sb.append(" checked='checked'");						
						}
						if(ExStringUtils.isNotEmpty(classCss)) {
							sb.append(" class='"+classCss+"'");
						}
						if(ExStringUtils.isNotEmpty(disabled)) {
							sb.append(" disabled='"+disabled+"'");
						}
						if(ExStringUtils.isNotEmpty(readonly)) {
							sb.append(" readonly='"+readonly+"'");
						}
						if(ExStringUtils.isNotEmpty(style)) {
							sb.append(" style='"+style+"'");
						}
						if(ExStringUtils.isNotEmpty(onblur)) {
							sb.append(" onblur='"+onblur+"'");
						}
						if(ExStringUtils.isNotEmpty(onchange)) {
							sb.append(" onchange='"+onchange+"'");
						}
						if(ExStringUtils.isNotEmpty(onclick)) {
							sb.append(" onclick='"+onclick+"'");
						}
						if(ExStringUtils.isNotEmpty(ondblclick)) {
							sb.append(" ondblclick='"+ondblclick+"'");
						}
						if(ExStringUtils.isNotEmpty(onfocus)) {
							sb.append(" onfocus='"+onfocus+"'");
						}
						if(++index==children.size()){
							if(ExStringUtils.isNotEmpty(validate)) {
								sb.append(" validate='"+validate+"'");
							}
							if(ExStringUtils.isNotEmpty(mes)) {
								sb.append(" mes='"+mes+"'");
							}
						}
						sb.append(" >");
						sb.append(childDict.getDictName());
						sb.append("&nbsp;");
						if (ExStringUtils.isNotBlank(displayDirection)&&"vertical".equals(displayDirection)) {
							sb.append("</br>");
						}
					}
				}
			}
		}
		JspWriter writer = this.pageContext.getOut();
		try {
			writer.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getFiltrationStr() {
		return filtrationStr;
	}

	public void setFiltrationStr(String filtrationStr) {
		this.filtrationStr = filtrationStr;
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

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

	public String getDictionaryCode() {
		return dictionaryCode;
	}

	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getDisplayDirection() {
		return displayDirection;
	}

	public void setDisplayDirection(String displayDirection) {
		this.displayDirection = displayDirection;
	}
	
}	
