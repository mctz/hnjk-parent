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
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;


/**
 *
 *
 * @author $Author: dk $
 * @version $Revision: 1.1 $
 */
@SuppressWarnings({"unchecked"})
public class JNDIValidator implements Validator {
    //~ Static fields/initializers /////////////////////////////////////////////


    //~ Methods ////////////////////////////////////////////////////////////////

    @Override
	public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException, WorkflowException {
        String location = (String) args.get(AbstractWorkflow.JNDI_LOCATION);

        if (location == null) {
            throw new WorkflowException(AbstractWorkflow.JNDI_LOCATION + " argument is null");
        }

        Validator validator;

        try {
            try {
                validator = (Validator) new InitialContext().lookup(location);
            } catch (NamingException e) {
                validator = (Validator) new InitialContext().lookup("java:comp/env/" + location);
            }
        } catch (NamingException e) {
            String message = "Could not look up JNDI Validator at: " + location;
            throw new WorkflowException(message, e);
        }

        validator.validate(transientVars, args, ps);
    }
}
