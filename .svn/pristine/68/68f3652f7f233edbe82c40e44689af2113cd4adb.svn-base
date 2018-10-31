package com.hnjk.edu.teaching.vo;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <code>学生成绩Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-5-18 上午10:20:03
 * @see 
 * @version 1.0
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentExamResultsVo implements Serializable {
	
	private String index;              //序号
	private Date   examStartTime;      //考试开始时间
	private Date   examEndTime;		   //考试结束时间
	private String examTime;   	       //考试时间  2010-12-25 09:00-11:00
	private Date   passTime;           //成绩审核通过时间
	private String courseId;           //课程ID
	private String courseCode;
	private String courseEnName;       //课程英文名
	private String courseName;		   //课程名称
	private String courseTerm;         //成绩对应的课程所在的学期
	private String courseType;         //课程性质
	private String courseTypeCode;     //课程性质(字典编码)
	private String inCreditHourStr;    //取得的学分字符形式，用于显示(如果小数点后为0，显示整数部分，否则显示全部)
	private Double inCreditHour;	   //取得的学分
	private Double creditHour;         //课程学分
	private String stydyHour;          //课程学时

	private String writtenScore;       //卷面成绩
	private String usuallyScore;       //平时成绩
	private String integratedScore;    //综合成绩 
	private Long   integratedScoreL;   //综合成绩-Long类型
	private String examResultsType;    //成绩类型--百分制、两分制
	private String examResultsChs;     //中文成绩--优、良、中、差
	private String examCount;          //考试次数
	private String examAbnormityCode;  //成绩异常代码--数据字典编码
	private String checkStatus;        //成绩审核状态
	private String checkStatusCode;    //成绩审核状态--数据字典编码
	private String isPass;             //是否通过
	private String isNoExam;           //是否免修 
	private String isStateExamResults; //是否统考
	private String isDegreeUnitExam;   //是否学位课程
	private String memo;               //备注
	private String isOutplancourse="N";//是否计划外课程
	private String examType;           //考试形式
	private String isRebuilt;          //是否重修
	private String bkScore;		   //补考成绩
	private String jbScore;        //结补成绩
	
	
	private Double totalScore;           //取得的总分数
	private Double totalCredit;          //取得的总学分 
	private Double totalMajorCredit;     //取得本专业总学分
	private Double requiredCredit;       //取得的必修学分
	private Double electiveCredit;       //取得的选修学分
	private Double avgScore;             //参考平均分 
	
	
	private Double mainCourseScoreAvg;  //专业主干课平均分
	private Double baseCourseScoreAvg;  //专业基础课平均分
	
	private int  recordCount;          //成绩记录总数
	private int  compulsoryed=0;       //已修必修课数
	private int  limitedCount= 0;      //限选课门数
	private int  mainCourse=0;         //专业主干课门数
	private int  baseCourse=0;         //专业基础课门数
	
	private boolean isDegreeEnglishPass; //是否通过学位英语
	private String thesisScore;        //毕业论文成绩
	private String batchName;          //考试批次名称
	private String isMakeupExam;       //是否补考
	
	//2014-8-13 16:12:24
	private String ismakeupexam; //补考状态
	private String isdelayexam; //缓考状态
	private String isunScore;//免修,免考状态
	private String makeupexamScore; //补考成绩
	private String delayexamScore;//缓考成绩
	private String unScore;//免修,免考成绩
	
	private String examSubType; //对应现在的成绩在考试批次里加入的 正考/一补...等  字典ExamResult
	private int examOrder;//成绩排序
	private String examClassType; //考核形式：字典
	private String courseNature;//课程类别
	
	private String examSubId;//考试批次id
	private String GPA;// 绩点
	private String sortNum;// 序号
	
	private String examResultId;// 考试id,增加20161017
	private String isMarkDelete;// 标记删除,增加20161017
	//private String scoreStyle;//成绩类型 30:等级制
	private String resourceid;
	
	private String studyNo;
	private String studentName;
	private String courseYearTerm;

	private String flag;
	private String message;

	private String classesid;

	private String index2;
	private String courseName2 = "";
	private String integratedScore2;
	public String getIsmakeupexam() {
		return this.ismakeupexam;
	}
	public void setIsmakeupexam(String ismakeupexam) {
		this.ismakeupexam = ismakeupexam;
	}
	public String getIsMakeupExam() {
		return this.isMakeupExam;
	}
	public void setIsMakeupExam(String isMakeupExam) {
		this.isMakeupExam = isMakeupExam;
	}
	public String getExamTime() {
		String str = "";
		try {
			if (null!=this.examStartTime) {
				str    +=ExDateUtils.formatDateStr(this.examStartTime,"yyyy-MM-dd HH:mm");
			}
			str    += "-";
			if (null!=this.examEndTime) {
				str    +=ExDateUtils.formatDateStr(this.examEndTime,"HH:mm");
			}
			if (null!=this.passTime) {
				str = ExDateUtils.formatDateStr(this.passTime,"yyyy-MM-dd");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public String getInCreditHourStr() {
		if (null!=inCreditHour) {
			inCreditHourStr 	  = inCreditHour.toString();
			boolean isDouble      = ExStringUtils.containsAny(inCreditHourStr,".".toCharArray());
			if ((isDouble&&ExStringUtils.endsWith(inCreditHourStr, "0"))||isDouble==false) {//学分为.0的，或没有小数位的显示为整数
				inCreditHourStr   = String.valueOf(inCreditHour.intValue());
			}
		}
		return inCreditHourStr;
	}

	public String getExamCount() {
		if (ExStringUtils.isNotEmpty(examCount) && !"1".equals(examCount)) {
			return "第"+examCount+"次重修";
		}else {
			return "正常";
		}
	}

	public int getExamCountInt() {
		int count = 0;
		if(ExStringUtils.isNumeric(examCount, 1)){
			count = Integer.parseInt(examCount);
		}
		return count;
	}
}
