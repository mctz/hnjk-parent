package com.opensymphony.workflow.spi.hibernate3;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 历史步骤上下节点.
 * @author hzg
 *
 */
@Entity
@Table(name = "OS_HISTORYSTEP_PREV")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateHistoryStepPrev extends HibernateStepPrev{

	private static final long serialVersionUID = -5055842961935542093L;
	
}
