package com.hnjk.edu.basedata.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.basedata.service.IExamroomService;


/**
 * 考场课室
 * <code>ExamroomServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-7 下午03:36:15
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examroomService")
public class ExamroomServiceImpl extends BaseServiceImpl<Examroom> implements IExamroomService {
	
	
	@Override
	public Page findExamRoomByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Examroom.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("examroomName")){
			hql += " and examroomName like :examroomName ";
			values.put("examroomName", "%"+condition.get("examroomName")+"%");
		}
		if(condition.containsKey("schoolId")){
			hql += " and branchSchool.resourceid = :schoolId ";
			values.put("schoolId", condition.get("schoolId"));
		}
		if(condition.containsKey("branchSchool")){
			hql += " and branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if (condition.containsKey("examroomSize")) {
			hql += " and examroomSize > "+condition.get("examroomSize");
		}
		if (condition.containsKey("isComputerRoom")) {
			if ("Y".equals(condition.get("isComputerRoom"))) {
				hql += " and isComputerRoom = :isComputerRoom ";
				values.put("isComputerRoom", condition.get("isComputerRoom"));
			}else {
				hql += " and isComputerRoom is null ";
			}
			
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
	public List findExamRoomByBranchSchool(String schoolid) throws ServiceException {
		String hql ="from Examroom  er where er.isDeleted='0' "
			 +" and er.branchSchool.resourceid=? ";
		return exGeneralHibernateDao.findByHql(hql, new String[]{schoolid});
	}
	 /**
	  * 根据传入的ID使用IN语句查询Examroom列表
	  */
	@Override
	public List<Examroom> findExamRoomListByIds(String ids) throws ServiceException {
		String [] idsArray = ids.split(","); 
		String  typeOfInId = "";
		for(String id:idsArray){
			   typeOfInId += ",'"+id+"'"; 
		}
		String hql = "from Examroom er where er.resourceid in("+typeOfInId.substring(1)+")";
		return (List<Examroom>) this.exGeneralHibernateDao.findByHql(hql);
	}






}
