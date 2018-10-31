package com.hnjk.core.rao.configuration.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.hnjk.core.exception.ServiceException;

/**
 * 提供基于DOM4J处理xml文件的工具类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-27上午11:22:04
 * @see 
 * @version 1.0
 */
public class XmlDom4JUtils {

	/**
	 * 查找指定元素的xml元素节点
	 * @param name 元素名称
	 * @param el 父元素
	 * @return 元素节点
	 */
	@SuppressWarnings("unchecked")
	public static Element findElement(String name, Element el) {
		Element ret = null;
		if (el != null) {
			List<Element> e = el.elements(name);
			for (int i = 0; i < e.size(); i++) {
				Element n = (Element) e.get(i);
				if (n.getName().equals(name)) {
					ret = n;
					break;
				}
			}
		}
		return ret;
	}
	
	/**
	 * 查找指定名称的元素的集合
	 * @param name 元素名称
	 * @param el 元素
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> findElements(String name, Element el) {
		List<Element> list = new ArrayList<Element>();
		if (el != null) {
			List<Element> e = el.elements(name);
			for (int i = 0; i < e.size(); i++) {
				Element n = e.get(i);
				if (n.getName().equals(name)) {
					list.add(n);
				}
			}
		}
		return list;
	}
	
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
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 读取XML文件并返回Document对象
	 * @param in
	 * @return
	 */
	public static Document readXML(InputStreamReader in){
		SAXReader reader = new SAXReader();
		try {
			return reader.read(in);
		} catch (DocumentException e) {
			throw new ServiceException("解析文件出错," + e.getMessage());
		}
	}
	
	/**
	 * 保存xml文件
	 * @param filePath 文件路径
	 * @param document document对象
	 */
	public static void saveXML(String filePath, Document document) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			@Cleanup FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			@Cleanup XMLWriter writer = new XMLWriter(fileOutputStream, format);
			writer.write(document);
		} catch (IOException e) {
			throw new ServiceException("保存配置文件失败");
		}
	}
}
