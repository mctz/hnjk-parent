package com.hnjk.extend.plugin.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ResourceUtils;

import com.hnjk.core.rao.configuration.xml.XmlDom4JUtils;

/**
 * 加载web插件的配置.<p>
 * 需要在spring context中注册这个类.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-30下午05:21:16
 * @see 
 * @version 1.0
 */

public class LoadComponentsFile implements InitializingBean{
	
	/**配置文件路径 */
	private String configFile;
	
	private Map<String, List> componentMap = new HashMap<String, List>();
	
	/**
	 * 
	 */	
	@Override
	public void afterPropertiesSet() throws Exception {
		File file = null;
		try{
			file = ResourceUtils.getFile(this.configFile);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("找不到配置文件："+configFile);
		}		
		Document doc = XmlDom4JUtils.readXML(file.getAbsolutePath());
		Element root = doc.getRootElement();
		List<Element> components = XmlDom4JUtils.findElements("component", root);
	
		for(Iterator<Element> it = components.iterator();it.hasNext();){
			Element component = it.next();
			String id = component.attributeValue("id");
			if(componentMap.containsKey(id)){
				throw new RuntimeException("id="+id+"重复定义");
			}
			
			List<Element> files = XmlDom4JUtils.findElements("file", component);
			List<ComponentsFile> cfs = new ArrayList<ComponentsFile>();
			for(int i=0;i<files.size();i++){
				Element fileNode = (Element)files.get(i);
				ComponentsFile cf = new ComponentsFile();
				String fileType = fileNode.attributeValue("fileType");
				String filePath = fileNode.attributeValue("filePath");
				String fileId = fileNode.attributeValue("fileId");
				cf.setComponentCode(id);
				cf.setFileType(fileType);
				cf.setFilePath(filePath);
				cf.setFileId(fileId);
				cfs.add(cf);
			}
			componentMap.put(id, cfs);
		}
		
	}
	
	/**
	 * 根据组件code获取文件列表
	 * @author cp
	 * @since  2009-5-5 下午03:12:09
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ComponentsFile> getComponentsFiles(String code){
		return componentMap.get(code);
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

}
