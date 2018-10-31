package com.hnjk.edu.recruit.service.impl.stat;

import java.util.HashMap;
import java.util.Map;

/**
 * this class is the viewer of a statistic for the fixup majors it is shared by
 * signup,entrance,notest check statistic.
 */
@SuppressWarnings("unchecked")
public class StatView implements Comparable {

	private Map<String, Integer> majors = new HashMap<String, Integer>();
	private String branch;
	private String classic;
	private int total;

	void set(String major, int cnt) {
		majors.put(major, new Integer(cnt));
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getClassic() {
		return classic;
	}

	public void setClassic(String classic) {
		this.classic = classic;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		// TODO 自动生成方法存根
		StatView s = (StatView) o;
		int i = this.branch.compareTo(s.branch);
		if (i == 0) {
			return this.classic.compareTo(s.classic);
		}
		return i;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof StatView) {
			StatView s = (StatView) o;
			return this.branch.equals(s.branch)
					&& this.classic.equals(s.classic);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.branch.hashCode() ^ this.classic.hashCode();
	}

	public Map<String, Integer> getMajors() {
		return majors;
	}

}
