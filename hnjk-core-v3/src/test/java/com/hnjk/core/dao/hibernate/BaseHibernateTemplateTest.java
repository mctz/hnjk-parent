package com.hnjk.core.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.dao.ITestService;
import com.hnjk.core.dao.model.Resource;

public class BaseHibernateTemplateTest extends BaseTest{

	
	@Test
	public void getEntity() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		Assert.assertTrue(testService.isExistEntity("com.hnjk.core.dao.model.Course"));
	}
	
	@Test
	public void testExcuteByHql() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		Map<String,Object> paramter = new HashMap<String, Object>();
		
		String hql = " update "+Resource.class.getSimpleName()+" r set r.isDeleted = :isDelted where r.resourceType = :type ";
		paramter.put("isDelted",0);
		paramter.put("type", "menu");
		System.out.println("excute results:"+testService.testExcuteByHql(hql, paramter));
	}
	
	public void testBatchSaveOrUpdate() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		Resource resource;
		for(int i=0;i<100;i++){
			resource = new Resource();
			resource.setResourceCode("");
		}
		
		
	}
}
