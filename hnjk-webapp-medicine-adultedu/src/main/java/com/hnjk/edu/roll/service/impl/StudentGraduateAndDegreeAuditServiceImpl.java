package com.hnjk.edu.roll.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
import com.hnjk.edu.roll.service.IStudentGraduateAndDegreeAuditService;

/**
 * 学生毕业、学位审核服务实现.
 * @author hzg
 *
 */
@Service("studentGraduateAndDegreeAuditService")
public class StudentGraduateAndDegreeAuditServiceImpl extends BaseServiceImpl<StudentGraduateAndDegreeAudit> implements IStudentGraduateAndDegreeAuditService{
	/* 
	 * 查找毕业审核的学生
	 * (non-Javadoc)
	 * @see com.hnjk.edu.roll.service.IGraduationQualifService#findGraduateionQualifByCondition(java.util.Map, com.hnjk.core.rao.dao.helper.Page)
	 */
	@Override
	public Page findGraduateAuditByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		hql += " and graduateAuditStatus is not null  ";
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and studentInfo.branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and studentInfo.major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and studentInfo.classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("name")){
			hql += " and studentInfo.studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("grade")){
			hql += " and studentInfo.grade.resourceid= :grade ";
			values.put("grade", condition.get("grade"));
		}
		if(condition.containsKey("studyNo")){
			hql += " and studentInfo.studyNo like :studyNo ";
			values.put("studyNo", "%" + condition.get("studyNo").toString().trim() + "%");
		}
		if(condition.containsKey("auditStatus")){
			hql += " and graduateAuditStatus= "+condition.get("auditStatus");
//			values.put("auditStatus", condition.get("auditStatus"));
		}
		if(condition.containsKey("other_auditStatus")){
			hql += " and theGraduationStatis= "+condition.get("other_auditStatus");
			//values.put("other_auditStatus", condition.get("other_auditStatus"));
		}
		if(condition.containsKey("classesId")){
			hql += " and studentInfo.classes.resourceid=:classesId ";
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("confirmStatus")){
			if("1".equals(ExStringUtils.trim(condition.get("confirmStatus")+""))){
				hql += " and comfirm = 1";
			}else{
				hql += " and (comfirm <> 1 or comfirm is null) ";
			}
		}
		//dateDuration 时间范围标识
		if(condition.containsKey("dateDuration")){
			hql += " and auditTime between to_date(:beginDate,'yyyy-mm-dd') and to_date(:endDate,'yyyy-mm-dd') ";
			values.put("beginDate", condition.get("graduateAuditBeginTime"));
			values.put("endDate", condition.get("graduateAuditEndTime"));
		}
		hql +=" order by auditTime desc";
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	/* 
	 * 查找毕业审核的学生
	 * (non-Javadoc)
	 * @see com.hnjk.edu.roll.service.IGraduationQualifService#findGraduateionQualifByCondition(java.util.Map, com.hnjk.core.rao.dao.helper.Page)
	 */
	@Override
	public Page findDegreeAuditByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" s left join  s.graduateData  where 1=1 ";
		hql += " and s.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		hql += " and s.degreeAuditStatus is not null  ";
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and s.studentInfo.branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and s.studentInfo.major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and s.studentInfo.classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("name")){
			hql += " and s.studentInfo.studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("grade")){
			hql += " and s.studentInfo.grade.resourceid= :grade ";
			values.put("grade", condition.get("grade"));
		}
		if(condition.containsKey("studyNo")){
			hql += " and s.studentInfo.studyNo like :studyNo ";
			values.put("studyNo", "%" + condition.get("studyNo").toString().trim() + "%");
		}
		if(condition.containsKey("auditStatus")){
			hql += " and s.degreeAuditStatus= "+condition.get("auditStatus");
		}
		if(condition.containsKey("confirmStatus")){
			hql += " and s.degreeAuditMemo like :confirmStatus ";
			values.put("confirmStatus", "%"+condition.get("confirmStatus"));
		}
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
}
