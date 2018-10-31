package com.opensymphony.workflow.spi.hibernate3;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 当前步骤上下节点表.
 * @author hzg
 *
 */
@Entity
@Table(name = "OS_CURRENTSTEP_PREV") 
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateCurrentStepPrev extends HibernateStepPrev {
	private static final long serialVersionUID = 6534300514467117648L;

	
	
	
}
