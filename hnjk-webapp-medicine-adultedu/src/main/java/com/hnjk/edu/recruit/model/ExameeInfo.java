package com.hnjk.edu.recruit.model;

import java.util.Date;
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
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
/**
 * 考生信息表
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_RECRUIT_EXAMINEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExameeInfo extends BaseModel{

	private static final long serialVersionUID = -6214884543766940138L;
	
	@Column(name="KSH",nullable=false)
	private String KSH;//考生号
	
	@Column(name="ZKZH",nullable=false)
	private String ZKZH;//准考证号
	
	@Column(name="XM",nullable=false)
	private String XM;//姓名
	
	@Column(name="WYYZDM")
	private String WYYZDM;//外语语种#CodeForeignLanguage
	
	@Column(name="CSRQ")
	@Temporal(TemporalType.DATE)
	private Date CSRQ;//出生日期
	
	@Column(name="XBDM",length=1,nullable=false)
	private String XBDM;//性别#CodeSex
	
	@Column(name="SFZH",nullable=false)
	private String SFZH;//身份证号
	
	@Column(name="KSTZBZ")
	private String KSTZBZ;//考生特征标志#CodeCharacteristic
	
	@Column(name="ZSLBDM",nullable=false)
	private String ZSLBDM;//招生类别#CodeRecruitType
	
	@Column(name="MZDM",nullable=false)
	private String MZDM;//民族#CodeNation
	
	@Column(name="ZZMMDM",nullable=false)
	private String ZZMMDM;//政治面貌#CodePolitics

	/**
	 * 报考专业属性:CodeMajorAttribute
	 */
	@Column(name="BKZYSXDM",nullable=false)
	private  String BKZYSXDM;
	
	@Column(name="YZBM",nullable=false)
	private String YZBM;//邮政编码
	
	@Column(name="LXDH",nullable=false)
	private String LXDH;//联系电话
	
	@Column(name="WHCDDM",nullable=false)
	private String WHCDDM;//文化程度#CodeEducationalLevel
	
	@Column(name="ZYLBDM",nullable=false)
	private String ZYLBDM;//专业类别#CodeMajorCatogery
	
	@Column(name="GZRQ")
	@Temporal(TemporalType.DATE)
	private Date GZRQ;//工作年月
	
	@Column(name="BYRQ")
	@Temporal(TemporalType.DATE)
	private Date BYRQ;//毕业日期
	
	@Column(name="BYXX")
	private String BYXX;//毕业学校
	
	@Column(name="TXDZ",nullable=false)
	private String TXDZ;//录取通知书邮寄地址
	
	@Column(name="LQZY",nullable=false)
	private String LQZY;//录取专业
	
	@Column(name="LQZYMC")
	private String LQZYMC;//录取专业名称
	
	@Column(name="CCDM",nullable=false)
	private String CCDM;//层次
	
	@Column(name="CCMC")
	private String CCMC;//层次名称
	
//	@Column(name="XXXSDM",nullable=false)
	@Column(name="XXXSDM")
	private String XXXSDM;//学习形式#CodeTeachingType
	
	@Column(name="XZ",length=6,scale=2)
	private Double XZ;//学制
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "RECRUITPLANID")
	private RecruitPlan recruitPlan;//招生计划
			
	@Column(name="MAINPHOTOPATH")
	private String mainPhotoPath;//考生照片
	
	@Column(name="BACKPHOTOPATH")
	private String backPhotoPath;//考生照片备份
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exameeInfo")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)	
	@Where(clause="isDeleted=0")
	@OrderBy("scoreCode ASC")
	private Set<ExameeInfoScore> exameeInfoScores = new LinkedHashSet<ExameeInfoScore>(0);//成绩
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exameeInfo")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)	
	@Where(clause="isDeleted=0")
	@OrderBy("showOrder ASC")
	private Set<ExameeInfoWish> exameeInfoWishs = new LinkedHashSet<ExameeInfoWish>(0);//成绩
	
	@Column(name="KSZT")
	private String KSZT;//考试状态#CodeEnrollStatus
	
	@Transient
	private String recruitMajorId;
	
	@Transient
	private String enrolleeInfoId;
	
	@Transient
	private String studentId;
	
	@Transient
	private String branchSchoolId;
	
	/**
	 * @return the kSZT
	 */
	public String getKSZT() {
		return KSZT;
	}
	/**
	 * @param kSZT the kSZT to set
	 */
	public void setKSZT(String kSZT) {
		KSZT = kSZT;
	}
	/**
	 * @return the exameeInfoWishs
	 */
	public Set<ExameeInfoWish> getExameeInfoWishs() {
		return exameeInfoWishs;
	}
	/**
	 * @param exameeInfoWishs the exameeInfoWishs to set
	 */
	public void setExameeInfoWishs(Set<ExameeInfoWish> exameeInfoWishs) {
		this.exameeInfoWishs = exameeInfoWishs;
	}
	/**
	 * @return the lQZYMC
	 */
	public String getLQZYMC() {
		return LQZYMC;
	}
	/**
	 * @param lQZYMC the lQZYMC to set
	 */
	public void setLQZYMC(String lQZYMC) {
		LQZYMC = lQZYMC;
	}
	/**
	 * @return the cCMC
	 */
	public String getCCMC() {
		return CCMC;
	}
	/**
	 * @param cCMC the cCMC to set
	 */
	public void setCCMC(String cCMC) {
		CCMC = cCMC;
	}
	/**
	 * @return the exameeInfoScores
	 */
	public Set<ExameeInfoScore> getExameeInfoScores() {
		return exameeInfoScores;
	}
	/**
	 * @param exameeInfoScores the exameeInfoScores to set
	 */
	public void setExameeInfoScores(Set<ExameeInfoScore> exameeInfoScores) {
		this.exameeInfoScores = exameeInfoScores;
	}
	/**
	 * @return the kSH
	 */
	public String getKSH() {
		return KSH;
	}
	/**
	 * @param kSH the kSH to set
	 */
	public void setKSH(String kSH) {
		KSH = kSH;
	}
	/**
	 * @return the zKZH
	 */
	public String getZKZH() {
		return ZKZH;
	}
	/**
	 * @param zKZH the zKZH to set
	 */
	public void setZKZH(String zKZH) {
		ZKZH = zKZH;
	}
	/**
	 * @return the xM
	 */
	public String getXM() {
		return XM;
	}
	/**
	 * @param xM the xM to set
	 */
	public void setXM(String xM) {
		XM = xM;
	}
	/**
	 * @return the wYYZDM
	 */
	public String getWYYZDM() {
		return WYYZDM;
	}
	/**
	 * @param wYYZDM the wYYZDM to set
	 */
	public void setWYYZDM(String wYYZDM) {
		WYYZDM = wYYZDM;
	}
	/**
	 * @return the cSRQ
	 */
	public Date getCSRQ() {
		return CSRQ;
	}
	/**
	 * @param cSRQ the cSRQ to set
	 */
	public void setCSRQ(Date cSRQ) {
		CSRQ = cSRQ;
	}
	/**
	 * @return the xBDM
	 */
	public String getXBDM() {
		return XBDM;
	}
	/**
	 * @param xBDM the xBDM to set
	 */
	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}
	/**
	 * @return the sFZH
	 */
	public String getSFZH() {
		return SFZH;
	}
	/**
	 * @param sFZH the sFZH to set
	 */
	public void setSFZH(String sFZH) {
		SFZH = sFZH;
	}
	/**
	 * @return the kSTZBZ
	 */
	public String getKSTZBZ() {
		return KSTZBZ;
	}
	/**
	 * @param kSTZBZ the kSTZBZ to set
	 */
	public void setKSTZBZ(String kSTZBZ) {
		KSTZBZ = kSTZBZ;
	}
	/**
	 * @return the zSLBDM
	 */
	public String getZSLBDM() {
		return ZSLBDM;
	}
	/**
	 * @param zSLBDM the zSLBDM to set
	 */
	public void setZSLBDM(String zSLBDM) {
		ZSLBDM = zSLBDM;
	}
	/**
	 * @return the mZDM
	 */
	public String getMZDM() {
		return MZDM;
	}
	/**
	 * @param mZDM the mZDM to set
	 */
	public void setMZDM(String mZDM) {
		MZDM = mZDM;
	}
	/**
	 * @return the zZMMDM
	 */
	public String getZZMMDM() {
		return ZZMMDM;
	}
	/**
	 * @param zZMMDM the zZMMDM to set
	 */
	public void setZZMMDM(String zZMMDM) {
		ZZMMDM = zZMMDM;
	}
	/**
	 * @return the bKZYSXDM
	 */
	public String getBKZYSXDM() {
		return BKZYSXDM;
	}
	/**
	 * @param bKZYSXDM the bKZYSXDM to set
	 */
	public void setBKZYSXDM(String bKZYSXDM) {
		BKZYSXDM = bKZYSXDM;
	}
	/**
	 * @return the yZBM
	 */
	public String getYZBM() {
		return YZBM;
	}
	/**
	 * @param yZBM the yZBM to set
	 */
	public void setYZBM(String yZBM) {
		YZBM = yZBM;
	}
	/**
	 * @return the lXDH
	 */
	public String getLXDH() {
		return LXDH;
	}
	/**
	 * @param lXDH the lXDH to set
	 */
	public void setLXDH(String lXDH) {
		LXDH = lXDH;
	}
	/**
	 * @return the wHCDDM
	 */
	public String getWHCDDM() {
		return WHCDDM;
	}
	/**
	 * @param wHCDDM the wHCDDM to set
	 */
	public void setWHCDDM(String wHCDDM) {
		WHCDDM = wHCDDM;
	}
	/**
	 * @return the zYLBDM
	 */
	public String getZYLBDM() {
		return ZYLBDM;
	}
	/**
	 * @param zYLBDM the zYLBDM to set
	 */
	public void setZYLBDM(String zYLBDM) {
		ZYLBDM = zYLBDM;
	}
	/**
	 * @return the gZRQ
	 */
	public Date getGZRQ() {
		return GZRQ;
	}
	/**
	 * @param gZRQ the gZRQ to set
	 */
	public void setGZRQ(Date gZRQ) {
		GZRQ = gZRQ;
	}
	/**
	 * @return the bYRQ
	 */
	public Date getBYRQ() {
		return BYRQ;
	}
	/**
	 * @param bYRQ the bYRQ to set
	 */
	public void setBYRQ(Date bYRQ) {
		BYRQ = bYRQ;
	}
	/**
	 * @return the bYXX
	 */
	public String getBYXX() {
		return BYXX;
	}
	/**
	 * @param bYXX the bYXX to set
	 */
	public void setBYXX(String bYXX) {
		BYXX = bYXX;
	}
	/**
	 * @return the tXDZ
	 */
	public String getTXDZ() {
		return TXDZ;
	}
	/**
	 * @param tXDZ the tXDZ to set
	 */
	public void setTXDZ(String tXDZ) {
		TXDZ = tXDZ;
	}
	/**
	 * @return the lQZY
	 */
	public String getLQZY() {
		return LQZY;
	}
	/**
	 * @param lQZY the lQZY to set
	 */
	public void setLQZY(String lQZY) {
		LQZY = lQZY;
	}
	/**
	 * @return the cCDM
	 */
	public String getCCDM() {
		return CCDM;
	}
	/**
	 * @param cCDM the cCDM to set
	 */
	public void setCCDM(String cCDM) {
		CCDM = cCDM;
	}
	/**
	 * @return the xXXSDM
	 */
	public String getXXXSDM() {
		return XXXSDM;
	}
	/**
	 * @param xXXSDM the xXXSDM to set
	 */
	public void setXXXSDM(String xXXSDM) {
		XXXSDM = xXXSDM;
	}
	/**
	 * @return the xZ
	 */
	public Double getXZ() {
		return XZ;
	}
	/**
	 * @param xZ the xZ to set
	 */
	public void setXZ(Double xZ) {
		XZ = xZ;
	}
	/**
	 * @return the recruitPlan
	 */
	public RecruitPlan getRecruitPlan() {
		return recruitPlan;
	}
	/**
	 * @param recruitPlan the recruitPlan to set
	 */
	public void setRecruitPlan(RecruitPlan recruitPlan) {
		this.recruitPlan = recruitPlan;
	}
	
	/**
	 * @return the mainPhotoPath
	 */
	public String getMainPhotoPath() {
		return mainPhotoPath;
	}
	/**
	 * @param mainPhotoPath the mainPhotoPath to set
	 */
	public void setMainPhotoPath(String mainPhotoPath) {
		this.mainPhotoPath = mainPhotoPath;
	}
	/**
	 * @return the backPhotoPath
	 */
	public String getBackPhotoPath() {
		return backPhotoPath;
	}
	/**
	 * @param backPhotoPath the backPhotoPath to set
	 */
	public void setBackPhotoPath(String backPhotoPath) {
		this.backPhotoPath = backPhotoPath;
	}
	
	public String getRecruitMajorId() {
		return recruitMajorId;
	}
	public void setRecruitMajorId(String recruitMajorId) {
		this.recruitMajorId = recruitMajorId;
	}
	public String getEnrolleeInfoId() {
		return enrolleeInfoId;
	}
	public void setEnrolleeInfoId(String enrolleeInfoId) {
		this.enrolleeInfoId = enrolleeInfoId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getBranchSchoolId() {
		return branchSchoolId;
	}
	public void setBranchSchoolId(String branchSchoolId) {
		this.branchSchoolId = branchSchoolId;
	}
}
