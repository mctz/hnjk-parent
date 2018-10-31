package com.hnjk.core.rao.configuration.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 提供一个系统配置的属性加载器. <p>
 * 通过一个线程跟踪属性文件的修改，实现修改完成后的热加载.
 * 使用方法<pre>String myProperty = ConfigPropertyUtil.getInstance().getProperty("myPropertyKey");</pre><p>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26下午02:51:06
 * @version 1.0
 */
public class ConfigPropertyUtil implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected static Logger logger = LoggerFactory.getLogger(ConfigPropertyUtil.class);

	private static ConfigPropertyUtil instance; //配置实例

	private final static String propertiesFile = "sys_conf.properties";//配置文件名

	private static Properties configInfoProps = null;

	private static long propertyModLasttime; //属性文件最后修改时间

	private File configrationFile; //属性文件

	/**构造方法：使用一个单例用来获取唯一实例*/
	private ConfigPropertyUtil(File configrationFile) {
		this.configrationFile = configrationFile;
	}

	public static Properties getConfigInfoProps() {
		return configInfoProps;
	}

	/**
	 * 初始化配置实例.
	 * @return
	 * @throws RuntimeException
	 */
	public static synchronized ConfigPropertyUtil initialize(String proFile) throws RuntimeException {
		if (instance != null) {
			throw new RuntimeException("配置属性只能初始化一次！");
		}
		File file = null;
		if (!ExStringUtils.isEmpty(proFile)) {
			file = new File(proFile);
		} else {
			ClassLoader loader = ConfigPropertyUtil.class.getClassLoader();
			try {
				file = new File(java.net.URLDecoder.decode(loader.getResource(propertiesFile).getFile(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("属性文件读取出错：" + e.getMessage());
			}
		}
		instance = new ConfigPropertyUtil(file);
		instance.setProperty();
		instance.new ConfigrationListener().start();
		return instance;
	}

	/**
	 * 返回配置实例
	 * @return
	 * @throws RuntimeException
	 */
	public static synchronized ConfigPropertyUtil getInstance() throws RuntimeException {
		if (instance == null) {
			initialize(null);
		}
		return instance;
	}

	/**
	 * 根据属性文件路径实例化
	 * @param proFile
	 * @return
	 * @throws RuntimeException
	 */
	public static synchronized ConfigPropertyUtil getInstance(String proFile) throws RuntimeException {
		if (instance == null) {
			initialize(proFile);
		}
		return instance;
	}

	/**
	* 获取属性
	* @param name
	* @return
	*/
	public  String getProperty(String name) {
		String retrunValue = "";
		retrunValue = (String) configInfoProps.getProperty(name);
		return retrunValue;
	}

	/**
	 * 设置属性
	 * @return
	 */
	@SuppressWarnings("static-access")
	private void setProperty() {
		Properties pro = new Properties();
		try {
			@Cleanup FileInputStream inputStream = new FileInputStream(configrationFile);
			pro.load(inputStream);
			configInfoProps = pro;
			propertyModLasttime = configrationFile.lastModified();
		} catch (IOException e) {
			logger.error("设置配置文件出错：" + e.getMessage());
		}
	}

	/**
	 * 配置线程监听器. <p>
	 * 
	 * @author： hzg ,政企软件中心 - 项目一部，广东数据通信网络有限公司.
	 * @since： 2009-2-26下午03:35:18
	 * @modify: 
	 * @主要功能：
	 * 用来监听配置文件是否修改，如果修改则重新载入.
	 * @version 1.0
	 */
	class ConfigrationListener extends Thread {
		public ConfigrationListener() {
			super("属性文件跟踪器已启动...");
			setDaemon(true);
		}

		@Override
		public void run() {
			long sleepTime = 10 * 1000; //扫描间隔时间
			while (true) {
				try {
					Thread.sleep(sleepTime);
					if (configrationFile.lastModified() > propertyModLasttime) {
						setProperty();
						logger.info(configrationFile.getName() + " 已更改，重新载入...");
					}
				} catch (Throwable throwable) {
					logger.error("线程跟踪器错误：" + throwable, throwable);
				}

			}
		}
	}

}
