package com.hnjk.platform.taglib;

import java.util.List;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.User;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.hibernate3.HibernateStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateWorkflowEntry;

/**
 * 自定义标签（业务模块）
 * <code>JstlCustomFunction</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-17 下午04:34:50
 * @see 
 * @version 1.0
 */
public class JstlCustomFunction {
	
	/**
	 * 通过字典编码与值查找字典名称
	 * @param dictionaryCode 字典码
	 * @param value 字典值
	 * @return
	 */
	public static String dictionaryCode2Value(String dictionaryCode,String value){
		//从缓存中读取数据
		List<Dictionary> children  = CacheAppManager.getChildren(dictionaryCode);
		String values              = "";
		if(null != children ){
			for(Dictionary childDict : children){
				if(Constants.BOOLEAN_YES.equals(childDict.getIsUsed())){ //启用
					if(ExStringUtils.isNotEmpty(value)){ //默认值
						/*if(value.equals(childDict.getDictValue())){ //相等返回字典名
							return childDict.getDictName();
						}*/
						//if(value.split("\\,").length>0){//如果传入的多个值，则遍历
							for(int i=0;i<value.split("\\,").length;i++){
								if(value.split("\\,")[i].equals(childDict.getDictValue())){
									values += childDict.getDictName();
									if(i<value.split("\\,").length-1){
										values += ",";
									}
								}								
							}
							
						//}						
					}
				}
			}
		}
		//if (values.length()>0) {
		//	return values.substring(1);		
		//}else {
			return values;		
		//}
		
	}
	/**
	 * 通过字典编码与字典名查找值
	 * @param dictionaryCode 字典码
	 * @param name 字典名
	 * @return
	 */
	public static String dictionaryCode2Name(String dictionaryCode,String name){
		//从缓存中读取数据
		List<Dictionary> children  = CacheAppManager.getChildren(dictionaryCode);
		String values              = "";
		if(null != children ){
			for(Dictionary childDict : children){
				if(Constants.BOOLEAN_YES.equals(childDict.getIsUsed())){ //启用
					if(ExStringUtils.isNotEmpty(name)){ //默认值
						/*if(value.equals(childDict.getDictValue())){ //相等返回字典名
							return childDict.getDictName();
						}*/
						//if(value.split("\\,").length>0){//如果传入的多个值，则遍历
							for(int i=0;i<name.split("\\,").length;i++){
								if(name.split("\\,")[i].equals(childDict.getDictName().trim())){
									values += ExStringUtils.isEmpty(childDict.getDictValue())?"":childDict.getDictValue();
									if(i<name.split("\\,").length-1){
										values += ",";
									}
								}								
							}
							
						//}						
					}
				}
			}
		}
		//if (values.length()>0) {
		//	return values.substring(1);		
		//}else {
			return values;		
		//}
		
	}
	/**
	 * 传递一个资源编码,判断当前登录用户是否有权限
	 * @param funcCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasAuth(String funcCode) {
		// 用户有权限的id		
		//Map<String, Object> userMap = UserDetailsServiceImpl.getUserMap();
		User user = SpringSecurityHelper.getCurrentUser();
		//if(null != userMap){
			String rightIds = user.getUserRightsIds();
			List<Resource> all = (List<Resource>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).get(CacheSecManager.CACHE_SEC_RESOURCE);
			for(Resource  r : all) {
				if(funcCode.equalsIgnoreCase(r.getResourceCode())) {
					return rightIds.indexOf(r.getResourceid())>=0;
				}
			}
		//}
		
		return false;
	}
	
	/**
	 * 将组织|角色|用户的id 转换为名称 用“,”隔开
	 * @param type --> org | user | role
	 * @param ids
	 * @return
	 */
	public static String ids2Names(String type,String ids) {
		return CacheSecManager.ids2names(type, ids);
	}
	/**
	 * 资源路径转换为资源名称
	 * @param resourcePath
	 * @return
	 */
	public static String resourcePath2Names(String resourcePath){
		@SuppressWarnings("unchecked")
		List<Resource> all = (List<Resource>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).get(CacheSecManager.CACHE_SEC_RESOURCE);
		for(Resource  r : all) {
			if(resourcePath.indexOf(r.getResourcePath())>-1){
				if(null != r.getParent()){
					return r.getParent().getResourceName()+">"+r.getResourceName();
				}else{
					return r.getResourceName();
				}
				
			}
		}
		return "其他";
	}
	
	/**
	 * 通过Step 获取流程相关参数
	 * @return
	 * @throws Exception
	 */
	public static String wfAttr(HibernateStep step, String attrName) throws Exception{
		Workflow wf = new BasicWorkflow(SpringSecurityHelper.getCurrentUser().getResourceid());
		HibernateWorkflowEntry entry = step.getEntry();
		String workflowname = entry.getWorkflowName();
		WorkflowDescriptor wfdescriptor = wf.getWorkflowDescriptor(workflowname);
		
		if("wfname".equalsIgnoreCase(attrName)) {//获取流程定义名称
			Object wfname = wfdescriptor.getMetaAttributes().get("name");
			return wfname == null ? "" : wfname.toString();
		}else if("wfcnname".equalsIgnoreCase(attrName)){//获取流程定义中文名称
			Object wfcnname = wfdescriptor.getMetaAttributes().get("cnname");
			return wfcnname == null ? "" : wfcnname.toString();
		}else if("wfurl".equalsIgnoreCase(attrName)) {//获取流程定义执行URL
			Object url = wfdescriptor.getMetaAttributes().get("url");
			return url == null ? "" : url.toString();
		}else if("curname".equalsIgnoreCase(attrName)) {//获取获取当前操作人
			int  currentStepId = entry.getCurrentSteps().get(0).getStepId();
			String currentStepName = wfdescriptor.getStep(currentStepId).getName();
			return currentStepName;
		}else if("spname".equalsIgnoreCase(attrName)) {//获取流程实例当前步骤
			int  stepId = step.getStepId();
			String stepName = wfdescriptor.getStep(stepId).getName();
			return stepName;
		}else if("wftype".equalsIgnoreCase(attrName)) {//获取流程实例名称
			return workflowname;
		}
		return null;
	}
	
	/**
	 * 获取配置参数.
	 * @param code 配置编码
	 * @param type 配置类型,local(本地配置:sys_config.properties)|server(全局参数)
	 * @return
	 * @throws Exception
	 */
	public static String getSysConfigurationValue(String code,String type) throws Exception{
		String result = "";
		if(ExStringUtils.isBlank(code) || ExStringUtils.isBlank(type)) {
			return result;
		}
		if("local".equals(type)){
			if(ExStringUtils.isNotBlank(ConfigPropertyUtil.getInstance().getProperty(code))){
				result = ConfigPropertyUtil.getInstance().getProperty(code);
			}		
		}else if("server".equals(type)){
			if(ExStringUtils.isNotBlank(CacheAppManager.getSysConfigurationByCode(code).getParamValue())){
				result = CacheAppManager.getSysConfigurationByCode(code).getParamValue();
			}	
		}
		return result;
	}
}
