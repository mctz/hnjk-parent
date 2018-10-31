package com.hnjk.edu.teaching.model;

// default package

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.security.model.OrgUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 年度指导性教学计划表
 * <code>TeachingGuidePlan</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午02:38:01
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_GUIPLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeachingGuidePlan  extends BaseModel implements Serializable {

	private static final long serialVersionUID = -65585145361395924L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
    private Grade grade;//所属年级
     
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANID")
    private TeachingPlan teachingPlan;//所属教学计划套餐
    
	@Column(name="ISPUBLISHED",length=1)
    private String ispublished = Constants.BOOLEAN_NO;//是否发布
	
	@Column(name="TASK_FLAG",length=1)
	private String generationTask = Constants.BOOLEAN_NO; //生成任务书标识
	
	/**3.1.1 新增是否设置统考课程*/
	@Column(name="ISSTATCOURSE")
	private String isStatcourse = Constants.BOOLEAN_NO;//是否设置了统考课程
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "DEGREEFOREIGNLANGUAGE")
    private Course course;//教学计划的学位外语
	
	@Transient
	private OrgUnit unit;
	
	@Transient
	private String gradeid;
	
	@Transient
	private String teachingPlanid;
	
}