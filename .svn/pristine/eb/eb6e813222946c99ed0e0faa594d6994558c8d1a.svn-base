package com.hnjk.core.support.base.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 用来excel导入导出的基本模型. <p>
 * 使用excel导入导出的模型必须继承此基类.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-30上午11:39:35
 * @see {@link BaseModel}
 * @version 1.0
 */
@MappedSuperclass
public class BaseExcelModel extends BaseModel{

	private static final long serialVersionUID = -8511856142623353846L;
	
	@Transient
	private String flag;//标示
	
	@Transient
	private String message;//提示消息

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
