package com.hnjk.core.rao.configuration.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.support.context.Constants;
/**
 * <code>JaxbUtil</code>;
 * 通过JAXB2 XML数据绑定技术 完成 JAVABEAN <=> XML 相互转换 <p>
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-7 上午10:18:04
 * @version 1.0
 */
public class JaxbUtil {
	
	private Marshaller   marshaller;//序列号
	private Unmarshaller unmarshaller;//反序列号
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public JaxbUtil(Class<?>...types){
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(types);
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			unmarshaller = jaxbContext.createUnmarshaller();
			
		} catch (JAXBException e) {
			logger.error("创建JaxbUtil实例出错:{}",e.fillInStackTrace());
			throw new RuntimeException(e);			
		}
	}
	/**
	 * 反序列化-从XML到JAVABEAN
	 * @param is
	 * @return
	 * @throws JAXBException
	 */
	public Object unmarshal(InputStream is){
		
		Object obj=null;
		try {
			obj = unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			logger.error("反序列化-从XML到JAVABEAN出错:{}",e.fillInStackTrace());
			return obj;
		}	
		
		 return obj;
	}
	/**
	 * 序列化-从JAVABEAN 到 XML
	 * @param obj
	 * @param os
	 */
	public boolean marshal(Object obj,OutputStream os){
		boolean success= false;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Constants.ACHIEVEMENTXML_ENCODING);
			XMLWriter writer= null;
			writer = new XMLWriter(os, format);
			marshaller.marshal(obj, writer);
			success = true;
		}  catch (Exception e) {
			logger.error("序列化-从JAVABEAN 到 XML出错:{}",e.fillInStackTrace());
			return success;
		}
		
		return success;
	}
	
}
