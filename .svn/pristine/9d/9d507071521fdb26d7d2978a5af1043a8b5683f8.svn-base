package com.hnjk.edu.learning.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

import com.hnjk.core.support.base.model.BaseTreeModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.platform.system.model.Attachs;

/**
 * 教师课程文件表
 * 文件结构:
 * -ROOT
 * 		-folder1
 * 			-file.zip
 * 			-file2.doc
 * 			-file..n
 * 			-subfolder2
 * 				-subfile1.zip
 * 		-folder2
 * 		...
 * 
 * @author hzg
 *
 */

@Entity
@Table(name="EDU_LEAR_FILES")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeacherCourseFiles extends BaseTreeModel {
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程
	
	@Column(name="FILENAME",nullable=false)
	private String fileName;//文件或文件夹名
	
	@Column(name="FILETYPE")
	private Integer fileType = 0;//文件类型：0-文件夹 1-文件
	
	//@Column(name="SERNAME")
	//private String serName;//文件服务器存储名
	
	//@Column(name="SERPATH")
	//private String serPath;//文件服务器存储路径
	
	//@Column(name="FILESIZE")
	//private Long fileSize;//文件大小
	
	@Column(name="FILEURL")
	private String fileUrl;//文件URL
	
	//@Column(name="FILEEXT")
	//private String fileExt;//文件扩展名
	
	//@Column(name="STORETYPE")
	//private String storeType = Attachs.FILE_SAVETYPE_LOCAL;//文件存储方式
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTACHID")
	private Attachs attach;//关联的附件
	
	 @Column(name="ISCHILD",length=1)
	 private String isChild = Constants.BOOLEAN_NO;//是否有子
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private TeacherCourseFiles parent;//父
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder asc")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<TeacherCourseFiles> childs = new LinkedHashSet<TeacherCourseFiles>(0);//子
	
	@Column(name="DESCRIPTION")
	private String description;//描述
	
	@Column(name="PERMISSION")
	private String permission;//#目录权限，公开/私有/
	
	@Column(name="ISPUBLISHED",length=1)
	private String isPublished;//是否发布
	
	@Column(name="FILLINMAN")
	private String fillinMan;//创建人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//创建人ID
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate = new Date();//创建日期
	
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号

	@Column(name="NODELEVEL")
	private Integer fileLevel;//节点级别
	
	@Transient
	private String parentId;
	@Transient
	private String isOutlink;//是否外部附件
	
	/**3.3.2 新增是否被引用*/
	@Column(name="ISUSED",length=1)
	private String isUsed = Constants.BOOLEAN_NO;//是否被引用
	
	public Integer getNextShowOrder(){
		Integer nextShowOrder = 0;
		for (TeacherCourseFiles child : getChilds()) {
			if(child.getShowOrder() > nextShowOrder.intValue()){
				nextShowOrder = child.getShowOrder();
			}
		}
		return nextShowOrder+1;
	}
	@Override
	public String getNodeName() {
		return getFileName();
	}

	@Override
	public String getNodeCode() {
		return getResourceid();
	}

	@Override
	public String getParentNodeId() {
		return getParentId();
	}

	@Override
	public Integer getNodeLevel() {
		return getFileLevel();
	}
}
