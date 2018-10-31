package com.hnjk.core.support.base.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 提供基础的流程模型.<p>;
 * 流程业务表单需要继承这个类.
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-11 上午11:03:36
 * @see 
 * @version 1.0
 */
@MappedSuperclass
public class BaseWorkFlowModel extends BaseModel{

	private static final long serialVersionUID = 6422033714041254547L;
	
	//流程实例ID
	@Column(name="wf_id",nullable=false)
	private long wf_id;

	public long getWf_id() {
		return wf_id;
	}

	public void setWf_id(long wf_id) {
		this.wf_id = wf_id;
	}
	
}
