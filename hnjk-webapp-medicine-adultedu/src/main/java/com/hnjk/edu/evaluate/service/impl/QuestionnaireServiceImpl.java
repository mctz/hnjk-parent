package com.hnjk.edu.evaluate.service.impl;

import java.rmi.ServerException;
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
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.evaluate.model.Questionnaire;
import com.hnjk.edu.evaluate.model.QuestionnaireBatch;
import com.hnjk.edu.evaluate.model.StuQuestionnaire;
import com.hnjk.edu.evaluate.service.IQuestionnaireBatchService;
import com.hnjk.edu.evaluate.service.IQuestionnaireService;
import com.hnjk.edu.evaluate.service.IStuQuestionnaireService;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.ICourseStatusClService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;

@Transactional
@Service("iQuestionnaireService")
public class QuestionnaireServiceImpl extends BaseServiceImpl<Questionnaire> implements IQuestionnaireService{
	

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("iStuQuestionnaireService")
	private IStuQuestionnaireService iStuQuestionnaireService;
	
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService iTeachingJDBCService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService iTeachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService iYearInfoService;
	
	@Autowired
	@Qualifier("courseStatusClService")
	private ICourseStatusClService courseStatusClService;
	
	@Autowired
	@Qualifier("iQuestionnaireBatchService")
	private IQuestionnaireBatchService iQuestionnaireBatchService;
	
	@Override
	public List<Questionnaire> findQuestionnaire(Map<String,Object> values) throws ServerException{
		
		String hql = " from "+Questionnaire.class.getSimpleName()+" where classes.resourceid=:classesid "
				+ " and course.resourceid=:courseid  and "
				+ " questionnaireBatch.resourceid =:batchid and isDeleted=0";
		if(values.containsKey("teacherid")){
			hql+=" and teacher.resourceid=:teacherid ";
		}
		
		List<Questionnaire> list = this.findByHql(hql, values);
		return list;
	}
	
	@Override
	public void batchDeleteQuestionnaire(Map<String,Object> values) throws ServerException{
		//唯一：班级、课程、老师、年度、学期、课程类型 
		List<Questionnaire> list = findQuestionnaireList(values);
		if(list !=null && list.size()>0){
			for(Questionnaire qn : list){
				qn.setIsDeleted(1);
				qn.setUpdatDate(new Date());
			}
			this.batchSaveOrUpdate(list);
		}
		
	}

	/**
	 * @param values
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Questionnaire> findQuestionnaireList(Map<String, Object> values)
			throws ServiceException {
		String hql = " from "+Questionnaire.class.getSimpleName()+" where isDeleted=0  ";		
		if(values.containsKey("classesid")){
			hql+=" and classes.resourceid=:classesid ";
		}
		if(values.containsKey("courseid")){
			hql+=" and course.resourceid=:courseid ";
		}
		if(values.containsKey("teacherid")){
			hql+=" and teacher.resourceid in(:teacherid) ";
		}
		if(values.containsKey("batchid")){
			hql+=" questionnaireBatch.resourceid =:batchid ";
		}
		if(values.containsKey("yearId")){
			hql+=" and questionnaireBatch.yearInfo.resourceid=:yearId ";
		}
		if(values.containsKey("term")){
			hql+=" and questionnaireBatch.term=:term ";
		}
		if(values.containsKey("firstYear")){
			hql+=" and questionnaireBatch.yearInfo.firstYear=:firstYear ";
		}
		if(values.containsKey("courseType")){
			hql+=" and courseType=:courseType ";
		}
		List<Questionnaire> list = this.findByHql(hql, values);
		return list;
	}
	
	@Override
	public List<Questionnaire> getQuestionnaire(String classesid,String studentInfoid,String returnFlag) throws ServerException{
		Map<String,Object> values = new HashMap<String, Object>();

		List<Questionnaire> returnlist = new ArrayList<Questionnaire>();
		List<Questionnaire> listQnNO = new ArrayList<Questionnaire>();//NO
		List<Questionnaire> listQnYes = new ArrayList<Questionnaire>();//YES
		values.put("classesid", classesid);
		values.put("now", new Date());
		String hql = "from "+Questionnaire.class.getSimpleName()+ " where isDeleted=0 and classes.resourceid=:classesid and isPublish='Y' and startTime<=:now order by course.courseCode";		
		//查找所有的问卷
		List<Questionnaire> listQn = this.findByHql(hql, values);

		//查找学生提交的问卷
		List<StuQuestionnaire> listSqn = iStuQuestionnaireService.findByClassesId(classesid, studentInfoid);
		
		if(listQn!=null && listQn.size()>0){
			if(listSqn==null || listSqn.size()==0){//当 问卷不为空，学生问卷为空时
				listQnNO.addAll(listQn);				
			}else{//问卷不为空，学生问卷也不为空
				for(Questionnaire qn :listQn){
					//遍历问卷
					for(StuQuestionnaire sqn:listSqn){
						//遍历学生提交的问卷
						if(sqn.getQuestionnaire().equals(qn)){//学生已提交过该问卷
							listQnYes.add(qn);//添加到 学生已提交问卷列表
							break;
						}
					}
				}
				listQnNO.addAll(listQn);
				listQnNO.removeAll(listQnYes);
			}			
		}
		if("NO".equalsIgnoreCase(returnFlag)){
			returnlist.addAll(listQnNO);
		}else if("YES".equalsIgnoreCase(returnFlag)){
			returnlist.addAll(listQnYes);
		}else{
			returnlist.addAll(listQn);
		}
		return returnlist;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findQuestionnairePage(Map<String, Object> condition,Page objPage) throws Exception{		
		String sql =findQuestionnairePageSQL(condition);		
		Page page = iTeachingJDBCService.findBySql(objPage, sql, condition);
		return page;
	}

	/**
	 * 
	 */
	private String findQuestionnairePageSQL(Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select  u.unitname,cl.classesname,c.coursename,y.yearname,substr(cs.term,7,1) term,               ");
		sql.append("  us.cnname,us.username, aa.stuCount,  decode(bb.sqCount, null, 0, bb.sqCount) sqCount,            ");
		sql.append("  qn.isPublish,decode(dd.validCount, null, 0, dd.validCount) validCount,cs.teachType,              ");
		sql.append("  case when dd.validCount is not null then trunc((dd.validCount/aa.stuCount), 3) * 100 || '%'      ");
		sql.append("  else '0' end as pervalid, qn.starttime,qn.endtime,qn.resourceid                                  ");
		sql.append("  from edu_teach_coursestatus cs                                                                   ");
		sql.append(" join edu_teach_plancourse pc on pc.resourceid = cs.plancourseid and pc.isdeleted = 0              ");
		sql.append(" join edu_teach_guiplan gp on gp.resourceid = cs.guiplanid and gp.isdeleted = 0                    ");
		sql.append(" join edu_base_grade g on g.resourceid = gp.gradeid                      ");
		sql.append(" join edu_base_year y on  y.isdeleted = 0  and y.firstyear = substr(cs.term, 0, 4)                 ");
		sql.append(" join edu_teach_plan p on p.resourceid = gp.planid and p.isdeleted = 0                             ");
		sql.append(" join hnjk_sys_unit u on u.resourceid = cs.schoolids                    ");
		sql.append(" join edu_base_course c on c.resourceid = pc.courseid and c.isdeleted = 0                          ");
		sql.append(" join (                                                                                            ");
		sql.append(" select s.branchschoolid,s.gradeid,s.classicid,s.teachingtype,s.majorid,                           ");
		sql.append(" s.classesid,count(s.resourceid) stuCount from edu_roll_studentinfo s                              ");
		sql.append(" where s.studentstatus ='11'                                                                       ");
		sql.append(" group by s.branchschoolid,s.gradeid,s.classicid,s.teachingtype,s.majorid,s.classesid              ");
		sql.append(" ) aa on aa.branchschoolid = u.resourceid and aa.gradeid = g.resourceid and                        ");
		sql.append(" aa.classicid = p.classicid and aa.teachingtype = p.schooltype and aa.majorid = p.majorid          ");
		sql.append(" join edu_roll_classes cl on cl.resourceid = aa.classesid and cl.isdeleted = 0                     ");		
		sql.append(" left join edu_evaluate_questionnaire qn on qn.classesid = cl.resourceid and qn.isdeleted = 0  and qn.courseid = pc.courseid    ");
		sql.append(" left join hnjk_sys_users us on us.resourceid = qn.teacherid and us.isdeleted = 0                 ");
		sql.append(" and qn.courseid  = c.resourceid and qn.isdeleted = 0                 ");
		sql.append(" left join edu_evaluate_batch b on b.resourceid = qn.evaluatebatchid and b.isdeleted = 0           ");
		sql.append(" and b.yearid = y.resourceid and b.term = substr(cs.term,7,1)                                      ");
		sql.append(" left join (                                                                                       ");
		sql.append("  select sq.questionnaireid, count(sq.resourceid) sqCount                                          ");
		sql.append("  from edu_evaluate_stuquestionnaire sq                                                            ");
		sql.append("  join edu_roll_studentinfo s on s.resourceid = sq.studentinfoid                                   ");
		sql.append("  and s.studentstatus = '11'  and s.isdeleted = 0                                                  ");
		sql.append("  where sq.isdeleted = 0                                                                           ");
		sql.append("  group by sq.questionnaireid) bb on bb.questionnaireid = qn.resourceid                            ");
		sql.append(" left join (                                                                                       ");
		sql.append("  select sq.questionnaireid, count(sq.resourceid) validCount                                       ");
		sql.append("  from edu_evaluate_stuquestionnaire sq                                                            ");
		sql.append("  join edu_roll_studentinfo s on s.resourceid = sq.studentinfoid                                   ");
		sql.append("  and s.studentstatus = '11' and s.isdeleted = 0                                                   ");
		sql.append("  where sq.isdeleted = 0 and sq.isvalid = 'Y'                                                      ");
		sql.append("  group by sq.questionnaireid) dd on dd.questionnaireid = qn.resourceid                            ");
		sql.append(" where  cs.isdeleted = 0 and cs.isopen ='Y'                                 ");
		
		if(condition.containsKey("brSchool")){
			sql.append(" and cs.schoolids=:brSchool ");
		}
		if(condition.containsKey("classesId")){
			sql.append(" and cl.resourceid=:classesId ");
		}
		if(condition.containsKey("courseId")){
			sql.append(" and c.resourceid=:courseId ");
		}
		if(condition.containsKey("teacherName")){//hql like 无法解析占位符			
			sql.append(" and us.cnname like '%'||:teacherName||'%' ");
		}
//		if(condition.containsKey("yearId")){
//			sql.append(" and y.resourceid=:yearId ");
//		}
//		if(condition.containsKey("term")){
//			sql.append(" and b.term=:term ");
//		}
		if(condition.containsKey("csTerm")){
			sql.append(" and cs.term=:csTerm ");
		}
		if(condition.containsKey("isPublish")){
			sql.append(" and qn.isPublish=:isPublish ");
		}
		if(condition.containsKey("teacherid")){
			sql.append(" and us.resourceid=:teacherid ");
		}
		if(condition.containsKey("hasTeacher")){
			if("Y".equalsIgnoreCase(condition.get("hasTeacher").toString())){//查询条件为Y时
				sql.append(" and us.resourceid is not null ");
			}else{
				sql.append(" and us.resourceid is null ");
			}			
		}
		if(condition.containsKey("teachType")){
			sql.append(" and cs.teachType=:teachType ");
		}
		
		sql.append(" order by  y.yearname desc,b.term,u.unitcode,u.unitname,cl.classesname                                            ");
		return sql.toString();
	}
	@Override
	public boolean cancelQuestionnaire(String resourceids, String operate) throws Exception{
		List<Questionnaire> list = new ArrayList<Questionnaire>();
		for(String resourceid : resourceids.split("\\,")){
			Questionnaire qn = this.get(resourceid);
			if(qn!=null){
				if("cancel".equalsIgnoreCase(operate)){//操作标识：cancel/update  发布/取消发布
					qn.setIsPublish("N");
				}else{
					qn.setIsPublish("Y");
				}
				qn.setUpdatDate(new Date());
				list.add(qn);
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public boolean editQuestionnaireTime(String resourceids, String startDate,String endDate) throws Exception{
		List<Questionnaire> list = new ArrayList<Questionnaire>();
		for(String resourceid : resourceids.split("\\,")){
			Questionnaire qn = this.get(resourceid);
			if(qn!=null){
				qn.setStartTime(ExDateUtils.convertToDateTime(startDate));
				qn.setEndTime(ExDateUtils.convertToDateTime(endDate));
				qn.setUpdatDate(new Date());
				list.add(qn);
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);
			return true;
		}else{
			return false;
		}
		
	}
	@Override
	public boolean updateQuestionnaire(String resourceids,String teachType) throws Exception{
		for(String resourceid : resourceids.split("\\,")){
			TeachingPlanCourseStatus cs = iTeachingPlanCourseStatusService.get(resourceid);
			//查找是否已经生成了问卷，查找条件：开课的教学点、年度、学期
			List<Questionnaire> list = new ArrayList<Questionnaire>();
			Map<String ,Object > values = new HashMap<String, Object>();
			
			String hql = " from "+Questionnaire.class.getSimpleName()+" qn ";
			hql+=" where qn.questionnaireBatch.yearInfo.resourceid=:yearId and qn.questionnaireBatch.term=:term";
			hql+=" and qn.classes.brSchool.resourceid=:unitId and qn.course.resourceid=:courseid and qn.isDeleted=0 ";
			YearInfo year = iYearInfoService.findUniqueByProperty("firstYear", Long.parseLong(cs.getTerm().substring(0, 4)));
			values.put("yearId", year.getResourceid());
			values.put("term", cs.getTerm().substring(6, 7));
			values.put("unitId", cs.getSchoolIds());
			values.put("courseid", cs.getTeachingPlanCourse().getCourse().getResourceid());
			list = this.findByHql(hql, values);
			//如果未生成，跳过，如果已经生成，则把这些问卷的类型相应的修改
			if(list==null || list.size()==0){//如果是从网络设置为面授，未找到，就要生成一条问卷记录
				if("faceTeach".equalsIgnoreCase(teachType)){
					QuestionnaireBatch qb = iQuestionnaireBatchService.findUnique("from "+QuestionnaireBatch.class.getSimpleName()+
							" where yearInfo.resourceid=? and term=? and isDeleted=0", new Object[]{year.getResourceid(),cs.getTerm().substring(6, 7)});

					Map<String,Object> param = new HashMap<String, Object>();
					param.put("firstYear", cs.getTerm().substring(0, 4));
					param.put("term",cs.getTerm().substring(6, 7));
					param.put("teachType", "faceTeach");
					param.put("brSchoolid", cs.getSchoolIds());
					param.put("courseid", cs.getTeachingPlanCourse().getCourse().getResourceid());
					List<CourseTeacherCl> cclist = courseStatusClService.findCourseTeacherCl_new(param);
					if(cclist!=null && cclist.size()>0 && qb!=null){
						for(CourseTeacherCl cl:cclist){
							iQuestionnaireBatchService.createQuestionnaire(list, param, qb, cl);
							
						}
					}
				}
			}else{//当前不做网络课程的问卷，所以直接删除
				if(!"faceTeach".equalsIgnoreCase(teachType)){
					for (Questionnaire qn :list){
						qn.setCourseType("1");
						qn.setIsDeleted(1);
						qn.setIsPublish("N");
						qn.setUpdatDate(new Date());
					}
				}				
			}
			if(list!=null && list.size()>0){
				this.batchSaveOrUpdate(list);
				return true;
			}
		}
		return true;
	}
}
