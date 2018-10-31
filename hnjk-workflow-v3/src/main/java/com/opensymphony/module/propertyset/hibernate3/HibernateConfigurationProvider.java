package com.opensymphony.module.propertyset.hibernate3;

import java.util.Map;

import org.hibernate.cfg.Configuration;


/**
 * Use this class to provide your own configurations to the PropertySet hibernate providers.
 * <p>
 * Simply implement this interface and return a Hibernate Configuration object.
 * <p>
 * This is setup by using the configuration.provider.class property, with the classname
 * of your implementation.
 */
public interface HibernateConfigurationProvider {

    /**
     * Get a Hibernate configuration object
     */
    public Configuration getConfiguration();
    
    /**
     * Get a HibernatePropertySetDAO
     */
    public HibernatePropertySetDAO getPropertySetDAO();
    /**
     * Setup a Hibernate configuration object with the given properties.
     * This will always be called before getConfiguration().
     */
    public void setupConfiguration(Map<String,String> properties);
}
