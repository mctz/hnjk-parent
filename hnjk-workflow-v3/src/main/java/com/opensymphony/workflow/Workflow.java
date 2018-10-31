/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateHistoryStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateStep;


/**
 * The core workflow interface.
 *
 * @author <a href="mailto:plightbo@hotmail.com">Patrick Lightbody</a>
 */
public interface Workflow {
	
    String BSF_COL = "col";
    String BSF_LANGUAGE = "language";
    String BSF_ROW = "row";
    String BSF_SCRIPT = "script";
    String BSF_SOURCE = "source";
    String BSH_SCRIPT = "script";

    // statics
    String CLASS_NAME = "class.name";
    String EJB_LOCATION = "ejb.location";
    String JNDI_LOCATION = "jndi.location";
    
    /**
     * 以下四个常量
     * 做用户|组织|角色扩展
     * 在HibernateStep中 Ower 扩展成为支持 用户 组织 角色，在ID前面增加如下类型的一种
     * 注意如下：
     * 1.每次获取代办，要进行匹配用户 组织 角色这三种条件
     * 2.当代办被处理的时候，在执行doAction前,如果Ower是组织/角色，要修改为当前操作用户 即用户类型
     *   因为当前步骤会被拷贝到历史步骤，要让记录出现在用户的已办信息中,不能再是组织/角色类型
     */ 
    String TYPE_USER = "USER_";
    String TYPE_ROLE = "ROLE_";
    String TYPE_DEPT = "DEPT_";

    //~ Methods ////////////////////////////////////////////////////////////////
    
    /**
     * Set the configuration for this workflow.
     * If not set, then the workflow will use the default configuration static instance.
     * @param configuration a workflow configuration
     */
    public void setConfiguration(Configuration configuration);
    
    /**
     * Modify the state of the specified workflow instance.
     * @param id The workflow instance id.
     * @param newState the new state to change the workflow instance to.
     * If the new state is {@link com.opensymphony.workflow.spi.WorkflowEntry.KILLED}
     * or {@link com.opensymphony.workflow.spi.WorkflowEntry.COMPLETED}
     * then all current steps are moved to history steps. If the new state is
     */
    public void changeEntryState(long id, int newState) throws WorkflowException;
    
    /**
     * Perform an action on the specified workflow instance.
     * @param id The workflow instance id.
     * @param actionId The action id to perform (action id's are listed in the workflow descriptor).
     * @param inputs The inputs to the workflow instance.
     * @throws InvalidInputException if a validator is specified and an input is invalid.
     * @throws InvalidActionException if the action is invalid for the specified workflow
     * instance's current state.
     */
    public void doAction(long id, int actionId, Map inputs) throws InvalidInputException, WorkflowException;
    
    /**
     * 取回操作
     * @param entryId
     * @throws WorkflowException
     */
    public void doGetBack(long entryId) throws WorkflowException;
    
    /**
     * Executes a special trigger-function using the context of the given workflow instance id.
     * Note that this method is exposed for Quartz trigger jobs, user code should never call it.
     * @param id The workflow instance id
     * @param triggerId The id of the speciail trigger-function
     */
    public void executeTriggerFunction(long id, int triggerId) throws WorkflowException;

    /**
     * Return the state of the specified workflow instance id.
     * @param id The workflow instance id.
     * @return int The state id of the specified workflow
     */
    public int getEntryState(long id);
    
    /**
    * Initializes a workflow so that it can begin processing. A workflow must be initialized before it can
    * begin any sort of activity. It can only be initialized once.
    *
    * @param workflowName The workflow name to create and initialize an instance for
    * @param initialAction The initial step to start the workflow
    * @param inputs The inputs entered by the end-user
    * @throws InvalidRoleException if the user can't start this function
    * @throws InvalidInputException if a validator is specified and an input is invalid.
    * @throws InvalidActionException if the specified initial action is invalid for the specified workflow.
    */
    public long initialize(String workflowName, String userId,String unitId,int initialAction, Map inputs) throws InvalidRoleException, InvalidInputException, WorkflowException, InvalidEntryStateException, InvalidActionException;
    
    /**
     * Check if the calling user has enough permissions to initialise the specified workflow.
     * @param workflowName The name of the workflow to check.
     * @param initialStep The id of the initial state to check.
     * @return true if the user can successfully call initialize, false otherwise.
     */
    public boolean canInitialize(String workflowName, int initialStep);

    /**
    * Check if the state of the specified workflow instance can be changed to the new specified one.
    * @param id The workflow instance id.
    * @param newState The new state id.
    * @return true if the state of the workflow can be modified, false otherwise.
    */
    public boolean canModifyEntryState(long id, int newState);
    
    /**
     * Determine if a particular workflow can be initialized.
     * @param workflowName The workflow name to check.
     * @param initialAction The potential initial action.
     * @param inputs The inputs to check.
     * @return true if the workflow can be initialized, false otherwise.
     */
    public boolean canInitialize(String workflowName, int initialAction, Map inputs);

    /**
     * Remove the specified workflow descriptor.
     * @param workflowName The workflow name of the workflow to remove.
     * @return true if the workflow was removed, false otherwise.
     * @throws FactoryException If the underlying workflow factory has an error removing the workflow,
     * or if it does not support the removal of workflows.
     */
    public boolean removeWorkflowDescriptor(String workflowName) throws FactoryException;

    /**
     * Add a new workflow descriptor
     * @param workflowName The workflow name of the workflow to add
     * @param descriptor The workflow descriptor to add
     * @param replace true, if an existing descriptor should be overwritten
     * @return true if the workflow was added, fales otherwise
     * @throws FactoryException If the underlying workflow factory has an error adding the workflow,
     * or if it does not support adding workflows.
     */
    public boolean saveWorkflowDescriptor(String workflowName, WorkflowDescriptor descriptor, boolean replace) throws FactoryException;
    
    /**
     * Get the name of the specified workflow instance.
     * @param id the workflow instance id.
     */
    public String getWorkflowName(long id);
    
    /**
     * Get the PropertySet for the specified workflow instance id.
     * @param id The workflow instance id.
     */
    public PropertySet getPropertySet(long id);
    
    /**
     * 返回配置信息 可以拿到store类
     * @return
     */
    public Configuration getConfiguration();
    
    /**
     * Get the workflow descriptor for the specified workflow name.
     * @param workflowName The workflow name.
     */
    public WorkflowDescriptor getWorkflowDescriptor(String workflowName);
    
    /**
     * use {@link #getAvailableActions(long, Map)}  with an empty Map instead.
     */
    public int[] getAvailableActions(long id);
    
    /**
     * Get the available actions for the specified workflow instance.
     * @ejb.interface-method
     * @param id The workflow instance id.
     * @param inputs The inputs map to pass on to conditions
     * @return An array of action id's that can be performed on the specified entry
     * @throws IllegalArgumentException if the specified id does not exist, or if its workflow
     * descriptor is no longer available or has become invalid.
     */
    public int[] getAvailableActions(long id, Map inputs);
    
    /**
     * Get all available workflow names.
     */
    public String[] getWorkflowNames();

    /**
     * Returns a Collection of Step objects that are the current steps of the specified workflow instance.
     *
     * @param id The workflow instance id.
     * @return The steps that the workflow instance is currently in.
     */
    public List<HibernateCurrentStep> getCurrentSteps(long id);


    /**
     * Returns a list of all steps that are completed for the given workflow instance id.
     *
     * @param id The workflow instance id.
     * @return a List of Steps
     * @see com.opensymphony.workflow.spi.Step
     */
    public List<HibernateHistoryStep> getHistorySteps(long id);

    /**
     * Get a collection (Strings) of currently defined permissions for the specified workflow instance.
     * @param id the workflow instance id.
     * @return A List of permissions specified currently (a permission is a string name).
     * @deprecated use {@link #getSecurityPermissions(long, java.util.Map)} with a null map instead.
     */
    public List<String> getSecurityPermissions(long id);

    /**
     * Get a collection (Strings) of currently defined permissions for the specified workflow instance.
     * @param id id the workflow instance id.
     * @param inputs inputs The inputs to the workflow instance.
     * @return A List of permissions specified currently (a permission is a string name).
     */
    public List<String> getSecurityPermissions(long id, Map inputs);

 
    /**
     * 更新一个step
     * 在doAction前 有一个更换owner操作
     */
    public void updateState(HibernateStep step) throws WorkflowException;
    /**
     * 删除一个流程实例以及相关数据
     * @param wfId
     * @throws WorkflowException
     */
    public void deleteEntry(long wfId) throws WorkflowException;
    
    /**
     * 获取当前用户待办事项
     * @param objPage
     * @param userId
     * @param paramsMap
     * @return
     * @throws WorkflowException
     */
    public  Page queryCurrentWorksByPage(Page objPage,String userId,Map<String, Object> paramsMap)   throws WorkflowException;
   
    /**
     * 获取当前用户已办事项
     * @param objPage
     * @param userId
     * @param paramsMap
     * @return
     * @throws WorkflowException
     */
    public abstract Page queryHistoryWorksByPage(Page objPage,String userId,Map<String, Object> paramsMap) throws WorkflowException;
    
    /**
     * 根据ID获取流程实例
     * @param id
     * @return
     * @throws WorkflowException
     */
    public WorkflowEntry getEntry(long id) throws WorkflowException;
    
    /**
     * 获取某个流程的某个步骤/节点的meta信息
     * @param workflowname
     * @param wf_id
     * @param type step/action
     * @return
     * @throws WorkflowException
     */
    public Map<String, Object> getWorkflowDescriptMeta(String workflowname,long wf_id,String type) throws WorkflowException;
}
