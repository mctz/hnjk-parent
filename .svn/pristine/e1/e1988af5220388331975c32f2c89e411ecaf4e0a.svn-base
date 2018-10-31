package com.opensymphony.workflow.spi.hibernate3;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * 历史步骤节点表. 
 * @see {@link com.opensymphony.workflow.spi.hibernate.HibernateHistoryStep}
 */
@Entity
@Table(name = "OS_HISTORYSTEP")
@SequenceGenerator(name="seq_steps",sequenceName="seq_os_currentsteps")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateHistoryStep extends HibernateStep {

	private static final long serialVersionUID = -5011628150181541816L;

	public HibernateHistoryStep() {
    }

    public HibernateHistoryStep(HibernateStep step) {
        super(step);
    }
}
