package com.hnjk.core.foundation.utils;

import javax.annotation.PostConstruct;

import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * 初始化拦截java.util.logging到Self4j. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-3下午01:02:05
 * @modify: 
 * @version 1.0
 */
public class JulOverSlf4jProcessor {

	@PostConstruct
	public void init() {
		SLF4JBridgeHandler.install();
		System.out.println("binding SLF4JB...");
	}
}
