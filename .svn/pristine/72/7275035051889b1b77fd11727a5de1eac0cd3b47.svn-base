package com.hnjk.edu.recruit.service.impl.statMatriculate;

/**统计辅助bean
 * @author 
 */
public class ReportStateBean
{
    private ReportStateKey key = null;
    
    private int count = 0;

    public ReportStateBean(String branchschoolguid, String majorguid,
            String classicguid, long count)
    {
        this.key = new ReportStateKey(branchschoolguid, majorguid, classicguid);
        this.count = ((Long)count).intValue();
    }

    public int getCount()
    {
        return count;
    }

    public ReportStateKey getKey()
    {
        return key;
    }
    
    
}
