package com.hnjk.edu.basedata.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * <code>Classroom</code>基础数据-校外学习中心-教学楼-课室
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午11:49:45
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_CLASSROOM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Classroom extends BaseModel implements Serializable{

	private static final long serialVersionUID = 8279013750080108245L;

	// 课室名称
	@Column(name = "CLASSROOMNAME", nullable = false)
	private String classroomName;

	// 课室类型，取基础数据
	@Column(name = "CLASSROOMTYPE", nullable = true)
	private String classroomType;

	// 所在楼层
	@Column(name = "LAYERNO", nullable = false)
	private Long layerNo;

	// 所在房号
	@Column(name = "UNITNO", nullable = false)
	private Long unitNo;

	// 座位数
	@Column(name = "SEATNUM", nullable = true)
	private Long seatNum = 0L;

	// 单座位数
	@Column(name = "SINGLESEATNUM", nullable = false)
	private Long singleSeatNum = 0L;

	// 双座位数
	@Column(name = "DOUBLESEATNUM", nullable = false)
	private Long doubleSeatNum = 0L;

	// 是否直播室
	@Column(name = "ISLIVING")
	private String isLiving;

	// 备注
	@Column(name = "MEMO")
	private String memo;

	// 排序号
	@Column(name = "SHOWORDER")
	private Long showOrder = 0L;

	// 所属教学楼
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDINGID")
	private Building building;

	//状态	0：正常，1：维修	默认0
	@Column(name = "STATUS")
	private Integer status = 0;

	//排课可用	Y/N
	@Column(name = "ISUSECOURSE")
	private String isUseCourse;

	//排课可用开始时间
	@Column(name="startDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	//排课可用结束时间
	@Column(name="endDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	//排考可用	Y/N
	@Column(name = "ISUSEEXAM")
	private String isUseExam;

	//有无空调
	@Column(name = "HASAIR")
	private String hasAir;

	//课室号
	@Column(name = "ROOMCODE")
	private String roomCode;
	
	/**
	 * 20141031 新增字段--所属班级
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSID")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private ClassInfo classInfo;
	

	@Transient
	private String buildingId;

}