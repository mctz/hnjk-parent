package com.hnjk.core.support.schedule.jdk;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂，用于创建新线程.<p>
 * <code>
 * 	ScheduleThreadFactory factory = new ScheduleTheadFactory("test-thread",true)
 *   factory.newThread(new TestThread(1)).start();
 *   factory.newThread(new TestThread(2)).start();
 * </code>
 * @author hzg(hzg139@163.com)
 *
 */
public class ScheduleTheadFactory implements ThreadFactory{
	
	private final static AtomicInteger INCREAT_NUM = new AtomicInteger(1);
	
	private final  boolean mDaemo;//是否为守护线程
	
	private final String threadName;//线程名
	
	private transient ThreadGroup threadGroup;//线程组名
	
	public ScheduleTheadFactory(){
		this("pool-"+INCREAT_NUM.getAndIncrement(),false);
	}
	
	public ScheduleTheadFactory(String threadName,boolean mDeamo){
		this.threadName = threadName;
		this.mDaemo = mDeamo;
		this.threadGroup = new ThreadGroup("edu3Schudles");
	}
	
	@Override
	public Thread newThread(Runnable r) {
		
        Thread ret = new Thread(threadGroup,r,threadName,0);
        ret.setDaemon(mDaemo);
        return ret;
	}

	
}
