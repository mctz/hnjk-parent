/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.util;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.StoreException;


/**
 * @deprecated Use IsUserOwnerCondition instead
 * @see com.opensymphony.workflow.util.IsUserOwnerCondition
 *
 * Simple utility condition that returns true if the owner is the caller. Looks
 * at ALL current steps unless a stepId is given in the optional argument
 * "stepId".
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody </a>
 */
public class AllowOwnerOnlyCondition implements Condition {

	@Override
	@SuppressWarnings("unchecked")
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws StoreException {
        IsUserOwnerCondition isOwnerCondition = new IsUserOwnerCondition();

        return isOwnerCondition.passesCondition(transientVars, args, ps);
    }
}
