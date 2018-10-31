/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created by IntelliJ IDEA.
 * User: plightbo
 * Date: Apr 29, 2002
 * Time: 11:12:41 PM
 */
package com.opensymphony.workflow.basic;

import com.opensymphony.workflow.WorkflowContext;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class BasicWorkflowContext implements WorkflowContext {
    //~ Instance fields ////////////////////////////////////////////////////////

	private static final long serialVersionUID = -8173121147504733659L;
	private String caller;

    //~ Constructors ///////////////////////////////////////////////////////////

    public BasicWorkflowContext(String caller) {
        this.caller = caller;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    @Override
	public String getCaller() {
        return caller;
    }

    @Override
	public void setRollbackOnly() {
        // does nothing, this is basic, remember!
    }
}
