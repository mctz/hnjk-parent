package com.hnjk.edu.roll.vo;

public class EdumanagerVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6141741325269658022L;
	
	private String type;  //类型
	private String num;
	private String c_q;   //合计全部
	private String c_b;	  //合计博士
	private String c_s;	  //合计硕士
	private String b_b;	  //博士学历获得 博士学位
	private String b_s;	  //博士学历获得 硕士学位
	private String b_q;	  //博士学历合计
	private String s_b;   //硕士学历获得 博士学位
	private String s_s;   //硕士学历获得 硕士学位
	private String s_q;   //硕士学历合计
	private String bk_b;  //本科学历获得 博士学位
	private String bk_s;  //本科学历获得 硕士学位
	private String bk_q;  //本科学历合计
	private String q_b;   //专科及一下获得 博士学位
	private String q_s;   //专科及一下获得 硕士学位
	private String q_q;   //专科及一下合计
	//导入时用的
	private String flag;
	private String message;
	
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getC_q() {
		return c_q;
	}
	public void setC_q(String c_q) {
		this.c_q = c_q;
	}
	public String getC_b() {
		return c_b;
	}
	public void setC_b(String c_b) {
		this.c_b = c_b;
	}
	public String getC_s() {
		return c_s;
	}
	public void setC_s(String c_s) {
		this.c_s = c_s;
	}
	public String getB_b() {
		return b_b;
	}
	public void setB_b(String b_b) {
		this.b_b = b_b;
	}
	public String getB_s() {
		return b_s;
	}
	public void setB_s(String b_s) {
		this.b_s = b_s;
	}
	public String getB_q() {
		return b_q;
	}
	public void setB_q(String b_q) {
		this.b_q = b_q;
	}
	public String getS_b() {
		return s_b;
	}
	public void setS_b(String s_b) {
		this.s_b = s_b;
	}
	public String getS_s() {
		return s_s;
	}
	public void setS_s(String s_s) {
		this.s_s = s_s;
	}
	public String getS_q() {
		return s_q;
	}
	public void setS_q(String s_q) {
		this.s_q = s_q;
	}
	public String getBk_b() {
		return bk_b;
	}
	public void setBk_b(String bk_b) {
		this.bk_b = bk_b;
	}
	public String getBk_s() {
		return bk_s;
	}
	public void setBk_s(String bk_s) {
		this.bk_s = bk_s;
	}
	public String getBk_q() {
		return bk_q;
	}
	public void setBk_q(String bk_q) {
		this.bk_q = bk_q;
	}
	public String getQ_b() {
		return q_b;
	}
	public void setQ_b(String q_b) {
		this.q_b = q_b;
	}
	public String getQ_s() {
		return q_s;
	}
	public void setQ_s(String q_s) {
		this.q_s = q_s;
	}
	public String getQ_q() {
		return q_q;
	}
	public void setQ_q(String q_q) {
		this.q_q = q_q;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
