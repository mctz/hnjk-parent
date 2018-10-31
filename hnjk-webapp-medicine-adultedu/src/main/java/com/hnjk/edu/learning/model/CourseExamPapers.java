package com.hnjk.edu.learning.model;

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;

/**
 * 考试试卷表.
 * <code>CourseExamPapers</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-4-13 上午10:01:01
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_EXAMPAPERS")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseExamPapers extends BaseModel{
	
	@Column(name="PAPERNAME",nullable=false)
	private String paperName;//试卷名称
	
	@Column(name="PAPERTYPE")
	private String paperType;//试卷类型：字典  ：入学考试/单元测试/期中考试/期末考试
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//关联的课程，由于入学考试的课程是保存在字典中的，所以入学考试课程无法管理这个字段
	
	
	@Column(name="COURSENAME")
	private String courseName;//课程名
	
	@Column(name="PAPERTIME")
	private Long paperTime;//考试时间 ,以分钟为单位
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="FILLINMAN")
	private String fillinMan;//录入人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//录入人ID
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate;//录入时间

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")
	private Classic classic;//层次
	
	@Column(name="ISOPENED")
	private String isOpened = Constants.BOOLEAN_YES;//是否开放
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "courseExamPapers")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	@OrderBy("showOrder ASC")
	@DeleteChild(deleteable=true)
	private Set<CourseExamPaperDetails> courseExamPaperDetails = new LinkedHashSet<CourseExamPaperDetails>(0);//试卷 - 试题
	
	/**
	 * 获取试题下个排序号
	 * @return
	 */
	public int getNextShowOrder(){
		int max = 0;
		for (CourseExamPaperDetails detail : getCourseExamPaperDetails()) {
			if (detail.getShowOrder() > max) {
				max = detail.getShowOrder();
			}
		}
		return ++max;
	}

	/**
	 * 检查试卷中是否存在试题
	 * @param courseExamId
	 * @return
	 */
	public boolean isExistCourseExam(String courseExamId){
		for (CourseExamPaperDetails detail : getCourseExamPaperDetails()) {
			if (detail.getCourseExam().getResourceid().equals(courseExamId)) {
				return true;
			}
		}
		return false;
	}
}
