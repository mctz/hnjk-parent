package com.hnjk.platform.system.history;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.EventSource;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.base.model.IBaseModel;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.platform.system.model.HistoryModel;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 使用hibernate监听器，用来处理审计日志. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-3下午09:26:53
 * @see 
 * @version 1.0
 */
public class HistoryEvenListener implements PostUpdateEventListener ,PostInsertEventListener,PostDeleteEventListener{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1300859460968614799L;
	

	/*
	 * 更新审计
	 * @see org.hibernate.event.PostUpdateEventListener#onPostUpdate(org.hibernate.event.PostUpdateEvent)
	 */
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		Object entity = event.getEntity();
		if(isAableLog(entity,OperationType.UPDATE)){//如果可记录日志则
			 Object[] oldValues = event.getOldState();//旧的属性
			 Object[] newValues = event.getState();//新的属性
		     String[] properties = event.getPersister().getPropertyNames();
			 	 
		     User currentUser = SpringSecurityHelper.getCurrentUser();
		     String userid = "";
		     String cnName =" ";
		     String loginIp = "";
		     if(currentUser!=null){
		    	 //通过外部接口访问的使用hnjk
		    	 userid = currentUser.getResourceid();
		    	 cnName = currentUser.getCnName();
		    	 loginIp = currentUser.getLoginIp();
		     }
		     //该方法貌似是单独实例化，暂时想不到一个较好的方法获取操作对应的来源。
		     IBaseSupportJdbcDao baseSupportJdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");//获取基础jdbc业务实现类
		     String requestUrl = "";
		     try{
			     List<Map<String,Object>> list =  baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select u.requestUrl from hnjk_sys_users u where u.resourceid = ? ", new Object[]{userid});
			     if(list.size()>0){
			    	 requestUrl = null==list.get(0).get("requestUrl")?"":list.get(0).get("requestUrl").toString();
			     }else{
			    	 requestUrl = "无法获取请求路径";
			     }
			     //取完数据就删除
			     baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" update hnjk_sys_users u set u.requesturl= '' where u.resourceid = '"+userid+"'", null);
		     }catch (Exception e) {
		    	 requestUrl = "无法获取请求路径";
		    	 logger.error("无法获取请求路径：{}",e.fillInStackTrace());
		     }
		     
		     boolean isChanged=false;
		 	 boolean isDeleted = false;
		 	 Map<String, Object> oldDataMap = new HashMap<String, Object>();
		 	 Map<String, Object> newDataMap = new HashMap<String, Object>();
		 	 
		 	 if(oldValues!=null && newValues!=null && properties!=null){
		 		 for(int i=0;i<properties.length;i++){
			    	 if ((oldValues[i] != null && !oldValues[i].equals(newValues[i])) || (oldValues[i] == null && newValues[i] != null)) {//如果数据有变化
			    		if (newValues[i] instanceof PersistentCollection) {
							continue;	//如果为集合，则忽略
						}
			    		if("version".equalsIgnoreCase(properties[i])) {
							continue;//版本变化，忽略
						}
			    		if("auditResults".equalsIgnoreCase(properties[i])) {
							continue;  //图片修改 忽略
						}
			    		if(oldValues[i] instanceof IBaseModel) {
			    			oldValues[i] = getEntityId(event.getSession(),oldValues[i]);
			    		}
			    		if(newValues[i] instanceof IBaseModel) {
			    			newValues[i] = getEntityId(event.getSession(),newValues[i]);
			    		}
			    		//HistoryElement element = new HistoryElement(String.valueOf(properties[i]),String.valueOf(oldValues[i]),String.valueOf(newValues[i]));
			    		
			    		oldDataMap.put(properties[i], oldValues[i]);
			    		newDataMap.put(properties[i], newValues[i]);
			    		
			    		isChanged = true;
			    		
			    		if("isdeleted".equalsIgnoreCase(properties[i])){
			    			if(Integer.parseInt(newValues[i].toString()) == 1){
			    				isDeleted = true;
			    			}
			    		}
			    		logger.debug(event.getId()+":"+properties[i]+" >>> "+oldValues[i]+" - "+newValues[i]);
			    	 }
			     }
		 	 }
		    
		     if(isChanged){
		    	  HistoryModel historyModel = new HistoryModel();
		    	  if(isDeleted){
		    		  historyModel.setOperatorType(OperationType.DELETE);
		    	  }else{
		    		  historyModel.setOperatorType(OperationType.UPDATE);
		    		  historyModel.setBeforeValue(JsonUtils.mapToJson(oldDataMap));
					  historyModel.setAfterValue(JsonUtils.mapToJson(newDataMap));
		    	  }
		    	 
				  historyModel.setEntiryId(String.valueOf(event.getId()));
				  historyModel.setEntiryName(event.getEntity().getClass().getSimpleName());				 
				  historyModel.setOperatorMan(cnName);
				  historyModel.setOperatorManId(userid);
				  historyModel.setOperatorIp(loginIp);
				  historyModel.setRequestUrl(requestUrl);
				  logHistory(event.getSession(),historyModel);
		     }
		   		
		}
	
	}

	/*
	 * 删除审计	
	 * 物理删除  
	 * @see org.hibernate.event.PostDeleteEventListener#onPostDelete(org.hibernate.event.PostDeleteEvent)
	 */
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();
		if(isAableLog(entity,OperationType.TRUNCATE)){
		User currentUser = SpringSecurityHelper.getCurrentUser();
		 String userid = "";
	     String cnName =" ";
	     String loginIp = "";
	     if(currentUser!=null){
	    	 //通过外部接口访问的使用hnjk
	    	 userid = currentUser.getResourceid();
	    	 cnName = currentUser.getCnName();
	    	 loginIp = currentUser.getLoginIp();
	     }
		//该方法貌似是单独实例化，暂时想不到一个较好的方法获取操作对应的来源。
	     IBaseSupportJdbcDao baseSupportJdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");//获取基础jdbc业务实现类
	     String requestUrl = "";
	     try{
		     List<Map<String,Object>> list =  baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select u.requestUrl from hnjk_sys_users u where u.resourceid = ? ", new Object[]{userid});
		     if(list.size()>0){
		    	 requestUrl = null==list.get(0).get("requestUrl")?"":list.get(0).get("requestUrl").toString();
		     }else{
		    	 requestUrl = "无法获取请求路径";
		     }
		     //取完数据就删除
		     baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" update hnjk_sys_users u set u.requesturl= '' where u.resourceid = '"+userid+"'", null);
	     }catch (Exception e) {
	    	 requestUrl = "无法获取请求路径";
	    	 logger.error("无法获取请求路径：{}",e.fillInStackTrace());
	     }
	     String[] properties = event.getPersister().getPropertyNames();
	     Map<String, Object> oldDataMap = new HashMap<String, Object>();
	     Object[] oldValues = event.getDeletedState();//旧的属性
	     
	     if(oldValues!=null && properties!=null){
	    	 for(int i=0;i<properties.length;i++){
		    	 if(oldValues[i] instanceof HibernateProxy){
		    		oldValues[i] = getEntityId(event.getSession(),oldValues[i]);
	 			 }else if(oldValues[i] instanceof IBaseModel){
	 				 //解决有些对象并非代理类
	 				try {
	 					Method m = oldValues[i].getClass().getMethod("getResourceid");
	 					if(null!=m){
	 						oldValues[i] = m.invoke(oldValues[i]);
	 					}
					} catch (Exception e) {
						e.printStackTrace();
					}
	 			}	
		    	oldDataMap.put(properties[i], oldValues[i]);
		    	logger.debug(event.getId()+":"+properties[i]+" >>> "+oldValues[i]);
		     }
	     }
	     
		if(isAableLog(entity,OperationType.TRUNCATE)){
			 HistoryModel historyModel = new HistoryModel();
			 //补充记录删除前的值
			 historyModel.setBeforeValue(JsonUtils.mapToJson(oldDataMap));
			 historyModel.setOperatorType(OperationType.TRUNCATE);
			 historyModel.setEntiryId(String.valueOf(event.getId()));
			 historyModel.setEntiryName(event.getEntity().getClass().getSimpleName());
			 historyModel.setOperatorMan(cnName);
			 historyModel.setOperatorManId(userid);
			 historyModel.setOperatorIp(loginIp);
			 historyModel.setRequestUrl(requestUrl);
			 logHistory(event.getSession(),historyModel);
		}
		}
	}

	/*
	 * 插入审计
	 * @see org.hibernate.event.PostInsertEventListener#onPostInsert(org.hibernate.event.PostInsertEvent)
	 */
	@Override
	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		if(isAableLog(entity,OperationType.INSERT)){
		User currentUser = SpringSecurityHelper.getCurrentUser();
		String userid = "";
        String cnName =" ";
        String loginIp = "";
        if(currentUser!=null){
	    	 //通过外部接口访问的使用hnjk
	    	 userid = currentUser.getResourceid();
	    	 cnName = currentUser.getCnName();
	    	 loginIp = currentUser.getLoginIp();
        }
		//该方法貌似是单独实例化，暂时想不到一个较好的方法获取操作对应的来源。
	     IBaseSupportJdbcDao baseSupportJdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");//获取基础jdbc业务实现类
	     String requestUrl = "";
	     try{
		     List<Map<String,Object>> list =  baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select u.requestUrl from hnjk_sys_users u where u.resourceid = ? ", new Object[]{userid});
		     if(list.size()>0){
		    	 requestUrl = null==list.get(0).get("requestUrl")?"":list.get(0).get("requestUrl").toString();
		     }else{
		    	 requestUrl = "无法获取请求路径";
		     }
		     //取完数据就删除
		     baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" update hnjk_sys_users u set u.requesturl= '' where u.resourceid = '"+userid+"'", null);
	     }catch (Exception e) {
	    	 requestUrl = "无法获取请求路径";
	    	 logger.error("无法获取请求路径：{}",e.fillInStackTrace());
	     }
		
		 String[] properties = event.getPersister().getPropertyNames();
	     Map<String, Object> newDataMap = new HashMap<String, Object>();
	     Object[] newValues = event.getState();//旧的属性
	     
	     if(newValues!=null && properties!=null){
	    	 for(int i=0;i<properties.length;i++){
		    		if(newValues[i] instanceof IBaseModel) {
		    			if(newValues[i] instanceof HibernateProxy){
		    				newValues[i] = getEntityId(event.getSession(),newValues[i]);
		    			}else if(newValues[i] instanceof IBaseModel){
		    		    //解决有些对象并非代理类
		    				try {
								Method m = newValues[i].getClass().getMethod("getResourceid");
								newValues[i] = m.invoke(newValues[i]);
							} catch (Exception e) {
								e.printStackTrace();
							}
		    			}
		    		}
		    		newDataMap.put(properties[i], newValues[i]);
		    		logger.debug(event.getId()+":"+properties[i]+" >>> "+newValues[i]);
		         }
	     }
		
		if(isAableLog(entity,OperationType.INSERT)){
			 HistoryModel historyModel = new HistoryModel();
			 //补充记录新增后的值
			 historyModel.setAfterValue(JsonUtils.mapToJson(newDataMap));
			 historyModel.setOperatorType(OperationType.INSERT);
			 historyModel.setEntiryId(String.valueOf(event.getId()));
			 historyModel.setEntiryName(event.getEntity().getClass().getSimpleName());
			 historyModel.setOperatorMan(cnName);
			 historyModel.setOperatorManId(userid);
			 historyModel.setOperatorIp(loginIp);
			 historyModel.setRequestUrl(requestUrl);
			 logHistory(event.getSession(),historyModel);			
		}
		}
	}
	
	//判断当前实体是否可记录日志
	private boolean isAableLog(Object entity,OperationType oprateType){		 
		 Historizable historizable = entity.getClass().getAnnotation(Historizable.class);
		 if(null != historizable && historizable.logable()){//可记录日志
			 //拿实体的enableLogHistory值
			 try{
				 boolean enableLogHistory = (Boolean)ExBeanUtils.getFieldValue(entity,"enableLogHistory");
				 if(enableLogHistory){
					 for(int i=0;i<historizable.value().length;i++){
						 if(oprateType.equals(historizable.value()[i])){
							 return true;
						 }//end if
					 }//end for
				 }//end if
				 
			 }catch(Exception e){
				 //do nothing
			 }		
			
		 }		
		return false;
	}

	//记录审计日志
	private void logHistory(EventSource session,HistoryModel historyModel){
		Session tempSession = session.getSessionFactory().openSession();
		Transaction tx = tempSession.beginTransaction();
		try {
            tx.begin();
            tempSession.save(historyModel);
            tempSession.flush();
            tx.commit();
            logger.info("log history for model[{}] sucessed...",historyModel.getEntiryName());
        } catch (Exception ex) {
            tx.rollback();            
        }finally{
        	tempSession.close();
        }
       
	}
	
	private Serializable getEntityId(EventSource session,Object obj){		
		LazyInitializer lazy = ((HibernateProxy) obj).getHibernateLazyInitializer();			
		final Object item = lazy.getImplementation();
		return session.getPersistenceContext().getEntry(item).getId();			
		
	}
	

}
