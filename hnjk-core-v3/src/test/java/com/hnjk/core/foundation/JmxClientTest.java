package com.hnjk.core.foundation;

import org.junit.Test;

import com.hnjk.core.foundation.utils.JmxClientUtils;

public class JmxClientTest {

	@Test
	public void testConnect() throws Exception{
		JmxClientUtils jmxclient = new JmxClientUtils("service:jmx:rmi:///jndi/rmi://localhost:1099/edu3connector");
		jmxclient.inoke("com.hnjk.edu.jmx:name=Edu3ServerManager", "autoRegisterStudent");
		jmxclient.close();
	}
}
