package com.hnjk.edu.teaching.model;

// default package

import java.text.ParseException;
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

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.security.model.OrgUnit;

/**
 * 考试(课程安排)信息表
 * <code>ExamInfo</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午03:18:17
 * @see
 * @version 1.0
 */
@Entity
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Table(name = "EDU_TEACH_EXAMINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamInfo extends BaseModel {

	@OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 考试课程
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXAMSTARTTIME")
	private Date examStartTime;// 考试开始时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXAMENDTIME")
	private Date examEndTime;// 考试结束时间

	@Column(name = "ISOUTPLANCOURSE", length = 1)
	private String isOutplanCourse;// 是否计划外课程 0、计划内课程 1、计划外课程

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "SHOWORDER")
	private Long showOrder;// 排序号

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMSUBID")
	private ExamSub examSub;// 考试/毕业论文批次预约表

	/**3.1.9 新增考试课程编号*/
	@Column(name="EXAMCOURSECODE")
	private String examCourseCode;//考试课程编号
	
	/**3.1.10 新增在线考试成卷规则*/
	@OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMRULEID")
	private CourseExamRules courseExamRules;//成卷规则
	
	/**3.1.10新增考试课程类型*/
	@Column(name="EXAMCOURSETYPE") 
	private Integer examCourseType;//考试课程类型  CodeExamInfoCourseType   0、网络教育课程、1、面授课程   2、面授+网授 3、期末机考  4、网络成人课程
	
	@OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOOLID")
	private OrgUnit brSchool;//所属学校中心
	
	/**3.1.11 新增机考类型*/
	@Column(name="ISMACHINEEXAM")
	private String isMachineExam = Constants.BOOLEAN_NO;//是否机考
	
	@Column(name="ADJUSTLINE")
	private Double adjustLine;//成绩调整分数线
	
	@Column(name="ISSHOWSCORE")
	private String isShowScore = Constants.BOOLEAN_NO;//交卷时是否显示成绩
	
	/**网络课程卷面成绩比例*/
	@Column(name="STUDYSCOREPER",length=6,scale=2)
	private Double studyScorePer;//考试课程对应网络卷面分比例

	/**面授课程卷面成绩比例*/
	@Column(name="FACESTUDYSCOREPER",length=6,scale=2)
	private Double facestudyScorePer;//考试课程对应的 面授学习方式卷面分比例
	
	/**面授课程平时成绩比例*/
	@Column(name="FACESTUDYSCOREPER2",length=6,scale=2)
	private Double facestudyScorePer2;//考试课程对应的 面授学习方式平时考核分比例
	
	@Deprecated
	@Column(name="FACESTUDYSCOREPER3",length=6,scale=2)
	private Double facestudyScorePer3;//考试课程对应的 面授学习方式网上学习分比例
	
	/**网络课程平时成绩比例*/
	@Column(name="NETSIDESTUDYSCOREPER",length=6,scale=2)
	private Double netsidestudyScorePer;//考试课程对应的网络面授学习方式平时分比例
	
	@Column(name="ISABNORMITYEND")
	private String isAbnormityEnd=Constants.BOOLEAN_NO;//异常成绩录入是否结束
	
	/**3.2.5 新增机考场次名称*/
	@Column(name="MACHINEEXAMNAME")
	private String machineExamName;//机考场次名
	
	/**3.2.6 新增机考混合考试*/
	@Column(name="ISMIXTURE",length=1)
	private String ismixture = Constants.BOOLEAN_NO;//是否混合
	
	@Column(name="MIXTRUESCOREPER")
	private Double mixtrueScorePer;//混合考，机考与笔考成绩比例
	
	@Column(name="EXAMPAPERTYPE")
	private String examPaperType;//试卷类型:随机试卷－random，固定试卷-fixed
		
	@OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMPAPERID")
	private CourseExamPapers courseExamPapers;//固定试卷

	/**
	 * 成绩类型:CodeCourseScoreStyle
	 * 10	数值型
	 * 11	百分制
	 * 12	150分制
	 * 20	字符型
	 * 22	二分制
	 * 23	三分制
	 * 24	四分制
	 * 25	五分制
	 * 30	等级制
	 */
	@Column(name="COURSESCORETYPE")
	private String  courseScoreType;

	/**3.2.11 新增考试类型，用来区分机考+纸质作答，机考+在线作答，机考+纸质+在线作答*/
	@Column(name="EXAMTYPE")
	private String examType;//考试类型:机考+纸质作答=1，机考+在线作答=2，机考+纸质+在线作答=3

	/**
	 * 教学形式（统考、非统考）
	 * 前字段名为考试课程类型examCourseType，根据教学形式、课程考试类型 取值：非统考为1，统考为2，机考为3，以及数据字典CodeExamInfoCourseType
	 */
	@Deprecated
	@Column(name="teachtype")
	private Integer teachtype;

	@Transient
	private String examTimeSegmentFlag;//第一天上午A 第一天中午 B....
	@Transient
	private String examTime;//考试时间


	public String getExamTimeSegmentFlag() {
		if (ExStringUtils.isNotEmpty(this.examCourseCode)) {
			return examCourseCode.substring(0,1);
		}else {
			return "";
		}
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
	
	public String getExamTime() {
		String examTime = "";
		try {
			if(getExamStartTime() != null){
				examTime += ExDateUtils.formatDateStr(getExamStartTime(), ExDateUtils.PATTREN_DATE_TIME);
			}
			if(getExamEndTime() != null){
				examTime += "-"+ExDateUtils.formatDateStr(getExamEndTime(), ExDateUtils.PATTREN_DATE_TIME);
			}
		} catch (ParseException e) {			
		}		
		return examTime;
	}
	
}