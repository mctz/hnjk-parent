package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentCheckService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduateMentor;
import com.hnjk.edu.teaching.model.GraduateMentorDetails;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduateMentorService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.edu.teaching.vo.GraduatePapersOrderCountVo;
import com.hnjk.edu.teaching.vo.GraduatePapersOrderVo;
import com.hnjk.edu.teaching.vo.ThesisExamResultsVo;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 毕业生论文预约信息表.
 * <code>GraduatePapersOrderController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午03:16:23
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduatepapersorderservice")
public class GraduatePapersOrderServiceImpl extends BaseServiceImpl<GraduatePapersOrder> implements IGraduatePapersOrderService {

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;//注入考试批次服务
	
	@Autowired
	@Qualifier("graduatementorservice")
	private IGraduateMentorService graduateMentorService;//论文指导老师与学生关系
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//注入基础的JDBC服务
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;//学生学习计划服务
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("studentCheckService")
	private IStudentCheckService studentCheckService;
	
	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(GraduatePapersOrder entity)	throws ServiceException {
		lockExamSub(entity);//锁定考试批次
		super.saveOrUpdate(entity);
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#save(java.lang.Object)
	 */
	@Override
	public GraduatePapersOrder save(GraduatePapersOrder entity)	throws ServiceException {
		lockExamSub(entity);//锁定考试批次
		return super.save(entity);
	}
	
	////锁定考试批次
	private void lockExamSub(GraduatePapersOrder order){
		if(null != order && null != order.getExamSub() && ExStringUtils.isNotEmpty(order.getExamSub().getResourceid())){
			ExamSub examSub = examSubService.get(order.getExamSub().getResourceid());
			examSub.setIsLocked(Constants.BOOLEAN_YES);
			examSubService.update(examSub);
		}
	}
	
	
	@Override
	public void saveOrUpdateGraduatePaperOrder(GraduatePapersOrder graduatePapersOrder) throws ServiceException {
		//设置对象
		if(ExStringUtils.isNotEmpty(graduatePapersOrder.getStudentId())){
			StudentInfo stu = studentInfoService.get(graduatePapersOrder.getStudentId());
			graduatePapersOrder.setStudentInfo(stu);
		}
		if(ExStringUtils.isNotEmpty(graduatePapersOrder.getExamSubId())){
			ExamSub exam = examSubService.get(graduatePapersOrder.getExamSubId());
			graduatePapersOrder.setExamSub(exam);
		}
		if(ExStringUtils.isNotEmpty(graduatePapersOrder.getGuidTeacherId())){
			Edumanager teacher = edumanagerService.get(graduatePapersOrder.getGuidTeacherId());
			graduatePapersOrder.setTeacher(teacher);
		}
		
		//判断：同一批次只能预约一次
		List<GraduatePapersOrder> list = findByHql("from "+GraduatePapersOrder.class.getSimpleName()+" where isDeleted = ? and examSub.resourceid = ? and studentInfo.resourceid = ?", 
				0,
				graduatePapersOrder.getExamSub().getResourceid(),
				graduatePapersOrder.getStudentInfo().getResourceid());
		if(null != list && list.size()>0 && ExStringUtils.isEmpty(graduatePapersOrder.getResourceid())){
			throw new ServiceException(graduatePapersOrder.getStudentInfo().getStudentName()+"已在该批次中预约了！");
		}
		
		if(ExStringUtils.isEmpty(graduatePapersOrder.getResourceid())){//如果为新增，则增加一条学习记录
			
			//遍历这个学生的教学计划课程，找出毕业论文
			Set<TeachingPlanCourse> teachingPlanCourses = graduatePapersOrder.getStudentInfo().getTeachingPlan().getTeachingPlanCourses();
			if(null != teachingPlanCourses){
				TeachingPlanCourse thesis = null;
				for (TeachingPlanCourse teachingPlanCourse :teachingPlanCourses) {
					if("thesis".equals(teachingPlanCourse.getCourseType())){
						thesis = teachingPlanCourse;
						break;
					}
				}
				if (null == thesis) {
					throw new ServiceException("学生："+graduatePapersOrder.getStudentInfo().getStudentName()+"的教学计划中没有毕业论文课程！");
				}
				StudentLearnPlan learnPlan 	= new StudentLearnPlan();
				//如果学生是重做毕业论文的情况，学习计划已经存在
				List<StudentLearnPlan> learPlanList = studentLearnPlanService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo.resourceid", graduatePapersOrder.getStudentInfo().getResourceid()),Restrictions.eq("teachingPlanCourse.resourceid", thesis.getResourceid()));
				if(ExCollectionUtils.isNotEmpty(learPlanList)){//已存在毕业论文学习计划
					learnPlan = learPlanList.get(0);
				}
				if(graduatePapersOrder.getCourse()==null){//缺少课程时，补全
					graduatePapersOrder.setCourse(thesis.getCourse());
				}
				learnPlan.setStudentInfo(graduatePapersOrder.getStudentInfo());
				learnPlan.setStatus(1);
				learnPlan.setTeachingPlanCourse(thesis);
				learnPlan.setYearInfo(graduatePapersOrder.getExamSub().getYearInfo());
				learnPlan.setTerm(graduatePapersOrder.getExamSub().getTerm());
				studentLearnPlanService.saveOrUpdate(learnPlan);
			}
			
		}
		
		saveOrUpdate(graduatePapersOrder);		
		//更新毕业论文指导老师-学生关联表
		GraduateMentor graduateMentor = graduateMentorService.findUnique("from "+GraduateMentor.class.getSimpleName()+" where isDeleted = ? and edumanager.resourceid = ? and examSub.resourceid = ?", 
				0,
				graduatePapersOrder.getTeacher().getResourceid(),
				graduatePapersOrder.getExamSub().getResourceid());
		//构造一个明细			
		GraduateMentorDetails gDetails = new GraduateMentorDetails();		
		
		try {
			if(null != graduateMentor){//更新
				Set<GraduateMentorDetails> graduateMentorDetails = graduateMentor.getGraduateMentorDetails();	
				boolean isExits = false;
				for(GraduateMentorDetails d : graduateMentorDetails){
					if(d.getStudentInfo().getResourceid().equals(graduatePapersOrder.getStudentInfo().getResourceid())){
						isExits = true;
						break;
					}
				}
				if(!isExits){
					gDetails.setGraduateMentor(graduateMentor);
					gDetails.setStudentInfo(graduatePapersOrder.getStudentInfo());
					graduateMentorDetails.add(gDetails);
					graduateMentorService.saveOrUpdate(graduateMentor);
				}
				
			}else{//新增
				graduateMentor = new GraduateMentor();
				graduateMentor.setEdumanager(graduatePapersOrder.getTeacher());
				graduateMentor.setExamSub(graduatePapersOrder.getExamSub());
				
				gDetails.setGraduateMentor(graduateMentor);
				gDetails.setStudentInfo(graduatePapersOrder.getStudentInfo());
				gDetails.setGraduateMentor(graduateMentor);
				
				graduateMentor.getGraduateMentorDetails().add(gDetails);
				graduateMentorService.save(graduateMentor);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
		

	@Override
	@Transactional(readOnly=true)
	public Page findGraduateByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = getGraduatePapersOrderHql(condition, values);		
				
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();	
		//hql += " order by examSub.yearInfo.firstYear desc,guidTeacherName,studentInfo.branchSchool.unitCode asc";
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	private String getGraduatePapersOrderHql(Map<String, Object> condition,	Map<String, Object> values) {
		String hql = " from "+GraduatePapersOrder.class.getSimpleName()+" where isDeleted = :isDeleted ";		
		values.put("isDeleted", 0);
		
		if(condition.containsKey("branchSchool")){//校外学习中心
			hql += " and studentInfo.branchSchool.resourceid = :resourceid ";
			values.put("resourceid", condition.get("branchSchool"));
		}
		if(condition.containsKey("grade")){//年级
			hql += " and studentInfo.grade.resourceid = :grade ";
			values.put("grade", condition.get("grade"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and studentInfo.major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and studentInfo.classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("name")){//学生姓名
			hql += " and studentInfo.studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("stuStudyNo")){//学号
			hql += " and studentInfo.studyNo = :studyNo";
			values.put("studyNo", condition.get("stuStudyNo"));
		}
		if(condition.containsKey("batchId")){//预约批次
			hql += " and examSub.resourceid = :batchId ";
			values.put("batchId", condition.get("batchId"));
		}
		if(condition.containsKey("status")){//状态
			hql += " and status = :status ";
			values.put("status", condition.get("status"));
		}		
		if(condition.containsKey("isExclude")){//是否过滤已分配导师学生
			hql += " and guidTeacherId is null ";
			//values.put("status", condition.get("status"));
		}
		if(condition.containsKey("teacherName")){//教师姓名
			hql += " and guidTeacherName like :teacherName";
			values.put("teacherName", condition.get("teacherName"));
		}
				
		if(condition.containsKey("isStudent")){//判断身份
			if(condition.get("isStudent").equals(Constants.BOOLEAN_YES)){//学生
				hql += " and studentInfo.sysUser.resourceid = :uid  ";				
			}else{
				hql += " and guidTeacherId = :uid ";				
			}
			values.put("uid", condition.get("loginId"));
		}
		return hql;
	}
	
	@Override
	public List<GraduatePapersOrder> findGraduateByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = getGraduatePapersOrderHql(condition, values);		
		hql += " order by examSub.yearInfo.firstYear desc,guidTeacherName,studentInfo.branchSchool.unitCode asc";
		return findByHql(hql, values);
	}

	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) throws ServiceException {
		//如果审核后则不可删除
		GraduatePapersOrder graduatePapersOrder = get(id);
		Date settingEndDate 		   = graduatePapersOrder.getExamSub().getEndTime();
		Date curDate        		   = new Date();
		settingEndDate = (null!=settingEndDate)?ExDateUtils.addDays(settingEndDate, 1):curDate;
		Date allowDate                 = settingEndDate;
		
		//当前时间已过预约结束时间
		if (curDate.getTime() >= allowDate.getTime()) {
			throw new ServiceException(graduatePapersOrder.getStudentInfo().getStudentName()+"的毕业论文预约已经结束，不能删除！");
		}
//		if(graduatePapersOrder.getStatus() == Constants.BOOLEAN_TRUE){
//			throw new ServiceException(graduatePapersOrder.getStudentInfo().getStudentName()+"的毕业论文预约已审核，不能删除！");
//		}
		//删除掉学生学习计划,重做毕业论文的情况还原学习计划
		Set<TeachingPlanCourse> teachingPlanCourses = graduatePapersOrder.getStudentInfo().getTeachingPlan().getTeachingPlanCourses();
		if(null != teachingPlanCourses){
			TeachingPlanCourse thesis = null;
			for (TeachingPlanCourse teachingPlanCourse :teachingPlanCourses) {
				if("thesis".equals(teachingPlanCourse.getCourseType())){
					thesis = teachingPlanCourse;
					break;
				}
			}
			if(null != thesis){
				List<StudentLearnPlan> list =  studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" where " +
						"isDeleted = ? and studentInfo.resourceid = ? and teachingPlanCourse.resourceid = ?", 
						0,graduatePapersOrder.getStudentInfo().getResourceid(),thesis.getResourceid());
				//2012-06-15修改,删除学习计划分两种情况：首次预约取消，重新预约取消
				if(null != list && !list.isEmpty()){
					StudentLearnPlan learnPlan = list.get(0);
					ExamResults examResults= learnPlan.getExamResults();
					ExamSub examSub        = ExStringUtils.isNotBlank(examResults.getExamsubId())?examSubService.get(examResults.getExamsubId()):null;
					if(null!=examResults&&"4".equals(examResults.getCheckStatus())){//重新预约毕业论文的情况，还原原本状态
						learnPlan.setStatus(3);
						if (null!=examSub) {
							learnPlan.setYearInfo(examSub.getYearInfo());
							learnPlan.setTerm(examSub.getTerm());
						}
						studentLearnPlanService.update(learnPlan);
					}else { //首次预约，然后取消时,删除学习计划
						studentLearnPlanService.delete(learnPlan);
					}
				}
				
				//12-06-15修改,删除毕业论文指导老师-学生关联表
				if(graduatePapersOrder.getTeacher() != null){//已分配老师					
					try {
						GraduateMentor graduateMentor = graduateMentorService.findUnique("from "+GraduateMentor.class.getSimpleName()+" where isDeleted = ? and edumanager.resourceid = ? and examSub.resourceid = ?", 
								0, graduatePapersOrder.getTeacher().getResourceid(), graduatePapersOrder.getExamSub().getResourceid());
						if(null != graduateMentor){
							Set<GraduateMentorDetails> graduateMentorDetails = graduateMentor.getGraduateMentorDetails();	
							for(GraduateMentorDetails d : graduateMentorDetails){
								if(d.getStudentInfo().getResourceid().equals(graduatePapersOrder.getStudentInfo().getResourceid())){
									exGeneralHibernateDao.delete(GraduateMentorDetails.class, d.getResourceid());
									break;
								}
							}
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
			}
			
		}
		
		super.delete(id);
		
	}
	

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);					
			}
		}
	}

	@Override
	public void audit(String resourceid, String userid, String cnName,int status) throws ServiceException{
		GraduatePapersOrder g = get(resourceid);
		if(g.getStatus() == 0){
			g.setAuditManId(userid);
			g.setAuditMan(cnName);
			g.setAuditTime(new Date());			
		}
		g.setStatus(status);
		update(g);
	}

	@Override
	public void batchAudit(String[] ids, String userid, String cnName,int status) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				audit(id, userid, cnName,status);	
			}
		}
	}
	
	@Override
	public void batchCascadeNext(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				next(id);	
			}
		}
	}

	@Override
	public void next(String resourceid) {
		GraduatePapersOrder order = get(resourceid);
		if("1".equals(order.getCurrentTache().trim())){
			order.setCurrentTache("2");
		}else if("2".equals(order.getCurrentTache().trim())){
			order.setCurrentTache("3");
		}
	}

	//SQL 统计预约
	@Override
	public Page countGradePaperOrderByCondition(Page objPage,	Map<String, Object> condition) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();		
		
		//				批次名称				学习中心     专业			层次			人数					批次ID
		sql.append("select sub.batchname,u.unitname,m.majorname,c.classicname,count(o.resourceid) as ordernum," +
				"sub.resourceid as batchid,s.majorid as majorid,s.classicid as classicid,s.branchschoolid,sub.starttime,u.unitcode from ");
		sql.append("edu_teach_papersorder o,edu_teach_examsub sub,edu_base_major m,edu_base_classic c,hnjk_sys_unit u,edu_roll_studentinfo s ");
		//条件
		sql.append(" where  o.isdeleted = 0 ");
		if(condition.containsKey("batchId")){//批次ID
			sql.append(" and o.examsubid = ? ");
			param.add(condition.get("batchId").toString());
		}
		if(condition.containsKey("major")){//专业
			sql.append(" and s.majorid = ? ");
			param.add(condition.get("major").toString());
		}
		if(condition.containsKey("classic")){//层次
			sql.append(" and s.classicid = ? ");
			param.add(condition.get("classic").toString());
		}
		if (condition.containsKey("branchSchool")) {//校外学习中心
			sql.append(" and s.branchschoolid = ? ");
			param.add(condition.get("branchSchool").toString());
			
		}
		sql.append(" and s.resourceid = o.studentid and o.examsubid=sub.resourceid and s.majorid = m.resourceid and s.classicid = c.resourceid and s.branchschoolid=u.resourceid");
		//分组
		sql.append(" group by sub.batchname,u.unitname,m.majorname,c.classicname,sub.resourceid,sub.starttime,u.unitcode,s.majorid,s.classicid,s.branchschoolid  ");
		//排序
		sql.append(" order by sub.starttime desc,u.unitcode ");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(), param.toArray(), GraduatePapersOrderCountVo.class);
	}
	
	@Override
	public void saveOrUpdateGraduateResults(HttpServletRequest request) throws ServiceException {
		String[] res = request.getParameterValues("resourceid");	
		if(res!=null && res.length>0){
			User user = SpringSecurityHelper.getCurrentUser();	
			String isInOralexaminputTime = ExStringUtils.trimToEmpty(request.getParameter("isInOralexaminputTime"));
			String isInExaminputTime = ExStringUtils.trimToEmpty(request.getParameter("isInExaminputTime"));
			Date curDate             = new Date();
			ExamResults result;
			for (int i = 0; i < res.length; i++) {
				GraduatePapersOrder order = get(res[i]);
				if(order.getExamResults()!=null && "4".equals(order.getExamResults().getCheckStatus())){
					continue;
				}				
				ExamSub examSub = order.getExamSub();
				String firstScoreStr = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("firstScore"+res[i])), "0");
				String secondScoreStr = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("secondScore"+res[i])), "0");
				
				Double firstScore = BigDecimalUtil.round(Double.parseDouble(firstScoreStr), 0);
				Double secondScore =  BigDecimalUtil.round(Double.parseDouble(secondScoreStr), 0);
				if(!Constants.BOOLEAN_NO.equals(isInExaminputTime)){//初评成绩
					order.setFirstScore(firstScore);
				} else {
					firstScore = order.getFirstScore();
				}
				if(!Constants.BOOLEAN_NO.equals(isInOralexaminputTime)){//答辩成绩
					order.setSecondScore(secondScore);
				} else {
					secondScore = order.getSecondScore();
				}
				Double finalScore = firstScore;				
				if(firstScore != null && secondScore!=null && secondScore>0.0){
					if(examSub.getWrittenScorePer()==null){
						throw new ServiceException("请先设置"+examSub.getBatchName()+"的初评成绩和答辩成绩比例");
					} else {
						finalScore = (firstScore * examSub.getWrittenScorePer() + secondScore * (100-examSub.getWrittenScorePer()))/100.0;
					}
				}							
				finalScore = BigDecimalUtil.round(finalScore, 0);//四舍五入
				if(order.getCourse()==null){
					throw new ServiceException("学生"+order.getStudentInfo().getStudentName()+"没有设置毕业论文课程,请联系管理员");
				}
				if(order.getExamResults()!=null){
					if(finalScore!=null && secondScore!=null){
						order.getExamResults().setIntegratedScore(convertScoreFive(finalScore));
					}										
				} else {
					result = new ExamResults();
					result.setCourse(order.getCourse());
					result.setCourseScoreType(Constants.COURSE_SCORE_TYPE_FIVE);
					result.setExamCount(1L);				
					result.setStudentInfo(order.getStudentInfo());
					result.setCheckStatus("-1");
					result.setExamAbnormity("0");
					result.setExamStartTime(curDate);
					result.setExamEndTime(curDate);
					result.setFillinDate(curDate);
					result.setFillinMan(user.getCnName());
					result.setFillinManId(user.getResourceid());
					result.setExamsubId(order.getExamSub().getResourceid());
					
					if (null!=order.getCourse().getExamType()) {
						result.setExamType(Integer.valueOf(order.getCourse().getExamType().toString()));
					}
					for (TeachingPlanCourse c : order.getStudentInfo().getTeachingPlan().getTeachingPlanCourses()) {
						if(order.getCourse().getResourceid().equals(c.getCourse().getResourceid())){
							result.setMajorCourseId(c.getResourceid());
							result.setCreditHour(c.getCreditHour()!=null?c.getCreditHour():null);
							result.setCourseType(c.getCourseType());
							result.setPlanCourseTeachType(c.getTeachType());
							result.setStydyHour(c.getStydyHour()!=null?c.getStydyHour().intValue():null);
						}
					}
					if(finalScore!=null && secondScore!=null){
						result.setIntegratedScore(convertScoreFive(finalScore));
					}
					
					exGeneralHibernateDao.save(result);
					order.setExamResults(result);
				}
				update(order);
			}	
		}
	}
	
	private String convertScoreFive(Double score){
		String score_5 = "";
		if(score<60.0){
			score_5 = "2501";
		} else if(score>=60.0 && score<70.0){
			score_5 = "2512";
		} else if(score>=70.0 && score<80.0){
			score_5 = "2513";
		} else if(score>=80.0 && score<90.0){
			score_5 = "2514";
		} else if(score>=90.0 && score<=100.0){
			score_5 = "2515";
		}		
		return score_5;
	}
	
	@Override
	public void auditThesisResults(String[] ids, String teachType) throws ServiceException {
		if(ids!=null && ids.length>0){
			User user = SpringSecurityHelper.getCurrentUser();
			List<StudentCheck> cklist = new ArrayList<StudentCheck>();
			Date curDate              = ExDateUtils.getCurrentDateTime();
			StudentLearnPlan plan;
			StudentCheck check;
			for (String id : ids) {
				ExamResults result = null;
				if("netsidestudy".equals(teachType)){
					result = examResultsService.get(id);
				} else {
					GraduatePapersOrder order = get(id);
					result = order.getExamResults();
				}				
				if(result!=null && "4".equals(result.getCheckStatus())){
					continue;
				}
				if(result==null || ExStringUtils.isBlank(result.getIntegratedScore())){
					throw new ServiceException("学生"+result.getStudentInfo().getStudentName()+"还未录入毕业论文成绩");
				}
				result.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
				result.setAuditDate(curDate);
				result.setAuditMan(user.getCnName());
				result.setAuditManId(user.getResourceid());
				
				check = new StudentCheck();
				check.setStudentId(result.getStudentInfo().getResourceid());
				cklist.add(check);
				
				List<StudentLearnPlan> list = studentLearnPlanService.findByHql(" from "+StudentLearnPlan.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and teachingPlanCourse.course.resourceid=? ", result.getStudentInfo().getResourceid(),result.getCourse().getResourceid());
				if(ExCollectionUtils.isNotEmpty(list)){
					plan = list.get(0);
					plan.setExamResults(result);		
					plan.setStatus(3);
				} else {
					if("netsidestudy".equals(teachType)){
						plan = new StudentLearnPlan();
						plan.setStudentInfo(result.getStudentInfo());
						plan.setExamResults(result);
						plan.setStatus(3);
						if(ExStringUtils.isNotBlank(result.getMajorCourseId())){
							TeachingPlanCourse teachingPlanCourse = (TeachingPlanCourse) exGeneralHibernateDao.get(TeachingPlanCourse.class, result.getMajorCourseId());
							plan.setTeachingPlanCourse(teachingPlanCourse);
						}	
						if(ExStringUtils.isNotBlank(result.getExamsubId())){
							ExamSub examSub = examSubService.get(result.getExamsubId());
							plan.setYearInfo(examSub.getYearInfo());
							plan.setTerm(examSub.getTerm());
						}						
						studentLearnPlanService.saveOrUpdate(plan);
					}
				}
			}
			if (!cklist.isEmpty()) {
				studentCheckService.batchSaveOrUpdate(cklist);
			}
		}
	}
	
	@Override
	public List<GraduatePapersOrderVo> findGraduatePapersOrderVoByBatchId(String batchId) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		List<GraduatePapersOrderVo> list = new ArrayList<GraduatePapersOrderVo>();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("batchId", batchId);
		
		sql.append(" select b.batchname,m.majorname,t.guidteachername,e.email,e.mobile,unit.unitname,unit.unitshortname,s.studentname,s.studyno ");
		sql.append(" from edu_teach_papersorder t  ");
		sql.append(" join edu_teach_examsub b on t.examsubid=b.resourceid ");
		sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid ");
		sql.append(" join edu_base_major m on s.majorid=m.resourceid ");
		sql.append(" join edu_base_edumanager e on t.guidteacherid=e.sysuserid ");
		sql.append(" join hnjk_sys_users u on e.sysuserid=u.resourceid ");
		sql.append(" join hnjk_sys_unit unit on s.branchschoolid=unit.resourceid ");
		sql.append(" where t.isdeleted=0 and t.examsubid=:batchId  ");
		sql.append(" group by b.batchname,m.majorname,t.guidteachername,e.email,e.mobile,unit.unitname,unit.unitshortname,s.studentname,s.studyno ");
		sql.append(" order by b.batchname,m.majorname,t.guidteachername,unit.unitname,s.studyno  ");
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), GraduatePapersOrderVo.class,param);
		} catch (Exception e) {
		}
		return list;
	}
	
	@Override
	public Page findThesisExamResultsVoByCondition(Page objPage, Map<String, Object> condition) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql =  getThesisExamResultsSqlByCondition(condition, params);		
		objPage = baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(), params.toArray(), ThesisExamResultsVo.class);
		return objPage;
	}
	
	private ThesisExamResultsVo getThesisExamResultsVoByBatchIdAndStudentId(String batchId, String studentId,String status) throws ServiceException{
		List<Object> params = new ArrayList<Object>();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("batchId", batchId);
		condition.put("studentId", studentId);
		condition.put("status", status);
		StringBuffer sql =  getThesisExamResultsSqlByCondition(condition, params);
		try {
			List<ThesisExamResultsVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), params.toArray(), ThesisExamResultsVo.class);
			if(ExCollectionUtils.isNotEmpty(list)){
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private StringBuffer getThesisExamResultsSqlByCondition(Map<String, Object> condition, List<Object> params){
		StringBuffer sql = new StringBuffer();
		sql.append(" select unit.unitcode,unit.unitname branchSchool,grade.gradename,major.majorname,classic.classicname,s.studyno stuStudyNo,s.studentname name,course.coursename,pc.courseid,pc.teachtype,pc.stydyhour, ");
		sql.append(" 	s.resourceid studentid,r.resourceid examResultsId,r.integratedscore,r.checkstatus,course.examtype,pc.resourceid majorcourseid,pc.credithour,pc.coursetype,r.auditMan,r.auditDate,r.examsubid ");
		sql.append(" from edu_roll_studentinfo s ");
		sql.append(" join edu_base_grade grade on s.gradeid=grade.resourceid ");
		sql.append(" join edu_base_year y on grade.yearid=y.resourceid ");
		sql.append(" join edu_base_major major on s.majorid=major.resourceid ");
		sql.append(" join edu_base_classic classic on s.classicid=classic.resourceid ");
		sql.append(" join hnjk_sys_unit unit on s.branchschoolid=unit.resourceid ");
		sql.append(" join edu_teach_plan p on s.teachplanid=p.resourceid ");
		sql.append(" join edu_teach_plancourse pc on s.teachplanid=pc.planid and pc.isdeleted=0 and pc.coursetype='thesis' and pc.teachtype='netsidestudy' ");//A+B模式
		sql.append(" join edu_base_course course on pc.courseid=course.resourceid  ");
		sql.append(" left join edu_teach_examresults r on s.resourceid=r.studentid and r.isdeleted=0 and r.courseid=pc.courseid and r.examsubid=? ");
		params.add(condition.get("batchId"));
		sql.append(" where s.isdeleted=0 ");	
		//不存在预约记录
		sql.append(" and not exists (select po.resourceid from edu_teach_papersorder po where po.isdeleted=0 and po.studentid=s.resourceid and po.examsubid=?) ");
		params.add(condition.get("batchId"));		
		if(condition.containsKey("status") && "1".equals(condition.get("status").toString())){//已录入成绩
			sql.append(" and r.checkstatus >=-1 ");			
		} else {
			sql.append(" and r.resourceid is null ");
			//未预约毕业论文
			sql.append(" and not exists ( select sp.resourceid from edu_learn_stuplan sp where sp.isdeleted=0  and sp.studentid=s.resourceid and sp.plansourceid=pc.resourceid and sp.status=3 ) ");
			sql.append(" and (s.studentstatus='11' or s.studentstatus='21') ");//在学或延期
			//sql.append(" and s.isableordersubject=1 ");//可以预约毕业论文
			sql.append(" and s.finishedcredithour>=p.applypaperminresult ");//已修总学分达到毕业论文申请最低学分
		}
		if(condition.containsKey("defaultGradeYear")){
			//学期数算法：学期数 = (默认年度起始年 - 学生年度起始年)*2+(默认学期 - 学生学期)+1
			sql.append(" and (?-y.firstyear)*2+(?-grade.term)+1>= pc.term ");//必须达到预约学期
			params.add(condition.get("defaultGradeYear"));
			params.add(condition.get("defaultGradeTerm"));
		}	
		if(condition.containsKey("branchSchool")){
			sql.append(" and s.branchschoolid=? ");
			params.add(condition.get("branchSchool"));
		}
		if(condition.containsKey("grade")){
			sql.append(" and s.gradeid=? ");
			params.add(condition.get("grade"));
		}
		if(condition.containsKey("major")){
			sql.append(" and s.majorid=? ");
			params.add(condition.get("major"));
		}
		if(condition.containsKey("classic")){
			sql.append(" and s.classicid=? ");
			params.add(condition.get("classic"));
		}
		if(condition.containsKey("stuStudyNo")){
			sql.append(" and s.studyno=? ");
			params.add(condition.get("stuStudyNo"));
		}
		if(condition.containsKey("name")){
			sql.append(" and s.studentname like ? ");
			params.add(condition.get("name")+"%");
		}
		if(condition.containsKey("studentId")){
			sql.append(" and s.resourceid=? ");
			params.add(condition.get("studentId"));
		}							
		sql.append(" order by unit.unitcode,s.studyno ");		
		return sql;
	}
	
	@Override
	public void saveOrUpdateNetsideStudyThesisResults(HttpServletRequest request) throws ServiceException {
		String[] res = request.getParameterValues("resourceid");//studentid
		String batchId = request.getParameter("batchId");
		if(ExStringUtils.isBlank(batchId)){
			throw new ServiceException("请选择一个论文批次");
		}
		if(res!=null && res.length>0){
			User user    = SpringSecurityHelper.getCurrentUser();	
			Date curDate = new Date();
			ExamResults result;
			for (int i = 0; i < res.length; i++) {							
				//网成班只录入终评成绩
				//String firstScoreStr = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("firstScore"+res[i])), "0");
				//String secondScoreStr = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("secondScore"+res[i])), "0");
				//Double firstScore = BigDecimalUtil.round(Double.parseDouble(firstScoreStr), 0);
				//Double secondScore =  BigDecimalUtil.round(Double.parseDouble(secondScoreStr), 0);
				String integratedScore = ExStringUtils.trimToEmpty(request.getParameter("integratedScore"+res[i]));
				String status = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("status"+res[i])), "0");
					
				ThesisExamResultsVo vo = getThesisExamResultsVoByBatchIdAndStudentId(batchId, res[i],status);
				if("4".equals(vo.getCheckStatus())){
					continue;
				}	
				if(ExStringUtils.isBlank(integratedScore)){
					throw new ServiceException("学生"+vo.getName()+"终评成绩不能为空");
				}
				
				result = new ExamResults();
				if(ExStringUtils.isNotBlank(vo.getExamResultsId())){
					result = examResultsService.get(vo.getExamResultsId());
				} else {
					Course course = (Course) exGeneralHibernateDao.get(Course.class, vo.getCourseId());
					result.setCourse(course);
					StudentInfo studentInfo = studentInfoService.get(res[i]);
					result.setStudentInfo(studentInfo);						
					
					result.setCourseScoreType(Constants.COURSE_SCORE_TYPE_FIVE);
					result.setExamCount(1L);					
					result.setCheckStatus("-1");
					result.setExamAbnormity("0");
					result.setExamStartTime(curDate);
					result.setExamEndTime(curDate);
					result.setFillinDate(curDate);
					result.setFillinMan(user.getCnName());
					result.setFillinManId(user.getResourceid());
					result.setExamsubId(batchId);
					result.setExamType(vo.getExamType());
					result.setMajorCourseId(vo.getMajorCourseId());
					result.setCreditHour(vo.getCreditHour());
					result.setCourseType(vo.getCourseType());
					result.setPlanCourseTeachType(vo.getTeachType());
					result.setStydyHour(vo.getStydyHour());
				}
				result.setIntegratedScore(integratedScore);
				examResultsService.saveOrUpdate(result);				
			}	
		}
	}
}
