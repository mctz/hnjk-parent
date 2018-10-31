package com.hnjk.edu.teaching.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Examroom;

/**
 * 试卷袋标签明细表
 * <code>ExamPaperBagDetails</code><p>
 * @author：广东学苑教育发展有限公司
 * @since： 2011-10-30 上午10:31:00
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMPAPERBAGDETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamPaperBagDetails extends BaseModel {

	private static final long serialVersionUID = 1244127333505665407L;
	
	@Column(name="TOTALBAGNNUM")
	private Integer totalBagNum ;       //该课程试卷袋总包数
	
	@Column(name="TOTALBAGINDEX")
	private Integer totalBagIndex;      //占该课程试卷袋总包数的第几包        
	
	@Column(name="BAGINDEX")
	private Integer bagIndex;           //占学习中心该课程试卷袋包数的第几包        
	
	@Column(name="EXAMNUM")
	private Integer examNum;            //应考人数
	
	@Column(name="PAPERNUM")
	private Integer paperNum;           //试卷份数
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name="EXAMROOMID")
	private Examroom examRoom;          //试室
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMPAPERBAGSTATID")
	private ExamPaperBag examPaperBag;  //学习中心试卷袋标签统计表
	
	
	public Integer getExamNum() {
		return examNum;
	}
	public void setExamNum(Integer examNum) {
		this.examNum = examNum;
	}
	public Integer getTotalBagNum() {
		return totalBagNum;
	}
	public void setTotalBagNum(Integer totalBagNum) {
		this.totalBagNum = totalBagNum;
	}
	public Integer getTotalBagIndex() {
		return totalBagIndex;
	}
	public void setTotalBagIndex(Integer totalBagIndex) {
		this.totalBagIndex = totalBagIndex;
	}
	public Integer getBagIndex() {
		return bagIndex;
	}
	public void setBagIndex(Integer bagIndex) {
		this.bagIndex = bagIndex;
	}
	public Integer getPaperNum() {
		return paperNum;
	}
	public void setPaperNum(Integer paperNum) {
		this.paperNum = paperNum;
	}
	public Examroom getExamRoom() {
		return examRoom;
	}
	public void setExamRoom(Examroom examRoom) {
		this.examRoom = examRoom;
	}
	public ExamPaperBag getExamPaperBag() {
		return examPaperBag;
	}
	public void setExamPaperBag(ExamPaperBag examPaperBag) {
		this.examPaperBag = examPaperBag;
	}
}
