package com.hnjk.core.support.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务调度工厂Bean.
 * @author hzg(hzg139@163.com)
 *
 */
public class SechudlerFactoryBean {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	 private final ScheduledExecutorService scheduledExecutorService
		= Executors.newScheduledThreadPool(3, new ScheduleTheadFactory(SechudlerFactoryBean.class.getSimpleName(), true));
	
	 private transient ScheduledFuture<?> sendFuture;
	 
	 private IScheduleTimerJob scheduleTimerJob;//执行任务接口
	 
	 private int initialDelay = 60;//延迟多长时间执行 单位：秒

	 private int delay = 60;//每间隔多长时间执行一次 单位：秒
	 
	 public void start(){
		 this.sendFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
	            @Override
				public void run() {
	                try {
	                	scheduleTimerJob.doing();
	                } catch (Throwable t) { // 防御性容错
	                    logger.error("启动调度器出错: " + t.getMessage(), t);
	                }
	            }
	        }, initialDelay, delay, TimeUnit.SECONDS);
	 }
	 
	 
	 
	 /**
	 * @param scheduleTimerJob the scheduleTimerJob to set
	 */
	public void setScheduleTimerJob(IScheduleTimerJob scheduleTimerJob) {
		this.scheduleTimerJob = scheduleTimerJob;
	}



	public void stop(){
		 try {
	            sendFuture.cancel(true);	          
	        } catch (Throwable t) {
	            logger.error("停止调度器出错: " + t.getMessage(), t);
	        }
	     
	 }



	/**
	 * @param initialDelay the initialDelay to set
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}



	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	
}
