package com.hnjk.edu.teaching.model;

// default package

import java.util.Date;
import java.util.HashSet;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.security.model.OrgUnit;

/**
 * 考试/毕业论文批次预约表
 * <code>ExamSub</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午03:03:23
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMSUB")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamSub extends BaseModel {	
	
	private static final long serialVersionUID = 8118942900823207622L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
    private YearInfo yearInfo;//所属年度     
	
	@OneToOne(optional = true, cascade = { CascadeType.ALL})
	@JoinColumn(name = "GRADUATETACHEENDDATEID")
    private GradendDate gradendDate;//毕业环节截止时间
     
	@Column(name="BATCHNAME")
    private String batchName;//批次名称
     
	@Column(name="TERM")
    private String term;//学期
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="STARTTIME")
    private Date startTime;//预约开始时间
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENDTIME")
    private Date endTime;//预约结束时间
     
	@Column(name="EXAMSUBSTATUS")
    private String examsubStatus = "1";//#预约状态 1-未开放 2-开放 3-关闭
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXAMINPUTSTARTTIME")
    private Date examinputStartTime;//成绩录入开始时间
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXAMINPUTENDTIME")
    private Date examinputEndTime;//成绩录入截止时间
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXAMSUBENDTIME")
    private Date examsubEndTime;//添加预约信息截止时间
     
	@Column(name="ISSEATPUBLISHED",length=1)
    private String isseatPublished;//是否发布座位号
     
	@Column(name="BATCHTYPE")
    private String batchType;//预约类型  exam 考试 thesis 论文
    
	/*是否特殊批次 ,针对成人教学模式的考试，校外学习中心自己拟定考试批次，
	 * 学生预约考试不走线上流程，有后台人员直接导入,
	 * 判断条件：保存批次时，如果当前用户单位类型为校外学习中心，则设置这个字段值为"Y"，同事，向brSchool字段中插入当前单位ID
	 * 	校外学习中心用户查看考试批次条件为本单位批次+网考批次，本部查看全部
	 *  供学生预约考试的批次为 isSpecial = N
	 *  
	 * */
	@Column(name="ISSPECIAL",length=1)
    private String isSpecial = Constants.BOOLEAN_NO;
     
	@Column(name="ISLOCKED",length=1)
    private String isLocked;//是否锁定
     
	@Column(name="EXPORTNUM")
    private Long exportNum;//导出次数

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examSub")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("examCourseCode ASC")	
	@Where(clause="isDeleted=0")
	private Set<ExamInfo> examInfo = new HashSet<ExamInfo>(0);//考试课程信息
	
	/**3.0.9 新增卷面分和平时分比例*/
	@Column(name="WRITTENSCOREPER",length=6,scale=2)
	private Double writtenScorePer;//网络卷面分比例（论文初评比例）
	
	/**3.2.7 调整：各考试类型的比例统一跳到批次*/
	@Column(name="FACESTUDYSCOREPER",length=6,scale=2)
	private Double facestudyScorePer;//面授课程课程卷面分比例

	@Column(name="FACESTUDYSCOREPER2",length=6,scale=2)
	private Double facestudyScorePer2;//面授课程课程平时考核分比例
	
	@Deprecated
	@Column(name="FACESTUDYSCOREPER3",length=6,scale=2)
	private Double facestudyScorePer3;//面授课程课程网上学习分比例
	
	@Column(name="NETSIDESTUDYSCOREPER")
	private Double netsidestudyScorePer;//网络面授卷面分比例
	
	/**3.0.9新增所属校外学习中心 针对成人*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOOLID")
	private OrgUnit brSchool;//所属校外学习中心
	
	@Column(name="ISABNORMITYEND")
	private String isAbnormityEnd=Constants.BOOLEAN_NO;//异常成绩录入是否结束
	
	/**3.2.1 新增论文审核时间*/
	@Column(name="GRADUATEAUDITDATE")
	private Date graduateAuditDate;//论文审核时间
	
	@Column(name="COURSESCORETYPE")
	private String  courseScoreType;//成绩类型 
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="USUALSCOREINPUTSTARTTIME")
    private Date usualScoreInputStartTime;//平时成绩录入开始时间
     
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="USUALSCOREINPUTENDTIME")
    private Date usualScoreInputEndTime;//平时成绩录入截止时间
	
	@Column(name="EXAMTYPE")
	private String examType;//考试类型:N,Y,T,Q,R,G
	/**
	 * @return the courseScoreType
	 */
	public String getCourseScoreType() {
		return courseScoreType;
	}

	/**
	 * @param courseScoreType the courseScoreType to set
	 */
	public void setCourseScoreType(String courseScoreType) {
		this.courseScoreType = courseScoreType;
	}

	/**
	 * @return the graduateAuditDate
	 */
	public Date getGraduateAuditDate() {
		return graduateAuditDate;
	}

	/**
	 * @param graduateAuditDate the graduateAuditDate to set
	 */
	public void setGraduateAuditDate(Date graduateAuditDate) {
		this.graduateAuditDate = graduateAuditDate;
	}

	public String getIsAbnormityEnd() {
		return isAbnormityEnd;
	}

	public void setIsAbnormityEnd(String isAbnormityEnd) {
		this.isAbnormityEnd = isAbnormityEnd;
	}

	public OrgUnit getBrSchool() {
		return brSchool;
	}

	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
	}

	

	/**
	 * @return the facestudyScorePer
	 */
	public Double getFacestudyScorePer() {
		return facestudyScorePer;
	}

	/**
	 * @param facestudyScorePer the facestudyScorePer to set
	 */
	public void setFacestudyScorePer(Double facestudyScorePer) {
		this.facestudyScorePer = facestudyScorePer;
	}

	/**
	 * @return the netsidestudyScorePer
	 */
	public Double getNetsidestudyScorePer() {
		return netsidestudyScorePer;
	}

	/**
	 * @param netsidestudyScorePer the netsidestudyScorePer to set
	 */
	public void setNetsidestudyScorePer(Double netsidestudyScorePer) {
		this.netsidestudyScorePer = netsidestudyScorePer;
	}

	public Double getWrittenScorePer() {
		return writtenScorePer;
	}

	public void setWrittenScorePer(Double writtenScorePer) {
		this.writtenScorePer = writtenScorePer;
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
	
	public Set<ExamInfo> getExamInfo() {
		return examInfo;
	}

	public void setExamInfo(Set<ExamInfo> examInfo) {
		this.examInfo = examInfo;
	}
	
	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getExaminputEndTime() {
		return examinputEndTime;
	}

	public void setExaminputEndTime(Date examinputEndTime) {
		this.examinputEndTime = examinputEndTime;
	}

	public Date getExaminputStartTime() {
		return examinputStartTime;
	}

	public void setExaminputStartTime(Date examinputStartTime) {
		this.examinputStartTime = examinputStartTime;
	}

	public Date getExamsubEndTime() {
		return examsubEndTime;
	}

	public void setExamsubEndTime(Date examsubEndTime) {
		this.examsubEndTime = examsubEndTime;
	}

	public String getExamsubStatus() {
		return examsubStatus;
	}

	public void setExamsubStatus(String examsubStatus) {
		this.examsubStatus = examsubStatus;
	}

	public Long getExportNum() {
		return exportNum;
	}

	public void setExportNum(Long exportNum) {
		this.exportNum = exportNum;
	}

	public GradendDate getGradendDate() {
		return gradendDate;
	}

	public void setGradendDate(GradendDate gradendDate) {
		this.gradendDate = gradendDate;
	}

	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getIsseatPublished() {
		return isseatPublished;
	}

	public void setIsseatPublished(String isseatPublished) {
		this.isseatPublished = isseatPublished;
	}

	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public Date getUsualScoreInputStartTime() {
		return usualScoreInputStartTime;
	}

	public void setUsualScoreInputStartTime(Date usualScoreInputStartTime) {
		this.usualScoreInputStartTime = usualScoreInputStartTime;
	}

	public Date getUsualScoreInputEndTime() {
		return usualScoreInputEndTime;
	}

	public void setUsualScoreInputEndTime(Date usualScoreInputEndTime) {
		this.usualScoreInputEndTime = usualScoreInputEndTime;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public Double getFacestudyScorePer2() {
		return facestudyScorePer2;
	}

	public void setFacestudyScorePer2(Double facestudyScorePer2) {
		this.facestudyScorePer2 = facestudyScorePer2;
	}

	public Double getFacestudyScorePer3() {
		return facestudyScorePer3;
	}

	public void setFacestudyScorePer3(Double facestudyScorePer3) {
		this.facestudyScorePer3 = facestudyScorePer3;
	}

	
	
}