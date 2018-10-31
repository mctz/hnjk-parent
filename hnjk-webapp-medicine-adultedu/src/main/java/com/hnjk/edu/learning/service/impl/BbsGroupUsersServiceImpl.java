package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.BbsGroupUsers;
import com.hnjk.edu.learning.service.IBbsGroupUsersService;

@Transactional
@Service("bbsGroupUsersService")
public class BbsGroupUsersServiceImpl extends BaseServiceImpl<BbsGroupUsers> implements IBbsGroupUsersService {

	@Override
	public void deleteBbsGroupUsers(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				BbsGroupUsers u = get(id);
				if(u.getStudentInfo().getResourceid().equals(u.getBbsGroup().getLeaderId())){
					u.getBbsGroup().setLeaderId(null);
					u.getBbsGroup().setLeaderName(null);
				}
				delete(id);	
				logger.info("删除学习小组成员="+id);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Page findBbsGroupUsersByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+BbsGroupUsers.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("groupId")){//组ID
			hql += " and bbsGroup.resourceid =:groupId ";
			values.put("groupId", condition.get("groupId"));
		}
		if(condition.containsKey("courseId")){//课程ID
			hql += " and bbsGroup.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("classesId")){//班级ID
			hql += " and studentInfo.classes.resourceid =:classesId ";
			values.put("classesId", condition.get("classesId"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BbsGroupUsers> findBbsGroupUsersByCourseId(String courseId) throws ServiceException {
		return (List<BbsGroupUsers>) exGeneralHibernateDao.findByHql(" from "+BbsGroupUsers.class.getSimpleName()+" where isDeleted=0 and bbsGroup.course.resourceid=?", courseId);
	}

	@Override
	public boolean isUserInGroup(String userId, String groupId) throws ServiceException {
		BbsGroupUsers groupUser = exGeneralHibernateDao.findUnique(" from "+BbsGroupUsers.class.getSimpleName()+" where isDeleted=0 and bbsGroup.resourceid=? and studentInfo.sysUser.resourceid=? ", new Object[]{groupId,userId});
		return groupUser!=null?true:false;
	}

}
