package com.hnjk.edu.basedata.model;

import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <code>Edumanager</code>基础数据-网院平台教务管理人员基本信息（本部各部门科室，校外学习中心人员）.
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:03:04
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_EDUMANAGER")
@PrimaryKeyJoinColumn(name = "SYSUSERID")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Edumanager extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	// 人员编号
	@Column(name = "TEACHERCODE", nullable = false, unique = true)
	private String teacherCode;

	// 中文拼音
	@Column(name = "NAMEPY")
	private String namePY;

	// 职称
	@Column(name = "TITLEOFTECHNICAL")
	private String titleOfTechnical;

	// 职务
	@Column(name = "DUTY")
	private String duty;

	// 文化程度
	@Column(name = "EDUCATION")
	private String education;

	// 性别
	@Column(name = "GENDER")
	private String gender;

	// 生日
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDAY")
	private Date birthday;

	// 办公电话
	@Column(name = "OFFICETEL")
	private String officeTel;

	// 家庭电话
	@Column(name = "HOMETEL")
	private String homeTel;

	// 手机
	@Column(name = "MOBILE")
	private String mobile;

	// 电子邮件
	@Column(name = "EMAIL")
	private String email;

	// 邮编
	@Column(name = "ZIPCODE")
	private String zipcode;

	// @Column(name="PHOTOPATH")
	// String photoPath;//相片路径 //注意：已放入系统用户表中

	@Column(name = "INTRODUCTION")
	private String introduction;// 简介

	// @Column(name="STATUS")
	// private Long status;//状态 0-激活;1-停止

	// 文号
	@Column(name = "DOCUMENTCODE")
	private String documentcode;

	/**
	 * 教师类型：课程负责人，主讲老师，辅导老师
	 * 3.0.4新增
	 * CodeTeacherType
	 */
	@Column(name = "TEACHERTYPE")
	private String teacherType;

	/**
	 * 授课模式：网络模式/成人面授模式
	 * 3.0.9新增
	 */
	@Column(name = "TEACHINGTYPE")
	private String teachingType;

	/** 3.1.15 */
	// 原单位
	@Column(name = "OLDUNITNAME")
	private String oldUnitName;

	// 是否有教师资格证
	@Column(name = "ISTEACHERCERT")
	private String isTeacherCert;

	// 教师相片路径
	@Column(name = "TEACHERCERTPHOTO")
	private String teacherCertPhoto;

	// 教师资格证相片路径
	@Column(name = "TEACHERCERTPHOTO1")
	private String teacherCertPhoto1;

	// 教师资格证相片路径
	@Column(name = "TEACHERCERTPHOTO2")
	private String teacherCertPhoto2;

	// 教师资格证相片路径
	@Column(name = "TEACHERCERTPHOTO3")
	private String teacherCertPhoto3;

	// 教师资格证相片路径
	@Column(name = "TEACHERCERTPHOTO4")
	private String teacherCertPhoto4;

	// 教师资格证相片路径
	@Column(name = "TEACHERCERTPHOTO5")
	private String teacherCertPhoto5;

	// 省份证号码
	@Column(name = "CERTNUM")
	private String certNum;

	// 编制 #取自字典 ，校内/校外 teacherOrgUnitType
	@Column(name = "ORGUNITTYPE")
	private String orgUnitType;

	// 最高学历，#取自字典 CodeEducation
	@Column(name = "EDUCATIONALLEVEL")
	private String educationalLevel;

	// 毕业时间
	@Column(name = "GRADUATEDATE")
	@Temporal(TemporalType.DATE)
	private Date graduateDate;

	// 毕业学校
	@Column(name = "GRADUATESCHOOL")
	private String graduateSchool;

	// 毕业专业
	@Column(name = "GRADUATEMAJOR")
	private String graduateMajor;

	// 单位住址
	@Column(name = "HOMEADDRESS")
	private String homeAddress;

	// 邮编
	@Column(name = "POSTCODE")
	private String postCode;

	// 审核日期
	@Column(name = "CHECKDATE")
	private Date checkDate;

	// 审核状态 ：Y(通过)N(未通过)W(未审核)D(待定)
	@Column(name = "CHECKSTATUS")
	private String checkStatus;

	// 备注
	@Column(name = "REMARKS")
	private String remarks;

	//（高基表新增“学位”字段）  取数据字典（CodeDegree）
	@Column(name = "DEGREE")
	private String degree;

	//（高基表新增）职级
	@Column(name = "POSITIONLEVEL")
	private String positionLevel;

	//（高基表新增字段）是否其它高校教师
	@Column(name = "ISOTHERTEACHER")
	private String isOtherTeacher;

	//（高基表新增字段）首次聘用时间
	@Column(name = "HIRINGTIME")
	@Temporal(TemporalType.DATE)
	private Date hiringTime;

	//(高基表新增字段)当前担任课程属于
	@Column(name = "CURRENTCOURSETYPE")
	private String currentCourseType;

	//(高基表新增字段)	其他职业（执业）资格
	@Column(name = "QUALIFICATION")
	private String qualification;

	// 当前担任课程
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course currentCourse;

	// 华工成教 手动输入当前担任课程
	@Column(name = "COURSENAME")
	private String coursename;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)

	// 担任课程专业
	@JoinColumn(name = "MAJORID")
	private Major currentMajor;

	// 华工成教 手动输入当前担任课程专业
	@Column(name = "MAJORNAME")
	private String majorname;

	// 广外 担任课程层次
	@Column(name = "COURSECLASSIC")
	private String courseClassic;

	//广外 授课班级
	@Column(name = "COURSECLASSES")
	private String courseClasses;

	/**
	 * 广外 聘用类型
	 * dictCode:CodeHireType
	 */
	@Column(name = "HIRETYPE")
	private String hireType;

	//广外 现工作单位
	@Column(name = "WORKPLACE")
	private String workPlace;

	/**
	 * 广外 所学专业学科类别
	 * dictCiode:CodeSpecialty
	 */
	@Column(name = "specialty")
	private String specialty;

	@Transient
	private String isMaster;// 是否是班主任
	
	@Transient
	private String isOnline;// 是否是班主任

	@Transient
	private String titleOfTechnical1;// 职称代码 拼写用(v3成教)

	@Transient
	private String teachTerm;// 教学学期(v3成教)子表重写
	
	@Transient
	private String isNewTeacher;// 是否新聘(v3成教) 子表重写
	
	@Transient
	private Date saveDate;// 录入时间(v3成教) 子表重写
	
	@Transient
	private User saveMan;// 录入人(v3成教) 子表重写
		
	public String getIsMaster() {
		String code = null == CacheAppManager
				.getSysConfigurationByCode("sysuser.role.master") ? ""
				: CacheAppManager.getSysConfigurationByCode(
						"sysuser.role.master").getParamValue();
		if (null != teacherType && teacherType.indexOf(code) != -1) {
			this.isMaster = "Y";
		} else {
			this.isMaster = "N";
		}
		return isMaster;
	}

	public String getIsOnline() {
		String code = null == CacheAppManager
				.getSysConfigurationByCode("sysuser.role.online") ? ""
				: CacheAppManager.getSysConfigurationByCode(
						"sysuser.role.online").getParamValue();
		if (null != teacherType && teacherType.indexOf(code) != -1) {
			this.isOnline = "Y";
		} else {
			this.isOnline = "N";
		}
		return isOnline;
	}

}