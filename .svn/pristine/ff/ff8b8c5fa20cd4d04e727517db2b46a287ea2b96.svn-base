package com.hnjk.edu.basedata.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * <code>BaseClassic</code>基础数据-层次类别表
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午10:48:14
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_CLASSIC")
@NamedQueries({
    @NamedQuery(
            name = "findClassicByName",
            query = "select c from Classic c where c.classicName = :classicName "
    )
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Classic extends BaseModel implements Serializable{

	private static final long serialVersionUID = -444720872742521006L;

	@Column(name = "CLASSICNAME", nullable = false, unique = true)
	private String classicName;// 层次名称

	@Column(name = "CLASSICENNAME")
	private String classicEnName;// 层次英文名

	@Column(name = "STARTPOINT", nullable = false)
	private String startPoint;// 起点

	@Column(name = "ENDPOINT", nullable = false)
	private String endPoint;// 终点

	@Column(name = "SHORTNAME", nullable = false)
	private String shortName;// 简称

	@Column(name = "CLASSICCODE", nullable = false)
	private String classicCode;// 层次代码,取基础数据

	@Column(name = "MEMO")
	private String memo;// 备注
	
	@Column(name = "CLASSICCODE4STUDYNO")
	private String classicCode4StudyNo;//层次编码(用于编学号)
	
	public String getClassicCode4StudyNo() {
		return classicCode4StudyNo;
	}

	public void setClassicCode4StudyNo(String classicCode4StudyNo) {
		this.classicCode4StudyNo = classicCode4StudyNo;
	}

	@Column(name = "SHOWORDER")
	private int showOrder;// 排序号

	public String getClassicCode() {
		return classicCode;
	}

	public void setClassicCode(String classicCode) {
		this.classicCode = classicCode;
	}

	public String getClassicEnName() {
		return classicEnName;
	}

	public void setClassicEnName(String classicEnName) {
		this.classicEnName = classicEnName;
	}

	public String getClassicName() {
		return classicName;
	}

	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	@Override
	public String toString() {
		return getClassicName();
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
}