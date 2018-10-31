package com.hnjk.core.annotation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import lombok.Cleanup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import com.hnjk.core.support.remoting.ExHttpInvokerServiceExporter;
import com.hnjk.core.support.remoting.HttpinvokerRemoteServiceFactory;

/**
 * 使用HTTP请求测试 remoting
 * @author hzg
 *
 */
public class HttpinvokerRemoteServiceFactoryTest {

	ConfigurableWebApplicationContext wac = null;
	HttpinvokerRemoteServiceFactory factory = null;	
	@Before
	public void setUp() throws Exception{
		MockServletContext sc = new MockServletContext("");
		wac = new XmlWebApplicationContext();
		wac.setServletContext(sc);
		wac.setConfigLocation("classpath*:spring/test-applicationContext*.xml");
		wac.refresh();
		
		factory = (HttpinvokerRemoteServiceFactory)wac.getBean("httpInvokerRemoteServiceFactory");
		factory.afterPropertiesSet();	
	}

	//测试使用http url 方式
	@Test
	public void testSimpleUrlHandlerMapping() throws Exception{
		HandlerMapping hm = (HandlerMapping) wac.getBean("handlerMapping");
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/remoting/test/remotingService.html");
		//校验Bean是否注册
		Object bean = wac.getBean("/remoting/test/remotingService.html");
		HandlerExecutionChain hec = hm.getHandler(req);		
		Assert.assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);
		
		//正常invoker
		HttpInvokerProxyFactoryBean proxyBean = getProxyBean(factory,"/remoting/test/remotingService.html");
		IRemotingService remotingService = (IRemotingService)proxyBean.getObject();
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("userid", "ff8080812a35b779012a3605fdd50003");//fjd565
		String sql = "select t.username from hnjk_sys_users t where t.resourceid=:userid ";
		Map<String, Object> resultMap = remotingService.remotingInvoker(sql, condition);
		Assert.assertEquals("fjd565", resultMap.get("username").toString());
		
		//非正常 Invoker
		 proxyBean = getProxyBean(factory,"/remoting/test/remotingService1.html");//传一个错误的地址
		 //remotingService = (IRemotingService)proxyBean.getObject();
		 Assert.assertNull("空对象", proxyBean);
	}
	
	
	private HttpInvokerProxyFactoryBean getProxyBean(HttpinvokerRemoteServiceFactory factory,String serviceUrl) throws Exception {
		try {
			final ExHttpInvokerServiceExporter exporter = factory.getServiceExporter(serviceUrl);
			if(null != exporter){
				//proxybean
				HttpInvokerProxyFactoryBean proxybean = new HttpInvokerProxyFactoryBean();
				proxybean.setServiceInterface(IRemotingService.class);
				proxybean.setServiceUrl("http://myservice/remoting/test/remotingService.html");
				
				//expoter
				proxybean.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {
					@Override
					protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
					
						MockHttpServletRequest request = new MockHttpServletRequest();
						MockHttpServletResponse response = new MockHttpServletResponse();
						request.setContent(baos.toByteArray());
						exporter.handleRequest(request, response);
						@Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());
						return readRemoteInvocationResult(inputStream, config.getCodebaseUrl());
					}
				});
				
				proxybean.afterPropertiesSet();
				return proxybean;
			}
		} catch (Exception e) {
			return null;
		}
		
		
		
		
		return null;
	}
}
