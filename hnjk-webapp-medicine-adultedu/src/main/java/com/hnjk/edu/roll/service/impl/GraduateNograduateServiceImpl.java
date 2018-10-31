package com.hnjk.edu.roll.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.model.GraduateNograduate;
import com.hnjk.edu.roll.model.GraduateNograduateSetting;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateNoSettingService;
import com.hnjk.edu.roll.service.IGraduateNograduateService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.security.model.User;

@Transactional
@Service("graduatenograduateservice")
public class GraduateNograduateServiceImpl extends BaseServiceImpl<GraduateNograduate> implements IGraduateNograduateService {
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	@Autowired
	@Qualifier("graduateNoSettingService")
	private IGraduateNoSettingService graduateNoSettingService;
	
	@Override
	public Page findGraduateByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+GraduateNograduate.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("branchSchool")){
			hql += " and studentInfo.branchSchool.resourceid = :resourceid ";
			values.put("resourceid", condition.get("branchSchool"));
		}
		if(condition.containsKey("grade")){
			hql += " and studentInfo.grade.resourceid = :grade ";
			values.put("grade", condition.get("grade"));
		}
		if(condition.containsKey("major")){
			hql += " and studentInfo.major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("name")){
			hql += " and studentInfo.studentName like :name ";
			values.put("name", condition.get("name")+"%");
		}
		if(condition.containsKey("studyNo")){
			hql += " and studentInfo.studyNo = :studyNo ";
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("classic")){
			hql += " and studentInfo.classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("applyNoGraduationPlan_sel")){
			hql += " and graduateNograduateSetting.resourceid = :applyNoGraduationPlan ";
			values.put("applyNoGraduationPlan", condition.get("applyNoGraduationPlan_sel"));
		}
		if(condition.containsKey("active")&&condition.containsKey("currentDate")){
			if("1".equals(condition.get("active"))){
				hql += " and (studentInfo.isApplyGraduate = 'W' and :currentDate between graduateNograduateSetting.endDate and graduateNograduateSetting.revokeDate  )";
				values.put("currentDate", condition.get("currentDate"));
			}
			
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	public List<GraduateNograduate> findGraduateByCondition_List(Map<String, Object> condition, Page objPage) {
		StringBuffer hql = new StringBuffer();
		hql.append( " from "+GraduateNograduate.class.getSimpleName()+" where 1=1 ");
		hql.append( " and isDeleted = 0 " );
		if(condition.containsKey("branchSchool")){
			hql.append(  " and studentInfo.branchSchool.resourceid = :resourceid ");
		}
		if(condition.containsKey("grade")){
			hql.append(  " and studentInfo.grade.resourceid = :grade ");
		}
		if(condition.containsKey("major")){
			hql.append(  " and studentInfo.major.resourceid = :major ");
		}
		if(condition.containsKey("name")){
			hql.append(  " and studentInfo.studentName like :name ");
		}
		if(condition.containsKey("studyNo")){
			hql.append(  " and studentInfo.studyNo = :studyNo ");
		}
		if(condition.containsKey("classic")){
			hql.append(  " and studentInfo.classic.resourceid = :classic ");
		}
		if(condition.containsKey("applyNoGraduationPlan")){
			hql.append(" and graduateNograduateSetting.resourceid = :applyNoGraduationPlan ");
		}
		if(condition.containsKey("active")&&condition.containsKey("currentDate")){
			if("1".equals(condition.get("active"))){
				hql.append(" and (studentInfo.isApplyGraduate = 'W' and :currentDate between graduateNograduateSetting.endDate and graduateNograduateSetting.revokeDate  )" );
			}
			
		}
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder() );
		
		Query query = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createQuery(hql.toString());
		
		if(condition.containsKey("branchSchool")){	
			query.setParameter("resourceid", condition.get("branchSchool"));
		}
		if(condition.containsKey("grade")){
			query.setParameter("grade", condition.get("grade"));
		}
		if(condition.containsKey("major")){
			query.setParameter("major", condition.get("major"));
		}
		if(condition.containsKey("name")){
			query.setParameter("name", condition.get("name")+"%");
		}
		if(condition.containsKey("studyNo")){	
			query.setParameter("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("classic")){
			query.setParameter("classic", condition.get("classic"));
		}
		if(condition.containsKey("applyNoGraduationPlan")){
			query.setParameter("applyNoGraduationPlan", condition.get("applyNoGraduationPlan"));
		}
		if(condition.containsKey("active")&&condition.containsKey("currentDate")){
			if("1".equals(condition.get("active"))){
				query.setParameter("currentDate", condition.get("currentDate"));
			}
		}
		
		
		
		List<GraduateNograduate> list = query.list();
		return list;
		
		
	}
	
	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}

	@Override
	public void audit(String resourceid, User user) {
		GraduateNograduate g  = get(resourceid);
		if(null == g.getIsPass() || !"Y".equals(g.getIsPass())){
			g.setIsPass("Y");
			g.setAuditManId(user.getResourceid());
			g.setAuditMan(user.getCnName());
		}
	}

	@Override
	public void batchAudit(String[] ids, User user) {
		for(String id : ids){
			audit(id, user);
		}
	}
	
	/**
	 * 批量生效的延迟毕业申请
	 */
	@Override
	public void batchNoGraduate() {	
		//当前日期
		Date currentDate = new Date(new java.util.Date(System.currentTimeMillis()).getTime());
		String parttern="yyyy-MM-dd";
		String currentdate=new SimpleDateFormat(parttern).format(currentDate); 
		//所有的延迟毕业申请时间段
		List<GraduateNograduateSetting> settings = graduateNoSettingService.getAll(); 
		//如果当前时间等于结束时间，批量设置
		try{
			for (GraduateNograduateSetting setting : settings) {
				Date endDate = new Date(setting.getEndDate().getTime());
				String enddate=new SimpleDateFormat(parttern).format(endDate); 
				if(enddate.equals(currentdate)){
					String settingid = setting.getResourceid();
					List<GraduateNograduate> noGraduateApplys =  this.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and  graduateNograduateSetting.resourceid = '"+settingid+"' ");
					for (GraduateNograduate graduateNograduate : noGraduateApplys) {
						StudentInfo info = graduateNograduate.getStudentInfo();
						info.setIsApplyGraduate(Constants.BOOLEAN_WAIT);
						studentInfoService.saveOrUpdate(info);
					}
				}
			}
		}catch (Exception e) {
			logger.error("批量生效的延迟毕业申请出错:{}",e.fillInStackTrace());
		}
	}
	/**
	 * 批量恢复的延迟毕业申请
	 */
	@Override
	public void batchRevoke() {
		//当前日期
		Date currentDate = new Date(new java.util.Date(System.currentTimeMillis()).getTime());
		String parttern="yyyy-MM-dd";
		String currentdate=new SimpleDateFormat(parttern).format(currentDate); 
		//所有的延迟毕业申请时间段
		List<GraduateNograduateSetting> settings = graduateNoSettingService.getAll(); 
		//如果当前时间等于自动恢复时间，批量恢复
		try{
			for (GraduateNograduateSetting setting : settings) {
				Date revokeDate = new Date(setting.getRevokeDate().getTime());
				String revokedate=new SimpleDateFormat(parttern).format(revokeDate); 
				if(revokedate.equals(currentdate)){
					String settingid = setting.getResourceid();
					//专科
					List<GraduateNograduate> noGraduateApplys =  this.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and graduateNograduateSetting.resourceid = '"+settingid+"' " +
							" and studentInfo.classic.classicCode ='3' ");
					for (GraduateNograduate graduateNograduate : noGraduateApplys) {
						StudentInfo info = graduateNograduate.getStudentInfo();
						info.setIsApplyGraduate(Constants.BOOLEAN_NO);
						studentInfoService.saveOrUpdate(info);
					}
					//专科以上
					noGraduateApplys =  this.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and graduateNograduateSetting.resourceid = '"+settingid+"' " +
							" and studentInfo.classic.classicCode !='3' ");
					for (GraduateNograduate graduateNograduate : noGraduateApplys) {
						StudentInfo info = graduateNograduate.getStudentInfo();
						info.setIsApplyGraduate(Constants.BOOLEAN_YES);
						studentInfoService.saveOrUpdate(info);
						
					}
				}
			}
		
		}catch (Exception e) {
			logger.error("批量恢复的延迟毕业申请出错:{}",e.fillInStackTrace());	
		}	
	}
}
