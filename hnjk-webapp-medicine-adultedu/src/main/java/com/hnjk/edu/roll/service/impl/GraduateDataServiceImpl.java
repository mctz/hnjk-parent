package com.hnjk.edu.roll.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.model.StudentMakeupList;

/**
 * 毕业生数据表
 * <code>GraduateDataServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:08:30
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduatedataservice")
public class GraduateDataServiceImpl extends BaseServiceImpl<GraduateData> implements IGraduateDataService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public Page findGraduateDataByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder(" from "+GraduateData.class.getSimpleName()+" where isDeleted=0 ");
		condition.put("orderBy", objPage.getOrderBy()+" "+ objPage.getOrder());
		values = getValuesByCondition(hql,condition);
//		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	/**
	 * 通过查询条件获取HQL
	 * @param hql
	 * @param condition
	 * @return
	 */
	private Map<String,Object> getValuesByCondition(StringBuilder hql, Map<String,Object> condition) {
		Map<String,Object> values =  new HashMap<String, Object>();
		if(condition.containsKey("branchSchool")){
			hql.append(" and studentInfo.branchSchool.resourceid = :resourceid ");
			values.put("resourceid", condition.get("branchSchool"));
		}
		if(condition.containsKey("grade")){
			hql.append(" and studentInfo.grade.resourceid = :grade ");
			values.put("grade", condition.get("grade"));
		}
		if(condition.containsKey("major")){
			hql.append(" and studentInfo.major.resourceid = :major ");
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("name")){
			hql.append(" and studentInfo.studentName like :name ");
			values.put("name", condition.get("name")+"%");
		}
		if(condition.containsKey("studyNo")){
			hql.append(" and studentInfo.studyNo like :studyNo ");
			values.put("studyNo", "%" + condition.get("studyNo").toString().trim() + "%");
		}
		if (condition.containsKey("studyNos")) {
			hql.append(" and studentInfo.studyNo in ("+condition.get("studyNos").toString()+")");
		} else if (condition.containsKey("studyNoList")) {
			hql.append(" and studentInfo.studyNo in (:studyNoList)");
			values.put("studyNoList", condition.get("studyNoList"));
		}
		if(condition.containsKey("classic")){
			hql.append(" and studentInfo.classic.resourceid = :classic ");
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("publishStatus")){
			hql.append(" and publishStatus = :publishStatus ");
			values.put("publishStatus", condition.get("publishStatus"));
		}
		if(condition.containsKey("stus")){
			hql.append(" and studentInfo.resourceid in ("+condition.get("stus")+") ");
		}
		if(condition.containsKey("forDegreeAudit")){
			//可以进行学位资格审核的是应届本科毕业生
//			hql += " and studentInfo.classic.classicCode = '5' "; //先代码规则  5是本科
			// TODO:以后会全部优化，目前先使用层次名称来判断
			hql.append(" and (studentInfo.classic.classicName = '本科' or studentInfo.classic.classicName = '专升本') "); //先代码规则  是本科或专升本
			hql.append(" and (degreeStatus != 'Y' or degreeStatus is null) ");
		}
		//班级
		if(condition.containsKey("classId")){
			hql.append(" and studentInfo.classes.resourceid = :classId");
			values.put("classId", condition.get("classId"));
		}else if(condition.containsKey("classes")){
			hql.append(" and studentInfo.classes.resourceid = :classes ");
			values.put("classes", condition.get("classes"));
		}
		//学习形式
		if(condition.containsKey("teachingType")){
			hql.append(" and studentInfo.teachingType = :teachingType");
			values.put("teachingType", condition.get("teachingType"));
		}else if(condition.containsKey("teachingtype")){
			hql.append(" and studentInfo.teachingType = '"+condition.get("teachingtype")+"' ");
		}
		if(condition.containsKey("graduateDate")){
			hql.append(" and graduateDate = to_date(:graduateDate,'yyyy-mm-dd') ");
			values.put("graduateDate", condition.get("graduateDate"));
		}
		if(condition.containsKey("rollCard")){
			hql.append(" and studentInfo.rollCardStatus=:rollCard ");
			values.put("rollCard", condition.get("rollCard"));
		}
		//学位状态
		if(condition.containsKey("degreeStatus")){
			hql.append(" and degreeStatus = :degreeStatus ");
			values.put("degreeStatus", condition.get("degreeStatus"));
		}
		if(condition.containsKey("degreeApplyStatus")){
			hql.append("  and degreeApplyStatus =:degreeApplyStatus ");
			values.put("degreeApplyStatus", condition.get("degreeApplyStatus"));
		}
		if(condition.containsKey("havePhoto")){
			if(Constants.BOOLEAN_YES.equals(condition.get("havePhoto").toString())){
				hql.append(" and studentInfo.studentBaseInfo.photoPath is not null ");
			}else{
				hql.append(" and studentBaseInfo.photoPath is null ");
			}
		}
		boolean hascGDb = condition.containsKey("confirmGraduateDateb");
		boolean hascGDe = condition.containsKey("confirmGraduateDatee");
		if(hascGDb||hascGDe){
			if (hascGDb&&hascGDe){
				hql.append(" and resourceid in (select a.graduateData.resourceid from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+ " a  where a.auditTime between to_date(:b,'yyyy-mm-dd') and to_date(:e,'yyyy-mm-dd')  and a.graduateAuditStatus='1' ) ");
				values.put("b", condition.get("confirmGraduateDateb"));
				values.put("e", condition.get("confirmGraduateDatee"));
			}
			if (hascGDb&&!hascGDe){
				hql.append(" and resourceid in (select a.graduateData.resourceid from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+ " a  where a.auditTime > to_date(:b,'yyyy-mm-dd')   and a.graduateAuditStatus='1' ) ");
				values.put("b", condition.get("confirmGraduateDateb"));
				values.put("e", condition.get("confirmGraduateDatee"));
			}
			if (!hascGDb&&hascGDe){
				hql.append(" and resourceid in (select a.graduateData.resourceid from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+ " a  where a.auditTime < to_date(:e,'yyyy-mm-dd')  and a.graduateAuditStatus='1' ) ");
				values.put("b", condition.get("confirmGraduateDateb"));
				values.put("e", condition.get("confirmGraduateDatee"));
			}
		}
		if(condition.containsKey("isgraduation")){
			hql.append(" and studentInfo.studentStatus = :isgraduation "); //毕业生
			values.put("isgraduation", condition.get("isgraduation"));
		}
		if(condition.containsKey("graduationType")){// 毕业生类型
			hql.append(" and studentInfo.studentStatus = :graduationType ");
			values.put("graduationType", condition.get("graduationType"));
		}
		if(condition.containsKey("degreeEnglish")){
			if ("Y".equals(condition.get("degreeEnglish"))) {
				hql.append(" and exists (from "+StateExamResults.class.getSimpleName()+" sr where sr.isDeleted = 0 and sr.isIdented = 'Y' and sr.studentInfo.studyNo = studentInfo.studyNo )");
			} else if ("N".equals(condition.get("degreeEnglish"))) {
				hql.append(" and not exists (from "+StateExamResults.class.getSimpleName()+" sr where sr.isDeleted = 0 and sr.isIdented = 'Y' and sr.studentInfo.studyNo = studentInfo.studyNo )");
			}
		}
		if(condition.containsKey("degreeApplyStatus")){//学位申请状态
			hql.append(" and degreeApplyStatus = :degreeApplyStatus ");
			values.put("degreeApplyStatus", condition.get("degreeApplyStatus"));
		}
		
		if(condition.containsKey("sbiNotNull")){
			hql.append(" and studentInfo.studentBaseInfo is not null ");
		}
		if (condition.containsKey("resourceid")) {
			hql.append(" and resourceid in(:resourceid)");
			values.put("resourceid", Arrays.asList(condition.get("resourceid").toString().split(",")));
		}
		if (condition.containsKey("orderBy")) {
			hql.append(" order by "+condition.get("orderBy"));
		}else {
			hql.append(" order by studentInfo.branchSchool.resourceid");
		}
		return values;
	}

	@Override
	public void batchCascadeDelete(String[] ids) {
//		if(ids!=null && ids.length>0){
//			for(String id : ids){
//				delete(id);	
//				logger.info("批量删除="+id);
//			}
//		}
		StringBuffer buffer=new StringBuffer();
		buffer.append("  update edu_teach_graduatedata set isdeleted=1 where resourceid in (");
		for(String id : ids){
			buffer.append("'").append(id).append("'").append(",");
		}
		buffer.delete(buffer.length()-1, buffer.length());
		buffer.append(")");
		Session session=exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createSQLQuery(buffer.toString());
		query.executeUpdate();
	}
	
	@Override
	public boolean isExist(String stuNum){
		StringBuffer buff=new StringBuffer();
		buff.append("select gra.resourceid from edu_teach_graduatedata gra"+
					" left join edu_roll_studentinfo stu on stu.resourceid=gra.studentid "+
					" where stu.studyNo='"+stuNum+"'");
		Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.SQLQuery query = session.createSQLQuery(buff.toString()) ;
		return ExCollectionUtils.isNotEmpty(query.list())?false:true;
	}

	@Override
	public List<GraduateData> findByHql(Map<String,Object> condition){
		Map<String, Object> values =  new HashMap<String, Object>(50);
		StringBuilder hql = new StringBuilder(1024);
		hql.append(" from "+GraduateData.class.getSimpleName()+" where isDeleted =0 ");
		values = getValuesByCondition(hql,condition);
		return findByHql(hql.toString(),values);
	}
	
	@Override
	public GraduateData findByHql(StudentInfo studentInfo){
		return findByStudentId(studentInfo.getResourceid());
	}
	
	@Override
	public StudentMakeupList findByHql1(StudentInfo studentInfo){
		StringBuffer hql = new StringBuffer(500);
		hql.append(" from "+StudentMakeupList.class.getSimpleName()+" where 1=1 and isDeleted =0 ")
		.append(" and studentInfo.resourceid ='"+studentInfo.getResourceid()+"'");
		
		Session session=exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createQuery(hql.toString());
		if(query.list().size()==0){
			return null;
		}else{
			return (StudentMakeupList) query.list().get(0);
		}
	}

	@Override
	public StudentMakeupList findByHql2(String studentid) {
		StringBuffer hql = new StringBuffer(500);
		hql.append(" from "+StudentMakeupList.class.getSimpleName()+" where 1=1 and isDeleted =0 ")
		.append(" and studentInfo.resourceid ='"+studentid+"'");
		
		Session session=exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createQuery(hql.toString());
		if(query.list().size()==0){
			return null;
		}else{
			return (StudentMakeupList) query.list().get(0);
		}
	}

	/**
	 * 根据学生ID获取毕业数据
	 * @param studentId
	 * @return
	 */
	@Override
	public GraduateData findByStudentId(String studentId) {
		StringBuilder hql = new StringBuilder(1000);
		hql.append(" from "+GraduateData.class.getSimpleName()+" where 1=1 ")
		.append(" and isDeleted =0  and studentInfo.resourceid ='")
		.append(studentId).append("'");
		
		Session session=exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createQuery(hql.toString());
		if(query.list().size()==0){
			return null;
		}else{
			return (GraduateData) query.list().get(0);
		}
	}

	/**
	 * 设置学位申请状态
	 * 
	 * @param resourceids
	 * @param degreeApplyStatus
	 * @throws Exception 
	 */
	@Override
	public void setDegreeApplyStatus(String resourceids, String degreeApplyStatus) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("degreeApplyStatus", degreeApplyStatus);
		
		StringBuffer sql = new StringBuffer(300);
		sql.append("update EDU_TEACH_GRADUATEDATA set DEGREEAPPLYSTATUS=:degreeApplyStatus where ")
		.append("DEGREEAPPLYSTATUS='applying' and RESOURCEID in ('")
		.append(resourceids).append("')");
		
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql.toString(), params);
	}

	/**
	 * 批量修改毕业生的学位申请状态
	 * @param graduateDataIds
	 * @return
	 * @throws Exception 
	 */
	@Override
	public int batchUpdateApplyStatus(String graduateDataIds) throws Exception {
		StringBuffer updateSql = new StringBuffer(400);
		updateSql.append("update edu_teach_graduatedata gd set gd.degreeapplystatus='applying',gd.degreeapplydate=sysdate where gd.degreestarus='Y' ")
		.append(" and gd.degreeapplystatus is null and gd.resourceid in ('").append(graduateDataIds).append("') ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(updateSql.toString(), null);
	}
}
