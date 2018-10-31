package com.hnjk.platform.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.CustomFormDefine;
import com.hnjk.platform.system.service.ICustomFormService;

@Service("customFormService")
@Transactional
public class CustomFormServiceImpl extends BaseServiceImpl<CustomFormDefine> implements ICustomFormService{

	
	@Override
	@Transactional(readOnly=true)
	public Page findCustomerFormByCondition(Map<String, Object> condition,	Page page) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		
		if(condition.containsKey("formName")){
			objCriterion.add(Restrictions.like("formName","%"+condition.get("formName")+"%"));
		}
		if(condition.containsKey("formCode")){
			objCriterion.add(Restrictions.eq("formCode",condition.get("formCode")));
		}
		
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		return  exGeneralHibernateDao.findByCriteria(CustomFormDefine.class, page, objCriterion.toArray(new Criterion[objCriterion.size()]));
		
	}
	
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids != null && ids.length>0){
			for (String id : ids) {
				truncate(get(id));
			}
		}
	}
}
