/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi.hibernate3;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.util.PropertySetDelegate;

/**
 * @author masini
 *         <p/>
 *         New Refactored Spring Managed Hibernate Store.
 *         Look at @link NewSpringHibernateFunctionalWorkflowTestCase for a use case.
 */
@Service
@Transactional
public class HibernateWorkflowStore extends AbstractHibernateWorkflowStore {

	@Autowired 
    public void initSessionFactory(SessionFactory sessionFactory,PropertySetDelegate propertySetDelegate) {
		super.setPropertySetDelegate(propertySetDelegate);
    	super.setSessionFactory(sessionFactory);
    }
	
    @Override
	@SuppressWarnings("unchecked")
	public void init(Map props) throws StoreException {
    }

    
}
