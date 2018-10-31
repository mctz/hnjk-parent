package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.INoExamApplyService;
import com.hnjk.edu.teaching.service.IStateExamResultsService;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.edu.teaching.vo.NoExamApplyVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 学生免考申请表. <code>NoExamApplyServiceImpl</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-12-6 上午11:49:14
 * @see
 * @version 1.0
 */
@Transactional
@Service("noexamapplyservice")
public class NoExamApplyServiceImpl extends BaseServiceImpl<NoExamApply> implements INoExamApplyService {
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	
	/**
	 * 根据条件查询NoExamApply，返回Page
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findNoExamApplyByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = findNoExamApplyByConditionHql(condition,values,objPage);
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	/**
	 * 根据条件查询NoExamApply，返回List
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<NoExamApply> findNoExamApplyByCondition(Map<String, Object> condition)throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = findNoExamApplyByConditionHql(condition,values,null);
		return (List<NoExamApply>) exGeneralHibernateDao.findByHql(hql, values);
	}
	/**
	 * 根据条件生成查询HQL
	 * @param condition
	 * @param values
	 * @param objPage
	 * @return
	 */
	public String findNoExamApplyByConditionHql(Map<String, Object> condition,Map<String,Object> values,Page objPage){
		
		StringBuffer hql = new StringBuffer(" from "+NoExamApply.class.getSimpleName()+" where 1=1  and isDeleted = :isDeleted and studentInfo.isDeleted = 0 and course.isDeleted = 0 ");
		values.put("isDeleted", 0);
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
		if(condition.containsKey("stuNo")){
			hql.append(" and studentInfo.studyNo=:stuNo ");
			values.put("stuNo", condition.get("stuNo"));
		}
		if(condition.containsKey("studentId")){
			hql.append(" and studentInfo.resourceid = :studentId ");
			values.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("batchId")){
			hql.append(" and examSub.resourceid = :batchId ");
			values.put("batchId", condition.get("batchId"));
		}
		if(condition.containsKey("classic")){
			hql.append(" and studentInfo.classic.resourceid = :classic ");
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("checkStatus")){
			hql.append(" and checkStatus = :checkStatus ");
			values.put("checkStatus", condition.get("checkStatus"));
		}
		
		if(condition.containsKey("appStartTime")){
			hql.append(" and subjectTime >= to_date('"+condition.get("appStartTime")+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(condition.containsKey("appEndTime")){
			hql.append(" and subjectTime <=  to_date('"+condition.get("appEndTime")+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(condition.containsKey("auditStartTime")){
			hql.append(" and checkTime >= to_date('"+condition.get("auditStartTime")+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(condition.containsKey("auditEndTime")){
			hql .append(" and checkTime <= to_date('"+condition.get("auditEndTime")+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if (null!=objPage && ExStringUtils.isNotBlank(objPage.getOrderBy())) {
			hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		}else {
			hql.append(" order by subjectTime desc");
		}
		return hql.toString();
		
	}
	/**
	 * 批量删除
	 * @param ids
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);					
			}
		}
	}
	
	/**
	 * 删除
	 * @param ids
	 */
	@Override
	public void delete(Serializable id) throws ServiceException {
		NoExamApply noExamApply = get(id);
		if("1".equals(noExamApply.getCheckStatus())){
			throw new ServiceException("学生："+noExamApply.getStudentInfo().getStudentName()+" 申请的免修免考已审核通过，不能删除.");
		}
		StudentCheck check = new StudentCheck();
		check.setStudentId(noExamApply.getStudentInfo().getResourceid());
		exGeneralHibernateDao.save(check);
		super.delete(id);
	}
	/**
	 * 批量审核通过
	 * @param ids
	 * @param userid
	 * @param cnName
	 */
	@Override
	public void batchAudit(String[] ids, String userid, String cnName) throws ServiceException{
		if(ids!=null && ids.length>0){
			for (String id : ids) {
				audit(id, userid, cnName);
			}
		}
	}
	/**
	 * 审核通过
	 */
	@Override
	public void audit(String resourceid, String userid, String cnName) throws ServiceException{
		NoExamApply apply = get(resourceid);
		if ("0".equals(apply.getCheckStatus())) {
			apply.setCheckManId(userid);
			apply.setCheckMan(cnName);
			apply.setCancleDate(new Date());
			apply.setCheckStatus("1");
			//插入成绩 默认为70分
			apply.setCourseScoreType("11");
			apply.setScoreForCount(70d);
			
			StudentCheck check = new StudentCheck();
			check.setStudentId(apply.getStudentInfo().getResourceid());
			exGeneralHibernateDao.save(check);
		}
	}

	/**
	 * 批量取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 */
	@Override
	public void batchCancel(String[] ids, String userid, String cnName) throws ServiceException{
		if(ids!=null && ids.length>0){
			for (String id : ids) {
				cancel(id, userid, cnName);
			}
		}
	}
	/**
	 * 取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 */
	@Override
	public void cancel(String resourceid, String userid, String cnName) throws ServiceException{
		NoExamApply apply = get(resourceid);
		if ("1".equals(apply.getCheckStatus()) || "2".equals(apply.getCheckStatus())) {
			apply.setCheckStatus("0");
			//撤销分数
			apply.setCourseScoreType(null);
			apply.setScoreForCount(null);
			
			StudentCheck check = new StudentCheck();
			check.setStudentId(apply.getStudentInfo().getResourceid());
			exGeneralHibernateDao.save(check);
		}
	}
	
	
	/**
	 * 批量审核通过
	 * @param resourceid
	 * @param userid
	 * @param cnName
	 * @param messageStr
	 */
	@Override
	public void batchAudit(String[] ids, String userid, String cnName,StringBuffer messageStr) throws ServiceException{
		if(ids!=null && ids.length>0){
			Date curTime = new Date();
			for (String id : ids) {
				audit(id, userid, cnName,messageStr,curTime);
			}
		}
	}
	/**
	 * 审核通过
	 * @param resourceid
	 * @param userid
	 * @param cnName
	 * @param messageStr
	 * @param checkTime
	 */
	@Override
	public void audit(String resourceid, String userid, String cnName,StringBuffer messageStr,Date checkTime) throws ServiceException{
		NoExamApply apply = get(resourceid);
		if ("0".equals(apply.getCheckStatus())) {
			apply.setCheckManId(userid);
			apply.setCheckMan(cnName);
			apply.setCheckTime(checkTime);
			apply.setCheckStatus("1");
			//插入成绩 默认为70分
			//apply.setScoreForCount(70d);
			apply.setCourseScoreType("11");
			
			// 删除该门课程存在非发布状态的成绩以及对应的补考名单
			// 1、判断该学生该门课程最近一次考试补考名单下一次考试批次对应的成绩是否存在或者已发布
			Map<String,Object> params = new HashMap<String, Object>();
			String studentId = apply.getStudentInfo().getResourceid();
			String courseId = apply.getCourse().getResourceid();
			List<Map<String, Object>> makeupList = studentMakeupListService.findByStudentIdAndCourseId(
					studentId, courseId);
			if(makeupList != null && !makeupList.isEmpty()) {
				Map<String, Object> makeup = makeupList.get(0);
				params.put("courseId", courseId);
				params.put("studentId", studentId);
				params.put("examSub", makeup.get("nextexamsubid"));
				List<ExamResults> examResultList = examResultsService.queryExamResultsByCondition(params);
				// 2、未发布，则删除对应考试批次的成绩以及该补考名单
				if(examResultList != null && examResultList.size() > 0) {
					ExamResults examResults = examResultList.get(0);
					if(! Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(examResults.getCheckStatus())) {
						examResultsService.delete(examResults);
					}
				} 
				// 3、不存在成绩，则删除该补考名单；
				studentMakeupListService.delete((String)makeup.get("resourceid"));
			} else {
				// 4、看看正考成绩是否是已发布状态，否，则删除该成绩
				params.clear();
				params.put("courseId", courseId);
				params.put("studentId", studentId);
				params.put("examtype", "N");
				List<Map<String, Object>> _examResultList = examResultsService.findBySidAndCidAndExamType(params);
				if(_examResultList != null && _examResultList.size() > 0){
					Map<String, Object>  _examResult = _examResultList.get(0);
					examResultsService.delete((String)_examResult.get("resourceid"));
				}
			}
			
			messageStr.append( apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>审核成功。<br>");
			
			StudentCheck check = new StudentCheck();
			check.setStudentId(apply.getStudentInfo().getResourceid());
			exGeneralHibernateDao.save(check);
		}else{
			messageStr.append(apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>已审核，如需更改请撤销审核后再执行此操作。<br>");
		}
	}
	/**
	 * 批量取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 * @param messageStr
	 */
	@Override
	public void batchCancel(String[] ids, String userid, String cnName,StringBuffer messageStr) throws ServiceException{
		if(ids!=null && ids.length>0){
			Date curDate = new Date();
			for (String id : ids) {
				cancel(id, userid, cnName, messageStr,curDate);
			}
		}
	}
	/**
	 * 取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 * @param messageStr
	 */
	@Override
	public void cancel(String resourceid, String userid, String cnName,StringBuffer messageStr,Date checkTime) throws ServiceException{
		NoExamApply apply = get(resourceid);
		if ("1".equals(apply.getCheckStatus()) || "2".equals(apply.getCheckStatus())) {
			apply.setCheckStatus("0");
			//撤销分数
			apply.setCancleDate(checkTime);
			apply.setCourseScoreType(null);
			apply.setScoreForCount(null);
			messageStr.append( apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>撤销成功。<br>");
			
			StudentCheck check = new StudentCheck();
			check.setStudentId(apply.getStudentInfo().getResourceid());
			exGeneralHibernateDao.save(check);
		}else{
			messageStr.append(apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>不是审核通过或不通过状态，撤销失败<br>");
		}
	}
	/**
	 * 学习中心替学生申请免修免考-保存/更新（条件检查）
	 * @param map
	 * @param curTime
	 * @param stu
	 * @param noExamIds
	 * @param courseIds
	 * @return
	 * @throws ParseException
	 */
	public boolean noExamAppSaveByBrSchoolCheck(User curUser,Map<String,Object> map,StudentInfo stu, String[] unScores,String [] courseIds,Date curTime) throws ParseException{
		
		String msg 				= "";
		boolean isPass          = true;
		boolean isAllowTime     = false; 
		Double oldCredit      	= 0D ;
		Double appCredit        = 0D ;
		List<Dictionary> dicLis = CacheAppManager.getChildren("CodeNoExamApplyTime");
		
		for (int i = 0 ;i < dicLis.size(); i++) {
			Dictionary   d      = dicLis.get(i);
			List<Date> dl   	= new ArrayList<Date>(); 
			String statTime 	= d.getDictName();
			String endTime  	= d.getDictValue();
			if (ExStringUtils.isNotBlank(statTime) && ExStringUtils.isNotBlank(endTime)) {
				Date s_Time 	=  ExDateUtils.convertToDate(statTime);
				Date e_Time 	=  ExDateUtils.convertToDate(endTime);
				dl.add(s_Time);
				dl.add(e_Time);
			}
			map.put("appTime_"+i, dl);
		}
		
		StringBuffer ds= new StringBuffer();
		for (String key : map.keySet()) {
			if (null!=map.get(key)) {
				List<Date>  allowDate = (List<Date>)map.get(key);
				Date s_t  = null==allowDate.get(0)?null:allowDate.get(0);
				Date e_t  = null==allowDate.get(1)?null:allowDate.get(1);
				if (null!=s_t && null!=e_t && curTime.getTime()>=s_t.getTime()&&curTime.getTime()<=e_t.getTime()) {
					ds.append(ExDateUtils.formatDateStr(s_t, ExDateUtils.PATTREN_DATE_TIME) +" 至 "+ExDateUtils.formatDateStr(e_t, ExDateUtils.PATTREN_DATE_TIME)+"<br/>");
					isAllowTime = true;
					break;
				}
			}
		}
		//如果不是校外学习中心人员，不受申请时间限制
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())<0){
			isAllowTime          = true;
		}
		List<NoExamApply> list1 	 = findByHql(" from "+NoExamApply.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? ",0,stu.getResourceid());
		List<StateExamResults> list2 = stateExamResultsService.findByHql(" from "+StateExamResults.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? ", 0,stu.getResourceid());
		
		Map<String,Double>  creditMap= new HashMap<String,Double>();
		for (TeachingPlanCourse plc : stu.getTeachingPlan().getTeachingPlanCourses()) {
			creditMap.put(plc.getCourse().getResourceid(), plc.getCreditHour());
		}
		//计算已申请的免修免考学分
		for (NoExamApply no : list1) {
			Double credit = creditMap.get(no.getCourse().getResourceid());
			if(null!=credit&&!"2".equals(ExStringUtils.trimToEmpty(no.getUnScore()))) {
				oldCredit+= credit;//免修不计入限制的学分
			}
		}
		//计算已申请的统考表里存放的免修免考学分
		for (StateExamResults sr : list2) {
			//免修不计入限制的学分
			if (!"2".equals(ExStringUtils.trimToEmpty(sr.getScoreType()))) {
				for (int i = 0; i < courseIds.length; i++) {
					if (null!=sr.getCourse()&&sr.getCourse().getResourceid().equals(courseIds[i])&&ExStringUtils.isNotBlank(unScores[i])) {
						Double credit = creditMap.get(sr.getCourse().getResourceid());
						if(null!=credit) {
							oldCredit+= credit;
						}
					}
				}	
			}
		}
		for (int i = 0; i < courseIds.length; i++) {
			Double credit = creditMap.get(courseIds[i]);
			if (ExStringUtils.isNotBlank(unScores[i])&&!"2".equals(ExStringUtils.trimToEmpty(unScores[i]))) {
				if(null!=credit) {
					appCredit+= credit;
				}
			}
		}
		//2014-10-29  改为不限制学分
//		if (isAllowTime==false||(appCredit+oldCredit)>20) {
//			isPass = false;
//		}
		if (isAllowTime==false) {
			msg 			   = "保存失败，不在申请时间范围内,申请时间:<br/>"+ds.toString();
			
		}
		
		//2014-10-29  改为不限制学分
//		if((appCredit+oldCredit)>20){
//			msg 			   = "保存失败，你已申请:"+oldCredit+"个学分的免修免考,本次申请:"+appCredit+"个学分,已超过20学分！";
//		}
		map.put("message", msg);
		return isPass;
	}
	/**
	 * 学习中心替学生申请免修免考-保存/更新
	 * @param map                 
	 * @param courseTypes       成绩类型
	 * @param noExamIds			免修免考ID 
	 * @param courseIds         课程ID
	 * @param memos             备注
	 * @param other_memos       申请理由选择其它时填入的值
	 * @param unScores          申请类型
	 * @param studentId         学籍ID
	 * @throws Exception
	 */
	@Override
	public void noExamAppSaveByBrSchool(Map<String, Object> map, String[] courseTypes, String[] noExamIds, String[] courseIds, String[] memos, String [] other_memos, String[] unScores, String [] scores, String studentId)throws Exception {

		User user	   = SpringSecurityHelper.getCurrentUser();
		Date   curTiem = ExDateUtils.getCurrentDateTime();
		StudentInfo stu= studentInfoService.get(studentId);

		String hql1    = " from "+NoExamApply.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ?";
		String hql2    = " from "+StateExamResults.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ? ";
		
		Course course  = null;
		NoExamApply app= null;
		boolean isAllow= noExamAppSaveByBrSchoolCheck(user,map,stu,unScores,courseIds,curTiem);
		Pattern pattern= Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
		List<Dictionary> children  = CacheAppManager.getChildren("CodeNoExamReasonCorrespondScore");
		Map<String,String> scoreMap= new HashMap<String, String>();
		for (Dictionary dict :children) {
			if (Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(dict.getIsUsed()))) {
				scoreMap.put(dict.getDictName(), dict.getDictValue());
			}
		}
		for (int i = 0; i < courseIds.length; i++) {		
			if (ExStringUtils.isNotBlank(unScores[i])) {
				if (ExStringUtils.isNotBlank(noExamIds[i])) {
					app 		 = get(noExamIds[i]);
				}else {
					app 		 = new NoExamApply();
				}
				if (ExStringUtils.isNotBlank(app.getCheckStatus())&&"0".equals(app.getCheckStatus())&&isAllow) {
					String memo  = ExStringUtils.trimToEmpty(memos[i]);
					course  	 = courseService.get(courseIds[i]);				
					app.setCourse(course);
					app.setCourseScoreType(courseTypes[i]);
					
					String score = "";
//					if ("Other".equals(memo)||"HaveScore".equals(memo)) {
//						score    = ExStringUtils.trimToEmpty(scores[i]);
//					}else {
//						score    = scoreMap.get(memo);
//					}
					/**
					 * uodate 2014-7-22 15:37:39  改成手动填写免考分数
					 */
					score    = ExStringUtils.trimToEmpty(scores[i]);
					
					if (ExStringUtils.isNotBlank(other_memos[i])) {
						app.setMemo(other_memos[i]);
					}else {
						memo 	 = JstlCustomFunction.dictionaryCode2Value("CodeNoExamReason", memos[i]);
						if(ExStringUtils.isNotEmpty(memo)){
							app.setMemo(memo);
						}else{
							app.setMemo(memos[i]);
						}
					}
					if(ExStringUtils.isBlank(score)) {
						throw new ServiceException("数字字典中未设置申请理由:"+memo+"，对应的成绩分值!");
					}
					Matcher m  	 = pattern.matcher(score);
					if (Boolean.FALSE == m.matches()) {
						throw new ServiceException(" 课程："+course.getCourseName()+"的成绩应为0-100的数字</br>");
					}
					app.setScoreForCount(Double.valueOf(score));
					app.setStudentInfo(stu);
					app.setSubjectTime(curTiem);
					app.setUnScore(unScores[i]);
					app.setCheckStatus("0");
					
					//校验该用户不能重复申请 :一个学籍只能申请一次相同课程的免修免考				
					List<NoExamApply> list1 	 = findByHql(hql1,0,stu.getResourceid(),courseIds[i]);
					List<StateExamResults> list2 = stateExamResultsService.findByHql(hql2, 0,stu.getResourceid(),courseIds[i]);
					if(ExCollectionUtils.isNotEmpty(list1)||ExCollectionUtils.isNotEmpty(list2)){
						throw new ServiceException("学生 "+stu.getStudentName()+" 已申请过 <font color='blue'>"+course.getCourseName()+"</font> 的免修免考或统考！<br/>如未审核，请联系教务管理人员.");
					}
					saveOrUpdate(app);
					
					map.put("statusCode", 200);
					map.put("message", "保存成功!<br/>");
					
					logger.info("学生: "+stu.getStudentName()+" 学号："+stu.getStudyNo()+" 申请类型："+JstlCustomFunction.dictionaryCode2Value("CodeUnScoreStyle",unScores[i])+" 申请时间："+ExDateUtils.formatDateStr(curTiem, ExDateUtils.PATTREN_DATE_TIME)+" 操作人："+user.getCnName()+" 操作人ID："+user.getResourceid());
				}else {
					map.put("statusCode", 300);
				}
			}
		}
	}
	/**
	 * 免修免考导入
	 * 
	 * @param datas
	 * @param returnMap
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> noExamApplyImport(List<NoExamApplyVo> datas, Map<String, Object> returnMap) throws Exception {
		
		if (null!=datas && !datas.isEmpty()) {
			
			Map<String,Map<String, TeachingPlanCourse>> stuAppMap = new HashMap<String, Map<String, TeachingPlanCourse>>();//导入文件中学生申请的课程
			Map<String,StudentInfo> stuNoMap  					  = new HashMap<String, StudentInfo>();//key:stuNo value:StudentInfo
			Map<String,TeachingPlanCourse> tpc					  = new HashMap<String, TeachingPlanCourse>();	   //key:teachingPlanId+courseName value:Course
			Map<String,NoExamApply> existsApp 					  = new HashMap<String, NoExamApply>();//key:stuNo+courseName value:NoExamApply
			Map<String,Double> existsAppCreditHour                = new HashMap<String, Double>();
			Map<String,Double> curAppCreditHour                   = new HashMap<String, Double>();
			Map<String,String> allowAppType                       = new HashMap<String, String>();
			Map<String,String> stuCourseAppType                   = new HashMap<String, String>();         
			List<String> stuNoList            					  = new ArrayList<String>();
			StringBuilder msg                 					  = new StringBuilder(" ");
			User curUser                      					  = SpringSecurityHelper.getCurrentUser();
			Date curDate                      					  = ExDateUtils.getCurrentDateTime();
			List<Dictionary> dictSet                              = CacheAppManager.getChildren("CodeNoExamAppType");
			
			for (Dictionary dict : dictSet ) {
				allowAppType.put(dict.getDictName().trim(),dict.getDictValue());
			}
			
			
			//第一步，取得本次导入同一学生的申请记录及学号
			//20180919 bug,当输入的学号不存在于系统时，报错，因此应先去掉不存在于系统中的学号
			for(NoExamApplyVo vo :datas){
				String studyNo 				  = ExStringUtils.trimToEmpty(vo.getStudyNo());
				if (ExStringUtils.isNotEmpty(studyNo)) {
					stuNoList.add(studyNo);//获取要导入的所有学号
				}
			}
			List<StudentInfo> stuList		  = studentInfoService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.in("studyNo", stuNoList));
			for(Iterator<NoExamApplyVo> it = datas.iterator();it.hasNext();){
				NoExamApplyVo  vo= it.next();
				String studyNo = vo.getStudyNo();
				List<String> studyNos = ExCollectionUtils.fetchPropertyToList(stuList, "studyNo");
				if(!studyNos.contains(studyNo)){
					msg.append("学号:"+studyNo+" 的学生不存在于系统，请检查学生学号是否正确后再重新导入！</br>");
					it.remove();
				}
			}
			if(ExCollectionUtils.isNotEmpty(datas)){
				for (NoExamApplyVo vo :datas) {
					
					String studyNo 				  = ExStringUtils.trimToEmpty(vo.getStudyNo());
					String courseName      		  = ExStringUtils.trimToEmpty(vo.getCourseName());;
					String applyType              = ExStringUtils.trimToEmpty(vo.getApplyType());
					
//					if (ExStringUtils.isNotEmpty(studyNo)) {
//						stuNoList.add(studyNo);//获取要导入的所有学号
//					}
					Map<String,TeachingPlanCourse> tpcs = null;  
					if (stuAppMap.containsKey(studyNo)) {						   //获取导入文件中同一学生的所有申请记录
						tpcs                      = stuAppMap.get(studyNo);
					}else {
						tpcs                      = new HashMap<String, TeachingPlanCourse>();
					}
					stuCourseAppType.put(studyNo+courseName,applyType);
					tpcs.put(courseName,null);
					stuAppMap.put(studyNo,tpcs);
				}
				String stuNos                     = "'"+ExStringUtils.join(stuNoList, "','")+"'";
				//第二步，根据学号取得学籍、免修免考申请、统考记录
				
				List<NoExamApply> appList   	  = findByHql(" from "+NoExamApply.class.getSimpleName()+" app where app.isDeleted=? and app.studentInfo.studyNo in("+stuNos+")",0);
				
				List<StateExamResults> statRslist = stateExamResultsService.findByHql(" from "+StateExamResults.class.getSimpleName()+" rs where rs.isDeleted=? and rs.studentInfo.studyNo in("+stuNos+")",0);
				stuNos                            = null;
				//第三步，将学籍放入MAP，设置好本次申请的课程与教学计划课程的对应关系
				for (StudentInfo stu : stuList) {
					String studyNo                = stu.getStudyNo();
					stuNoMap.put(studyNo, stu);//取得导入文件中学号对应的学籍
					Set<TeachingPlanCourse> set   = stu.getTeachingPlan().getTeachingPlanCourses();
					Map<String,TeachingPlanCourse>appCourse = stuAppMap.get(studyNo);
					for (TeachingPlanCourse pc: set) {
						tpc.put(studyNo+pc.getCourse().getCourseName(), pc);//取得学生教学计划课程
						if (appCourse.containsKey(pc.getCourse().getCourseName())) {
							appCourse.put(pc.getCourse().getCourseName(),pc);//将导入文件中同一学生的所有申请记录的教学计划课程放入一个MAP中
						}
					}
					stuAppMap.put(studyNo,appCourse);
				}
				
				//第四步，计算已申请免考的学分，及本次申请的学分
				//1.计算已申请的学分(免修免考表中的记录)
				for (NoExamApply app : appList) {
					String studyNo                = app.getStudentInfo().getStudyNo();
					String courseName             = app.getCourse().getCourseName();
					existsApp.put(studyNo+courseName,app);//将导入文件中学号对应的已申请过的免修免考记录放入一个MAP中
					
					if ("1".equals(ExStringUtils.trimToEmpty(app.getUnScore()))) {//计算已免考学分
						Double existsCreditTotal  = existsAppCreditHour.containsKey(studyNo)?existsAppCreditHour.get(studyNo):0;
						Double courseCredit       = tpc.containsKey(studyNo+courseName)?
													(null!=tpc.get(studyNo+courseName).getCreditHour()?tpc.get(studyNo+courseName).getCreditHour():0):
													(null!=app.getCourse().getPlanoutCreditHour()?app.getCourse().getPlanoutCreditHour():0);
													
						existsAppCreditHour.put(studyNo,existsCreditTotal+courseCredit);
					}
				}
				
				//2.计算已申请的统考表里存放的免修免考学分
				for (StateExamResults sr : statRslist) {
					String studyNo                = sr.getStudentInfo().getStudyNo();
					String courseName             = sr.getCourse().getCourseName();
					//免修不计入限制的学分
					if (!"2".equals(ExStringUtils.trimToEmpty(sr.getScoreType()))&&stuAppMap.get(studyNo).containsKey(courseName)) {
						Double existsCreditTotal  = existsAppCreditHour.containsKey(studyNo)?existsAppCreditHour.get(studyNo):0;
						Double courseCredit       = tpc.containsKey(studyNo+courseName)?
												    (null!=tpc.get(studyNo+courseName).getCreditHour()?tpc.get(studyNo+courseName).getCreditHour():0):
												    (null!=sr.getCourse().getPlanoutCreditHour()?sr.getCourse().getPlanoutCreditHour():0);
						existsAppCreditHour.put(studyNo,existsCreditTotal+courseCredit);						    
					}
				}
				
				//3.计算本次申请的学分
				for (String stuNo : stuAppMap.keySet()) {
					Double appCredit              = 0D ;
					Collection<TeachingPlanCourse> appPc = stuAppMap.get(stuNo).values();
					for (TeachingPlanCourse pc:appPc){
						if ("免考".equals(stuCourseAppType.get(stuNo + pc.getCourse().getCourseName()))) {
							appCredit                += (null!=pc.getCreditHour()?pc.getCreditHour():0);
						}
					}
					curAppCreditHour.put(stuNo, appCredit);
				}
				appList                           = new ArrayList<NoExamApply>();
				Pattern pattern 			 	  = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
				//第五步，将符合条件的导入数据写到免修免考表中，设置为审核通过
				for (NoExamApplyVo vo :datas) {
					
					String studyNo 				  = ExStringUtils.trimToEmpty(vo.getStudyNo());
					String courseName      		  = ExStringUtils.trimToEmpty(vo.getCourseName());;
					String reason                 = ExStringUtils.trimToEmpty(vo.getReason());			
					String applyType              = ExStringUtils.trimToEmpty(vo.getApplyType());
					String score                  = ExStringUtils.trimToEmpty(vo.getScore());
					
					Matcher m 					  = pattern.matcher(score);
					if (Boolean.FALSE == m.matches()) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"的成绩应为0-100的数字</br>");
						continue;
					}
					//学号不能为空
					if (ExStringUtils.isBlank(studyNo)) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，数据不完整，学号不能为空！</br>");
						continue;
					}
					
					//课程不能为空
					if (ExStringUtils.isBlank(courseName)) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，数据不完整，课程不能为空！</br>");
						continue;
					}
					//申请类型不能为空
					if (ExStringUtils.isBlank(applyType)) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，未填写申请类型，请根据情况填入：免修或免考！</br>");
						continue;
					}
					if (ExStringUtils.isBlank(score)) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，数据不完整，成绩不能为空！</br>");
						continue;
					}
					//判断是否已申请
					if (existsApp.containsKey(studyNo+courseName)) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，系统中已存在免修免考，记录，当前状态为："+JstlCustomFunction.dictionaryCode2Value("CodeCheckStatus",existsApp.get(studyNo+courseName).getCheckStatus())+"！</br>");
						continue;
					}
					
					//申请类型只能是免修或免考
					if (ExStringUtils.isNotBlank(applyType)&&!allowAppType.containsKey(applyType.trim())) {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，请根据情况填入：免修或免考！</br>");
						continue;
					}
					//注：此行代码需放置在申请类型判断之后！
					String unScore 				  = allowAppType.get(applyType.trim());
					if ("1".equals(unScore) &&((existsAppCreditHour.containsKey(studyNo)?existsAppCreditHour.get(studyNo):0)+(curAppCreditHour.containsKey(studyNo)?curAppCreditHour.get(studyNo):0))>20) {
						msg.append("学号:"+studyNo+",课程："+courseName+" 已申请:"+existsAppCreditHour.get(studyNo)+"个学分的免修免考,本次申请:"+curAppCreditHour.get(studyNo)+"个学分,已超过20学分！</br>");
						continue;
					}
					
					if (tpc.containsKey(studyNo+courseName)){
						
						NoExamApply app = new NoExamApply();
						app.setCheckMan(curUser.getCnName());
						app.setCheckManId(curUser.getResourceid());
						app.setCheckStatus("1");
						app.setCourseScoreType("11");
						app.setStudentInfo(stuNoMap.get(studyNo));
						app.setSubjectTime(curDate);
						app.setCheckTime(curDate);
						app.setCourse(tpc.get(studyNo+courseName).getCourse());
						app.setUnScore(unScore);
						app.setMemo(reason);
						app.setScoreForCount(Double.valueOf(score));
						
						
						appList.add(app);
					}else {
						msg.append("学号:"+studyNo+" 课程："+courseName+"，在系统中无法找到对应的记录，或者学生所修教学计划不包括申请课程，请检查要导入的学号与课程！</br>");
					}
				}
				try {
					logger.debug("免修免考导入：操作人："+curUser.getCnName()+" ID："+curUser.getResourceid()+" 时间："+ExDateUtils.formatDateStr(curDate,ExDateUtils.PATTREN_DATE_TIME)+"总人数："+datas.size()+" 成功人数:"+appList.size()+" 操作结果："+msg.toString());
				} catch (ParseException e) {
				}
				batchSaveOrUpdate(appList);
				if (ExStringUtils.isBlank(msg.toString())) {
					returnMap.put("msg", "导入成功！");
					returnMap.put("success",true);
				}else {
					returnMap.put("msg",msg.toString());
					returnMap.put("success",false);
				}
			}
			
			
		}else {
			returnMap.put("success", false);
			returnMap.put("msg", "文件内容为空，请上传一个含有免修免考数据的文件！");
		}
		return returnMap;
	}
}
