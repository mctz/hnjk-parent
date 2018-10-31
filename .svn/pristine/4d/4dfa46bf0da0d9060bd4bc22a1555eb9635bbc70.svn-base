package com.hnjk.edu.netty.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContext implements ApplicationContextAware
{

    public static final String TCP_SERVER = "tcpServer";

    // The spring application context.
    private static ApplicationContext applicationContext;

    @Override
	public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        AppContext.applicationContext = applicationContext;
    }

    // 根据beanName获取bean
    public static Object getBean(String beanName)
    {
        if (null == beanName)
        {
            return null;
        }
        return applicationContext.getBean(beanName);
    }
}