package com.hnjk.edu.basedata.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.ClassInfo;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.service.IClassService;
/**
 * 
  * @description:班级基础数据-实现类
  * @author xiangy  
  * @date 2013-12-4 上午11:01:11 
  * @version V1.0
 */
@Service("classService")
@Transactional
public class ClassServiceImpl extends BaseServiceImpl<ClassInfo> implements IClassService {
    
	
	@Override
	@Transactional(readOnly=true)
	public Page findClassByCondition(Map<String,Object>condition,Page objPage){
		Map<String ,Object> values=new HashMap<String ,Object>();
		String hql=" from "+ClassInfo.class.getSimpleName()+" where 1=1";
		hql+=" and isDeleted=:isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("className")){
			hql+=" and className like :className";
			values.put("className", "%"+condition.get("className")+"%");
		}
		if(condition.containsKey("orgUnit")){
			hql+=" and branchSchoolId= '"+condition.get("orgUnit")+"'";
		}
		hql+=" order by "+objPage.getOrderBy()+" "+objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	//根据resourceid去班级名称
	@Override
	@Transactional(readOnly=true)
	public String getClassName(String classId) throws ServiceException{
		String className=exGeneralHibernateDao.findUnique("select className from "+ ClassInfo.class.getSimpleName()+" c where c.isDeleted=0 and c.resourceid=?",classId);
		if(className==null){
			className="";
		}
		return className;
	}

}
