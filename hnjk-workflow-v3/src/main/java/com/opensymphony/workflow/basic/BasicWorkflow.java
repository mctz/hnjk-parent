/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created by IntelliJ IDEA.
 * User: plightbo
 * Date: Apr 29, 2002
 * Time: 11:12:05 PM
 */
package com.opensymphony.workflow.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.config.SpringConfiguration;


/**
 * A basic workflow implementation which does not read in
 * the current user from any context, but allows one to be
 * specified via the constructor. Also does not support rollbacks.
 */
@Service
public class BasicWorkflow extends AbstractWorkflow {

	@Autowired
	public void init(SpringConfiguration springConfiguration) {
		super.setConfiguration(springConfiguration);
	}
	
	// 紧紧开放给初始化时
	@Deprecated
	public BasicWorkflow() {
		
	}
	
    public BasicWorkflow(String caller) {
        super.context = new BasicWorkflowContext(caller);
    }
    
}
