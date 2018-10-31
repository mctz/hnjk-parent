package com.hnjk.platform.system.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.SetTime;
import com.hnjk.platform.system.service.ISetTimeService;

/**
 * 设置时间实现
 * @author Zik
 *
 */
@Service("setTimeService")
@Transactional
public class SetTimeServiceImpl extends BaseServiceImpl<SetTime> implements ISetTimeService {

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findSetTimeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select st from "+SetTime.class.getSimpleName()+" st where st.isDeleted=0 ");
		
		if(condition.containsKey("businessType")){
			hql.append("and st.businessType=:businessType ");
			values.put("businessType", condition.get("businessType"));
		}
		return findByHql(objPage, hql.toString(), values);
	}

	/**
	 * 批量删除
	 * @param ids
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
			}
		}
	}

	/**
	 * 根据业务类型获取时间设置
	 * @param businessType
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public SetTime getSetTimeByBusinessType(String businessType) throws ServiceException {
		String hql = "select st from "+SetTime.class.getSimpleName()+" st where st.isDeleted=0 and st.businessType=? ";
		
		return findUnique(hql, businessType);
	}

	

}
