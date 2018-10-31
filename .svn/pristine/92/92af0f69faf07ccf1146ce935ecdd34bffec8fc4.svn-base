package com.hnjk.edu.recruit.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolMajorService;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;


/**
 * 招生专业服务实现<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-11 上午09:35:44
 * @see 
 * @version 1.0
*/
@Service("recruitMajorService")
@Transactional
public class RecruitMajorServiceImpl extends BaseServiceImpl<RecruitMajor>
                  implements IRecruitMajorService{

//	@Autowired
//	@Qualifier("examSubjectSetService")
//	private IExamSubjectSetService examSubjectSetService;
//	
//
//	@Autowired
//	@Qualifier("examSubjectService")
//	private IExamSubjectService examSubjectService;
	
	@Autowired
	@Qualifier("branchSchoolMajorService")
	private IBranchSchoolMajorService branchSchoolMajorService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;

	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	
	@Override
	public Page findMajorByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		//所属招生计划
		objCriterion.add(Restrictions.eq("recruitPlan.resourceid", condition.get("planid")));
		//招生专业名称
		if(condition.containsKey("recruitMajorName")){
			objCriterion.add(Restrictions.like("recruitMajorName","%"+condition.get("recruitMajorName")+"%"));
		}
		if(condition.containsKey("brSchoolid")){
			objCriterion.add(Restrictions.eq("brSchool.resourceid",condition.get("brSchoolid")));
		}
		if(condition.containsKey("status")){
			objCriterion.add(Restrictions.eq("status",Long.valueOf(condition.get("status").toString())));			
		}
		//层次
		if (condition.containsKey("classic")) {
			String classic=condition.get("classic").toString();
			objCriterion.add(Restrictions.eq("classic.resourceid",classic));
		}
		if (condition.containsKey("major")) {
			objCriterion.add(Restrictions.eq("major.resourceid",condition.get("major")));
		}
		if (condition.containsKey("teachingType")) {
			objCriterion.add(Restrictions.eq("teachingType",condition.get("teachingType")));
		}
		
		
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		objCriterion.add(Restrictions.isNotNull("brSchool"));
		return exGeneralHibernateDao.findByCriteria(RecruitMajor.class,page,objCriterion.toArray(new Criterion[objCriterion.size()]));
	}

	@Override
	public Page getUnAssignMajorList(Page page,Map<String, Object> condition) throws ServiceException {
		String hql ="from RecruitMajor rm where rm.isDeleted='0'"
			         +" and rm.resourceid not in ( "
			         +" select es.recruitMajor.resourceid from ExamSubject es"
			         +" where es.isDeleted='0')";
		if(condition.containsKey("recruitPlan"))
		{
			hql+=" and rm.recruitPlan.resourceid='"+condition.get("recruitPlan")+"'";
		}
		if(condition.containsKey("classic"))
		{
			hql+=" and rm.classic.resourceid='"+condition.get("classic")+"'";
		}
		System.out.println(hql);
		return exGeneralHibernateDao.findByHql(page, hql, condition);
	}

	@Override
	public Page getRecruitMajorList(Page page, Map<String, Object> condition)
			throws ServiceException {
		String hql =" from RecruitMajor rm where rm.isDeleted='0' "
			       +" and rm.recruitPlan.resourceid= :recruitPlan "
			       +" and rm.resourceid not in ( "
			       +" select esm.recruitMajor.resourceid from BranchSchoolMajor esm "
			       +" where esm.isDeleted='0'  and  esm.branchSchool.resourceid= :branchSchool)";
		return exGeneralHibernateDao.findByHql(page, hql, condition);
	}
	/**
	 * 根据校外学习中心限定的专业Id及招生批次ID查找招生专业
	 */
	@Override
	public List<RecruitMajor> findBranSchoolLimitRecruitMajorListByPlanId(Map<String,Object> condition) throws ServiceException {
		Map<String,Object> paraMap = new HashMap<String, Object>();
		StringBuffer hql		   = new StringBuffer(" from "+RecruitMajor.class.getSimpleName()+" rm where rm.isDeleted=0 ");
		if (condition.containsKey("brSchoolLimitMajorIds")) {
			hql.append( " and rm.major.resourceid in("+condition.get("brSchoolLimitMajorIds")+")");
		}
		if (condition.containsKey("recruitPlanId")) {
			hql.append( " and rm.recruitPlan.resourceid=:recruitPlanId");
			paraMap.put("recruitPlanId", condition.get("recruitPlanId"));
		}
		hql.append(" and not exists ( select brm.resourceid from "+BranchSchoolMajor.class.getSimpleName());
		hql.append(" brm where brm.isDeleted=0 and brm.branchSchoolPlan.isDeleted=0 and brm.branchSchoolPlan.isAudited='Y' ");
		if (condition.containsKey("recruitPlanId")) {
			hql.append(" and brm.branchSchoolPlan.recruitplan.resourceid=:recruitPlanId");
			paraMap.put("recruitPlanId", condition.get("recruitPlanId"));
		}
		if (condition.containsKey("brschoolId")) {
			hql.append(" and brm.branchSchoolPlan.branchSchool.resourceid=:brschoolId");
			paraMap.put("brschoolId", condition.get("brschoolId"));
		}
		hql.append(" and brm.recruitMajor.resourceid=rm.resourceid )");
		
		return (List<RecruitMajor>) this.exGeneralHibernateDao.findByHql(hql.toString(),paraMap);
	}
	/**
	 * 根据条件查找招生专业-返回LIST
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<RecruitMajor> findMajorByCondition(Map<String, Object> condition)throws ServiceException {
		StringBuffer hql 		     = new StringBuffer();
		Map<String, Object>  paraMap = new HashMap<String, Object>();
		hql.append(" from "+RecruitMajor.class.getSimpleName()+" major where major.isDeleted= :isDeleted");
		hql.append(" and major.recruitPlan.isDeleted=:isDeleted");
		paraMap.put("isDeleted", 0);
		if (condition.containsKey("recruitPlan")) {
			hql.append(" and major.recruitPlan.resourceid=:recruitPlan");
			paraMap.put("recruitPlan", condition.get("recruitPlan"));
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and major.teachingType=:teachingType");
			paraMap.put("teachingType", condition.get("teachingType"));
		}
		if (condition.containsKey("major")) {
			hql.append(" and major.major.resourceid=:major");
			paraMap.put("major", condition.get("major"));
		}else if (condition.containsKey("majorid")) {
			hql.append(" and major.major.resourceid=:majorid");
			paraMap.put("majorid", condition.get("majorid"));
		}
		if (condition.containsKey("classic")) {
			hql.append(" and major.classic.resourceid=:classic");
			paraMap.put("classic", condition.get("classic"));
		}else if (condition.containsKey("classicId")) {
			hql.append(" and major.classic.resourceid=:classicId");
			paraMap.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("branchSchool")){
			hql.append(" and major.brSchool.resourceid=:branchSchool");
			paraMap.put("branchSchool", condition.get("branchSchool"));
		}else if (condition.containsKey("branchSchoolId")) {
			hql.append(" and major.brSchool.resourceid=:branchSchoolId");
			paraMap.put("branchSchoolId", condition.get("branchSchoolId"));
		}
		if(condition.containsKey("grade")){
			hql.append(" and major.recruitPlan.grade.resourceid=:grade");
			paraMap.put("grade", condition.get("grade"));
		}else if (condition.containsKey("gradeid")) {
			hql.append(" and major.recruitPlan.grade.resourceid=:gradeid");
			paraMap.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("yearInfo")){
			hql.append(" and major.recruitPlan.yearInfo.resourceid=:yearInfo");
			paraMap.put("yearInfo", condition.get("yearInfo"));
		}
		if(condition.containsKey("recruitMajorCode")){
			hql.append(" and major.recruitMajorCode=:recruitMajorCode");
			paraMap.put("recruitMajorCode", condition.get("recruitMajorCode"));
		}
		hql.append(" order by major.recruitMajorName");
		return findByHql(hql.toString(),paraMap);
	}


	@Override
	public List<JsonModel> getRecruitMajorJsonList(Map<String, Object> condition)throws ServiceException {
		
		List<JsonModel>  modelList   = new ArrayList<JsonModel>();
		Map<String,Object> paramMap  = new HashMap<String, Object>();
		StringBuffer hql 			 = new StringBuffer();
		hql.append(" from "+RecruitMajor.class.getSimpleName()+" major where major.isDeleted = :isDeleted ");
		paramMap.put("isDeleted", 0);
		if (condition.containsKey("recruitPlan")) {//招生批次或批次所在年度 by hzg
			hql.append(" and (major.recruitPlan.resourceid=:recruitPlan or major.recruitPlan.yearInfo.resourceid=:recruitPlan)");
			paramMap.put("recruitPlan", condition.get("recruitPlan"));
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and major.teachingType=:teachingType");
			paramMap.put("teachingType", condition.get("teachingType"));
		}
		hql.append(" order by major.recruitMajorName");
		List<RecruitMajor> list 	 = findByHql(hql.toString(),paramMap);
		if (null!=list && !list.isEmpty()) {	
			for (RecruitMajor major:list) {
				
				JsonModel jsonModel  = new JsonModel();
				jsonModel.setKey(major.getResourceid());
				jsonModel.setValue(major.getRecruitMajorName());
				if (major.getRecruitMajorName().length()>9) {
					jsonModel.setName(major.getRecruitMajorName().substring(0, 9)+"...");
				}else {
					jsonModel.setName(major.getRecruitMajorName());
				}
				modelList.add(jsonModel);
			}
		}
		return modelList;
	}
	
	/**
	 * 校外学习中心申报新专业成功后转后校外学习中心招生专业
	 * @throws ServiceException
	 */	
	@Override
	public void branSchoolNewMajorIntoRecruitMajor(Map<String,Object> map) throws ServiceException {
		
		Map<String,Object> paraMap     = new HashMap<String, Object>();
	
		String memo                    = (String)   	   map.get("memo");
		Date curDate				   = (Date)  		   map.get("curDate");
		String limitNum                = (String)   	   map.get("limitNum");
		String lowerNum                = (String)   	   map.get("lowerNum");
		String studyperiod             = (String)   	   map.get("studyperiod");
		//String examSubjectSet          = (String)   	   map.get("examSubjectSet");
		Long brSchLimitNumL            = (Long)     	   map.get("brSchLimitNumL");
		Long brSchLowerNumL            = (Long)     	   map.get("brSchLowerNumL");
		String [] teachingTypes        = (String[]) 	   map.get("teachingTypes");
		
		User user                      = (User)            map.get("user");
		OrgUnit uint                   = (OrgUnit)         map.get("uint");
		Classic classic    			   = (Classic)         map.get("baseClassic");
		Major major                    = (Major)           map.get("baseMajor");
		BranchSchoolPlan   brSchPlan   = (BranchSchoolPlan)map.get("brSchPlan");
		RecruitPlan recruitPlan        = (RecruitPlan)	   map.get("recruitPlan");
		
		paraMap.put("major", major.getResourceid());
		paraMap.put("classic", classic.getResourceid());
		paraMap.put("isAudited", Constants.BOOLEAN_YES);
		paraMap.put("branchSchool", uint.getResourceid());
		paraMap.put("recruitPlan", recruitPlan.getResourceid());

		for (int j = 0; j < teachingTypes.length; j++) {
			
			String teachingType        = teachingTypes[j];
			paraMap.put("teachingType", teachingType);
		
			List<RecruitMajor> rmjList = this.findMajorByCondition(paraMap);
			//如果已当前招生批次存在相同学习中心、层次、模式、专业的招生专业则只加入校外学习中习招生
			if (null!=rmjList && rmjList.size()>0) {
				
				RecruitMajor rmj 	   			 = rmjList.get(0);
				paraMap.put("recruitMajor", rmj.getResourceid());
				List<BranchSchoolMajor> brmjList = branchSchoolMajorService.getCanUseMajorList(paraMap);
				
				if (null==brmjList || brmjList.isEmpty()) {
					
					BranchSchoolMajor  brmj= new BranchSchoolMajor();  
					brmj.setBranchSchoolPlan(brSchPlan);
					brmj.setRecruitMajor(rmj);
					brmj.setLimitNum(brSchLimitNumL);
					brmj.setLowerNum(brSchLowerNumL);
					brmj.setIsPassed(Constants.BOOLEAN_YES);
					
					brSchPlan.getBranchSchoolMajor().add(brmj);
				} 
				
			}else {
				
				RecruitMajor rmj      	= new RecruitMajor();
				rmj.setClassic(classic);
				rmj.setLimitNum(Long.valueOf(limitNum));
				rmj.setLowerNum(Long.valueOf(lowerNum));
				rmj.setMajor(major);
				rmj.setMemo("由"+uint.getUnitName()+"申报新专业通过后产生。"+memo);
				rmj.setRecruitMajorName(major.getMajorName()+"_"+classic.getClassicName()+"("+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", teachingType)+")");
				rmj.setStudyperiod(Double.valueOf(studyperiod));
				rmj.setRecruitPlan(recruitPlan);
				rmj.setTeachingType(teachingType);
				rmj.setFillinDate(curDate);
				rmj.setFillinMan(user.getUsername());
				rmj.setFillinManId(user.getResourceid());
				rmj.setBrSchool(uint);
				//TODO 招生专业编码(1位学习形式3位流水号)
				rmj.setRecruitMajorCode(getRecruitMajorCode(recruitPlan, teachingType));
				
				this.save(rmj);
				recruitPlan.getRecruitMajor().add(rmj);
				
				//ExamSubjectSet set 			  = examSubjectSetService.get(examSubjectSet);
				//List<ExamSubject> subjectList = examSubjectSetService.findExamSubjectBySet(set);
																	
				//for(ExamSubject subject : subjectList){//安排入学考试科目
				//	subject.setCourseGroupName(set.getCourseGroupName());
				//	subject.setRecruitMajor(rmj);
				//	examSubjectService.save(subject);
				//}
				
				BranchSchoolMajor  brmj = new BranchSchoolMajor();  
				brmj.setBranchSchoolPlan(brSchPlan);
				brmj.setRecruitMajor(rmj);
				brmj.setLimitNum(brSchLimitNumL);
				brmj.setLowerNum(brSchLowerNumL);
				brmj.setIsPassed(Constants.BOOLEAN_YES);
				
				brSchPlan.getBranchSchoolMajor().add(brmj);
			}
			
		}
		recruitPlanService.update(recruitPlan);
		branchSchoolPlanService.update(brSchPlan);
	}
	/**
	 * 获取招生专业编码(1位学习形式3位流水号)
	 * @param recruitPlan
	 * @param teachingType
	 * @return
	 */
	private String getRecruitMajorCode(RecruitPlan recruitPlan, String teachingType){
		Long maxSuffix = 0L;//最大流水号
		for (RecruitMajor r : recruitPlan.getRecruitMajor()) {
			if(ExStringUtils.equals(r.getTeachingType(), teachingType)){
				Long curFix = Long.valueOf(ExStringUtils.substring(r.getRecruitMajorCode(), 1));
				if(curFix > maxSuffix){
					maxSuffix = curFix;
				}
			}
		}
		DecimalFormat decFormat = new DecimalFormat("000");
		return teachingType + decFormat.format(maxSuffix + 1L);
	}
	
	@Override
	public List<RecruitMajor> findByUnitid(String unitid) throws Exception{
		List<RecruitMajor> resultList = new ArrayList<RecruitMajor>();
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("from "+RecruitMajor.class.getSimpleName()+" where brSchool.resourceid=? and isDeleted=0 and status=1 ");
			resultList =this.findByHql(hql.toString(), new Object[]{unitid});
		} catch (Exception e) {
			throw new Exception("根据教学点ID查询教学点招生专业列表出错");
		}
		return resultList;
	}
}
