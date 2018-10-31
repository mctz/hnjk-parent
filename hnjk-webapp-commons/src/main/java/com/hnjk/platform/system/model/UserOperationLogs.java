package com.hnjk.platform.system.model;

import java.util.Date;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 用户操作日志.
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_SYS_OPERATELOGS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class UserOperationLogs extends BaseModel{
	
	public static final String INSERT = "INSERT";//新增数据
	public static final String UPDATE = "UPDATE";//修改数据
	public static final String DELETE = "DELETE";//删除数据
	public static final String REPEAL = "REPEAL";//撤销数据
	public static final String PASS = "PASS";//发布/取消发布	
	public static final String COPY = "COPY";//复制数据
	public static final String IMPORT = "IMPORT";//导入数据
	public static final String EXPORT = "EXPORT";//导出数据
	public static final String PRINT = "PRINT";//打印
	public static final String LOGIN = "LOGIN";//登录
	public static final String LOGOUT = "LOGOUT";//登出
	public static final String SWITCH = "SWITCH";//切换
	public static final String EXE_FILE = "EXE_FILE";//执行文件命令
	
	@Column(name="IPADDRESS",length=255)
	private String ipaddress;//IP
	
	@Column(name="USERID",length=36)
	private String userId;//用户ID
	
	@Column(name="USERNAME",length=50)
	private String userName;//用户名
	
	/**
	 * dictCode： systemLogsModulesCode
	 * 1、站点管理
	 * 2、录取管理
	 * 3、学籍管理
	 * 4、教学管理
	 * 5、成绩管理
	 * 6、收费管理
	 * 7、基础数据管理
	 * 8、系统管理
	 * 9、资源管理
	 * 10、在线教学
	 * 11、教学过程管理
	 * 12、教学计划管理
	 * 13、学生工作管理
	 */
	@Column(name="MODULES")
	public String modules;
	
	
	@Column(name="OPERATIONTYPE")
	private String operationType;//操作类型
	
	@Column(name="OPERATIONCONTENT")
	private String operationContent;//操作内容
	
	@Column(name="EXPORTFLAG")
	private String exportFlag = "0";//导出记录:0-未导出，1-已导出
	
	@Column(name="RECORDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordTime;//记录时间

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTACHID")
	private Attachs  attachs;//附件

	public UserOperationLogs() {
		super();
	}

	public UserOperationLogs(String ipaddress, String userId, String userName,
			String modules, String operationType, String operationContent,
			String exportFlag, Date recordTime) {
		super();
		this.ipaddress = ipaddress;
		this.userId = userId;
		this.userName = userName;
		this.modules = modules;
		this.operationType = operationType;
		this.operationContent = operationContent;
		this.exportFlag = exportFlag;
		this.recordTime = recordTime;
	}
	
}
