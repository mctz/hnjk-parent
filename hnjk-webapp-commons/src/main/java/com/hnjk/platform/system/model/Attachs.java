package com.hnjk.platform.system.model;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统附件表. <code>AttachInfo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-2 上午11:36:49
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_SYS_ATTACHS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Attachs extends BaseModel implements Serializable{

	/**附件存储方式 - ftp*/
	public final  static  String FILE_SAVETYPE_FTP = "ftp";
	
	/**附件存储方式 - 本地存储*/
	public final  static  String FILE_SAVETYPE_LOCAL = "local";
		
	@Column(name = "FORMID")
	private String formId;//表单ID
	
	@Column(name="FORMTYPE")
	private String formType;//表单类型
	
	@Column(name = "ATTNAME",nullable=false)
	private String attName;//附件名（原始名）
	
	@Column(name = "SERNAME",nullable=false)
	private String serName;//附件存储在服务器上的名称
	
	@Column(name = "ATTTYPE")
	private String attType;//附件类型
	
	@Column(name = "ATTSIZE")
	private Long attSize = 0L;//附件大小
	
	@Column(name="SERPATH")
	private String serPath;//服务器存储目录
	
	@Column(name="SAVETYPE")
	private String saveType = FILE_SAVETYPE_LOCAL;//保存方式，如local,ftp等
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPLOADTIME")
	private Date uploadTime = new Date();//上传时间
	
	@Column(name="CACHEWEBPATH")
	private String cacheWebPath;//缓存web服务器的路径
	
	@Column(name = "SHOWORDER")
	private Integer showOrder;//排序号
	
	@Column(name = "FILLINMAN")
	private String fillinName;//上传人
	
	@Column(name = "FILLINMANID")
	private String fillinNameId;//上传人ID
	
	/* ~~~~~~~~~用于FormBean~~~~~~~~*/
	@Transient
	private String isStoreToDatabase = Constants.BOOLEAN_YES;//是否存储数据
	
	@Transient
	private String replaceName;//替换文件名
	
	@Transient
	private String storePath;//服务器上存储路径

	/**
	 * 获取文件后缀名
	 * @return
	 */
	public String getPostfix() {
		return ExStringUtils.getPostfix(attName);
	}

	public File getFile() {
		File file =  new File(getSerPath() + File.separator + getSerName());
		return  file;
	}
}
