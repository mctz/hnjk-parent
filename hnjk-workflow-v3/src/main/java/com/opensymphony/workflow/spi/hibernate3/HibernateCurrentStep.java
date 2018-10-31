package com.opensymphony.workflow.spi.hibernate3;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 当前步骤节点表. 
 * @see {@link com.opensymphony.workflow.spi.hibernate.HibernateHistoryStep}
 */
@Entity
@Table(name = "OS_CURRENTSTEP")
@SequenceGenerator(name="seq_steps",sequenceName="seq_os_currentsteps")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateCurrentStep extends HibernateStep {

	private static final long serialVersionUID = 4000043953696821791L;

	public HibernateCurrentStep() {
    }

    public HibernateCurrentStep(HibernateStep step) {
        super(step);
    }
}
