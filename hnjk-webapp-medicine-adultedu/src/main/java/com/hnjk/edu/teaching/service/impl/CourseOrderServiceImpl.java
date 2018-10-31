package com.hnjk.edu.teaching.service.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.CourseOrder;
import com.hnjk.edu.teaching.model.CourseOrderStat;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.model.ReOrderSetting;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseBooks;
import com.hnjk.edu.teaching.service.ICourseBookService;
import com.hnjk.edu.teaching.service.ICourseOrderService;
import com.hnjk.edu.teaching.service.ICourseOrderStatService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSettingDetailsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IOrderCourseSettingService;
import com.hnjk.edu.teaching.service.IReOrderSettingService;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
/**
 * 
 * <code>学生课程预约ServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-13 上午10:56:14
 * @see 
 * @version 1.0
 */
@Service("courseOrderService")
@Transactional
public class CourseOrderServiceImpl extends BaseServiceImpl<CourseOrder>
									implements ICourseOrderService{
		
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("courseorderstatservice")
	private ICourseOrderStatService courseorderstatservice;

//	@Autowired
//	@Qualifier("courseBookOrderStatService")
//	private ICourseBookOrderStatService courseBookOrderStatService;

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	
	@Autowired
	@Qualifier("orderCourseSettingService")
	private IOrderCourseSettingService orderCourseSettingService;  
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examSettingDetailsService")
	private IExamSettingDetailsService examSettingDetailsService;
	
	@Autowired
	@Qualifier("courseBookService")
	private ICourseBookService courseBookService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("reOrderSettingService")
	private IReOrderSettingService reOrderSettingService;
	
	@Override
	@Transactional(readOnly=true)
	public CourseOrder getCourseOrderByStuIdAndCourseId(String[] params) throws ServiceException {
		
		StringBuffer hql = new StringBuffer("from CourseOrder courseOrder where courseOrder.isDeleted=0");
					 hql.append(" and courseOrder.studentInfo.resourceid=? ");
					 hql.append(" and courseOrder.courseOrderStat.resourceid=?");
					 hql.append(" and courseOrder.courseOrderStat.course.resourceid=?");
					 
		List<CourseOrder> list =  (List<CourseOrder>)this.exGeneralHibernateDao.findByHql(hql.toString(), params);
		if (null!=list && !list.isEmpty()){
			return list.get(0);
		}else {
			return null;
		}	
		
	}

	/**
	 * 根据学号查询学生是否交费
	 * @param matriculateNoticeNo
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map checkFeeByMatriculateNoticeNo(String matriculateNoticeNo) throws ServiceException {
		
		Map<String,Object>  resultMap = new HashMap <String,Object>() ;
		try {
			
			Long count                = 0L ;
			Date curDate              = new Date();
			int year                  = Integer.parseInt(DateUtils.getFormatDate(curDate,"yyyy"));
			
			resultMap.put("matriculateNoticeNo", matriculateNoticeNo);
			resultMap.put("year",year);
			
			StringBuffer sql          = new StringBuffer("select count(StudentID) from EDU_ROLL_FISTUDENTFEE where StudentID=:matriculateNoticeNo");
			sql.append(" and ChargeYear=:year and ( (FactPayFee+deratefee) < RecPayFee or FactPayFee is null  )");
		
			//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			//int count                 = jdbcTemplate.queryForInt(sql.toString());
			
			count                     = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql.toString(), resultMap);
			
			if(count>0){
				resultMap.put("isFee", false);
				resultMap=setResultMapMsg(resultMap,"未交费");
				return resultMap;
			}else {
				resultMap.put("isFee", true);
				resultMap=setResultMapMsg(resultMap,"已交费");
			}
			
		} catch (Exception e) {
			resultMap.put("isFee", false);
			resultMap=setResultMapMsg(resultMap,"获取学费信息出错!"+e.fillInStackTrace());
			logger.error("查询学费出错:{}"+e.fillInStackTrace());
			
		}
		
		return resultMap;
	}
	/**
	 * 根据学号查询学生是否交费--学生预约时使用
	 * @param matriculateNoticeNo
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map checkFeeByMatriculateNoticeNoForCourseOrder(String matriculateNoticeNo) throws ServiceException {
		
		Map<String,Object>  resultMap = new HashMap <String,Object>() ;
		try {
			
			
			Long count                = 0L ;
			Date curDate              = new Date();
			int year                  = Integer.parseInt(DateUtils.getFormatDate(curDate,"yyyy"));
			
			resultMap.put("matriculateNoticeNo", matriculateNoticeNo);
			resultMap.put("year",year);
			
			StringBuffer sql          = new StringBuffer("select count(StudentID) from EDU_ROLL_FISTUDENTFEE where StudentID=:matriculateNoticeNo");
			sql.append(" and ChargeYear=:year and ( (FactPayFee+deratefee) < RecPayFee or FactPayFee is null )");
		
			//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			//int count                 = jdbcTemplate.queryForInt(sql.toString());
			
			count                     = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql.toString(), resultMap);
			
			if(count>0){
				resultMap.put("isFee", false);
				resultMap=setResultMapMsg(resultMap,"未交费");
				return resultMap;
			}else {
				resultMap.put("isFee", true);
				resultMap=setResultMapMsg(resultMap,"已交费");
			}
			
		} catch (Exception e) {
			resultMap.put("isFee", false);
			resultMap=setResultMapMsg(resultMap,"获取学费信息出错!"+e.fillInStackTrace());
			logger.error("查询学费出错:{}"+e.fillInStackTrace());
			
		}
		return resultMap;

		
	}
	
	/**
	 * 查询一个课程预约记录(删除预约)
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public CourseOrder findCourseOrderForDelCourse(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from CourseOrder courseOrder where courseOrder.isDeleted=0");
		hql.append(" and  courseOrder.studentInfo.resourceid=:studentId");
		hql.append(" and  courseOrder.courseOrderStat.yearInfo.resourceid=:yearInfoId");
		hql.append(" and  courseOrder.courseOrderStat.course.resourceid=:courseId");
		hql.append(" and  courseOrder.courseOrderStat.term=:term");
		
		return exGeneralHibernateDao.findUnique(hql.toString(), condition);
	}
	/**
	 * 删除课程预约-根据学习计划ID删除课程预约及相关的记录
	 * @param leanrPlanId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String,Object> delCourseOrderByLearnPlanId(Map<String,Object> condition) throws Exception {
		
		List<OrderCourseSetting> list 	  = (List<OrderCourseSetting>)condition.get("orderCourseSetting");
		boolean isDelSuccess              = false;
		//有开放的预约批次
		if (null!=list && list.size()>0) {
			StudentLearnPlan learnPlan    = (StudentLearnPlan)condition.get("learnPlan");
			String studentId              = ExStringUtils.defaultIfEmpty(learnPlan.getStudentInfo().getResourceid(),"");
			String operater               = null==condition.get("operater")?"":condition.get("operater").toString();
			String notLimitflag           = null==condition.get("notLimitflag")?"":condition.get("notLimitflag").toString();
			Date settingEndDate 		  = list.get(0).getEndDate();
			Date curDate        		  = new Date();
			Date allowDate                = null;
			if (null==settingEndDate) {
				settingEndDate= curDate; //如果批次未设置预约结束时间，则等于当前时间
			}
			
			//要求批次预约结束时间不为空 
			if ("manager".equals(operater)&& null!=list.get(0).getEndDate() ){	 //教务员允许删除的时间为：年度预约权限结束时间加7天
				 allowDate     			  = ExDateUtils.addDays(settingEndDate, 7);
			}else if("student".equals(operater)&& null!=list.get(0).getEndDate()){//学生允许删除的时间为：年度预约权限结束时间
				 
				 Map<String,Object> paramMap = new HashMap<String, Object>();
				 paramMap.put("branchSchool", learnPlan.getStudentInfo().getBranchSchool().getResourceid());
				 paramMap.put("reOrderType", "1");
				 paramMap.put("orderCourseSetting", list.get(0).getResourceid());
					
				 List<ReOrderSetting> reOrderSet = reOrderSettingService.findReOrderSettingByCondition(paramMap);
				//补预约时间
				if (null!=reOrderSet&&reOrderSet.size()>0&&
					curDate.getTime()>=reOrderSet.get(0).getStartTime().getTime()&&
					curDate.getTime()<=reOrderSet.get(0).getEndTime().getTime()) {
					 allowDate            = reOrderSet.get(0).getEndTime();
				}else {
					allowDate                = settingEndDate;
				}
			}
			//如果是个别补预约功能中的删除,不限定在预约批次时间范围内
			if(ExStringUtils.isNotBlank(notLimitflag)){
				allowDate                    = ExDateUtils.addDays(curDate,1);
			}
			if (curDate.getTime() <= allowDate.getTime()) {
				
				String courseId            = null==learnPlan.getTeachingPlanCourse()?learnPlan.getPlanoutCourse().getResourceid():learnPlan.getTeachingPlanCourse().getCourse().getResourceid();
				
				String yearInfoId          = ExStringUtils.defaultIfEmpty(learnPlan.getYearInfo().getResourceid(),"");
				String term                = ExStringUtils.defaultIfEmpty(learnPlan.getTerm(),"");
				
			
				condition.put("course", null==learnPlan.getTeachingPlanCourse()?learnPlan.getPlanoutCourse():learnPlan.getTeachingPlanCourse().getCourse());
				condition.put("yearInfoId", yearInfoId);
				condition.put("studentId", studentId);
				condition.put("courseId", courseId);
				condition.put("term", term);
				
				if (learnPlan.getStatus()==1) {
					//1、删除课程预约明细及统计记录
					delCourseOrderAndCourseOrderStat(condition);
					//2、删除教材预约及统计记录
					//delCourseBookOrderAndCourseBookOrderStat(condition);
					//3、删除学习计划记录
					delStudentlearnPlan(condition);
					isDelSuccess   = true;
				}else {
					isDelSuccess   = false;
					condition  = setResultMapMsg(condition,"请传入正确的学习计划！");
				}

			}else {
					String msg = "请在规定的时间内完成预约删除操作，时间为:"+ExDateUtils.formatDateStr(allowDate, ExDateUtils.PATTREN_DATE_TIME);
					condition  = setResultMapMsg(condition,msg);
			}
			
		//没有开的预约批次，不允许删除,因为无法比较预约结止时间
		}else {
			String msg = "没有开的预约批次，未知预约结束时间不允许删除!";
			condition  = setResultMapMsg(condition,msg);
		}
		condition.put("isDelSuccess", isDelSuccess);
		return condition;
	}
	/**
	 * 删除课程预约-删除课程预约明细及统计记录
	 * @param condition
	 * @throws ServiceException
	 */
	public void delCourseOrderAndCourseOrderStat(Map<String,Object> condition)throws ServiceException{
		
		String yearInfo  	  	  = ExStringUtils.defaultIfEmpty(condition.get("yearInfoId").toString(),"");
		String term 	 	  	  = ExStringUtils.defaultIfEmpty(condition.get("term").toString(),"");
		String studentId 	  	  = ExStringUtils.defaultIfEmpty(condition.get("studentId").toString(),"");
		String courseId  	  	  = ExStringUtils.defaultIfEmpty(condition.get("courseId").toString(),"");
		
		CourseOrderStat orderStat = courseorderstatservice.findCourseOrderStatByYearInfoAndCourse(yearInfo, courseId, term);
		int oldOrderNum 		  = null==orderStat?0:orderStat.getOrderNum();
		CourseOrder delOrder	  = null;
		
		if (null!=orderStat) {
			for(CourseOrder order:orderStat.getCourseOrders()){
				if (studentId.equals(order.getStudentInfo().getResourceid())) {
					delOrder = order;
					break;
				}
			}
			
			orderStat.getCourseOrders().remove(delOrder);
			
			orderStat.setOrderNum(oldOrderNum<=0?0:oldOrderNum-1);
			
			courseorderstatservice.update(orderStat);
		}
		

	}	
	/**
	 * 删除课程预约-删除教材预约明细及统计记录
	 * @param condition
	 * @throws ServiceException
	 */
	public void delCourseBookOrderAndCourseBookOrderStat(Map<String,Object> condition)throws ServiceException{
		
		/*
		 
		版本一: 一个教学计划课程关联一个教材
		Course course 		    = (Course)condition.get("course");
		String hql   			= "from "+CourseBook.class.getSimpleName()+" c where c.isDeleted=0 and c.status=1 and c.course.resourceid=?";
		//CourseBook courseBook = courseBookService.findUniqueByProperty("course.resourceid", course.getResourceid());
		CourseBook courseBook   = courseBookService.findByHql(hql, course.getResourceid()).get(0);
		
		String yearInfo  = ExStringUtils.defaultIfEmpty(condition.get("yearInfoId").toString(),"");
		String term 	 = ExStringUtils.defaultIfEmpty(condition.get("term").toString(),"");
		String courseId  = ExStringUtils.defaultIfEmpty(condition.get("courseId").toString(),"");
		String studentId = ExStringUtils.defaultIfEmpty(condition.get("studentId").toString(),"");
		
		CourseBookOrderStat courseBookOrderStat = courseBookOrderStatService.getCourseBookOrdersByYearInfoAndCourse(yearInfo, term, courseBook.getResourceid());
		Long oldOrderNum						= courseBookOrderStat.getOrderNum();
		CourseBookOrders delCourseBookOrder 	= null;
		
		for(CourseBookOrders courseBookOrder:courseBookOrderStat.getOrders()){
			if (studentId.equals(courseBookOrder.getStudentInfo().getResourceid())) {
				delCourseBookOrder = courseBookOrder;
				break;
			}
		}

		courseBookOrderStat.getOrders().remove(delCourseBookOrder);
		courseBookOrderStat.setOrderNum(oldOrderNum<=0?0:oldOrderNum-1);
		
		courseBookOrderStatService.update(courseBookOrderStat);*/
		
		
		
		//版本二:一个教学计划课程关联多个教材
		String yearInfo  	  	      			= ExStringUtils.defaultIfEmpty(condition.get("yearInfoId").toString(),"");
		String term 	 	  	      			= ExStringUtils.defaultIfEmpty(condition.get("term").toString(),"");
		String studentId 	  	      			= ExStringUtils.defaultIfEmpty(condition.get("studentId").toString(),"");
		
		StudentLearnPlan stuLearnPlan 			= (StudentLearnPlan)condition.get("learnPlan");
		TeachingPlanCourse planCourse 			= stuLearnPlan.getTeachingPlanCourse();         //计划内课程
		Course course                           = stuLearnPlan.getPlanoutCourse();              //计划外课程
		Set<TeachingPlanCourseBooks> courseBook = null;                                         //计划内课程的教材
		if (null!=planCourse) {
			 courseBook                         = planCourse.getTeachingPlanCourseBooks();      
		}
		
		//计划内课程预约教材明细对象
		/*if (null!=courseBook) {
			for (TeachingPlanCourseBooks tcb:courseBook) {
				//年度教材预约统计对象
				CourseBookOrderStat courseBookOrderStat     = courseBookOrderStatService.getCourseBookOrdersByYearInfoAndCourse(yearInfo, term, tcb.getCourseBook().getResourceid());
				
				if (null!=courseBookOrderStat) {
					Long oldOrderNum						= null==courseBookOrderStat.getOrderNum()?0L:courseBookOrderStat.getOrderNum();
					CourseBookOrders delCourseBookOrder 	= null;
					
					for(CourseBookOrders courseBookOrder:courseBookOrderStat.getOrders()){
						if (studentId.equals(courseBookOrder.getStudentInfo().getResourceid())) {
							delCourseBookOrder = courseBookOrder;
							break;
						}
					}

					courseBookOrderStat.getOrders().remove(delCourseBookOrder);
					courseBookOrderStat.setOrderNum(oldOrderNum<=0?0:oldOrderNum-1);
					
					courseBookOrderStatService.update(courseBookOrderStat);
				}
			}
		}*/
		
		//计划外课程预约教材明细对象
		/*if (null!=course) {
			//要进行操作的计划外课程教材
			List<CourseBook> outPlanCourseBooks 			= courseBookService.findByHql("from "+CourseBook.class.getSimpleName()+" c where c.isDeleted=0 and c.status=1 and c.course.resourceid=?", course.getResourceid());
			for (CourseBook book : outPlanCourseBooks) {
				
				//年度预约教材统计对象
				CourseBookOrderStat oldstat 			    = courseBookOrderStatService.getCourseBookOrdersByYearInfoAndCourse(yearInfo,term,book.getResourceid());
				
				if (null!=oldstat) {
					
					Long oldOrderNum						= null==oldstat.getOrderNum()?0L:oldstat.getOrderNum();
					CourseBookOrders delCourseBookOrder 	= null;
					
					for (CourseBookOrders order:oldstat.getOrders()) {
						if (studentId.equals(order.getStudentInfo().getResourceid())) {
							delCourseBookOrder              = order;
							break;
						}
					}
					
					oldstat.getOrders().remove(delCourseBookOrder);
					oldstat.setOrderNum(oldOrderNum<=0?0:oldOrderNum-1);
					
					courseBookOrderStatService.update(oldstat);
				}

			
			}
		}*/
	}
	/**
	 * 删除课程预约-删除学习计划
	 * @param condition
	 * @throws ServiceException
	 */
	
	public void delStudentlearnPlan(Map<String,Object> condition)throws ServiceException{
		studentLearnPlanService.delete(condition.get("learnPlanId").toString());
	}
	/**
	 * 删除考试预约-根据学习计划ID删除考试预约及相关的记录
	 */
	@Override
	public Map<String, Object> delExamOrderByLearnPlanId(Map<String, Object> condition) throws ServiceException {
		
		List<ExamSub> subList      		  = (List<ExamSub>)condition.get("subList");
		boolean isDelSuccess              = false;
		//有开放的预约批次
		if (null!=subList && !subList.isEmpty()) {
			StudentLearnPlan learnPlan    = (StudentLearnPlan)condition.get("learnPlan");
			String studentId              = ExStringUtils.defaultIfEmpty(learnPlan.getStudentInfo().getResourceid(),"");
			String operater               = null==condition.get("operater")?"":condition.get("operater").toString();
			Date settingEndDate 		  = subList.get(0).getEndTime();
			Date examsubEndTime           = subList.get(0).getExamsubEndTime();
			Date curDate        		  = new Date();
			Date allowDate                = null;
			if (null==settingEndDate) {
				settingEndDate= curDate; //如果批次未设置预约结束时间，则等于当前时间
			}
			if (null==examsubEndTime) {
				examsubEndTime= curDate;
			}
			//要求批次预约结束时间不为空 
			if ("manager".equals(operater) &&null!=subList.get(0).getExamsubEndTime()){	 //教务员允许删除的时间为：考试批次预约信息结束时间加7天
				 allowDate     			  = ExDateUtils.addDays(examsubEndTime, 7);
			}else if("student".equals(operater)&&null!=subList.get(0).getEndTime()){     //学生允许删除的时间为：考试批次预约信息结束时间
				 
				 Map<String,Object> paramMap = new HashMap<String, Object>();
				 paramMap.put("branchSchool", learnPlan.getStudentInfo().getBranchSchool().getResourceid());
				 paramMap.put("reOrderType", "2");
				 paramMap.put("examSubId", subList.get(0).getResourceid());
					
				 List<ReOrderSetting> reOrderSet = reOrderSettingService.findReOrderSettingByCondition(paramMap);
					
				//补预约时间
				if (null!=reOrderSet&&reOrderSet.size()>0&&
						curDate.getTime()>=reOrderSet.get(0).getStartTime().getTime()&&
						curDate.getTime()<=reOrderSet.get(0).getEndTime().getTime()) {
					 allowDate            = reOrderSet.get(0).getEndTime();
				}else {
					 allowDate            = settingEndDate;
				}
			}

			//当前时间小于预约结束时间
			if (curDate.getTime() <= allowDate.getTime()) {
					
				
					String courseId            = null==learnPlan.getTeachingPlanCourse()?learnPlan.getPlanoutCourse().getResourceid():learnPlan.getTeachingPlanCourse().getCourse().getResourceid();
					
					String yearInfo            = ExStringUtils.defaultIfEmpty(learnPlan.getOrderExamYear().getResourceid(),"");
					String term                = ExStringUtils.defaultIfEmpty(learnPlan.getOrderExamTerm(),"");
					String rsId                = learnPlan.getExamResults().getResourceid();
					ExamResults examResults    = examResultsService.get(rsId);
					
					if (examResults!=null) {
						
						Examroom examroom   = examResults.getExamroom();
						//String examAbnormity= ExStringUtils.defaultIfEmpty(examResults.getExamAbnormity(),"");
						String writtenScore = ExStringUtils.defaultIfEmpty(examResults.getWrittenScore(), "");
						String examSeatNum  = ExStringUtils.defaultIfEmpty(examResults.getExamSeatNum(),"");
						String isDelayExam  = ExStringUtils.defaultIfEmpty(examResults.getIsDelayExam(),"");
						
						if (examroom ==null&&                                    //未按排考试       
							//ExStringUtils.isEmpty(examAbnormity)&&             //成绩异常代码为空
							ExStringUtils.isEmpty(writtenScore)&&                //成绩为空
							ExStringUtils.isEmpty(examSeatNum)&&                 //座位为空
							("".equals(isDelayExam)||"N".equals(isDelayExam))&&  //未申请缓考
							2==learnPlan.getStatus()) {                          //学习计划状态为2(预约考试)
							
							StringBuffer hql 		   = new StringBuffer();
						
							hql.append(" from "+CourseOrder.class.getSimpleName()+" order where order.isDeleted=? and order.studentInfo.resourceid=? ");
							hql.append("  and order.orderExamYear.resourceid=? and order.orderExamTerm=? and order.courseOrderStat.course.resourceid=? ");
							
							List<CourseOrder> list = (List<CourseOrder>) exGeneralHibernateDao.findByHql(hql.toString(),0,studentId,yearInfo,term,courseId);
							
							//1.修改课程预约中的状态
							for (CourseOrder order:list) {
								
								order.setStatus(1L);
								order.setOrderExamTerm(null);
								order.setOrderExamTime(null);
								order.setOrderExamYear(null);
								
								exGeneralHibernateDao.saveOrUpdate(order);
							}	
							
							//2.修改学习计划中的状态
							learnPlan.setStatus(1);
							learnPlan.setExamResults(null);
							learnPlan.setExamInfo(null);
							learnPlan.setOrderExamTerm(null);
							learnPlan.setOrderExamYear(null);
							
							studentLearnPlanService.update(learnPlan);
							
							//3.删除考试预约记录
							examResultsService.truncate(examResults);
							isDelSuccess   = true;
						}else {
							String msg = "删除失败,已经安排座位的预约或者已经考过试取得成绩的不允许删除！";
							condition = setResultMapMsg(condition,msg);
						}
					}else {
						String msg = "未找到考试预约记录!";
						condition = setResultMapMsg(condition,msg);
					}
			
			//当前时间大于预约结束时间
			}else {
				
				try {
					String msg = "请在规定的时间内完成预约删除操作，时间为:"+ExDateUtils.formatDateStr(allowDate, ExDateUtils.PATTREN_DATE_TIME);
					condition  = setResultMapMsg(condition,msg);
				} catch (Exception e) {
					String msg = "请在规定的时间内完成预约删除操作，预约结束时间加7天!";
					condition  = setResultMapMsg(condition,msg);
				}
			}
			
			
		//没有开的预约批次，不允许删除,因为无法比较预约结止时间
		}else {
			String msg = "没有开的预约批次，未知预约结束时间不允许删除!";
			condition  = setResultMapMsg(condition,msg);
		}
		condition.put("isDelSuccess", isDelSuccess);
		return condition;
	}

	/**
	 * 根据学习计划ID删除毕业论文预约及相关的记录
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> delGraduatePaperByLearnPlanId(Map<String, Object> condition) throws ServiceException {
		
		List<ExamSub> subList      		   = (List<ExamSub>)condition.get("subList");
		//有开放的论文批次
		if (null!=subList && !subList.isEmpty()) {
			Date settingEndDate 		   = subList.get(0).getEndTime();
			Date curDate        		   = new Date();
			if (null!=settingEndDate) {
				settingEndDate = ExDateUtils.addDays(settingEndDate, 1); //由于数据库中保存的日期格式是2011-8-10，而业务真实需求是当天还可以操作
			}
			if (null==settingEndDate) {
				settingEndDate = curDate; 							    //如果批次未设置预约结束时间，则等于当前时间
			}
			Date allowDate                 = settingEndDate;
			
			//当前时间小于预约结束时间
			if (curDate.getTime() <= allowDate.getTime()) {
				
				StudentLearnPlan learnPlan = (StudentLearnPlan)condition.get("learnPlan");
				String courseId            = ExStringUtils.defaultIfEmpty(learnPlan.getTeachingPlanCourse().getCourse().getResourceid(),"");
				String studentId           = ExStringUtils.defaultIfEmpty(learnPlan.getStudentInfo().getResourceid(),"");
				Map<String,Object> paramMap= new HashMap<String, Object>();
				
				paramMap.put("courseId", courseId);
				paramMap.put("studentInfoId", studentId);
				paramMap.put("examSubId", subList.get(0).getResourceid());
				
				String hql 				   = " delete from "+GraduatePapersOrder.class.getSimpleName();
				hql						  += " paper where paper.isDeleted=0 and paper.studentInfo.resourceid=:studentInfoId and paper.examSub.resourceid=:examSubId";
				//hql       				  += " and paper.course.resourceid=:courseId and paper.status = 0";
				//2012-06-15,K0528C_毕业论文功能调整:取消毕业论文申请的审核步骤
				hql       				  += " and paper.course.resourceid=:courseId ";
				
				int counts                 = exGeneralHibernateDao.executeHQL(hql,paramMap);
				if (counts>0) {
					ExamResults examResults= null==learnPlan?null:learnPlan.getExamResults();
					ExamInfo info          = null==examResults?null:examResults.getExamInfo();
					if(null!=examResults&&"4".equals(examResults.getCheckStatus())){
						learnPlan.setStatus(3);
						if (null!=info) {
							learnPlan.setYearInfo(info.getExamSub().getYearInfo());
							learnPlan.setTerm(info.getExamSub().getTerm());
						}
						studentLearnPlanService.update(learnPlan);
					}else {
						studentLearnPlanService.delete(learnPlan);
					}
					
				}else{
					condition  				= setResultMapMsg(condition,"论文预约已审核，不能撤销！");
				}
				
				
			}else {
				try {
					String msg = "请在规定的时间内完成预约删除操作，时间为:"+ExDateUtils.formatDateStr(allowDate, ExDateUtils.PATTREN_DATE_TIME);
					condition  = setResultMapMsg(condition,msg);
				} catch (Exception e) {
					String msg = "请在规定的时间内完成预约删除操作!";
					condition  = setResultMapMsg(condition,msg);
				}
			}
			
		//没有开的预约批次，不允许删除,因为无法比较预约结止时间
		}else {
			String msg = "没有开的论文预约批次，未知预约结束时间不允许删除!";
			condition  = setResultMapMsg(condition,msg);
		}
		return condition;
	}
	/**
	 * 预约计划外课程
	 * @param info         学籍对象
	 * @param courseId     课程ID
	 * @param orderType    预约类型
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> orderOutPlanCourse(StudentInfo info,String courseId,String orderType) throws ServiceException {
		
		Map<String,Object> paramMap  	 = new HashMap<String, Object>();
		List<StudentLearnPlan> learnPlan = new ArrayList<StudentLearnPlan>();

		Course course                    = courseService.get(courseId);
		boolean  operatingStatus         = false;

		paramMap.put("studentInfo", info);
		paramMap.put("course", course);
		//当前用户是否为学生
		if (SpringSecurityHelper.isUserInRole("ROLE_STUDENT")) {
			paramMap.put("operater", "student");
		}else {
			paramMap.put("operater", "teacher");
		}
		if ("orderCourse".equals(orderType)) {
			
			learnPlan                    = studentLearnPlanService.getStudentLearnPlanList(info.getResourceid(),info.getTeachingPlan().getResourceid());//学生的学习计划
			paramMap.put("learnPlan", learnPlan);
			//1.检查是否允许预约计划外课程
			paramMap                     = isAllowOrderOutPlanCoure(paramMap);
			boolean  isArrowOrderCourse  = (Boolean)paramMap.get("isArrowOrderCourse");
			
			//2.1允许预约计划外课程
			if (isArrowOrderCourse) {
				
				//2.1.1 通过预约条件检查时执行的操作
				paramMap                 = allowOrderOutPlanCourseOperating(paramMap);
				boolean  operateStatus   = (Boolean)paramMap.get("orderCourseOperateStatus");
				
				if (operateStatus) {
					paramMap.put("operatingStatus", true);
				}else {
					paramMap.put("operatingStatus", false);
				}
				
			//2.2不允许预约计划外课程	
			}else {           
				paramMap.put("operatingStatus", false);
			}
		}else if ("orderExam".equals(orderType)) {
			learnPlan                    = studentLearnPlanService.findByHql("from StudentLearnPlan plan where plan.isDeleted=0 and plan.studentInfo.resourceid =? and plan.planoutCourse is not null ", new String[]{info.getResourceid()});//学生的学习计划
			paramMap.put("learnPlan", learnPlan);
			
			
			//1.检查是否允许预约计划外课程的考试
			paramMap                     = isAllowOrderExamForOutPlanCourse(paramMap);
			boolean  isAllowOrderExam    = (Boolean)paramMap.get("isAllowOrderExam");
			//2.1允许预约计划外课程的考试
			if (isAllowOrderExam) {
				
				//2.1.1 通过预约条件检查时执行的操作
				paramMap                 = allowOrderExamOperate(paramMap);
				boolean  operateStatus   = (Boolean)paramMap.get("orderExamOperateStatus");
				if (operateStatus) {
					paramMap.put("operatingStatus", true);
				}else {
					paramMap.put("operatingStatus", false);
				}
				
			//2.2不允许预约计划外课程的考试		
			}else {
				paramMap.put("operatingStatus", false);
			}
		}
		
		return paramMap;
	}
	//-----------------------------------------------------------预约学习计划外课程-----------------------------------------------------------
	/**
	 * 预约学习-是否允许预约计划外课程
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object>  isAllowOrderOutPlanCoure(Map<String,Object> paramMap){
		
		try {
			boolean isAllowStatus        = false;
			boolean yearOrderStatus 	 = false;		//年度的预约状态
			boolean isArrowOrderCourse   = false;		//是否允许预约课程
			StudentInfo info             = (StudentInfo)paramMap.get("studentInfo");
			TeachingPlan plan		     = info.getTeachingPlan();
			List<OrderCourseSetting> settingList = orderCourseSettingService.findOpenedSetting();
			List<StudentLearnPlan> learnPlan     = studentLearnPlanService.getStudentLearnPlanList(info.getResourceid(),plan.getResourceid());	//学生的学习计划
			paramMap.put("learnPlan", learnPlan);
			//补预约权限记录
			List<ReOrderSetting> reOrderSet  = null;
			
			if (null!=settingList&&!settingList.isEmpty()) {
				
				OrderCourseSetting setting=settingList.get(0);
				
				Date curTime       		 = new Date();
				Date startOrderTime 	 = setting.getStartDate();
				Date endOrderTime   	 = setting.getEndDate();
				
				if (null!=startOrderTime && null!=endOrderTime) {
					
					if (curTime.getTime()>= startOrderTime.getTime() &&
						curTime.getTime()<= endOrderTime.getTime()) {//在预约时限范围内
						
						yearOrderStatus  = true;
					}else {
						paramMap.put("branchSchool", info.getBranchSchool().getResourceid());
						paramMap.put("reOrderType", "1");
						paramMap.put("orderCourseSetting", setting.getResourceid());
						
						reOrderSet       = reOrderSettingService.findReOrderSettingByCondition(paramMap);
						paramMap.remove("branchSchool");
						paramMap.remove("reOrderType");
						paramMap.remove("orderCourseSetting");
						//补预约时间
						if (null!=reOrderSet&&reOrderSet.size()>0&&
							curTime.getTime()>=reOrderSet.get(0).getStartTime().getTime()&&
							curTime.getTime()<=reOrderSet.get(0).getEndTime().getTime()) {
							yearOrderStatus  = true;
						}else {
							yearOrderStatus  = false;
							String msg       = "当前不在预约时间范围内,如果已超过预约时间请与教务人员联系！";
							paramMap         = setResultMapMsg(paramMap,msg);
						}
						
					}
				}else {
					String msg      	 = "当前年度预约权限未设置时间范围,请联系教务管理员！";
					paramMap        	 = setResultMapMsg(paramMap,msg);
					yearOrderStatus 	 = false;
				}
				paramMap.put("orderCourseSetting", settingList.get(0));
			}else {
				String msg      		 = "年度预约未开放！";
				paramMap        		 = setResultMapMsg(paramMap,msg);
				yearOrderStatus 		 = false;
			}
			
			//个人的预约状态
			boolean personalOrderStatus  = 1 ==info.getOrderCourseStatus()? true:false;
			//检查是否已超过年度预约上限
			//paramMap 					 = studentLearnPlanService.checkIsFullInStudentLearnPlan(paramMap);
			//boolean isFullInStudentLearnPlan =(Boolean) paramMap.get("isFullInStudentLearnPlan");
			boolean isFullInStudentLearnPlan =true;
		
			//学籍状态
			String studentstatus         = null==info.getStudentStatus()?"":info.getStudentStatus();
			
			if ("11".equals(studentstatus)) {
				isAllowStatus            = true;
			}
			//是否交费
			Map isFeeMap  				 = this.checkFeeByMatriculateNoticeNoForCourseOrder(info.getStudyNo());
			boolean isFee 				 = (Boolean)isFeeMap.get("isFee");
			
			//检查考试时间冲突
			paramMap					 = checkExamConFlictForOrderOutPlanCoure(paramMap);
			boolean isExamConFlictPass   = (Boolean)paramMap.get("isExamConFlictPass");
			
			
			//当已交费，年度预约开放，个人预约权限开放,未超过年度预约上限的情况下才允许进行预约学习
			if (yearOrderStatus && personalOrderStatus&&isFullInStudentLearnPlan==false && isFee&&isAllowStatus&&isExamConFlictPass) {	
				isArrowOrderCourse       =   true;
			}else {
				if (isAllowStatus == false) {
					String msg         	 = "系统只允许在学状态的学员预约，您当前的学籍状态为："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", studentstatus);
					paramMap             = setResultMapMsg(paramMap,msg);
				}
				if(isFee == false){
					String msg         	 = "你的预约权限未开放，原因为未交费！";
					paramMap             = setResultMapMsg(paramMap,msg);
				}
				if (personalOrderStatus==false) {
					String msg           = "你的预约权限未开放，请联系管理人员！";
					paramMap             = setResultMapMsg(paramMap,msg);
				}
				if (isFullInStudentLearnPlan) {
					OrderCourseSetting setting = (OrderCourseSetting)paramMap.get("orderCourseSetting");
					String yearInfoName        = setting.getYearInfo().getYearName()+"-"+JstlCustomFunction.dictionaryCode2Value("CodeTerm", setting.getTerm());
					String msg                 = "你当已达到本年度预约学习上限！<br/>"+yearInfoName+",允许预约学习的课程数为："+setting.getLimitOrderNum();
					paramMap                   = setResultMapMsg(paramMap,msg);
				}
				isArrowOrderCourse 		 = false;
			}
			paramMap.put("isArrowOrderCourse",isArrowOrderCourse);
		} catch (Exception e) {
			logger.error("检查是否允许预约计划外课程出错："+e.fillInStackTrace());
			paramMap.put("isArrowOrderCourse",false);
			String msg = "检查是否允许预约计划外课程出错："+e.getMessage();
			paramMap = setResultMapMsg(paramMap, msg);
		}
		
		return paramMap;
	}
	/**
	 * 预约学习-预约计划外课程:检查考试冲突
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object>  checkExamConFlictForOrderOutPlanCoure(Map<String,Object> paramMap)throws ServiceException{
		
		List<StudentLearnPlan> learnPlan     = (List<StudentLearnPlan>)paramMap.get("learnPlan");
		Course course                        = (Course)paramMap.get("course");
		if (null!=learnPlan && !learnPlan.isEmpty()) {
			
			//年度预约设置
			OrderCourseSetting setting 		 = (OrderCourseSetting) paramMap.get("orderCourseSetting");
			
			String settingYearInfoId         = setting.getYearInfo().getResourceid();
			String settingTerm               = setting.getTerm();
			String detailsHQL 			     = "from "+ExamSettingDetails.class.getSimpleName()+" details where details.isDeleted=0 and  (details.isSpecial != 'Y' or  details.isSpecial is null) and details.courseId.resourceid=?";
			
			//预约课程的考试时间设置对象
			ExamSettingDetails orderCourseExamDetails =  null;
			List<ExamSettingDetails> detailsList      = examSettingDetailsService.findByHql(detailsHQL,course.getResourceid());
			if (null!=detailsList && !detailsList.isEmpty()) {
				orderCourseExamDetails = detailsList.get(0);
			}
			boolean isExamConFlictPass = true;
			boolean isNeedChooseCourse = false;
			
			if (null!=orderCourseExamDetails) {
				//预约课程的考试设置名称 如:  第一天上午   第一天下午   第二天上午.....
				String orderCourseExamSettingName=orderCourseExamDetails.getExamSetting().getSettingName();
				
				for (StudentLearnPlan plan:learnPlan) {
					
					//学习计划中已预约的计划外课程ID
					String outPlanCourseId   = null==plan.getPlanoutCourse()?"":plan.getPlanoutCourse().getResourceid();
					
					//学习计划中的课程ID
					String learnPlanCourseId = plan.getTeachingPlanCourse().getCourse().getResourceid();
					String planYearInfoId    = plan.getYearInfo().getResourceid();
					String planTerm          = plan.getTerm();
					
					
					//学习计划中的课程考试时间设置对象
					ExamSettingDetails learnPlanCourseExamDetails            =  null;
					List<ExamSettingDetails> learnPlanCourseExamDetailsList  = examSettingDetailsService.findByHql(detailsHQL,learnPlanCourseId);
					if (null!=learnPlanCourseExamDetailsList && !learnPlanCourseExamDetailsList.isEmpty()) {
						learnPlanCourseExamDetails 							 = learnPlanCourseExamDetailsList.get(0);
					}
					//学习计划中已预约的计划外课程考试时间设置对象
					ExamSettingDetails outPlanCourseExamDetails              =  null;
					List<ExamSettingDetails> outPlanCourseExamDetailsList    = examSettingDetailsService.findByHql(detailsHQL,outPlanCourseId);
					if (null!=outPlanCourseExamDetailsList && !outPlanCourseExamDetailsList.isEmpty()) {
						outPlanCourseExamDetails 							 = outPlanCourseExamDetailsList.get(0);
					}
					
					//学习计划中的课程的考试设置名称 如: 第一天上午   第一天下午   第二天上午.....
					String learnPlanCourseExamSettingName = null==learnPlanCourseExamDetails?"":learnPlanCourseExamDetails.getExamSetting().getSettingName();
					//学习计划中已预约的计划外课程考试设置名称
					String outPlanCourseExamSettingName   = null==outPlanCourseExamDetails?"":outPlanCourseExamDetails.getExamSetting().getSettingName();
					
					//当 预约课程的考试设置名称 与 学习计划中的课程的考试设置名称 相同时，表示两门课程的考试时间在同一个时段
					if ((orderCourseExamSettingName.equals(learnPlanCourseExamSettingName)||
						 orderCourseExamSettingName.equals(outPlanCourseExamSettingName))  & 
						 settingYearInfoId.equals(planYearInfoId) & settingTerm.equals(planTerm)) {
						
						isExamConFlictPass = false;
						isNeedChooseCourse = true;
						String msg ="<strong>考试时间冲突:</strong></br>";
					    msg+= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='blue'>"+course.getCourseName()+"</font>---<font color='red'>"+orderCourseExamSettingName+"</font></br>";
				        msg+= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='blue'>"+plan.getTeachingPlanCourse().getCourse().getCourseName()+"</font>---<font color='red'>"+learnPlanCourseExamSettingName+"</font></br>";
				        paramMap = setResultMapMsg(paramMap,msg);
						break;
					}
				}
			}else {
				isExamConFlictPass=false;
				String msg		  ="未安排考试时间,暂时不允许预约!";
				paramMap 	      = setResultMapMsg(paramMap,msg);
			}
			paramMap.put("isNeedChooseCourse", isNeedChooseCourse);
			paramMap.put("isExamConFlictPass",isExamConFlictPass);

		}else {
			paramMap.put("isExamConFlictPass",true);
		}
		return paramMap;
	}
	/**
	 * 预约学习-预约计划外课程:通过预约条件检查时执行的操作
	 * @param param
	 * @return
	 */
	public Map<String,Object> allowOrderOutPlanCourseOperating(Map<String,Object> param){
	    //1.更新学习计划
		param = updateStudentLearnPlanForOrderOutPlanCourse(param);
		boolean updateStudentLearnPlan = null==param.get("updateStudentLearnPlan")?false:(Boolean)param.get("updateStudentLearnPlan");
		
		//2.增加教材预约
		if (updateStudentLearnPlan) {
			param = addCourseBoookOrdersForOrderOutPlanCourse(param);
		}else {
			throw new ServiceException("预约计划外课程-更新学习计划操作中断,数据不合法或缺少数据！");
		}
		
		boolean addCourseBoookOrders = null==param.get("addCourseBoookOrders")?false:(Boolean)param.get("addCourseBoookOrders");
		if (!addCourseBoookOrders) {
			throw new ServiceException("预约计划外课程-增加教材预约操作中断,数据不合法或缺少数据！");
		}	
		
		//3.更新预约统计及明细表
		if (updateStudentLearnPlan) {
			param = updateCourseOrderAndCourseOrderStatForOrderOutPlanCourse(param);
		}else {
			throw new ServiceException("预约计划外课程-更新学习计划操作中断,数据不合法或缺少数据！");
		}
		boolean updateCourseOrderAndCourseOrderStatSuccess = null==param.get("updateCourseOrderAndCourseOrderStatSuccess")?false:(Boolean)param.get("updateCourseOrderAndCourseOrderStatSuccess");
		
		//4.返回处理结果
		if (updateCourseOrderAndCourseOrderStatSuccess&&updateStudentLearnPlan&&addCourseBoookOrders) {
			param.put("orderCourseOperateStatus", true);
		}else {
			param.put("orderCourseOperateStatus", false);
		}

		return param;
	}
	/**
	 * 预约学习  ---------预约计划外课程:更新学习计划
	 */
	public Map<String,Object> updateStudentLearnPlanForOrderOutPlanCourse(Map<String,Object> paramMap) throws ServiceException {
	
		//获得学生的学籍信息
		StudentInfo studentInfo 		    = (StudentInfo) paramMap.get("studentInfo");
		//要进行预约操作的课程
		Course orderOutPlanCourse           = (Course) paramMap.get("course");	
		
		//年度预约权限设置对象 
		OrderCourseSetting setting		    = (OrderCourseSetting) paramMap.get("orderCourseSetting");
		
		//学生的学习计划
		List<StudentLearnPlan> oldLearnPlan = (List<StudentLearnPlan>) paramMap.get("learnPlan");
		
		if (null!=oldLearnPlan && !oldLearnPlan.isEmpty()) {
			
			boolean isLearnPlanhas 			= false;
			
			for (StudentLearnPlan plan : oldLearnPlan) {
				//学习计划中已有需要预约操作的课程
				if (null!=plan.getPlanoutCourse()&&orderOutPlanCourse.getResourceid().equals(plan.getPlanoutCourse().getResourceid())) {
					isLearnPlanhas = true;
					String msg 	   = "你已预约了当前课程！";
					paramMap 	   = setResultMapMsg(paramMap, msg);
					paramMap.put("updateStudentLearnPlan", false);
					break;
				}
			}
			if(isLearnPlanhas==false){//学习计划中没有需要预约操作的课程
				
				StudentLearnPlan newLearnPlan = new StudentLearnPlan();
				newLearnPlan.setStudentInfo(studentInfo);
				newLearnPlan.setStatus(1);
				newLearnPlan.setPlanoutCourse(orderOutPlanCourse);
				newLearnPlan.setYearInfo(setting.getYearInfo());
				newLearnPlan.setTerm(setting.getTerm());
				
				studentLearnPlanService.saveOrUpdate(newLearnPlan);
				paramMap.put("updateStudentLearnPlan", true);
				
			}else if(isLearnPlanhas==true){
				return paramMap;
			}
			
		}else {
			
			StudentLearnPlan  newStudentLearnPlan = new StudentLearnPlan();
			newStudentLearnPlan.setStudentInfo(studentInfo);
			newStudentLearnPlan.setStatus(1);
			newStudentLearnPlan.setPlanoutCourse(orderOutPlanCourse);
			newStudentLearnPlan.setYearInfo(setting.getYearInfo());
			newStudentLearnPlan.setTerm(setting.getTerm());
			
			studentLearnPlanService.saveOrUpdate(newStudentLearnPlan);
			paramMap.put("updateStudentLearnPlan", true);
		}

		return paramMap;
		
	}
	/**
	 * 预约学习 2 ---------预约计划外课程:增加教材预约
	 */
	public Map<String, Object> addCourseBoookOrdersForOrderOutPlanCourse(Map<String, Object> paramMap) throws ServiceException {
		/*
			//获得学生的学籍信息
			StudentInfo studentInfo 		    = (StudentInfo) paramMap.get("studentInfo");
			//要进行预约操作的课程
			Course orderOutPlanCourse           = (Course) paramMap.get("course");	
			//年度预约权限设置对象 
			OrderCourseSetting setting		    = (OrderCourseSetting) paramMap.get("orderCourseSetting");
			
			//要进行预约操作的教学计划课程教材
			List<CourseBook> courseBook			= courseBookService.findByHql("from "+CourseBook.class.getSimpleName()+" c where c.isDeleted=0 and c.status=1 and c.course.resourceid=?", orderOutPlanCourse.getResourceid());
			
			for (CourseBook book: courseBook) {
				
				//预约教材明细
				CourseBookOrders courseBookOrders   = new CourseBookOrders();
				//年度预约教材统计对象
				CourseBookOrderStat oldstat 		= courseBookOrderStatService.getCourseBookOrdersByYearInfoAndCourse(setting.getYearInfo().getResourceid(), setting.getTerm(), null==book?"":book.getResourceid());
				if (oldstat==null&&book!=null) {//统计对象为空
					
					oldstat = new CourseBookOrderStat();

					oldstat.setCourseBook(book);
					oldstat.setYearInfo(setting.getYearInfo());
					oldstat.setTerm(setting.getTerm());
					oldstat.setOrderNum(1L);
					
					courseBookOrders.setCount(1L);
					courseBookOrders.setFee(book.getPrice());
					courseBookOrders.setStudentInfo(studentInfo);
					courseBookOrders.setCourseBookOrderStat(oldstat);
				
					oldstat.getOrders().add(courseBookOrders);
					
					courseBookOrderStatService.saveOrUpdate(oldstat);	
					
					 
				}else if(oldstat!=null&&book!=null){//统计对象不为空
					
					Long oldStatOrderNum = oldstat.getOrderNum();
					
					courseBookOrders.setCount(1L);
					courseBookOrders.setFee(book.getPrice());
					courseBookOrders.setStudentInfo(studentInfo);
					courseBookOrders.setCourseBookOrderStat(oldstat);
					
					oldstat.setOrderNum(null==oldStatOrderNum?0L:oldStatOrderNum+1);
					oldstat.getOrders().add(courseBookOrders);
					
					courseBookOrderStatService.saveOrUpdate(oldstat);	
					
					
				}else {//没有教材
					String msg ="没有对应的教材信息，请与教务管理人员联系！";
					paramMap   = setResultMapMsg(paramMap,msg);
					
				}
			}*/
			paramMap.put("addCourseBoookOrders", true);
			
		return paramMap;
	}
	

	/**
	 * 预约学习 3 ---------预约计划外课程:更新预约统计及明细表
	 */
	public Map<String, Object> updateCourseOrderAndCourseOrderStatForOrderOutPlanCourse(Map<String, Object> paramMap) throws ServiceException {

			
		//获得学生的学籍信息
		StudentInfo studentInfo        = (StudentInfo) paramMap.get("studentInfo");
		//要进行预约操作的课程
		Course orderOutPlanCourse      = (Course) paramMap.get("course");	
		//年度预约权限设置对象 
		OrderCourseSetting setting     = (OrderCourseSetting) paramMap.get("orderCourseSetting");
		
		if (null!=setting) {
		
			//查出预约统计对象
			CourseOrderStat oldCourseOrderStat = 
			courseorderstatservice.findCourseOrderStatByYearInfoAndCourse( setting.getYearInfo().getResourceid(),orderOutPlanCourse.getResourceid(),setting.getTerm());
			if (null!=oldCourseOrderStat) {
				//查出预约统计明细对象
				CourseOrder oldCourseOrder =
				this.getCourseOrderByStuIdAndCourseId(new String []{studentInfo.getResourceid(),orderOutPlanCourse.getResourceid(),oldCourseOrderStat.getResourceid()});
			
				if (null==oldCourseOrder) {
					oldCourseOrder= new CourseOrder();
					oldCourseOrder.setStudentInfo(studentInfo);
					oldCourseOrder.setStatus(1L);
					oldCourseOrder.setShowOrder(0);
					oldCourseOrder.setCourseOrderStat(oldCourseOrderStat);
					
					oldCourseOrderStat.getCourseOrders().add(oldCourseOrder);
					
				}else {
					oldCourseOrder.setStatus(1L);
					this.saveOrUpdate(oldCourseOrder);
				}
				
				oldCourseOrderStat.setOrderNum(null==oldCourseOrderStat.getOrderNum()?1:oldCourseOrderStat.getOrderNum()+1);
				courseorderstatservice.saveOrUpdate(oldCourseOrderStat);
				
			}else {
				CourseOrderStat newcourseOrderStat = new CourseOrderStat();
				
				newcourseOrderStat.setCourse(orderOutPlanCourse);
				newcourseOrderStat.setYearInfo(setting.getYearInfo());
				newcourseOrderStat.setTerm(setting.getTerm());
				newcourseOrderStat.setOrderNum(1);
				
				CourseOrder newCourseOrder = new CourseOrder();
				newCourseOrder.setStudentInfo(studentInfo);
				newCourseOrder.setStatus(1L);
				newCourseOrder.setShowOrder(0);
				newCourseOrder.setCourseOrderStat(newcourseOrderStat);
				
				newcourseOrderStat.getCourseOrders().add(newCourseOrder);
				
				courseorderstatservice.save(newcourseOrderStat);
			}
			
			paramMap.put("updateCourseOrderAndCourseOrderStatSuccess", true);
			
		}else {
			String msg ="当前年度预约未开放!";
			paramMap = setResultMapMsg(paramMap,msg);
			paramMap.put("updateCourseOrderAndCourseOrderStatSuccess", false);
		}	
			

		return paramMap;
	}
	//-----------------------------------------------------------预约学习计划外课程-----------------------------------------------------------
	
	
/**-------------------------------------------------------------------------计划外课程预约考试---------------------------------------------------------------------------------------------------------------**/
	
	
	/**
	 * 预约考试--计划外课程:检查是否允许预约考试
	 */
	public Map<String, Object> isAllowOrderExamForOutPlanCourse(Map<String, Object> paramMap) throws ServiceException {
		try {
			Date curDate 			 = new Date();
			//获取当前年度的考试批次预约对象
			List<ExamSub> examSubList= examSubService.findOpenedExamSub("exam");
			StudentInfo studentInfo  = (StudentInfo)paramMap.get("studentInfo");
			
			boolean isAllowStatus    =  false;
			
			//学籍状态
			String studentstatus     = null==studentInfo.getStudentStatus()?"":studentInfo.getStudentStatus();
			
			if ("11".equals(studentstatus)) {
				isAllowStatus        = true;
			}
			if (isAllowStatus==false) {
				String msg           = "系统只允许在学状态的学员预约，您当前的学籍状态为："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", studentstatus);
				paramMap             = setResultMapMsg(paramMap,msg);
				 throw new Exception("不合法的预约考试,当前学籍为不允许预约的学籍!");
			}
			//没有当前年度的考试批次预约记录
			if (null==examSubList || examSubList.isEmpty()) {
				
				paramMap.put("isAllowOrderExam",false);
				String msg          = "未设置年度考试预约批次!";
				paramMap=setResultMapMsg(paramMap,msg);
				
				
			//有当前年度的考试预约批次记录	
			}else {
				
				ExamSub examSub     = examSubList.get(0);
				paramMap.put("examSub", examSub);
				//批次是否开放
				boolean isOpen      = null==examSub.getExamsubStatus()?false: "2".equals(examSub.getExamsubStatus());
				
				//预约开始时间
				Date startTime      = examSub.getStartTime();
				//预约结束时间
				Date endTime        = examSub.getEndTime();

				//添加预约信息截止时间
				Date examsubEndTime = examSub.getExamsubEndTime();
				//补预约权限记录
				List<ReOrderSetting> reOrderSet  = null;
				if (isOpen) {//当前年度考试预约批次开放
					
					//学生自主预约,要求在年度预约时限范围内完成操作
					if("student".equals(paramMap.get("operater").toString())){
						
						if(null==startTime || null==endTime){
							
							paramMap.put("isAllowOrderExam",false);
							String msg = "未设置预约的开始时间或结束时间";
							paramMap=setResultMapMsg(paramMap,msg);
							
					    }else if ((curDate.getTime() >= startTime.getTime()) && (curDate.getTime() <= endTime.getTime()) ) {//当前时间在考试预约批次范围内
					    	
					    	paramMap.put("isAllowOrderExam",true);
						}else {
							
							paramMap.put("branchSchool", studentInfo.getBranchSchool().getResourceid());
							paramMap.put("reOrderType", "2");
							paramMap.put("examSubId", examSub.getResourceid());
							
							reOrderSet       = reOrderSettingService.findReOrderSettingByCondition(paramMap);
							paramMap.remove("branchSchool");
							paramMap.remove("reOrderType");
							paramMap.remove("examSubId");
							//补预约时间
							if (null!=reOrderSet&&reOrderSet.size()>0&&
									curDate.getTime()>=reOrderSet.get(0).getStartTime().getTime()&&
									curDate.getTime()<=reOrderSet.get(0).getEndTime().getTime()) {
								
								paramMap.put("isAllowOrderExam",true);
							}else {
								paramMap.put("isAllowOrderExam",false);
								String msg = "预约时间为:</br>"+DateUtils.getFormatDate(startTime, "yyyy-MM-dd HH:mm:ss")+"-"+DateUtils.getFormatDate(endTime,"yyyy-MM-dd HH:mm:ss");
								paramMap=setResultMapMsg(paramMap,msg);
							}
							
							
						}
					
					// 错过预约时限，老师帮学生预约 前提是不能超过 添加预约信息截止时间
					}else if ("teacher".equals(paramMap.get("operater").toString())) {
						
						if (null==examsubEndTime) {
							paramMap.put("isAllowOrderExam",false);
							String msg = "没有设置添加预约信息截止时间!";
							paramMap=setResultMapMsg(paramMap,msg);
						}else if (curDate.getTime()<=examsubEndTime.getTime()) {
							paramMap.put("isAllowOrderExam",true);
						}else {
							paramMap.put("isAllowOrderExam",false);
							String msg = "已超过添加预约信息截止时间,不允许预约!";
							paramMap=setResultMapMsg(paramMap,msg);
						}
					}
					
				}else {//当前年度考试预约批次不开放
					
					paramMap.put("isAllowOrderExam",false);
					String msg = "当前年度考试预约批次未开放!";
					paramMap=setResultMapMsg(paramMap,msg);
				}
			}
		} catch (Exception e) {
			paramMap.put("isAllowOrderExam",false);
			String msg = "检查是否允许预约考试操作出错:"+e.getMessage();
			paramMap = setResultMapMsg(paramMap, msg);
			
			logger.error("预约考试-检查是否允许预约考试异常：{}",e.fillInStackTrace());
		}
		return paramMap;
	}
	
	
	/**
	 * 预约考试-计划外课程:条件满足时的增加或更新操作
	 */
	public Map<String, Object> allowOrderExamOperate(Map<String, Object> paramMap) throws ServiceException {
			
			
		//1.添加一个学生预约考试记录
		
		paramMap = orderExamAddExamResults(paramMap);
		boolean isAddExamResultSuccess =null==paramMap.get("isAddExamResultSuccess")?false:(Boolean)paramMap.get("isAddExamResultSuccess");
		
		//2.修改学习计划中的课程状态
		if (isAddExamResultSuccess){
			paramMap =orderExamUpdateLearnPlan(paramMap);
		}else {
			throw new ServiceException("计划外课程预约考试-添加一个学生预约考试记录操作中断,数据不合法或缺少数据！");
		}
		
		boolean isUpdateLearnPlanSuccess = null==paramMap.get("isUpdateLearnPlanSuccess")?false:(Boolean)paramMap.get("isUpdateLearnPlanSuccess");
		
		//3.修改学生预约课程状态
		if (isUpdateLearnPlanSuccess) {
			paramMap = orderExamUpdateCourseOrder(paramMap);
		}else {
			throw new ServiceException("计划外课程预约考试-修改学习计划中的课程状态操作中断,数据不合法或缺少数据！");
		}
		
		boolean isUpdateCourseOrderSuccess = null==paramMap.get("isUpdateCourseOrderSuccess")?false:(Boolean)paramMap.get("isUpdateCourseOrderSuccess");
		if (!isUpdateCourseOrderSuccess) {
			throw new ServiceException("计划外课程预约考试-修改学生预约课程状态操作中断,数据不合法或缺少数据！");
		}

		//4.返回理结果
		if (isUpdateLearnPlanSuccess&&isUpdateCourseOrderSuccess&&isAddExamResultSuccess) {
			paramMap.put("orderExamOperateStatus", true);
		}else {
			paramMap.put("orderExamOperateStatus", false);
			throw new ServiceException("计划外课程预约考试-操作中断,数据不合法或缺少数据！");
		}

		return paramMap;
	}
	/**
	 * 预约考试1--------计划外课程：添加一个学生预约考试
	 */
	public Map<String, Object> orderExamAddExamResults(Map<String, Object> paramMap) throws ServiceException {
	
		if (null!=paramMap&&null!=paramMap.get("isAllowOrderExam")&&(Boolean)paramMap.get("isAllowOrderExam")) {
			//获得学生的学籍信息
			StudentInfo studentInfo        = (StudentInfo) paramMap.get("studentInfo");
			//要进行预约操作的课程
			Course orderOutPlanCourse 	   = (Course) paramMap.get("course");
			
			//当前年份的预约考试批次
			ExamSub examSub 			   = (ExamSub) paramMap.get("examSub");
			
			if (null!=examSub&&null!=examSub.getExamInfo()&&!examSub.getExamInfo().isEmpty()) {
				
				Set<ExamInfo> set          = examSub.getExamInfo();	
				//要进行预约操作的考试信息
				ExamInfo examInfo          = null;
				for(ExamInfo examSubsExamInfo :set ){
					if (examSubsExamInfo.getCourse().getResourceid().equals(orderOutPlanCourse.getResourceid())) {
						examInfo           = examSubsExamInfo;
						break;
					}
				}	
				if (null!=examInfo) {
					
					ExamResults examResults = new ExamResults();
					examResults.setCourse(orderOutPlanCourse);
					examResults.setMajorCourseId(orderOutPlanCourse.getResourceid());
					examResults.setStudentInfo(studentInfo);
					examResults.setExamInfo(examInfo);
					examResults.setCheckStatus("-1");
					examResults.setExamAbnormity("0");
					examResults.setExamStartTime(examInfo.getExamStartTime());
					examResults.setExamEndTime(examInfo.getExamEndTime());
					examResults.setExamsubId(examInfo.getExamSub().getResourceid());
					examResults.setCourseType("22");
					examResults.setPlanCourseTeachType("networkstudy");
					
					examResults.setCourseScoreType(examInfo.getCourseScoreType());
					examResults.setCreditHour(orderOutPlanCourse.getPlanoutCreditHour()!=null?orderOutPlanCourse.getPlanoutCreditHour():null);
					examResults.setStydyHour(orderOutPlanCourse.getPlanoutStudyHour()!=null?orderOutPlanCourse.getPlanoutStudyHour().intValue():null);
					
					if (null!=orderOutPlanCourse.getExamType()) {
						examResults.setExamType(Integer.valueOf(orderOutPlanCourse.getExamType().toString()));
					}
					
					examResultsService.saveOrUpdate(examResults);
					
					paramMap.put("isAddExamResultSuccess",true);
					paramMap.put("examResult", examResults);
				}else {
					paramMap.put("isAddExamResultSuccess",false);
					String msg =orderOutPlanCourse.getCourseName()+ "未设置考试时间,暂时不允许预约！";
					paramMap = setResultMapMsg(paramMap, msg);
				}
			}else {
				paramMap.put("isAddExamResultSuccess",false);
				String msg = "当前年度预约批次未开放！";
				paramMap = setResultMapMsg(paramMap, msg);
			}
			
		}else{
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			paramMap.put("isAddExamResultSuccess",false);
			String msg = "数据不合法,或考试预约批次未开放!";
			paramMap= setResultMapMsg(paramMap, msg);
		}

		return paramMap;
	}
	/**
	 * 预约考试2--------计划外课程：修改学习计划
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> orderExamUpdateLearnPlan(Map<String, Object> paramMap)throws ServiceException{
		
		//学生的学习计划
		List<StudentLearnPlan> learnPlan  = (List<StudentLearnPlan>) paramMap.get("learnPlan");
		//要进行预约操作的课程
		Course orderPlanOutCourse         = (Course) paramMap.get("course");
		//关联的成绩对象
		ExamResults rs 				      = (ExamResults) paramMap.get("examResult");
		ExamSub examSub 				  = (ExamSub) paramMap.get("examSub");
		boolean  isUpdateLearnPlanSuccess = false;
		//修改学习计划中的课程学习状态
		if (null!=learnPlan&&!learnPlan.isEmpty()&&null!=orderPlanOutCourse){
			
			Set<ExamInfo> set 		      = examSub.getExamInfo();
			ExamInfo examInfo 			  = null;//要进行预约操作的考试信息
			if (set!=null) {
				for (ExamInfo examSubsExamInfo:set) {
					if (examSubsExamInfo.getCourse().getResourceid().equals(orderPlanOutCourse.getResourceid())) {
						examInfo 		  = examSubsExamInfo;
						break;
					}
			   }
			}
			if (null!=examInfo) {
				for (StudentLearnPlan oldLearnPlan:learnPlan) {
					if (null!=oldLearnPlan.getPlanoutCourse()&&orderPlanOutCourse.getResourceid().equals(oldLearnPlan.getPlanoutCourse().getResourceid())) {
						
						oldLearnPlan.setStatus(2);
						oldLearnPlan.setExamInfo(examInfo);
						oldLearnPlan.setExamResults(rs);
						oldLearnPlan.setOrderExamYear(examSub.getYearInfo());
						oldLearnPlan.setOrderExamTerm(examSub.getTerm());
						
						studentLearnPlanService.saveOrUpdate(oldLearnPlan);
						isUpdateLearnPlanSuccess = true;
						break;
					}
				}
			}else {
				isUpdateLearnPlanSuccess = false;
				String msg = "考试计划中没有当前课程的信息，请联系教务管理人员";
				paramMap = setResultMapMsg(paramMap, msg);
			}
			
			paramMap.put("isUpdateLearnPlanSuccess", isUpdateLearnPlanSuccess);
		}else {
			paramMap.put("isUpdateLearnPlanSuccess", false);
			String  msg = "学习计划为空不能预约考试,请联系管理员！";
			paramMap    = setResultMapMsg(paramMap, msg);
		}


		return paramMap;
	}

	/**
	 * 预约考试3--------计划外课程：修改学生预约课程状态
	 */
	public Map<String, Object> orderExamUpdateCourseOrder(Map<String, Object> paramMap) throws ServiceException {
				
		//获得学生的学籍信息
		StudentInfo studentInfo        = (StudentInfo) paramMap.get("studentInfo");
		//要进行预约操作的课程
		Course orderOutPlanCourse      = (Course) paramMap.get("course");
		//修改课程预约中的课程学习状态
		String courseOrderHQL          = getCourseOrderHQL();
		ExamSub examSub 			   = (ExamSub) paramMap.get("examSub");
		Date curDate                   = new Date();
		/*List<CourseOrder> courOrders   = 
		this.findByHql(courseOrderHQL,new String []{studentInfo.getResourceid(),orderOutPlanCourse.getResourceid()});
		
		if (null!=courOrders&&!courOrders.isEmpty()) {
			CourseOrder courseOrder    = courOrders.get(0);
			courseOrder.setStatus(2L);
			this.saveOrUpdate(courseOrder);
			paramMap.put("isUpdateCourseOrderSuccess",true);
		}else {
			String msg 				   = "缺少课程预约数据！";
			paramMap  				   = setResultMapMsg(paramMap, msg);
			paramMap.put("isUpdateCourseOrderSuccess",false);
		}	*/
		List<CourseOrder> courOrders   = 
			this.findByHql(courseOrderHQL,new String []{studentInfo.getResourceid(),orderOutPlanCourse.getResourceid(),examSub.getYearInfo().getResourceid(),examSub.getTerm()});
		
			if (null!=courOrders&&!courOrders.isEmpty()) {
				CourseOrder courseOrder    = courOrders.get(0);
				courseOrder.setStatus(2L);
				courseOrder.setOrderExamTime(curDate);
				courseOrder.setOrderExamTerm(examSub.getTerm());
				courseOrder.setOrderExamYear(examSub.getYearInfo());
				
				this.saveOrUpdate(courseOrder);
				paramMap.put("isUpdateCourseOrderSuccess",true);
			}else {
				//学生查出预约统计对象
				CourseOrderStat courseOrderStat= 
				courseorderstatservice.findCourseOrderStatByYearInfoAndCourse(examSub.getYearInfo().getResourceid(),orderOutPlanCourse.getResourceid(),examSub.getTerm());

				if (null==courseOrderStat){
					courseOrderStat        = new CourseOrderStat();
					courseOrderStat.setCourse(orderOutPlanCourse);
					courseOrderStat.setYearInfo(examSub.getYearInfo());
					courseOrderStat.setTerm(examSub.getTerm());
					courseOrderStat.setOrderNum(0);
				}
				
				CourseOrder courseOrder    =  new CourseOrder();
				courseOrder.setOrderExamTerm(examSub.getTerm());
				courseOrder.setOrderExamYear(examSub.getYearInfo());
				courseOrder.setOrderExamTime(curDate);
				courseOrder.setStatus(2L);
				courseOrder.setStudentInfo(studentInfo);
				courseOrder.setCourseOrderStat(courseOrderStat);
				
				courseOrderStat.getCourseOrders().add(courseOrder);
				courseOrderStat.setOrderNum(courseOrderStat.getOrderNum()+1);
				
				courseorderstatservice.saveOrUpdate(courseOrderStat);
				paramMap.put("isUpdateCourseOrderSuccess",true);
			}
		return paramMap;
	}
	private String getCourseOrderHQL(){
		StringBuffer hql = new StringBuffer();
		hql.append("from CourseOrder courseorder where  courseorder.isDeleted=0");
		hql.append(" and courseorder.studentInfo.resourceid=?");
		hql.append(" and courseorder.courseOrderStat.course.resourceid=?");
		hql.append(" and courseorder.courseOrderStat.yearInfo.resourceid=?");
		hql.append(" and courseorder.courseOrderStat.term=?");
		hql.append(" order by courseorder.courseOrderStat.yearInfo.firstYear desc");
		
		return hql.toString();
	}
	//设置返回的resultMap的消息数组
	public Map<String,Object> setResultMapMsg(Map <String,Object> resultMap,String msg){
	
	   if (null==resultMap) {
		   resultMap = new HashMap<String, Object>();
	   }
	   
	    List<String> msgList =  (List<String>) resultMap.get("msg"); 

        if (null==msgList) {
        	msgList = new ArrayList<String>() ;
        	msgList.add(msg);
			resultMap.put("msg",msgList);
		}else {
			msgList.add(msg);
			resultMap.put("msg",msgList);
		}
        
		return resultMap;
	}

	
	/**
	 * 根据条件查询课程预约-返回PAGE
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findCourseOrderByCondition(Map<String, Object> condition,Page page) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+CourseOrder.class.getSimpleName()+" o where o.isDeleted=0 ");
		
		if (condition.containsKey("courseId")) {
			hql.append(" and o.courseOrderStat.course.resourceid=:courseId ");
		}
		//预约统计ID
		if (condition.containsKey("statid")) {
			hql.append(" and o.courseOrderStat.resourceid=:statid ");
		}
		//预约状态
		if (condition.containsKey("status")) {
			condition.put("status",Long.valueOf(condition.get("status").toString()));
			hql.append(" and o.status=:status ");
		}
		//专业
		if (condition.containsKey("major")) {
			hql.append(" and o.studentInfo.major.resourceid=:major ");
		}
		//年级
		if (condition.containsKey("grade")) {
			hql.append(" and o.studentInfo.grade.resourceid=:grade ");
		}
		//学号
		if (condition.containsKey("studyNo")) {
			hql.append(" and o.studentInfo.studyNo=:studyNo ");
		}
		//姓名
		if (condition.containsKey("studentName")) {
			hql.append(" and o.studentInfo.studentName like'%"+condition.get("studentName")+"%'");
		}
		//层次
		if (condition.containsKey("classic")) {
			hql.append(" and o.studentInfo.classic.resourceid=:classic ");
		}
		//学习中心
		if (condition.containsKey("branchSchool")) {
			hql.append(" and o.studentInfo.branchSchool.resourceid=:branchSchool ");
		}
		//预约学习时间
		if (condition.containsKey("orderCourseStartTime")) {
			hql.append(" and o.orderCourseTime >= to_date('"+condition.get("orderCourseStartTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约学习时间
		if (condition.containsKey("orderCourseEndTime")) {
			hql.append(" and o.orderCourseTime <= to_date('"+condition.get("orderCourseEndTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约考试时间
		if (condition.containsKey("orderExamStartTime")) {
			hql.append(" and o.orderExamTime >= to_date('"+condition.get("orderExamStartTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约考试时间
		if (condition.containsKey("orderExamEndTime")) {
			hql.append(" and o.orderExamTime <= to_date('"+condition.get("orderExamEndTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约学习年度
		if (condition.containsKey("orderCourseYearInfo")) {
			hql.append(" and o.orderCourseYear.resourceid=:orderCourseYearInfo");
		}
		//预约考试年度
		if (condition.containsKey("orderExamYearInfo")) {
			hql.append(" and o.orderExamYear.resourceid=:orderExamYearInfo");
		}
		//预约学习学期
		if (condition.containsKey("orderCourseTerm")) {
			hql.append(" and o.orderCourseTerm=:orderCourseTerm");
		}
		//预约考试学期
		if (condition.containsKey("orderExamTerm")) {
			hql.append(" and o.orderExamTerm=:orderExamTerm");
		}
		if (null!=page.getOrderBy()) {
			hql.append(" order by "+page.getOrderBy());
		}
		if (null!=page.getOrder()) {
			hql.append(page.getOrder());
		}
		
		return exGeneralHibernateDao.findByHql(page, hql.toString(),condition);
	}

	/**
	 * 根据条件查询课程预约-返回List
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<CourseOrder> findCourseOrderByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer hql 		 = new StringBuffer();
		hql.append(" from "+CourseOrder.class.getSimpleName()+" o where o.isDeleted=0 ");
		
		if (condition.containsKey("courseId")) {
			hql.append(" and o.courseOrderStat.course.resourceid=:courseId ");
			param.put("courseId", condition.get("courseId"));
		}
		//预约统计ID
		if (condition.containsKey("statid")) {
			hql.append(" and o.courseOrderStat.resourceid=:statid ");
			param.put("statid", condition.get("statid"));
		}
		//预约状态
		if (condition.containsKey("status")) {
			hql.append(" and o.status=:status ");
			param.put("status",Long.valueOf(condition.get("status").toString()));
		}
		//专业
		if (condition.containsKey("major")) {
			hql.append(" and o.studentInfo.major.resourceid=:major ");
			param.put("major", condition.get("major"));
		}
		//年级
		if (condition.containsKey("grade")) {
			hql.append(" and o.studentInfo.grade.resourceid=:grade ");
			param.put("grade", condition.get("grade"));
		}
		//学号
		if (condition.containsKey("studyNo")) {
			hql.append(" and o.studentInfo.studyNo=:studyNo ");
			param.put("studyNo", condition.get("studyNo"));
		}
		//姓名
		if (condition.containsKey("studentName")) {
			hql.append(" and o.studentInfo.studentName like'%"+condition.get("studentName")+"%'");
		}
		//层次
		if (condition.containsKey("classic")) {
			hql.append(" and o.studentInfo.classic.resourceid=:classic ");
			param.put("classic", condition.get("classic"));
		}
		//学习中心
		if (condition.containsKey("branchSchool")) {
			hql.append(" and o.studentInfo.branchSchool.resourceid=:branchSchool ");
			param.put("branchSchool", condition.get("branchSchool"));
		}
		//预约学习时间
		if (condition.containsKey("orderCourseStartTime")) {
			hql.append(" and o.orderCourseTime >= to_date('"+condition.get("orderCourseStartTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约学习时间
		if (condition.containsKey("orderCourseEndTime")) {
			hql.append(" and o.orderCourseTime <= to_date('"+condition.get("orderCourseEndTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约考试时间
		if (condition.containsKey("orderExamStartTime")) {
			hql.append(" and o.orderExamTime >= to_date('"+condition.get("orderExamStartTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约考试时间
		if (condition.containsKey("orderExamEndTime")) {
			hql.append(" and o.orderExamTime <= to_date('"+condition.get("orderExamEndTime")+"','yyyy-MM-dd hh24:mi:ss')");
		}
		//预约学习年度
		if (condition.containsKey("orderCourseYearInfo")) {
			hql.append(" and o.orderCourseYear.resourceid=:orderCourseYearInfo");
			param.put("orderCourseYearInfo", condition.get("orderCourseYearInfo"));
		}
		//预约考试年度
		if (condition.containsKey("orderExamYearInfo")) {
			hql.append(" and o.orderExamYear.resourceid=:orderExamYearInfo");
			param.put("orderExamYearInfo", condition.get("orderExamYearInfo"));
		}
		//预约学习学期
		if (condition.containsKey("orderCourseTerm")) {
			hql.append(" and o.orderCourseTerm=:orderCourseTerm");
			param.put("orderCourseTerm", condition.get("orderCourseTerm"));
		}
		//预约考试学期
		if (condition.containsKey("orderExamTerm")) {
			hql.append(" and o.orderExamTerm=:orderExamTerm");
			param.put("orderExamTerm", condition.get("orderExamTerm"));
		}
		hql.append(" order by o.studentInfo.studyNo");
		return (List<CourseOrder>) exGeneralHibernateDao.findByHql( hql.toString(),param);
	}

	

	






	
}
