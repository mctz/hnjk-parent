package com.hnjk.edu.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.finance.model.StuPerpayfee;
import com.hnjk.edu.finance.model.StudentFeeRule;
import com.hnjk.edu.finance.model.StudentFeeRuleDetails;
import com.hnjk.edu.finance.service.IStudentFeeRuleService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 年级预交费用设置表.
 * <code>StudentFeeRuleServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-8-13 下午02:07:27
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentfeeruleservice")
public class StudentFeeRuleServiceImpl extends BaseServiceImpl<StudentFeeRule> implements IStudentFeeRuleService {

	@Override
	public Page findFeeRuleByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+com.hnjk.edu.finance.model.StudentFeeRule.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("grade")){
			hql += " and grade.resourceid = :grade ";
			values.put("grade", condition.get("grade").toString().trim());
		}
		if(condition.containsKey("branchSchool")){
			hql += " and branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool").toString().trim());
		}
		if(condition.containsKey("major")){
			hql += " and major.resourceid = :major ";
			values.put("major", condition.get("major").toString().trim());
		}
		if(condition.containsKey("classic")){
			hql += " and classic.resourceid = :classic ";
			values.put("classic", condition.get("classic").toString().trim());
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		List  values1 = new ArrayList();
		values1.add("11");
		exGeneralHibernateDao.findEntitysByIds(StudentFeeRule.class, values1);
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	
	
	
	@Override
	public void update(StudentFeeRule fee, HttpServletRequest request) {
		String[] feeDids = request.getParameterValues("feeDid");
		String[] feeTermNums = request.getParameterValues("feeTermNum");
		String[] feeScales = request.getParameterValues("feeScale");
		String[] showOrders = request.getParameterValues("showOrder");
		StudentFeeRuleDetails feeDetail =  null;
		if(feeDids!=null){
		for(int index=0;index<feeDids.length;index++){
			if(ExStringUtils.isNotEmpty(feeDids[index])){
				feeDetail = (StudentFeeRuleDetails) load(StudentFeeRuleDetails.class, feeDids[index]);
			}else{
				feeDetail = new StudentFeeRuleDetails();
			}
			feeDetail.setFeeTermNum(ExStringUtils.isEmpty(feeTermNums[index]) ? 0 : Integer.parseInt(feeTermNums[index]));
			feeDetail.setFeeScale(ExStringUtils.isEmpty(feeScales[index]) ? 0.0 : Double.parseDouble(feeScales[index]));
			feeDetail.setShowOrder(ExStringUtils.isEmpty(showOrders[index]) ? 0 : Integer.parseInt(showOrders[index]));
			feeDetail.setIsDeleted(0);
			feeDetail.setStudentFeeRule(fee);
			fee.addStuFeeRuleDetails(feeDetail);
		}  } //end if
		
		if(ExStringUtils.isNotEmpty(fee.getGradeid()) && ExStringUtils.isNotEmpty(fee.getBranchSchoolId()) 
				&& ExStringUtils.isNotEmpty(fee.getMajorid()) && ExStringUtils.isNotEmpty(fee.getClassicid())){
			fee.setGrade(gradeService.get(fee.getGradeid()));
			fee.setBranchSchool(orgUnitService.get(fee.getBranchSchoolId()));
			fee.setMajor(majorService.get(fee.getMajorid()));
			fee.setClassic(classicService.get(fee.getClassicid()));
		
			BigDecimal sum = AjaxSumPoint(fee.getGradeid(), fee.getMajorid(), fee.getClassicid());
			fee.setTotalFee(BigDecimalUtil.mul(sum.doubleValue(), fee.getCreditFee()));
			fee.setFeeRuleName(fee.getGrade().getGradeName()+"-"+fee.getBranchSchool().getUnitName()
					+"-"+fee.getMajor().getMajorName()+"-"+fee.getClassic().getClassicName());
		}
	}




	@Override
	public void batchCascadeDelete(String[] split) throws ServiceException {
		for(String id : split){
			StudentFeeRule studentFeeRule = get(id);
			if(Constants.BOOLEAN_YES.equals(studentFeeRule.getGeneratorFlag())){
				throw new ServiceException(studentFeeRule.getFeeRuleName()+"已生成缴费明细，不能删除！");
			}
			delete(id);
		}
	}


	@Override
	public void deleteDetail(String c_id) throws ServiceException {
		String[] ids = c_id.split(",");
		for(int index=0;index<ids.length;index++){
			exGeneralHibernateDao.delete(StudentFeeRuleDetails.class, ids[index]);
		}
	}

	@Override
	public BigDecimal AjaxSumPoint(String grade, String major, String classic) throws ServiceException {
		//String sql = "select sum(t.credithour) sumPoint from edu_teach_plancourse t where t.isdeleted = '0' and t.planid = (select p.resourceid from edu_teach_plan p where p.majorid = :major and p.classicid = :classic and p.resourceid in (select g.planid from edu_teach_guiplan g where g.gradeid = :grade and g.isdeleted = '0' and g.ispublished = 'Y'))";
		//Session session = exGeneralHibernateDao.getSessionFactory().openSession();
		//SQLQuery query = session.createSQLQuery(sql);
		//query.setString("grade", grade);
		//query.setString("major", major);
		//query.setString("classic", classic);
		//Object obj = query.uniqueResult();
		//session.close();
		 List<TeachingGuidePlan> teachingGuidePlans = teachingGuidePlanService.findByHql("from "+TeachingGuidePlan.class.getSimpleName()+" t where t.isDeleted=0  "+
				 " and t.grade.resourceid = ? "+//年级
				 " and t.ispublished = ? "+//发布
				 " and t.teachingPlan.major.resourceid = ? "+//专业
				 " and t.teachingPlan.classic.resourceid= ? ",//层次
				 grade,Constants.BOOLEAN_YES,major,classic);
		 if(null != teachingGuidePlans && teachingGuidePlans.size()>0) {
			 return new BigDecimal(teachingGuidePlans.get(0).getTeachingPlan().getMinResult());
		 }
		 return new BigDecimal("0");
	}

	
	@Override
	public void exeGeneratorFee(String resourceids) throws ServiceException {
		String[] ids = resourceids.split(",");
		List<StuPerpayfee> feelist = new ArrayList<StuPerpayfee>();
		for(int index=0;index<ids.length;index++){
			StudentFeeRule rule = get(ids[index]);
			if(null != rule){
			//	if(Constants.BOOLEAN_NO.equals(rule.getGeneratorFlag())){//如果未生成明細
					Set<StudentFeeRuleDetails> ruledetail = rule.getStudentFeeRuleDetails();
					Long year = rule.getGrade().getYearInfo().getFirstYear();
					Map<String, Object> pamars = new HashMap<String, Object>();
					String hql = "from "+StudentInfo.class.getSimpleName()+" s where s.isDeleted = :isDeleted ";
					pamars.put("isDeleted", 0);
					if(null != rule.getGrade() && ExStringUtils.isNotEmpty(rule.getGrade().getResourceid())){//年級
						hql += " and s.grade.resourceid= :gradeId ";
						pamars.put("gradeId", rule.getGrade().getResourceid());
					}
					if(null != rule.getMajor() && ExStringUtils.isNotEmpty(rule.getMajor().getResourceid())){//專業
						hql += " and s.major.resourceid= :majorId ";
						pamars.put("majorId", rule.getMajor().getResourceid());
					}
					if(null != rule.getClassic() && ExStringUtils.isNotEmpty(rule.getClassic().getResourceid())){//層次
						hql += " and s.classic.resourceid= :classicId ";
						pamars.put("classicId", rule.getClassic().getResourceid());
					}
					if(null != rule.getBranchSchool() && ExStringUtils.isNotEmpty(rule.getBranchSchool().getResourceid())){//學習中心
						hql += " and  s.branchSchool.resourceid= :brSchoolId ";
						pamars.put("brSchoolId", rule.getBranchSchool().getResourceid());
					}
					//List<StudentInfo> stuInfo = findByHql(hql, new Object[]{rule.getGrade().getResourceid(),rule.getBranchSchool().getResourceid(),rule.getMajor().getResourceid(),rule.getClassic().getResourceid()});
					List<StudentInfo> stuInfo = studentInfoService.findByHql(hql, pamars);
					
					if(!stuInfo.isEmpty()) {						
						for(StudentInfo info : stuInfo){
							for(StudentFeeRuleDetails detail : ruledetail){
								StuPerpayfee fee = new StuPerpayfee();
								fee.setStudentFeeRule(rule);
								fee.setStudentInfo(info);
								fee.setStudyNo(info.getStudyNo());
								fee.setPayableFee(BigDecimalUtil.mul(BigDecimalUtil.mul(detail.getFeeScale(), rule.getTotalFee()),0.01));
								fee.setPayFeeScale(detail.getFeeTermNum());
								fee.setPayedYear(Integer.parseInt(year.toString()));
								feelist.add(fee);
							} //end for
						}
						if(!feelist.isEmpty()){
							rule.setGeneratorFlag("Y");//设置生成标识
							saveOrUpdate(rule);
						}						
					}			
					
			//	}
			}
		} //end for
		exGeneralHibernateDao.saveOrUpdateCollection(feelist);
	}
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService; 
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
}
