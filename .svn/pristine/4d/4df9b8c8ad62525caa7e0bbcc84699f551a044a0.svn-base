package com.hnjk.edu.teaching.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Attachs;

/**
 * 毕业生与老师毕业论文交流表
 * <code>GraduateMsg</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午02:31:54
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_GRADUATEMSG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduateMsg extends BaseModel{

	private static final long serialVersionUID = -4641365239770854103L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "PAPERSORDERID")
	private GraduatePapersOrder graduatePapersOrder;//毕业论文预约信息
	
	@Column(name="SENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime = new Date();//发送时间
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", nullable = false)
	private String content;//交流内容
	
	@Column(name="CURRENTTACHE")
	private String currentTache;//所属毕业环节 取自字典：提纲初稿/二稿/定稿 .如果为
	
	@Column(name="FILLINMANID")
	private String fillinManId;//留言人ID
	
	@Column(name="FILLINMAN")
	private String fillinMan;//留言人姓名

	/**3.1.10 新增学生与老师小组讨论字段*/
	@Column(name="TITLE",length=255)
	private String title;//讨论话题
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private GraduateMsg parent;//话题
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy(value = "sendTime")
	@Where(clause = "isDeleted=0")
	@DeleteChild(deleteable=true)
	private Set<GraduateMsg> childs = new HashSet<GraduateMsg>();//用于多人讨论话题
	
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADUATEMENTORID")
	private GraduateMentor graduateMentor;//话题所关联小组（老师与学生分配表）
	
	@Column(name="ISSTUDENT")
	private String isStudent;//是否学生	，用于获取不同角色的基本信息
	
	@Column(name="ISGROUPTOPIC")
	private String  isGroupTopic = Constants.BOOLEAN_NO ;//是否讨论话题,区分讨论话题与个人辅导话题
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();//附件
	@Transient
	private Date lastReplyDate;//最新回复日期
	@Transient
	private String lastReplyMan;//最新留言人
	
	
	/**
	 * @return the isGroupTopic
	 */
	public String getIsGroupTopic() {
		return isGroupTopic;
	}

	/**
	 * @param isGroupTopic the isGroupTopic to set
	 */
	public void setIsGroupTopic(String isGroupTopic) {
		this.isGroupTopic = isGroupTopic;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the parent
	 */
	public GraduateMsg getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(GraduateMsg parent) {
		this.parent = parent;
	}

	/**
	 * @return the childs
	 */
	public Set<GraduateMsg> getChilds() {
		return childs;
	}

	/**
	 * @param childs the childs to set
	 */
	public void setChilds(Set<GraduateMsg> childs) {
		this.childs = childs;
	}

	/**
	 * @return the isStudent
	 */
	public String getIsStudent() {
		return isStudent;
	}

	/**
	 * @param isStudent the isStudent to set
	 */
	public void setIsStudent(String isStudent) {
		this.isStudent = isStudent;
	}

	/**
	 * @return the graduateMentor
	 */
	public GraduateMentor getGraduateMentor() {
		return graduateMentor;
	}

	/**
	 * @param graduateMentor the graduateMentor to set
	 */
	public void setGraduateMentor(GraduateMentor graduateMentor) {
		this.graduateMentor = graduateMentor;
	}

	

	public List<Attachs> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCurrentTache() {
		return currentTache;
	}

	public void setCurrentTache(String currentTache) {
		this.currentTache = currentTache;
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

	public GraduatePapersOrder getGraduatePapersOrder() {
		return graduatePapersOrder;
	}

	public void setGraduatePapersOrder(GraduatePapersOrder graduatePapersOrder) {
		this.graduatePapersOrder = graduatePapersOrder;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public Date getLastReplyDate() {
		Date d = getSendTime();
		for (GraduateMsg msg : getChilds()) {
			if(msg.getSendTime().after(d)){
				d = msg.getSendTime();
			}
		}
		return d;
	}
	
	public String getLastReplyMan() {
		Date d = getSendTime();
		String fillinMan = getFillinMan();
		for (GraduateMsg msg : getChilds()) {
			if(msg.getSendTime().after(d)){
				fillinMan = msg.getFillinMan();
			}
		}
		return fillinMan;
	}
}
