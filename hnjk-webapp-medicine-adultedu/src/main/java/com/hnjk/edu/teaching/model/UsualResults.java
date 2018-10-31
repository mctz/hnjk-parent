package com.hnjk.edu.teaching.model;

import java.math.BigDecimal;
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
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

/**
 * 学生平时成绩表
 * <code>UsualResults</code><p>
 * 注意：每个分项都以100分计算，最后使用计分规则中的百分比,换算为比例分值 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-12-20 上午11:30:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_USUALRESULTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UsualResults extends BaseModel{

	private static final long serialVersionUID = 1696156020220219026L;
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private String term;//学期
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学生学籍信息
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//课程
	
	
	@Column(name="MAJORCOURSEID")
	private String majorCourseId;//专业课程ID
	
	@Column(name="BBSRESULTS")
	private String bbsResults;//网络辅导分，写入时加密，读出是解密 参见 @link{ScoreEncryptionDecryptionUtil}
	
	@Column(name="EXERCISERESULTS")
	private String exerciseResults;//作业练习分，加密 ，写入时加密，读出是解密 参见 @link{ScoreEncryptionDecryptionUtil}
	
	@Column(name="SELFTESTRESULTS")
	private String selftestResults;//同步自测分,加密 ，写入时加密，读出是解密 参见 @link{ScoreEncryptionDecryptionUtil}
	
	@Column(name="OTHERRESULTS")
	private String otherResults;//实践及其他分 ，写入时加密，读出是解密 参见 @link{ScoreEncryptionDecryptionUtil}
	
	@Column(name="FACERESULTS")
	private String faceResults;//面授考勤分，写入时加密，读出是解密 参见 @link{ScoreEncryptionDecryptionUtil}
	
	@Column(name="FILLINMANID")
	private String fillinManId;//录入人ID
	
	@Column(name="FILLINMAN")
	private String fillinMan;//录入人姓名
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate = new Date();//录入时间

	@Column(name="USUALRESULTS")
	private String usualResults;//平时分总分 写入时加密，读出是解密 参见 @link{ScoreEncryptionDecryptionUtil}
	
	/**3.1.1 新增*/
	@Column(name="ASKQUESTIONRESULTS")
	private String askQuestionResults;//随堂问答分
	
	@Column(name="COURSEEXAMRESULTS")
	private String courseExamResults;//随堂练习分
	
	/**3.1.11 新增平时成绩状态和使用规则*/
	@Column(name="STATUS")
    private String status = "0";//平时成绩状态 0-保存,1-提交
	
	//TODO:this field is string ,why not class
	@Column(name="RESULTSRULEID")
	private String resultsRuleId;//使用的积分规则ID

	@Transient
	private Double originalBbsResults; //网络辅导原始分
	@Transient	
	private Double originalExerciseResults;
	@Transient
	private Double originalAskQuestionResults;
	@Transient
	private Double originalCourseExamResults;
	
	public String getAskQuestionResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.askQuestionResults):"0";
	}

	public void setAskQuestionResults(String askQuestionResults) {
		this.askQuestionResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), askQuestionResults);
	}

	public String getCourseExamResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.courseExamResults):"0";
	}

	public void setCourseExamResults(String courseExamResults) {
		this.courseExamResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), courseExamResults);
	}

	public String getUsualResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.usualResults):"0";
	}

	public void setUsualResults(String usualResults) {
		this.usualResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), usualResults);
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

	public String getBbsResults() {	
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.bbsResults):"0";
	}

	public void setBbsResults(String bbsResults) {
		this.bbsResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), bbsResults);
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getExerciseResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.exerciseResults):"0";
	}

	public void setExerciseResults(String exerciseResults) {
		this.exerciseResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), exerciseResults);
	}

	public String getFaceResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.faceResults):"0";
	}

	public void setFaceResults(String faceResults) {
		this.faceResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), faceResults);
	}

	public Date getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getFillinMan() {
		return fillinMan;
	}

	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	public String getFillinManId() {
		return fillinManId;
	}

	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	public String getMajorCourseId() {
		return majorCourseId;
	}

	public void setMajorCourseId(String majorCourseId) {
		this.majorCourseId = majorCourseId;
	}

	public String getOtherResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.otherResults):"0";
	}

	public void setOtherResults(String otherResults) {
		this.otherResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), otherResults);
	}

	public String getSelftestResults() {
		return (getStudentInfo()!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.getStudentInfo().getResourceid(),this.selftestResults):"0";
	}

	public void setSelftestResults(String selftestResults) {
		this.selftestResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.getStudentInfo().getResourceid(), selftestResults);
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}
	/**
	 * 根据规则计算总分
	 * @param rule
	 * @return
	 */
	public String getUsualTotalResults(UsualResultsRule rule){
		Double total = 0.0;
		total += Double.parseDouble(this.getBbsResults())*rule.getBbsResultPer();
		total += Double.parseDouble(this.getExerciseResults())*rule.getExerciseResultPer();
		total += Double.parseDouble(this.getFaceResults())*rule.getFaceResultPer();
		total += Double.parseDouble(this.getSelftestResults())*rule.getSelftestResultPer();
		total += Double.parseDouble(this.getOtherResults())*rule.getOtherResultPer();	
		total += Double.parseDouble(this.getCourseExamResults())*rule.getCourseExamResultPer();	
		total += Double.parseDouble(this.getAskQuestionResults())*rule.getAskQuestionResultPer();	
		return BigDecimal.valueOf(total).divide(BigDecimal.valueOf(100.0),0,BigDecimal.ROUND_HALF_UP).toString();
	}

	public Double getOriginalAskQuestionResults() {
		return originalAskQuestionResults;
	}

	public void setOriginalAskQuestionResults(Double originalAskQuestionResults) {
		this.originalAskQuestionResults = originalAskQuestionResults;
	}

	public Double getOriginalBbsResults() {
		return originalBbsResults;
	}

	public void setOriginalBbsResults(Double originalBbsResults) {
		this.originalBbsResults = originalBbsResults;
	}

	public Double getOriginalCourseExamResults() {
		return originalCourseExamResults;
	}

	public void setOriginalCourseExamResults(Double originalCourseExamResults) {
		this.originalCourseExamResults = originalCourseExamResults;
	}

	public Double getOriginalExerciseResults() {
		return originalExerciseResults;
	}

	public void setOriginalExerciseResults(Double originalExerciseResults) {
		this.originalExerciseResults = originalExerciseResults;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultsRuleId() {
		return resultsRuleId;
	}

	public void setResultsRuleId(String resultsRuleId) {
		this.resultsRuleId = resultsRuleId;
	}

	public UsualResults() {
		super();
	}

	public UsualResults(Double originalBbsResults, Double originalExerciseResults, Double originalAskQuestionResults, Double originalCourseExamResults) {
		super();
		this.originalBbsResults = originalBbsResults;
		this.originalExerciseResults = originalExerciseResults;
		this.originalAskQuestionResults = originalAskQuestionResults;
		this.originalCourseExamResults = originalCourseExamResults;
	}	
}
