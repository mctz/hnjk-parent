/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi.hibernate3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.hibernate.BaseHibernateTemplate;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.security.model.UserAdaptor;
import com.hnjk.security.service.IUserService;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.QueryNotSupportedException;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.query.FieldExpression;
import com.opensymphony.workflow.query.NestedExpression;
import com.opensymphony.workflow.query.WorkflowQuery;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.util.PropertySetDelegate;


/**
 * @author <a href="mailto:cucuchen520@yahoo.com.cn">Chris Chen</a>
 */
public abstract class AbstractHibernateWorkflowStore extends BaseHibernateTemplate implements  WorkflowStore{
	
    private PropertySetDelegate propertySetDelegate;
	
    private String cacheRegion = null;
    private boolean cacheable = false;
    private final String HQL_CURRENT_STEP_BY_ENTRYID = "from HibernateCurrentStep pojo where pojo.entry.id=? order by pojo.id";
    private final String HQL_CURRENT_STEP_PREVID_BY_STEPID = "select pojo.stepPrevPK.previousId from HibernateCurrentStepPrev pojo where pojo.stepPrevPK.id=?";
    private final String HQL_HISTORY_STEP_BY_ENTRYID = "from HibernateHistoryStep pojo where pojo.entry.id=? order by pojo.id";
    private final String HQL_HISTORY_STEP_PREVID_BY_STEPID = "select pojo.stepPrevPK.previousId from HibernateHistoryStepPrev pojo where pojo.stepPrevPK.id=?";
    private final String HQL_CURRENT_STEP_PREV_BY_STEPID = "delete from HibernateCurrentStepPrev pojo where pojo.stepPrevPK.id= ?";
    private final String HQL_HISTORY_STEP_PREV_BY_STEPID = "delete from HibernateHistoryStepPrev pojo where pojo.stepPrevPK.id= ?";

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
	public PropertySet getPropertySet(long entryId) throws StoreException {
        if (this.propertySetDelegate == null) {
            throw new StoreException("PropertySetDelegate is not properly configured");
        }
        return this.propertySetDelegate.getPropertySet(entryId);
    }
    

    public void setPropertySetDelegate(PropertySetDelegate propertySetDelegate) {
        this.propertySetDelegate = propertySetDelegate;
    }
        

	@Override
	public WorkflowEntry createEntry(WorkflowEntry entry) throws StoreException {
		this.save(entry);		
		return entry;
	}
	@Override
	public WorkflowEntry findEntry(long entryId) throws StoreException {
        return loadEntry(entryId);
    }
	  

	@Override
	public void setEntryState(final long entryId, final int state) throws StoreException {
        HibernateWorkflowEntry entry = loadEntry(entryId);
        entry.setState(state);
        this.update(entry);
    }
    
	@Override
	public Step createCurrentStep(final long entryId, final int stepId, final String owner, final Date startDate, final Date dueDate, final String status, final long[] previousIds) throws StoreException {
    	final HibernateWorkflowEntry entry = loadEntry(entryId);
        final HibernateCurrentStep step = new HibernateCurrentStep();
                      
        step.setStepId(stepId);
        step.setOwner(owner);
        step.setStartDate(startDate);
        step.setDueDate(dueDate);
        step.setStatus(status);
        step.setPreviousStepIds(previousIds);
        step.setEntry(entry);      
        entry.getCurrentSteps().add(step);      
        this.saveOrUpdate(entry);
        for (int i = 0; i < previousIds.length; i++) {
            HibernateCurrentStepPrev prev = new HibernateCurrentStepPrev();
            //MODIFY : 映射为注解方式复合主键
            //prev.setId(step.getId());
            //prev.setPreviousId(previousIds[i]);
            StepPrevPK stepPrevPK = new StepPrevPK();
            stepPrevPK.setId(step.getId());
            stepPrevPK.setPreviousId(previousIds[i]);
            prev.setStepPrevPK(stepPrevPK);
            this.save(prev);
        }
        return step;
    }

    @Override
	public List<HibernateCurrentStep> findCurrentSteps(final long entryId) throws StoreException {
        List<HibernateCurrentStep> list = getHibernateCurrentStepList(entryId);
        ArrayList<HibernateCurrentStep> currentSteps = new ArrayList<HibernateCurrentStep>();
        if (list != null && !list.isEmpty()) {
            for (HibernateCurrentStep hcs : list) {
                long[] prevIds = getCurrentPreviousIdList(hcs.getId());
                hcs.setPreviousStepIds(prevIds);
                currentSteps.add(hcs);
            }
        }
        return currentSteps;
    }

    @Override
	public List<HibernateHistoryStep> findHistorySteps(final long entryId) throws StoreException {
        List<HibernateHistoryStep> list = getHibernateHistoryStepList(entryId);
        ArrayList<HibernateHistoryStep> currentSteps = new ArrayList<HibernateHistoryStep>();

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                HibernateHistoryStep hhs = (HibernateHistoryStep) list.get(i);
                long []prevIds = getHistoryPreviousIdList(hhs.getId());
                hhs.setPreviousStepIds(prevIds);
                currentSteps.add(hhs);
            }
        }
        return currentSteps;
    }

    @Override
	public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller) throws StoreException {
    	HibernateCurrentStep currentStep = (HibernateCurrentStep) step;
    	currentStep.setActionId(actionId);
        currentStep.setFinishDate(finishDate);
        currentStep.setStatus(status);
        currentStep.setCaller(caller);
        
        this.update(currentStep);
        return currentStep;
    }

    @Override
	public void moveToHistory(final Step step) throws StoreException {
    	HibernateCurrentStep currentStep = (HibernateCurrentStep) step;
        final HibernateHistoryStep historyStep = new HibernateHistoryStep(currentStep);
        // set the same id as current table
        historyStep.setId(currentStep.getId());
        historyStep.setPreviousStepIds(currentStep.getPreviousStepIds());
        this.save(historyStep);
        long[] previousIds = historyStep.getPreviousStepIds();

        if (previousIds != null && previousIds.length > 0) {
            for (int i = 0; i < previousIds.length; i++) {
                HibernateHistoryStepPrev p = new HibernateHistoryStepPrev();
                //MODIFY 改为注解方式复合主键
//                p.setId(historyStep.getId());
//                p.setPreviousId(previousIds[i]);
                StepPrevPK stepPrevPK = new StepPrevPK();
                stepPrevPK.setId(historyStep.getId());
                stepPrevPK.setPreviousId(previousIds[i]);
                p.setStepPrevPK(stepPrevPK);
                this.save(p);
            }
        }
        deleteCurrentStepPrev(historyStep.getId());
        this.delete(currentStep);
    }

  
    
    private Criterion getExpression(final WorkflowQuery query) throws StoreException {
        int operator = query.getOperator();
        switch (operator) {
            case WorkflowQuery.EQUALS:
                return Expression.eq(getFieldName(query.getField()), query.getValue());
            case WorkflowQuery.NOT_EQUALS:
                return Expression.not(Expression.like(getFieldName(query.getField()), query.getValue()));
            case WorkflowQuery.GT:
                return Expression.gt(getFieldName(query.getField()), query.getValue());
            case WorkflowQuery.LT:
                return Expression.lt(getFieldName(query.getField()), query.getValue());
            default:
                return Expression.eq(getFieldName(query.getField()), query.getValue());
        }
    }

    private String getFieldName(int field) {
        switch (field) {
            case FieldExpression.ACTION: // actionId
                return "actionId";
            case FieldExpression.CALLER:
                return "caller";
            case FieldExpression.FINISH_DATE:
                return "finishDate";
            case FieldExpression.OWNER:
                return "owner";
            case FieldExpression.START_DATE:
                return "startDate";
            case FieldExpression.STEP: // stepId
                return "stepId";
            case FieldExpression.STATUS:
                return "status";
            case FieldExpression.STATE:
                return "state";
            case FieldExpression.NAME:
                return "workflowName";
            case FieldExpression.DUE_DATE:
                return "dueDate";
            default:
                return "1";
        }
    }

    @SuppressWarnings("unchecked")
	private Class<?> getQueryClass(com.opensymphony.workflow.query.Expression expr, Collection<Class> classesCache) {
        if (classesCache == null) {
            classesCache = new HashSet<Class>();
        }

        if (expr instanceof FieldExpression) {
            FieldExpression fieldExpression = (FieldExpression) expr;

            switch (fieldExpression.getContext()) {
                case FieldExpression.CURRENT_STEPS:
                    classesCache.add(HibernateCurrentStep.class); break;
                case FieldExpression.HISTORY_STEPS:
                    classesCache.add(HibernateHistoryStep.class); break;
                case FieldExpression.ENTRY:
                    classesCache.add(HibernateWorkflowEntry.class); break;
                default:
                    throw new QueryNotSupportedException("Query for unsupported context " + fieldExpression.getContext());
            }
        } else {
            NestedExpression nestedExpression = (NestedExpression) expr;
            for (int i = 0; i < nestedExpression.getExpressionCount(); i++) {
                com.opensymphony.workflow.query.Expression expression = nestedExpression.getExpression(i);
                if (expression.isNested()) {
                    classesCache.add(getQueryClass(nestedExpression.getExpression(i), classesCache));
                } else {
                    classesCache.add(getQueryClass(expression, classesCache));
                }
            }
        }

        if (classesCache.size() > 1) {
            throw new QueryNotSupportedException("Store does not support nested queries of different types (types found:" + classesCache + ")");
        }

        return (Class) classesCache.iterator().next();
    }

	private Criterion buildExpression(WorkflowQuery query) throws StoreException {
        if (query.getLeft() == null) {
            if (query.getRight() == null) {
                return getExpression(query); // leaf node
            } else {
                throw new StoreException("Invalid WorkflowQuery object.  QueryLeft is null but QueryRight is not.");
            }
        } else {
            if (query.getRight() == null) {
                throw new StoreException("Invalid WorkflowQuery object.  QueryLeft is not null but QueryRight is.");
            }
            int operator = query.getOperator();
            WorkflowQuery left = query.getLeft();
            WorkflowQuery right = query.getRight();
            switch (operator) {
                case WorkflowQuery.AND:
                    return Expression.and(buildExpression(left), buildExpression(right));
                case WorkflowQuery.OR:
                    return Expression.or(buildExpression(left), buildExpression(right));
                case WorkflowQuery.XOR:
                    throw new QueryNotSupportedException("XOR Operator in Queries not supported by " + this.getClass().getName());
                default:
                    throw new QueryNotSupportedException("Operator '" + operator + "' is not supported by " + this.getClass().getName());
            }
        }
    }

    private Criterion buildNested(NestedExpression nestedExpression) {
        Criterion full = null;
        for (int i = 0; i < nestedExpression.getExpressionCount(); i++) {
            Criterion expr;
            com.opensymphony.workflow.query.Expression expression = nestedExpression.getExpression(i);
            if (expression.isNested()) {
                expr = buildNested((NestedExpression) nestedExpression.getExpression(i));
            } else {
                FieldExpression sub = (FieldExpression) nestedExpression.getExpression(i);
                expr = queryComparison(sub);
                if (sub.isNegate()) {
                    expr = Expression.not(expr);
                }
            }
            if (full == null) {
                full = expr;
            } else {
                switch (nestedExpression.getExpressionOperator()) {
                    case NestedExpression.AND:
                        full = Expression.and(full, expr); 	break;
                    case NestedExpression.OR:
                        full = Expression.or(full, expr);
                }
            }
        }

        return full;
    }

    private Criterion queryComparison(FieldExpression expression) {
        int operator = expression.getOperator();
        switch (operator) {
            case FieldExpression.EQUALS:
                return Expression.eq(getFieldName(expression.getField()), expression.getValue());
            case FieldExpression.NOT_EQUALS:
                return Expression.not(Expression.like(getFieldName(expression.getField()), expression.getValue()));
            case FieldExpression.GT:
                return Expression.gt(getFieldName(expression.getField()), expression.getValue());
            case FieldExpression.LT:
                return Expression.lt(getFieldName(expression.getField()), expression.getValue());
            default:
                return Expression.eq(getFieldName(expression.getField()), expression.getValue());
        }
    }
    
    // ~ DAO Methods ////////////////////////////////////////////////////////////////
    @Override
    public void delete(final Object entry) throws ServiceException {
        this.getSession().delete(entry);
        this.getSession().flush();
    }

    @Override
    public Object save(final Object entry) throws ServiceException {
        this.getSession().save(entry);
        this.getSession().flush();
        return entry;
    }
    
    @Override
    public void saveOrUpdate(final Object entry) throws ServiceException {
    	this.getSession().saveOrUpdate(entry);
    	this.getSession().flush();
    }
   
    @Override
    public void update(final Object entry) throws ServiceException {
    	this.getSession().flush();
    	this.getSession().update(entry);
    	this.getSession().flush();
    }
    
    private HibernateWorkflowEntry loadEntry(final long entryId) throws StoreException {
        return (HibernateWorkflowEntry)this.getSession().load(HibernateWorkflowEntry.class, new Long(entryId));
    }

    @SuppressWarnings("unchecked")
	private List<HibernateCurrentStep> getHibernateCurrentStepList(final long entryId) throws StoreException {
        Query query = this.getSession().createQuery(HQL_CURRENT_STEP_BY_ENTRYID);
        query.setLong(0, new Long(entryId));
        return query.list();
    }

    @SuppressWarnings("unchecked")
	private long[] getCurrentPreviousIdList(final long stepId) throws StoreException {
        Query query = this.getSession().createQuery(HQL_CURRENT_STEP_PREVID_BY_STEPID);
        query.setLong(0, new Long(stepId));
        List<Long> list = query.list();
        long[] prevIds = new long[list.size()];
        for (int i=0;i<prevIds.length;i++) {
            prevIds[i] = list.get(i);
        }
        return prevIds;
    }

    @SuppressWarnings("unchecked")
	private List<HibernateHistoryStep> getHibernateHistoryStepList(final long entryId) throws StoreException {
	    Query query = this.getSession().createQuery(HQL_HISTORY_STEP_BY_ENTRYID);
	    query.setLong(0, new Long(entryId));
        return query.list();
    }

    @SuppressWarnings("unchecked")
	private long[] getHistoryPreviousIdList(final long stepId) throws StoreException {
        Query query = this.getSession().createQuery(HQL_HISTORY_STEP_PREVID_BY_STEPID);
        query.setLong(0, new Long(stepId));
        List<Long> list = query.list();
        long[] prevIds = new long[list.size()];
        for (int i=0;i<prevIds.length;i++) {
            prevIds[i] = list.get(i);
        }
        return prevIds;
    }

    private void deleteCurrentStepPrev(final long stepId) throws StoreException {
        Query query = this.getSession().createQuery(HQL_CURRENT_STEP_PREV_BY_STEPID);
        query.setLong(0, new Long(stepId));
        query.executeUpdate();
    }

	private void deleteHistoryStepPrev(final long stepId) throws StoreException {
        Query query = this.getSession().createQuery(HQL_HISTORY_STEP_PREV_BY_STEPID);
        query.setLong(0, new Long(stepId));
        query.executeUpdate();
    }
	
	/*
	 * update 当前step
	 */
	@Override
	public void updateStep(HibernateStep step) throws StoreException{
		this.update(step);
	}
	
	/*
	 * 删除流程实例 以及 相关引用数据
     * @param entryId
     * @throws StoreException
     */
    @Override
	public void deleteEntry(long entryId) throws StoreException{
    	List<HibernateCurrentStep> curs = this.findCurrentSteps(entryId);
		List<HibernateHistoryStep> hiss = this.findHistorySteps(entryId);
		for(HibernateCurrentStep cur : curs) {
			deleteCurrentStepPrev(cur.getId());
		}
		
		for(HibernateHistoryStep his : hiss) {
			deleteHistoryStepPrev(his.getId());
		}
		
		this.delete(findEntry(entryId));
    }
	
	/*
	 * 有一个默认的init action
	 * 取回条件: historystep 至少有一条记录
	 * 			currentstep 至少有一条记录
	 */
	@Override
	public void doGetBack(long wfId) throws StoreException{
		List<HibernateCurrentStep> curs = this.findCurrentSteps(wfId);
		List<HibernateHistoryStep> hiss = this.findHistorySteps(wfId);
		int entryState = this.findEntry(wfId).getState();
		if(WorkflowEntry.ACTIVATED == entryState && hiss.size()>0 && curs.size()>0) {
			
			// 获取当前步骤的主键ID 　这个ID在CurrentStepPrev中主键一致　可获取对应HistoryStep的主键ID
			HibernateCurrentStep curStep = curs.get(0);
			long historyStepKeyId = this.getCurrentPreviousIdList(curStep.getId())[0];// 获取HistoryStep记录id
			// 删除对应的CurrentStep 以及　CurrentStepPrev 记录
			deleteCurrentStepPrev(curStep.getId());
			this.delete(curStep);
			
			// 新建CurrentStep
			HibernateCurrentStep newCurStep = new HibernateCurrentStep();
			// 获取对应的HistoryStep 并复制
			for(HibernateHistoryStep hs : hiss) {
				if(hs.getId() == historyStepKeyId) {
					
					newCurStep.setActionId(hs.getActionId());
					newCurStep.setCaller(hs.getCaller());
					newCurStep.setEntry(hs.getEntry());
					newCurStep.setOwner(hs.getOwner());
					newCurStep.setStartDate(hs.getStartDate());
//					StepDescriptor sd = wf.getWorkflowDescriptor(hs.getEntry().getWorkflowName()).getStep(hs.getStepId())
					newCurStep.setStatus("Underway");
					newCurStep.setStepId(hs.getStepId());
					// 将复制后的CurrentStep保存
					hs.getEntry().getCurrentSteps().add(newCurStep);
					
					// 删除这个HistoryStep
					this.delete(hs);
					
					// 更新Entry
					this.update(hs.getEntry());
					
					// 当历史表中存在这个流程实例的记录 在currentStepPrev要步骤对应的关系 并删除历史表的步骤关系
					if(newCurStep.getEntry().getHistorySteps().size()>0) {
					
						// 获取historyStepKeyId　在HistoryStepPrev中作为主键对应的值
						long historyStepPrevValue = this.getHistoryPreviousIdList(historyStepKeyId)[0];
						deleteHistoryStepPrev(historyStepKeyId);
						
						HibernateCurrentStepPrev hcsp = new HibernateCurrentStepPrev();
						//MODIFY 修改为注解方式复合主键
						
						//hcsp.setId(newCurStep.getId());
						//hcsp.setPreviousId(historyStepPrevValue);
						StepPrevPK stepPrevPK = new StepPrevPK();
						stepPrevPK.setId(newCurStep.getId());
						stepPrevPK.setPreviousId(historyStepPrevValue);
						hcsp.setStepPrevPK(stepPrevPK);
						
						
						this.save(hcsp);
					}
					break;
				}
			}
		}else {
			throw new StoreException("流程状态"+(WorkflowEntry.ACTIVATED == entryState?"":"不")+"可用,历史步骤为"+hiss.size()+",因此当前不可回退!");
		}
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.workflow.spi.WorkflowStore#queryCurrentWorksByPage(java.util.Map)
	 */
	@Override	
	@Transactional(readOnly=true)
	public Page queryCurrentWorksByPage(Page objPage,String userId,Map<String, Object> paramsMap)	throws StoreException {		
		Assert.hasText(userId, "当前用户ID不能为空.");
		
		StringBuffer hql = new StringBuffer();
		Map<String, Object> values = new HashMap<String, Object>(20);
		hql.append(" from "+HibernateCurrentStep.class.getSimpleName()+" c where c.entry.state = :state");
		values.put("state", WorkflowEntry.ACTIVATED);
		
		IUserService userService = (IUserService)SpringContextHolder.getBean("userService");//获取用户服务接口
    	UserAdaptor ui = userService.getUserAdaptor(userId);
    	
    	if(null != ui){
    		hql.append(" and ( c.owner = :userId ");	
    		values.put("userId", Workflow.TYPE_USER.concat(userId));
    		if(null != ui.getRoleIdArr()){//TODO 
    			for(String roleId : ui.getRoleIdArr()) {
    				hql.append(" or c.owner = '"+Workflow.TYPE_ROLE.concat(roleId)+"' ");
    			}
    		}
    		if(null != ui.getDeptIdArr()){
    			for(String unitId : ui.getDeptIdArr()){
    				hql.append(" or c.owner = '"+Workflow.TYPE_DEPT.concat(unitId)+"' ");
    			}
    		}    		
    		hql.append(")");    		
    	}
		
    	if(paramsMap.containsKey("currentUnitId")){//只查出组织ID为他自己的的记录
    		hql.append(" and c.entry.unitId = :currentUnitId ");
    		values.put("currentUnitId", paramsMap.get("currentUnitId"));
    	}
    	
    	if(paramsMap.containsKey("workflowName")){
    		hql.append(" and c.entry.workflowName like :workflowName ");
    		values.put("workflowName", "%"+paramsMap.get("workflowName").toString()+"%");
    	}
		
    	if(paramsMap.containsKey("startDate")){
    		hql.append(" and c.startDate >= :startDate ");
    		values.put("startDate", paramsMap.get("startDate"));
    	}
    	
    	if(paramsMap.containsKey("endDate")){
    		hql.append(" and c.startDate <= :endDate ");
    		values.put("endDate", paramsMap.get("startDate"));
    	}
    	 
    	hql.append("order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return findByHql(objPage, hql.toString(), values);
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.workflow.spi.WorkflowStore#queryHistoryWorksByPage(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Page queryHistoryWorksByPage(Page objPage,String userId,Map<String, Object> paramsMap)throws StoreException {
		Assert.hasText(userId, "当前用户ID不能为空.");
		StringBuffer hql = new StringBuffer();
		Map<String, Object> values = new HashMap<String, Object>(20);
		hql.append(" from "+HibernateHistoryStep.class.getSimpleName()+" h where h.caller = :caller ");
		values.put("caller", Workflow.TYPE_USER.concat(userId));
		
		if(paramsMap.containsKey("workflowName")){
    		hql.append(" and h.entry.workflowName like :workflowName ");
    		values.put("workflowName", "%"+paramsMap.get("workflowName").toString()+"%");
    	}
		
		if(paramsMap.containsKey("startDate")){
    		hql.append(" and h.startDate >= :startDate ");
    		values.put("startDate", paramsMap.get("startDate"));
    	}
    	
    	if(paramsMap.containsKey("endDate")){
    		hql.append(" and h.startDate <= :endDate ");
    		values.put("endDate", paramsMap.get("startDate"));
    	}
    	hql.append("order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findEntryByCondition(Page pageObj, Map<String, Object> params)throws StoreException {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> values = new HashMap<String, Object>();
		hql.append("from "+HibernateWorkflowEntry.class.getSimpleName()+" where 1=1 ");
		if(params.containsKey("workflowName")){
			hql.append(" and workflowName = :workflowName");
			values.put("workflowName", params.get("workflowName").toString());
		}
		return null;
	}
	
}