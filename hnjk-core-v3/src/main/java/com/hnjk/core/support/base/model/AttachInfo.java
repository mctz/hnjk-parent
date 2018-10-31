package com.hnjk.core.support.base.model;

import java.util.Date;

/**
 * 系统附件. <code>AttachInfo</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-2 上午11:36:49
 * @see
 * @version 1.0
 */
public class AttachInfo extends BaseModel {

	private static final long serialVersionUID = 2225867624463776873L;

	private String formId;// 表单ID

	private String attName;// 附件名（原始名）

	private String serName;// 附件存储在服务器上的名称

	private String attType;// 附件类型

	private long attSize = 0;// 附件大小

	private String serPath;// 服务器存储目录

	private String saveType;// 保存方式，如local,ftp等

	private Date uploadTime = new Date();// 上传时间

	private int showOrder;// 排序号

	private String fillinName;// 上传人

	private String fillinNameId;// 上传人ID
	
	private String cacheWebPath;//缓存的web服务器路径
	
	

	public String getCacheWebPath() {
		return cacheWebPath;
	}

	public void setCacheWebPath(String cacheWebPath) {
		this.cacheWebPath = cacheWebPath;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public long getAttSize() {
		return attSize;
	}

	public void setAttSize(long attSize) {
		this.attSize = attSize;
	}

	public String getAttType() {
		return attType;
	}

	public void setAttType(String attType) {
		this.attType = attType;
	}

	public String getFillinName() {
		return fillinName;
	}

	public void setFillinName(String fillinName) {
		this.fillinName = fillinName;
	}

	public String getFillinNameId() {
		return fillinNameId;
	}

	public void setFillinNameId(String fillinNameId) {
		this.fillinNameId = fillinNameId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getSerName() {
		return serName;
	}

	public void setSerName(String serName) {
		this.serName = serName;
	}

	public String getSerPath() {
		return serPath;
	}

	public void setSerPath(String serPath) {
		this.serPath = serPath;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

}
