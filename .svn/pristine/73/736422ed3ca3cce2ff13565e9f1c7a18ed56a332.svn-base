package com.hnjk.edu.teaching.model;

// default package

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;

/**
 * 教学计划(套餐)表. 
 * <code>TeachingPlan</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午01:46:35
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_PLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingPlan extends BaseModel implements Serializable{

	private static final long serialVersionUID = -823523200045001527L;
	
	@Column(name = "PLANNAME")
	private String planName;// 计划名称
		
	@Column(name = "TRAININGTARGET")
	private String trainingTarget;// 培养目标

	@Column(name = "EDUYEAR")
	private String eduYear;// 学制 #数据字典 2.5/3年

	/**3.0.8 中去掉，在明细表中加多是否主干标示*/
	//@Column(name = "MAINCOURSE")
	//private String mainCourse;// 主干课程，使用','隔开

	@Column(name = "LEARNINGDESCRIPT")
	private String learningDescript;// 修读说明

	@Column(name = "MEMO")
	private String memo;// 备注

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")
	private Classic classic;// 所属层次
	
	@Transient
	private String classicId;
	
	@Transient
	private String classicName;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major major;// 所属专业
	
	@Transient
	private String majorId;
	
	@Transient
	private String majorName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teachingPlan")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("courseType,term ASC")
	@Where(clause = "isDeleted=0")
	private Set<TeachingPlanCourse> teachingPlanCourses = new LinkedHashSet<TeachingPlanCourse>(0);// 教学计划 ： 教学计划课程 1:n
	
	

	/*3.0.8 新增最低毕业学分*/
	@Column(name="MINRESULT",length=6,scale=1)
	private Double minResult;//最低毕业学分

	/**3.0.9 新增办学模式和办学单位*/
	@Column(name="SCHOOLTYPE")
	private String schoolType;//#办学模式 ：纯网络模式/网络成人模式/成人模式
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOOLID")
	private OrgUnit orgUnit;// 办学单位
	
	@Transient
	private String unitShortName;
	
	@Column(name="APPLYPAPERMINRESULT",length=6,scale=1)
	private Double applyPaperMinResult = 0d;//毕业论文申请最低学分
	
	/**3.1.1 新增最低选修课门数，学位授予，用于毕业条件*/
	@Column(name="OPTIONALCOURSENUM")
	private Integer optionalCourseNum = 0;//选修课最少修读门数
	
	@Column(name="DEGREENAME")
	private String degreeName;//授予学位
	
	/**3.1.1 新增版本号、是否使用*/
	@Column(name="VERSIONNUM")
	private Integer versionNum = 1;//版本号：默认为1，以后本专业本层次的教学计划新增一次，版本+1
	
	@Column(name="ISUSED")
	private String isUsed = Constants.BOOLEAN_NO;// Y/N 是否使用，如果该专业已在年级教学计划中发布，则不能修改
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.DATE)
	private Date fillinDate = new Date();//创建时间
		
	@Transient
	private String mainCourse;//主干课
	
	
	@Column(name="theGraduationScore")
	private Double theGraduationScore;//结业申请最低学分
	
	
	
	//@Transient
	//private String planNameDetail;
	
	/**3.1.1 去掉计划名称，计划名称使用专业+层次*/
	//@Transient
	//private String planName;// 计划名称
	
	public Double getTheGraduationScore() {
		return theGraduationScore;
	}

	public void setTheGraduationScore(Double theGraduationScore) {
		this.theGraduationScore = theGraduationScore;
	}

	/**3.1.1 新增学位授予条件*/
	@Column(name="DEGREERULES")
	private String degreeRules;//学位授予条件，存储格式为key:value,多个条件使用","分割,条件的key存放在数据字段中,value为自定义
	
	@Transient
	private String teachingPlanName;
	
	/**3.2.1 新增查询字段*/
	@Formula("(select sum(t.credithour) from edu_teach_plancourse t where t.planid=resourceid and (t.coursetype='11' or t.coursetype='thesis')) ")
	private Double mustCourseTotalCreditHour;//必修课总学分
	
		
	/**
	 * @return the mustCourseTotalCreditHour
	 */
	public Double getMustCourseTotalCreditHour() {
		return mustCourseTotalCreditHour;
	}

	/**
	 * @param mustCourseTotalCreditHour the mustCourseTotalCreditHour to set
	 */
	public void setMustCourseTotalCreditHour(Double mustCourseTotalCreditHour) {
		this.mustCourseTotalCreditHour = mustCourseTotalCreditHour;
	}

	public String getDegreeRules() {
		return degreeRules;
	}

	public void setDegreeRules(String degreeRules) {
		this.degreeRules = degreeRules;
	}
	
	public void setMainCourse(String mainCourse) {
		this.mainCourse = mainCourse;
	}

	//public void setPlanName(String planName) {
	//	this.planName = planName;
	//}

	public Date getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public Integer getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Integer versionNum) {
		this.versionNum = versionNum;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public Integer getOptionalCourseNum() {
		return optionalCourseNum;
	}

	public void setOptionalCourseNum(Integer optionalCourseNum) {
		this.optionalCourseNum = optionalCourseNum;
	}

	public Double getApplyPaperMinResult() {
		return applyPaperMinResult;
	}

	public void setApplyPaperMinResult(Double applyPaperMinResult) {
		this.applyPaperMinResult = applyPaperMinResult;
	}

	public OrgUnit getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(OrgUnit orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getMainCourse() {
		String str = "";
		TeachingPlanCourse course = null;
		
		if(null != teachingPlanCourses && teachingPlanCourses.size()>0){
			Iterator<TeachingPlanCourse> iterator = getTeachingPlanCourses().iterator();
			for(;iterator.hasNext();){
				course = iterator.next();
				if(ExStringUtils.isNotEmpty(course.getIsMainCourse()) && course.getIsMainCourse().equals(Constants.BOOLEAN_YES)){
					str += course.getCourse().getCourseName() + ",";				
				}
				
			}
		}
		return str;
	}

	public Double getMinResult() {
		return minResult;
	}

	public void setMinResult(Double minResult) {
		this.minResult = minResult;
	}

	//public String getPlanNameDetail() {
		//planNameDetail = getPlanName() + " - " + getClassic().getClassicName();
	//	return planNameDetail;
	//}

	//public void setPlanNameDetail(String planNameDetail) {
	//	this.planNameDetail = planNameDetail;
	//}

	public String getEduYear() {
		return eduYear;
	}

	public void setEduYear(String eduYear) {
		this.eduYear = eduYear;
	}

	public String getLearningDescript() {
		return learningDescript;
	}

	public void setLearningDescript(String learningDescript) {
		this.learningDescript = learningDescript;
	}

//	public String getMainCourse() {
//		return mainCourse;
//	}
//
//	public void setMainCourse(String mainCourse) {
//		this.mainCourse = mainCourse;
//	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

//	public String getPlanName() {//教学计划名称：专业+层次+版本
//		if(null != getMajor().getMajorName() && null != getClassic().getClassicName()){
//			return getMajor().getMajorName()+" - "+getClassic().getClassicName();
//		}
	//	return "";
//	}

	//public void setPlanName(String planName) {
	//	this.planName = planName;
	//}

	public Set<TeachingPlanCourse> getTeachingPlanCourses() {
		return teachingPlanCourses;
	}

	public void setTeachingPlanCourses(
			Set<TeachingPlanCourse> teachingPlanCourses) {
		this.teachingPlanCourses = teachingPlanCourses;
	}

	public String getTrainingTarget() {
		return trainingTarget;
	}

	public void setTrainingTarget(String trainingTarget) {
		this.trainingTarget = trainingTarget;
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	public Classic getClassic() {
		return classic;
	}

	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public String getClassicId() {
		return classicId;
	}

	public void setClassicId(String classicId) {
		this.classicId = classicId;
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}	

	public String getUnitShortName() {
		return unitShortName;
	}

	public void setUnitShortName(String unitShortName) {
		this.unitShortName = unitShortName;
	}

	/**
	 * @return the teahcingPlanName
	 */
	public String getTeachingPlanName() {
		return getMajor().getMajorName()+" - "+getClassic().getClassicName()+" ("+getVersionNum()+")";
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getClassicName() {
		return classicName;
	}

	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	
}