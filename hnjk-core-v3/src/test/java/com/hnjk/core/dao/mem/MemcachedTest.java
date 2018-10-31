package com.hnjk.core.dao.mem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.dao.model.Resource;
import com.hnjk.core.foundation.cache.MemcachedManager;


public class MemcachedTest extends BaseTest{


	public  MemcachedManager  mclient = null;
	
	
	@Before
	public void before(){	
		
		mclient = (MemcachedManager)springContextForUnitTestHolder.getBean("spyMemcachedClient");		
	}
	
	@Test
	public void testNormal() throws Exception{
		//put		
		long startTime = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			mclient.put("user"+i, 120, "Marco.hu-"+i);
		}
		System.out.println("put total time:"+(System.currentTimeMillis()-startTime)+"ms");
		//get
		startTime = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			mclient.get("user"+i);
		}
		System.out.println("get  total time:"+(System.currentTimeMillis()-startTime)+"ms");
		//delete
		for(int i=0;i<10000;i++){
			mclient.remove("user"+i);
		}	
		
		//Bulk
		Resource r = null;
		startTime = System.currentTimeMillis();
		List<String> keys = new ArrayList<String>(); 
		for(int i=0;i<10000;i++){
			r = new Resource();
			r.setResourceid(String.valueOf(i));
			r.setResourceCode("CODE_"+i);
			r.setResourceName("NAME_"+i);
			mclient.put("resource_"+i, 120, r);
			keys.add("resource_"+i);
		}
		
		Map<String, Resource> map = mclient.getBulk(keys);
		System.out.println("bulk data ("+map.size()+") total time:"+(System.currentTimeMillis()-startTime)+"ms...");
	}
	
	@Test
	public void testBinaryConnection() throws Exception{
		//large data	
		Resource r = null;
		long startTime = System.currentTimeMillis();
		
		//binnary protocol
		for(int i=0;i<10000;i++){
			r = new Resource();
			r.setResourceid(String.valueOf(i));
			r.setResourceCode("CODE_"+i);
			r.setResourceName("NAME_"+i);
			mclient.put("resource_"+i, 0, r);
		}
		//get
		for(int i=0;i<10000;i++){
			r = (Resource)mclient.get("resource_"+i);
		}
		System.out.println("big data test for binary protocol result:"+(System.currentTimeMillis()-startTime)+"ms..");
		for(int i=0;i<10000;i++){
			mclient.remove("resource_"+i);
		}
	}
	
	public void testCAS() throws Exception{
		
	}
	
	@Test
	public void testExam() throws Exception{
		String str = "[{\"name\":\"paperId\", \"value\":\"5a402f033b4b1057013b4b857b5000fa\"}," +
				"{\"name\":\"planId\", \"value\":\"4aa66367388f5d6e0138b316707a18ef\"},{\"name\":\"recruitExamLogsId\"," +
				" \"value\":\"402881383b509eda013b5e5f45bc0055\"},{\"name\":\"sid\", \"value\":\"ff8080812e8f0a5f012e99e00fbd32ec\"}," +
				"{\"name\":\"cid\", \"value\":\"FBECB1BCE723171EE030007F01001614\"},{\"name\":\"isMachineExam\", \"value\":\"Y\"}," +
				"{\"name\":\"examType\", \"value\":\"online_exam\"},{\"name\":\"courseName\", \"value\":\"管理学原理\"}," +
				"{\"name\":\"studyNo\", \"value\":\"201104603023001\"},{\"name\":\"lastHandDate\", \"value\":\"2012-12-26 12:36:49\"}," +
				"{\"name\":\"endTime\", \"value\":\"20:07:28\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b50011d\", \"value\":\"A\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b50011d\", \"value\":\"B\"},{\"name\":\"answer5a402f033b4b1057013b4b857b50011d\"," +
				" \"value\":\"C\"},{\"name\":\"answer5a402f033b4b1057013b4b857b50011d\", \"value\":\"E\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b50011e\", \"value\":\"1111设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。1111\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b50011f\", \"value\":\"设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。2222\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b500120\", \"value\":\"1111设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。1111\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b500121\", \"value\":\"1111设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。1111\"}," +
				"{\"name\":\"answer5a402f033b4b1057013b4b857b500122\", \"value\":\"1111设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。11111111设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。11111111设置Cookie存活的时间，默认情况下，用户关闭浏览器则Cookie自动删除，如果没有设置Cookie失效的时间，那么用户关闭浏览器时Cookie也消失。如果设置该项，就能延长Cookie的生命期。1111\"}]";
		for(int i=0;i<10000;i++){
			mclient.put("_answer_4aa66367388f5d6e0138b316707a18ef_"+i, 120, str);
		}
		System.out.println("put ok ...");
		Thread.sleep(5000);
		for(int i=0;i<10000;i++){
			mclient.get("_answer_4aa66367388f5d6e0138b316707a18ef_"+i);
		}
		System.out.println("get ok ...");
	}	
	
}
