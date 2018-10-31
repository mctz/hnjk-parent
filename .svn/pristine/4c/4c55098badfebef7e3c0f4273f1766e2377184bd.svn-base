package com.hnjk.edu.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.service.IClassroomService;
/**
 * 课室管理服务接口实现
 * <code>ClassroomServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-21 下午02:11:57
 * @see 
 * @version 1.0
 */
@Transactional
@Service("classroomService")
public class ClassroomServiceImpl extends BaseServiceImpl<Classroom> implements IClassroomService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private BaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly = true)
	public Page findClassroomByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Criteria objCriterion = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createCriteria(Classroom.class);
		
		objCriterion.add(Restrictions.eq("isDeleted", 0));
		if (condition.containsKey("classroomName")) {
			objCriterion.add(Restrictions.ilike("classroomName", "%" + condition.get("classroomName") + "%"));
		}
		if (condition.containsKey("buildingId")) {
			objCriterion.add(Restrictions.eq("building.resourceid", condition.get("buildingId")));
		}
		if (condition.containsKey("classroomType")) {
			objCriterion.add(Restrictions.eq("classroomType", condition.get("classroomType")));
		}
		
		// 排序
		if (ExStringUtils.isNotEmpty(objPage.getOrderBy())
				&& ExStringUtils.isNotEmpty(objPage.getOrder())) {
			if (Page.ASC.equals(objPage.getOrder())) {
				objCriterion.addOrder(Order.asc(objPage.getOrderBy()));
			} else {
				objCriterion.addOrder(Order.desc(objPage.getOrderBy()));
			}
		}		
		return exGeneralHibernateDao.findByCriteriaSession(Classroom.class, objPage, objCriterion);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Classroom> findClassroomByCondition(Map<String, Object> condition) throws ServiceException{
		Map<String,Object> values = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer("from " + Classroom.class.getSimpleName() + " cr where cr.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if (condition.containsKey("classroomName")) {
			sb.append(" and cr.classroomName like :classroomName ");
			values.put("classroomName", "%" + condition.get("classroomName") + "%");
		}
		if (condition.containsKey("classroomname")) {
			sb.append(" and cr.classroomName = :classroomname ");
			values.put("classroomname", condition.get("classroomname"));
		}
		if (condition.containsKey("buildingId")) {
			sb.append(" and cr.building.resourceid =:buildingId ");
			values.put("buildingId", condition.get("buildingId"));
		}
		if (condition.containsKey("classroomType")) {
			sb.append(" and cr.classroomType=:classroomType ");
		    values.put("classroomType", condition.get("classroomType"));
		}
		if(condition.containsKey("branchSchool")){
			sb.append(" and cr.building.branchSchool.resourceid=:branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}else if(condition.containsKey("brSchoolid")){
			sb.append(" and cr.building.branchSchool.resourceid=:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if (condition.containsKey("isUseCourse")) {
			sb.append(" and cr.isUseCourse=:isUseCourse ");
		    values.put("isUseCourse", condition.get("isUseCourse"));
		}
		if(condition.containsKey("addSql")){
			sb.append(condition.get("addSql"));
		}
		sb.append(" order by cr.layerNo,cr.unitNo,cr.showOrder ");
		return (List<Classroom>) exGeneralHibernateDao.findByHql(sb.toString(), values);
	}
	
	@Override
	public Page findClassroomByHql(Map<String, Object> condition, Page objPage)	throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append("from ").append(Classroom.class.getSimpleName()).append(" where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if (condition.containsKey("classroomName")) {
			hql.append(" and classroomName like :classroomName ");
			values.put("classroomName", "%" + condition.get("classroomName") + "%");
		}			
		if (condition.containsKey("buildingId")) {
			hql.append(" and building.resourceid =:buildingId ");
			values.put("buildingId", condition.get("buildingId"));
		}
		if (condition.containsKey("classroomType")) {
			hql.append(" and classroomType =:classroomType ");
			values.put("classroomType", condition.get("classroomType"));
		}
		if (condition.containsKey("brSchoolid")) {
			hql.append(" and building.branchSchool.resourceid =:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(objPage.isOrderBySetted()){
			hql.append(" order by ").append(objPage.getOrderBy()).append(" ").append(objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
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
	@Transactional(readOnly = true)
	public Long getNextShowOrder(String buildingId) throws ServiceException {
		Long showOrder = exGeneralHibernateDao.findUnique("select max(c.showOrder) from "+Classroom.class.getSimpleName()+" c where c.isDeleted=0 and c.building.resourceid=? ",buildingId);
		if(showOrder==null) {
			showOrder = 0l;
		}
		return showOrder.longValue()+1;
	}

	@Override
	public String constructOptions(Map<String, Object> condition,String defaultValue) throws ServiceException {
		List<Classroom> list = findClassroomByCondition(condition);
		StringBuffer option = new StringBuffer("<option value=''></option>");
		if(null != list && list.size()>0){
			for(Classroom t : list){
				if(t.getResourceid().equals(defaultValue)){
					option.append("<option selected='selected' value='"+t.getResourceid()+"'");				
					option.append(">"+t.getClassroomName()+"/"+t.getSeatNum()+"位</option>");
				}else {
					option.append("<option value='"+t.getResourceid()+"'");				
					option.append(">"+t.getClassroomName()+"/"+t.getSeatNum()+"位</option>");
				}
			}
		}
		return option.toString();
	}

	@Override
	public String findNamesByOutResourceids(String ids, String brSchoolid) {
		String classroomNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("brSchoolid", brSchoolid);
			param.put("ids", ids);
			String sql = " select wm_concat(cl.classroomname) classroomNames from edu_base_classroom cl join edu_base_building b on b.resourceid=cl.buildingid"
					+ " where b.branchschoolid=:brSchoolid and cl.resourceid not in(:ids) and cl.isusecourse='Y'";
			Map<String, Object> map = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(map!=null && map.size()>0){
				classroomNames = (String)map.get("classroomNames");
			}
		} catch (Exception e) {
			logger.error("根据课室id获取课室名称集合出错", e);
		}
		return classroomNames;
	}
}
