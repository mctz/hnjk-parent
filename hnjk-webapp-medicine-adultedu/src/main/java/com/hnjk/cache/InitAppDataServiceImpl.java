package com.hnjk.cache;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.edu.netty.server.AbstractNettyServer;
import com.hnjk.edu.netty.server.AppContext;
import com.hnjk.platform.system.cache.AbstractInitAppDataServiceImpl;
import com.hnjk.security.cache.CacheSecManager;

/**
 * 初始化系统所需的缓存数据.
 * <code>InitAppDataServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-3 上午11:24:53
 * @see 
 * @version 1.0
 */
@Service
@Transactional
public class InitAppDataServiceImpl extends AbstractInitAppDataServiceImpl{
	
	private AbstractNettyServer tcpServer;
	
	@Override
	public  void doInitAppData() {
		initDictionary();//初始化字典
		initSysConfiguration();//初始化全局变量
		initResource();//初始化资源
		initResourceRoleRef();//初始化角色资源关系
		initOrgsAndUsers();//初始化组织与用户
		initSensitiveWord();// 初始化敏感词库
	}
	
	@Override
	public void reloadResource() {
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).removeall();
		initResource();		
	}

	public void reloadResourceRoleRef() {
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE).removeall();
		initResourceRoleRef();
	}
	
	@Override
	public void startNettyServer(){
		// String baseDir = System.getProperty("user.dir");
		// String contextFile =
		// "../../src/main/resources/spring/applicationContext-netty.xml";
		// ApplicationContext context = new
		// FileSystemXmlApplicationContext(contextFile);

		// WebApplicationContext wac =
		// ContextLoader.getCurrentWebApplicationContext();
		// nettyTCPServer =(AbstractNettyServer) wac.getBean("NettyTCPServer");
		// wac.getBean("tempStudentFeeService");
		//       wac.getBean("NettyServerHandler");
		AppContext.getBean("NettyServerHandler");
		tcpServer = (AbstractNettyServer)AppContext.getBean(AppContext.TCP_SERVER);
		
		 try {
			tcpServer.startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
