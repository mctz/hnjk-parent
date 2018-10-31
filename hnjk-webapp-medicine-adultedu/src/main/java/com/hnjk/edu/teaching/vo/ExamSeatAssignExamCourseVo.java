package com.hnjk.edu.teaching.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <code>考试课程信息Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-10-27 下午2:45:03
 * @see 
 * @version 1.0
 */
public class ExamSeatAssignExamCourseVo {

	private String RESOURCEID;
	private String COURSENAME;
	private String EXAMTYPE;
	private String EXAMCOURSECODE;
	private Date EXAMSTARTTIME;
	private Date EXAMENDTIME;
	private Long ASSIGEND =0L;
	private Long TOTALNUM =0L;
	
	public String getRESOURCEID() {
		return RESOURCEID;
	}
	public void setRESOURCEID(String rESOURCEID) {
		RESOURCEID = rESOURCEID;
	}
	public String getCOURSENAME() {
		return COURSENAME;
	}
	public void setCOURSENAME(String cOURSENAME) {
		COURSENAME = cOURSENAME;
	}
	public String getEXAMTYPE() {
		return EXAMTYPE;
	}
	public void setEXAMTYPE(String eXAMTYPE) {
		EXAMTYPE = eXAMTYPE;
	}
	public String getEXAMCOURSECODE() {
		return EXAMCOURSECODE;
	}
	public void setEXAMCOURSECODE(String eXAMCOURSECODE) {
		EXAMCOURSECODE = eXAMCOURSECODE;
	}
	

	public Date getEXAMSTARTTIME() {
		return EXAMSTARTTIME;
	}
	public void setEXAMSTARTTIME(Date eXAMSTARTTIME) {
		EXAMSTARTTIME = eXAMSTARTTIME;
	}
	public Date getEXAMENDTIME() {
		return EXAMENDTIME;
	}
	public void setEXAMENDTIME(Date eXAMENDTIME) {
		EXAMENDTIME = eXAMENDTIME;
	}
	public Long getASSIGEND() {
		return ASSIGEND;
	}
	public void setASSIGEND(Long aSSIGEND) {
		ASSIGEND = aSSIGEND;
	}
	public Long getTOTALNUM() {
		return TOTALNUM;
	}
	public void setTOTALNUM(Long tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}


	
}
