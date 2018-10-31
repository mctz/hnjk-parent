package com.hnjk.edu.teaching.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.service.IStudentCheckService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.model.PublishedExamResultsAudit;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IPublishedExamResultsAuditService;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.vo.AuditExamResultsVo;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 成绩复审服务接口实现.
 * <code>PublishedExamResultsAuditServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-1-16 下午03:25:24
 * @see 
 * @version 1.0
 */
@Transactional
@Service("publishedExamResultsAuditService")
public class PublishedExamResultsAuditServiceImpl extends BaseServiceImpl<PublishedExamResultsAudit> implements IPublishedExamResultsAuditService {

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("studentCheckService")
	private IStudentCheckService studentCheckService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
	
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;//注入考试/毕业论文批次预约服务 
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
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
		hql.append(" from "+PublishedExamResultsAudit.class.getSimpleName()+" where isDeleted=:isDeleted");
		
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
		if(condition.containsKey("scoreType")){
			if("1".equals(condition.get("scoreType").toString())){//毕业论文
				hql.append(" and scoreType=:scoreType ");
			} else {
				hql.append(" and (scoreType is null or scoreType=:scoreType) ");
			}
			param.put("scoreType", condition.get("scoreType"));
		}
		if (condition.containsKey("courseId")) {
			hql.append(" and examResults.course.resourceid=:courseId");
			param.put("courseId", condition.get("courseId"));
		}
		if (condition.containsKey("studyNo")) {
			hql.append(" and examResults.studentInfo.studyNo like :studyNo");
			param.put("studyNo", "%"+condition.get("studyNo")+"%");
		}
		if (condition.containsKey("studentName")) {
			hql.append(" and examResults.studentInfo.studentName like :studentName");
			param.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if (condition.containsKey("changedMan")) {
			hql.append(" and changedMan like :changedMan");
			param.put("changedMan", "%"+condition.get("changedMan")+"%");
		}
		if (condition.containsKey("auditMan")) {
			hql.append(" and auditMan like :auditMan");
			param.put("auditMan", "%"+condition.get("auditMan")+"%");
		}
		if (condition.containsKey("startTime")) {
			hql.append(" and changedDate >= :startTime");
			try {
				param.put("startTime", ExDateUtils.parseDate(condition.get("startTime").toString(), "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (condition.containsKey("endTime")) {
			hql.append(" and changedDate <= :endTime");
			try {
				param.put("endTime", ExDateUtils.parseDate(condition.get("endTime").toString(), "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (page.isOrderBySetted()) {
			hql.append(" order by " +page.getOrderBy()+" "+page.getOrder());
		}
		return findByHql(page, hql.toString(), param);
	}

	@Override
	public void publishAuditExamResults(String[] ids) throws Exception {
		if(ids!=null && ids.length>0){
			User user = SpringSecurityHelper.getCurrentUser();
			List<StudentCheck> list = new ArrayList<StudentCheck>();
			for (String id : ids) {
				PublishedExamResultsAudit audit = get(id);
				if(audit.getAuditStatus()!=null && audit.getAuditStatus()==1){
					continue;
				}
				Date curTime       = new Date ();
				audit.setAuditDate(curTime);
				audit.setAuditMan(user.getCnName());
				audit.setAuditManId(user.getResourceid());
				audit.setAuditStatus(1);				
				ExamResults rs = audit.getExamResults();
				// 以前的成绩类型
				String examAbnormity = rs.getExamAbnormity();
				//取出以前的综合成绩
				Double oldScore  = 0.0;	
				try {
					oldScore = Double.parseDouble(ExStringUtils.isBlank(rs.getIntegratedScore()) ? "0.0" : rs.getIntegratedScore());
				} catch (Exception e) {
					
				}
				//更正
				rs.setWrittenScore(audit.getChangedWrittenScore());
				rs.setUsuallyScore(audit.getChangedUsuallyScore());
				rs.setIntegratedScore(audit.getChangedIntegratedScore());
				rs.setExamAbnormity(audit.getChangedExamAbnormity());
				rs.setWrittenHandworkScore(audit.getChangedWrittenHandworkScore());
				rs.setWrittenMachineScore(audit.getChangedWrittenMachineScore());				
				examResultsService.update(rs);				
				//更正后,修改学生计划
				List<StudentLearnPlan> plans = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" p where p.examResults.resourceid=?", rs.getResourceid());
				StudentLearnPlan plan = null;
				if(plans.size()>0){
					plan = plans.get(0);
					plan.setStatus(3);
					plan.setIntegratedScore(rs.getIntegratedScore());
				}
				//更正后的成绩 如果从及格到不及格需要出现在补考名单里  如果从不及格到及格需要从补考名单剔除
				Double newScore  = 0.0;	
				try {
					newScore = Double.parseDouble(ExStringUtils.isBlank(audit.getChangedIntegratedScore()) ? "0.0" : audit.getChangedIntegratedScore());
				} catch (Exception e) {
					logger.error("字符转换为数字出错：", e);
				}
				//更正处采用百分制 进入补考名单的逻辑也是采用了百分制 所以这里也先采用百分制
				/*
				 * String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? ";
				 * List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
				*/
				if("0".equals(examAbnormity) && ((oldScore >= 60 && newScore < 60) 
						|| "5".equals(rs.getExamAbnormity())
						|| "2".equals(rs.getExamAbnormity())
						|| "1".equals(rs.getExamAbnormity())) ){//由及格到不及格
					if(plan!=null){
						plan.setIsPass("N");//进入补考,不通过
					}
					examResultsService.handleMadeUp(curTime, user, rs);
				}else if(oldScore < 60 && newScore >= 60){ //由不及格到及格
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
							if(plan!=null){
								plan.setIsPass("Y");//补考通过,通过
							}
						}
						
					}
					/* 此前删除所有补考名单，逻辑错误，增加 examresults条件判断，更正为删除此次成绩关联的补考名单*/
					String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? and a.examResults.resourceid=? ";
					List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
					makeupList = studentMakeupListService.findByHql(hqlMakeup,rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid(),rs.getResourceid());
					if(null != makeupList && makeupList.size() > 0){
						for(StudentMakeupList bk : makeupList){
							studentMakeupListService.delete(bk);
						}
					}
					
				}
				StudentCheck check = new StudentCheck();
				check.setStudentId(rs.getStudentInfo().getResourceid());
				list.add(check);
				
				update(audit);
			}
			if (!list.isEmpty()) {
				studentCheckService.batchSaveOrUpdate(list);
			}
		}
	}
	@Override
	public void publishAuditThesisExamResulsts(String[] ids)throws ServiceException {
		if(ids!=null && ids.length>0){
			User user = SpringSecurityHelper.getCurrentUser();
			for (String id : ids) {
				PublishedExamResultsAudit audit = get(id);
				if(audit.getAuditStatus()!=null && audit.getAuditStatus()==1){//跳过以审核的
					continue;
				}
				audit.setAuditDate(new Date());
				audit.setAuditMan(user.getCnName());
				audit.setAuditManId(user.getResourceid());
				audit.setAuditStatus(1);
				
				ExamResults rs = audit.getExamResults();
				
				AuditExamResultsVo vo = examResultsService.findAuditExamResulstsVoByExamResultsId(rs.getResourceid());
				if(vo != null && vo.getPaperOrderId() != null){//初评成绩和答辩成绩记录的是原始成绩，记录在预约表上
					GraduatePapersOrder order = (GraduatePapersOrder) exGeneralHibernateDao.get(GraduatePapersOrder.class, vo.getPaperOrderId());
					order.setFirstScore(audit.getChangedFirstScore());//初评成绩
					order.setSecondScore(audit.getChangedSecondScore());//答辩成绩
				}				
				rs.setIntegratedScore(audit.getChangedIntegratedScore());
				examResultsService.update(rs);
				
				update(audit);
			}
		}
	}
}
