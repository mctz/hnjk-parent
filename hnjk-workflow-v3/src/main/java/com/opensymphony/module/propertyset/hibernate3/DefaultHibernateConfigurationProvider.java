package com.opensymphony.module.propertyset.hibernate3;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by IntelliJ IDEA. User: Mike Date: Jul 26, 2003 Time: 5:05:55 PM To
 * change this template use Options | File Templates.
 */
public class DefaultHibernateConfigurationProvider implements HibernateConfigurationProvider {

	private Configuration configuration;
	private HibernatePropertySetDAO propertySetDAO;
	private SessionFactory sessionFactory;

	// implements interface
	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	// implements interface
	@Override
	public HibernatePropertySetDAO getPropertySetDAO() {
		if (this.propertySetDAO == null) {
			HibernatePropertySetDAOImpl hpd = new HibernatePropertySetDAOImpl();
			hpd.setSessionFactory(this.sessionFactory);
			this.propertySetDAO = hpd;
		}
		return this.propertySetDAO;
	}
	
	public void setSessionFactory(SessionFactory _sessionFactory) {
		this.sessionFactory = _sessionFactory;
	}

	@Override
	public void setupConfiguration(Map<String,String> configurationProperties) {
		try {
			this.configuration = new Configuration().addClass(PropertySetItem.class);
			for(String key : configurationProperties.keySet()) {
				if (key.startsWith("hibernate3")) {
					this.configuration.setProperty(key, configurationProperties.get(key));
				}
			}
			this.sessionFactory = this.configuration.buildSessionFactory();
		} catch (HibernateException e) {
			// Swallow it
		}
	}
}