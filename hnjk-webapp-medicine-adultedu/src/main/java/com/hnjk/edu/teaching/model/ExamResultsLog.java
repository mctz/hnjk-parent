package com.hnjk.edu.teaching.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.platform.system.model.Attachs;

/**
 * 学生成绩导入导出日志表
 * <code>ExamResultsLog</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-23 上午10:19:34
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_RESULTSLOG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamResultsLog extends BaseModel{

	private static final long serialVersionUID = 5456269603014385917L;
	
	@Column(name="OPTIONTYPE")
	private String optionType;//操作类型
	
	
	//TODO:this field is string ,why not class
	@Column(name="EXAMSUBID")
	private String examSubId;//考试批次ID
	
	@Column(name="FILLINMAN")
	private String fillinMan;//操作人姓名
	
	@Column(name="FILLINMANID")
	private String fillinManId;//操作人ID
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate;//操作日期
	
	@Column(name="ATTACHID")
	private String attachId;//对应的上传下载附件ID
	
	
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();

	public List<Attachs> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}

	public String getExamSubId() {
		return examSubId;
	}

	public void setExamSubId(String examSubId) {
		this.examSubId = examSubId;
	}

	public Date getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getFillinMan() {
		return fillinMan;
	}

	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	public String getFillinManId() {
		return fillinManId;
	}

	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	
	
}
