package com.hnjk.edu.jmx;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;



/**
 * edu3 JXM服务实现.
 * <code>Edu3ServerManagerImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-9-20 上午11:57:36
 * @see 
 * @version 1.0
 */
@Component
@ManagedResource(objectName="com.hnjk.edu.jmx:name=Edu3ServerManager",
		description="edu3 server manager.")
public class Edu3ServerManagerImpl implements Edu3ServerManager{

	protected static Logger logger = LoggerFactory.getLogger(Edu3ServerManagerImpl.class);

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isServerRunning() {
		// TODO Auto-generated method stub
		return false;
	}
	
		
}
