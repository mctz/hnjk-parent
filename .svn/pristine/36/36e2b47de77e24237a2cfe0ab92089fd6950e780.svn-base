package com.hnjk.edu.learning.model;

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
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.platform.taglib.JstlCustomFunction;

/**
 * 
 * 试题成卷规则表
 * <code>CourseExamRules</code><p>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-7-26 下午04:06:29
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_EXAMRULES")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseExamRules extends BaseModel{

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")	
	private Course course;//关联课程
	
	
	@Column(name="COURSENAME")
	private String courseName;//课程名，入学考试
	
	@Column(name="ISENROLEXAM")
	private String isEnrolExam;//是否入学考试
	
	@Column(name="VERSIONNUM")
	private Integer versionNum = 1;//版本号

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "courseExamRules")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	@OrderBy("showOrder asc")
	@DeleteChild(deleteable=true)
	private Set<CourseExamRulesDetails> courseExamRulesDetails = new LinkedHashSet<CourseExamRulesDetails>(0);//规则明细
	
	@Column(name="EXAMTIMELONG")
	private Integer examTimeLong;//考试时长
	
	@Transient
	private String courseExamRulesName;

	/**3.1.10 新增试题来源*/
	@Column(name="PAPERSOURSE")
	private String paperSourse;//试题来源  ，取字典值

	public String getCourseExamRulesName() {
		if(Constants.BOOLEAN_YES.equals(getIsEnrolExam())){
			return JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam", getCourseName())+"("+getVersionNum()+")";
		} else {
			String courseExamRulesName = getCourse().getCourseName()+"("+getVersionNum()+")";
//			if(ExStringUtils.isNotBlank(getPaperSourse())){
//				courseExamRulesName += "(试题来源:"+JstlCustomFunction.dictionaryCode2Value("CodeExamform", getPaperSourse())+")";
//			}
			return courseExamRulesName;
		}
	}

}
