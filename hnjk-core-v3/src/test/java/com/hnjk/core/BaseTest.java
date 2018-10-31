package com.hnjk.core;

import org.junit.After;
import org.junit.Before;

import com.hnjk.core.support.context.SpringContextForUnitTestHolder;

public class BaseTest {

	public static SpringContextForUnitTestHolder springContextForUnitTestHolder;
	
	@Before
	public void setup(){
		springContextForUnitTestHolder = new SpringContextForUnitTestHolder();
	}
	
	 @After
	 public void tearDown() throws Exception {
		 springContextForUnitTestHolder.close();
	  }
}
