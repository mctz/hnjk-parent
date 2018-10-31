package com.opensymphony.module.propertyset.hibernate3;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Property表的复合主键.
 * 
 * @author hzg
 * 
 */
@Embeddable
public class PropertyPK implements java.io.Serializable {

	private static final long serialVersionUID = 7405381434768642492L;

	public PropertyPK() {
	}


	@Column(name="ENTITY_ID",nullable=false)
	private Long entityId;// 流程实例ID

	@Column(name="ENTITY_NAME",nullable=false)
	private String entityName;// 流程实例名称

	@Column(name="ENTITY_KEY",nullable=false)
	private String key;// 属性KEY

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof PropertyPK)) {
			return false;
		}

		final PropertyPK pro = (PropertyPK) obj;
		if (!key.equals(pro.getKey()) || !entityName.equals(pro.getEntityName())) {
			return false;
		}
		if (null == entityId || entityId.intValue() != pro.getEntityId()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = entityId.hashCode();
		result = 29 * result + entityName.hashCode() + key.hashCode();
		return result;
	}

}
