package com.hnjk.edu.basedata.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.security.model.OrgUnit;

/**
 * <code>Examroom</code>基础数据-校外学习中心考场课室表.
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午12:46:10
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_EXAMROOM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Examroom extends BaseModel implements Serializable {

	private static final long serialVersionUID = 6390511302298255447L;

	@Column(name = "EXAMROOMNAME", nullable = false)
	private String examroomName;// 课室名称

	@Column(name = "EXAMROOMSIZE", nullable = false)
	private Long examroomSize;// 课室容量

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "SHOWORDER")
	private Long showOrder;// 排序号

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHSCHOOLID")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private OrgUnit branchSchool;// 所属校外学习中心
	
	/*20141031 新增字段--所属班级*/
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSID")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private ClassInfo classInfo;// 所属班级
	
	/**3.1.8新增字段*/
	@Column(name="SINGLESEATNUM")
	private Integer singleSeatNum;//单隔容量
	
	@Column(name="DOUBLESEATNUM")
	private Integer doubleSeatNum;//双隔容量
	
	@Column(name="ISCOMPUTERROOM",length=1)
	private String isComputerRoom = Constants.BOOLEAN_NO;//是否机房
	
	
	@Transient
	private String branchSchoolId;
	

	
	public ClassInfo getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(ClassInfo classInfo) {
		this.classInfo = classInfo;
	}
	

	/**
	 * @return the singleSeatNum
	 */
	public Integer getSingleSeatNum() {
		return singleSeatNum;
	}

	/**
	 * @param singleSeatNum the singleSeatNum to set
	 */
	public void setSingleSeatNum(Integer singleSeatNum) {
		this.singleSeatNum = singleSeatNum;
	}

	/**
	 * @return the doubleSeatNum
	 */
	public Integer getDoubleSeatNum() {
		return doubleSeatNum;
	}

	/**
	 * @param doubleSeatNum the doubleSeatNum to set
	 */
	public void setDoubleSeatNum(Integer doubleSeatNum) {
		this.doubleSeatNum = doubleSeatNum;
	}

	/**
	 * @return the isComputerRoom
	 */
	public String getIsComputerRoom() {
		return isComputerRoom;
	}

	/**
	 * @param isComputerRoom the isComputerRoom to set
	 */
	public void setIsComputerRoom(String isComputerRoom) {
		this.isComputerRoom = isComputerRoom;
	}

	public OrgUnit getBranchSchool() {
		return branchSchool;
	}

	public void setBranchSchool(OrgUnit branchSchool) {
		this.branchSchool = branchSchool;
	}

	public String getExamroomName() {
		return examroomName;
	}

	public void setExamroomName(String examroomName) {
		this.examroomName = examroomName;
	}

	public Long getExamroomSize() {
		return examroomSize;
	}

	public void setExamroomSize(Long examroomSize) {
		this.examroomSize = examroomSize;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public String getBranchSchoolId() {
		return branchSchoolId;
	}

	public void setBranchSchoolId(String branchSchoolId) {
		this.branchSchoolId = branchSchoolId;
	}

	@Override
	public String toString() {
		return getExamroomName();
	}

}