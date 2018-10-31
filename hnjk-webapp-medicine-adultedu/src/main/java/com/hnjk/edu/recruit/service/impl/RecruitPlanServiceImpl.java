package com.hnjk.edu.recruit.service.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.ExportRecruitPlan;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;


/**
 * 招生计划服务实现<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-10 上午11:03:57
 * @see 
 * @version 1.0
*/
@Service("recruitPlanService")
@Transactional
public class RecruitPlanServiceImpl extends BaseServiceImpl<RecruitPlan>
                  implements IRecruitPlanService{
	/**
	 *  根据条件查找招生计划-返回分页对象
	 */
	@Override
	public Page findPlanByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		
		StringBuffer hql  = new StringBuffer(" from "+RecruitPlan.class.getSimpleName()+" plan where plan.isDeleted=0 ");
		if(condition.containsKey("recruitPlanname")){//招生计划名称
			hql.append(" and plan.recruitPlanname like '%"+condition.get("recruitPlanname")+"%'");
		}
		if (condition.containsKey("recruitPlan")){
			hql.append(" and plan.resourceid=:recruitPlan");
		}
		if (condition.containsKey("startDate")) {
			hql.append(" and plan.startDate >= to_date('"+condition.get("startDate")+"','yyyy-MM-dd')");
		}
		if (condition.containsKey("endDate")) {
			hql.append(" and plan.endDate <=  to_date('"+condition.get("endDate")+"','yyyy-MM-dd')");
		}
		if (condition.containsKey("publishDate")) {
			hql.append(" and plan.publishDate <= to_date('"+condition.get("publishDate")+"','yyyy-MM-dd')");
		}
		if (condition.containsKey("isPublished")) {
			hql.append(" and plan.isPublished=:isPublished");
		}
		if (condition.containsKey("grade")) {
			hql.append(" and plan.grade.resourceid=:grade");
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and plan.teachingType=:teachingType");
		}
		
		hql.append(" order by plan."+page.getOrderBy() +" "+page.getOrder() );
		return exGeneralHibernateDao.findByHql(page, hql.toString(), condition);

	}
	/**
	 *  根据条件查找招生计划-返回列表对象
	 */
	@Override
	public List<RecruitPlan> findPlanByCondition(Map<String, Object> condition)throws ServiceException {
		StringBuffer hql  = new StringBuffer(" from "+RecruitPlan.class.getSimpleName()+" plan where plan.isDeleted=0 ");
		if(condition.containsKey("recruitPlanname")){//招生计划名称
			hql.append(" and plan.recruitPlanname like '%"+condition.get("recruitPlanname")+"%'");
		}
		if(condition.containsKey("planname")){//招生计划名称
			hql.append(" and trim(plan.recruitPlanname) = :planname");
		}
		if (condition.containsKey("recruitPlan")){
			hql.append(" and plan.resourceid=:recruitPlan");
		}
		if (condition.containsKey("startDate")) {
			hql.append(" and plan.startDate >= to_date('"+condition.get("startDate")+"','yyyy-MM-dd')");
		}
		if (condition.containsKey("endDate")) {
			hql.append(" and plan.endDate <=  to_date('"+condition.get("endDate")+"','yyyy-MM-dd')");
		}
		if (condition.containsKey("isPublished")) {
			hql.append(" and plan.isPublished=:isPublished");
		}
		if (condition.containsKey("grade")) {
			hql.append(" and plan.grade.resourceid=:grade");
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and plan.teachingType=:teachingType");
		}
		return (List<RecruitPlan>) exGeneralHibernateDao.findByHql(hql.toString(), condition);
	}
	@Override
	public RecruitPlan getLastPlan() throws ServiceException {
		List<RecruitPlan> list = this.findByCriteria(
				RecruitPlan.class, new Criterion[] { Restrictions.eq("isDeleted", 0), Restrictions.le("startDate", ExDateUtils.getCurrentDateTime()) }, Order.desc("publishDate"));
		RecruitPlan plan = null;
		if (list.size() > 0) {
			plan = list.get(0);
		} else {
			throw new WebException("当前时间没有可用的招生计划");
		}
		return plan;
	}

	@Override
	public RecruitPlan getOnePublishedPlan() throws ServiceException {
		String hql= "from RecruitPlan plan where plan.isPublished='Y' order by plan.publishDate desc";
		List<RecruitPlan> list = (List<RecruitPlan>) exGeneralHibernateDao.findByHql(hql);
		if(list!=null&& !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<RecruitPlan>  getPublishedPlanList(String type) throws Exception {
		
		Date curDate = new Date();
		String hql = "from "+RecruitPlan.class.getSimpleName()+" plan where plan.isDeleted=0 and plan.isPublished='Y' ";
		if ("forStu".equals(type)) {
			 hql+= " and to_date('"+ExDateUtils.formatDateStr(curDate, "yyyy-MM-dd") +"','yyyy-MM-dd')  >= plan.startDate ";
			 hql+= " and to_date('"+ExDateUtils.formatDateStr(curDate, "yyyy-MM-dd") +"','yyyy-MM-dd')  <= plan.endDate ";
		}else if("forManager".equals(type)){
			 hql+= " and to_date('"+ExDateUtils.formatDateStr(curDate, "yyyy-MM-dd") +"','yyyy-MM-dd')  <= plan.brSchoolApplyCloseTime ";
		}else if("fromWeb".equals(type)){//网上报名，过滤掉报名截止的批次\特批
			hql+= " and to_date('"+ExDateUtils.formatDateStr(curDate, "yyyy-MM-dd") +"','yyyy-MM-dd') <= plan.endDate and plan.isSpecial != 'Y' ";
		}
		hql += " order by plan.recruitPlanname desc";	  
		
		return (List<RecruitPlan>) exGeneralHibernateDao.findByHql(hql);
	}

/*	public List<RecruitPlan> getPublishedBrschoolPlanList(String brSchoolId) throws Exception {
		
		Date curDate = new Date();
		String hql ="from RecruitPlan plan where plan.isDeleted=0 and plan.isPublished='Y' and plan.orgUnit.resourceid=?";
			   hql+= " and to_date('"+ExDateUtils.formatDateStr(curDate, "yyyy-MM-dd") +"','yyyy-MM-dd')  >= plan.startDate ";
			   hql+= " and to_date('"+ExDateUtils.formatDateStr(curDate, "yyyy-MM-dd") +"','yyyy-MM-dd')  <= plan.endDate ";
			   
		return (List<RecruitPlan>) exGeneralHibernateDao.findByHql(hql,brSchoolId);
	}*/

	@Override
	public List<ExportRecruitPlan> exportList(Map<String,Object> condition){
	//	excel  exportRecruitPlan  existRealityEnrollExcel
		//表一.试点高校网络教育招生计划备案表（招生办）.xls
		if(condition!=null&&"exportRecruitPlan".equals(condition.get("excel"))){
			return exportRecruitPlan( condition);
		}
		//表二.试点高校网络教育实际录取情况表（学籍办）.xls  list方法
		else if(condition!=null&&"existRealityEnrollExcel".equals(condition.get("excel"))){
			return existRealityEnrollExcel( condition);
		}
		//表四.试点高校本年度计划招生的校外学习中心备案表（招生办）.xls
		else if(condition!=null&&"exportRecruitForYear".equals(condition.get("excel"))){
			return exportRecruitForYear(condition);
		}
		return null;
	}
	
	
	/**
	 * 表一.试点高校网络教育招生计划备案表（招生办）.xls  list方法
	 * @return
	 */
	private List<ExportRecruitPlan> exportRecruitPlan(Map<String,Object> map){
		List<ExportRecruitPlan> list=new ArrayList<ExportRecruitPlan>();
		StringBuffer sql=new StringBuffer();
		
		sql.append("select plan.recruitplanname ,cla.classicCode,major.majorNationCode,major.majorName,maj.limitNum,fee.totalFee,unit.localCity"+
				   "  from edu_base_grade grade, edu_recruit_recruitplan plan,"+
				   "    (select majorid,recruitplanid,recruitMajorName,classic,sum(limitNum)as limitNum from  edu_recruit_major group by majorId,classic,recruitMajorName,majorid,recruitplanid) maj "+
				   "  ,edu_base_major major,  edu_recruit_brschplan brplan,hnjk_sys_unit unit,edu_base_classic cla ,edu_roll_feerule fee "+
				   " where plan.gradeid = grade.resourceid "+
				   "   and maj.recruitplanid   = plan.resourceid "+
				   "   and maj.majorid = major.resourceid "+
				   "   and brplan.recruitplanid = plan.resourceid and brplan.isdeleted='0'"+
				   "   and brplan.branchschoolid = unit.resourceid "+
				   "   and cla.resourceid=maj.classic "+
				   "   and  fee.majorId=major.resourceid and fee.classicId=cla.resourceid  "+
				   "   and brplan.isaudited = 'Y'");
		Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createSQLQuery(sql.toString());
		List st=query.list();
		for(int i=0;i<st.size();i++){
			Object[] obj=(Object[])st.get(i);
			ExportRecruitPlan ep=new ExportRecruitPlan();
			ep.setRecruitPlanname(obj[0]!=null?obj[0].toString():"");
			ep.setClassicName(obj[1]!=null?obj[1].toString():"");
			ep.setMajorNationCode(obj[2]!=null?obj[2].toString():"");
			ep.setMajorName(obj[3]!=null?obj[3].toString():"");
			ep.setLimitNum(obj[4]!=null?obj[4].toString():"");
			ep.setTotalFee(obj[5]!=null?obj[5].toString():"");
			ep.setStudentOrigin(obj[6]!=null?obj[6].toString():"");
			list.add(ep);
		}
		ExportRecruitPlan rPln=new ExportRecruitPlan("/记录行结束/","","","","","","","","");//“/记录行结束/” 为数据行结束标志，请勿删除
		list.add(rPln);
		return list;
	}

	
	/**
	 * 表二.试点高校网络教育实际录取情况表（学籍办）.xls  list方法
	 * @return
	 */
	private List<ExportRecruitPlan> existRealityEnrollExcel(Map<String,Object> map){
		List<ExportRecruitPlan> list=new ArrayList<ExportRecruitPlan>();
		StringBuffer sql=new StringBuffer();
		sql.append("select  ljo.recruitplanname,cla.classicCode,bmajor.majorNationCode,bmajor.majorName,a.cout , "+
				   " ljo.localCity from "+ 
				   " (select resourceid, majorid,recruitplanid,recruitMajorName,classic,sum(limitNum)as limitNum,isdeleted from  "+
				   " edu_recruit_major group by resourceid,majorId,classic,recruitMajorName,majorid,recruitplanid,isdeleted) maj "+
				   " inner join  "+
				   " (select count (studentBaseInfoId) as cout,recruitMajorId from edu_recruit_enrolleeInfo  "+
				   " group by recruitMajorId ,isMatriculate having isMatriculate='Y') a  on a.recruitMajorId=maj.resourceid "+
				   " left join edu_base_classic cla on   cla.resourceid=maj.classic "+
				   " left join edu_base_major bmajor on bmajor.resourceid=maj.majorid "+
				   " inner join ( "+
				   " select unit.localCity ,plan.resourceid as pid ,grade.resourceid as gid,plan.recruitplanname from edu_recruit_recruitplan plan "+
				   " inner join edu_base_grade grade on grade.resourceid=plan.gradeId "+
    
				   " inner join edu_recruit_brschplan brplan on brplan.recruitplanId=plan.resourceid   "+
    
				   " inner join hnjk_sys_unit unit on brplan.branchSchoolId = unit.resourceid and brplan.isaudited = 'Y' "+
				   " ) ljo on ljo.pid=maj.recruitplanId    where maj.isdeleted='0'");
		Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createSQLQuery(sql.toString());
		List st=query.list();
		for(int i=0;i<st.size();i++){
			Object[] obj=(Object[])st.get(i);
			ExportRecruitPlan ep=new ExportRecruitPlan();
			ep.setRecruitPlanname(obj[0]!=null?obj[0].toString():"");
			ep.setClassicName(obj[1]!=null?obj[1].toString():"");
			ep.setMajorNationCode(obj[2]!=null?obj[2].toString():"");
			ep.setMajorName(obj[3]!=null?obj[3].toString():"");
			ep.setCout(obj[4]!=null?obj[4].toString():"0");
			ep.setStudentOrigin(obj[5]!=null?obj[5].toString():"");
			list.add(ep);
		}
		ExportRecruitPlan rPln=new ExportRecruitPlan("/记录行结束/","","","","","","","","");//“/记录行结束/” 为数据行结束标志，请勿删除
		list.add(rPln);
		return list;
	}
	
	//
	private List<ExportRecruitPlan> exportRecruitForYear(Map<String,Object> map){
		List<ExportRecruitPlan> list=new ArrayList<ExportRecruitPlan>();
		StringBuffer sql=new StringBuffer();
		
		sql.append("select unit.unitCode,unit.unitName,unit.localCity,unit.address,unit.principal,unit.linkman , "+
				   " unit.contectCall,unit.email,bplan.fillinDate, bplan.documentCode from edu_recruit_brschplan bplan "+
				   " left join edu_recruit_recruitplan plan on plan.resourceid=bplan.recruitplanid  "+ 
				   " left join edu_base_grade grade on  grade.resourceid = plan.gradeId "+
				   " left join hnjk_sys_unit unit   on  unit.resourceid  = bplan.branchSchoolId "+
				   " left join edu_base_year year   on  year.resourceid  = plan.yearId "+
				   " WHERE bplan.isAudited = 'Y' and bplan.isDeleted='0' ");
		if(map.containsKey("yearRresourceid")){
			sql.append("and year.Resourceid='"+map.get("yearRresourceid")+"'");
		}
		Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createSQLQuery(sql.toString());
		List st=query.list();
		for(int i=0;i<st.size();i++){
			Object[] obj=(Object[])st.get(i);
			ExportRecruitPlan ep=new ExportRecruitPlan();
			ep.setSchoolCode(obj[0]!=null?obj[0].toString():"");
			ep.setSchoolName(obj[1]!=null?obj[1].toString():"");
			ep.setDistrict(obj[2]!=null?obj[2].toString():"");
			ep.setAddress(obj[3]!=null?obj[3].toString():"");
			ep.setPrincipal(obj[4]!=null?obj[4].toString():"");
			ep.setLimitNum(obj[5]!=null?obj[5].toString():"");
			ep.setContectCall(obj[6]!=null?obj[6].toString():"");
			ep.setEmail(obj[7]!=null?obj[7].toString():"");
			ep.setCheckDate(obj[8]!=null?obj[8].toString():"");
			ep.setDocumentCode(obj[9]!=null?obj[9].toString():"");
			list.add(ep);
		}
		ExportRecruitPlan rPln=new ExportRecruitPlan("/记录行结束/");//“/记录行结束/” 为数据行结束标志，请勿删除
		list.add(rPln);
		return list;
	}


	/**
	 * 获取招生批次的教学模式，返回JSON集合
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<JsonModel> getTeachingTypeJsonList(String planId)throws ServiceException {
		List<JsonModel>  modelList   = new ArrayList<JsonModel>();
		RecruitPlan plan         	 = this.load(planId);
		String planType       	 	 = plan.getTeachingType();
		List<Dictionary> types	 	 = CacheAppManager.getChildren("CodeTeachingType");
		 
		for (Dictionary dic:types) {
			if (ExStringUtils.isNotEmpty(planType)&&planType.indexOf(dic.getDictValue())<0) {
				continue;
			}else {
				JsonModel m  	  = new JsonModel();
				m.setKey(dic.getDictValue());
				m.setValue(dic.getDictName());
				
				modelList.add(m);
			}
		}

		return modelList;
	}



	
}
