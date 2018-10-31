package com.hnjk.edu.learning.vo;

import java.io.Serializable;
import java.util.Set;

import com.hnjk.edu.basedata.model.TextBook;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/**
 * 
 * <code>用于学员主页-我的学习计划 VO </code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午05:26:24
 * @see 
 * @version 1.0
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class LearningPlanVo implements Serializable {
	
	private String  course_Id;						   //课程ID
	private String  teachingPlan_Id;				   //教学计划ID
	private String  teachingPlan_Name;				   //教学计划名称
	private Integer teachingPlan_Course_num;		   //教学计划课程序号
	private String  teachingPlan_Course_Id;			   //教学计划课程ID
	private String  teachingPlan_Course_name;		   //教学计划课程名称
	private String  teachingPlan_Course_teachType_code;//教学计划课程   #教学方式  网络学习/面授  /网络+面授(数字字典编码)
	private String  teachingPlan_Course_teachType_Name;//教学计划课程   #教学方式  网络学习/面授  /网络+面授
	private String  teachingPlan_Course_type;		   //教学计划课程类型
	private String  teachingPlan_Course_type_code;	   //教学计划课程类型(数字字典值)
	private Double  teachingPlan_Course_creditHour;	   //教学计划课程学分
	private String  teachingPlan_Course_term_code;	   //教学计划课程所在的学期
	private String  teachingPlan_Course_term;		   //教学计划课程所在的学期(数字字典值)
	private String  teachingPlan_Course_learnStatus;   //教学计划课程学习状态显示值  未预约学习、正在学习、完成学习、预约时间已过
	private Integer teachingPlan_Course_learnStatusInt;//教学计划课程学习状态值  1 ，2 ，3
	private String  teachingPlan_Course_bookingStatus; //教学计划课程预约状态   预约学习、预约考试、已考完
	private String  teachingPlan_Course_examType;      //教学计划课程考试形式
	private String  teachingPlan_Course_examType_code; //教学计划课程考试形式(字典值)
	private Double  teachingPlan_Course_score;		   //教学计划课程成绩
	private String  teachingPlan_Course_scoreStr;      //教学计划课程成绩字符串
	private String  teachingPlan_Course_examStartTime; //教学计划课程考试开始时间
	private String  teachingPlan_Course_examEndTime;   //教学计划课程考试结束时间
	private String  grdeStatu;						   //年级状态
	private String  eduYear;						   //学制
	private Double  totalScore=0.0;					   //累计学分
	private Double	compulsoryedScore=0.0;			   //已修必修课总学分
	private int     termCompulsory;				   	   //本学期必修课
	private int     compulsoryed=0;					   //已修必修课
	private Double 	minResult;						   //最低毕业学分
	private String  orderExamTime;					   //考试预约截止时间
	private String  nationalexaminationTime;		   //全国统考时间
	private String  orderExamTimeDisNowTime;		   //考试预约截止时间距离当前的时间  单位天
	private String  nationalexaminationTimeDisTime;	   //全国统考时间距离当前的时间  单位天
	private String  isNeedReExamination="N";           //是否需要重考
	private String  isPlanCourse="Y";                  //是否计划内课程 
	private String  specialCourseFlag;                 //统考、免修免考课程标识，如果为这几种课程不允许重修
	private String  thesisOrderYearInfo;               //毕业论文预约年度----2011.10.12加入临时变量
	private String  thesisOrderTerm;				   //毕业论文预约学期----2011.10.12加入临时变量
	private String  teachingPlan_Course_UsuallyScore;  //学习计划平时成绩
	private String  teachingPlan_Course_WrittenScore;	//学习计划卷面成绩
	
	private Long teachingPlan_Course_studyHour;	//教学计划课程总学时
	private Long teachingPlan_Course_faceStudyHour;	//教学计划课程面授学时
	private String teachingPlan_Course_examClassType;//考试类别
	private String teachingPlan_Course_isMainCourse;//是否主干课
	private String teachingPlan_Course_isDegreeCourse;//是否学位课
	
	private String isHasResource;//是否有课程资源
	private String openTerm;//开课学期
	
	private Set<TextBook> textBooks;
	
	private String textBookStr;

	public String getTextBookStr() {
		String textBookStr="";
		Set<TextBook> tbs = this.textBooks;
		for(TextBook tb:tbs){
			textBookStr+=tb.getBookName()+" ";
		}
		return textBookStr;
	}
	public String getTeachingPlan_Course_openTerm_code() {
		return openTerm;
	}
	public void setTeachingPlan_Course_openTerm_code(String openTerm) {
		this.openTerm = openTerm;
	}
}
