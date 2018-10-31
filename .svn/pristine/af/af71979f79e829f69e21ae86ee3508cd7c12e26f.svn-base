package com.hnjk.edu.teaching.model;

// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 毕业环节截止时间表
 * <code>GradendDate</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午02:56:58
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_GRADENDDATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GradendDate   extends BaseModel {


	private static final long serialVersionUID = 7097068848753833959L;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="SYLLABUSENDDATE")
    private Date syllabusEndDate;//写作提纲截止时间
    
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FIRSTDRAFTENDDATE")
    private Date firstDraftEndDate;//初稿二稿截止时间
    
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="SECONDDRAFTENDDATE")
    private Date secondDraftEndDate;//定稿截止时间
    
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="PUBLISHDATE")
    private Date publishDate;//预约公布时间
	
	/**3.1.11 新增毕业论文答辩成绩录入时间*/
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="ORALEXAMINPUTSTARTTIME")
    private Date oralexaminputStartTime;//答辩成绩录入开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="ORALEXAMINPUTENDTIME")
    private Date oralexaminputEndTime;//答辩成绩录入截止时间

	public Date getFirstDraftEndDate() {
		return firstDraftEndDate;
	}

	public void setFirstDraftEndDate(Date firstDraftEndDate) {
		this.firstDraftEndDate = firstDraftEndDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getSecondDraftEndDate() {
		return secondDraftEndDate;
	}

	public void setSecondDraftEndDate(Date secondDraftEndDate) {
		this.secondDraftEndDate = secondDraftEndDate;
	}

	public Date getSyllabusEndDate() {
		return syllabusEndDate;
	}

	public void setSyllabusEndDate(Date syllabusEndDate) {
		this.syllabusEndDate = syllabusEndDate;
	}

	public Date getOralexaminputStartTime() {
		return oralexaminputStartTime;
	}

	public void setOralexaminputStartTime(Date oralexaminputStartTime) {
		this.oralexaminputStartTime = oralexaminputStartTime;
	}

	public Date getOralexaminputEndTime() {
		return oralexaminputEndTime;
	}

	public void setOralexaminputEndTime(Date oralexaminputEndTime) {
		this.oralexaminputEndTime = oralexaminputEndTime;
	} 
	
}