package com.hnjk.edu.recruit.service.impl.statMatriculate;
/**
 * 统计辅助bean
 * 
 */

public class ReportStateKey
{
    private int hashValue = -1;
    
    private String branchschoolguid = null;

    private String majorguid = null;

    private String classicguid = null;
    
    public ReportStateKey(String branchschoolguid, String majorguid,
            String classicguid)
    {
        this.branchschoolguid = branchschoolguid;
        this.majorguid = majorguid;
        this.classicguid = classicguid;
    }

    public String getBranchschoolguid()
    {
        return branchschoolguid;
    }

    public String getClassicguid()
    {
        return classicguid;
    }

    public String getMajorguid()
    {
        return majorguid;
    }
    
    @Override
	public int hashCode()
    {
        if (hashValue == -1)
        {
            hashValue = this.branchschoolguid.hashCode();
            hashValue = hashValue * 37 + this.majorguid.hashCode();
            hashValue = hashValue * 37 + this.classicguid.hashCode();
        }
        return hashValue;
    }

    @Override
	public boolean equals(Object obj)
    {
        if (obj instanceof ReportStateKey)
        {
            ReportStateKey key = (ReportStateKey) obj;
            return (key.branchschoolguid.equals(this.branchschoolguid)
                    && key.majorguid.equals(this.majorguid) && key.classicguid
                    .equals(this.classicguid));
        }
        else
        {
            return false;
        }
    }
}
