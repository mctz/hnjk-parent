package com.hnjk.edu.teaching.vo;

import java.text.ParseException;
import java.util.Date;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 
 * <code>预约统计导出Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-11-2 下午03:42:03
 * @see 
 * @version 1.0
 */
public class ExamOrderStatExportVo {
	
	private String COURSEID;         //课程ID
	private String BRSCHOOLID;        //学习中心ID
	private String UNITNAME;          //学习中心名称
	private String EXAMCOURSECODE;    //考试编号
	private String COURSECODE;        //课程编号 
	private String COURSENAME;        //课程名
	private Integer COUNTS;           //预约人数
	private String EXAMTYPE;          //考试形式
	private Date STARTTIME;          //考试开始时间
	private Date ENDTIME;            //考试结束时间
	private String MEMO = "";             //备注
	private String memo2 = "";
	
	private String examTime;// 考试时间 yyyy-MM-dd HH24:mm - HH24:mm
	private String examCourseCodeFlag;
	
	
	
	public String getCOURSECODE() {
		return COURSECODE;
	}
	public void setCOURSECODE(String cOURSECODE) {
		COURSECODE = cOURSECODE;
	}
	public String getMemo2() {
		return memo2;
	}
	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}
	public String getExamCourseCodeFlag() {
		if (ExStringUtils.isNotEmpty(this.EXAMCOURSECODE)&&this.EXAMCOURSECODE.length()>1) {
			return this.EXAMCOURSECODE.substring(0,1);
		}else {
			return "";
		}
	}
	public void setExamCourseCodeFlag(String examCourseCodeFlag) {
		this.examCourseCodeFlag = examCourseCodeFlag;
	}
	public String getExamTime() {
		if (null!=STARTTIME && null!=STARTTIME) {		
			try {
				return ExDateUtils.formatDateStr(this.STARTTIME,"yyyy-MM-dd HH:mm")+"-"+ExDateUtils.formatDateStr(this.ENDTIME,"HH:mm");
			} catch (ParseException e) {
				return "";
			}
		}else {
			return "";
		}
	}
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
	public String getCOURSEID() {
		return COURSEID;
	}
	public void setCOURSEID(String cOURSEID) {
		COURSEID = cOURSEID;
	}
	public String getBRSCHOOLID() {
		return BRSCHOOLID;
	}
	public void setBRSCHOOLID(String bRSCHOOLID) {
		BRSCHOOLID = bRSCHOOLID;
	}
	public String getUNITNAME() {
		return UNITNAME;
	}
	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}
	public String getEXAMCOURSECODE() {
		return EXAMCOURSECODE;
	}
	public void setEXAMCOURSECODE(String eXAMCOURSECODE) {
		EXAMCOURSECODE = eXAMCOURSECODE;
	}
	public String getCOURSENAME() {
		return COURSENAME;
	}
	public void setCOURSENAME(String cOURSENAME) {
		COURSENAME = cOURSENAME;
	}
	
	public Integer getCOUNTS() {
		return COUNTS;
	}
	public void setCOUNTS(Integer cOUNTS) {
		COUNTS = cOUNTS;
	}
	public String getEXAMTYPE() {
		return EXAMTYPE;
	}
	public void setEXAMTYPE(String eXAMTYPE) {
		EXAMTYPE = eXAMTYPE;
	}

	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	public Date getSTARTTIME() {
		return STARTTIME;
	}
	public void setSTARTTIME(Date sTARTTIME) {
		STARTTIME = sTARTTIME;
	}
	public Date getENDTIME() {
		return ENDTIME;
	}
	public void setENDTIME(Date eNDTIME) {
		ENDTIME = eNDTIME;
	}
	
	
	
}
