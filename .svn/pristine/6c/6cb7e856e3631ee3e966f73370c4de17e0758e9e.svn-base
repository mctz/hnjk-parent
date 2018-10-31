package com.hnjk.core.foundation.cache;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;

import org.junit.Test;

public class EhcacheTest {
		
	//获取配置
	@Test
	public void testGetConfiguration() throws Exception{
	
		URL uri = EhcacheTest.class.getResource("ehcache.xml");
		Configuration config =  ConfigurationFactory.parseConfiguration(uri);
		Set<String> set = config.getCacheConfigurationsKeySet();
		for(String name : set){
			System.out.println(name);
		}
	}
	
	@Test
	public void testRmiCluster() throws Exception{
		for(int i=0;i<4;i++){//模拟4个线程
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(i+"_KEY", "VALUE_"+i);
			
			EhCacheMock mock1= new EhCacheMock(map);
			mock1.run();
			Thread.sleep(2000);
		}
		//打印出当前的值
		List list = (List)EhCacheManager.getCache("CACHE_APP_DICT").getAll();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
				
	}
	
	class EhCacheMock implements Runnable{
		
		private Map<String, Object> map;
				
		public EhCacheMock(Map<String, Object> map){
			this.map = map;
		}
		
		@Override
		public void run() {
			for(String key :map.keySet()){
				EhCacheManager.getCache("CACHE_APP_DICT").put(key,map.get(key));				
			}
			
		}
		
	}
}
