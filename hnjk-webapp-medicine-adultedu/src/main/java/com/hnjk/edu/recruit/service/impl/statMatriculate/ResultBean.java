/*
返回的表现层bean
 */
package com.hnjk.edu.recruit.service.impl.statMatriculate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 
 */
public class ResultBean {
	
    private String schoolName;
    private String classic;
    private List counts;
    
    public ResultBean(){
        schoolName = null;
        classic = null;
        counts = new ArrayList();
    }
    
    /**
     * @return Returns the classic.
     */
    public String getClassic() {
        return classic;
    }
    /**
     * @param classic
     * The classic to set.
     */
    public void setClassic(String classic) {
        this.classic = classic;
    }
    /**
     * @return Returns the counts.
     */
    public List getCounts() {
        return counts;
    }
    /**
     * @param counts
     *            The counts to set.
     */
    public void setCounts(List counts) {
        this.counts = counts;
    }
    /**
     * @return Returns the schoolName.
     */
    public String getSchoolName() {
        return schoolName;
    }
    /**
     * @param schoolName The schoolName to set.
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}