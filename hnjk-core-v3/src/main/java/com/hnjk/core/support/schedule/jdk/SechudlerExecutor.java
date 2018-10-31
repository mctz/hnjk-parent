package com.hnjk.core.support.schedule.jdk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.timer.TimerTaskExecutor;

/**
 * 用来执行任务的Executor.<p>
 * 通常可以用来构造一个简单的Scheduler，如某个Job需要间隔一定时间执行.可以模拟心跳.
 * @author hzg(hzg139@163.com)
 *
 */
public class SechudlerExecutor {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	 private final ScheduledExecutorService scheduledExecutorService
		= Executors.newScheduledThreadPool(3, new ScheduleTheadFactory(SechudlerExecutor.class.getSimpleName(), true));
	
	 private transient ScheduledFuture<?> sendFuture;//执行结果
	 
	 //private IScheduleTimerJob scheduleTimerJob;//执行任务接口
	 
	 private Runnable workThread;//工作线程
	 
	 private int initialDelay = 60;//延迟多长时间执行 单位：秒

	 private int delay = 60;//每间隔多长时间执行一次 单位：秒
	 
	 private TimeUnit timeUnit = TimeUnit.SECONDS;
	 
	 public void start(){
		 this.sendFuture = scheduledExecutorService.scheduleWithFixedDelay(getWorkThread(), initialDelay, delay, timeUnit);			 
	 }
	 
	 
	
	public void stop(){
		 try {
	            sendFuture.cancel(true);	          
	        } catch (Throwable t) {
	            logger.error("停止调度器出错: " + t.getMessage(), t);
	        }
	     
	 }
	
	


	/**
	 * @return the workThread
	 */
	public Runnable getWorkThread() {
		return workThread;
	}



	/**
	 * @param workThread the workThread to set
	 */
	public void setWorkThread(Runnable workThread) {
		this.workThread = workThread;
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



	/**
	 * @return the timeUnit
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}



	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	
}
