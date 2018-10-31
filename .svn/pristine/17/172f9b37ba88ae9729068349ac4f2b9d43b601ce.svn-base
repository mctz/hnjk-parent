package com.hnjk.edu.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Building;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.service.IBuildingService;
/**
 * 教学楼管理服务接口实现
 * <code>BuildingServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-21 下午01:50:21
 * @see 
 * @version 1.0
 */
@Transactional
@Service("buildingService")
public class BuildingServiceImpl extends BaseServiceImpl<Building> implements IBuildingService {

	@Override
	@Transactional(readOnly = true)
	public Page findBuildingByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Criteria objCriterion = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createCriteria(Building.class);
		
		objCriterion.add(Restrictions.eq("isDeleted", 0));
		if (condition.containsKey("buildingName")) {
			objCriterion.add(Restrictions.ilike("buildingName", "%" + condition.get("buildingName") + "%"));
		}
		if (condition.containsKey("branchSchoolId")) {
			objCriterion.add(Restrictions.eq("branchSchool.resourceid", condition.get("branchSchoolId")));
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
		return exGeneralHibernateDao.findByCriteriaSession(Building.class, objPage, objCriterion);
	}
	
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				Building building = get(id);
				for (Classroom classroom : building.getClassroom()) {
					exGeneralHibernateDao.delete(Classroom.class, classroom.getResourceid());
				}
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long getNextShowOrder() throws ServiceException {
		Long showOrder = exGeneralHibernateDao.findUnique("select max(b.showOrder) from "+Building.class.getSimpleName()+" b where b.isDeleted=0 ");
		if(showOrder==null) {
			showOrder = 0l;
		}
		return showOrder.longValue()+1;
	}

	/**
	 * 根据条件构造成select标签中的option(只供select标签用)
	 * @param condition
	 * @return String
	 * @throws ServiceException
	 */
	@Override
	public StringBuffer constructOptions(Map<String, Object> condition, String defaultValue) throws ServiceException{
		StringBuffer buildingOption = new StringBuffer("<option value=''></option>");
		Map<String, Object> values = new HashMap<String, Object>();
		String hql = " from "+Building.class.getSimpleName()+" where isDeleted=0";
		if(condition.containsKey("brSchoolid")){
			hql += " and branchSchool.resourceid=:brSchoolid";
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		
		List<Building> buildingList = findByHql(hql, values);
		if(null != buildingList && buildingList.size()>0){
			for(Building t : buildingList){
				if(t.getResourceid().equals(defaultValue)){
					buildingOption.append("<option selected='selected' value='"+t.getResourceid()+"'");				
					buildingOption.append(">"+t.getBuildingName()+"</option>");
				}else {
					buildingOption.append("<option value='"+t.getResourceid()+"'");				
					buildingOption.append(">"+t.getBuildingName()+"</option>");
				}
			}
		}
		return buildingOption;
	}

}
