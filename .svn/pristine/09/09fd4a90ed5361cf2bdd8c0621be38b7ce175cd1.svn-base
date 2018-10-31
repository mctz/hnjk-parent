package com.hnjk.core.foundation.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.springframework.beans.factory.DisposableBean;

/**
 * Memcached 客户端.
 * 这个类需要配置在spring context中.
 * <property name="memcachedClient">
			<bean class="net.spy.memcached.spring.MemcachedClientFactoryBean">
				<property name="servers" value="${memcached.url}" />
				<!-- 默认是TEXT -->
				<property name="protocol" value="TEXT" />
				<!-- 默认是ARRAY_MOD -->
				<property name="locatorType" value="CONSISTENT" />
				<!-- 默认是2500毫秒 -->
				<property name="opTimeout" value="1000" />
			</bean>
		</property>
 * @author hzg(hzg139@163.com)
 *
 */
public class MemcachedManager  implements DisposableBean {
		
	private long shutdownTimeout = 1000;//关闭超时时间
	
	private long defaultTimeout = 5;//异步操作时，默认超时时间
	
	private MemcachedClient memcachedClient;//从net.spy.memcached.spring.MemcachedClientFactoryBean中拿		
	
	//put
	public void put(String key,int expiredTime,Object value){
		memcachedClient.set(key, expiredTime, value);		
	}
	
	//get
	public <T> T get(String key) {		
		return (T) memcachedClient.get(key);		
	}

	//批量获取
	public <T> Map<String, T> getBulk(Collection<String> keys) {		
		return (Map<String, T>) memcachedClient.getBulk(keys);		
	}

	//remove 
	public void remove(String key){
		memcachedClient.delete(key);
	}
	
	//异步获取
	public <T> T asyncGet(String key) {         
        Future<Object> f = memcachedClient.asyncGet(key);  
        try {  
            return (T)f.get(defaultTimeout, TimeUnit.MILLISECONDS);  
        } catch (Exception e) {  
            f.cancel(false);  
        }  
        return null;  
    }  
	
	//异步获取多个
	public <T> Map<String, T> asyncGetMulti(Collection<String> keys) {  
	   
	      Future<Map<String, Object>> f = memcachedClient.asyncGetBulk(keys);  
	       try {  
	           return  (Map<String, T>)f.get(defaultTimeout, TimeUnit.MILLISECONDS);  
	        } catch (Exception e) {  
	            f.cancel(false);  
	     }  
	      return null;  
	  }  
	
	public long increment(String key, int by, long defaultValue, int expire) {  
        return memcachedClient.incr(key, by, defaultValue, expire);  
    }  
      
    public long increment(String key, int by) {  
        return memcachedClient.incr(key, by);  
    }  
      
    public long decrement(String key, int by, long defaultValue, int expire) {  
        return memcachedClient.decr(key, by, defaultValue, expire);  
    }  
      
    public long decrement(String key, int by) {  
        return memcachedClient.decr(key, by);  
    }  
      
    public long asyncIncrement(String key, int by) {  
        Future<Long> f = memcachedClient.asyncIncr(key, by);  
        return getLongValue(f);  
    }  
      
    public long asyncDecrement(String key, int by) {  
        Future<Long> f = memcachedClient.asyncDecr(key, by);  
        return getLongValue(f);  
    }  
	
    private long getLongValue(Future<Long> f) {  
        try {  
            Long l = f.get(defaultTimeout, TimeUnit.MILLISECONDS);  
            return l.longValue();  
        } catch (Exception e) {  
            f.cancel(false);  
        }  
        return -1;  
    }  
  	

	@Override
	public void destroy() throws Exception {		
		if (memcachedClient != null) {
			memcachedClient.shutdown(shutdownTimeout, TimeUnit.MILLISECONDS);
		}	
	}

	/**
	 * @param shutdownTimeout the shutdownTimeout to set
	 */
	public void setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}

	/**
	 * @param defaultTimeout the defaultTimeout to set
	 */
	public void setDefaultTimeout(long defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	/**
	 * @param memcachedClient the memcachedClient to set
	 */
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	/**
	 * @return the memcachedClient
	 */
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}
	
	
}
