package com.hnjk.core.support.base.model;

public interface IBaseModel {
	
	public String getResourceid();

	public void setResourceid(String resourceid) ;
	
	public Long getVersion();

	public void setVersion(Long version);

	public Integer getIsDeleted();
	
	public void setIsDeleted(Integer isDeleted) ;
}
