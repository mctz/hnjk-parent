package com.hnjk.core.dao.hibernate;

import java.sql.Connection;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.dao.ITestService;

public class HibernateConnectionNumTest extends BaseTest{

	@Test
	public void testConnect() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		for(int i=0;i<101;i++){
			Connection connection = testService.getConn();
			System.out.println(connection.toString());
			Thread.sleep(1000);
		}
		
	}
}
