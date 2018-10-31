package com.hnjk.edu.learning.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.BbsGroup;
import com.hnjk.edu.learning.model.BbsGroupUsers;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IBbsGroupService;
import com.hnjk.edu.learning.service.IBbsGroupUsersService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
//import com.hnjk.edu.teaching.model.TeachTask;
/**
 * 学生讨论小组服务接口实现
 * <code>BbsGroupServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-27 下午02:07:55
 * @see 
 * @version 1.0
 */
@Transactional
@Service("bbsGroupService")
public class BbsGroupServiceImpl extends BaseServiceImpl<BbsGroup> implements IBbsGroupService {

	@Autowired
	@Qualifier("bbsGroupUsersService")
	private IBbsGroupUsersService bbsGroupUsersService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//JDBC 支持
	
	@Override
	public void deleteBbsGroup(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				BbsGroup bbsGroup = get(id);
				for (BbsGroupUsers u : bbsGroup.getGroupUsers()) {
					bbsGroupUsersService.delete(u.getResourceid());
				}
				delete(id);	
				logger.info("删除学习小组="+id);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Page findBbsGroupByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " select b from "+BbsGroup.class.getName()+" b where 1=1 ";
		hql += " and b.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("groupName")){//组名
			hql += " and lower(b.groupName) like lower(:groupName) ";
			values.put("groupName", "%"+condition.get("groupName")+"%");
		}
		if(condition.containsKey("courseName")){//课程
			hql += " and lower(b.course.courseName) like lower(:courseName) ";
			values.put("courseName", "%"+condition.get("courseName")+"%");
		}
		if(condition.containsKey("courseId")){//课程ID
			hql += " and b.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("classesId")){//班级ID
			hql += " and b.classes.resourceid =:classesId ";
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("teacherId")){//课程老师
//			hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=b.course.resourceid and (t.teacherId like :teacherId or t.assistantIds like :teacherId ) ) ";
//			values.put("teacherId", "%"+condition.get("teacherId")+"%");//老师
		}
		if(condition.containsKey("status")){//状态
			hql += " and b.status =:status ";
			values.put("status", condition.get("status"));
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql += " order by b."+objPage.getOrderBy().replace(",", ",b.") +" "+ objPage.getOrder();
		}
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BbsGroup> findBbsGroupByCourseId(String courseId) throws ServiceException {
		List<BbsGroup> bbsGroups = findByHql(" from "+BbsGroup.class.getSimpleName()+" where isDeleted=0 and course.resourceid = ? ", courseId);
		return bbsGroups;
	}

	
	@Override
	public void saveOrUpdateBbsGroup(BbsGroup bbsGroup) throws ServiceException {
		bbsGroup.getGroupUsers().clear(); 
		Set<String> list = new HashSet<String>(0);
		if(ExStringUtils.isNotEmpty(bbsGroup.getGroupUserIds())){
			list = new HashSet<String>(Arrays.asList(bbsGroup.getGroupUserIds().split(",")));
		}
		if(ExStringUtils.isNotEmpty(bbsGroup.getLeaderId())){
			list.add(bbsGroup.getLeaderId());
		}
		for (String id : list) {
			StudentInfo stu = (StudentInfo)(exGeneralHibernateDao.get(StudentInfo.class,id));					
			BbsGroupUsers guser = new BbsGroupUsers();
			guser.setStudentInfo(stu);
			guser.setBbsGroup(bbsGroup);
			bbsGroup.getGroupUsers().add(guser);
		}	
		saveOrUpdate(bbsGroup);	
	}
	
	@Override
	public List<BbsGroup> findBbsGroupByUser(String userId) throws ServiceException {
		return (List<BbsGroup>) exGeneralHibernateDao.findByHql("select g.bbsGroup from "+BbsGroupUsers.class.getSimpleName()+" g where g.isDeleted=? and g.studentInfo.sysUser.resourceid = ? order by g.studentInfo.grade.yearInfo.firstYear ", 0,userId);
	}
	
	
	@Override
	public Page findOnlineStu(Map<String, Object> paramMap, Page objPage)
			throws ServiceException {
		StringBuffer hql = new StringBuffer();
		hql.append("from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted=0 ");
	
		if (paramMap.containsKey("teacherId")) {
			hql.append(" and stu.classes.resourceid in  (select te.classes.resourceid from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.isDeleted = 0 and te.teacherId = :teacherId ) su ");
		}
		
		if (paramMap.containsKey("branchSchool")) {
			hql.append(" and stu.branchSchool.resourceid = :branchSchool ");
		}
		
		return this.exGeneralHibernateDao.findByHql(objPage, hql.toString(), paramMap);
	}
	
	
	@Override
	public Page findOnlineStuGroup(Map<String, Object> paramMap, Page objPage) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		hql.append(" select tt.*,cl.stunum stuNumber from (");
		hql.append(" select cl.resourceid,cl.classesname classname,u.unitname,g.gradename,ci.classicname,m.majorname,cl.teachingtype,t.courseid,c.coursename,t.term");
		hql.append(" from EDU_TEACH_TIMETABLE t ");
		hql.append(" join edu_roll_classes cl on cl.resourceid=t.classesid and cl.isdeleted=0");
		hql.append(" join hnjk_sys_unit u on u.resourceid=cl.orgunitid and u.isdeleted=0");
		hql.append(" join edu_base_grade g on g.resourceid=cl.gradeid and g.isdeleted=0");
		hql.append(" join edu_base_classic ci on ci.resourceid=cl.classicid and ci.isdeleted=0");
		hql.append(" join edu_base_major m on m.resourceid=cl.majorid and m.isdeleted=0");
		hql.append(" join edu_base_course c on c.resourceid=t.courseid and c.isdeleted=0");
		hql.append(" where t.isdeleted=0 ");
		if (paramMap.containsKey("teacherId")){
			hql.append(" and t.teacherId = ? ");
			param.add(paramMap.get("teacherId"));
		}
		if (paramMap.containsKey("branchSchool")){
			hql.append(" and u.resourceid = ? ");
			param.add(paramMap.get("branchSchool"));
		}
		if(paramMap.containsKey("courseId")){ 
			hql.append(" and c.resourceid = ? ");
			param.add(paramMap.get("courseId"));
		}
		if(paramMap.containsKey("major")) {
			hql.append(" and m.resourceid = ? ");
			param.add(paramMap.get("major"));
		}
		if(paramMap.containsKey("classesId")){
			hql.append(" and cl.resourceid = ? ");
			param.add(paramMap.get("classesId"));
		}
		if(paramMap.containsKey("yearInfoTerm")){
			hql.append(" and t.term = ? ");
			param.add(paramMap.get("yearInfoTerm"));
		}
		hql.append(" group by  cl.resourceid,cl.classesname,u.unitname,g.gradename,ci.classicname,m.majorname,cl.teachingtype,t.courseid,c.coursename,t.term) tt");
		//查询班级人数
		hql.append(" join (select si.classesid,count(*) stunum from edu_roll_studentinfo si where si.isdeleted=0 and si.studentstatus in('11', '21', '25') group by si.classesid) cl");
		hql.append(" on cl.classesid=tt.resourceid order by tt.unitname,tt.gradename,tt.classicname,tt.majorname,tt.teachingtype");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, hql.toString(), param.toArray());
	}

}
