package com.hnjk.core.support.schedule;

import org.junit.Test;

import com.hnjk.core.BaseTest;

public class TestSpringTimer extends BaseTest {

	@Test
	public void run() throws Exception{
		//springContextForUnitTestHolder.getBean("timerTaskExcutor");
		
		synchronized (TestSpringTimer.class) {
			for(;;){
				try {
					TestSpringTimer.class.wait();
				} catch (Exception e) {					
				}
				
			}
		}
	}
}
