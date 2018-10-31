package com.hnjk.edu.recruit.service.impl.stat;

/**
 * gross data to support stat of signup check,entrance check,notest check and
 * stat of enrollee
 * 
 * @author
 * @since
 */
public class Statistic {
	private String branch;
	private String classic;
	private String major;
	private int signup;
	private int entrance;
	private int notest;
	private int total;

	/**
	 * @return
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * @return
	 */
	public String getClassic() {
		return classic;
	}

	/**
	 * @return
	 */
	public int getEntrance() {
		return entrance;
	}

	/**
	 * @return
	 */
	public String getMajor() {
		return major;
	}

	/**
	 * @return
	 */
	public int getNotest() {
		return notest;
	}

	/**
	 * @return
	 */
	public int getSignup() {
		return signup;
	}

	/**
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param string
	 */
	public void setBranch(String string) {
		branch = string;
	}

	/**
	 * @param string
	 */
	public void setClassic(String string) {
		classic = string;
	}

	/**
	 * @param i
	 */
	public void setEntrance(int i) {
		entrance = i;
	}

	/**
	 * @param string
	 */
	public void setMajor(String string) {
		major = string;
	}

	/**
	 * @param i
	 */
	public void setNotest(int i) {
		notest = i;
	}

	/**
	 * @param i
	 */
	public void setSignup(int i) {
		signup = i;
	}

	/**
	 * @param i
	 */
	public void setTotal(int i) {
		total = i;
	}

}
