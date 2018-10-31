package com.hnjk.edu.basedata.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.service.IGradeService;

/**
 * <code>GradeServiceImpl</code>基础数据-年级-服务实现.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午02:27:43
 * @see 
 * @version 1.0
 */
@Service("gradeService")
@Transactional
public class GradeServiceImpl extends BaseServiceImpl<Grade> implements IGradeService{

	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持
	
	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(Grade entity) throws ServiceException {
		super.saveOrUpdate(entity);
	}

	@Transactional(readOnly=true)
	@Override
	public Page findGradeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Grade.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("gradeName")){
			hql += " and gradeName like :gradeName ";
			values.put("gradeName", "%"+condition.get("gradeName")+"%");
		}
		if(condition.containsKey("gradename")){
			hql += " and gradeName = :gradename ";
			values.put("gradename", condition.get("gradename"));
		}
		if(condition.containsKey("yearInfoId")){
			hql += " and yearInfo.resourceid=:yearInfoId" ;
			values.put("yearInfoId",condition.get("yearInfoId"));
		}
		if (condition.containsKey("term")) {
			hql += " and term=:term" ;
			values.put("term",condition.get("term"));
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
	public Grade getDefaultGrade() throws ServiceException {
		return findUnique("from "+Grade.class.getSimpleName()+" where isDeleted = ? and isDefaultGrade = ?", 0,Constants.BOOLEAN_YES);
	}

	/**
	 * 根据年级ID集合获取名称集合
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNamesByIds(String ids) throws ServiceException {
		String gradeNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ids", Arrays.asList(ids.split(",")));
			String sql = "select max(name) gradeNames from (select wm_concat(g.gradename) over (order by g.gradename) name  from edu_base_grade g where g.resourceid in (:ids)) ";
			Map<String, Object> gradeNamesMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(gradeNamesMap!=null && gradeNamesMap.size()>0){
				gradeNames = (String)gradeNamesMap.get("gradeNames");
			}
		} catch (Exception e) {
			logger.error("根据年级ID集合获取名称集合出错", e);
		}
		return gradeNames;
	}

}
