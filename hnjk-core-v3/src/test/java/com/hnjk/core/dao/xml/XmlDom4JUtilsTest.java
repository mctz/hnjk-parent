package com.hnjk.core.dao.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import com.hnjk.core.rao.configuration.xml.XmlDom4JUtils;

public class XmlDom4JUtilsTest {

	//@Test
	public void testXml() throws Exception{
		
		URL url = new URL("http://www.google.com/ig/api?hl=zh_cn&weather=guangzhou");
	    InputStream inputstream = url.openStream(); 	
	    InputStreamReader in =  new InputStreamReader(inputstream,"GBK");
		Document document  = XmlDom4JUtils.readXML(in);
		
		Element root = document.getRootElement();
		List<Element> list =  XmlDom4JUtils.findElements("forecast_conditions",XmlDom4JUtils.findElement("weather", root));
		//Map<String, Object> map = new HashMap<String, Object>();
		Iterator<Element> e1;
		Element e2;
		for(Element e : list){
			e1 = e.elementIterator();
			while(e1.hasNext()){
				e2 = e1.next();
				System.out.println(e2.getName()+" - "+e2.attributeValue("data"));
			}			
			System.out.println("==============");
		}
		inputstream.close();
		in.close();
	}
}
