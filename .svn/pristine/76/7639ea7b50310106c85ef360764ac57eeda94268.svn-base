package com.hnjk.edu.learning.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.Classes;

/**
 * 学生讨论小组表
 * <code>BbsGroup</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-27 上午11:43:12
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BBS_GROUP")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsGroup extends BaseModel{

	/**
	 * 组名
	 */
	@Column(name="GROUPNAME",nullable=false)
	private String groupName;

	/**
	 * 组描述
	 */
	@Column(name="GROUPDESCRIPT")
	private String groupDescript;

	/**
	 * 组长ID
	 */
	@Column(name="LEADERID")
	private String leaderId;

	/**
	 * 组长姓名
	 */
	@Column(name="LEADERNAME")
	private String leaderName;

	/**
	 * 关联课程ID
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;

	/**
	 * 关联班级ID
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSESID")
	private Classes classes;

	/**
	 * 状态0-正常 1-停用
	 */
	@Column(name="STATUS")
	private Integer status = 0;

	/**
	 * 小组成员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bbsGroup")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<BbsGroupUsers> groupUsers = new HashSet<BbsGroupUsers>(0);
	
	@Transient
	private String groupUserNames;
	@Transient
	private String groupUserIds;

	public String getGroupUserNames() {
		String groupUserNames = "";
		for (BbsGroupUsers u : getGroupUsers()) {
			groupUserNames += u.getStudentInfo().getStudentName()+",";
		}
		return groupUserNames;
	}

	
}
