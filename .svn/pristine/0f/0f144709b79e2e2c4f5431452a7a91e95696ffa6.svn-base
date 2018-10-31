package com.hnjk.edu.teaching.jaxb.entity;

import java.text.ParseException;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import com.hnjk.core.foundation.utils.ExDateUtils;

/**
 * 导出成绩操作者
 * @author luof 
 */
public class OperateInfo
{

    private String user ;
    private String date ;
    private String batchguid ;
    private String batchname ;
    private String isgraduate ;

    public OperateInfo()
    {

    }

    public OperateInfo(String user, String date, String batchguid, String batchname,String isgraduate)
    {
        this.user = user;
        this.date = date;
        this.batchguid = batchguid;
        this.batchname = batchname;
        this.isgraduate =isgraduate;
    }

    public OperateInfo(String user, String batchguid, String batchname,String isgraduate) throws ParseException
    {
    	this(user, ExDateUtils.formatDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"), batchguid, batchname,isgraduate);
    }
    
    @XmlAttribute
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
    
    @XmlAttribute
    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
    
    @XmlAttribute
    public String getBatchname()
    {
        return batchname;
    }

    public void setBatchname(String batchname)
    {
        this.batchname = batchname;
    }
    
    @XmlAttribute
    public String getBatchguid()
    {
        return batchguid;
    }

    public void setBatchguid(String batchguid)
    {
        this.batchguid = batchguid;
    }
    
    @XmlAttribute
	public String getIsgraduate()
	{
		return isgraduate;
	}

	public void setIsgraduate(String isgraduate)
	{
		this.isgraduate = isgraduate;
	}

    
}
