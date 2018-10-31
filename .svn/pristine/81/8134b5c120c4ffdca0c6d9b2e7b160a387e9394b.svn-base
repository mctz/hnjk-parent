/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.util;

import java.util.List;
import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.util.TextUtils;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;


/**
 * Simple utility condition that returns true if the current step's status is
 * the same as the required argument "status". Looks at ALL current steps unless
 * a stepId is given in the optional argument "stepId".
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 */
public class StatusCondition implements Condition {

    @Override
	@SuppressWarnings("unchecked")
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws StoreException {
        String status = TextUtils.noNull((String) args.get("status"));
        int stepId = 0;
        Object stepIdVal = args.get("stepId");

        if (stepIdVal != null) {
            try {
                stepId = Integer.parseInt(stepIdVal.toString());
            } catch (Exception ex) {
            }
        }

        WorkflowEntry entry = (WorkflowEntry) transientVars.get("entry");
        WorkflowStore store = (WorkflowStore) transientVars.get("store");
        List<HibernateCurrentStep> currentSteps = store.findCurrentSteps(entry.getId());

        if (stepId == 0) {
        	for (HibernateCurrentStep step : currentSteps) {
                if (status.equals(step.getStatus())) {
                    return true;
                }
            }
        } else {
        	for (HibernateCurrentStep step : currentSteps) {
                if (stepId == step.getStepId()) {
                    if (status.equals(step.getStatus())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
