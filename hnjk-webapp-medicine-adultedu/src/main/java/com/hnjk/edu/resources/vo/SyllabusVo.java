package com.hnjk.edu.resources.vo;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.hnjk.core.support.context.Constants;

public class SyllabusVo {
	private String resourceid;
	private String courseId;
	private String syllabusName;
	private Long syllabusLevel;//节点级别
	private String syllabusType;//节点类型
	private Integer resCount;//材料数
	private Integer guidCount;//学习目标数
	private String parentId;
	boolean showMenu = false;//是否展开菜单
	private Set<SyllabusVo> childs = new LinkedHashSet<SyllabusVo>();
	
	/*学习记录使用*/
	private Date learHandoutTime;//学习讲义时间
	private Date learMeteTime;//学习授课时间
	private Date learCourseexamTime;//提交随堂练习时间
	private Integer examCount;//随堂练习数
	private Integer examTotalCount;//随堂练习数
	private Integer examDoneCount;//已做的随堂练习数
	private Integer examFinishedCount;//提交的随堂练习数
	private Integer examCorrectCount;//正确的随堂练习数
	private String hasResource = Constants.BOOLEAN_NO;
	
	private int handoutCount;//讲义数
	private int mateCount;//视频数
	private int courseExamCount;//习题数
	
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getSyllabusName() {
		return syllabusName;
	}
	public void setSyllabusName(String syllabusName) {
		this.syllabusName = syllabusName;
	}
	public Long getSyllabusLevel() {
		return syllabusLevel;
	}
	public void setSyllabusLevel(Long syllabusLevel) {
		this.syllabusLevel = syllabusLevel;
	}
	public String getSyllabusType() {
		return syllabusType;
	}
	public void setSyllabusType(String syllabusType) {
		this.syllabusType = syllabusType;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Set<SyllabusVo> getChilds() {
		return childs;
	}
	public void setChilds(Set<SyllabusVo> childs) {
		this.childs = childs;
	}	
	public Integer getResCount() {
		return resCount;
	}
	public void setResCount(Integer resCount) {
		this.resCount = resCount;
	}
	public boolean getShowMenu() {
		return showMenu;
	}
	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}
	public Integer getGuidCount() {
		return guidCount;
	}
	public void setGuidCount(Integer guidCount) {
		this.guidCount = guidCount;
	}
	public Date getLearHandoutTime() {
		return learHandoutTime;
	}
	public void setLearHandoutTime(Date learHandoutTime) {
		this.learHandoutTime = learHandoutTime;
	}
	public Date getLearMeteTime() {
		return learMeteTime;
	}
	public void setLearMeteTime(Date learMeteTime) {
		this.learMeteTime = learMeteTime;
	}
	public Date getLearCourseexamTime() {
		return learCourseexamTime;
	}
	public void setLearCourseexamTime(Date learCourseexamTime) {
		this.learCourseexamTime = learCourseexamTime;
	}
	public Integer getExamCount() {
		return examCount;
	}
	public void setExamCount(Integer examCount) {
		this.examCount = examCount;
	}
	public Integer getExamDoneCount() {
		return examDoneCount;
	}
	public void setExamDoneCount(Integer examDoneCount) {
		this.examDoneCount = examDoneCount;
	}
	public Integer getExamFinishedCount() {
		return examFinishedCount;
	}
	public void setExamFinishedCount(Integer examFinishedCount) {
		this.examFinishedCount = examFinishedCount;
	}
	public Integer getExamCorrectCount() {
		return examCorrectCount;
	}
	public void setExamCorrectCount(Integer examCorrectCount) {
		this.examCorrectCount = examCorrectCount;
	}	
	public Integer getExamTotalCount() {
		return examTotalCount;
	}
	public void setExamTotalCount(Integer examTotalCount) {
		this.examTotalCount = examTotalCount;
	}
	public String getHasResource() {
		if(learHandoutTime != null || learMeteTime!=null || examDoneCount != null && examDoneCount.intValue()>0){
			return Constants.BOOLEAN_YES;
		}
		return hasResource;
	}
	public int getHandoutCount() {
		return handoutCount;
	}
	public void setHandoutCount(int handoutCount) {
		this.handoutCount = handoutCount;
	}
	public int getMateCount() {
		return mateCount;
	}
	public void setMateCount(int mateCount) {
		this.mateCount = mateCount;
	}
	public int getCourseExamCount() {
		return courseExamCount;
	}
	public void setCourseExamCount(int courseExamCount) {
		this.courseExamCount = courseExamCount;
	}	
	
	public boolean isRemove() {
		return (syllabusLevel==3L && resCount==0 
				|| syllabusLevel==2L && resCount==0 && childs.isEmpty() 
				|| syllabusLevel==1L && childs.isEmpty() && guidCount==0);
	}
	@Override
	public String toString() {
		return "SyllabusVo [syllabusName=" + syllabusName + ", syllabusLevel="
				+ syllabusLevel + ", resCount=" + resCount + ", guidCount="
				+ guidCount + ", showMenu=" + showMenu + ", handoutCount="
				+ handoutCount + ", mateCount=" + mateCount
				+ ", courseExamCount=" + courseExamCount + "]";
	}
	
}
