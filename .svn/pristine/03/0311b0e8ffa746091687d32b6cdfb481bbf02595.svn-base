package com.hnjk.edu.teaching.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamResultsAudit;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.service.IExamResultsAuditService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 
 * <code>成绩复审Service实现</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-12-22 下午15:21:02
 * @see 
 * @version 1.0
 */

@Service("examResultsAuditService")
@Transactional
public class ExamResultsAuditServiceImpl extends BaseServiceImpl<ExamResultsAudit> implements IExamResultsAuditService{

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	/**
	 * 根据条件查询成绩复审-返回分页对象
	 * @param condition
	 * @param page
	 * @return
	 */
	@Override
	public Page findExamResultsAuditByCondition(Map<String, Object> condition,Page page) {
		
		StringBuffer hql 		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted", 0);
		hql.append(" from "+ExamResultsAudit.class.getSimpleName()+" where isDeleted=:isDeleted");
		
		if (condition.containsKey("examSubId")) {
			hql.append(" and examResults.examsubId=:examSubId");
			param.put("examSubId", condition.get("examSubId"));
		}
		if (condition.containsKey("examInfoId")) {
			hql.append(" and examResults.examInfo.resourceid=:examInfoId");
			param.put("examInfoId", condition.get("examInfoId"));
		}
		if (condition.containsKey("auditStatus")) {
			hql.append(" and  auditStatus=:auditStatus");
			param.put("auditStatus",Integer.valueOf(condition.get("auditStatus").toString()));
		}
		if (condition.containsKey("auditType")) {
			hql.append(" and auditType=:auditType");
			param.put("auditType", condition.get("auditType"));
		}
		if (ExStringUtils.isNotBlank(page.getOrderBy())) {
			hql.append(" order by " +page.getOrderBy() );
		}
		if (ExStringUtils.isNotBlank(page.getOrder())) {
			hql.append(" "+page.getOrder());
		}
		return exGeneralHibernateDao.findByHql(page, hql.toString(), param);
	}
	/**
	 * 成绩复审
	 * @param ids
	 * @param flag
	 * @throws ServiceException
	 */
	@Override
	public void examResultsReview(String[] idsArray, String flag,HttpServletRequest request)throws Exception {
		
		User curUser 	  			 = SpringSecurityHelper.getCurrentUser();		 //当前用户
		Date curTime      			 = new Date();                                	 //当前时间
		StringBuffer ids  			 = new StringBuffer();
		Map<String,String> checkMemo = new HashMap<String, String>();
		List<StudentCheck> checkList = new ArrayList<StudentCheck>();
		
		for (int i = 0; i < idsArray.length; i++) {
			ids.append(",'"+idsArray[i]+"'");
			checkMemo.put(idsArray[i], ExStringUtils.trimToEmpty(request.getParameter("memo_"+idsArray[i])));
		}
		List<ExamResultsAudit> list = this.findByHql(" from "+ExamResultsAudit.class.getSimpleName()+" audit where audit.isDeleted=0 and audit.resourceid in("+ids.substring(1)+")");
		ids                         = new StringBuffer();
		
		Map<String,StudentLearnPlan> map = new HashMap<String, StudentLearnPlan>();
		if(ExStringUtils.isNotEmpty(ids.toString())){
			List<StudentLearnPlan> lp   = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=?  and plan.examResults.resourceid in("+ids.substring(1)+")",0);
			for (StudentLearnPlan p:lp) {
				map.put(p.getExamResults().getResourceid(), p);
			}
		}
		
		for (ExamResultsAudit audit:list) {
			ExamResults rs = audit.getExamResults();
			if (Constants.BOOLEAN_YES.equals(flag)) {
				
				/*String s1 = audit.getChangedIntegratedScore();
				String s2 = audit.getChangedWrittenScore();
				String s3 = audit.getChangedUsuallyScore();
				String s4 = audit.getChangedExamAbnormity();*/
				
				
				rs.setWrittenScore(audit.getChangedWrittenScore());
				rs.setUsuallyScore(audit.getChangedUsuallyScore());
				rs.setIntegratedScore(audit.getChangedIntegratedScore());
				rs.setExamAbnormity(audit.getChangedExamAbnormity());
				rs.setWrittenHandworkScore(audit.getChangedWrittenHandworkScore());
				rs.setWrittenMachineScore(audit.getChangedWrittenMachineScore());
				
				/*String s_1  = rs.getIntegratedScore();
				String s_2  = rs.getWrittenScore();
				String s_3  = rs.getUsuallyScore();
				String s_4  = rs.getExamAbnormity();*/
				
				rs.setCheckNotes(checkMemo.get(audit.getResourceid()));
				rs.setAuditDate(curTime);
				rs.setAuditMan(curUser.getCnName());
				rs.setAuditManId(curUser.getResourceid());
				rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
				
				ids.append(",'"+rs.getResourceid()+"'");
				
				StudentCheck check = new StudentCheck();
				check.setStudentId(rs.getStudentInfo().getResourceid());
				checkList.add(check);
				
				audit.setAuditStatus(1);//设置已复审通过
				
				StudentLearnPlan plan = null;
				if (map.containsKey(rs.getResourceid())) {//通过后则设置最终成绩和通过
					plan = map.get(rs.getResourceid());
					plan.setCheckStatus("3");
					plan.setIntegratedScore(rs.getIntegratedScore());
					plan.setIsPass("Y");
				}
				
                // 处理不及格已经异常成绩进入补考名单的逻辑
				Double IntegratedScore = Double.valueOf(rs.getIntegratedScore()==null?"0":rs.getIntegratedScore());
				/* 此前逻辑为 IntegratedScore <= 60，更正为 < 60 分   20160504*/
				if((IntegratedScore > 0 && IntegratedScore < 60)
						|| "5".equals(rs.getExamAbnormity())
						|| "2".equals(rs.getExamAbnormity())
						|| "1".equals(rs.getExamAbnormity())){
					
					if(plan!=null){//进入补考,则不通过
						plan.setIsPass("N");
					}
					examResultsService.handleMadeUp(curTime, curUser, rs);
				}
				//如果补考成绩及格，标记上次补考名单中的是否通过为  Y 
				if(ExStringUtils.isNotBlank(rs.getIntegratedScore())&&Double.parseDouble(rs.getIntegratedScore())>=60){//由于当前只能录入百分制度的分值成绩，所以只判断成绩>=60分为及格
					String hql = "from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.nextExamSubId=? and a.studentInfo.resourceid=? and a.course.resourceid=? ";
					List<StudentMakeupList> stumakeupList = studentMakeupListService.findByHql(hql, rs.getExamsubId(),rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
					if(stumakeupList!=null && stumakeupList.size()>0){
						StudentMakeupList stumakeup= new StudentMakeupList();
						stumakeup=	stumakeupList.get(0);
						stumakeup.setIsPass("Y");
						//makeupList.add(stumakeup);
						studentMakeupListService.saveOrUpdate(stumakeup);
						
						if(plan!=null){//补考通过,则通过
							plan.setIsPass("Y");
						}
					}
					
				}
				
			}else {
				rs.setCheckNotes(checkMemo.get(audit.getResourceid()));
				rs.setAuditDate(curTime);
				rs.setAuditMan(curUser.getCnName());
				rs.setAuditManId(curUser.getResourceid());
				rs.setCheckStatus("1");
				
				audit.setAuditStatus(2);//设置已复审不通过
			}
			
			examResultsService.update(rs);
			this.update(audit);
			
			
		}
		
		
		if (checkList.size()>0) {
			exGeneralHibernateDao.batchSaveOrUpdate(checkList);
		}
	}
}
