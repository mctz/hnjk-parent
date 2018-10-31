package com.hnjk.core.support.schedule;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务调度工厂.
 * @author hzg(hzg139@163.com)
 *
 */
public class ScheduleTheadFactory implements ThreadFactory{
	
	private final static AtomicInteger INCREAT_NUM = new AtomicInteger(1);
	
	private final  boolean mDaemo;
	
	private final String threadName;
	
	private transient ThreadGroup threadGroup;
	
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
