/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created by IntelliJ IDEA.
 * User: plightbo
 * Date: Apr 29, 2002
 * Time: 10:46:56 PM
 */
package com.opensymphony.workflow;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * A runtime exceptiont that singals a serious and unexpected error in OSWorkflow.
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 */
public class InternalWorkflowException extends RuntimeException {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
	 * 
	 */
	private static final long serialVersionUID = -9098338979930047759L;
	private Throwable rootCause;

    //~ Constructors ///////////////////////////////////////////////////////////

    public InternalWorkflowException() {
    }

    public InternalWorkflowException(String s) {
        super(s);
    }

    public InternalWorkflowException(String s, Throwable rootCause) {
        super(s);
        this.rootCause = rootCause;
    }

    public InternalWorkflowException(Throwable rootCause) {
        this.rootCause = rootCause;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Throwable getRootCause() {
        return rootCause;
    }

    @Override
	public void printStackTrace() {
        super.printStackTrace();

        if (rootCause != null) {
            synchronized (System.err) {
                System.err.println("\nRoot cause:");
                rootCause.printStackTrace();
            }
        }
    }

    @Override
	public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);

        if (rootCause != null) {
            synchronized (s) {
                s.println("\nRoot cause:");
                rootCause.printStackTrace(s);
            }
        }
    }

    @Override
	public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);

        if (rootCause != null) {
            synchronized (s) {
                s.println("\nRoot cause:");
                rootCause.printStackTrace(s);
            }
        }
    }
}
