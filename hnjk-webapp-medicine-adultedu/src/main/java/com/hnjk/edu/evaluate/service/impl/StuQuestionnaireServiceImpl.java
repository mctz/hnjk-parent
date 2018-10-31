package com.hnjk.edu.evaluate.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.evaluate.model.QuestionBank;
import com.hnjk.edu.evaluate.model.Questionnaire;
import com.hnjk.edu.evaluate.model.StuQuestionBank;
import com.hnjk.edu.evaluate.model.StuQuestionnaire;
import com.hnjk.edu.evaluate.service.IQuestionBankService;
import com.hnjk.edu.evaluate.service.IQuestionnaireService;
import com.hnjk.edu.evaluate.service.IStuQuestionBankService;
import com.hnjk.edu.evaluate.service.IStuQuestionnaireService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.platform.system.cache.CacheAppManager;

@Transactional
@Service("iStuQuestionnaireService")
public class StuQuestionnaireServiceImpl extends BaseServiceImpl<StuQuestionnaire> implements IStuQuestionnaireService{

	@Autowired
	@Qualifier("iQuestionnaireService")
	private IQuestionnaireService iQuestionnaireService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService iStudentInfoService;
	
	@Autowired
	@Qualifier("iQuestionBankService")
	private IQuestionBankService iQuestionBankService;
	
	@Autowired
	@Qualifier("iStuQuestionBankService")
	private IStuQuestionBankService iStuQuestionBankService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService iTeachingJDBCService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public List<StuQuestionnaire> findByClassesId(String classesid,String studentInfoid){
		Map<String ,Object > values = new HashMap<String, Object>();
		values.put("classesid", classesid);
		values.put("studentInfoid", studentInfoid);
		String hql=" from "+StuQuestionnaire.class.getSimpleName()+" sqn where sqn.isDeleted=0 ";
		hql+=" and sqn.questionnaire.classes.resourceid =:classesid and sqn.studentInfo.resourceid=:studentInfoid ";
		hql+=" order by sqn.questionnaire.course.courseCode ";
		return this.findByHql(hql, values);
	}
	@Override
	public boolean stuQuestionnaireSave(String studentInfoid,String commentlabel,String resourceids,String questionnaireid) throws Exception{
		List<StuQuestionBank> sqbList = new ArrayList<StuQuestionBank>();
		StuQuestionnaire sqn = new StuQuestionnaire();
		sqn.setCommentlabel(commentlabel);
		Questionnaire qn = iQuestionnaireService.get(questionnaireid);
		sqn.setQuestionnaire(qn);
		sqn.setStudentInfo(iStudentInfoService.get(studentInfoid));
		double totalScore = 0.0;
		String [] questionBanks = resourceids.split(";");
		for(String keys:questionBanks){
			String [] tmp =keys.split(":");
			String questionBankid=tmp[0];
			String strStuScore=tmp[1];
			double stuScore = Double.parseDouble(strStuScore);
			totalScore+=stuScore;
			QuestionBank qb = iQuestionBankService.get(questionBankid);
			StuQuestionBank sqb = new StuQuestionBank();
			sqb.setQuestionShowOrder(qb.getShowOrder());
			sqb.setQuestionTarget(qb.getQuestionTarget());
			sqb.setQuestionScore(qb.getScore());
			sqb.setQuestion(qb.getQuestion());
			sqb.setStuScore(stuScore);
			sqb.setStuQuestionnaire(sqn);
			sqb.setUpdateDate(new Date());
			sqbList.add(sqb);
		}
		sqn.setTotalScore(totalScore);
		Date now = new Date();
		sqn.setUpdatDate(now);
		if(now.after(qn.getStartTime()) && now.before(qn.getEndTime())){
			sqn.setIsvalid("Y");//有效条件：问卷开始时间<当前时间<问卷结束时间
		}else{
			sqn.setIsvalid("N");
		}
		if(sqn!=null && sqbList.size()>0){
			this.saveOrUpdate(sqn);//保存学生提交的问卷
			iStuQuestionBankService.batchSaveOrUpdate(sqbList);//保存学生提交的问卷题目
			return true;
		}else{
			return false;
		}
	}
	@Override
	public Page findStuQuestionnairePage(Map<String ,Object> condition,Page objPage) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" select teacherid,batchid,username,cnname,yearname,term,unitname,                                      ");
		//此处的validper 有效问卷收集率= 有效问卷之和/班级人数之和
//		sql.append(" round(avg(totalscore),1) validavg,round(sum(validCount-round(validCount*0.1)*2)/sum(stuCount),3)*100||'%' validper ");
		sql.append(" round(avg(totalscore),1) validavg,round(sum(validCount)/sum(stuCount),3)*100||'%'  validper ");
		sql.append("  from (                                                                                                ");
		findTeacherCourseGroup(sql);
		if(condition.containsKey("brSchool")){
			sql.append(" and u.resourceid=:brSchool ");
		}
		if(condition.containsKey("yearId")){
			sql.append(" and y.resourceid=:yearId ");
		}
		if(condition.containsKey("term")){
			sql.append(" and b.term=:term ");
		}
		if(condition.containsKey("teacherName")){
			sql.append(" and us.cnname like '%'||:teacherName||'%' ");
		}
		if(condition.containsKey("teacherAccount")){
			sql.append(" and us.username like '%'||:teacherAccount||'%' ");
		}
		if(condition.containsKey("teacherid")){
			sql.append(" and us.resourceid=:teacherid ");
		}
		sql.append(" ) where lagscore!=-1 and leadscore!=-1                                                                 ");
		sql.append(" group by teacherid,batchid,username,cnname,yearname,term,unitname                                      ");
		sql.append(" order by yearname desc,term,username,cnname                                                            ");
		
		objPage.setPageSize(100);
		Page page = iTeachingJDBCService.findBySql(objPage, sql.toString(), condition);
		return page;
	}
	@Override
	public List<Map<String,Object>> findSqnCourse(Map<String ,Object> condition) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" select  unitname,teacherid,batchid,qnid,username,cnname,yearname,term,courseid,coursename,classesname,stuCount,validCount,  ");
		//此处的validper 有效问卷收集率= 有效问卷之和/班级人数之和
//		sql.append(" round(avg(totalscore),1) validavg,round(sum(validCount-round(validCount*0.1)*2)/sum(stuCount),3)*100||'%' validper ");
		sql.append(" round(avg(totalscore),1) validavg,round(sum(validCount)/sum(stuCount),3)*100||'%' validper ");
		sql.append("  from (                                                                                                ");
		findTeacherCourseGroup(sql);
		if(condition.containsKey("teacherid")){
			sql.append(" and us.resourceid=:teacherid ");
		}
		if(condition.containsKey("brSchool")){
			sql.append(" and u.resourceid=:brSchool ");
		}
		if(condition.containsKey("yearId")){
			sql.append(" and y.resourceid=:yearId ");
		}
		if(condition.containsKey("term")){
			sql.append(" and b.term=:term ");
		}
		if(condition.containsKey("orgUnitType")){//编制
			sql.append(" and us.orgUnitType=:orgUnitType ");
		}
		sql.append(" ) where lagscore!=-1 and leadscore!=-1                                                                 ");
		sql.append(" group by unitname,teacherid,batchid,qnid,username,cnname,yearname,term,courseid,coursename,classesname,stuCount,validCount  ");
		sql.append(" order by courseid  ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
	}
	
	@Override
	public Page findSqnListByQn(Map<String ,Object> condition,Page objPage) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" select teacherid,batchid,qnid,username,cnname,yearname,term,courseid,classesname,studyno,studentname,totalscore,commentlabel,                                                                  ");
		sql.append(" case when lagscore=-1 or leadscore=-1 then '<font color=\"red\">否</font>' else '是' end isUse ");
		sql.append("  from (                                                                                                ");
		findTeacherCourseGroup(sql);
		if(condition.containsKey("questionnaireid")){
			sql.append(" and qn.resourceid=:questionnaireid ");
		}
		sql.append(" )                                                                 ");		
		sql.append(" order by studyno,courseid                                                            ");
		objPage.setPageSize(20);
		return iTeachingJDBCService.findBySql(objPage, sql.toString(), condition);
	}
	/**
	 * @param sql
	 */
	private void findTeacherCourseGroup(StringBuffer sql) {
		double perRate = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("maxScore_per").getParamValue());
		sql.append(" select sqn.resourceid sqnid,b.resourceid batchid,qn.resourceid qnid,u.unitname,                      ");
		sql.append(" us.resourceid teacherid,lag(sqn.totalscore,round(bb.validCount*"+perRate+"),-1) over                         ");
		sql.append(" (partition by us.resourceid,qn.resourceid order by co.resourceid,sqn.totalscore desc) lagscore,      ");
		sql.append(" lead(sqn.totalscore,round(bb.validCount*"+perRate+"),-1)                                                     ");
		sql.append(" over (partition by us.resourceid,qn.resourceid order by co.resourceid,sqn.totalscore desc) leadscore, ");
		sql.append(" us.username,us.cnname,y.yearname,b.term,bb.validCount,                                               ");
		sql.append(" aa.stuCount,s.studentname,s.studyno,sqn.totalscore,sqn.commentlabel,                                 ");
		sql.append(" trunc((bb.validCount-round(bb.validCount*"+perRate+")*2)/aa.stuCount,3) perValid           ");
		sql.append(" ,co.resourceid courseid,aa.classesname,co.coursename                                                               ");
		sql.append(" from edu_evaluate_stuquestionnaire sqn                                                               ");
		sql.append(" join edu_evaluate_questionnaire qn on qn.resourceid = sqn.questionnaireid  and qn.isdeleted=0                          ");
		sql.append(" join edu_evaluate_batch b on b.resourceid = qn.evaluatebatchid   and b.isdeleted=0                                    ");
		sql.append(" join edu_base_year y on y.resourceid = b.yearid                                                      ");
		sql.append(" join hnjk_sys_users us on us.resourceid = qn.teacherid                                               ");
		sql.append(" join hnjk_sys_unit u on u.resourceid = us.unitid                                              ");
		sql.append(" join edu_base_course co on co.resourceid = qn.courseid                                               ");
		sql.append(" join edu_roll_studentinfo s on s.resourceid = sqn.studentinfoid                                      ");
		sql.append(" join(                                                                                                ");
		sql.append(" select cl.resourceid,cl.classesname,count(s1.resourceid) stuCount from edu_roll_classes cl           ");
		sql.append(" join edu_roll_studentinfo s1 on s1.classesid = cl.resourceid                                         ");
		sql.append(" where s1.studentstatus='11'                                                                          ");
		sql.append(" group by cl.resourceid,cl.classesname                                                                ");
		sql.append(" ) aa on aa.resourceid = qn.classesid                                                                 ");
		sql.append(" join (                                                                                               ");
		sql.append(" select qn1.teacherid,qn1.evaluatebatchid,qn1.resourceid,count(sqn1.resourceid) validCount            ");
		sql.append(" from edu_evaluate_stuquestionnaire sqn1                                                              ");
		sql.append(" join edu_evaluate_questionnaire qn1 on qn1.resourceid = sqn1.questionnaireid  and qn1.isdeleted=0                        ");
		sql.append(" where sqn1.isdeleted = 0 and sqn1.isvalid = 'Y'                                                      ");
		sql.append(" group by qn1.teacherid,qn1.evaluatebatchid,qn1.resourceid                                            ");
		sql.append(" ) bb on bb.teacherid = qn.teacherid and bb.evaluatebatchid = qn.evaluatebatchid                      ");
		sql.append("  and bb.resourceid = qn.resourceid                                                                   ");
		sql.append(" where sqn.isdeleted = 0 and sqn.isvalid = 'Y'                                                        ");
	}
}
