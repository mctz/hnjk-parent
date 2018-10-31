package com.hnjk.platform.taglib;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.CustomFormDefine;
import com.hnjk.platform.system.model.CustomFormFields;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.ICustomFormService;

import freemarker.template.Template;

/**
 * 自定义表单查看标签.
 * @author hzg
 * reedit gu
 */
public class CustomFormViewTag extends BaseTagSupport{
	
	private static final long serialVersionUID = -6759633400691598558L;
	
	private String formCode;//表单编码
	
	private String customDataId;//表单数据resourceid
	
	private String htmlFormId;//绑定数据的form元素id,默认为formCode的值
	
	private String storeDir;//附件存储目录
	
	private List<Attachs> attachList;//附件列表
	
	private Map<String, Collection<JsonModel>> customVar = new HashMap<String, Collection<JsonModel>>();//自定义变量，key 变量名,value变量值，一定是List
	
	@Override
	public int doEndTag() throws JspException {
		if(ExStringUtils.isNotBlank(formCode)){//根据表单编码，取出表单定义及数据
			ICustomFormService customFormService = (ICustomFormService)SpringContextHolder.getBean("customFormService");
			CustomFormDefine customFormDefine = customFormService.findUnique("from "+CustomFormDefine.class.getSimpleName()+" where isDeleted = ? and formCode = ? ", 0,formCode);
			
			try {
				if(customFormDefine == null) {
					throw new JspException("没有这个表单定义项:"+formCode);
				}
				Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"customformview.ftl");;
				Map root = new HashMap();
				root.put("baseUrl", getBaseUrl());
				root.put("form", customFormDefine);
				root.put("formMap", groupFields(customFormDefine));
				root.put("customVar", customVar);
				root.put("customDataId", customDataId);
				root.put("htmlFormId", ExStringUtils.defaultIfEmpty(htmlFormId, formCode));
				//处理字典值，生成字典select options
				Map<String, List<Dictionary>> dictMap = new HashMap<String, List<Dictionary>>();
				
				for(CustomFormFields field : customFormDefine.getFields()){
					if("dict".equals(field.getFormDomType())){
						List<Dictionary> children  = CacheAppManager.getChildren(field.getDictCode());
						List<Dictionary> dictList = new ArrayList<Dictionary>();
						for(Dictionary childDict : children){
							if(null != children && Constants.BOOLEAN_YES.equals(childDict.getIsUsed())){
								dictList.add(childDict);
							}
						}
						dictMap.put(field.getDictCode(), dictList);
					}
				}
				root.put("dictMap", dictMap);
				template.process(root, super.pageContext.getOut());
				
			} catch (Exception e) {
				logger.error("输出自定义表单出错："+e.fillInStackTrace());
			}
			
		}
		return EVAL_PAGE;
	}
	
	private Map<String,Set<CustomFormFields>> groupFields(CustomFormDefine customFormDefine){
		Map<String,Set<CustomFormFields>> map = new LinkedHashMap<String, Set<CustomFormFields>>();//改为LinkedHashMap，按分组顺序加载数据
		Set<CustomFormFields> fields = customFormDefine.getFields();
		
		if(null != fields && !fields.isEmpty()){			
			//获取
			IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");
			Map<String, Object> parameters = new HashMap<String, Object>();
			try {
				parameters.put("formid", customFormDefine.getResourceid());
				 List<Map<String, Object>>  result = jdbcDao.getBaseJdbcTemplate().findForListMap("select count(t.groupnum) as groupcount,t.groupnum,t.groupname from hnjk_sys_formfields t where t.formid=:formid group by t.groupnum,t.groupname order by t.groupnum",
						parameters);				
				
				if(null != result && !result.isEmpty()){//分组
					for(Map<String, Object> resultMap : result){//遍历，并分组
						int groupcount = ((BigDecimal)resultMap.get("groupcount")).intValue();
						Set<CustomFormFields> _fields = new LinkedHashSet<CustomFormFields>();
						if(groupcount >0){//若存在分组
							int groupnum = ((BigDecimal)resultMap.get("groupnum")).intValue();
							String groupname = resultMap.get("groupname").toString();						
							for(CustomFormFields field : fields ){
								if(field.getGroupNum() == groupnum){
									_fields.add(field);
								}
							}
							map.put(groupnum+"_"+groupname, _fields);//放入分组Map
						}else{//不存在分组
							_fields.addAll(fields);
							map.put(customFormDefine.getFormCode()+"_"+customFormDefine.getFormName(), _fields);//放入分组Map
						}
											
						
					}					
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return map;
	}
	
	/**
	 * @return the formCode
	 */
	public String getFormCode() {
		return formCode;
	}

	/**
	 * @param formCode the formCode to set
	 */
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	/**
	 * @return the customVar
	 */
	public Map<String, Collection<JsonModel>> getCustomVar() {
		return customVar;
	}

	/**
	 * @param customVar the customVar to set
	 */
	public void setCustomVar(Map<String, Collection<JsonModel>> customVar) {
		this.customVar = customVar;
	}
	
	public String getCustomDataId() {
		return customDataId;
	}
	
	public void setCustomDataId(String customDataId) {
		this.customDataId = customDataId;
	}
	
	public String getHtmlFormId() {
		return htmlFormId;
	}
	
	public void setHtmlFormId(String htmlFormId) {
		this.htmlFormId = htmlFormId;
	}


	/**
	 * @return the storeDir
	 */
	public String getStoreDir() {
		return storeDir;
	}

	/**
	 * @param storeDir the storeDir to set
	 */
	public void setStoreDir(String storeDir) {
		this.storeDir = storeDir;
	}

	/**
	 * @return the attachList
	 */
	public List<Attachs> getAttachList() {
		return attachList;
	}

	/**
	 * @param attachList the attachList to set
	 */
	public void setAttachList(List<Attachs> attachList) {
		this.attachList = attachList;
	}
	
	
	
}
