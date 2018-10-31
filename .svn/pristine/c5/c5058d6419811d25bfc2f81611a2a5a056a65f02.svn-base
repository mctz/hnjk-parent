/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi.hibernate;

import java.util.Properties;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.loader.XMLWorkflowFactory;


/**
 * DOCUMENT ME!
 * @author Marco.hu
 */
public class SpringWorkflowFactory extends XMLWorkflowFactory {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
	 * 
	 */
	private static final long serialVersionUID = 917525569760070737L;
	private String resource;//wokflows.xml的存放位置

    //~ Constructors ///////////////////////////////////////////////////////////

    public SpringWorkflowFactory() {
        super();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setReload(String reload) {
        this.reload = Boolean.valueOf(reload).booleanValue();
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void init() {
        try {
            Properties props = new Properties();
            props.setProperty("reload", getReload());
            props.setProperty("resource", getResource());

            super.init(props);//初始化配置属性
            initDone();//初始化
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    private String getReload() {
        return String.valueOf(reload);
    }

    private String getResource() {
        return resource;
    }
}
