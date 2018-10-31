package com.hnjk.edu.basedata.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.service.IClassicService;

/**
 *  <code>ClassicServiceImpl</code>基础数据-层次类型-服务实现.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午02:19:03
 * @see 
 * @version 1.0
 */
@Service("classicService")
@Transactional
public class ClassicServiceImpl extends BaseServiceImpl<Classic> implements IClassicService {

	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public Page findClassicByCondition(Map<String, Object> condition, Page objPage)throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Classic.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("classicName")){
			hql += " and classicName like :classicName ";
			values.put("classicName", "%"+condition.get("classicName")+"%");
		}
		if(condition.containsKey("classicname")){
			hql += " and classicName = :classicname ";
			values.put("classicname", condition.get("classicname"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}

	@Override
	public long getStudentNumbers(String resourceid) throws Exception  {
		Map<String , Object> values = new HashMap<String, Object>();
		long numbers = 0;
		if(resourceid!=null && resourceid!=""){
			values.put("resid", resourceid);
			numbers = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong("select count(*) from edu_roll_studentinfo s where s.isdeleted=0 and s.classesid=:resid and s.studentStatus = '11' ", values);
		}
		return numbers;
	}

}
