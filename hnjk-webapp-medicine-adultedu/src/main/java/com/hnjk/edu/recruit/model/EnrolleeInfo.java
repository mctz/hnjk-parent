package com.hnjk.edu.recruit.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.security.model.OrgUnit;

/**
 * <code>EnrolleeInfo</code>学生报名信息.
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 上午11:48:03
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_RECRUIT_ENROLLEEINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnrolleeInfo extends BaseModel implements Serializable {
	private static final long serialVersionUID = -8156479403556963569L;

	@Column(name = "FROMMEDIA")
	private String fromMedia;							 // 媒体来源：基础数据

	@Column(name = "ENROLLEECODE")
	private String enrolleeCode;						 // 考试号，用于新生上报
	//@Column(name = "EXAMCERTIFICATENO",unique=true)
	@Column(name = "EXAMCERTIFICATENO")
	private String examCertificateNo;					 // 准考证号

	@Column(name = "EDUCATIONALLEVEL")
	private String educationalLevel;					 // 最后学历：基础数据

	@Column(name = "GRADUATESCHOOL")
	private String graduateSchool;						 // 毕业院校

	@Column(name = "GRADUATESCHOOLCODE")
	private String graduateSchoolCode;					 // 毕业学校代码

	@Column(name = "GRADUATEMAJOR")
	private String graduateMajor;						 // 毕业专业

	@Column(name = "GRADUATEID")
	private String graduateId;							 // 毕业证书号

	@Temporal(TemporalType.DATE)
	@Column(name = "GRADUATEDATE")
	private Date graduateDate;							 // 毕业日期

	@Column(name = "FROMOTHERMEDIA")
	private String fromOtherMedia;						 // 其他媒体来源

	@Column(name = "ENROLLEETYPE")
	private Long enrolleeType = 0L;						 // 报名类型：0-现场报名;1-网上报名

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SIGNUPDATE")
	private Date signupDate;				 // 报名日期

	@Column(name = "ISPREPARATORY")
	private String isPreparatory = Constants.BOOLEAN_NO; // 是否为进修生 默认为否

	@Column(name = "SIGNUPFLAG")
	private String signupFlag = Constants.BOOLEAN_WAIT;	 // 报名资格标示位 默认为未通过报名资格审核

	@Column(name = "ENTRANCEFLAG")
	private String entranceflag= Constants.BOOLEAN_WAIT;   // 入学资格标示位：W-待审核;Y-审核通过;N-审核不通过   默认为审核不通过

	@Column(name = "ISAPPLYNOEXAM")
	private String isApplyNoexam;						 // 是否申请免试入学

	@Column(name = "NOEXAMFLAG")
	private String noExamFlag;							 // 通过免试入学资格标示位

	@Column(name = "ORIGINALPOINT")
	private Long originalPoint;							 // 原始分

	@Transient
	private String originalPointStr;					 // 原始分(临时)

	@Column(name = "TOTALPOINT")
	private Long totalPoint;							 // 总分

	@Transient
	private String totalPointStr;						 // 总分(临时)

	@Column(name = "ISPRINTADMISSION")
	private String isPrintAdmission=Constants.BOOLEAN_NO;// 是否打印录取通知书 默认为否

	@Column(name = "ISMATRICULATE")
	private String isMatriculate =Constants.BOOLEAN_NO;  // 是否录取 默认为否

	@Column(name = "ISSPECIALLYCODE")
	private String isSpeciallycode=Constants.BOOLEAN_NO; // 是否特批 默认为否

	@Column(name = "MATRICULATENOTICENO")
	private String matriculateNoticeNo;					 // 录取通知书条形码 对应学生学号

	// TODO:旧字段已被teachType替换，以后可删除，但要注意字典
	@Column(name = "STUTYMODE")
	private String stutyMode;							 // 学习方式 

	//20171227 改为是否下载录取通知书字段
	@Column(name = "ISPRINT")
	private String isPrint;								 // 是否打印
	
	@Column(name = "ISQUERY")
	private String isQuery;	//是否有查询录取通知书
	
	//@Column(name = "RECRUITEPOSTID")
	//private String recruitePostId;				     // 招生点ID

	@Column(name = "OPTIONALSUBJECTID")
	private String optionalSubjectId;					 // 选考科目ID

	@Column(name = "ISCHECKIN")
	private String isCheckIn=Constants.BOOLEAN_NO;		 // 是否报到 默认为否

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	private Grade grade;								 // 入学资格审核通过的年级

	@Temporal(TemporalType.DATE)
	@Column(name = "RECRUITPLANDATE")
	private Date recruitplanDate;						 // 审核批次时间，入学资格被调整到另一批次的情况

	@Column(name = "RECRUITPLAN")
	private String recruitPlan;						 	 // 审核批次ID，入学资格被调整的情况

	@Column(name = "ENTRANCECHECKMAN")
	private String entranceCheckMan;					 // 入学资格审核人

	@Column(name = "ENTRANCECHECKMANID")
	private String entranceCheckManId;					 // 入学资格审核人ID

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRANCECHECKDATE")
	private Date entranceCheckDate;						 // 入学资格审核日期

	@Column(name = "NOEXAMCHECKMAN")
	private String noexamCheckMan;						 // 免试资格审核人

	@Column(name = "NOEXAMCHECKMANID")
	private String noexamCheckManId;					 // 免试资格审核人ID

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NOEXAMCHECKDATE")
	private Date noexamCheckDate;						 // 免试资格审核日期

	// ADDED 新增职业状态字段 MODIFY: 3.0.6 移动到学生基本信息表中
	//@Column(name = "WORKSTATUS")
	//private String workStatus;						 // 职业状态

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTBASEINFOID")
	private StudentBaseInfo studentBaseInfo; 			 // 学生基本信息

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHSCHOOLID")
	private OrgUnit branchSchool;						 // 所属校外学习中心

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "RECRUITMAJORID")
	private RecruitMajor recruitMajor;					 // 招生专业

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enrolleeinfo")
//	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL })
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	@OrderBy("showOrder ASC")
//	@Where(clause="isDeleted=0")
//	private Set<ExamScore> examScore = new HashSet<ExamScore>(0);//学生入学考试成绩

	/* 3.0.3 新增 MODIFY: 3.0.6 移动到学生基本信息表中 */
	//@Column(name = "INDUSTRYTYPE")
	//private String industryType;						  // 行业类别

	/*
	 * 学生注册号自左至右排列方式为：
	 * 两位入学年份（例如：2009年入学即09）+W1（W1即网络教育编码）+一位培养层次编码（1高起本；2高起专；3专升本；4专业硕士；5本科第二学位；6研究生课程进修；7其他）+五位试点高校编码+八位随机数（数字编码）。
	 * 倒数第五位0表示为春季学生，1表示为秋季学生。如：10W121057100000001（2010年入学高起专春季学生）
	 */
	@Column(name = "REGISTORNO")
	private String registorNo;							  // 录取时生成，用于上报新生

	@Column(name = "REGISTER_FLAG")
	private String registorFlag;						  // 注册标识

	@Column(name = "ISQUALIFIED")
	private String isQualified = Constants.BOOLEAN_NO;	  // 是否满足报名条件，如果学习中心的下限人数已超过，则将这个学校中心的学生设置为Y

	 /**3.1.3新增教学模式*/
	 @Column(name="TEACHINGTYPE")
	 private String teachingType;						  //学习形式 字典值
	 
	 @Column(name="COURSEGROUPNAME")
	 private String courseGroupName;				     //考试科目组名    专升本文科、  专升本理科...
	 
	 /**3.1.8 新增是否机考查询*/
	 @Column(name="ISMACHINEEXAMSCORE")
	private String isMachineExamScore = Constants.BOOLEAN_NO;//是否可查询机考成绩	
	
//	@OneToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
//	@JoinColumn(name="STUDENTID")
//	private Set<StudentFactFee> studentFactFees = new HashSet<StudentFactFee>(0);//学生费用情况
	
	/**3.1.11新增免试备注*/
	@Column(name="NOEXAMCHECKMEMO",length=500)
	 private String noExamCheckMemo;
	
	/** 2015/11/10 新增未报到原因、是否跟读(Y/N)、备注 **/
	@Column(name="NOREPORTREASON",length=500)
	private String noReportReason;// 未报到原因
	
	@Column(name="ISSTUDYFOLLOW")
	private String isStudyFollow;// 是否跟读(Y/N)
	
	@Column(name="MEMO", length=1000)
	private String memo;// 备注
	
	@Column(name="ENROLLNO")
	private String enrollNO;// 录取编号 
	//20170510 新增，用于广大需要核对的录取流水号，以教学点、年级 为单
	@Column(name="REGISTORSERIALNO")
	private String registorSerialNO;//广大录取流水号
	
	@Column(name="feeNO")
	private String feeNO;//缴费号
	//预分配教学点   20180626，广外需求
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "PREDISTRIBUTIONID")
	private Predistribution predistribution;
	
	

	public Predistribution getPredistribution() {
		return predistribution;
	}

	public void setPredistribution(Predistribution predistribution) {
		this.predistribution = predistribution;
	}

	//@Column(name = "EDUYEAR")
	@Transient
	private String eduYear;//学制
	
	@Transient
	private String recruitMajorCode;
	
	@Transient
	private String CCMD; //临时存放层次代码
	
	@Transient
	private String KSH; //临时存放考生号
	
	public String getKSH() {
		return KSH;
	}

	public void setKSH(String kSH) {
		KSH = kSH;
	}

	/**
	 * @return the studentFactFees
	 */
//	public Set<StudentFactFee> getStudentFactFees() {
//		return studentFactFees;
//	}
//
//	/**
//	 * @param studentFactFees the studentFactFees to set
//	 */
//	public void setStudentFactFees(Set<StudentFactFee> studentFactFees) {
//		this.studentFactFees = studentFactFees;
//	}
	
	
	
	public String getCCMD() {
		return CCMD;
	}

	public String getRegistorSerialNO() {
		return registorSerialNO;
	}

	public void setRegistorSerialNO(String registorSerialNO) {
		this.registorSerialNO = registorSerialNO;
	}

	public void setCCMD(String cCMD) {
		CCMD = cCMD;
	}

	/**
	 * @return the isMachineExamScore
	 */
	public String getIsMachineExamScore() {
		return isMachineExamScore;
	}

	/**
	 * @return the noExamCheckMemo
	 */
	public String getNoExamCheckMemo() {
		return noExamCheckMemo;
	}

	/**
	 * @param noExamCheckMemo the noExamCheckMemo to set
	 */
	public void setNoExamCheckMemo(String noExamCheckMemo) {
		this.noExamCheckMemo = noExamCheckMemo;
	}

	/**
	 * @param isMachineExamScore the isMachineExamScore to set
	 */
	public void setIsMachineExamScore(String isMachineExamScore) {
		this.isMachineExamScore = isMachineExamScore;
	}

	/**
	 * @return the courseGroupName
	 */
	public String getCourseGroupName() {
		return courseGroupName;
	}

	/**
	 * @param courseGroupName the courseGroupName to set
	 */
	public void setCourseGroupName(String courseGroupName) {
		this.courseGroupName = courseGroupName;
	}

	/**
	 * @return the teachingType
	 */
	public String getTeachingType() {
		return teachingType;
	}

	/**
	 * @param teachingType the teachingType to set
	 */
	public void setTeachingType(String teachingType) {
		this.teachingType = teachingType;
	}

	public String getIsQualified() {
		return isQualified;
	}

	public void setIsQualified(String isQualified) {
		this.isQualified = isQualified;
	}

	public String getRegistorNo() {
		return registorNo;
	}

	public void setRegistorNo(String registorNo) {
		this.registorNo = registorNo;
	}
		

	public OrgUnit getBranchSchool() {
		return branchSchool;
	}

	public void setBranchSchool(OrgUnit branchSchool) {
		this.branchSchool = branchSchool;
	}

	public Long getEnrolleeType() {
		return enrolleeType;
	}

	public void setEnrolleeType(Long enrolleeType) {
		this.enrolleeType = enrolleeType;
	}

	public Date getEntranceCheckDate() {
		return entranceCheckDate;
	}

	public void setEntranceCheckDate(Date entranceCheckDate) {
		this.entranceCheckDate = entranceCheckDate;
	}

	public String getEntranceCheckMan() {
		return entranceCheckMan;
	}

	public void setEntranceCheckMan(String entranceCheckMan) {
		this.entranceCheckMan = entranceCheckMan;
	}

	public String getEntranceCheckManId() {
		return entranceCheckManId;
	}

	public void setEntranceCheckManId(String entranceCheckManId) {
		this.entranceCheckManId = entranceCheckManId;
	}

	public String getEntranceflag() {
		return entranceflag;
	}

	public void setEntranceflag(String entranceflag) {
		this.entranceflag = entranceflag;
	}

	public String getFromMedia() {
		return fromMedia;
	}

	public void setFromMedia(String fromMedia) {
		this.fromMedia = fromMedia;
	}

	public String getFromOtherMedia() {
		return fromOtherMedia;
	}

	public void setFromOtherMedia(String fromOtherMedia) {
		this.fromOtherMedia = fromOtherMedia;
	}

	

	/**
	 * @return the grade
	 */
	public Grade getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getIsApplyNoexam() {
		return isApplyNoexam;
	}

	public void setIsApplyNoexam(String isApplyNoexam) {
		this.isApplyNoexam = isApplyNoexam;
	}

	public String getIsCheckIn() {
		return isCheckIn;
	}

	public void setIsCheckIn(String isCheckIn) {
		this.isCheckIn = isCheckIn;
	}

	public String getIsMatriculate() {
		return isMatriculate;
	}

	public void setIsMatriculate(String isMatriculate) {
		this.isMatriculate = isMatriculate;
	}

	public String getIsPreparatory() {
		return isPreparatory;
	}

	public void setIsPreparatory(String isPreparatory) {
		this.isPreparatory = isPreparatory;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getIsPrintAdmission() {
		return isPrintAdmission;
	}

	public void setIsPrintAdmission(String isPrintAdmission) {
		this.isPrintAdmission = isPrintAdmission;
	}

	public String getIsSpeciallycode() {
		return isSpeciallycode;
	}

	public void setIsSpeciallycode(String isSpeciallycode) {
		this.isSpeciallycode = isSpeciallycode;
	}

	public String getMatriculateNoticeNo() {
		return matriculateNoticeNo;
	}

	public void setMatriculateNoticeNo(String matriculateNoticeNo) {
		this.matriculateNoticeNo = matriculateNoticeNo;
	}

	public Date getNoexamCheckDate() {
		return noexamCheckDate;
	}

	public void setNoexamCheckDate(Date noexamCheckDate) {
		this.noexamCheckDate = noexamCheckDate;
	}

	public String getNoexamCheckMan() {
		return noexamCheckMan;
	}

	public void setNoexamCheckMan(String noexamCheckMan) {
		this.noexamCheckMan = noexamCheckMan;
	}

	public String getNoexamCheckManId() {
		return noexamCheckManId;
	}

	public void setNoexamCheckManId(String noexamCheckManId) {
		this.noexamCheckManId = noexamCheckManId;
	}

	public String getNoExamFlag() {
		return noExamFlag;
	}

	public void setNoExamFlag(String noExamFlag) {
		this.noExamFlag = noExamFlag;
	}

	public String getOptionalSubjectId() {
		return optionalSubjectId;
	}

	public void setOptionalSubjectId(String optionalSubjectId) {
		this.optionalSubjectId = optionalSubjectId;
	}

	public Long getOriginalPoint() {
		return originalPoint;
	}

	public void setOriginalPoint(Long originalPoint) {
		this.originalPoint = originalPoint;
	}


	public RecruitMajor getRecruitMajor() {
		return recruitMajor;
	}

	public void setRecruitMajor(RecruitMajor recruitMajor) {
		this.recruitMajor = recruitMajor;
	}

	public String getRecruitPlan() {
		return recruitPlan;
	}

	public void setRecruitPlan(String recruitPlan) {
		this.recruitPlan = recruitPlan;
	}

	public Date getRecruitplanDate() {
		return recruitplanDate;
	}

	public void setRecruitplanDate(Date recruitplanDate) {
		this.recruitplanDate = recruitplanDate;
	}

	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public String getSignupFlag() {
		return signupFlag;
	}

	public void setSignupFlag(String signupFlag) {
		this.signupFlag = signupFlag;
	}

	// public StudentBaseInfo getStudent() {
	// return studentBaseInfo;
	// }
	//
	// public void setStudent(StudentBaseInfo studentBaseInfo) {
	// this.studentBaseInfo = studentBaseInfo;
	// }

	public String getStutyMode() {
		return stutyMode;
	}

	public void setStutyMode(String stutyMode) {
		this.stutyMode = stutyMode;
	}

	public Long getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(Long totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getEducationalLevel() {
		return educationalLevel;
	}

	public void setEducationalLevel(String educationalLevel) {
		this.educationalLevel = educationalLevel;
	}

	public String getEnrolleeCode() {
		return enrolleeCode;
	}

	public void setEnrolleeCode(String enrolleeCode) {
		this.enrolleeCode = enrolleeCode;
	}

	public Date getGraduateDate() {
		return graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	public String getGraduateId() {
		return graduateId;
	}

	public void setGraduateId(String graduateId) {
		this.graduateId = graduateId;
	}

	public String getGraduateMajor() {
		return graduateMajor;
	}

	public void setGraduateMajor(String graduateMajor) {
		this.graduateMajor = graduateMajor;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getGraduateSchoolCode() {
		return graduateSchoolCode;
	}

	public void setGraduateSchoolCode(String graduateSchoolCode) {
		this.graduateSchoolCode = graduateSchoolCode;
	}

	public String getExamCertificateNo() {
		return examCertificateNo;
	}

	public void setExamCertificateNo(String examCertificateNo) {
		this.examCertificateNo = examCertificateNo;
	}

	public StudentBaseInfo getStudentBaseInfo() {
		return studentBaseInfo;
	}

	public void setStudentBaseInfo(StudentBaseInfo studentBaseInfo) {
		this.studentBaseInfo = studentBaseInfo;
	}


	public String getOriginalPointStr() {
		if (originalPoint == null) {
			originalPointStr = "";
		} else {
			if (originalPoint == -1) {
				originalPointStr = "作弊";
			} else if (originalPoint == -2) {
				originalPointStr = "缺考";
			} else if (originalPoint < -2) {
				originalPointStr = "0";
			} else {
				originalPointStr = originalPoint.toString();
			}
		}
		return originalPointStr;
	}

	public String getTotalPointStr() {
		if (totalPoint == null) {
			totalPointStr = "";
		} else {
			if (totalPoint == -1) {
				totalPointStr = "作弊";
			} else if (totalPoint == -2) {
				totalPointStr = "缺考";
			} else if (totalPoint < -2) {
				totalPointStr = "0";
			} else {
				totalPointStr = totalPoint.toString();
			}
		}
		return totalPointStr;
	}

	public String getRegistorFlag() {
		return registorFlag;
	}

	public void setRegistorFlag(String registorFlag) {
		this.registorFlag = registorFlag;
	}
	
	public String getRecruitMajorCode() {
		return recruitMajorCode;
	}
	public void setRecruitMajorCode(String recruitMajorCode) {
		this.recruitMajorCode = recruitMajorCode;
	}

	public void setTotalPointStr(String totalPointStr) {
		this.totalPointStr = totalPointStr;
	}

	public String getNoReportReason() {
		return noReportReason;
	}

	public void setNoReportReason(String noReportReason) {
		this.noReportReason = noReportReason;
	}

	public String getIsStudyFollow() {
		return isStudyFollow;
	}

	public void setIsStudyFollow(String isStudyFollow) {
		this.isStudyFollow = isStudyFollow;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getEnrollNO() {
		return enrollNO;
	}

	public void setEnrollNO(String enrollNO) {
		this.enrollNO = enrollNO;
	}

	public String getFeeNO() {
		return feeNO;
	}

	public void setFeeNO(String feeNO) {
		this.feeNO = feeNO;
	}

	public String getEduYear() {
		return eduYear;
	}

	public void setEduYear(String eduYear) {
		this.eduYear = eduYear;
	}

	public String getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}
}