/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi;


/**
 * Interface for a workflow entry.
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 * 实现Serializable接口
 */
public interface WorkflowEntry extends java.io.Serializable{

    public static final int CREATED = 0;	//创建，流程被创建
    public static final int ACTIVATED = 1;	//激活，流程创建后，一旦调用便成为激活状态
    public static final int SUSPENDED = 2;//挂起，流程被挂起后，将被冻结
    public static final int KILLED = 3;//终止
    public static final int COMPLETED = 4;//完成
    public static final int UNKNOWN = -1;//未知

    /**
     * Returns the unique ID of the workflow entry.
     */
    public long getId();

    /**
     * Returns true if the workflow entry has been initialized.
     */
    public boolean isInitialized();

    public int getState();

    /**
     * Returns the name of the workflow that this entry is an instance of.
     */
    public String getWorkflowName();
   
        
    //获取用户ID
    public String getUserId();
    
    //获取组织id
    public String getUnitId();
}
