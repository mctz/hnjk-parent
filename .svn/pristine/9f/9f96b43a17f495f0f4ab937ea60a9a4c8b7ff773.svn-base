package com.hnjk.edu.teaching.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Cleanup;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
/**
 * 预约温馨提示消息内容模板xml读取工具
 * <code>MsgXmlUtils</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-7-20 下午04:05:03
 * @see 
 * @version 1.0
 */
public class MsgXmlUtils {	
	/**
	 * 读取xml文件并返回Document对象
	 * @param filePath 文件路径
	 * @return
	 */
	public static Document readXML(String filePath) {
		try {
			@Cleanup FileInputStream in = new FileInputStream(filePath);
			SAXReader reader = new SAXReader();
			return reader.read(in);
		} catch (FileNotFoundException e) {
			throw new ServiceException("不可能发生的异常,找不到文件：" + filePath);
		} catch (DocumentException e) {
			throw new ServiceException("解析文件" + filePath + "出错," + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	/**
	 * 查找指定元素的xml元素节点
	 * @param id 元素id
	 * @param root 父元素
	 * @return
	 */
	public static Element findElementById(String id, Element root) {		
		Element ret = null;			
		if (root != null) {
			List el = root.elements();
			for (int i = 0; i < el.size(); i++) {
				Element child = (Element) el.get(i);
				if (id.equals(child.attributeValue("id"))) {
					ret = child;
					break;
				}
			}
		}
		return ret;
	}
	/**
	 * 返回模板标题和内容
	 * @param id 查找id
	 * @param filePath xml文件路径
	 * @return
	 */
	public static Map<String, Object> getMsgContent(String id, String filePath){
		Map<String, Object> msg = new HashMap<String, Object>();
		Document doc = readXML(filePath);
		Element el = null;
		if(doc != null){
			Element root = doc.getRootElement();			
			el = findElementById(id,root);
			if(el != null){
				 msg = new HashMap<String, Object>();
				 msg.put("title", el.attributeValue("title", ""));			 
				 msg.put("content", el.getTextTrim());
			}
		}		
		return msg;		
	}
	
	/**
	 * 返回所有模板内容
	 * @param filePath xml文件
	 * @return key=id,values=对应单个模板标题和内容
	 */
	public static Map<String, Map<String, Object>> getMsgContentMap(String filePath,String ids){
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String,Object>>();
		Document doc = readXML(filePath);
		if(doc != null){
			Element root = doc.getRootElement();			
			if (root != null) {
				List el = root.elements();
				for (int i = 0; i < el.size(); i++) {
					Element child = (Element) el.get(i);
					String childId=  child.attributeValue("id");
					Map<String, Object> msg = new HashMap<String, Object>();
					msg.put("title", child.attributeValue("title", ""));			 
					msg.put("content", child.getTextTrim());
					
					if (ExStringUtils.isNotBlank(ids)) {
						if(ids.indexOf(childId)>=0) {
							resultMap.put(childId, msg);
						}
					}else {
						resultMap.put(childId, msg);
					}
				}
			}
		}
		return resultMap;		
	}
	

	public static void main(String[] args) {
		String filePath = "D:\\orderMsgTips.xml";	
		Map<String, Object> msg = getMsgContent("orderCourseMsg", filePath);
		System.out.println("title="+msg.get("title"));
		System.out.println("content="+msg.get("content"));
		
		Map<String, Map<String, Object>> resultMap = getMsgContentMap(filePath,"");
		System.out.println(resultMap);
	}
}
