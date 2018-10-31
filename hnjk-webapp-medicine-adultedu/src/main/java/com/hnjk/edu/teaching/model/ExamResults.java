package com.hnjk.edu.teaching.model;

// default package

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.platform.system.model.BaseLogHistoryModel;

/**
 * 学生考试成绩表
 * <code>ExamResults</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午04:24:26
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMRESULTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ExamResults   extends BaseLogHistoryModel{
	
	 public final static String EXAMRESULTS_FORM_TYPE_REEXPORT       = "examResultsReExport";	  //重新导出附件表单类型
	 public final static String EXAMRESULTS_FORM_TYPE_EXPORT         = "examResultsExport";		  //成绩导出附件表单类型
	 public final static String EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK = "examResultsImportUncheck";//导入未审核成绩附件表单类型
	 public final static String EXAMRESULTS_FORM_TYPE_IMPORT_CHECKED = "examResultsImportChecked";//导入已审核成绩附件表单类型
	 
	 public final static String EXAMRESULTS_LOG_DOWNLOAD_TRANSCRIPTS = "download_transcripts";    //下载成绩单
	 public final static String EXAMRESULTS_LOG_TYPE_SUBMIT 		 = "examResultsSubmit";		  //提交成绩
	 public final static String EXAMRESULTS_LOG_TYPE_PUBLISH 	 	 = "examResultsPublish";	  //发布成绩
	 
	 public final static String REVOCATION_TYPE_ALL                  = "all";   //撤销成绩类型-全部
	 public final static String REVOCATION_TYPE_COURSE               = "course";//撤销成绩类型-按课程
	 public final static String CHEAT_SCORE  = "0▲";// 考试作弊的分数表示
	 
	 public final static String RESULTCALCULATERULE_USUALLYSCORE = "resultCalculateRule_0";// 面授平时成绩计算规则
	 public final static String RESULTCALCULATERULE_WRITTENSCORE = "resultCalculateRule_1";// 面授卷面成绩计算规则
	
	 @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	 @JoinColumn(name = "COURSEID")
	 private Course course;// 考试课程
	 
	 @Column(name="PARENTID", length=32, nullable=true)
	 private String parentId;

	@Column(name="MAJORCOURSEID")
     private String majorCourseId;//当前专业考试课程ID     
   
     @Column(name="ISOUTPLANCOURSE" ,length=1)
     private String isOutplancourse = Constants.BOOLEAN_NO;//是否计划外课程
     
     @Column(name="COURSESCORETYPE")
     private String courseScoreType;//#考试成绩类型 10-数值型11-百分制12-150分制20-字符型22-二分制23-三分制24-四分制     25-五分制 30-等级制

     /*
      * 1)传统考试，卷面成绩=纸质试卷分数,由老师录入
      * 2)机考（纯客观题），卷面成绩=机考客观题成绩(系统自动统计)
      * 3)机考（客观题+非客观题在线作答），卷面成绩=机考原始成绩(系统自动统计)+机考非客观题在线作答，老师评分成绩
      * 4）机考（客观题+非客观题纸质作答），卷面成绩=机考原始成绩(系统自动统计)+机考手工评卷成绩
      */
     @Column(name="WRITTENSCORE")
     private String writtenScore;//卷面成绩 
          
     @Column(name="USUALLYSCORE")
     private String usuallyScore;//平时成绩
     
     @Column(name="ONLINESCORE")
     private String onlineScore;//网上学习成绩
     
     @Column(name="INTEGRATEDSCORE")
     private String integratedScore;//综合成绩
     
     @Column(name="EXAMABNORMITY")
     private String examAbnormity;//#考试异常情况 0-正常 1-作弊 2-缺考 3-无卷 4-其它 5-缓考 6-免考 7-未修 8-免修
     
     @Column(name="ISDELAYEXAM")
     private String isDelayExam = Constants.BOOLEAN_NO;//是否缓考    
    
     @Column(name="EXAMSEATNUM")
     private String examSeatNum;//座位号
     
     @Column(name="EXAMCOUNT")
     private Long examCount ;//选考次数
     
     @Column(name="CHECKSTATUS")
     private String checkStatus;//审核状态 -1、成绩默认状态 0、保存 1、提交 2、待审核 3、审核通过 4、发布
     
     @Column(name="MODIFYFLAG")
     private Long modifyFlag;//修改标示
     
     @Temporal(TemporalType.TIMESTAMP)
     @Column(name="EXAMSTARTTIME")
     private Date examStartTime;//考试开始时间
     
     @Temporal(TemporalType.TIMESTAMP)
     @Column(name="EXAMENDTIME")
     private Date examEndTime;//考试结束时间
     
     @Column(name="FILLINMAN")
     private String fillinMan;//填写人
     
     @Column(name="FILLINMANID")
     private String fillinManId;//填写人ID
   
     @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
 	 @JoinColumn(name = "CLASSROOMID") 
     private Examroom examroom;//考场
     
     @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
 	 @JoinColumn(name = "STUDENTID")     
	 private StudentInfo studentInfo;//学生学籍信息
	 
     @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
 	 @JoinColumn(name = "EXAMINFOID")
     private ExamInfo examInfo;//考试信息

     /**3.0.9 新增考试批次ID*/
     @Column(name="EXAMSUBID")
     private String examsubId;//考试安排ID
     
//     @ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//     @JoinColumn(name = "EXAMSUBID")
//     private ExamSub examSub;
     
     @Transient
 	 private String emptyStr;
     
	    /**3.0.9新增成绩审核意见*/
     @Column(name="CHECKNOTES",length=255)
     private String checkNotes;//成绩审核意见，有异常时，管理人员退回给老师修改时，必须填写
     
     /**3.0.9新增填写人、审核人等信息*/
     @Column(name="FILLINDATE")
     @Temporal(TemporalType.TIMESTAMP)
     private Date fillinDate;//成绩录入时间
     
     @Column(name="AUDITMAN")
     private String auditMan;//成绩审核人
     
     @Column(name="AUDITMANID")
     private String auditManId;//成绩审核人ID
     
     @Column(name="AUDITDATE")
     @Temporal(TemporalType.TIMESTAMP)
     private Date auditDate;//成绩审核时间
     
     /**3.1.1 新增学时学分字段*/
     @Column(name="CREDITHOUR",scale=1)
     private Double creditHour;//学分
     
     @Column(name="STYDYHOUR")
     private Integer stydyHour;//学时    
     
     @Transient
     private String tempwrittenScore;//卷面成绩 用于导出隔离服务器XML中加密的成绩字段(密文)
     
     @Transient
     private String tempusuallyScore;//平时成绩 用于导出隔离服务器XML中加密的成绩字段(密文)
     
     @Transient
     private String tempintegratedScore;//综合成绩 用于导出隔离服务器XML中加密的成绩字段(密文)
     
     @Transient
     private String tempwrittenScore_d;//卷面成绩 用于计算综合成绩时的成绩字段(明文)
     
     @Transient
     private String tempusuallyScore_d;//平时成绩 用于计算综合成绩时的成绩字段(明文)
     
     @Transient
     private String tempintegratedScore_d;//综合成绩 用于计算综合成绩时的成绩字段(明文)
          

	/**3.1.6新增考试形式、课程形式、是否补考字段*/
     @Column(name="EXAMTYPE",length=1)
     private Integer examType;//考试形式
     
     @Column(name="COURSETYPE")
     private String courseType;//课程形式
     
     @Column(name="ISMAKEUPEXAM")
     private String isMakeupExam = Constants.BOOLEAN_NO;//是否补考，该字段更换为考试类型
     
     /**3.1.11 新增机考考试*/
     @Column(name="ISMACHINEEXAM")
     private String isMachineExam = Constants.BOOLEAN_NO;//是否机考 ，安排机考后需要update这个字段
     
     @Column(name="PLANCOURSETEACHTYPE")
     private String planCourseTeachType;//所属课程对应的教学方式   网络 \ 面授 \网络+面授
     
     /**3.1.12 新增成绩变更审核对象 1:1*/
     @OneToOne(fetch=FetchType.LAZY,mappedBy="examResults")
     @Where(clause = "isDeleted=0 and auditType=0")
     private ExamResultsAudit examResultsAudit;//成绩变更审核对象
          
     /**3.2.6   为适应机考混合考(笔试+机考)形式，新增机考成绩和笔试成绩，老师提交时更新到卷面成绩*/
     @Column(name="WRITTENMACHINESCORE")
     private String writtenMachineScore;//机考原始成绩，客观题成绩（系统统计）
     
     @Column(name="WRITTENHANDWORKSCORE")
     private String writtenHandworkScore;//纸质作答，机考手工评卷成绩
     
     /**3.2.6 新增论文部分成绩*/
     
     @Column(name="FIRSTSCORE")
     private String firstScore;//初评成绩
     
     @Column(name="SECONDSCORE")
     private String secondScore;//答辩成绩
     
     /**3.2.11 新增机考非客观题在线作答分数*/         
     @Column(name="WRITTENONLINEHANDWORKSCORE")
     private String writtenOnlineHandworkScore;//机考非客观题在线作答，老师评分成绩(老师在线批阅).
     
     /*20130521 增加字段*/
     @Column(name="USFILLINDATE")
     private Date usFillinDate ;//平时成绩填写时间
     @Column(name="USFILLINMAN")
     private String usFillinMan;//平时成绩填写人
     @Column(name="USFILLINMANID")
     private String usFillinManId;//平时成绩填写人id
     @Column(name="USCHECKSTATUS")
     private String usCheckStatus;//平时成绩提交状态
//     @Column(name="ISADJUST")
//     private String isAdjust;//是否对卷面成绩做调整  55-59调整为60
     
    @Column(name="MEMO")
    private String memo;
    
    @Column(name="ISMARKDELETE")
    private String isMarkDelete = "N";//学籍异动复学使用,是否标记删除
    


	/**
	 * @return the writtenOnlineHandworkScore
	 */
	public String getWrittenOnlineHandworkScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.writtenOnlineHandworkScore);
	}

	/**
	 * @param writtenOnlineHandworkScore the writtenOnlineHandworkScore to set
	 */
	public void setWrittenOnlineHandworkScore(String writtenOnlineHandworkScore) {
		this.writtenOnlineHandworkScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), writtenOnlineHandworkScore);
	}

	/**
	 * @return the firstScore
	 */
	public String getFirstScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.firstScore);
	}

	/**
	 * @param firstScore the firstScore to set
	 */
	public void setFirstScore(String firstScore) {
		this.firstScore =  ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), firstScore);
	}

	/**
	 * @return the secondScore
	 */
	public String getSecondScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.secondScore);
	}

	/**
	 * @param secondScore the secondScore to set
	 */
	public void setSecondScore(String secondScore) {
		this.secondScore =  ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), secondScore);
	}

	/**
	 * @return the writtenMachineScore
	 */
	public String getWrittenMachineScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.writtenMachineScore);
	}

	/**
	 * @param writtenMachineScore the writtenMachineScore to set
	 */
	public void setWrittenMachineScore(String writtenMachineScore) {
		 this.writtenMachineScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), writtenMachineScore);
	}

	/**
	 * @return the writtenHandworkScore
	 */
	public String getWrittenHandworkScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.writtenHandworkScore);
	}

	/**
	 * @param writtenHandworkScore the writtenHandworkScore to set
	 */
	public void setWrittenHandworkScore(String writtenHandworkScore) {
		this.writtenHandworkScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), writtenHandworkScore);;
	}

	public String getTempwrittenScore_d() {
		return tempwrittenScore_d==null?"":tempwrittenScore_d;
	}
	public String getTempusuallyScore_d() {
		return tempusuallyScore_d==null?"":tempusuallyScore_d;
	}
	public String getTempintegratedScore_d() {
		return tempintegratedScore_d==null?"":tempintegratedScore_d;
	}

	public String getTempintegratedScore() {
		return this.integratedScore;
	}
	public String getTempusuallyScore() {
		return this.usuallyScore;
	}
	public String getTempwrittenScore() {
		return this.writtenScore;
	}

	public String getIntegratedScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.integratedScore);
	}

	public void setIntegratedScore(String integratedScore) {
		if (integratedScore.endsWith(".0")) {
			integratedScore = integratedScore.substring(0,integratedScore.length()-2);
		} else if (integratedScore.endsWith(".00")) {
			integratedScore = integratedScore.substring(0,integratedScore.length()-3);
		}
		this.integratedScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(),integratedScore);
	}

	public String getUsuallyScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.usuallyScore) ;
	}

	public void setUsuallyScore(String usuallyScore) {
		if (usuallyScore.endsWith(".0")) {
			usuallyScore = usuallyScore.substring(0,usuallyScore.length()-2);
		} else if (usuallyScore.endsWith(".00")) {
			usuallyScore = usuallyScore.substring(0,usuallyScore.length()-3);
		}
		this.usuallyScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), usuallyScore);
	}

	public String getWrittenScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.writtenScore) ;
	}

	public void setWrittenScore(String writtenScore) {
		if (writtenScore.endsWith(".0")) {
			writtenScore = writtenScore.substring(0,writtenScore.length()-2);
		} else if (writtenScore.endsWith(".00")) {
			writtenScore = writtenScore.substring(0,writtenScore.length()-3);
		}
		this.writtenScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(), writtenScore);
	}


	@Override
	public String getResourceid() {
		return super.getResourceid();
	}


	public String getOnlineScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),onlineScore);
	}

	public void setOnlineScore(String onlineScore) {
		this.onlineScore = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(),onlineScore);
	}
}