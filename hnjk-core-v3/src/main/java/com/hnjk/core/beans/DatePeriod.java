package com.hnjk.core.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatePeriod{

	private Date startDate;
	private Date endDate;
	
	public DatePeriod() {}
	
	public DatePeriod(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		if(startDate!=null){
			this.startDate = startDate;
		}
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		if(endDate!=null){
			this.endDate = endDate;
		}
	}
	public boolean hasIntersection(Date startDate1,Date endDate1) {
		if(endDate1!=null && startDate!=null && endDate1.after(startDate) || startDate1!=null && endDate!=null && startDate1.before(endDate)){
			return true;
		}
		return false;
	}
}
