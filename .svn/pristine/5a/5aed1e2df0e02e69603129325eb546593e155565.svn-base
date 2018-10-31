/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.util.jndi;

import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;


/**
 *
 *
 * @author $Author: dk $
 * @version $Revision: 1.1 $
 */
@SuppressWarnings({"unchecked"})
public class JNDIFunctionProvider implements FunctionProvider {
    //~ Static fields/initializers /////////////////////////////////////////////

    //~ Methods ////////////////////////////////////////////////////////////////

    @Override
	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        String location = (String) args.get(AbstractWorkflow.JNDI_LOCATION);

        if (location == null) {
            throw new WorkflowException(AbstractWorkflow.JNDI_LOCATION + " argument is null");
        }

        location = location.trim();

        FunctionProvider provider;

        try {
            try {
                provider = (FunctionProvider) new InitialContext().lookup(location);
            } catch (NamingException e) {
                provider = (FunctionProvider) new InitialContext().lookup("java:comp/env/" + location);
            }
        } catch (NamingException e) {
            String message = "Could not get handle to JNDI FunctionProvider at: " + location;
            throw new WorkflowException(message, e);
        }

        provider.execute(transientVars, args, ps);
    }
}
