package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.Syllabus;

/**
 * <code>MateResource</code>课程 - 素材资源表(打包拆分的课件).<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:22:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_MATE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class MateResource extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSTREEID")
	private Syllabus syllabus;//所属课程教学大纲知识节点
	
	@Column(name="MATENAME",nullable=false)
	private String mateName;//材料名称
	
	//3.1.2 废弃
	//@Column(name="MATECODE",unique=true)
	//private String mateCode;//材料编码
	
	@Column(name="MATETYPE")
	private String mateType;//来自字典CodeMateType，材料类型：文档/视频/音频/FLASH动画/综合
	
	@Column(name="MATEURL")
	private String mateUrl;//材料所在服务器上URL路径(公网地址)
	
	@Column(name="ISPUBLISHED")
	private String isPublished = Constants.BOOLEAN_NO;//是否发布 
	
	/**3.0.9新增教育网URL*/
	@Column(name="MATEEDUNETURL")
	private String mateEdunetUrl;//教育网地址，系统根据用户的登录来源，自动选择使用教育网地址还是公网地址
	
	@Transient
	private String realUrl;//存储实际url
	
	/**3.1.9 新增排序号*/
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号
	
	/**3.2.4 新增栏目类型，视频拍摄时间**/
	@Column(name="CHANNELTYPE",nullable=false)
	private String channelType;//栏目类型，来自字典表learnMetaChannelType
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程
	
	
	@Column(name="CAPTUREDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date captureDate;//拍摄时间
	
	@Column(name="TOTALTIME")
	private Integer totalTime;// 总时长
	
	//@Transient
	//private List<Attachs> attachs = new ArrayList<Attachs>();//附件列表

}
