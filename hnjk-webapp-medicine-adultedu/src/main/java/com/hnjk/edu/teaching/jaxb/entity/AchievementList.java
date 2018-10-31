package com.hnjk.edu.teaching.jaxb.entity;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 要导出的学生成绩列表
 * @author luof 
 */
@XmlRootElement(name="achievement")
public class AchievementList
{
   
    private OperateInfo operateinfo ;

	
    private Collection<CourseAchievementList> lists ;

    public AchievementList()
    {
    	lists = new ArrayList<CourseAchievementList>();
    }
  
    public AchievementList(OperateInfo operateinfo,
            Collection<CourseAchievementList> lists)
    {
        this.operateinfo = operateinfo;
        this.lists = lists;
    }
    
    @XmlElement(name="operateinfo")
    public OperateInfo getOperateinfo()
    {
        return operateinfo;
    }

    public void setOperateinfo(OperateInfo operateinfo)
    {
        this.operateinfo = operateinfo;
    }
    

	@XmlElement(name = "list")
    public Collection<CourseAchievementList> getLists()
    {
        return lists;
    }

    public void setLists(Collection<CourseAchievementList> lists)
    {
        this.lists = lists;
    }
}
