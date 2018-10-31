package com.hnjk.edu.teaching.vo;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

/**
 * 学生平时成绩Vo.
 * <code>UsualResultsVo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-7-2 上午09:27:10
 * @see 
 * @version 1.0
 */
public class UsualResultsVo {

	private String studentLearnPlanId;//学习计划id
	private String studentId;//学生id
	private String sysUserId;//学生用户id
	private String usualResultsId;
	private String yearInfoId;//年度id
	private String courseId;//课程id
	private String yearName;//年度
	private String term;//学期
	private String studyNo;//学号
	private String studentName;//姓名
	private String courseName;//课程
	private String courseExamResults;//随堂练习分
	private String exerciseResults;//作业练习分
	private String bbsResults;//网络辅导分
	private String askQuestionResults;//随堂问答分	
	private String selftestResults;//同步自测分
	private String otherResults;//实践及其他分
	private String faceResults;//面授考勤分
	private String usualResults;//平时分总分
    private String status;//0-保存,1-提交
    private String unitName;// 教学站名称
    private String gradeName;// 年级名称
    private String majorName;// 专业名称
    private String classesName;// 班级名称
    private String askquestionresultper;// 随堂问答比例
    private String courseexamresultper;// 随堂练习比例
    private String exerciseresultper;// 课后作业比例
    private String resultsProportion;// 网上学习比重
    
    
	public String getStudentLearnPlanId() {
		return studentLearnPlanId;
	}
	public void setStudentLearnPlanId(String studentLearnPlanId) {
		this.studentLearnPlanId = studentLearnPlanId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getYearName() {
		return yearName;
	}
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getStudyNo() {
		return studyNo;
	}
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseExamResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.courseExamResults):"0";
	}
	public void setCourseExamResults(String courseExamResults) {
		this.courseExamResults = courseExamResults;
	}
	public String getExerciseResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.exerciseResults):"0";
	}
	public void setExerciseResults(String exerciseResults) {
		this.exerciseResults = exerciseResults;
	}
	public String getBbsResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.bbsResults):"0";
	}
	public void setBbsResults(String bbsResults) {
		this.bbsResults = bbsResults;
	}
	public String getAskQuestionResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.askQuestionResults):"0";
	}
	public void setAskQuestionResults(String askQuestionResults) {
		this.askQuestionResults = askQuestionResults;
	}
	public String getSelftestResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.selftestResults):"0";
	}
	public void setSelftestResults(String selftestResults) {
		this.selftestResults = selftestResults;
	}
	public String getOtherResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.otherResults):"0";
	}
	public void setOtherResults(String otherResults) {
		this.otherResults = otherResults;
	}
	public String getFaceResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.faceResults):"0";
	}
	public void setFaceResults(String faceResults) {
		this.faceResults = faceResults;
	}
	public String getUsualResults() {
		return (this.studentId!=null)?ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.usualResults):"0";
	}
	public void setUsualResults(String usualResults) {
		this.usualResults = usualResults;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	public String getYearInfoId() {
		return yearInfoId;
	}
	public void setYearInfoId(String yearInfoId) {
		this.yearInfoId = yearInfoId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getUsualResultsId() {
		return usualResultsId;
	}
	public void setUsualResultsId(String usualResultsId) {
		this.usualResultsId = usualResultsId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
	public String getAskquestionresultper() {
		return askquestionresultper;
	}
	public void setAskquestionresultper(String askquestionresultper) {
		this.askquestionresultper = askquestionresultper;
	}
	public String getCourseexamresultper() {
		return courseexamresultper;
	}
	public void setCourseexamresultper(String courseexamresultper) {
		this.courseexamresultper = courseexamresultper;
	}
	public String getExerciseresultper() {
		return exerciseresultper;
	}
	public void setExerciseresultper(String exerciseresultper) {
		this.exerciseresultper = exerciseresultper;
	}
	
	/**
	 * 获取网上学习比重
	 * @return
	 */
	public String getResultsProportion() {
		StringBuffer rp = new StringBuffer("");
		if(ExStringUtils.isEmpty(this.askquestionresultper) && ExStringUtils.isEmpty(this.courseexamresultper) 
				&& ExStringUtils.isEmpty(this.exerciseresultper)){
			rp.append("无成绩积分规则");
		}else{
			rp.append("问答: "+(ExStringUtils.isEmpty(this.askquestionresultper)?"0":this.askquestionresultper)+"% ");
			rp.append("练习: "+(ExStringUtils.isEmpty(this.courseexamresultper)?"0":this.courseexamresultper)+"% ");
			rp.append("作业: "+(ExStringUtils.isEmpty(this.exerciseresultper)?"0":this.exerciseresultper)+"% ");
		}
		this.resultsProportion = rp.toString();
		
		return resultsProportion;
	}

}
