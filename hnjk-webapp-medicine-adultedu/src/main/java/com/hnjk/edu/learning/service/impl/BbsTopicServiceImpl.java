package com.hnjk.edu.learning.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.BbsGroup;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.BbsUserInfo;
import com.hnjk.edu.learning.service.IBbsGroupService;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.IBbsUserInfoService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IRollJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 论坛帖子管理服务接口实现
 * <code>BbsTopicServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-7 上午11:55:14
 * @see 
 * @version 1.0
 */
@Transactional
@Service("bbsTopicService")
public class BbsTopicServiceImpl extends BaseServiceImpl<BbsTopic> implements IBbsTopicService {
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("bbsUserInfoService")
	private IBbsUserInfoService bbsUserInfoService;
	
	@Autowired
	@Qualifier("bbsGroupService")
	private IBbsGroupService bbsGroupService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;         //JDBC服务 
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("bbsReplyService")
	private IBbsReplyService bbsReplyService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService tpctService;
	
	@Autowired
	@Qualifier("rollJDBCService")
	private IRollJDBCService rollJDBCService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findBbsTopicByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = getBbsTopicHql(condition, values);
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by b."+objPage.getOrderBy().replace(",", ",b.") +" "+ objPage.getOrder());
		}
		Page page = exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
		List<BbsTopic> bbsTopics = page.getResult();
		User user = SpringSecurityHelper.getCurrentUser();
		// 获取这个用户所有所在的教学点
		for (BbsTopic bbsTopic : bbsTopics) {
			String hqls = "from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.resourceid=(select ue.exValue from "+UserExtends.class.getSimpleName()+" ue where ue.isDeleted=0 and ue.exCode='defalutrollid' and ue.user.resourceid=?) ";
			StudentInfo _studentInfo = studentInfoService.findUnique(hqls, bbsTopic.getBbsUserInfo().getSysUser().getResourceid());
			if((_studentInfo !=null && user.getOrgUnit().getResourceid().equals(_studentInfo.getBranchSchool().getResourceid()))
					|| SpringSecurityHelper.isTeachingCentreTeacher(user)){
				String classesId = "";
				try {
					Map<String, Object> classesIdMap = rollJDBCService.findClassesByUserId(bbsTopic.getBbsUserInfo().getSysUser().getResourceid());
					if(classesIdMap!=null && classesIdMap.size() > 0){
						classesId = (String)classesIdMap.get("classesId");
					}
				} catch (Exception e) {}
				bbsTopic.setHasCheckAuthority(tpctService.hasAuthority(user.getResourceid(),bbsTopic.getCourse().getResourceid(),classesId));
			} else {
				bbsTopic.setHasCheckAuthority(false);
			}
			
			if (_studentInfo != null) {
				if(_studentInfo.getClasses()!=null){
					bbsTopic.setClasses(_studentInfo.getClasses());
				}
				bbsTopic.setUnitName(_studentInfo.getBranchSchool().getUnitName());
			} else {
				bbsTopic.setClasses(new Classes());
				bbsTopic.setUnitName("");
			}
		}
		return page;
	}

	private StringBuffer getBbsTopicHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select b from "+BbsTopic.class.getSimpleName()+" b where b.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("startLong")){
			hql.append(" and b.howLongReply >= :startLong ");
			values.put("startLong", new BigDecimal((String)condition.get("startLong")));
		}
		if(condition.containsKey("endLong")){
			hql.append(" and b.howLongReply <= :endLong ");
			values.put("endLong",new BigDecimal((String)condition.get("endLong")));
		}
		if(condition.containsKey("feedbackSectionCode")){//反馈版块
			hql.append(" and b.bbsSection.sectionCode <> :feedbackSectionCode ");
			values.put("feedbackSectionCode", condition.get("feedbackSectionCode"));
		}
		if(condition.containsKey("title")){//标题
			hql.append(" and lower(b.title) like lower(:title) ");
			values.put("title", "%"+condition.get("title")+"%");
		}	
		if(condition.containsKey("content")){//内容
			hql.append(" and lower(b.content) like lower(:content) ");
			values.put("content", "%"+condition.get("content")+"%");
		}
		if(condition.containsKey("titlecontent")){//全文
			hql.append(" and (lower(b.title) like :titlecontent or lower(b.content) like :titlecontent or lower(b.keywords) like :titlecontent) ");
			values.put("titlecontent", "%"+condition.get("titlecontent").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("topicType")){//帖子类型
			hql.append(" and b.topicType =:topicType ");
			values.put("topicType", condition.get("topicType"));
		}
		if(condition.containsKey("replyCount")){//回复
			hql.append(" and b.replyCount >:replyCount ");
			values.put("replyCount", condition.get("replyCount"));
		}
		if(condition.containsKey("isAnswered")){
			hql.append(" and b.isAnswered =:isAnswered ");
			values.put("isAnswered", Integer.parseInt(condition.get("isAnswered").toString()));
		}
		if(condition.containsKey("status")){//帖子状态
			hql.append(" and b.status =:status ");
			values.put("status", Integer.parseInt(condition.get("status").toString()));
		}
		if(condition.containsKey("bbsSectionId")){//所属论坛版块
			hql.append(" and b.bbsSection.isDeleted=0 and (b.bbsSection.parent.isDeleted=0 or b.bbsSection.parent is null) ");
			hql.append(" and b.bbsSection.resourceid =:bbsSectionId ");
			values.put("bbsSectionId", condition.get("bbsSectionId"));
		}
		if(condition.containsKey("masterId")){//版主
			hql.append(" and b.bbsSection.masterId like :masterId ");
			values.put("masterId", "%"+condition.get("masterId")+"%");//版主
		}		
		if(condition.containsKey("teacherId") && condition.containsKey("classesIds")){//课程老师并且是班主任
			hql.append(" and exists ( select te.classes.resourceid from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.isDeleted = 0 and (te.teacherId = :teacherId or te.classes.resourceid in (:classesIds)) and te.teachingPlanCourse.course.resourceid = b.course.resourceid Group by te.classes.resourceid ) ");
			values.put("teacherId", condition.get("teacherId"));//老师
			values.put("classesIds", condition.get("classesIds"));//班主任
		} else {
			if(condition.containsKey("teacherId")){//课程老师
				hql.append(" and exists ( select te.classes.resourceid from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.isDeleted = 0 and te.teacherId = :teacherId and te.teachingPlanCourse.course.resourceid = b.course.resourceid Group by te.classes.resourceid ) ");
				values.put("teacherId", condition.get("teacherId"));//老师
			}	
			if(condition.containsKey("classesIds")){//班主任
				hql.append(" and exists ( select te.classes.resourceid from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.isDeleted = 0 and te.classes.resourceid in (:classesIds) and te.teachingPlanCourse.course.resourceid = b.course.resourceid Group by te.classes.resourceid ) ");
				values.put("classesIds", condition.get("classesIds"));//班主任
			}	
		}
		if(condition.containsKey("schoolId")){// 教务员
			hql.append(" and exists ( select te.classes.resourceid from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.isDeleted = 0 ");
			hql.append(" and te.classes.resourceid in (select cl.resourceid from Classes cl where cl.isDeleted=0 and cl.brSchool.resourceid=:schoolId) and te.teachingPlanCourse.course.resourceid = b.course.resourceid Group by te.classes.resourceid ) ");
			values.put("schoolId", condition.get("schoolId"));// 教务员
		}
		if(condition.containsKey("sectionCode")){//所属论坛版块编码
			hql.append(" and b.bbsSection.sectionCode =:sectionCode ");
			values.put("sectionCode", condition.get("sectionCode"));
		}
		if(condition.containsKey("notSectionCode")){//所属论坛版块编码
			hql.append(" and b.bbsSection.sectionCode !=:notSectionCode ");
			values.put("notSectionCode", condition.get("notSectionCode"));
		}
		if(condition.containsKey("fillinManId")){//发贴人id
			hql.append(" and b.bbsUserInfo.sysUser.resourceid =:fillinManId ");
			values.put("fillinManId", condition.get("fillinManId"));
		}
		if(condition.containsKey("fillinMan")){//发贴人
			hql.append(" and b.fillinMan like :fillinMan ");
			values.put("fillinMan", "%"+condition.get("fillinMan")+"%");
		}
		if(condition.containsKey("courseId")){//课程
			hql.append(" and b.course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("courseIds")){//课程
			hql.append(" and b.course.resourceid in (:courseIds) ");
			values.put("courseIds", condition.get("courseIds").toString().split("\\,"));
		}
		if(condition.containsKey("groupIds")){//小组
			hql.append(" and b.bbsGroup.resourceid in (:groupIds) ");
			values.put("groupIds", condition.get("groupIds").toString().split("\\,"));
		}
		if(condition.containsKey("notInBbsSectionCodes")){//排除版块
			hql.append(" and b.bbsSection.sectionCode not in (:notInBbsSectionCodes) ");
			values.put("notInBbsSectionCodes", condition.get("notInBbsSectionCodes").toString().split("\\,"));
		}
		if(condition.containsKey("isCourseSection")){//是否课程版块
			hql.append(" and b.bbsSection.isCourseSection =:isCourseSection) ");
			values.put("isCourseSection", condition.get("isCourseSection"));
		}
		if(condition.containsKey("orgUnitId")){
//			hql.append(" and b.bbsUserInfo.studentInfo.branchSchool.resourceid =:orgUnitId");
//			hql.append(" and b.bbsUserInfo.sysUser.resourceid in (select so.sysUser.resourceid from StudentInfo so where so.isDeleted=0 and so.branchSchool.resourceid=:orgUnitId)");
			hql.append(" and b.bbsUserInfo.sysUser.orgUnit.resourceid=:orgUnitId ");
			values.put("orgUnitId", condition.get("orgUnitId"));
		}
		/*if(condition.containsKey("courseName")){//课程
			hql.append(" and b.course.courseCode =:courseName ");
			values.put("courseName", condition.get("courseName"));
		}*/
		//前端传过来的的courseName是id
		if(condition.containsKey("courseName")){//课程
			hql.append(" and b.course.resourceid =:courseName ");
			values.put("courseName", condition.get("courseName"));
		}
		if(condition.containsKey("groupName")){//小组
			hql.append(" and lower(b.bbsGroup.groupName) like:groupName ");
			values.put("groupName", "%"+condition.get("groupName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("gtReplyCount")){
			hql.append(" and b.replyCount >0 ");
		}
		if(condition.containsKey("emptyCourse")){
			hql.append(" and b.course is null ");
		}
		if(condition.containsKey("parenTopicId")){//父帖
			hql.append(" and b.parenTopic.resourceid =:parenTopicId ");
			values.put("parenTopicId", condition.get("parenTopicId"));
		}
		if(condition.containsKey("parenTopic")){
			hql.append(" and b.parenTopic is null ");
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql.append(" and b.syllabus.resourceid =:syllabusId ");
			values.put("syllabusId", condition.get("syllabusId"));
		}
		if(condition.containsKey("tags")){//标记
			hql.append(" and b.tags =:tags ");
			values.put("tags", condition.get("tags"));
		}
		if(condition.containsKey("nottags")){//除此标记
			hql.append(" and b.tags !=:nottags ");
			values.put("nottags", condition.get("nottags"));
		}
			
		if(condition.containsKey("yearInfoId")){
			hql.append(" and b.yearInfo.resourceid =:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and b.term =:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("notEmptySyllabus")){
			hql.append(" and syllabus is not null ");
		}
		if(condition.containsKey("topLevel")) {
			if(Integer.parseInt(condition.get("topLevel").toString())>0){ //置顶帖
				hql.append(" and b.topLevel > 0 ");
			} else {
				hql.append(" and b.topLevel = 0 ");
			}
		}
		if(condition.containsKey("facebookType")){
			hql.append(" and b.facebookType =:facebookType ");
			values.put("facebookType", condition.get("facebookType"));
		}
		if(condition.containsKey("fillinDateStart")){
			hql.append(" and b.fillinDate >= :fillinDateStart ");
			values.put("fillinDateStart", new Date(Long.parseLong(condition.get("fillinDateStart").toString())));
		}
		if(condition.containsKey("fillinDateEnd")){
			hql.append(" and b.fillinDate <= :fillinDateEnd ");
			values.put("fillinDateEnd", new Date(Long.parseLong(condition.get("fillinDateEnd").toString())));
		}
		if(condition.containsKey("classesId")){//班级
			hql.append(" and exists ( select stu.resourceid from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.classes.resourceid = :classesId and stu.sysUser.resourceid = b.bbsUserInfo.sysUser.resourceid  ) ");
			values.put("classesId", condition.get("classesId"));//班级
		}
		if(condition.containsKey("studentStatus")){
			hql.append(" and b.bbsUserInfo.sysUser.resourceid in (select stu.sysUser.resourceid from "+StudentInfo.class.getSimpleName()+" stu where stu.studentStatus in("+condition.get("studentStatus")+")) ");
		}
		return hql;
	}

	@Override
	public List<BbsTopic> findBbsTopicByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = getBbsTopicHql(condition, values);
		hql.append(" order by b.fillinDate desc ");
		return findByHql(hql.toString(), values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findBbsTopicByFullText(Page page, String[] filed, String keys) throws ServiceException {
		return luceneTextQuery.findByFullText(page, filed, keys, BbsTopic.class);
	}

	@Override
	public void deleteBbsTopic(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				BbsTopic bbsTopic = get(id); 
				//if(bbsTopic.getIsAnswered()==Constants.BOOLEAN_TRUE){
				//	throw new ServiceException("已回答的问题不能删除！");
				//}
				BbsSection bbsSection = bbsTopic.getBbsSection();
				bbsSection.setTopicCount(bbsSection.getTopicCount()-1);//版块帖子数-1
				bbsSection.setTopicAndReplyCount(bbsSection.getTopicAndReplyCount()-bbsTopic.getBbsReplys().size()-1);				
				if(ExDateUtils.isSameDay(bbsSection.getTodayTopicDate(), bbsTopic.getFillinDate())){
					bbsSection.setTodayTopicCount(bbsSection.getTodayTopicCount()-bbsTopic.getBbsReplys().size()-1);
				}
				for (BbsReply reply : bbsTopic.getBbsReplys()) {
					reply.getBbsUserInfo().setTopicCount(reply.getBbsUserInfo().getTopicCount()-1);//用户帖子总数-1
				}
				bbsTopic.getBbsUserInfo().setTopicCount(bbsTopic.getBbsUserInfo().getTopicCount()-1);
				delete(id);	
				logger.info("删除论坛帖子="+id);
			}
		}
	}	
	/*
	 * status:置顶3,精华1,锁定-1,还原0,取消置顶-3
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.IBbsTopicService#setBbsTopicStatus(java.lang.String[], java.lang.Integer)
	 */
	@Override
	public void setBbsTopicStatus(String[] ids, Integer status) throws ServiceException {
		if(ids!=null && ids.length>0){
			switch (status) {
				case 0:					
				case 1:
				case -1:
					for(String id : ids){
						BbsTopic bbsTopic = get(id); 
						bbsTopic.setStatus(status);
						update(bbsTopic);
						logger.info("更新论坛帖子状态="+id);
					}
					break;
				case -3:
					for(String id : ids){
						BbsTopic bbsTopic = get(id); 
						bbsTopic.setTopLevel(0);
						update(bbsTopic);
						logger.info("更新论坛帖子状态="+id);
					}
					break;
				case 3:
					Integer topLevel = exGeneralHibernateDao.findUnique("select max(b.topLevel) from "+BbsTopic.class.getSimpleName()+" b where b.isDeleted=0 and b.bbsSection.resourceid=? ", get(ids[0]).getBbsSection().getResourceid());;
					for(String id : ids){
						BbsTopic bbsTopic = get(id); 
						bbsTopic.setTopLevel(++topLevel);
						update(bbsTopic);
						logger.info("更新论坛帖子状态="+id);
					}
					break;
			}
		}
	}

	@Override
	public void saveOrUpdateBbsTopic(BbsTopic bbsTopic, String[] attachIds, String[] groupIds) throws ServiceException {
		User user = SpringSecurityHelper.getCurrentUser();
		user = (User) exGeneralHibernateDao.get(User.class, user.getResourceid());
		if(ExStringUtils.isEmpty(bbsTopic.getResourceid())){//新主题
			BbsSection section = bbsTopic.getBbsSection();
			if (section != null) {
				section.setTopicAndReplyCount(section.getTopicAndReplyCount()+1);//总贴＋1
				if(section.getTodayTopicDate()==null || !ExDateUtils.isSameDay(section.getTodayTopicDate(), new Date())){
					section.setTodayTopicDate(new Date());//设置为当天日期
					section.setTodayTopicCount(0);//重置今日贴为0
				}
				section.setTodayTopicCount(section.getTodayTopicCount()+1);//今日贴＋1
			}
			
			try {
				String Syearterm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				String[] ARRYterm = Syearterm.split("\\.");
				List<Grade> LISTyear = gradeService.findByHql(" from "+Grade.class.getSimpleName() +" y where y.isDeleted = 0 and y.yearInfo.firstYear = ? ", Long.parseLong(ARRYterm[0]));
				if(null != LISTyear && LISTyear.size() > 0){
					bbsTopic.setYearInfo(LISTyear.get(0).getYearInfo());//设置为当前年度学期
					bbsTopic.setTerm(ARRYterm[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(ExStringUtils.isEmpty(bbsTopic.getResourceid())&&bbsTopic.getBbsUserInfo()==null){			
			BbsUserInfo bbsUserInfo = bbsUserInfoService.findUniqueByProperty("sysUser.resourceid",user.getResourceid());
			if(bbsUserInfo==null){//创建论坛用户信息
				bbsUserInfo = new BbsUserInfo();
				bbsUserInfo.setSysUser(user);
				bbsUserInfo.setUserName(user.getUsername());
			}
			bbsTopic.setBbsUserInfo(bbsUserInfo);			
			bbsUserInfo.setTopicCount(bbsUserInfo.getTopicCount()+1);
			bbsUserInfoService.saveOrUpdate(bbsTopic.getBbsUserInfo());
			bbsTopic.setLastReplyManId(bbsUserInfo.getResourceid());
		}
		bbsTopic.setIsAttachs((null != attachIds && attachIds.length >0)?Constants.BOOLEAN_YES:Constants.BOOLEAN_NO);//附件状态
		if(groupIds!=null&&groupIds.length>0){ //小组讨论话题，创建子贴
			for (int i = 0; i < groupIds.length; i++) {
				if(ExStringUtils.isNotEmpty(groupIds[i])){
					BbsGroup bbsGroup = bbsGroupService.get(groupIds[i]);							
					BbsTopic child = new BbsTopic();
					ExBeanUtils.copyProperties(child, bbsTopic);						
					child.setResourceid(null);
					child.setBbsReplys(new HashSet<BbsReply>(0));
					child.setChilds(new HashSet<BbsTopic>(0));
					child.setClickCount(1);
					child.setParenTopic(bbsTopic);
					child.setBbsGroup(bbsGroup);
					bbsTopic.getChilds().add(child);
				}						
			}
		}
		saveOrUpdate(bbsTopic);	
		if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsTopic.getIsAttachs()))//更新附件
		{
			attachsService.updateAttachByFormId(attachIds, bbsTopic.getResourceid(), BbsTopic.class.getSimpleName(), user.getResourceid(), user.getCnName());
		}
	}

	@Override
	public void moveBbsTopic(String[] ids, String bbsSectionId) throws ServiceException {
		BbsSection bbsSection = (BbsSection) exGeneralHibernateDao.get(BbsSection.class, bbsSectionId);
		for (String id : ids) {
			BbsTopic bbsTopic = get(id);
			if(!bbsTopic.getBbsSection().getResourceid().equals(bbsSection.getResourceid())){
				bbsTopic.getBbsSection().setTopicCount(bbsTopic.getBbsSection().getTopicCount().intValue()-1);//主题数－1
				bbsTopic.getBbsSection().setTopicAndReplyCount(bbsTopic.getBbsSection().getTopicAndReplyCount()-1);//帖子数－1
				exGeneralHibernateDao.update(bbsTopic.getBbsSection());
				bbsSection.setTopicCount(bbsSection.getTopicCount().intValue()+1);
				bbsSection.setTopicAndReplyCount(bbsSection.getTopicAndReplyCount().intValue()+1);
				bbsTopic.setBbsSection(bbsSection);
				update(bbsTopic);
				logger.info("移动帖子="+id);
			} 		
		}
	}

	@Override
	public Integer statTopicAndReply(Map<String,Object> condition) throws ServiceException {
		StringBuffer hql1 = new StringBuffer();
		StringBuffer hql2 = new StringBuffer();
		hql1.append("select count(t) from "+BbsTopic.class.getSimpleName()+" t where t.isDeleted=0 ");
		hql2.append("select count(t) from "+BbsReply.class.getSimpleName()+" t where t.isDeleted=0 ");
		
		if(condition.containsKey("courseId")){
			hql1.append(" and t.course.resourceid=:courseId ");
			hql2.append(" and t.bbsTopic.course.resourceid=:courseId ");
		}	
		if(condition.containsKey("userId")){
			hql1.append(" and t.bbsUserInfo.sysUser.resourceid=:userId ");
			hql2.append(" and t.bbsUserInfo.sysUser.resourceid=:userId ");
		}
		if(condition.containsKey("isBest")){
			hql1.append(" and t.isBest=:isBest ");
			hql2.append(" and t.isBest=:isBest ");
		}
		if(condition.containsKey("sectionCode")){
			hql1.append(" and t.bbsSection.sectionCode=:sectionCode ");
			hql2.append(" and t.bbsTopic.bbsSection.sectionCode=:sectionCode ");
		}
		if(condition.containsKey("notSectionCode")){
			hql1.append(" and t.bbsSection.sectionCode<>:notSectionCode ");
			hql2.append(" and t.bbsTopic.bbsSection.sectionCode<>:notSectionCode ");
		}
		Long topics = exGeneralHibernateDao.findUnique(hql1.toString(), condition);
		Long replys = exGeneralHibernateDao.findUnique(hql2.toString(), condition);
		return (topics!=null?topics.intValue():0) + (replys!=null?replys.intValue():0);
	}
	
	@Override
	public void markBbsTopic(String[] ids, String mark,String keywords) throws ServiceException {
		if(ids!=null && ids.length>0){
			for (String id : ids) {
				BbsTopic topic = get(id);
				topic.setTags(mark);
				topic.setKeywords(keywords);
				update(topic);
			}			
		}
	}
	
	/**
	 * 查找需要回复的BBS(存在未解答的提问),如果需要排除已生成任务书且主讲老师或辅导老师不为空请传入excludeNoTask=true
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,Object>> findNeedsReplyBbsTopicInfo(Map<String,Object> param)throws Exception{
		
		StringBuffer sql       = new StringBuffer();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("isDeleted",0);	
		//过滤未生成任务书或 主讲老师或辅导老师为空
		if (param.containsKey("excludeNoTask")&&(Boolean)param.get("excludeNoTask")==true) {
			sql.append(" select ot.*,task.defaultteacherids,task.assistantids from ( ");
		}
		sql.append(" select it.courseid,it.yearInfoId,it.yearname,it.coursename,it.term, nvl(sum(it.unanswered),0) unanswered,nvl(sum(it.answered),0) answered ");
		sql.append("   from (  ");
		sql.append(" 		select tpc.courseid,y.resourceid yearInfoId,y.yearname,tpc.term,c.coursename, ");
		sql.append(" 		  case tpc.isanswered when 0 then count(tpc.resourceid)  end unanswered, ");
		sql.append("  		  case tpc.isanswered when 1 then count(tpc.resourceid)  end answered ");
		sql.append("  		  from edu_bbs_topic tpc ");
		sql.append("  		  inner join edu_base_course c on c.resourceid = tpc.courseid ");
		sql.append("  		  inner join edu_base_year y on tpc.yearid = y.resourceid ");
		sql.append("		  inner join edu_bbs_section s on s.resourceid = tpc.sectionid ");
		
		if (param.containsKey("sectionCode")) {
			sql.append("and s.sectioncode = :sectionCode");
			map.put("sectionCode",param.get("sectionCode"));
		}
		
		sql.append("  		  where tpc.isdeleted = :isDeleted  ");
		
		if (param.containsKey("yearInfoId")) {
			sql.append(" and tpc.yearid = :yearInfoId ");	
			map.put("yearInfoId",param.get("yearInfoId"));
		}
		if (param.containsKey("term")) {
			sql.append("  and tpc.term = :term ");	
			map.put("term",param.get("term"));
		}
		if (param.containsKey("courseId")) {
			sql.append(" and  tpc.courseid = :courseId ");	
			map.put("courseId",param.get("courseId"));
		}
		sql.append("  		  group by tpc.courseid,c.coursename, tpc.isanswered,y.resourceid,y.yearname,tpc.term ");
		sql.append(" ) it group by   it.yearInfoId,it.yearname,it.term,it.courseid,it.coursename having nvl(sum(it.unanswered),0)>0 ");
		
		//过滤未生成任务书或 主讲老师或辅导老师为空
		if (param.containsKey("excludeNoTask")&&(Boolean)param.get("excludeNoTask")==true) {
			sql.append(" ) ot ");
			sql.append(" inner join edu_teach_teachtask task on task.isdeleted=:isDeleted   ");
			sql.append(" and ot.courseid = task.courseid and ot.yearInfoId= task.yearid and ot.term = task.term  ");
			if (param.containsKey("taskstatus")) {
				sql.append(" and task.taskstatus = :taskstatus ");
				map.put("taskstatus",param.get("taskstatus"));
			}
			sql.append(" and (task.assistantids is not null or task.defaultteacherids is not null)");
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), map);
	}
	
	@Override
	public void saveBbsTopicScore(HttpServletRequest request) throws ServiceException {
		String resourceid = request.getParameter("resourceid");
		BbsTopic bbsTopic = get(resourceid);
		bbsTopic.setScoreType(request.getParameter("score"+bbsTopic.getResourceid()));
		//兴瑶老师说 只要评级就有状态改变，故而
		bbsTopic.setIsAnswered(Constants.BOOLEAN_TRUE);	
		List<BbsReply> replyList = bbsReplyService.findByHql("from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsUserInfo.sysUser.userType=? and bbsTopic.resourceid=? order by replyDate asc,resourceid", "student",bbsTopic.getResourceid());
		if(ExCollectionUtils.isNotEmpty(replyList)){
			for (BbsReply bbsReply : replyList) {
				bbsReply.setScoreType(request.getParameter("score"+bbsReply.getResourceid()));
			}
		}		
		
		String replyid = request.getParameter("replyid");		
		String replyContent = request.getParameter("replyContent");
		String[] uploadfileids = request.getParameterValues("uploadfileid");
		BbsReply bbsReply = null;
		if(ExStringUtils.isNotBlank(replyid)){ //--------------------更新
			bbsReply = bbsReplyService.get(replyid);	
			bbsReply.setReplyContent(replyContent);	
			bbsReply.setReplyDate(new Date());
			bbsReply.setIsPersisted(true);			
		}else{ //-------------------------------------------------------------------保存
			if(ExStringUtils.isNotBlank(ExStringUtils.trimToEmpty(replyContent))){
				User user =SpringSecurityHelper.getCurrentUser();
				bbsReply = new BbsReply();							
				bbsReply.setReplyContent(replyContent);
				bbsReply.setReplyDate(new Date());
				bbsReply.setReplyMan(user.getCnName());			
				bbsReply.setShowOrder(bbsReplyService.getNextShowOrder(resourceid));//排序号
				bbsTopic.setLastReplyDate(bbsReply.getReplyDate());
				bbsTopic.setLastReplyMan(bbsReply.getReplyMan());
				if(bbsTopic.getReplyCount()==null) {
					bbsTopic.setReplyCount(0);
				}
				bbsTopic.setReplyCount(bbsTopic.getReplyCount().intValue()+1);
				bbsReply.setBbsTopic(bbsTopic);
				bbsTopic.setIsAnswered(Constants.BOOLEAN_TRUE);	
				
				if(ExStringUtils.isEmpty(bbsTopic.getFirstReplyManId()) || ExStringUtils.isEmpty(bbsTopic.getFirstReplyMan())){
					bbsTopic.setFirstReplyAccount(user.getUsername());
					bbsTopic.setFirstReplyManId(user.getResourceid());
					bbsTopic.setFirstReplyMan(user.getCnName());
					Date now = new Date();
					Date endDate= bbsTopic.getFillinDate();    
					float hour=(now.getTime()-endDate.getTime())/(60*60*1000);
					BigDecimal b = new BigDecimal(hour);
					b.setScale(2, BigDecimal.ROUND_HALF_UP);
					bbsTopic.setHowLongReply(b);
				}
			}			
		}
		if(bbsReply!=null){
			bbsReplyService.saveOrUpdateBbsReply(bbsReply, uploadfileids);	
		}
	}
	
	/**
	 * 保存回复问题的评分信息--（供批量回复使用）
	 * @param topicId
	 * @param scoreType
	 * @param replyContent
	 * @param user
	 * @throws ServiceException
	 */
	@Override
	public void saveReplyTopicScore(String topicId, String scoreType, String replyContent, User user) throws ServiceException{
		BbsTopic bbsTopic = get(topicId);
		if(bbsTopic!=null){
			bbsTopic.setIsAnswered(Constants.BOOLEAN_TRUE);	
			bbsTopic.setScoreType(scoreType);
			List<BbsReply> replyList = bbsReplyService.findByHql("from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsUserInfo.sysUser.userType=? and bbsTopic.resourceid=? order by replyDate asc,resourceid", "student",topicId);
			if(ExCollectionUtils.isNotEmpty(replyList)){
				for (BbsReply bbsReply : replyList) {
					bbsReply.setScoreType(scoreType);
				}
			}
			BbsReply teacherReply = bbsReplyService.getSomeOneLastReply(user.getResourceid(), topicId);
			if(teacherReply!=null){
				teacherReply.setReplyContent(replyContent);	
				teacherReply.setReplyDate(new Date());
				teacherReply.setIsPersisted(true);
			} else {
				teacherReply = new BbsReply();							
				teacherReply.setReplyContent(replyContent);
				teacherReply.setReplyDate(new Date());
				teacherReply.setReplyMan(user.getCnName());			
				teacherReply.setShowOrder(bbsReplyService.getNextShowOrder(topicId));//排序号
				bbsTopic.setLastReplyMan(teacherReply.getReplyMan());
				bbsTopic.setReplyCount(bbsTopic.getReplyCount()==null?0:bbsTopic.getReplyCount().intValue()+1);
				if(ExStringUtils.isEmpty(bbsTopic.getFirstReplyManId()) || ExStringUtils.isEmpty(bbsTopic.getFirstReplyMan())){
					bbsTopic.setFirstReplyAccount(user.getUsername());
					bbsTopic.setFirstReplyManId(user.getResourceid());
					bbsTopic.setFirstReplyMan(user.getCnName());
					Date now = new Date();
					Date endDate= bbsTopic.getFillinDate();    
					float hour=(now.getTime()-endDate.getTime())/(60*60*1000);
					BigDecimal b = new BigDecimal(hour);
					b.setScale(2, BigDecimal.ROUND_HALF_UP);
					bbsTopic.setHowLongReply(b);
				}
				teacherReply.setBbsTopic(bbsTopic);
			}
			
			bbsTopic.setLastReplyDate(teacherReply.getReplyDate());
			// 保存回复信息
			if(teacherReply!=null){
				bbsReplyService.saveOrUpdateBbsReply(teacherReply, null);	
				bbsReplyService.flush();
			}
			// 计算平时分
			Course c = bbsTopic.getCourse();
			User _user = bbsTopic.getBbsUserInfo().getSysUser();
			if(null != _user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				StudentInfo stu = studentInfoService.get(_user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue());
				/*Grade grade = gradeService.getDefaultGrade();
				usualResultsService.saveSpecificUsualResults(stu, grade.getYearInfo().getResourceid(), grade.getTerm(), c.getResourceid());*/
				/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				String yearInfoId = "";
				String term = "";
				if (null!=yearTerm) {
					String[] ARRYyterm = yearTerm.split("\\.");
					yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
					term = ARRYyterm[1];
				}*/
				//开课学期
				TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(c.getResourceid(), stu.getResourceid(),"studentId").getTeachingPlanCourse();
				TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
						.findOneByCondition(stu.getGrade()
								.getResourceid(), stu.getTeachingPlan().getResourceid(),
								teachingPlanCourse.getResourceid(), stu.getBranchSchool().getResourceid());
				/*if(teachingPlanCourseStatus==null){
					//更新学生成绩失败:{课程还没有开课}
					return;
				}*/
				String yearTermStr = teachingPlanCourseStatus.getTerm();
				String year = "";
				String term = "";
				String yearInfoId = "";
				if (null!=yearTermStr) {
					year = yearTermStr.substring(0, 4);
					term = yearTermStr.substring(6, 7);
				}
				YearInfo yearInfo = yearInfoService.getByFirstYear(Long.parseLong(year));
				yearInfoId = yearInfo.getResourceid();
				usualResultsService.saveSpecificUsualResults(stu, yearInfoId, term, c.getResourceid());
			}
		}
	}

}
