package com.hnjk.edu.roll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hnjk.core.support.base.model.BaseModel;
/**
 * 用于学分计算的学籍ID表
 * <code>StudentCheck</code>
 * <p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2012-6-7 下午04:03:57
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEMP_STUDENTCHECK")
public class StudentCheck  extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="STUDENTID")
	private String studentId;
	
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	
}
