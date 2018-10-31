package com.hnjk.core.support.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.InitializingBean;

public class MyTimer extends Timer implements InitializingBean{

	private int hourOfday = 23;
	
	private int minuteOfday = 0;
	
	private int secondOfday = 0;
	
	private Date time;
	
	private TimerTask task;
	
	public MyTimer(){
		super(true);		
	  }

	@Override
	public void afterPropertiesSet() throws Exception {
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY,hourOfday);
	    calendar.set(Calendar.MINUTE,minuteOfday);
	    calendar.set(Calendar.SECOND,secondOfday);
	    time = calendar.getTime();	
	    schedule(task, time);
	    System.out.println("init....");
	}



	/**
	 * @param task the task to set
	 */
	public void setTask(TimerTask task) {
		this.task = task;
	}

	
	/**
	 * @param hourOfday the hourOfday to set
	 */
	public void setHourOfday(int hourOfday) {
		this.hourOfday = hourOfday;
	}

	/**
	 * @param minuteOfday the minuteOfday to set
	 */
	public void setMinuteOfday(int minuteOfday) {
		this.minuteOfday = minuteOfday;
	}

	/**
	 * @param secondOfday the secondOfday to set
	 */
	public void setSecondOfday(int secondOfday) {
		this.secondOfday = secondOfday;
	}
	
	
}
