package com.hnjk.core.rao.configuration.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

/**
 * <code>JaxbBinder</code>使用jaxb2的特性持久化xml的binder<p>;
 * 在实际项目中，经常遇到读取xml，然后转换为JAVABEAN，或者需要将数据输出为XML等，这时可以考虑使用这个绑定器。<p>
 * 用例Demo，请参见单元测试用例
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-9-29 上午09:39:04
 * @see 
 * @version 1.0
 */
public class JaxbBinder {

	private Marshaller marshaller;//序列号
	private Unmarshaller unmarshaller;//反序列号
	
	/**
	 * 通过传入POJO的class type序列号对象
	 * @param types 所有需要序列化的Root对象的类型.
	 */
	public JaxbBinder(Class<?>... types) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(types);
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Java->Xml, 特别支持对Root Element是Collection的情形.
	 */
	@SuppressWarnings("unchecked")
	public String toXml(Collection root, String rootName) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
					CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			getMarshaller().marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java->Xml.
	 */
	public String toXml(Object root) {
		try {
			StringWriter writer = new StringWriter();
			marshaller.marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Xml->Java.
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class CollectionWrapper {
		@SuppressWarnings("unchecked")
		@XmlAnyElement
		Collection collection;
	}

	public Marshaller getMarshaller() {
		return marshaller;
	}

	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}
}
