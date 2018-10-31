package com.hnjk.edu.teaching.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * 学生平时分积分规则.
 * <code>UsualResultsRule</code><p>
 * 1、	随堂练习（自测，系统自动给分）
在资源模块录入，题目、题号、选项、答案、相关知识点、提示等。100%=量占%+质占%；可供调节。每题得分=100分/题目数目。如规定正确率80%或以上的就可以赋予满分100分。
只提供一次机会，做对就给分。做对与否给予提示，帮助学习。
2、	作业环节（教师批改、给分）
可以设置在线或者课后，或者可以两者都有。
在线作业，教师在线批改，给予每个学生一个评价(评价界面)，个别化反馈；
课后作业：教师给分，课后作业，用附件方式收发，只需要提供一个界面，让教师录入分数即可。教师给分的机制，如果达上千人的话，抽样、典型批改，抽查的分值就是所有学生的分值。
如果涉及到好几次作业，每次作业设置一个权重（总权重是1），然后再取平均值。
3、	提问与答疑环节
提问与答疑——由学生在学习知识点过程中，发起有效提问，一种是教师给予反馈，并可以累积问题与答复，在问题库中查找并调出答复即可。另外一种方式就是将问题汇总到论坛里面。
问题库或论坛里面有相关这个问题的解答（教师和学生）都可以调用历史记录。
提问与答疑如何计分？计分机制与课程论坛一致。
4、	讨论：课程论坛
分组or不分组？是进入课程论坛讨论（预约的所有人员）还是小组内成员（预约人员中的部分）讨论？
由辅导教师指定小组组长，组长与成员的职责又有所不同。
课程论坛：发帖按照精华贴、有效贴、无效贴。教师只要判别并归类就可以了（有效贴和无效贴都不给分）。（评分机制：如n条精华帖设置为满分（可供设置数目），辅导教师可以做相应的引导：
对学科教学比较有见地的、或者比较好地答复他人疑问的可以设置为精华帖。）学生可以随时看到自己的精华帖数、得分等。
5、	面授、即时在线辅导
辅导：教师提供导学建议、ppt、阶段性学习指导等（放置在课程公告中）
实时答疑和辅导：如语音视频会议系统（类似诚讯通视频会议系统）
面授：由教师给分

 * @author： 广东学苑教育发展有限公司.
 * 
 * @since： 2010-12-20 上午11:50:50
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_URESULTSRULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UsualResultsRule extends BaseModel{
	
	private static final long serialVersionUID = 7790547566393419601L;
	
	//@Column(name="RULENAME")
	//private String ruleName;//规则名称
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private String term;//学期
	
	
	@Column(name="BBSRESULTPER")
	private Integer bbsResultPer = 10;//网络辅导分比例
	
	@Column(name="BBSBESTTOPICNUM")
	private Integer bbsBestTopicNum  = 0;//网络辅导精华帖满分个数
	
	@Column(name="EXERCISERESULTPER")
	private Integer exerciseResultPer = 20;//作业练习分比例
	
	@Column(name="SELFTESTRESULTPER")
	private Integer selftestResultPer = 20;//同步自测分比例
	
	@Column(name="OTHERRESULTPER")
	private Integer otherResultPer = 20;//实践及其他分比例
	
	@Column(name="FACERESULTPER")
	private Integer faceResultPer = 10;//面授考勤分比例
	
	@Column(name="MEMO")
	private String memo;//备注

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usualResultsRule")
//	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	private Set<UsualResultsRuleCourse> courses = new HashSet<UsualResultsRuleCourse>();//适用课程列表,如果课程列表为空，则通用
	
	/**3.1.1 调整*/
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//课程
	
	
	@Column(name="VERSIONNUM")
	private Integer versionNum = 1;//规则版本,使用时，取最大版本的规则，同一门课程的规则，每新增一条，版本+1
	
	@Column(name="ISUSED")
	private String isUsed = Constants.BOOLEAN_NO;//是否使用
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate = new Date();//创建时间
	
	/**3.1.1 新增*/
	@Column(name="ASKQUESTIONRESULTPER")
	private Integer askQuestionResultPer = 10;//随堂问答分比例
	
	@Column(name="COURSEEXAMRESULTPER")
	private Integer courseExamResultPer = 10;//随堂练习分比例
	
	@Column(name="COURSEEXAMCORRECTPER")
	private Integer courseExamCorrectPer = 80;//随堂练习满分真确率
	
	/**20141009 新增*/
	@Column(name="ISMAXSCORE")
	private String isMaxScore = Constants.BOOLEAN_YES;//作业练习分取分规则   Y-(默认)取作业最高分   N-取作业平均分
	
	
	public UsualResultsRule(){}
	
	/**
	 * 随堂问答分:随堂练习分:作业练习分
	 * @param defaultRule
	 */
	public UsualResultsRule(String[] defaultRule) {
		// TODO Auto-generated constructor stub
		this.bbsResultPer = 0;
		//this.courseExamCorrectPer = 0;
		this.faceResultPer = 0;
		this.otherResultPer = 0;
		this.selftestResultPer = 0;
		this.askQuestionResultPer = Integer.parseInt(defaultRule[0]);
		this.courseExamResultPer = Integer.parseInt(defaultRule[1]);
		this.exerciseResultPer = Integer.parseInt(defaultRule[2]);
	}

	public String getIsMaxScore() {
		return isMaxScore;
	}

	public void setIsMaxScore(String isMaxScore) {
		this.isMaxScore = isMaxScore;
	}

	/**
	 * @return the yearInfo
	 */
	public YearInfo getYearInfo() {
		return yearInfo;
	}

	/**
	 * @param yearInfo the yearInfo to set
	 */
	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	public Integer getAskQuestionResultPer() {
		return askQuestionResultPer;
	}

	public void setAskQuestionResultPer(Integer askQuestionResultPer) {
		this.askQuestionResultPer = askQuestionResultPer;
	}

	public Integer getCourseExamCorrectPer() {
		return courseExamCorrectPer;
	}

	public void setCourseExamCorrectPer(Integer courseExamCorrectPer) {
		this.courseExamCorrectPer = courseExamCorrectPer;
	}

	public Integer getCourseExamResultPer() {
		return courseExamResultPer;
	}

	public void setCourseExamResultPer(Integer courseExamResultPer) {
		this.courseExamResultPer = courseExamResultPer;
	}

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

//	public Set<UsualResultsRuleCourse> getCourses() {
//		return courses;
//	}
//
//	public void setCourses(Set<UsualResultsRuleCourse> courses) {
//		this.courses = courses;
//	}

	public Integer getBbsBestTopicNum() {
		return bbsBestTopicNum;
	}

	public void setBbsBestTopicNum(Integer bbsBestTopicNum) {
		this.bbsBestTopicNum = bbsBestTopicNum;
	}

	public Integer getBbsResultPer() {
		return bbsResultPer;
	}

	public void setBbsResultPer(Integer bbsResultPer) {
		this.bbsResultPer = bbsResultPer;
	}

	public Integer getExerciseResultPer() {
		return exerciseResultPer;
	}

	public void setExerciseResultPer(Integer exerciseResultPer) {
		this.exerciseResultPer = exerciseResultPer;
	}

	public Integer getFaceResultPer() {
		return faceResultPer;
	}

	public void setFaceResultPer(Integer faceResultPer) {
		this.faceResultPer = faceResultPer;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getOtherResultPer() {
		return otherResultPer;
	}

	public void setOtherResultPer(Integer otherResultPer) {
		this.otherResultPer = otherResultPer;
	}

//	public String getRuleName() {
//		return ruleName;
//	}
//
//	public void setRuleName(String ruleName) {
//		this.ruleName = ruleName;
//	}

	public Integer getSelftestResultPer() {
		return selftestResultPer;
	}

	public void setSelftestResultPer(Integer selftestResultPer) {
		this.selftestResultPer = selftestResultPer;
	}

//	public String getTerm() {
//		return term;
//	}
//
//	public void setTerm(String term) {
//		this.term = term;
//	}
//
//	public YearInfo getYearInfo() {
//		return yearInfo;
//	}
//
//	public void setYearInfo(YearInfo yearInfo) {
//		this.yearInfo = yearInfo;
//	}
	
	/**
	 * 比例总和
	 */
	public Integer getResultPerSum(){
		return this.getBbsResultPer().intValue()+this.getExerciseResultPer().intValue()+this.getFaceResultPer().intValue()
				+this.getSelftestResultPer().intValue()+this.getOtherResultPer().intValue()+this.getCourseExamResultPer().intValue()+this.getAskQuestionResultPer().intValue();
	}
	/**
	 * 获取每个精华帖的分值
	 * @return
	 */
	public Integer getBbsBestTopicResult(){
		return 100 / getBbsBestTopicNum();
	}
}
