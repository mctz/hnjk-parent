package com.hnjk.core.support.context;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 提供系统支持的类. <code>SystemContextHolder</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-3 上午11:53:02
 * @modify: 
 * @主要功能：
 * @see 
 * @version 1.0
 */
public class SystemContextHolder {
	

	private static String EDU3_CACHE_WEBROOTPATH;
	/**
	 * 获取应用的根目录路径
	 * @return
	 */
	public static String getAppRootPath(){
		if (ExStringUtils.isBlank(EDU3_CACHE_WEBROOTPATH)) {
			EDU3_CACHE_WEBROOTPATH =  System.getProperty("xy.root");
		}
		return EDU3_CACHE_WEBROOTPATH;
	}
}
