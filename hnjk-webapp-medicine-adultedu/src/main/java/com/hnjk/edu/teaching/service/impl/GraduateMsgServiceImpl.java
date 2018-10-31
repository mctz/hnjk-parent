package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.model.GraduateMentorDetails;
import com.hnjk.edu.teaching.model.GraduateMsg;
import com.hnjk.edu.teaching.service.IGraduateMsgService;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.model.User;

/**
 * 毕业生与老师毕业论文交流表.
 * <code>GraduateMsgServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:26:03
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduatemsgservice")
public class GraduateMsgServiceImpl extends BaseServiceImpl<GraduateMsg> implements IGraduateMsgService {

	@Override
	public Page findGraduateMsgByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+GraduateMsg.class.getSimpleName()+" g where 1=1 ");
		hql.append(" and g.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("batchId")){//论文批次
			hql.append(" and g.graduateMentor.examSub.resourceid =:batchId ");
			values.put("batchId", condition.get("batchId"));
		}
		if(condition.containsKey("isGroupTopic")){			
			hql.append(" and g.isGroupTopic =:isGroupTopic ");
			values.put("isGroupTopic", condition.get("isGroupTopic"));			
		}
		if(condition.containsKey("parentId")){			
			hql.append(" and g.parent.resourceid =:parentId ");
			values.put("parentId", condition.get("parentId"));			
		}
		if(condition.containsKey("isMainTopic")){
			hql.append(" and g.parent is null ");
		}
		if(condition.containsKey("isStudent")){			
			if(Constants.BOOLEAN_NO.equals(condition.get("isStudent"))){
				if(condition.containsKey("userId")){
					hql.append(" and g.graduateMentor.edumanager.resourceid =:userId ");
					values.put("userId", condition.get("userId"));
				}
			} else {				
				if(condition.containsKey("userId")){
					hql.append(" and exists (from "+GraduateMentorDetails.class.getSimpleName()+" d where d.isDeleted=0 and d.graduateMentor.resourceid=g.graduateMentor.resourceid and d.studentInfo.sysUser.resourceid=:userId ) ");
					values.put("userId", condition.get("userId"));
				}
			}
		}		
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);					
			}
		}
	}

	@Override
	public void saveOrUpdate(GraduateMsg graduateMsg, String[] files, User user) {
		saveOrUpdate(graduateMsg);
		if(null!=files && files.length>0){
			for(String id : files){
				Attachs attach = attachsService.get(id);
				attach.setFillinNameId(user.getResourceid());
				attach.setFillinName(user.getCnName());
				attach.setFormId(graduateMsg.getResourceid());
				attach.setFormType("GraduateMsg");
				attachsService.saveOrUpdate(attach);
			}
		}
	}
	@Autowired
	@Qualifier("attachsService")
	IAttachsService attachsService;
	
}
