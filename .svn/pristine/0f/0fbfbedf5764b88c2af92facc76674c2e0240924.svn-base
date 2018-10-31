package com.opensymphony.workflow.spi.hibernate3;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * 上下步骤.
 * @author hzg
 *
 */
@MappedSuperclass
public class HibernateStepPrev implements Serializable {
	private static final long serialVersionUID = -3191636695303887992L;
	
	@EmbeddedId
	private StepPrevPK stepPrevPK;

	public StepPrevPK getStepPrevPK() {
		return stepPrevPK;
	}

	public void setStepPrevPK(StepPrevPK stepPrevPK) {
		this.stepPrevPK = stepPrevPK;
	}
	
}
