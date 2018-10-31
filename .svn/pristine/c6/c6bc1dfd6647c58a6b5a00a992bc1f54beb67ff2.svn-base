package com.hnjk.edu.teaching.jaxb.entity;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
/**
 * 课程的成绩列表
 * @author luof
 */

public class CourseAchievementList
{	 
	
    private AchievementCourseTip coursetip ;

    private Collection<AchievementItem> items ;

    public CourseAchievementList()
    {
    	items = new ArrayList<AchievementItem>();
    }

    public CourseAchievementList(AchievementCourseTip coursetip,
            Collection<AchievementItem> items)
    {
        this.coursetip = coursetip;
        this.items = items;
    }
    
    @XmlElement(name="coursetip")
    public AchievementCourseTip getCoursetip()
    {
        return coursetip;
    }

    public void setCoursetip(AchievementCourseTip coursetip)
    {
        this.coursetip = coursetip;
    }
    
    @XmlElement(name="item")
    public Collection<AchievementItem> getItems()
    {
        return items;
    }

    public void setItems(Collection<AchievementItem> items)
    {
        this.items = items;
    }

}
