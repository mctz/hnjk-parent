package com.hnjk.edu.teaching.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.AbnormalExam;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.IAbnormalExamService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.INoExamApplyService;
import com.hnjk.edu.teaching.service.IStateExamResultsService;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * 学生异常成绩-缓考申请表
 * <code>AbnormalExamServiceImpl</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-06-17 上午 11:12:45
 * @see
 * @version 1.0
 */
@Transactional
@Service("abnormalExamService")
public class AbnormalExamServiceImpl extends BaseServiceImpl<AbnormalExam> implements
		IAbnormalExamService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("noexamapplyservice")
	private INoExamApplyService noExamApplyService;
	
	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;

	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;

	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;

	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	/**
	 * 保存/更新异常成绩申请（目前只用于缓考）
	 * 
	 * @param map
	 * @param courseTypes         课程类型
	 * @param abnormalTypes     申请异常成绩类型
	 * @param courseIds              课程ID
	 * @param planCourseIds       教学计划课程ID
	 * @param abnormalExamIds            申请异常成绩ID
	 * @param scores                   分数
	 * @param AEexamTypes        考试类型
	 * @param reasons                 申请理由
	 * @param studentId               学生ID
	 * @throws Exception
	 */
	@Override
	public void saveAbnormalExamApply(Map<String, Object> map,
			String[] courseTypes, String[] abnormalTypes, String[] courseIds,
			String[] planCourseIds, String[] abnormalExamIds, String[] scores,
			String[] AEexamTypes,String [] reasons, String studentId) throws Exception {
		
		String statusCode = "200";
		String message ="";
		try {
			User user	   = SpringSecurityHelper.getCurrentUser();
			Date   curTime = ExDateUtils.getCurrentDateTime();
			StudentInfo stu= studentInfoService.get(studentId);
			
			Course course  = null;
			TeachingPlanCourse teachingPlanCourse = null;
			
			Pattern pattern= Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
			AbnormalExam abnormalExam= null;
			for (int i = 0; i < courseIds.length; i++) {
				if(StringUtils.isNotEmpty(message)){
					message += "<br/>------------------------------------<br/>";
				}
				if (StringUtils.isNotBlank(abnormalTypes[i])) {
					//校验该用户不能重复申请 :一个学籍只能申请一次相同课程的免修免考	或者统考		
					String noExamApply_hql    = " from "+NoExamApply.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ?";
					String stateExam_hql    = " from "+StateExamResults.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ? ";
					
					List<NoExamApply> noExamApplyList 	 = noExamApplyService.findByHql(noExamApply_hql,0,stu.getResourceid(),courseIds[i]);
					List<StateExamResults> stateExamList = stateExamResultsService.findByHql(stateExam_hql, 0,stu.getResourceid(),courseIds[i]);
					if(CollectionUtils.isNotEmpty(noExamApplyList)||CollectionUtils.isNotEmpty(stateExamList)){
						message = "学生 "+stu.getStudentName()+" 已申请过 <font color='blue'>"+course.getCourseName()+"</font> 的免修免考或统考！"
								+ "<br/>如未审核，请联系教务管理人员.";
						continue;
					}
					// 检查该学生的该门课程是否已经申请过缓考
					String abnormalExam_hql    = " from "+AbnormalExam.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ? ";
					List<AbnormalExam> abnormalExamList = findByHql(abnormalExam_hql, 0,stu.getResourceid(),courseIds[i]);
					if(CollectionUtils.isNotEmpty(abnormalExamList)){
						message = "学生 "+stu.getStudentName()+" 已申请过 <font color='blue'>"+course.getCourseName()+"</font> 的缓考！"
								+ "<br/>请刷新该页面查看！";
						continue;
					}
					if (StringUtils.isNotBlank(abnormalExamIds[i])) {
						abnormalExam 		 = get(abnormalExamIds[i]);
					}
					if(null == abnormalExam){
						abnormalExam 		 = new AbnormalExam();
						course  	 = courseService.get(courseIds[i]);	
						teachingPlanCourse = teachingPlanCourseService.get(planCourseIds[i]);
						abnormalExam.setCourse(course);
						abnormalExam.setTeachingPlanCourse(teachingPlanCourse);
						abnormalExam.setCourseScoreType(courseTypes[i]);
						
						String score    = StringUtils.trimToEmpty(scores[i]);
						
						if (StringUtils.isNotBlank(reasons[i])) {
							abnormalExam.setReason(reasons[i]);
						}
						Matcher m  	 = pattern.matcher(score);
						if (Boolean.FALSE == m.matches()) {
							message = " 课程："+course.getCourseName()+"的成绩应为0-100的数字</br>";
							continue;
						}
						abnormalExam.setScoreForCount(Double.valueOf(score));
						abnormalExam.setStudentInfo(stu);
						abnormalExam.setApplyDate(curTime);
						abnormalExam.setApplyMan(user);
						abnormalExam.setApplyManName(user.getCnName());
						abnormalExam.setAbnormalType(abnormalTypes[i]);
						abnormalExam.setExamType(AEexamTypes[i]);
						abnormalExam.setCheckStatus(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT);
						
						saveOrUpdate(abnormalExam);
						// 发送信息
						sendMessage(abnormalExam, user, Integer.valueOf(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT));
						message += course.getCourseName()+" 保存成功!";
						logger.info("学生: "+stu.getStudentName()+" 学号："+stu.getStudyNo()+" 申请类型："
						+JstlCustomFunction.dictionaryCode2Value("CodeAbnormalType",abnormalTypes[i])+" 申请时间："
						+ExDateUtils.formatDateStr(curTime, ExDateUtils.PATTREN_DATE_TIME)+" 操作人："
						+user.getCnName()+" 操作人ID："+user.getResourceid());
					}
				}
			}
		} catch (Exception e) {
			logger.error("保存/更新异常成绩申请失败", e);
			statusCode = "300";
			message = "保存/更新异常成绩申请失败!";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
	}

	/**
	 * 获取缓考申请列表，返回 Page
	 */
	@Override
	public Page findAbnormalExamByCondition(Map<String, Object> condition,
			Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>(20);
		String hql = findAbnormalExamByConditionHql(condition,values,objPage);
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	/**
	 * 组装查询缓考申请列表的Hql
	 * 
	 * @param condition
	 * @param values
	 * @param objPage
	 * @return
	 */
	public String findAbnormalExamByConditionHql(Map<String, Object> condition, Map<String, Object> values,
			Page objPage) {

		StringBuffer hql = new StringBuffer(" from "+AbnormalExam.class.getSimpleName()+" where 1=1  and isDeleted = 0 "
				+ "and studentInfo.isDeleted = 0 and course.isDeleted = 0 ");
		
		if(condition.containsKey("abnormalExamListSchool")){
			hql.append(" and studentInfo.branchSchool.resourceid = :resourceid ");
			values.put("resourceid", condition.get("abnormalExamListSchool"));
		}
		if(condition.containsKey("abnormalExamListGrade")){
			hql.append(" and studentInfo.grade.resourceid = :grade ");
			values.put("grade", condition.get("abnormalExamListGrade"));
		}
		if(condition.containsKey("abnormalExamListMajor")){
			hql.append(" and studentInfo.major.resourceid = :major ");
			values.put("major", condition.get("abnormalExamListMajor"));
		}
		if(condition.containsKey("abnormalExamListName")){
			hql.append(" and studentInfo.studentName like :name ");
			values.put("name", "%" + condition.get("abnormalExamListName")+"%");
		}
		if(condition.containsKey("abnormalExamListStuNo")){
			hql.append(" and studentInfo.studyNo like :stuNo ");
			values.put("stuNo", "%" + condition.get("abnormalExamListStuNo")+"%");
		}
		if(condition.containsKey("studentId")){
			hql.append(" and studentInfo.resourceid = :studentId ");
			values.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("userId")){
			hql.append(" and studentInfo.sysUser.resourceid = :userId ");
			values.put("userId", condition.get("userId"));
		}
		if(condition.containsKey("abnormalExamListClassic")){
			hql.append(" and studentInfo.classic.resourceid = :classic ");
			values.put("classic", condition.get("abnormalExamListClassic"));
		}
		if(condition.containsKey("abnormalExamListCheckStatus")){
			hql.append(" and checkStatus = :checkStatus ");
			values.put("checkStatus", condition.get("abnormalExamListCheckStatus"));
		}
		if(condition.containsKey("abnormalExamListAppStartTime")){
			hql.append(" and applyDate >= to_date('"+condition.get("abnormalExamListAppStartTime")+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(condition.containsKey("abnormalExamListAppEndTime")){
			hql.append(" and applyDate <=  to_date('"+condition.get("abnormalExamListAppEndTime")+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(condition.containsKey("abnormalExamListAuditStartTime")){
			hql.append(" and checkDate >= to_date('"+condition.get("abnormalExamListAuditStartTime")+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(condition.containsKey("abnormalExamListAuditEndTime")){
			hql .append(" and checkDate <= to_date('"+condition.get("abnormalExamListAuditEndTime")+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if (null!=objPage && StringUtils.isNotBlank(objPage.getOrderBy())) {
			hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		}else {
			hql.append(" order by checkDate desc");
		}
		
		return hql.toString();
	}

	/**
	 * 批量审批通过
	 */
	@Override
	public Map<String, Object> batchAudit(String[] abnormalExamIds, User user, String checkStatus) 
			throws ServiceException {
		Map<String ,Object> returnMap 	= new HashMap<String, Object>(10);
		if(abnormalExamIds != null && abnormalExamIds.length > 0){
			String statusCode = "200";
			String message = "";
			Date curTime = new Date();
			Map<String ,Object> _map;
			for (String abnormalExamId : abnormalExamIds) {
				_map = audit(abnormalExamId,user,curTime, checkStatus);
				statusCode = (String)_map.get("statusCode");
				message += (String)_map.get("message");
				if(abnormalExamIds.length > 1 && StringUtils.isNotEmpty(message)){
					message += "<br/>-------------------------------<br/>";
				}
			}
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message.toString());
		}
		return returnMap;
	}

	/**
	 * 单个审批通过
	 * 
	 * @param abnormalExamId
	 * @param user
	 * @param curTime
	 * @return
	 * @throws ServiceException 
	 */
	private Map<String, Object> audit(String abnormalExamId, User user, Date curTime, String checkStatus) {
		Map<String ,Object> map 	= new HashMap<String, Object>(10);
		StringBuffer message = new StringBuffer("");
		String statusCode = "200";
		String isSuccess = "Y";
		String courseName = "";
		for(int i=0;i<1;i++){
			try {
				AbnormalExam abnormalExam = get(abnormalExamId);
				if (abnormalExam.getCheckStatus().equals(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT)) {
					courseName = abnormalExam.getCourse().getCourseName();
					if(checkStatus.equals(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_PAST)) {
						/**
						 * 如果该学生该门课程没有正考成绩，则在成绩表添加一条记录并且添加对应的补考名单，
						 * 否则update对应的正考成绩（如果有补考名单有记录则不做处理，否则在补考名单表中添加一条记录）
						 */
						handleAbnormalExamResults(user, curTime, abnormalExam, message);
						if(StringUtils.isNotEmpty(message.toString())){
							continue;
						}
						// 将该学生用于学分计算的学籍ID表
						StudentCheck check = new StudentCheck();
						check.setStudentId(abnormalExam.getStudentInfo().getResourceid());
						exGeneralHibernateDao.save(check);
					}
					abnormalExam.setCheckDate(curTime);
					abnormalExam.setCheckManName(user.getCnName());
					abnormalExam.setCheckMan(user);
					abnormalExam.setCheckStatus(checkStatus);
					// 更新缓考申请记录
					saveOrUpdate(abnormalExam);
					
					message.append( abnormalExam.getStudentInfo().getStudentName()
							+"<font color='red'>《"+abnormalExam.getCourse().getCourseName()+"》</font>审核成功。");
					// 发送信息给申请人
					sendMessage(abnormalExam,user, Integer.valueOf(checkStatus));
				}else{
					message.append(abnormalExam.getStudentInfo().getStudentName()+"<font color='red'>《"
							+abnormalExam.getCourse().getCourseName()+"》</font>已审核，如需更改请撤销审核后再执行此操作。");
					isSuccess = "N";
				}
			} catch (Exception e) {
				statusCode = "300";
				logger.error("单个审核失败", e);
				message.append("《" + courseName + "》审核失败");
				isSuccess = "N";
			} finally {
				map.put("statusCode", statusCode);
				map.put("message", message.toString());
				map.put("isSuccess", isSuccess);
			}
		}
	
		return map;
	}

	/**
	 * 处理缓考申请的成绩和补考名单
	 * 
	 * @param user
	 * @param curTime
	 * @param abnormalExam
	 * @throws Exception
	 */
	private void handleAbnormalExamResults(User user, Date curTime,
			AbnormalExam abnormalExam, StringBuffer message) throws Exception {
		List<TeachingGuidePlan> guidePlanList;
		List<TeachingPlanCourseStatus> courseStatusList;
		List<ExamResults> examResultsList;
		ExamResults _examResults = null;
		List<StudentMakeupList> makeupList;
		for(int k=0;k<1;k++){
			// 1、获取年级教学计划
			String guidePlanHql = "from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and teachingPlan.resourceid=? and grade.resourceid=?";
			guidePlanList = teachingGuidePlanService.findByHql(guidePlanHql, abnormalExam.getStudentInfo().getTeachingPlan().getResourceid()
					,abnormalExam.getStudentInfo().getGrade().getResourceid());
			// 2、获取课程开课学期
			//所属入学年份
			Long year = 0L;
			// 课程上课学期
			String term = "";
			if(guidePlanList != null && guidePlanList.size() > 0) {
				TeachingPlanCourse teachingPlanCourse = abnormalExam.getTeachingPlanCourse();
				year = abnormalExam.getStudentInfo().getGrade().getYearInfo().getFirstYear();
				String courseStatusHql = "from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingPlanCourse.resourceid=?"
						+" and teachingGuidePlan.resourceid=? and schoolIds=? and isOpen='Y' and checkStatus!='cancelY' ";
				courseStatusList = teachingPlanCourseStatusService.findByHql(courseStatusHql,teachingPlanCourse.getResourceid(),
						guidePlanList.get(0).getResourceid(), abnormalExam.getStudentInfo().getBranchSchool().getResourceid());
				String isMachineExam = Constants.BOOLEAN_NO;
				if (courseStatusList == null || courseStatusList.isEmpty()) {
					message.append("《"+abnormalExam.getCourse().getCourseName()+ "》这门课程还未开课! ");
					continue;
//					throw new ServiceException("《"+abnormalExam.getCourse().getCourseName()+ "》这门课程还未开课! ");
				}
				Integer examForm = courseStatusList.get(0).getExamForm();
				if(examForm==3){
					isMachineExam = Constants.BOOLEAN_YES;
				}
				term = courseStatusList.get(0).getTerm();
				String[] year_term = term.split("_");
				// 该课程开课年份
				Long _year = Long.parseLong(year_term[0]);
				// 开课学期
				String _term = year_term[1];
				_term = _term.substring(_term.length()-1);
				ExamSub examSub = examSubService.findExamSubBycondition("N", _year, _term);
				if(examSub == null) {
					message.append("请先添加\""+_year + "年第" + _term + "学期正考 \"这个考试批次！");
					continue;
				}
				// 3、成绩处理（考试批次课程信息）
				String examResultHql = "from " + ExamResults.class.getSimpleName() + " where isDeleted=0 and majorCourseId=? "
						+ " and studentInfo.resourceid=? and examsubId=? ";
				examResultsList = examResultsService.findByHql(examResultHql, teachingPlanCourse.getResourceid(),
						abnormalExam.getStudentInfo().getResourceid(), examSub.getResourceid());

				if(examResultsList != null && examResultsList.size() > 0){
					_examResults = examResultsList.get(0);
					_examResults.setIntegratedScore(abnormalExam.getScoreForCount().toString());
					_examResults.setExamAbnormity("5");// 缓考类型
					//2016年1月11日增加，平时成绩应该修改为0分  by lion;
					_examResults.setUsuallyScore("0");
					_examResults.setIsDelayExam("Y");
				} else {
					Integer examCourseType = "facestudy".equals(teachingPlanCourse.getTeachType())?1:2;
					if(examForm==3){
						examCourseType = 3;
					}
					ExamInfo examInfo = examInfoService.saveExamInfo(60D, examSub, abnormalExam.getCourse(), examCourseType,isMachineExam);
					_examResults = new ExamResults();
					_examResults.setCourse(abnormalExam.getCourse());	
					_examResults.setExamInfo(examInfo);
					_examResults.setMajorCourseId(teachingPlanCourse.getResourceid());
					_examResults.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
					_examResults.setExamsubId(examSub.getResourceid());
					_examResults.setCreditHour(teachingPlanCourse.getCreditHour()!=null?teachingPlanCourse.getCreditHour():null);
					_examResults.setStydyHour(teachingPlanCourse.getStydyHour()!=null?teachingPlanCourse.getStydyHour().intValue():null);
					_examResults.setExamType(abnormalExam.getCourse().getExamType()!=null?abnormalExam.getCourse().getExamType().intValue():null);
					_examResults.setIsMakeupExam(examSub.getExamType());//考试类型
					_examResults.setCourseType(teachingPlanCourse.getCourseType());														
					_examResults.setStudentInfo(abnormalExam.getStudentInfo());
					_examResults.setCourseScoreType(examInfo.getCourseScoreType());
					_examResults.setExamCount(1L);	
					_examResults.setPlanCourseTeachType(teachingPlanCourse.getTeachType());
					_examResults.setIsDelayExam("Y");
					_examResults.setExamAbnormity("5");
					_examResults.setWrittenScore("0");
					_examResults.setUsuallyScore("0");
					_examResults.setOnlineScore("0");
					_examResults.setIntegratedScore("0");
					_examResults.setFillinDate(curTime);
					_examResults.setFillinMan(user.getCnName());
					_examResults.setFillinManId(user.getResourceid());
					_examResults.setIsMachineExam(isMachineExam);
				}
				examResultsService.saveOrUpdate(_examResults);
				// 4、补考名单处理
				// 获取补考的考试批次年份和学期
				Long index = 1L;//上课学期
				for (Long i = year; i <= _year; i++) {
					for (int j = 1; j <= 2; j++) {
						if (term.equals(i + "_0" + j)) {
							break;
						}
						index++;
					}
				}
				
				if (index>=1 && index<=5) {//下学期
					if ("02".equals(year_term[1])) {
						_year = Long.parseLong(year_term[0]) + 1;
						_term = "1";
					} else {
						_year = Long.parseLong(year_term[0]);
						_term = "2";
					}
				} else {//第6学期
					_year = year +2;
					_term = "2";
				} 
				examSub = examSubService.findExamSubBycondition("Y", _year, _term);
				if(examSub == null ){
					message.append("请先添加\""+_year + "年第" + _term + "学期一补 \"这个考试批次！");
					continue;
				}
				
				String makeupHql = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? "
						+ " and a.course.resourceid=? and a.examResults.resourceid=? and teachingPlanCourse.resourceid=? and nextExamSubId=? ";
				//如果学生这门课没通过，查询补考表，有则替换成绩，没有则添加
				makeupList = studentMakeupListService.findByHql(makeupHql,abnormalExam.getStudentInfo().getResourceid(),
						abnormalExam.getCourse().getResourceid(),_examResults.getResourceid(),teachingPlanCourse.getResourceid(),examSub.getResourceid());
				if(null==makeupList || makeupList.size()==0){
					StudentMakeupList makeup = new StudentMakeupList();
					makeup.setStudentInfo(abnormalExam.getStudentInfo());
					makeup.setCourse(abnormalExam.getCourse());
					makeup.setExamResults(_examResults);
					makeup.setTeachingPlanCourse(teachingPlanCourse);
					makeup.setIsMachineExam(isMachineExam);
					makeup.setIsPass("N");
					makeup.setNextExamSubId(examSub.getResourceid());
					studentMakeupListService.saveOrUpdate(makeup);
				}
			}
		}
	}

	/**
	 * 给相关人员发送信息
	 * 
	 * @param abnormalExam
	 * @param checkStatus
	 */
	private void sendMessage(AbnormalExam abnormalExam, User user, int checkStatus) {

		StudentInfo studentTmp = abnormalExam.getStudentInfo();
		String unitCode 	   = studentTmp.getBranchSchool().getUnitCode();
		String student_userName = "";
		if (studentTmp.getSysUser() != null) {
			student_userName 		   = studentTmp.getSysUser().getUsername();
		}
		String courseName 		   = abnormalExam.getCourse().getCourseName();
		String studentName 		   = studentTmp.getStudentName();
		
		// 给申请缓考的学生发送提示信息
		if(!(studentTmp.getSysUser().getResourceid().equals(user.getResourceid()))) {
			sendTipsMessage(unitCode, "student",studentName, student_userName, courseName, checkStatus);
		}
		// 给教务员发送提示信息
		sendTipsMessage(unitCode, "acdemicDean",studentName, student_userName, courseName, checkStatus);
	
	}

	/**
	 * 发送提示信息
	 * 
	 * @param unitCode
	 * @param string
	 * @param studentName
	 * @param student_userName
	 * @param courseName
	 * @param checkStatus
	 */
	private void sendTipsMessage(String unitCode, String receiverRole,String studentName, String student_userName,
			String courseName,int checkStatus) {

		Message message = new Message();
//		MessageSender messageSender	= new MessageSender();
		MessageReceiver messageReceiver = new MessageReceiver();		
		String content 	= "";
		User user 	= SpringSecurityHelper.getCurrentUser();
		if("student".equals(receiverRole)){// 学生
			content = studentName+"同学:您的《"+courseName+"》这门课程申请缓考";
			messageReceiver.setReceiveType("user");
			messageReceiver.setUserNames(student_userName+",");
			if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT
					.equals(String.valueOf(checkStatus))){
				content = studentName+"同学:您的《"+courseName+"》这门课程申请了缓考，但未审核。";
			} else if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_PAST
					.equals(String.valueOf(checkStatus))){
				content += "已审核通过。";
			}else if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOPAST
					.equals(String.valueOf(checkStatus))){
				content += "审核未通过。";
			} else if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_REVOKE
					.equals(String.valueOf(checkStatus))){
				content += "已被撤销。";
			}
		}else if("acdemicDean".equals(receiverRole)){ // 教务员
			content = "贵中心的"+studentName+"同学《"+courseName+"》这门课程申请缓考";
			messageReceiver.setReceiveType("role");
			messageReceiver.setRoleCodes("ROLE_BRS_STUDENTSTATUS");
			
			messageReceiver.setOrgUnitCodes(unitCode);
		
			/*messageReceiver.setReceiveType("org",org);
			messageReceiver.setOrgUnitCodes(unitCode);//可以以后设置成全局参数
			messageReceiver.setRoleCodes("ROLE_BRS_STUDENTSTATUS");*/
			
			Map<String, Object> condition = new HashMap<String, Object>(20);
			StringBuffer userNames = new StringBuffer();
			condition.put("unitCode", unitCode);
			condition.put("userRole", "ROLE_BRS_STUDENTSTATUS");
			
			/*Page _page = new Page();
			_page.setPageSize(1000);
			Page userPage = userService.findUserByCondition(condition, _page);*/
			List<User> userList = userService.findUserByCondition(condition);
			if(CollectionUtils.isNotEmpty(userList)){
				User u;
				for(Object o:userList){
					u = (User)o;
					userNames.append(u.getUsername()+",");
				}
			}
			messageReceiver.setUserNames(userNames.toString());
			
			if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT
					.equals(String.valueOf(checkStatus))){
				content = "贵中心的"+studentName+"同学《"+courseName+"》这门课程申请了缓考，但还未审核。";
			} else if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_PAST
					.equals(String.valueOf(checkStatus))){
				content = "已同意"+content;
			}else if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOPAST
					.equals(String.valueOf(checkStatus))){
				content = "不同意"+content;
			} else if(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_REVOKE
					.equals(String.valueOf(checkStatus))){
				content += "已被撤销。";
			}
		}
		//	[温馨提示]  [06/27 09:35] 来自曹慧敏：李少健同学的申请缓考结果提示。
		message.setContent(content);
		message.setMsgTitle(studentName+"同学的申请缓考结果提示。");
		message.setMsgType("tips");
		message.setSendTime(new Date());
		message.setSender(user);	
		message.setSenderName(user.getCnName());
		message.setOrgUnit(user.getOrgUnit());
	/*	messageSender.setMessage(message);
		messageSender.setSender(user.getCnName());
		messageSender.setSenderId(user.getResourceid());
		messageSender.setOrgUnit(user.getOrgUnit());*/
			
		messageReceiver.setMessage(message);
		sysMsgService.saveMessage(message, messageReceiver,null);
//		sysMsgService.saveMessage_old(messageSender, messageReceiver,null);
	}

	/**
	 * 批量撤销
	 */
	@Override
	public Map<String, Object> batchRevoke(String[] abnormalExamIds, User user)
			throws ServiceException {
		Map<String ,Object> returnMap 	= new HashMap<String, Object>(10);
		StringBuffer message = new StringBuffer("");
		String statusCode = "200";
		try {
			if(abnormalExamIds != null && abnormalExamIds.length > 0){
				List<String> successList = new ArrayList<String>();
				List<AbnormalExam> successAbnormalExamList = new ArrayList<AbnormalExam>();
				for (String abnormalExamId : abnormalExamIds) {
					AbnormalExam abnormalExam = get(abnormalExamId);
					if(StringUtils.isNotEmpty(abnormalExam.getCheckStatus()) 
							&& AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT
							.equals(abnormalExam.getCheckStatus())){
						successList.add(abnormalExamId);
						successAbnormalExamList.add(abnormalExam);
					} else {
						if(StringUtils.isNotEmpty(message.toString()) && abnormalExamIds.length > 1){
							message.append("<br/>-------------------------------<br/>");
						}
						message.append(abnormalExam.getStudentInfo().getStudentName() + "的《" 
						+ abnormalExam.getCourse().getCourseName() +"》这门课程已审批，不能撤销！");
					}
				}
				if(successList != null && successList.size() > 0){
					batchTruncate(AbnormalExam.class, successList.toArray(new String[successList.size()]));
				}
				if(successAbnormalExamList != null && successAbnormalExamList.size() > 0){
					for(AbnormalExam ae : successAbnormalExamList){
						// 发送信息
						sendMessage(ae, user, Integer.valueOf(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_REVOKE));
					}
				}
			}
		} catch (Exception e) {
			logger.error("撤销出错", e);
			statusCode = "300";
			message.setLength(0);// 清空
			message.append("撤销失败");
		} finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message.toString());
		}
		return returnMap;
	}

	/**
	 * 保存/更新缓考申请
	 * 
	 * @param abnormalExam
	 * @param studentId
	 * @param courseId
	 * @param planCourseId
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> saveAbnormalExamApply(AbnormalExam abnormalExam,
			String studentId, String courseId, String planCourseId, User user)
			throws ServiceException {
		Map<String,Object> returnMap = new HashMap<String, Object>(10);
		String statusCode = "200";
		String message = "保存成功!";
		try {
			StudentInfo studentInfo = studentInfoService.get(studentId);
			Course course = courseService.get(courseId);
			//校验该用户不能重复申请 :一个学籍只能申请一次相同课程的免修免考	或者统考		
			message = checkAbnormalExam(studentId, courseId, studentInfo, course);
			if(StringUtils.isEmpty(message)){
				AbnormalExam _abnormalExam = null;
				abnormalExam.setStudentInfo(studentInfo);
				abnormalExam.setCourse(course);
				TeachingPlanCourse teachingPlanCourse = teachingPlanCourseService.get(planCourseId);
				abnormalExam.setTeachingPlanCourse(teachingPlanCourse);
				if(StringUtils.isNotBlank(abnormalExam.getResourceid())){ // 更新
					_abnormalExam = get(abnormalExam.getResourceid());
					if(_abnormalExam.getCheckStatus().equals(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT)){
						_abnormalExam.setReason(abnormalExam.getReason());
					}
				} else {
					abnormalExam.setApplyDate(new Date());
					abnormalExam.setApplyMan(user);
					abnormalExam.setApplyManName(user.getCnName());
					abnormalExam.setCheckStatus(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT);
				}
				if(_abnormalExam == null){
					_abnormalExam = abnormalExam;
				}
				saveOrUpdate(_abnormalExam);
				// 发送信息
				sendMessage(_abnormalExam, user, Integer.valueOf(AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOAUDIT));
				message = "保存成功!";
				logger.info("学生: "+studentInfo.getStudentName()+" 学号："+studentInfo.getStudyNo()+" 申请类型："
						+"正考 申请时间：" +ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)
						+" 操作人：" +user.getCnName()+" 操作人ID："+user.getResourceid());
			}
		} catch (Exception e) {
			logger.error("保存/更新缓考申请出错", e);
			statusCode = "300";
			message = "保存失败!";
		} finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message);		
		}
		
		return returnMap;
	}

	/**
	 * 检验缓考申请
	 * 
	 * @param studentId
	 * @param courseId
	 * @param message
	 * @param studentInfo
	 * @param course
	 * @return
	 */
	private String checkAbnormalExam(String studentId, String courseId,StudentInfo studentInfo, Course course) {
		String _message ="";
		String noExamApply_hql    = " from "+NoExamApply.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ?";
		String stateExam_hql    = " from "+StateExamResults.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ? ";
		
		List<NoExamApply> noExamApplyList 	 = noExamApplyService.findByHql(noExamApply_hql,0,studentId,courseId);
		List<StateExamResults> stateExamList = stateExamResultsService.findByHql(stateExam_hql, 0,studentId,courseId);
		if(CollectionUtils.isNotEmpty(noExamApplyList)||CollectionUtils.isNotEmpty(stateExamList)){
			_message = "学生 "+studentInfo.getStudentName()+" 已申请过 <font color='blue'>"+course.getCourseName()+"</font> 的免修免考或统考！"
					+ "<br/>如未审核，请联系教务管理人员.";
		}
		// 检查该学生的该门课程是否已经申请过缓考
		String abnormalExam_hql    = " from "+AbnormalExam.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ? ";
		List<AbnormalExam> abnormalExamList = findByHql(abnormalExam_hql, 0,studentId,courseId);
		if(CollectionUtils.isNotEmpty(abnormalExamList)){
			_message = "学生 "+studentInfo.getStudentName()+" 已申请过 <font color='blue'>"+course.getCourseName()+"</font> 的缓考！"
					+ "<br/>请刷新该页面查看！";
		}
		return _message;
	}
	
}
