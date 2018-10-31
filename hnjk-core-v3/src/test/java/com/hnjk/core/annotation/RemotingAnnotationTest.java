package com.hnjk.core.annotation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.hnjk.core.BaseTest;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.support.remoting.ExHttpInvokerServiceExporter;
import com.hnjk.core.support.remoting.HttpinvokerRemoteServiceFactory;
import com.hnjk.core.support.schedule.quartz.QuartzTrigger;

/**
 * 远程服务注解测试.
 * @author hzg
 *
 */
public class RemotingAnnotationTest extends BaseTest{
	
	//private Set<String> pkSet = new HashSet<String>();

	private HttpinvokerRemoteServiceFactory factory;
	
	private Scheduler sched ;
	

	
	@Before
	public void setUp() throws Exception{
		//pkSet.add(RemotingAnnotationTest.class.getPackage().getName());
		//初始化exporter工厂
		factory = (HttpinvokerRemoteServiceFactory)springContextForUnitTestHolder.getBean("httpInvokerRemoteServiceFactory");
		factory.afterPropertiesSet();	
		//startQuartz();
	}
		
	//测试本地服务
	@Test
	public void testLocalService() throws Exception{					
		IRemotingService remotingService = (IRemotingService)springContextForUnitTestHolder.getBean("remotingService");		
		Assert.assertTrue(null, AopUtils.isCglibProxy(remotingService));		
		Assert.assertEquals("hellow,marco.hu", remotingService.simpleInvoker("marco.hu"));
		
	}
	
	
	
	//测试远程服务
	@Test
	public void testRemotingService() throws Exception{
		
		HttpInvokerProxyFactoryBean proxybean = getProxyBean(factory);		
		
		IRemotingService remotingService = (IRemotingService)proxybean.getObject();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("userid", "ff8080812a35b779012a3605fdd50003");//fjd565
		String sql = "select t.username from hnjk_sys_users t where t.resourceid=:userid ";
		Map<String, Object> resultMap = remotingService.remotingInvoker(sql, condition);
		Assert.assertEquals("fjd565", resultMap.get("username").toString());
		
	
	}
	
	
	//测试含调度的远程服务
	@Test
	public void testRemotingScheduleService() throws Exception{		
		final HttpInvokerProxyFactoryBean proxybean = getProxyBean(factory);		
		IRemotingService remotingService = (IRemotingService)proxybean.getObject();
		
		remotingService.remotingScheduleInvoker();
		MemcachedManager memcachedManager = (MemcachedManager)springContextForUnitTestHolder.getBean("spyMemcachedClient");
		QuartzTrigger quartzTrigger = memcachedManager.get("testSchdule");
		//构造调度bean		
		MethodInvokingJobDetailFactoryBean mijdfb = new MethodInvokingJobDetailFactoryBean();
		mijdfb.setBeanName("myJob1");
		mijdfb.setTargetObject(remotingService);
		mijdfb.setTargetMethod("remotingScheduleInvoker");
		mijdfb.afterPropertiesSet();
		

		CronTriggerBean trigger0 = new CronTriggerBean();
		trigger0.setBeanName(quartzTrigger.getJobName()+"Trigger");		
		trigger0.setJobDetail((JobDetail)mijdfb.getObject());
		trigger0.setCronExpression("*/5 * * * * ?");
		trigger0.afterPropertiesSet();
		//调度工厂
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean(); 
		sched = schedulerFactoryBean.getScheduler();
		
		Properties properties = new Properties();
		properties.load(RemotingAnnotationTest.class.getResourceAsStream("quartz.properties"));
		schedulerFactoryBean.setQuartzProperties(properties);
		schedulerFactoryBean.setJobFactory(null);
		
		schedulerFactoryBean.setTriggers(new org.quartz.Trigger[] {trigger0});
		schedulerFactoryBean.afterPropertiesSet();
		//scheduleJob(trigger);
        
	}
	
	
	
	//测试任务调度
	@Test
	public void testSchedule() throws Exception{
		//添加调度
		
		if(sched.isStarted()){
			JobDetail jobDetail = new JobDetail("testjob", Scheduler.DEFAULT_GROUP, RemoteScheduleJob.class);
			sched.addJob(jobDetail, true);
			org.quartz.CronTrigger cronTrigger = new org.quartz.CronTrigger();
			cronTrigger.setName("CronTrigger");
			cronTrigger.setJobName("testjob");
			
	        cronTrigger.setCronExpression("*/5 * * * * ?");
	        sched.scheduleJob(cronTrigger);
		}
		
	}
	
	private HttpInvokerProxyFactoryBean getProxyBean(HttpinvokerRemoteServiceFactory factory) throws Exception {
		final ExHttpInvokerServiceExporter exporter = factory.getServiceExporter("/remoting/test/remotingService.html");
				
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
				return readRemoteInvocationResult(
						new ByteArrayInputStream(response.getContentAsByteArray()), config.getCodebaseUrl());
			}
			
		});
		
		proxybean.afterPropertiesSet();
		
		return proxybean;
	}
	
	private HandlerExecutionChain getHandler(HandlerMapping hm, MockHttpServletRequest req) throws Exception {
		HandlerExecutionChain hec = hm.getHandler(req);
		HandlerInterceptor[] interceptors = hec.getInterceptors();
		if (interceptors != null) {
			for (int i = 0; i < interceptors.length; i++) {
				interceptors[i].preHandle(req, null, hec.getHandler());
			}
		}
		return hec;
	}	
	
	private  void startQuartz() throws Exception{
		QuartzServer quartzServer = new QuartzServer();
		quartzServer.run();
		testRemotingScheduleService();
		synchronized (RemotingAnnotationTest.class) {
			for(;;){
				try {
					RemotingAnnotationTest.class.wait();
				} catch (Exception e) {					
				}
				
			}
		}
	}
	
	//调度器
	class QuartzServer implements Runnable{
		 
		 public void start() throws Exception{
			 StdSchedulerFactory sf = new StdSchedulerFactory();		 
			 sf.initialize(RemotingAnnotationTest.class.getResourceAsStream("quartz.properties"));
			 sched = sf.getScheduler();
			 sched.start();
			 System.out.println("调度服务已启动...");
			 
		 }
		 
		 public void stop() throws Exception{
			 sched.shutdown(true);
		 }
		 
		 public void addJob(JobDetail jobDetail,org.quartz.CronTrigger cronTrigger) throws Exception{
			 sched.addJob(jobDetail, true);	
			 sched.scheduleJob(cronTrigger);
		     sched.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);
		 }

		public Scheduler getSched() {
			return sched;
		}

		@Override
		public void run() {
			try {
				start();
			} catch (Exception e) {
				
			}			
		}
		 
		 
	}	
	
}
