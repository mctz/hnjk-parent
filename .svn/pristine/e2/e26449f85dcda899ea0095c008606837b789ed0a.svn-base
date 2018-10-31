package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.ExamStaff;
import com.hnjk.edu.teaching.service.IExamStaffService;
/**
 * 考试考务-监巡考人员服务接口管理实现.
 * <code>ExamStaffServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-8-19 上午11:10:19
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examStaffService")
public class ExamStaffServiceImpl extends BaseServiceImpl<ExamStaff> implements IExamStaffService {

	@Override
	@Transactional(readOnly=true)
	public Page findExamStaffByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+ExamStaff.class.getSimpleName()+" where 1=1 ");
		hql.append(" and isDeleted = :isDeleted ");
		values.put("isDeleted", 0);

		if(condition.containsKey("name")){//姓名
			hql.append(" and lower(name) like :name ");
			values.put("name", "%"+condition.get("name").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("idcardNum")){//身份证
			hql.append(" and idcardNum =:idcardNum ");
			values.put("idcardNum", condition.get("idcardNum"));
		}
		if(condition.containsKey("orgUnitId")){//所属单位
			hql.append(" and orgUnit.resourceid =:orgUnitId ");
			values.put("orgUnitId", condition.get("orgUnitId"));
		}
		if(condition.containsKey("gender")){//性别
			hql.append(" and gender =:gender ");
			values.put("gender", condition.get("gender"));
		}
		if(condition.containsKey("hasExamstaff")){//是否有过监考
			hql.append(" and hasExamstaff =:hasExamstaff ");
			values.put("hasExamstaff", condition.get("hasExamstaff"));
		}
		if(condition.containsKey("workLevel")){//工作级别
			hql.append(" and workLevel =:workLevel ");
			values.put("workLevel", condition.get("workLevel"));
		}
		if(objPage.isOrderBySetted()){
			hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder()+" ");
		}
		return findByHql(objPage, hql.toString(), values);
	}

}
