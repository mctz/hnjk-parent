package com.hnjk.core.rao.configuration.property;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性文件工具. <code>PropertiesFileUtils</code><p>;
 * 
 * @author： hzg ,政企软件中心 - 项目一部，广东数据通信网络有限公司.
 * @since： 2009-11-23 下午02:24:48
 * @modify: 
 * @主要功能：
 * @see 
 * @version 1.0
 */
public final class PropertiesFileUtils {

	protected static Logger logger = LoggerFactory.getLogger(PropertiesFileUtils.class);

	/**
	 * 根据property名获取值
	 * 
	 * @param propertiesFileName 属性文件名,ex. a.properties
	 * @param propertyName 要获取的属性值名
	 * @return
	 */
	public static String getValueByPropertyName(String propertiesFileName, String propertyName) {
		String s = "";
		// 加载属性文件读取类
		Properties p = new Properties();
		try {
			// propertiesFileName如test.properties
			//以流的形式读入属性文件
			@Cleanup FileInputStream in = new FileInputStream(propertiesFileName);
			// 属性文件将该流加入的可被读取的属性中
			p.load(in);
			// 取得对应的属性值
			s = p.getProperty(propertyName);
		} catch (Exception e) {
			logger.error("获取属性文件" + propertiesFileName + "-->" + propertyName + "出错：" + e.fillInStackTrace());
		}
		return s;
	}

	/**
	 * 获取某个属性文件所有键值，并用Map返回
	 * @param propertiesFileName
	 * @return 封装键值的map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getAllValue(String propertiesFileName) {
		// 加载属性文件读取类
		Properties p = new Properties();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			@Cleanup FileInputStream in = new FileInputStream(propertiesFileName);
			p.load(in);
			for (Map.Entry entry : p.entrySet()) {
				map.put((String) entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			logger.error("获取属性文件" + propertiesFileName + "的键值出错：" + e.fillInStackTrace());
		}
		return map;
	}

	/**
	 * 根据<code>ResourceBundle</code>获取属性文件的对应值（用于只读）
	 * 
	 * @param propertiesFileNameWithoutPostfix  属性文件名，不要带"properties"后缀
	 * @param propertyName 获取的属性值名
	 * @return
	 */
	public static String getValueByPropertyNameOnResourceBundle(String propertiesFileNameWithoutPostfix,
			String propertyName) {
		String s = "";
		// 如属性文件是test.properties，那此时propertiesFileNameWithoutPostfix的值就是test
		ResourceBundle bundel = ResourceBundle.getBundle(propertiesFileNameWithoutPostfix);
		s = bundel.getString(propertyName);
		return s;
	}

	/**
	 * 根据属性名更改属性值
	 * @param propertiesFileName 属性文件名,ex. a.properties
	 * @param proMap
	 * @return 是否操作成功
	 */
	public static boolean changeValueByPropertyName(String propertiesFileName, Map proMap) {
		boolean writeOK = true;
		Properties p = new Properties();
		try {
			@Cleanup FileInputStream in = new FileInputStream(propertiesFileName);
			p.load(in);
			Iterator it = proMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				p.setProperty(entry.getKey().toString(), entry.getValue().toString());
			}
			// 输出流
			@Cleanup FileOutputStream out = new FileOutputStream(propertiesFileName);
			// 设置属性头，如不想设置，请把后面一个用""替换掉
			p.store(out, "greenhome config");
		} catch (Exception e) {
			writeOK = false;
			logger.error("更改属性文件" + propertiesFileName + "键值出错：" + e.fillInStackTrace());
		}
		return writeOK;
	}

	/**
	 * 获取系统环境变量
	 * @return
	 */
	public static Map getEnv() {
		return System.getenv();
	}

	public static void main(String[] args) {
		Map map = getEnv();
		for(Object o :map.keySet()){
			System.out.println(o.toString()+" - "+map.get(o));
		}
		//		Map map =  new HashMap();
		//		map.put("IP", "127.0.0.1");
		//		map.put("PORT", "8080");
		//		changeValueByPropertyName("c:\\temp\\config.ini",map);

		//	Document doc = XmlDom4JUtils.readXML("c:\\temp\\context.xml");
		//	Element el = XmlDom4JUtils.findElement("Resource",doc.getRootElement());
		//	el.attribute("username").setText("root1");
		//	XmlDom4JUtils.saveXML("c:\\temp\\context.xml", doc);
		//System.out.println(getEnv().get("green_home"));
//		Map map = new LinkedHashMap();
//		map.put("[NET];", "");
//		map.put("PAGEIP", "192.168.19.253");
//		map.put("PAGEPORT", "8100");
//		map.put("DEVICEIP", "192.168.19.253");
//		map.put("DEVICEPORT", "8800");
//		changeValueByPropertyName("D:\\Marco\\greenhome\\dataConvSer\\config.ini", map);
	}
}
