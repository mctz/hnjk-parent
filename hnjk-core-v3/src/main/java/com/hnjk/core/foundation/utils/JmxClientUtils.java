package com.hnjk.core.foundation.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jmx的客户端辅助工具类. <p>
 * 主要用来管理JMX服务连接，创建远程MBean为本地Bean，get/set远程MBean属性.<p>
 * 远程服务端请参看文档转换.
 * @author： 广东学苑教育发展有限公司.
 * @since： Jul 9, 20094:39:05 PM
 * @see 
 * @version 1.0
 */
public class JmxClientUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(JmxClientUtils.class);

	private JMXConnector connector;//连接器
	
	private MBeanServerConnection mbsc;//MB服务连接	
	
	private AtomicBoolean connected = new AtomicBoolean(false);//是否已连接
	
	/**
	 * 构造方法
	 * @param serviceUrl
	 * @throws IOException
	 */
	public JmxClientUtils(final String serviceUrl) throws IOException {
		initConnector(serviceUrl, null, null);
	}

	/**
	 * 构造方法
	 * @param serviceUrl
	 * @param userName
	 * @param passwd
	 * @throws IOException
	 */
	public JmxClientUtils(final String serviceUrl, final String userName, final String passwd) throws IOException {
		initConnector(serviceUrl, userName, passwd);
	}

	
	/**
	 * 初始化并创建远程连接.
	 * @param serviceUrl
	 * @param userName
	 * @param passwd
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void initConnector(final String serviceUrl, final String userName, final String passwd) throws IOException {
		JMXServiceURL url = new JMXServiceURL(serviceUrl);
		//如果用户名和密码不为空，则设置验证
		if (null != userName && !"".equals(userName)) {
			Map environment = new HashMap();
			environment.put(JMXConnector.CREDENTIALS, new String[] { userName, passwd });
			connector = JMXConnectorFactory.connect(url, environment);
		} else {
			connector = JMXConnectorFactory.connect(url);
		}

		mbsc = connector.getMBeanServerConnection();
		logger.debug("MBServer已连接....");
		connected.set(true);
	}

	/**
	 * 关闭连接
	 * @throws IOException
	 */
	public void close() throws IOException {
		connector.close();
		logger.debug("MBServer已关闭...");
		connected.set(false);
	}

	/**
	 * 根据mbname和接口创建MBean代理
	 * @param <T>
	 * @param mbeanName mbean名称
	 * @param mBeanInterface MBean接口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getMBeanProxy(final String mbeanName, final Class<T> mBeanInterface) {				
		assertConnected();
		ObjectName objectName = buildObjectName(mbeanName);
		return (T) MBeanServerInvocationHandler.newProxyInstance(mbsc, objectName, mBeanInterface, false);
	}

	/**
	 * 按属性名直接读取MBean属性(无MBean的Class文件时使用).
	 */
	public Object getAttribute(final String mbeanName, final String attributeName) {	
		assertConnected();
		try {
			ObjectName objectName = buildObjectName(mbeanName);
			return mbsc.getAttribute(objectName, attributeName);
		} catch (JMException e) {		
			throw new IllegalArgumentException("参数不正确", e);
		} catch (IOException e) {
			throw new IllegalStateException("连接出错", e);
		}
	}

	/**
	 * 按属性名直接设置MBean属性(无MBean的Class文件时使用).
	 */
	public void setAttribute(final String mbeanName, final String attributeName, final Object value) {		
		assertConnected();
		try {
			ObjectName objectName = buildObjectName(mbeanName);
			Attribute attribute = new Attribute(attributeName, value);
			mbsc.setAttribute(objectName, attribute);
		} catch (JMException e) {
			throw new IllegalArgumentException("参数不正确", e);
		} catch (IOException e) {
			throw new IllegalStateException("连接出错", e);
		}
	}

	/**
	 * 按方法名直接调用MBean方法(无MBean的Class文件时使用).
	 * 
	 * 所调用方法无参数时的简写函数.
	 */
	public void inoke(final String mbeanName, final String methodName) {
		invoke(mbeanName, methodName, new Object[] {}, new String[] {});
	}

	/**
	 * 按方法名直接调用MBean方法(无MBean的Class文件时使用).
	 * 
	 * @param signature 所有参数的Class全称集合.
	 */
	public void invoke(final String mbeanName, final String methodName, final Object[] params, final String[] signature) {		
		assertConnected();
		try {
			ObjectName objectName = buildObjectName(mbeanName);
			mbsc.invoke(objectName, methodName, params, signature);
		} catch (JMException e) {
			throw new IllegalArgumentException("参数不正确", e);
		} catch (IOException e) {
			throw new IllegalStateException("连接出错", e);
		}
	}

	/**
	 * 确保Connection已连接.
	 */
	private void assertConnected() {
		if (!connected.get()) {
			throw new IllegalStateException("connector已关闭");
		}
	}

	/**
	 * 转换ObjectName构造函数抛出的异常为unchecked exception.
	 */
	private ObjectName buildObjectName(final String mbeanName) {
		try {
			return new ObjectName(mbeanName);
		} catch (MalformedObjectNameException e) {
			throw new IllegalArgumentException("mbeanName:" + mbeanName + "不正确", e);
		}
	}
}
