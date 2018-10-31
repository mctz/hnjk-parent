package com.hnjk.edu.portal.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.User;

/**
 * 消息接受者明细.
 * <code>MessageReceiver</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-16 上午10:53:07
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_PORTAL_RECEIVER")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageReceiver extends BaseModel{

	private static final long serialVersionUID = 3635644353406131051L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MSGID")
	private Message message;// 消息
	
	@Column(name="RECEIVETYPE",nullable=false)
	private String receiveType;//消息接收类型，字典CodeReceiveType： org - 按组织/ role - 按角色/ user - 按用户/grade - 年级/classes - 班级
	
	@Column(name="ORGUNITCODES")
	private String orgUnitCodes;//接受者：组织编码集合,使用',’分割
	
	@Column(name="ROLECODES")
	private String roleCodes;//接收者：角色编码集合,使用','分割
	
	/**
	 * 新增年级ID集合，班级ID集合这几个字段 2016-1-21
	 * TODO: 以后再看看有没有需要添加用户类型（学生，班主任，任课老师等），
	 * 目前年级和班级都只是发给学生
	 */
	@Column(name="GRADES")
	private String grades;// 接收者：年级ID集合，使用","分隔
	
	@Column(name="CLASSES")
	private String classes;// 接收者：班级ID集合，使用","分隔
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "messageReceiver")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	@OrderBy("resourceid ASC")
	@Where(clause = "isDeleted=0")
	private Set<MessageReceiverUser> messageReceiverUsers = new LinkedHashSet<MessageReceiverUser>(0);

	@Transient
	private String orgUnitNames;	
	@Transient
	private String roleNames;	
	@Transient
	private String userCnNames;
	@Transient
	private String userNames="";//接受者：用户名集合，使用','分割
	@Transient
	private String gradeNames;
	@Transient
	private String classesNames;
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getOrgUnitCodes() {
		return orgUnitCodes;
	}

	public void setOrgUnitCodes(String orgUnitCodes) {
		this.orgUnitCodes = orgUnitCodes;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getOrgUnitNames() {
		return orgUnitNames;
	}

	public void setOrgUnitNames(String orgUnitNames) {
		this.orgUnitNames = orgUnitNames;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getUserCnNames() {
		return userCnNames;
	}

	public void setUserCnNames(String userCnNames) {
		this.userCnNames = userCnNames;
	}

	public Set<MessageReceiverUser> getMessageReceiverUsers() {
		return messageReceiverUsers;
	}

	public void setMessageReceiverUsers(
			Set<MessageReceiverUser> messageReceiverUsers) {
		this.messageReceiverUsers = messageReceiverUsers;
	}

	public String getUserNames() {
		if(ExStringUtils.isEmpty(ExStringUtils.trimToEmpty(userNames))){
			for(MessageReceiverUser mu:messageReceiverUsers){
				userNames +=mu.getUserName()+",";
			}
		}
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public String getGrades() {
		return grades;
	}

	public void setGrades(String grades) {
		this.grades = grades;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getGradeNames() {
		return gradeNames;
	}

	public void setGradeNames(String gradeNames) {
		this.gradeNames = gradeNames;
	}

	public String getClassesNames() {
		return classesNames;
	}

	public void setClassesNames(String classesNames) {
		this.classesNames = classesNames;
	}
	
}
