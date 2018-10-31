package com.hnjk.edu.teaching.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.CourseBook;

/**
 * 教学计划课程关联教材表
 * <code>TeachingPlanCourseBooks</code><p>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-7-14 上午10:39:25
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_PLANCOURSEBOOK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingPlanCourseBooks extends BaseModel{

	private static final long serialVersionUID = -1307434697953951400L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEBOOKID")
	private CourseBook courseBook;//教材
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANCOURSEID")
	private TeachingPlanCourse teachingPlanCourse;//所属教学计划课程

	/**
	 * @return the courseBook
	 */
	public CourseBook getCourseBook() {
		return courseBook;
	}

	/**
	 * @param courseBook the courseBook to set
	 */
	public void setCourseBook(CourseBook courseBook) {
		this.courseBook = courseBook;
	}

	/**
	 * @return the teachingPlanCourse
	 */
	public TeachingPlanCourse getTeachingPlanCourse() {
		return teachingPlanCourse;
	}

	/**
	 * @param teachingPlanCourse the teachingPlanCourse to set
	 */
	public void setTeachingPlanCourse(TeachingPlanCourse teachingPlanCourse) {
		this.teachingPlanCourse = teachingPlanCourse;
	}
	
	
	
}
