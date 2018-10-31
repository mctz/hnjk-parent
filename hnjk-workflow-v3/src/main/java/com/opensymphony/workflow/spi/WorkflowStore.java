/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateHistoryStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateStep;


/**
 * Interface for pluggable workflow stores configured in osworkflow.xml.
 * Only one instance of a workflow store is ever created, meaning that
 * if your persistence connections (such as java.sql.Connection) time out,
 * it would be un-wise to use just one Connection for the entire object.
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 */
public interface WorkflowStore {
	
	/**
     * Called once when the store is first created.
     * @param props properties set in osworkflow.xml
     */
    public void init(Map props) throws StoreException;
    
    /**
     * Returns a PropertySet that is associated with this workflow instance ID.
     * @param entryId The workflow instance id.
     * @return a property set unique to this entry ID
     */
    public PropertySet getPropertySet(long entryId) throws StoreException;
    
    
    /**
     * Persists a new workflow entry that has <b>not been initialized</b>.
     * @param workflowName the workflow name that this entry is an instance of
     * @return a representation of the workflow instance persisted
     */
   // public WorkflowEntry createEntry(String workflowName) throws StoreException;
    
    /**
     * 保存流程实例
     * @param entry
     * @return
     * @throws StoreException
     */
    public WorkflowEntry createEntry(WorkflowEntry entry) throws StoreException;
    
    /**
     * 根据条件查找流程实例列表
     * @param pageObj
     * @param params
     * @return
     * @throws StoreException
     */
    public Page findEntryByCondition(Page pageObj,Map<String, Object> params) throws StoreException;
    
    /**
     * Pulls up the workflow entry data for the entry ID given.
     *
     * @param entryId The workflow instance id.
     * @return a representation of the workflow instance persisted
     */
    public WorkflowEntry findEntry(long entryId) throws StoreException;
    
    /**
     * Set the state of the workflow instance.
     * @param entryId The workflow instance id.
     * @param state The state to move the workflow instance to.
     */
    public void setEntryState(long entryId, int state) throws StoreException;

    /**
     * Persists a step with the given parameters.
     *
     * @param entryId The workflow instance id.
     * @param stepId the ID of the workflow step associated with this new
     *               Step (not to be confused with the step primary key)
     * @param owner the owner of the step
     * @param startDate the start date of the step
     * @param status the status of the step
     * @param previousIds the previous step IDs
     * @return a representation of the workflow step persisted
     */
    public Step createCurrentStep(long entryId, int stepId, String owner, Date startDate, Date dueDate, String status, long[] previousIds) throws StoreException;

    /**
     * Returns a list of all current steps for the given workflow instance ID.
     *
     * @param entryId The workflow instance
     * @return a List of Steps
     * @see com.opensymphony.workflow.spi.Step
     */
    public List<HibernateCurrentStep> findCurrentSteps(long entryId) throws StoreException;

    /**
     * Returns a list of all steps that are finished for the given workflow instance ID.
     *
     * @param entryId The workflow instance
     * @return a List of Steps
     * @see com.opensymphony.workflow.spi.Step
     */
	public List<HibernateHistoryStep> findHistorySteps(long entryId) throws StoreException;

    /**
     * Mark the specified step as finished.
     * @param step the step to finish.
     * @param actionId The action that caused the step to finish.
     * @param finishDate the date when the step was finished.
     * @param status The status to set the finished step to.
     * @param caller The caller that caused the step to finish.
     * @return the finished step
     */
    public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller) throws StoreException;

    /**
     * Called when a step is finished and can be moved to workflow history.
     * @param step the step to be moved to workflow history
     */
    public void moveToHistory(Step step) throws StoreException;

        
    /**
     * update step entry
     */
    public void updateStep(HibernateStep step) throws StoreException;
    
    /**
     * to get back previous step status
     * @param entryId 
     * @throws StoreException
     */
    public void doGetBack(long entryId) throws StoreException;
    
    /**
     * delete entry instance 
     * @param entryId
     * @throws StoreException
     */
    public void deleteEntry(long entryId) throws StoreException;
    
   /**
    * 根据条件查找待办
    * @param paramsMap
    * @return
    * @throws StoreException
    */
    public abstract Page queryCurrentWorksByPage(Page objPage,String userId,Map<String, Object> paramsMap)   throws StoreException;

   /**
    * 根据条件查找待办
    * @param paramsMap
    * @return
    * @throws StoreException
    */
    public abstract Page queryHistoryWorksByPage(Page objPage,String userId,Map<String, Object> paramsMap)   throws StoreException;
    
}