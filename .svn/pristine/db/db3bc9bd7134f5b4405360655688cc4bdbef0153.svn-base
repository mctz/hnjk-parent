package com.hnjk.edu.learning.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.platform.system.model.Attachs;

/**
 * 课程 - 课后作业批次表
 * <code>MateResource</code>
 *  课程，作业批次，作业题主干，作业答案，学生完成作业情况之间的关系如下<p>
 *  一门课程有多个作业批次，课程与作业批次之间单向关联；
 *  一个作业批次里有多个作业题(主干);
 *  一个作业主干对应一个或多个答案；
 *  学生完成作业情况表用来记录学生回答的答案及评分.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:22:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_EXERCISEBATCH")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ExerciseBatch extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程

	@Column(name="COLNAME",nullable=false)
	private String colName;//批次名称
	
	@Column(name="COLTYPE")
	private String colType;//作业类型 ： 主观题、客观题
	
	@Column(name="DESCRIPT")
	private String descript;//作业说明
	
	@Column(name="STARTDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;//作业开始日期

	@Column(name="ENDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;//作业截止日期
	
	@Column(name="STATUS")
	private Integer status = 0;//作业批次状态：字典CodeExerciseBatchStatus：0-未发布，1-发布 ，-1-结束，2-部分批改，3-全部批改
	
	@Column(name="ISCOUNTDOWN")
	private String isCountdown = Constants.BOOLEAN_NO;//是否启用作业倒计时功能
	
	@Column(name="COUNTDOWNTIME")
	private Long countdownTime;//倒计时时间（秒）
	
	@Column(name="ISSCORING")
	private String isScoring = Constants.BOOLEAN_NO;//是否启用计分机制
	
	@Column(name="SCORINGTYPE")
	private String scoringType;//计分形式：平均分摊，按实际分数
	
	@Column(name="ISSHOWCORRECT")
	private String isShowcorrect = Constants.BOOLEAN_NO;//交题后是否显示正确答案

//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "ATTACHID")
//	private Attachs attachs;//离线作业信息

	@Column(name="FILLINMAN")
	private String fillinMan;//填写人姓名
	
	@Column(name="FILLINMANID")
	private String fillinManId;//填写人ID	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exerciseBatch")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause="isDeleted=0")
	@DeleteChild(deleteable=true)
	private Set<Exercise> exercises = new HashSet<Exercise>(0);
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();//离线作业
	
	/**3.1.1 新增年度和学期*/
	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private String term;//学期 字典值
	
	@Column(name="ORDERCOURSENUM")
	private Integer orderCourseNum;//预约该课程的学生人数
	
	//@Formula("(select count(s.resourceid) from edu_lear_studentexercise s where s.isdeleted=0 and s.exerciseid is null and s.status>0 and s.exercisebatchid=resourceid )")
	//private Integer handNum;//已提交
	
	//@Formula("(select count(s.resourceid) from edu_lear_studentexercise s where s.isdeleted=0 and s.exerciseid is null and s.status=2 and s.exercisebatchid=resourceid )")
	//private Integer correctNum;//已批改
	
	@Formula("(select count(b.resourceid) from edu_lear_exercisebatch b where b.isdeleted=0 and b.status>0 and b.courseid=courseid and b.yearid=yearid and b.term=term)")
	private Integer exerciseNum;//作业次数
	
	/**3.1.11 新增客观题数量*/
	@Column(name="OBJECTIVENUM")
	private Integer objectiveNum ;//客观题数量
	
	@Transient
	private String isEnd;//是否已到截止时间
	@Transient
	private long remainingTime;//剩余时间

	/**
	 * 是否发布成功
	 */
	@Transient
	private boolean isPublishOrUnpublish = false;
	
	/**
	 * 新增班级 在线学习
	 * @return
	 */
	@Column(name="CLASSESIDS")
	private String classesIds;//班级id 以“,”号分割
	
	@Column(name="CLASSESNAMES")
	private String classesNames;//班级名称 以“,”号分割


	public boolean getIsPublishOrUnpublish() {
		return this.isPublishOrUnpublish;
	}

	public void setIsPublishOrUnpublish(boolean isPublishOrUnpublish) {
		this.isPublishOrUnpublish = isPublishOrUnpublish;
	}

	public Integer getNextShowOrder(){
		int showOrder = 0;
		for (Exercise exercise : getExercises()) {
			if(exercise.getShowOrder()!=null && exercise.getShowOrder()>showOrder){
				showOrder = exercise.getShowOrder();
			}
		}
		return ++showOrder;
	}	
	
	public String getIsEnd() {
		if(ExDateUtils.getToday().after(getEndDate())){
			return Constants.BOOLEAN_YES;
		}
		return Constants.BOOLEAN_NO;
	}
	
	public long getRemainingTime() {
		if(Constants.BOOLEAN_NO.equals(getIsEnd())){
			//return (ExDateUtils.getDayEnd(getEndDate()).getTime()-new Date().getTime())/(24*60*60*1000);
			return (ExDateUtils.getDayEnd(getEndDate()).getTime()-System.currentTimeMillis())/(24*60*60*1000);
		}
		return 0;
	}

}
