/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created on 20-ott-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.opensymphony.module.propertyset.hibernate3;

import java.util.HashMap;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;
import com.opensymphony.workflow.util.PropertySetDelegate;


/**
 * @author $author$
 * @version $Revision: 1.1 $
 */

@Service
public class DefaultHibernatePropertySetDelegate implements PropertySetDelegate {

	@Autowired
    private SessionFactory sessionFactory;

    public DefaultHibernatePropertySetDelegate() {
        super();
    }

    @Override
	public PropertySet getPropertySet(long entryId) {
        HashMap<String,Object> args = new HashMap<String,Object>();
        args.put("entityName", "OSWorkflowEntry");
        args.put("entityId", new Long(entryId));

        DefaultHibernateConfigurationProvider configurationProvider = new DefaultHibernateConfigurationProvider();
        
        configurationProvider.setSessionFactory(this.sessionFactory);
        
        args.put("configurationProvider", configurationProvider);

        return PropertySetManager.getInstance("hibernate3", args);
    }
}
