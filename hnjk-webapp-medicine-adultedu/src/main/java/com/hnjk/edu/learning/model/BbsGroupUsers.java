package com.hnjk.edu.learning.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 小组成员表
 * <code>BbsGroupUsers</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-27 上午11:51:33
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BBS_GROUPUSERS")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsGroupUsers extends BaseModel{

	/**
	 * 学生信息
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;

	/**
	 * 所属小组
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private BbsGroup bbsGroup;
	
}
