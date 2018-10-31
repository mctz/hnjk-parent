package com.hnjk.edu.jmx;

import java.io.IOException;

import javax.management.JMException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.ConnectorServerFactoryBean;

public class ExConnectorServerFactoryBean extends ConnectorServerFactoryBean{

	@Autowired
	private ExRmiRegistryFactoryBean rmiRegistryFactoryBean;
	
	@Override
	public void afterPropertiesSet() throws JMException, IOException {
		try {
			if(null != rmiRegistryFactoryBean.getObject()){				
				super.afterPropertiesSet();
			}else{
				//skip it
			}
			
		} catch (Exception e) {
		
		}
		
	}
	
	
}
