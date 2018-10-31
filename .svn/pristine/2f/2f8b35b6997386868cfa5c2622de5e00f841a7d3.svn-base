package com.hnjk.edu.jmx;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

public class ExRmiRegistryFactoryBean extends RmiRegistryFactoryBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		if(!isBindPort(getPort())){
			super.afterPropertiesSet();
		}
		
	}

	private boolean isBindPort(int registryPort){
		ServerSocket serverSocket = null;
		boolean flag = false;
		try {
			serverSocket = new ServerSocket(registryPort);	
			
		} catch (Exception e) {
			logger.warn("Rmi 端口："+registryPort+" 已绑定,跳过...");
			 flag = true;
		}finally{
			try {
				if(null != serverSocket) {
					serverSocket.close();
				}
			} catch (IOException e) {				
			}
		}
		return flag;
	}
	
}
