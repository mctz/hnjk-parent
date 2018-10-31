package com.hnjk.security.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseTreeModel;
import com.hnjk.core.support.context.Constants;

/**
 * 系统组织架构表. <code>OrgUnit</code>
 * <p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-3 下午05:41:52
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "hnjk_sys_unit")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrgUnit extends BaseTreeModel implements  Serializable{

	//public final static String ORGUNIT_BRSCHOOL = "brSchool";
	
	@Column(name = "unitName", nullable = false,unique = true)
	private String unitName;// 组织单位名

	@Column(name = "unitCode", nullable = false, unique = true)
	private String unitCode;// 组织编码

	@Column(name = "unitShortName")
	private String unitShortName;// 组织单位简称

	@Column(name = "unitLevel")
	private int unitLevel = 0;// 组织单位级别

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private OrgUnit parent;// 父单位

	@Column(name = "ISCHILD")
	private String isChild;// 是否有子集合

	@Column(name = "unitType")
	private String unitType;// 组织类型，如分公司，部门等

	@Column(name = "unitDescript")
	private String unitDescript;// 组织单位描述

	@Column(name = "showorder")
	private int showorder;// 排序号

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime")
	private Date createTime = new Date();// 创建时间
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showorder asc")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<OrgUnit> childs = new HashSet<OrgUnit>(0);

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "orgUnit")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause="isDeleted=0")
	private Set<User> user = new LinkedHashSet<User>(0);	//组织用户
	

	/** hnjk core 1.0.2 netedu 3.0.3 修改 */
	@Column(name="PRINCIPAL")
	private String principal;//负责人
	
	@Column(name="LINKMAN")
	private String linkman;//联系人
	
	@Column(name="CONTECTCALL")
	private String contectCall;//联系电话
	
	@Column(name="LOCALCITY")
	private String localCity;//所属城市
	
	@Column(name="ZIPCODE")
	private String zipcode;//邮编
	
	@Column(name="EMAIL")
	private String email;//邮件地址
	
	@Column(name="ADDRESS")
	private String address;//地址
	
	@Column(name="ISALLOWNEWMAJOR")
	private String isAllowNewMajor = Constants.BOOLEAN_YES;//是否允许开新专业 默认允许
	
	@Column(name="LIMITMAJORNUM")
	private Integer limitMajorNum = 0;//专业数限制 0 为不限制
	
	@Transient
	private String parentId;
	
	/**3.1.1 新增学习中心类别*/
	@Column(name="SCHOOLTYPE")
	private String schoolType;//学习中心类别(质量工程项目中，对应高等学校办学性质)
	
	/**3.1.4 新增组织状态*/
	@Column(name="STATUS")
	private String status;//状态 字典
	
	/**3.1.6新增签约开始结束时间*/
	@Column(name="STARTDATE")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name="ENDDATE")
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	/**新增是否允许分班 */
	@Column(name="isAllowClass")
	private String isAllowClass;//是否允许分班
	
	/***3.1.8新增是否机考*/
	private String isMachineExam;//是否机考
	
	@Transient
	private String unitCodeAndName;//编号+名称
	
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "orgUnit")
	@MapKey(name = "exCode")
	@Where(clause="isDeleted=0")
	private Map<String, OrgUnitExtends> orgUnitExtends = new LinkedHashMap<String, OrgUnitExtends>(0);//组织扩展
	
	@Column(name="PAYFORM")
	private String payForm;// 收费形式，字典CodeUnitPayForm：singlePremium-一次缴清，installmentPay-分期付
	
	@Column(name="ROYALTYRATE")
	private BigDecimal royaltyRate ;// 分成比例
	
	@Column(name="UNITCODE4STUNO")
	private String unitCode4StuNo;
	
	@Column(name="reportTime")
	private String reportTime;//报道时间
	
	@Column(name="reportSite")
	private String reportSite;//报道地点
	
	@Column(name="isRecruit")
	private String isRecruit = "Y";//是否招生
	
	// 20180731新增分成比例2和预留比例字段
	// 目前广外的外语类和非外语类有区分，这个是指外语类
	@Column(name="ROYALTYRATE2")
	private BigDecimal royaltyRate2;// 分成比例2
	
	@Column(name="RESERVERATIO")
	private BigDecimal reserveRatio;// 预留比例

	/**
	 * 是否市内教学点
	 */
	@Column
	private String isLocal;

	public String getUnitCodeAndName() {
		return this.unitCode+"-"+this.unitName;
	}

	/**
	 * 获取教学点简称（长度最短）
	 * @return
	 */
	public String getUnitShortNameForCustom() {
		if (unitName.length() > unitShortName.length()) {
			return unitShortName;
		} else {
			return unitName;
		}
	}

	@Override
	public String toString() {
		return unitName;
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.IBaseTreeModel#getNodeName()
	 */
	@Override
	public String getNodeName() {		
		return getUnitName();
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.IBaseTreeModel#getNodeCode()
	 */
	@Override
	public String getNodeCode() {	
		return getUnitCode();
	}


	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.IBaseTreeModel#getParentNodeId()
	 */
	@Override
	public String getParentNodeId() {		
		return getParentId();
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.model.IBaseTreeModel#getNodeLevel()
	 */
	@Override
	public Integer getNodeLevel() {		
		return getUnitLevel();
	}
	
}
