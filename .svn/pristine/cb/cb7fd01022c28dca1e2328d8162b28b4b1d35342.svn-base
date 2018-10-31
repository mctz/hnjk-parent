package com.hnjk.core.foundation.cache;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.FactoryConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;

/**
 * 提供一个内存缓存池管理类. <p>
 * 通过<code>ehcache</code>的内存缓存管理，所有缓存.实现了缓存的装载、移除等方法.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26下午04:42:11 * 
 * @version 1.0
 */
public class EhCacheManager{
	protected static Logger logger = LoggerFactory.getLogger(EhCacheManager.class);
	
	/**cache Manager实例*/
	private static EhCacheManager ehCacheManager = null;
	
	private static CacheManager cacheManager = null;
	//主机端口
	private CopyOnWriteArraySet<String> distributionHosts = new CopyOnWriteArraySet<String>();
	
	/**cache 实例*/
	private  Cache cache ;
	
	private EhCacheManager(){}
	
	/**
	 * 获取指定配置的cache
	 * @param cache
	 * @return
	 */
	public static  EhCacheManager getCache(String cacheName){
		if(null == ehCacheManager){			
			URL url = org.springframework.util.ClassUtils.getDefaultClassLoader().getResource("/ehcache.xml");
			//if(url == null )
				//url = EhcacheTest.class.getResource("ehcache.xml");
			//构造配置
			ehCacheManager = new EhCacheManager();
			cacheManager = ehCacheManager.createCacheManager(url);
		}
		
		ehCacheManager.setCache(cacheManager.getCache(cacheName));
		return ehCacheManager;
	}
	
	//创建cacheManager
	private CacheManager createCacheManager(URL url){		
		Configuration configuration = ConfigurationFactory.parseConfiguration(url);
		//动态添加分布式同步支持	
		String distributionConfig = ConfigPropertyUtil.getInstance().getProperty("ehcache.distribution.rmi");
		if(ExStringUtils.isNotBlank(distributionConfig)){
			for(String host : distributionConfig.split("\\,")){
				distributionHosts.add(host);
			}
			if(distributionHosts.size()<2){
				logger.warn("Requered to config tow hosts,skiped...");
				return new CacheManager(configuration);
			}
				
			//add listener			
			configuration.addCacheManagerPeerListenerFactory(createListenerConfigurationFactory(configuration));			
			//add privoder					
			configuration.addCacheManagerPeerProviderFactory(createProviderConfigurationFactory(configuration));
		}		
		return new CacheManager(configuration);
	}
	
	/*
	 * properties="hostName=202.38.227.232, port=40003, socketTimeoutMillis=120000"
	 */
	private FactoryConfiguration createListenerConfigurationFactory(Configuration configuration){
		FactoryConfiguration factory = new FactoryConfiguration();		
		factory.setClass("net.sf.ehcache.distribution.RMICacheManagerPeerListenerFactory");
		
		StringBuffer listernerProp = new StringBuffer("hostName=");
		boolean isCreated = false;
		for(String host :distributionHosts){
			String[] temp =  host.split("\\:");
			if(temp.length != 2) {
				throw new IllegalArgumentException();
			}
			if(!isBindPort(temp[0],Integer.parseInt(temp[1])) && !isCreated){
				listernerProp.append(temp[0]+",port="+temp[1]);
				distributionHosts.remove(host);
				break;
			}
		}			
		
		listernerProp.append(",socketTimeoutMillis=12000");
		factory.setProperties(listernerProp.toString());
		return factory;
	}
	
	
	/*
	 * properties="peerDiscovery=manual,rmiUrls=//192.168.1.51:40002/CACHE_APP_DICT|
									 //192.168.1.51:40003/CACHE_APP_DICT"
	 */
	private FactoryConfiguration createProviderConfigurationFactory(Configuration configuration){
		FactoryConfiguration factory = new FactoryConfiguration();
		factory.setClass("net.sf.ehcache.distribution.RMICacheManagerPeerProviderFactory");
		
		 Set<String> set = configuration.getCacheConfigurationsKeySet();				 
		 StringBuffer providerProp = new StringBuffer("peerDiscovery=manual,rmiUrls=");
		 //校验端口		 
		 for(String cacheName : set){
			 for(String host : distributionHosts){
				 providerProp.append("//"+host+"/"+cacheName+"|");
			 }			
		 }		
	
		 factory.setProperties(providerProp.toString().substring(0, providerProp.length()-1));
		return factory;
	}
	
	//校验端口，并给定未绑定端口
	private boolean isBindPort(String host,int port){
		try {			 
		  Registry registry  = LocateRegistry.getRegistry(host, port);			 	 
		  if(null != registry.list()) {
			  return true;
		  }
		
		} catch (RemoteException e) {
			return false;
		}
		return false;
	}
			
	private  void setCache(Cache cache) {
		this.cache = cache;
	}

	public Cache getCache() {
		return cache;
	}

	/**
	 * 将一个对象放入缓存池中.	 
	 * @param key 缓存名称
	 * @param obj 缓存对象
	 */
	public    void put(String key,Object obj){
		Element element = new Element(key,obj);
		cache.put(element);
		logger.debug(key+"("+(obj == null ? "NULL" : obj.toString())+")已放入缓存...");
	}
	
	/**
	 * 从缓存池中获取一个缓存对象	
	 * @param key 缓存对象名
	 * @return 如果没有可获取的对象，则返回<code>null</code>.
	 */
	public  Object get(String key){
		Element element = null;
		try {
			element = cache.get(key);		
		} catch (CacheException cacheException) {
			logger.error("获取缓存失败："+cacheException.getMessage());
			throw new DataRetrievalFailureException("获取缓存失败: " + cacheException.getMessage(), cacheException);
		}
		if (element == null) {
			return null;
		} else {
			return (Object) element.getValue();
		}
	 } 
	
	/**
	 * 获取指定缓存的所有key集合
	 * @return
	 */
	public List getKeys () {
		return cache.getKeys();
	}
	
	/**
	 * 获取指定类型缓存对象
	 * @param <T> 指定类型
	 * @param key  缓存KEY
	 * @param classz 类型Class
	 * @return 指定类型缓存对象
	 */
	@SuppressWarnings("unchecked")
	public  <T> T get(String key, Class<T> clazz) {
		if(null == get(key)) {
			return null;
		}
		return (T) get(key);
	}
	
	/**
	 * 从缓存池中移除缓存对象.	
	 * @param key 缓存对象名称
	 */
	public  void remove(String key){		
		cache.remove(key);
		logger.debug(key+"已从缓存中移除...");
	}
	
	/**
	 * 从缓存池中移除全部对象.
	 */
	public void removeall(){
		cache.removeAll();
	}
	
	/**
	 * 获取指定类型LIST缓存
	 * @param <T>  指定类型
	 * @param key 缓存KEY
	 * @param classz 类型Class
	 * @return 指定类型LIST缓存
	 */
	@SuppressWarnings("unchecked")
	public  <T> List<T> getList(String key, Class<T> classz) {
		if(null == get(key)) {
			return null;
		}		
		return (List<T>) get(key);
	}
	
	/**
	 * 获取全部的缓存集合.
	 * @return 封装好的缓存对象集合.
	 */
	@SuppressWarnings("unchecked")
	public  Collection getAll(){
		List<String> cacheObjs;
		List<Object> cacheObjslist = new ArrayList<Object>();
		try {
			cacheObjs = cache.getKeys();
		} catch (IllegalStateException e) {
			logger.error("获取缓存对象失败："+e.getMessage());
			throw new IllegalStateException(e.getMessage(), e);
		} catch (CacheException e) {
			logger.error("获取缓存对象失败："+e.getMessage());
			throw new UnsupportedOperationException(e.getMessage(), e);			
		}
		for (String key:cacheObjs) {//遍历放入集合中
			Object rd = get(key);
			cacheObjslist.add(rd);
		}
		return cacheObjslist;
	}	
		
	
}
