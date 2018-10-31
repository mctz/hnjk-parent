package com.hnjk.core.support.base.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * 
 * <code>IBaseTreeModel</code><p>
 * 数模型接口.用来生产数据树.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-6-28 下午07:34:15
 * @see 
 * @version 1.0
 */
@MappedSuperclass
public abstract class BaseTreeModel extends BaseModel {

	public abstract String getNodeName();//节点名称
	
	public abstract String getNodeCode();//节点编码
	
	public  String getNodeId(){//节点ID
		return getResourceid();
	}
	
	public abstract String getParentNodeId();//父节点ID
	
	public abstract Integer getNodeLevel();//节点层级
}
