package com.hnjk.core.foundation.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Cleanup;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * XML工具类
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 3, 2016 9:40:11 AM 
 * 
 */
public class XMLUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(XMLUtils.class);

	private static final String ENCODING_UTF8="UTF-8";
	
	/** 
     *  
     * @方法功能描述：生成空的xml文件头 
     * @方法名:createEmptyXmlFile 
     * @param xmlPath 
     * @返回类型：Document 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Document createEmptyXmlFile(String xmlPath){  
        if(xmlPath==null || "".equals(xmlPath)) {
			return null;
		}
  
        XMLWriter output;  
        Document document = DocumentHelper.createDocument();  
              
        OutputFormat format = OutputFormat.createPrettyPrint();  
        try {  
            output = new XMLWriter(new FileWriter(xmlPath), format);  
            output.write(document);  
            output.close();  
        } catch (IOException e) {  
            return null;  
        }  
        return document;  
    }  
    
    /** 
     * 根据xml文件路径取得document对象 
     * @param xmlPath 
     * @return 
     * @throws DocumentException 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Document getDocument(String xmlPath){  
        if(xmlPath==null || "".equals(xmlPath)) {
			return null;
		}
  
        File file = new File(xmlPath);  
        if(file.exists()==false){  
            return createEmptyXmlFile(xmlPath);  
        }  
          
        SAXReader reader = new SAXReader();  
        Document document = null;  
        try {  
            document = reader.read(xmlPath);  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
        return document;  
    }  
    
    /** 
     *  
     * @方法功能描述：得到根节点 
     * @方法名:getRootEleme 
     * @param DOC对象 
     * @返回类型：Element 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Element getRootNode(Document document){  
        if(document==null) {
			return null;
		}
          
        Element root = document.getRootElement();  
        return root;  
    }  
    
    /** 
     *  
     * @方法功能描述: 根据路径直接拿到根节点 
     * @方法名:getRootElement 
     * @param xmlPath 
     * @return 
     * @throws DocumentException @参数描述 : 
     * @返回类型：Element 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Element getRootNode(String xmlPath) {  
            if(xmlPath==null|| "".equals(xmlPath.trim())) {
				return null;
			}
            Document document = getDocument(xmlPath);  
            if(document==null) {
				return null;
			}
            return getRootNode(document);  
       }  
    
    /** 
     *  
     * @方法功能描述:得到指定元素的迭代器 
     * @方法名:getIterator 
     * @param parent 
     * @返回类型：Iterator<Element> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    @SuppressWarnings("unchecked")  
    public static Iterator<Element> getIterator(Element parent){  
        if(parent == null) {
			return null;
		}
        Iterator<Element> iterator = parent.elementIterator();  
        return iterator;  
    }  
      
    /** 
     *  
     * @方法功能描述: 根据子节点名称得到指定的子节点 
     * @方法名:getChildElement 
     * @param parent 
     * @param childName 
     * @返回类型：List<Element> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    @SuppressWarnings("unchecked")  
    public static  List<Element> getChildElements(Element parent,String childName){  
        childName  = childName.trim();  
        if (parent==null) {
			return null;
		}
        childName = "//"+childName;  
        List<Element> childElements = parent.selectNodes(childName);  
        return childElements;  
    }  
  
    /** 
     *  
     * @方法功能描述：获取某个节点下的所有子节点
     * @方法名:getChildList 
     * @param node 
     * @返回类型：List<Element> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static  List<Element> getChildList(Element node){  
        if (node==null) {
			return null;
		}
        Iterator<Element> itr = getIterator(node);  
        if(itr==null) {
			return null;
		}
        List<Element> childList = new ArrayList<Element>();  
        while(itr.hasNext()){  
            Element kidElement = itr.next();  
            if(kidElement!=null){  
                childList.add(kidElement);  
            }  
        }  
        return childList;  
    }  
    
    /** 
     *  
     * @方法功能描述 : 查询没有子节点的节点，使用xpath方式 
     * @方法名:getSingleNode 
     * @param parent 
     * @param nodeNodeName 
     *  @参数描述 : 父节点，子节点名称 
     * @返回类型：Node 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    /*public static Node getSingleNode(Element parent,String nodeNodeName){  
        nodeNodeName = nodeNodeName.trim();  
        String xpath = "//";  
        if(parent==null)  
            return null;  
        if (nodeNodeName==null||nodeNodeName.equals(""))   
            return null;  
        xpath += nodeNodeName;  
        Node kid = parent.selectSingleNode(xpath);  
        return kid;  
    }  */
    
    /** 
     *  
     * @方法功能描述：得到子节点，不使用xpath 
     * @方法名:getChild 
     * @param parent 
     * @param childName 
     * @return @参数描述 : 
     * @返回类型：Element 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    @SuppressWarnings("rawtypes")  
    public static Element getChild(Element parent,String childName){  
        childName = childName.trim();  
        if(parent==null) {
			return null;
		}
        if(childName==null || "".equals(childName)) {
			return null;
		}
        Element e = null;  
        Iterator it = getIterator(parent);  
        while(it!=null && it.hasNext()){  
            Element k = (Element)it.next();  
            if(k==null) {
				continue;
			}
            if(k.getName().equalsIgnoreCase(childName)){  
                e = k;  
                break;  
            }  
        }  
        return e;  
    }  
    
    /** 
     *  
     * @方法功能描述：判断节点是否还有子节点 
     * @方法名:hasChild 
     * @param e 
     * @返回类型：boolean 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static boolean hasChild(Element e){  
        if(e==null) {
			return false;
		}
        return e.hasContent();  
    }  
    
    /** 
     *  
     * @方法功能描述：得到指定节点的属性的迭代器 
     * @方法名:getAttrIterator 
     * @param e 
     * @返回类型：Iterator<Attribute> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    @SuppressWarnings("unchecked")  
    public static Iterator<Attribute> getAttrIterator(Element e){  
        if(e==null) {
			return null;
		}
        Iterator<Attribute> attrIterator = e.attributeIterator();  
        return attrIterator;  
    }  
    
    /** 
     *  
     * @方法功能描述：遍历指定节点的所有属性 
     * @方法名:getAttributeList 
     * @param e 
     * @return 节点属性的list集合 
     * @返回类型：List<Attribute> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static List<Attribute> getAttributeList(Element e){  
        if(e==null) {
			return null;
		}
        List<Attribute> attributeList = new ArrayList<Attribute>();  
        Iterator<Attribute> atrIterator = getAttrIterator(e);  
        if(atrIterator == null) {
			return null;
		}
        while (atrIterator.hasNext()) {  
            Attribute attribute = atrIterator.next();  
            attributeList.add(attribute);  
        }  
        return attributeList;  
    }  
    
    /** 
     *  
     * @方法功能描述：  得到指定节点的指定属性 
     * @方法名:getAttribute 
     * @param element 指定的元素 
     * @param attrName 属性名称 
     * @return Attribute 
     * @返回类型：Attribute 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Attribute getAttribute(Element element , String attrName){  
        attrName = attrName.trim();  
        if(element==null) {
			return null;
		}
        if(attrName==null|| "".equals(attrName)) {
			return null;
		}
        Attribute attribute = element.attribute(attrName);  
        return attribute;  
    }  
    
    /** 
     *  
     * @方法功能描述:获取指定节点指定属性的值 
     * @方法名:attrValue 
     * @param e 
     * @param attrName 
     * @返回类型：String 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static String attrValue(Element e,String attrName){  
        attrName = attrName.trim();  
        if(e == null) {
			return null;
		}
        if (attrName== null || "".equals(attrName)) {
			return null;
		}
        return e.attributeValue(attrName);  
    }  
      
    /** 
     *  
     * @方法功能描述：得到指定节点的所有属性及属性值 
     * @方法名:getNodeAttrMap 
     * @return 属性集合 
     * @返回类型：Map<String,String> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Map<String,String> getNodeAttrMap(Element e){  
        Map<String,String> attrMap = new HashMap<String, String>();  
        if (e == null) {  
            return null;  
        }  
        List<Attribute> attributes = getAttributeList(e);  
        if (attributes == null) {  
            return null;  
        }  
        for (Attribute attribute:attributes) {  
            String attrValueString = attrValue(e, attribute.getName());  
            attrMap.put(attribute.getName(), attrValueString);  
        }  
        return attrMap;  
    }  
    
    /** 
     *  
     * @方法功能描述: 遍历指定节点的下所有子节点的元素的text值 
     * @方法名:getSingleNodeText 
     * @param e 
     * @参数描述 : 
     * @返回类型：Map<String,String> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Map<String, String> getSingleNodeText(Element e){  
        Map<String, String> map = new HashMap<String, String>();  
        if(e == null) {
			return null;
		}
        List<Element> kids = getChildList(e);  
        for(Element e2 :kids){  
            if(e2.getTextTrim()!=null){  
                map.put(e2.getName(), e2.getTextTrim());  
            }  
        }  
        return map;  
    }  
      
    /** 
     *  
     * @方法功能描述：遍历根节点下，所有子节点的元素节点，并将此节点的text值放入map中返回 
     * @方法名:getSingleNodeText 
     * @param xmlFilePath 
     * @return @参数描述 : 
     * @返回类型：Map<String,String> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Map<String,String> getSingleNodeText(String xmlFilePath){  
        xmlFilePath = xmlFilePath.trim();  
        if(xmlFilePath==null|| "".equals(xmlFilePath)){
            return null;  
        }  
        Element rootElement = getRootNode(xmlFilePath);  
        if(rootElement==null||!hasChild(rootElement)){  
            return null;  
        }  
        return getSingleNodeText(rootElement);  
    }  
    
    /** 
     *  
     * @方法功能描述:根据xml路径和指定的节点的名称，得到指定节点,从根节点开始找 
     * @方法名:getNameNode 
     * @param xmlFilePath 
     * @param tagName 
     * @param flag : 指定元素的个数 
     * @返回类型：Element 指定的节点 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     *  
     */  
      
    public enum Flag{one,more}  
    
    @SuppressWarnings("unchecked")  
    public static <T>T getNameNode(String xmlFilePath,String tagName,Flag flag){  
        xmlFilePath = xmlFilePath.trim();  
        tagName = tagName.trim();  
        if(xmlFilePath==null||tagName==null|| "".equals(xmlFilePath) || "".equals(tagName)) {
			return null;
		}
        Element rootElement = getRootNode(xmlFilePath);  
        if(rootElement==null) {
			return null;
		}
        List<Element> tagElementList = getNameElement(rootElement, tagName);  
        if(tagElementList == null) {
			return null;
		}
        switch (flag) {  
        case one:  
            return (T) tagElementList.get(0);  
        }  
        return (T) tagElementList;  
    }  
    
    /** 
     *  
     * @方法功能描述:得到指定节点下所有子节点的属性集合 
     * @方法名:getNameNodeAllAttributeMap 
     * @param e 
     * @return @参数描述 : 
     * @返回类型：Map<Integer,Object> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static Map<Integer,Object> getNameNodeAllKidsAttributeMap(Element parent){  
        Map<Integer,Object> allAttrMap = new HashMap<Integer, Object>();  
        if(parent == null) {
			return null;
		}
        List<Element> childlElements = getChildList(parent);  
        if (childlElements == null) {
			return null;
		}
        for (int i = 0; i < childlElements.size(); i++) {  
            Element childElement = childlElements.get(i);  
            Map<String,String> attrMap = getNodeAttrMap(childElement);  
            allAttrMap.put(i,attrMap);  
        }  
        return allAttrMap;  
    }  
    
    /** 
     *  
     * @方法功能描述:根据xml文件名路径和指定的节点名称得到指定节点所有子节点的所有属性集合 
     * @方法名:getNameNodeAllAttributeMap 
     * @param xmlFileName 
     * @param nodeName 
     * @return @参数描述 : 
     * @返回类型：Map<Integer,Object> 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    @SuppressWarnings("unchecked")  
    public static <T>T getNameNodeAllAttributeMap(String xmlFilePath,String nodeName,Flag flag){  
        nodeName = nodeName.trim();  
        Map<String, String> allAttrMap = null;  
        Map<Integer,Map<String,String>> mostKidsAllAttriMap = new HashMap<Integer, Map<String,String>>();  
        if (xmlFilePath==null||nodeName==null|| "".equals(xmlFilePath) || "".equals(nodeName)) {
			return null;
		}
        switch (flag) {  
        case one:  
            Element nameNode = getNameNode(xmlFilePath, nodeName,Flag.one);  
            allAttrMap = getNodeAttrMap(nameNode);  
            return (T) allAttrMap;  
        case more:  
            List<Element> nameKidsElements = getNameNode(xmlFilePath, nodeName, Flag.more);  
            for (int i = 0; i < nameKidsElements.size(); i++) {  
                Element kid = nameKidsElements.get(i);  
                allAttrMap = getNodeAttrMap(kid);  
                mostKidsAllAttriMap.put(i,allAttrMap);  
            }  
            return (T) mostKidsAllAttriMap;  
        }  
        return null;  
    }  
    
    /** 
     *  
     * @方法功能描述:遍历指定的节点下所有的节点 
     * @方法名:ransack 
     * @param element @参数描述 : 
     * @返回类型：void 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static List<Element> ransack(Element element,List<Element> allkidsList){  
        if(element == null) {
			return null;
		}
        if(hasChild(element)){  
            List<Element> kids = getChildList(element);  
            for (Element e : kids) {  
                allkidsList.add(e);  
                ransack(e,allkidsList);  
            }  
        }  
        return allkidsList;  
    }  
    
    /** 
     *  
     * @方法功能描述:得到指定节点下的指定节点集合 
     * @方法名:getNameElement 
     * @param element 
     * @param nodeName 
     * @return @参数描述 : 
     * @返回类型：Element 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static List<Element> getNameElement(Element element ,String nodeName){  
        nodeName = nodeName.trim();  
        List<Element> kidsElements = new ArrayList<Element>();  
        if(element == null) {
			return null;
		}
        if(nodeName == null || "".equals(nodeName)) {
			return null;
		}
        List<Element> allKids = ransack(element,new ArrayList<Element>());  
        if(allKids == null) {
			return null;
		}
        for (int i = 0; i < allKids.size(); i++) {  
            Element kid = allKids.get(i);  
            if(nodeName.equals(kid.getName())) {
				kidsElements.add(kid);
			}
        }  
        return kidsElements;  
    }  
      
    /** 
     *  
     * @方法功能描述:验证节点是否唯一 
     * @方法名:validateSingle 
     * @param element 
     * @返回类型：int 节点唯一返回1,节点不唯一返回大于一的整型数据 
     * @author Zik, 广东学苑教育发展有限公司 
     * @since Nov 3, 2016 9:50:35 AM
     */  
    public static int validateSingle(Element element){  
        int j = 1;  
        if(element == null) {
			return j;
		}
        Element parent = element.getParent();  
        List<Element> kids = getChildList(parent);  
        for (Element kid : kids) {  
            if(element.equals(kid)) {
				j++;
			}
        }  
        return j;  
    }  
    
    /**
     * 创建节点并给节点设置文本值
     * @param parent
     * @param elementName
     * @param text
     * @return
     */
    public static Element addElement(Element parent, String elementName, String text){
    	Element childElement = parent.addElement(elementName);
    	if(ExStringUtils.isEmpty(text)){
    		text = "";
    	}
    	childElement.setText(text);
    	return childElement;
    }
    
    /**
     * 返回格式化的XML字段串 
     * 
     * @param document  要格式化的文档 
     * @return  格式化的XML字段串 
     */
    public static String toXMLString(Document document) {
    	return toXMLString(document, null);
    }
    
    /**
     * 返回格式化的XML字段串 
     * 
     * @param element 要格式化的节点元素 
     * @return 格式化的XML字段串 
     */
    public static String toXMLString(Element element) {
    	return toXMLString(element, null);
    }

    /** 
     * 返回格式化的XML字段串 
     *  
     * @param document  要格式化的文档 
     * @param encoding 使用的编码,如果为null,则使用默认编码(UTF-8) 
     * @return 格式化的XML字段串 
     */  
    public static String toXMLString(Document document, String encoding) {  
        if (encoding == null) {  
            encoding = ENCODING_UTF8;  
        }  
        StringWriter writer = new StringWriter();  
        OutputFormat format = OutputFormat.createPrettyPrint();  
        format.setEncoding(ENCODING_UTF8);  
        XMLWriter xmlwriter = new XMLWriter(writer, format);  
        try {  
            xmlwriter.write(document);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return writer.toString();  
    }  
  
    /** 
     * 返回格式化的XML字段串 
     *  
     * @param element 要格式化的节点元素 
     * @param encoding 使用的编码,如果为null则使用默认编码(UTF-8) 
     * @return 格式化的XML字段串 
     */  
    public static String toXMLString(Element element, String encoding) {  
        if (encoding == null) {  
            encoding = ENCODING_UTF8;  
        }  
        StringWriter writer = new StringWriter();  
        OutputFormat format = OutputFormat.createPrettyPrint();  
        format.setEncoding(encoding);  
        XMLWriter xmlwriter = new XMLWriter(writer, format);  
        try {  
            xmlwriter.write(element);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return writer.toString();  
    }  
  
    /** 
     * 格式化文档并输出到文件 
     *  
     * @param document 要输出的文档 
     * @param filename XML文件名 
     * @param encoding 使用的编码,如果为null则使用默认编码(UTF-8) 
     * @return true or false 
     */  
    public static boolean toXMLFile(Document document, String filename, String encoding) {  
        if (encoding == null) {  
            encoding = ENCODING_UTF8;  
        }  
        boolean returnValue = false;  
        try {  
            XMLWriter output = null;  
            /** 格式化输出,类型IE浏览一样 */  
            OutputFormat format = OutputFormat.createPrettyPrint();  
            /** 指定XML字符集编码 */  
            format.setEncoding(encoding);  
            output = new XMLWriter(new FileWriter(new File(filename)), format);  
            output.write(document);  
            output.close();  
            /** 执行成功,需返回1 */  
            returnValue = true;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            returnValue = false;  
        }  
        return returnValue;  
    }  
  
    /** 
     * 格式化XML文件并保存 
     *  
     * @param srcFileName 源XML文件 
     * @param desFileName 格式化后的XML文件,如果为null,则使用srcFileName 
     * @param encoding 使用的编码,如果为null刚使用默认编码(UTF-8) 
     * @return true or false 
     */  
    public static boolean toXMLFile(String srcFileName, String desFileName, String encoding) {  
        if (encoding == null) {  
            encoding = ENCODING_UTF8;  
        }  
        if (desFileName == null) {  
            desFileName = srcFileName;  
        }  
        boolean returnValue = false;  
        try {  
            SAXReader saxReader = new SAXReader();  
            Document document = saxReader.read(new File(srcFileName));  
            XMLWriter output = null;  
            /** 格式化输出,类型IE浏览一样 */  
            OutputFormat format = OutputFormat.createPrettyPrint();  
            /** 指定XML字符集编码 */  
            format.setEncoding(encoding);  
            output = new XMLWriter(new FileWriter(new File(desFileName)),  
                    format);  
            output.write(document);  
            output.close();  
            /** 执行成功,需返回1 */  
            returnValue = true;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            returnValue = false;  
        }  
        return returnValue;  
    }  
  
    /** 
     * 从读取XML文件 
     *  
     * @param fileName 
     * @return Document对象 
     */  
    public static Document read(String fileName) {  
        SAXReader reader = new SAXReader();  
        Document document = null;  
        try {  
            document = reader.read(new File(fileName));  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
        return document;  
    }  
  
    /** 
     * 从XML字符串转换到document 
     *  
     * @param xmlStr XML字符串 
     * @return Document 
     * @throws DocumentException 
     */  
    public static Document parseText(String xmlStr) throws DocumentException  {  
        return  DocumentHelper.parseText(xmlStr);  
    }  
    /**
     * 将字符串转为无BOM编码
     * @param str
     * @return
     */
    public static String convertWithBOM(String str,String encoding){
    	logger.debug("输入的字符串为："+str);
    	StringBuffer returnStr= new StringBuffer();
    	try {
		@Cleanup InputStream in = new ByteArrayInputStream(str.getBytes());
		@Cleanup UnicodeInputStream uin = new UnicodeInputStream(in, encoding);
		uin.init();
		@Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(uin));
		String strLine=null;
        while((strLine=br.readLine())!=null) {
        	returnStr.append(strLine);
        }
        uin.close();
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	logger.debug("返回无BOM的字符串为："+returnStr.toString());
    	return returnStr.toString();
    }
}
