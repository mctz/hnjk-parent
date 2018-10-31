/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.opensymphony.workflow.spi.Step;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@SuppressWarnings({"unchecked"})
public class JoinNodes {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Collection steps;
    private DummyStep dummy = new DummyStep();

    //~ Constructors ///////////////////////////////////////////////////////////

    public JoinNodes(Collection steps) {
        this.steps = steps;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Step getStep(int stepId) {
        for (Iterator iterator = steps.iterator(); iterator.hasNext();) {
            Step step = (Step) iterator.next();

            if (step.getStepId() == stepId) {
                return step;
            }
        }

        // no match, not ready to join ...
        // ... so return a dummy step that is always false
        return dummy;
    }

    //~ Inner Classes //////////////////////////////////////////////////////////
    
    private static class DummyStep implements Step {
        /**
		 * 
		 */
		private static final long serialVersionUID = 7596577753647268681L;

		@Override
		public int getActionId() {
            return -1;
        }

        @Override
		public String getCaller() {
            return null;
        }

        @Override
		public Date getDueDate() {
            return null;
        }

        @Override
		public long getEntryId() {
            return -1;
        }

        @Override
		public Date getFinishDate() {
            return null;
        }

        @Override
		public long getId() {
            return -1;
        }

        @Override
		public String getOwner() {
            return null;
        }

        @Override
		public long[] getPreviousStepIds() {
            return new long[0];
        }

        @Override
		public Date getStartDate() {
            return null;
        }

        @Override
		public String getStatus() {
            return null;
        }

        @Override
		public int getStepId() {
            return -1;
        }
    }
}
