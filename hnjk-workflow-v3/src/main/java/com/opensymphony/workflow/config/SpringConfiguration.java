/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.config;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowFactory;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.util.DefaultVariableResolver;
import com.opensymphony.workflow.util.VariableResolver;

import java.net.URL;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author        Quake Wang
 * @since        2004-5-2
 * @version $Revision: 1.1 $
 *
 **/
@Service
public class SpringConfiguration implements Configuration {

	private VariableResolver variableResolver = new DefaultVariableResolver();
    
    @Autowired
    private WorkflowFactory factory;
    
    @Autowired
    private WorkflowStore store;


    @Override
	public boolean isInitialized() {
        return false;
    }

    @Override
	public boolean isModifiable(String name) {
        return factory.isModifiable(name);
    }

    @Override
	public String getPersistence() {
        return null;
    }

    @Override
	@SuppressWarnings("unchecked")
	public Map getPersistenceArgs() {
        return null;
    }

    public void setVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    @Override
	public VariableResolver getVariableResolver() {
        return variableResolver;
    }

    @Override
	public WorkflowDescriptor getWorkflow(String name) throws FactoryException {
        WorkflowDescriptor workflow = factory.getWorkflow(name);
        if (workflow == null) {
            throw new FactoryException("Unknown workflow name");
        }
        return workflow;
    }

    @Override
	public String[] getWorkflowNames() throws FactoryException {
        return factory.getWorkflowNames();
    }

    @Override
	public WorkflowStore getWorkflowStore() throws StoreException {
        return store;
    }

    @Override
	public void load(URL url) throws FactoryException {
    }

    @Override
	public boolean removeWorkflow(String workflow) throws FactoryException {
        return factory.removeWorkflow(workflow);
    }

    @Override
	public boolean saveWorkflow(String name, WorkflowDescriptor descriptor, boolean replace) throws FactoryException {
        return factory.saveWorkflow(name, descriptor, replace);
    }
}
