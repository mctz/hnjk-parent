package com.opensymphony.module.propertyset.hibernate3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 全局属性表
 * @author hzg
 *
 */
@Entity
@Table(name = "OS_PROPERTYENTRY")
@NamedQueries({
@NamedQuery(name="all_keys",
	query="select item.propertyPK.key from item in class PropertySetItem where item.propertyPK.entityName = :entityName and item.propertyPK.entityId = :entityId"),
@NamedQuery(name="all_keys_with_type",
	query="select item.propertyPK.key from item in class PropertySetItem where item.propertyPK.entityName = :entityName and item.propertyPK.entityId = :entityId and item.type = :type"),
@NamedQuery(name="all_keys_like",
	query="select item.propertyPK.key from item in class PropertySetItem where item.propertyPK.entityName = :entityName and item.propertyPK.entityId = :entityId and item.propertyPK.key LIKE :like"),
@NamedQuery(name="all_keys_with_type_like",
	query="select item.propertyPK.key from item in class PropertySetItem where item.propertyPK.entityName = :entityName and item.propertyPK.entityId = :entityId and item.type = :type and item.propertyPK.key LIKE :like")
					
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PropertySetItem implements Serializable {

	private static final long serialVersionUID = -5870468287811279433L;

	@EmbeddedId
	private PropertyPK propertyPK;
	
	@Column(name="INT_VAL")
	private int intVal;
	
	@Column(name="KEY_TYPE")
	private int type;
	
	@Column(name="DATE_VAL")
	private Date dateVal;
	
	@Column(name="LONG_VAL")
	private long longVal;
	
	@Column(name="STRING_VAL")
	private String stringVal;
	
	@Column(name="BOOLEAN_VAL")
	private boolean booleanVal;
	
	@Column(name="DOUBLE_VAL")
	private double doubleVal;
	
	// needed for hibernate
	public PropertySetItem() {
	}	
	
	
	
	public PropertyPK getPropertyPK() {
		return propertyPK;
	}



	public void setPropertyPK(PropertyPK propertyPK) {
		this.propertyPK = propertyPK;
	}



	public void setBooleanVal(boolean booleanVal) {
		this.booleanVal = booleanVal;
	}

	public boolean getBooleanVal() {
		return booleanVal;
	}

	public void setDateVal(Date dateVal) {
		this.dateVal = dateVal;
	}

	public Date getDateVal() {
		return dateVal;
	}

	public void setDoubleVal(double doubleVal) {
		this.doubleVal = doubleVal;
	}

	public double getDoubleVal() {
		return doubleVal;
	}

	

	public void setIntVal(int intVal) {
		this.intVal = intVal;
	}

	public int getIntVal() {
		return intVal;
	}

	

	public void setLongVal(long longVal) {
		this.longVal = longVal;
	}

	public long getLongVal() {
		return longVal;
	}

	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	public String getStringVal() {
		return stringVal;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	
}
