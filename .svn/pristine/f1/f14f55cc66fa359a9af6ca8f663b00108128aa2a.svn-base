package com.hnjk.core.support.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.scheduling.timer.TimerTaskExecutor;

import com.hnjk.core.support.schedule.jdk.ScheduleTheadFactory;
import com.hnjk.core.support.schedule.jdk.SechudlerExecutor;


public class JdkScheduleTest {

	//测试线程工厂
	@Test
	public void testSchedulerFactory() throws Exception{
		ScheduleTheadFactory factory = new ScheduleTheadFactory("test-thread",false);
		Thread thread = factory.newThread(new HeartBeatJob(1));
		Thread thread2 = factory.newThread(new HeartBeatJob(2));
		thread.start();
		thread2.start();
	
	} 
	
	//模拟心跳测试
	@Test
	public void testSchedule() throws Exception{
		ScheduleTheadFactory factory = new ScheduleTheadFactory("test-thread",false);
		
		SechudlerExecutor timer = new SechudlerExecutor();		
		timer.setWorkThread(factory.newThread(new HeartBeatJob(1)));
		timer.setDelay(1);
		timer.setInitialDelay(1);
		timer.setTimeUnit(TimeUnit.MINUTES);
		timer.start();
					
		synchronized (JdkScheduleTest.class) {
			for(;;){
				try {
					JdkScheduleTest.class.wait();
				} catch (Exception e) {					
				}
				
			}
		}
	}
	
	@Test
	public void testTimerTask() throws Exception{
		TimerTaskExecutor executor = new TimerTaskExecutor();
		
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY,11);
	    calendar.set(Calendar.MINUTE,27);
	    calendar.set(Calendar.SECOND,0);
	    Date time = calendar.getTime();
	    Timer timer = new Timer(true);
	    timer.schedule(new MyTask(), time);
	    executor.setTimer(timer);
	    executor.setDelay(10);
	   	    
	    synchronized (JdkScheduleTest.class) {
			for(;;){
				try {
					JdkScheduleTest.class.wait();
				} catch (Exception e) {					
				}
				
			}
		}
	}
	
	class MyTask extends TimerTask{

		@Override
		public void run() {
			System.out.println("hi,this is task demo run at "+new Date().toGMTString());			
		}
		
	}
	
	class HeartBeatJob implements Runnable{
		private int callNum = 0;
		public HeartBeatJob(int callNum){
			this.callNum =callNum;
		}
		@Override
		public void run() {
			System.out.println(callNum+" - hi,i'm test thread!!!");			
		}
		
	}
	
}
