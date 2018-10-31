package com.hnjk.edu.teaching.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

/**
 * 成绩审核表，父表
 *  <code>ExamResultsAuditParent</code>
 * - 0 正常成绩审核记录
 * - 1 发布成绩审核记录.
 * @author：广东学苑教育发展有限公司.
 * @since： 2012-1-16 下午03:16:42
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_AUDITRESULTS")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="auditType",discriminatorType=DiscriminatorType.INTEGER)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamResultsAuditParent extends BaseModel {

	private static final long serialVersionUID = -7533518320248601274L;


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESULTSID")
	private ExamResults examResults;//学生成绩
	
	@Column(name="BEFOREWRITTENSCORE",length=255)
	private String beforeWrittenScore;//变更前卷面成绩
	
	@Column(name="BEFOREUSUALLYSCORE",length=255)
	private String beforeUsuallyScore;//变更前平时成绩
	
	@Column(name="BEFOREINTEGRATEDSCORE",length=255)
	private String beforeIntegratedScore;//变更前综合成绩
	
	@Column(name="BEFOREEXAMABNORMITY",length=50)
	private String beforeExamAbnormity;//变更前绩异常
	
	@Column(name="CHANGEDWRITTENSCORE",length=255)
	private String changedWrittenScore;//变更后卷面成绩
	
	@Column(name="CHANGEDUSUALLYSCORE",length=255)
	private String changedUsuallyScore;//变更后平时成绩
	
	@Column(name="CHANGEDINTEGRATEDSCORE",length=255)
	private String changedIntegratedScore;//变更后综合成绩
	
	@Column(name="CHANGEDEXAMABNORMITY",length=50)
	private String changedExamAbnormity;//变更后成绩异常
	
	@Column(name="CHANGEDMAN")
	private String changedMan;//变更人
	
	@Column(name="CHANGEDMANID")
	private String changedManId;//变更人ID
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="CHANGEDDATE")
	private Date changedDate;//变更日期
	
	@Column(name="AUDITMAN")
	private String auditMan;//审核人
	
	@Column(name="AUDITMANID")
	private String auditManId;//审核人ID
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="AUDITDATE")
	private Date auditDate;//审核日期
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="AUDITSTATUS")
	private Integer auditStatus;//审核状态  0 待审核 1审核通过

	/**3.2.1 新增审核类型*/
	//@Column(name="AUDITTYPE")
	//private Integer auditType = 0;//0-默认 1-发布成绩后的复审
	
	/**3.2.6 新增机考混合考成绩变更记录*/
	
	@Column(name="BEFOREWRITTENMACHINESCORE",length=255)
    private String beforeWrittenMachineScore;//变更前机考原始成绩
    
    @Column(name="BEFOREWRITTENHANDWORKSCORE",length=255)
    private String beforeWrittenHandworkScore;//变更前机考手工评卷成绩
	
    @Column(name="CHANGEDWRITTENMACHINESCORE",length=255)
    private String changedWrittenMachineScore;//变更后机考原始成绩
    
    @Column(name="CHANGEDWRITTENHANDWORKSCORE",length=255)
    private String changedWrittenHandworkScore;//变更后机考手工评卷成绩
    
    /**3.2.6 新增毕业论文成绩**/
    @Column(name="BEFOREFIRSTSCORE")
    private Double beforeFirstScore;//变更前初评成绩
    
    @Column(name="BEFORESECONDSCORE")
    private Double beforeSecondScore;//变更答辩成绩
    
    @Column(name="CHANGEDFIRSTSCORE")
    private Double changedFirstScore;//变更后初评成绩
    
    @Column(name="CHANGEDSECONDSCORE")
    private Double changedSecondScore;//变更后答辩成绩
    
    @Column(name="SCORETYPE",length=1)
    private Integer scoreType = 0;//成绩类型 0-普通成绩  1-毕业论文
    
	/**
	 * @return the auditType
	 */
//	public Integer getAuditType() {
//		return auditType;
//	}
//
//	/**
//	 * @param auditType the auditType to set
//	 */
//	public void setAuditType(Integer auditType) {
//		this.auditType = auditType;
//	}

    
 
	/**
	 * @return the scoreType
	 */
	public Integer getScoreType() {
		return scoreType;
	}

	
	/**
	 * @return the beforeFirstScore
	 */
	public Double getBeforeFirstScore() {
		return beforeFirstScore;
	}


	/**
	 * @param beforeFirstScore the beforeFirstScore to set
	 */
	public void setBeforeFirstScore(Double beforeFirstScore) {
		this.beforeFirstScore = beforeFirstScore;
	}


	/**
	 * @return the beforeSecondScore
	 */
	public Double getBeforeSecondScore() {
		return beforeSecondScore;
	}


	/**
	 * @param beforeSecondScore the beforeSecondScore to set
	 */
	public void setBeforeSecondScore(Double beforeSecondScore) {
		this.beforeSecondScore = beforeSecondScore;
	}


	/**
	 * @return the changedFirstScore
	 */
	public Double getChangedFirstScore() {
		return changedFirstScore;
	}


	/**
	 * @param changedFirstScore the changedFirstScore to set
	 */
	public void setChangedFirstScore(Double changedFirstScore) {
		this.changedFirstScore = changedFirstScore;
	}


	/**
	 * @return the changedSecondScore
	 */
	public Double getChangedSecondScore() {
		return changedSecondScore;
	}


	/**
	 * @param changedSecondScore the changedSecondScore to set
	 */
	public void setChangedSecondScore(Double changedSecondScore) {
		this.changedSecondScore = changedSecondScore;
	}


	/**
	 * @param scoreType the scoreType to set
	 */
	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * @return the beforeWrittenMachineScore
	 */
	public String getBeforeWrittenMachineScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.beforeWrittenMachineScore);
	}

	/**
	 * @param beforeWrittenMachineScore the beforeWrittenMachineScore to set
	 */
	public void setBeforeWrittenMachineScore(String beforeWrittenMachineScore) {
		this.beforeWrittenMachineScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), beforeWrittenMachineScore);
	}

	/**
	 * @return the beforeWrittenHandworkScore
	 */
	public String getBeforeWrittenHandworkScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.beforeWrittenHandworkScore);
	}

	/**
	 * @param beforeWrittenHandworkScore the beforeWrittenHandworkScore to set
	 */
	public void setBeforeWrittenHandworkScore(String beforeWrittenHandworkScore) {
		this.beforeWrittenHandworkScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), beforeWrittenHandworkScore);
	}

	/**
	 * @return the changedWrittenMachineScore
	 */
	public String getChangedWrittenMachineScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.changedWrittenMachineScore);
	}

	/**
	 * @param changedWrittenMachineScore the changedWrittenMachineScore to set
	 */
	public void setChangedWrittenMachineScore(String changedWrittenMachineScore) {
		this.changedWrittenMachineScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), changedWrittenMachineScore);
	}

	/**
	 * @return the changedWrittenHandworkScore
	 */
	public String getChangedWrittenHandworkScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.changedWrittenHandworkScore);
	}

	/**
	 * @param changedWrittenHandworkScore the changedWrittenHandworkScore to set
	 */
	public void setChangedWrittenHandworkScore(String changedWrittenHandworkScore) {
		this.changedWrittenHandworkScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), changedWrittenHandworkScore);
	}

	/**
	 * @return the beforeWrittenScore
	 */
	public String getBeforeWrittenScore() {
		
		if ("作弊".equals(this.beforeWrittenScore)||
			"缺考".equals(this.beforeWrittenScore)||
			"无卷".equals(this.beforeWrittenScore)||
			"其它".equals(this.beforeWrittenScore)) {
			
			return this.beforeWrittenScore;
		}else {
			return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.beforeWrittenScore);
		}
		
	}

	/**
	 * @param beforeWrittenScore the beforeWrittenScore to set
	 */
	public void setBeforeWrittenScore(String beforeWrittenScore) {
		
		if ("作弊".equals(beforeWrittenScore)||
			"缺考".equals(beforeWrittenScore)||
			"无卷".equals(beforeWrittenScore)||
			"其它".equals(beforeWrittenScore)) {
			this.beforeWrittenScore = beforeWrittenScore;
		}else {
			this.beforeWrittenScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), beforeWrittenScore);
			 
		}
	}

	/**
	 * @return the beforeUsuallyScore
	 */
	public String getBeforeUsuallyScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.beforeUsuallyScore);
	}

	/**
	 * @param beforeUsuallyScore the beforeUsuallyScore to set
	 */
	public void setBeforeUsuallyScore(String beforeUsuallyScore) {
		this.beforeUsuallyScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), beforeUsuallyScore);;
	}

	/**
	 * @return the beforeIntegratedScore
	 */
	public String getBeforeIntegratedScore() {
		
		if ("作弊".equals(this.beforeIntegratedScore)||
			"缺考".equals(this.beforeIntegratedScore)||
			"无卷".equals(this.beforeIntegratedScore)||
			"其它".equals(this.beforeIntegratedScore)) {
			
			return this.beforeIntegratedScore;
		}else {
			return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.beforeIntegratedScore);
		}
		
	}

	/**
	 * @param beforeIntegratedScore the beforeIntegratedScore to set
	 */
	public void setBeforeIntegratedScore(String beforeIntegratedScore) {
		
		if ("作弊".equals(beforeIntegratedScore)||
			"缺考".equals(beforeIntegratedScore)||
			"无卷".equals(beforeIntegratedScore)||
			"其它".equals(beforeIntegratedScore)) {
			this.beforeIntegratedScore = beforeIntegratedScore;
		}else {
			this.beforeIntegratedScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), beforeIntegratedScore);
		}
	}

	/**
	 * @return the beforeExamAbnormity
	 */
	public String getBeforeExamAbnormity() {
		return beforeExamAbnormity;
	}

	/**
	 * @param beforeExamAbnormity the beforeExamAbnormity to set
	 */
	public void setBeforeExamAbnormity(String beforeExamAbnormity) {
		this.beforeExamAbnormity = beforeExamAbnormity;
	}

	/**
	 * @return the examResults
	 */
	public ExamResults getExamResults() {
		return examResults;
	}

	/**
	 * @param examResults the examResults to set
	 */
	public void setExamResults(ExamResults examResults) {
		this.examResults = examResults;
	}

	/**
	 * @return the changedWrittenScore
	 */
	public String getChangedWrittenScore() {
		
		if ("作弊".equals(this.changedWrittenScore)||
			"缺考".equals(this.changedWrittenScore)||
			"无卷".equals(this.changedWrittenScore)||
			"其它".equals(this.changedWrittenScore)) {
			return this.changedWrittenScore;
		}else {
			return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.changedWrittenScore);
		}
	}

	/**
	 * @param changedWrittenScore the changedWrittenScore to set
	 */
	public void setChangedWrittenScore(String changedWrittenScore) {
		if ("作弊".equals(changedWrittenScore)||
			"缺考".equals(changedWrittenScore)||
			"无卷".equals(changedWrittenScore)||
			"其它".equals(changedWrittenScore)) {
			this.changedWrittenScore = changedWrittenScore;
		}else {
			this.changedWrittenScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), changedWrittenScore);
		}
	}

	/**
	 * @return the changedUsuallyScore
	 */
	public String getChangedUsuallyScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.changedUsuallyScore);
	}

	/**
	 * @param changedUsuallyScore the changedUsuallyScore to set
	 */
	public void setChangedUsuallyScore(String changedUsuallyScore) {
		this.changedUsuallyScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(), changedUsuallyScore);
	}

	/**
	 * @return the changedIntegratedScore
	 */
	public String getChangedIntegratedScore() {
		if ("作弊".equals(this.changedIntegratedScore)||
			"缺考".equals(this.changedIntegratedScore)||
			"无卷".equals(this.changedIntegratedScore)||
			"其它".equals(this.changedIntegratedScore)) {
			return this.changedIntegratedScore;
		}else {
			return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.examResults.getStudentInfo().getResourceid(), this.changedIntegratedScore);
		}
	}

	/**
	 * @param changedIntegratedScore the changedIntegratedScore to set
	 */
	public void setChangedIntegratedScore(String changedIntegratedScore) {
		
		if ("作弊".equals(changedIntegratedScore)||
			"缺考".equals(changedIntegratedScore)||
			"无卷".equals(changedIntegratedScore)||
			"其它".equals(changedIntegratedScore)) {
			
			this.changedIntegratedScore = changedIntegratedScore;
		}else {
			this.changedIntegratedScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.examResults.getStudentInfo().getResourceid(),changedIntegratedScore);
		}
	}

	/**
	 * @return the changedExamAbnormity
	 */
	public String getChangedExamAbnormity() {
		return changedExamAbnormity;
	}

	/**
	 * @param changedExamAbnormity the changedExamAbnormity to set
	 */
	public void setChangedExamAbnormity(String changedExamAbnormity) {
		this.changedExamAbnormity = changedExamAbnormity;
	}

	/**
	 * @return the changedMan
	 */
	public String getChangedMan() {
		return changedMan;
	}

	/**
	 * @param changedMan the changedMan to set
	 */
	public void setChangedMan(String changedMan) {
		this.changedMan = changedMan;
	}

	/**
	 * @return the changedManId
	 */
	public String getChangedManId() {
		return changedManId;
	}

	/**
	 * @param changedManId the changedManId to set
	 */
	public void setChangedManId(String changedManId) {
		this.changedManId = changedManId;
	}

	/**
	 * @return the changedDate
	 */
	public Date getChangedDate() {
		return changedDate;
	}

	/**
	 * @param changedDate the changedDate to set
	 */
	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	/**
	 * @return the auditMan
	 */
	public String getAuditMan() {
		return auditMan;
	}

	/**
	 * @param auditMan the auditMan to set
	 */
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	/**
	 * @return the auditManId
	 */
	public String getAuditManId() {
		return auditManId;
	}

	/**
	 * @param auditManId the auditManId to set
	 */
	public void setAuditManId(String auditManId) {
		this.auditManId = auditManId;
	}

	/**
	 * @return the auditDate
	 */
	public Date getAuditDate() {
		return auditDate;
	}

	/**
	 * @param auditDate the auditDate to set
	 */
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the auditStatus
	 */
	public Integer getAuditStatus() {
		return auditStatus;
	}

	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	
}
