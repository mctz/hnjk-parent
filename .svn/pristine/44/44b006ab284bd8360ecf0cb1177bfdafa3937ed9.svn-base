package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 年级预交费用设置表
 * <code>GradeFeeRule</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-19 下午05:50:46
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_ROLL_FEERULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentFeeRule extends BaseModel implements Serializable {

	@Column(name = "FEERULENAME")
	private String feeRuleName;// 费用标准名称，默认以年级+专业+层次设置费用，也可以使用校外学习中心+年级+专业+层次设置费用

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;// 所属年级

	@Transient
	private String gradeid;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHSCHOOLID")
	private OrgUnit branchSchool;// 所属校外学习中心

	@Transient
	private String branchSchoolId;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major major;// 所属专业

	@Transient
	private String majorid;

	@Transient
	private String classicid;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")
	private Classic classic;// 所属层次

	@Column(name = "CREDITFEE", scale = 2)
	private Double creditFee;// 每学分缴费金额

	@Column(name = "TOTALFEE", scale = 2)
	private Double totalFee;// 学生应缴总费用：根据当前年级教学计划的学分总数*每学分缴费标准数

	@Column(name = "MEMO")
	private String memo;// 说明

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "studentFeeRule")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause = "isDeleted=0")
	private Set<StudentFeeRuleDetails> studentFeeRuleDetails = new HashSet<StudentFeeRuleDetails>(0);// 学生缴费标准设置明细
	
	@Column(name = "GENERATOR_FLAG")
	private String generatorFlag = Constants.BOOLEAN_NO;//是否生成繳費明細
	
	/**3.1.6新增教学模式*/
	@Column(name="SCHOOLTYPE")
	private String schoolType;//教学模式，字典值  CodeTeachingType

	public void addStuFeeRuleDetails(StudentFeeRuleDetails detail) {
		getStudentFeeRuleDetails().add(detail);
	}

	@Override
	public String toString() {
		String feeRule = getGrade().getGradeName() + "-"
				+ getBranchSchool().getUnitName() + "-" + getTotalFee();
		return feeRule;
	}

}