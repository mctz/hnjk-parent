package com.hnjk.edu.teaching.vo;

/**
 * 毕业论文预约统计VO。
 * <code>GraduatePapersOrderCountVo</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-9-16 下午03:47:50
 * @see 
 * @version 1.0
 */
public class GraduatePapersOrderCountVo implements java.io.Serializable{

	private static final long serialVersionUID = 7448529505271775773L;
	
	private String batchname;//批次名称
	private String unitname;//学习中心
	private String branchschoolid;//学习中心ID
	private String majorname;//专业名称
	private String majorid;//专业ID
	private String classicname;//层次名称
	private String classicid;//层次ID
	private Integer ordernum;//统计人数
	private String batchid;//批次ID
	
	
	
	/**
	 * @return the majorid
	 */
	public String getMajorid() {
		return majorid;
	}
	/**
	 * @param majorid the majorid to set
	 */
	public void setMajorid(String majorid) {
		this.majorid = majorid;
	}
	/**
	 * @return the classicid
	 */
	public String getClassicid() {
		return classicid;
	}
	/**
	 * @param classicid the classicid to set
	 */
	public void setClassicid(String classicid) {
		this.classicid = classicid;
	}
	/**
	 * @return the batchid
	 */
	public String getBatchid() {
		return batchid;
	}
	/**
	 * @param batchid the batchid to set
	 */
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	/**
	 * @return the branchschoolid
	 */
	public String getBranchschoolid() {
		return branchschoolid;
	}
	/**
	 * @param branchschoolid the branchschoolid to set
	 */
	public void setBranchschoolid(String branchschoolid) {
		this.branchschoolid = branchschoolid;
	}
	
	
	/**
	 * @return the batchname
	 */
	public String getBatchname() {
		return batchname;
	}
	/**
	 * @param batchname the batchname to set
	 */
	public void setBatchname(String batchname) {
		this.batchname = batchname;
	}
	/**
	 * @return the unitname
	 */
	public String getUnitname() {
		return unitname;
	}
	/**
	 * @param unitname the unitname to set
	 */
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	/**
	 * @return the majorname
	 */
	public String getMajorname() {
		return majorname;
	}
	/**
	 * @param majorname the majorname to set
	 */
	public void setMajorname(String majorname) {
		this.majorname = majorname;
	}
	/**
	 * @return the classicname
	 */
	public String getClassicname() {
		return classicname;
	}
	/**
	 * @param classicname the classicname to set
	 */
	public void setClassicname(String classicname) {
		this.classicname = classicname;
	}
	/**
	 * @return the ordernum
	 */
	public Integer getOrdernum() {
		return ordernum;
	}
	/**
	 * @param ordernum the ordernum to set
	 */
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	
	
	
}
