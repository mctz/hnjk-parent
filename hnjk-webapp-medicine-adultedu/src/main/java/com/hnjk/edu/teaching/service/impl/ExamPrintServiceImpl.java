package com.hnjk.edu.teaching.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.HeaderFooter;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamPrintService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.edu.teaching.vo.ExportCourseAndTeacherVo;
import com.hnjk.edu.teaching.vo.FailExamStudentVo;
import com.hnjk.edu.teaching.vo.NonexaminationExportVo;
import com.hnjk.edu.teaching.vo.NonexaminationExportVoForGZDX;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 考试打印
 * @author luof
 *
 */
@Service("examPrintService")
@Transactional
public class ExamPrintServiceImpl extends BaseServiceImpl<ExamSub> implements IExamPrintService{
	
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService teachingPlanCourseTimetableService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	
	
	
	/**
	 * 获取所选考试年批次有学生预约考试的学习中心
	 * @param examSublId
	 * @return
	 */
	@Override
	public List<Object> getExamSubUint(String examSublId)throws ServiceException{
		String hql = " select distinct (unit.resourceid),unit.unitName from "+OrgUnit.class.getSimpleName()+" as unit , "+ExamResults.class.getSimpleName()+" as rs ";
		hql +="  where rs.studentInfo.branchSchool.resourceid=unit.resourceid";
		hql +="    and rs.examsubId = ? ";
		List list = exGeneralHibernateDao.findByHql(hql, new String[]{examSublId});
		return list;
	}
	
	/**
	 * 打印准考证列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findExamCardList(Map<String, Object> condition, Page objPage)throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		//hql.append(" select stu from "+StudentInfo.class.getSimpleName()+" stu where stu.resourceid In(");
		//hql.append(" select  rs.studentInfo.resourceid ");
		hql.append(" select distinct(rs.studentInfo.resourceid), ");
		hql.append("rs.studentInfo.studyNo,rs.studentInfo.studentName,");
		hql.append("rs.studentInfo.branchSchool.unitName,");
		hql.append("rs.studentInfo.classic.classicName, ");
		hql.append("rs.studentInfo.grade.gradeName, ");
		hql.append("rs.studentInfo.major.majorName, ");
		hql.append("sub.batchName ");
		hql.append("from ExamResults rs,ExamSub sub ");
		hql.append("where rs.examsubId=sub.resourceid ");
		
		
		if (condition.containsKey("examSub")) {
			hql.append(" and sub.resourceid=:examSub");
		}
		
		if(condition.containsKey("branchSchool")) {
			hql.append(" and rs.studentInfo.branchSchool.resourceid=:branchSchool");
		}
		
		if(condition.containsKey("gradeid")) {
			hql.append(" and rs.studentInfo.grade.resourceid=:gradeid");
		}
		
		if(condition.containsKey("major")) {
			hql.append(" and rs.studentInfo.major.resourceid=:major");
		}
		
		if( condition.containsKey("classic")) {
			hql.append(" and rs.studentInfo.classic.resourceid=:classic");
		}
		
		if(condition.containsKey("studyNo")) {
			hql.append(" and rs.studentInfo.studyNo=:studyNo");
		}
		
		if(condition.containsKey("name")) {
			hql.append(" and rs.studentInfo.studentName like '%"+condition.get("name")+"%'");
		}
		
		hql.append(" order by "+objPage.getOrderBy()+ " "+objPage.getOrder());
		//hql.append(")");
		return this.exGeneralHibernateDao.findByHql(objPage, hql.toString(), condition);
	}	
	/**
	 * 设置excel打印参数
	 * @param filePath
	 * @throws IOException
	 * @throws BiffException
	 * @throws WriteException
	 */
	public static void setExcelPrintSetting(String filePath) throws IOException, BiffException, WriteException {
		//设置excel的页脚
		HeaderFooter pageFooter = new HeaderFooter();						
		pageFooter.getLeft().appendPageNumber();
		pageFooter.getLeft().append("/");
		pageFooter.getLeft().appendTotalPages();
		pageFooter.getRight().append("教师签字：_____________       审核人：_____________");
		
		Workbook book = Workbook.getWorkbook(new File(filePath));
		WritableWorkbook wbook = Workbook.createWorkbook(new File(filePath), book);
		wbook.getSheet(0).getSettings().setFooter(pageFooter);//设置页脚
		wbook.getSheet(0).getSettings().setScaleFactor(90);//设置缩放比例
		book.close();
		wbook.write();
		wbook.close();
	}
	
	/**
	 * 按班级导出补考名单
	 */
	@Override
	public List<FailExamStudentVo> nonexamExportByClasses(
		Map<String, Object> condition) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql = ExamResultsServiceImpl.getMakeupListHql(condition, param, hql);
		List<FailExamStudentVo> list = new ArrayList<FailExamStudentVo>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), param.toArray(), FailExamStudentVo.class);
		} catch (Exception e) {
			return null;
		}
		return list;
	}
	/**
	 * 
	 */
	@Override
	public List<NonexaminationExportVoForGZDX> nonexamExportByClassesForGZDX(Map<String, Object> condition) throws ServiceException{
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select classesName,classicName,teachingtype,classesSum,examclasstype,term,examtype,classesmaster,masterPhone,teacherName,       ");
		hql.append(" teacherPhone,courseName,courseCode,  ");//count(stuids) as studentSum,max(stuids) as stuid
		hql.append(" nvl(LENGTHB(TRANSLATE(stuid,'ABCDEFGHIJKLMNOPQRSTUVWXYZ-1234657890',',')),0)+1 as studentSum,stuid,stunames");
		hql.append(" ,ROWNUM as sortNum");
		hql.append(" from ( select distinct ers.branchschoolid,ers.gradeid,ers.classicid,ers.majorid,ers.classesid,etm.courseid,erc.classesname,            ");
		hql.append(" ebc.classicname,ers.teachingtype,ebco.coursename,etp.examclasstype,etc.term,bb.examtype,                                           ");
		hql.append(" erc.classesmaster,erc.masterphone,decode(etcl.lecturername,null,etcl.teachername,etcl.lecturername) as teachername,    ");
		hql.append(" decode(ebe.mobile,null,ebe.officetel,ebe.mobile) as teacherphone,etm.nextexamsubid,aa.classesSum,ebco.coursecode,      ");
		//hql.append(" wm_concat(ers.studyno) over (partition by ers.branchschoolid,ers.gradeid,                ");
		//hql.append(" ers.classicid,ers.teachingtype,ers.majorid,ers.classesid,etm.courseid,etm.nextexamsubid                                ");
		//hql.append(" order by ers.studyno ) as stuids                                                         ");
		hql.append(" wm_concat(distinct ers.studyno) over(partition by ers.classesid, etm.courseid, etm.nextexamsubid ) as stuid ");
		hql.append(" ,wm_concat(distinct ers.studentname) over(partition by ers.classesid, etm.courseid, etm.nextexamsubid ) as stunames ");
		hql.append(" from edu_teach_makeuplist etm                                                                                          ");
		hql.append(" join edu_roll_studentinfo ers on ers.resourceid = etm.studentid and ers.isdeleted = 0                                  ");
		if (condition.containsKey("gradeId")) {
			 hql.append(" and ers.gradeid=:gradeId ");
			 param.add(condition.get("gradeId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and ers.branchschoolid=:branchSchool ");
			param.add(condition.get("branchSchool"));
		}else if (condition.containsKey("unitId")) {
			hql.append(" and ers.branchschoolid=:unitId ");
			 param.add(condition.get("unitId"));
		}
		if (condition.containsKey("major")) {
			 hql.append(" and ers.majorid =:major ");
			 param.add(condition.get("major"));
		}
		if (condition.containsKey("classicId")) {
			 hql.append(" and ers.classicid =:classicId ");
			 param.add(condition.get("classicId"));
		}
		hql.append(" join edu_roll_classes erc on erc.resourceid = ers.classesid and ers.isdeleted = 0                                      ");
		if (condition.containsKey("classesId")) {
			 hql.append(" and ers.classesid =:classesId ");
			 param.add(condition.get("classesId"));
		}
		hql.append(" left join edu_teach_graduatedata gd on gd.studentid=ers.resourceid and gd.isdeleted=0                                  ");
		hql.append(" join edu_base_classic ebc on ebc.resourceid = ers.classicid and ebc.isdeleted = 0                                      ");
		hql.append(" join edu_base_grade ebg on ebg.isdeleted = 0 and ebg.resourceid = ers.gradeid                                     ");
		hql.append(" join edu_base_major ebm on ebm.resourceid = ers.majorid and ebm.isdeleted = 0                                    ");
		hql.append(" join hnjk_sys_unit hsu on hsu.resourceid = ers.branchschoolid and hsu.isdeleted = 0                                     ");
//		hql.append(" --join edu_teach_guiplan etg on etg.gradeid = ers.gradeid and etg.planid = ers.teachplanid and etg.isdeleted = 0       ");
		hql.append(" join edu_teach_plancourse etp on etp.resourceid = etm.plansourceid and etp.isdeleted = 0                               ");
		hql.append(" join edu_teach_coursestatus etc on etc.plancourseid = etm.plansourceid and etc.schoolids = ers.branchschoolid          ");
		hql.append(" and etc.isdeleted=0 and etc.openstatus = 'Y' and etc.isopen = 'Y'                                                      ");
		//当课程设置登分老师，却没有设置任课老师时，出现重复的情况，因此去掉edu_teach_courseteachercl  edu_base_edumanager的 left join ,改为 join     20170314 
		hql.append("  join edu_teach_courseteachercl etcl on etcl.coursestatusid = etc.resourceid and etcl.courseid = etm.courseid      ");
		hql.append("  and etcl.classesid = ers.classesid and etcl.isdeleted = 0                                                             ");
		hql.append("  join edu_base_edumanager ebe on ebe.sysuserid = etcl.teacherid and ebe.isdeleted =0                               ");
		hql.append(" join edu_base_course ebco on ebco.resourceid = etm.courseid and ebco.isdeleted = 0                                     ");
		if (condition.containsKey("courseId")) {
			 hql.append(" and ebco.resourceid=:courseId ");
			 param.add(condition.get("courseId"));
		}
		hql.append("  join edu_teach_examresults er on er.resourceid=etm.resultsid and er.isdeleted=0");
//		hql.append(" --join edu_teach_examsub ete on ete.resourceid = etm.nextexamsubid and ete.isdeleted = 0                               ");
		hql.append(" join (                                                                                                                 ");
		hql.append(" select sub.resourceid examsubid,sub.examtype,sub.term,eby.resourceid yearId,eby.firstyear from edu_teach_examsub sub   ");
		hql.append(" join edu_base_year eby on eby.resourceid = sub.yearid and eby.isdeleted = 0                                            ");
		hql.append(" where sub.isdeleted = 0                                                                                                ");
		if (condition.containsKey("yearId")) {//年度
			hql.append(" and sub.yearId=:yearId ");
			param.add(condition.get("yearId"));
		}
		if (condition.containsKey("term")) {//学期
			hql.append(" and sub.term=:term ");
			param.add(condition.get("term"));
		}
		if (condition.containsKey("examType")) {//考试类型
			hql.append(" and sub.examType=:examType ");
			param.add(condition.get("examType"));
		}
		hql.append(" )bb on bb.examsubid = etm.nextexamsubid and bb.examsubid!=er.examsubid                                                                                ");
		hql.append(" join (                                                                                                                 ");
		hql.append(" select ers2.classesid,count(ers2.studyno) as classesSum from edu_roll_studentinfo ers2                                 ");
		hql.append(" left join edu_teach_graduatedata gd2 on gd2.studentid=ers2.resourceid and gd2.isdeleted=0								");
		hql.append(" join edu_roll_classes erc2 on erc2.resourceid = ers2.classesid and ers2.isdeleted = 0									");
		hql.append(" where (ers2.studentstatus = '11' or ers2.studentstatus = '25' or (ers2.studentstatus='24' and gd2.isAllowSecGraduate='Y'))  and  ers2.isdeleted=0   ");
		hql.append(" group by ers2.classesid                                                                                                ");
		hql.append(" ) aa on aa.classesid = ers.classesid                                                                                   ");
		hql.append(" where etm.isdeleted =0                                                                                                 ");
		if (condition.containsKey("isPass")) {
			if("Y".equals(condition.get("isPass"))){
				hql.append(" and etm.isPass=:isPass ");
			}else {
				hql.append(" and (etm.isPass=:isPass or etm.isPass is null) ");
			}
			//hql.append(" and etm.isPass=:isPass ");
			param.add(condition.get("isPass"));
		}
		String examResultsTimes=null;
		if(CacheAppManager.getSysConfigurationByCode("examResultsTimes")!=null){
			examResultsTimes=CacheAppManager.getSysConfigurationByCode("examResultsTimes").getParamValue();
		}
        if(examResultsTimes!=null){
			int ksNum=  Integer.parseInt(examResultsTimes);
			ksNum+=1;
			hql.append(" and  (select count(*) from EDU_TEACH_EXAMRESULTS ete " +
        			"where ete.isdeleted = 0 and ete.COURSEID = etm.courseid " +
        			"and ete.studentid = etm.studentid and ete.checkstatus='4')<:ksNum "); 
        	param.add(ksNum);
		}
        //结补不及格课程门数
        String finalExamFailNum=null;
		if(CacheAppManager.getSysConfigurationByCode("finalExamFailNum")!=null){
			finalExamFailNum=CacheAppManager.getSysConfigurationByCode("finalExamFailNum").getParamValue();
		}
		if(finalExamFailNum!=null && condition.containsKey("examType") && "Q".equals(condition.get("examType"))){
			int failNum=  Integer.parseInt(finalExamFailNum);
			failNum += 1;
			hql.append(" and (select count(*) from edu_teach_makeuplist ml where ml.studentid=ers.resourceid and ml.isdeleted=0 and ml.ispass!='Y' and ml.nextexamsubid=bb.examsubid)<:failNum ");
			param.add(failNum);
		}
        hql.append(" and (ers.studentstatus = '11' or ers.studentstatus = '25' or (ers.studentstatus='24' and gd.isAllowSecGraduate='Y'))								");
		//hql.append(" order by erc.classesname,ebc.classicname,ers.teachingtype,ebco.coursename,etp.examclasstype,etc.term,                  ");
		//hql.append(" erc.classesmaster,erc.masterphone,etcl.teachername,ebe.mobile,ebe.officetel                                            ");
		//hql.append(" ) group by branchschoolid,gradeid,classicid,majorid,classesid,courseid,classesname,classicname,teachingtype,           ");
        hql.append(" order by classesname,coursename)");
        //hql.append(" coursename,examclasstype,term,classesmaster,masterphone,teachername,classesSum,coursecode,nextexamsubid,teacherphone   ");

		
//		hql.append("select nonExamJxd, nonExamNj ,nonExamClassic ,nonExamXxxs ,nonExamMajor, nonExamBj ,");
//		hql.append(" nonExamBkkc ,nonExamKcbm ,nonExamKhxs ,nonExamJyxq ,nonExamSkxq ,nonExamDfls ,");
//		hql.append(" nonExamDflsPhone, nonExamMaster, nonExamMasterPhone, nonExamRkls ,nonExamRklsPhone, count(stuid) nonExamXsrs, nonClassSum,");
//		hql.append("max(stuid) stuid from (");
//		hql.append(" select hsu.unitname nonExamJxd,ebg.gradename nonExamNj,ebcl.classicname nonExamClassic,"
//				+ "ers.teachingtype nonExamXxxs,ebm.majorname nonExamMajor,erc.classesname nonExamBj, ");
//		hql.append(" ebc.coursename nonExamBkkc,ebc.coursecode nonExamKcbm,etp.examclasstype nonExamKhxs,"
//				+ "etp.term nonExamJyxq,etc.term nonExamSkxq,etcl.teachername nonExamDfls,");
//		hql.append(" ete1.mobile nonExamDflsPhone,erc.classesmaster nonExamMaster,ete2.mobile nonExamMasterPhone,"
//				+ "etcl.lecturername nonExamRkls,decode(ete3.mobile,null,ete3.officetel,ete3.mobile) nonExamRklsPhone, dd.classSum nonClassSum, ");
//		hql.append(" wm_concat(substr(ers.studyno,LENGTH(ers.studyno)-2)) over (partition by hsu.unitname,ebg.gradename,ebcl.classicname,ers.teachingtype,ebm.majorname,erc.classesname,ebc.coursename,"
//				+ "ebc.coursecode,etp.examclasstype,etp.term,etc.term,etcl.teachername,ete1.mobile,erc.classesmaster,ete2.mobile,ett.teachername,ete3.mobile order by ers.studyno ) as stuid ");
//		hql.append(" from edu_teach_makeuplist etm ");
//		hql.append(" join edu_roll_studentinfo ers on ers.resourceid = etm.studentid and ers.isdeleted = 0 ");
//		hql.append(" join hnjk_sys_unit hsu on hsu.resourceid = ers.branchschoolid ");
//		//join hnjk_sys_unit hsu on hsu.resourceid = ers.branchschoolid and hsu.resourceid = '01000'
//		hql.append(" join edu_base_major ebm on ers.majorid = ebm.resourceid ");
//		hql.append(" join edu_base_course ebc on ebc.resourceid = etm.courseid and ebc.isdeleted = 0 ");
//		hql.append(" join edu_roll_classes erc on ers.classesid = erc.resourceid and erc.isdeleted = 0 ");
//		hql.append(" join edu_base_classic ebcl on ebcl.resourceid = ers.classicid ");
//		hql.append(" join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
//		//join edu_base_grade ebg on ebg.resourceid = ers.gradeid and ebg.gradename = '2014级'
//		hql.append(" join edu_teach_examresults ete on ete.resourceid = etm.resultsid and ete.isdeleted = 0 ");
//		hql.append(" join edu_teach_examsub etes on etes.resourceid = ete.examsubid  and etes.isdeleted = 0 ");
//		hql.append(" join edu_teach_plancourse etp on etp.resourceid = etm.plansourceid and etp.isdeleted = 0 ");
//		hql.append(" join edu_teach_coursestatus etc on etc.plancourseid = etp.resourceid and etc.isdeleted = 0 and etc.schoolids = hsu.resourceid  and etc.isopen = 'Y' ");
//		hql.append(" left join edu_teach_courseteachercl etcl on etcl.classesid = erc.resourceid and etcl.courseid = ebc.resourceid and etcl.coursestatusid = etc.resourceid ");//left join 才能查出复学学生
//		hql.append(" left join edu_teach_timetable ett on ett.classesid = erc.resourceid and ett.plancourseid = etp.resourceid and ett.courseid = ebc.resourceid ");
//		hql.append(" left join edu_base_edumanager ete1 on ete1.sysuserid = etcl.teacherid ");//登分老师
//		hql.append(" left join edu_base_edumanager ete2 on ete2.sysuserid = erc.classesmasterid ");//班主任
//		hql.append(" left join edu_base_edumanager ete3 on etcl.lecturerid = ete3.sysuserid ");//任课老师
//		hql.append(" join (select erc1.resourceid,count(ers1.resourceid) classSum from edu_roll_classes erc1  ");
//		hql.append("      join edu_roll_studentinfo ers1 on ers1.classesid = erc1.resourceid ");
//		hql.append("      where ers1.studentstatus='11' or ers1.studentstatus='21' ");
//		hql.append("      group by erc1.resourceid ");
//		hql.append("      ) dd on dd.resourceid = erc.resourceid ");
//		hql.append(" where ers.isdeleted = '0' and  (ers.studentstatus='11' or ers.studentstatus='21') and etm.isdeleted = 0 and etes.isdeleted = 0 ");		
	
		List<NonexaminationExportVoForGZDX> list = new ArrayList<NonexaminationExportVoForGZDX>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), param.toArray(), NonexaminationExportVoForGZDX.class);
			
		} catch (Exception e) {
			e.fillInStackTrace();
			return null;
		}
		return list;
	}
	@Override
	public List<ExportCourseAndTeacherVo> exportCourseAndTeacher(Map<String,Object> condition) throws ServiceException{
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("  select ebg.gradename gradeName,ebcl.classicname classicName,decode(cc.schooltype,'2','业余','函授') teachingType,ebm.majorname majorName,hsu.unitname unitName,   ");
		hql.append("  ebc.coursename courseName,cc.dictname examType,cc.examclasstype,(cc.facestudyhour+cc.experimentperiod) facePeriod,cc.experimentperiod,cc.classesresourceid,   ");
		hql.append("  cc.classesname,cc.stucount sumStu,cc.lecturername teacherName,ebe.mobile teacherPhone,cc.recordName,ebe2.mobile    ");
		hql.append("  from (   ");
		hql.append("  select dd.gradeid,dd.classicid,dd.schooltype,dd.majorid,dd.schoolids,dd.examclasstype,dd.facestudyhour,   ");
		hql.append("  dd.experimentperiod,erc.resourceid classesresourceid,erc.classesname,count(ers.resourceid) stucount,   ");
		hql.append("  etcl.lecturerid lecturerid,etcl.lecturername lecturername,etcl.teacherid recordid,etcl.teachername recordName,dd.courseid,dd.dictname   ");
		hql.append("  from edu_roll_classes erc    ");
		hql.append("  left join edu_roll_studentinfo ers on ers.classesid = erc.resourceid   ");
		hql.append("  join    ");
		hql.append("  (   ");
		hql.append("  select etc.resourceid coursestatusid,etp.courseid,etg.gradeid,etpl.classicid,etpl.schooltype,etpl.majorid,etc.schoolids,hsd.dictname,   ");
		hql.append("  etp.examClassType,etp.facestudyhour,etp.experimentperiod,etp.resourceid plancourseid   ");
		hql.append("  from edu_teach_coursestatus etc    ");
		hql.append("  join edu_teach_plancourse etp on etp.resourceid = etc.plancourseid and etp.isdeleted = 0   ");
		hql.append("  join edu_teach_guiplan etg on etg.resourceid = etc.guiplanid and etg.isdeleted = 0   ");
		hql.append("  join edu_teach_plan etpl on etpl.resourceid = etg.planid and etp.isdeleted = 0   ");
		hql.append("  join hnjk_sys_dict hsd on hsd.parentid = '5a402f033e00f79f013e0135f4120001' and hsd.dictvalue = etp.examclasstype  and hsd.isdeleted = 0 ");
		hql.append("  where etc.isopen = 'Y'  and etc.isdeleted = 0    ");
		if(condition.containsKey("unitid")){
			hql.append(" and etc.schoolids =:unitid  ");
			param.add(condition.get("unitid"));
		} else if (condition.containsKey("brSchoolid")) {
			hql.append(" and etc.schoolids =:brSchoolid  ");
			param.add(condition.get("brSchoolid"));
		}
		if (condition.containsKey("gradeid")) {
			hql.append(" and etg.gradeid =:gradeid  ");
			param.add(condition.get("gradeid"));
		}
		if (condition.containsKey("classicid")) {
			hql.append(" and etpl.classicid =:classicid  ");
			param.add(condition.get("classicid"));
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and etpl.schooltype =:teachingType  ");
			param.add(condition.get("teachingType"));
		}
		if(condition.containsKey("term")){
			hql.append(" and etc.term =:term  ");
			param.add(condition.get("term"));
		}

		hql.append("  group by etc.resourceid,etp.courseid,etg.gradeid,etpl.classicid,etpl.schooltype,etpl.majorid,etc.schoolids,   ");
		hql.append("  hsd.dictname,etp.examClassType,etp.facestudyhour,etp.experimentperiod,etp.resourceid   ");
		hql.append("  ) dd on dd.gradeid = erc.gradeid and dd.classicid = erc.classicid and dd.schooltype = erc.teachingtype and dd.majorid = erc.majorid and dd.schoolids = erc.orgunitid   ");
		hql.append("  left join edu_teach_timetable ett on ett.classesid = erc.resourceid and ett.plancourseid = dd.plancourseid and ett.courseid = dd.courseid  and ett.isdeleted = 0 ");
		hql.append("  left join edu_teach_courseteachercl etcl on etcl.coursestatusid = dd.coursestatusid and etcl.classesid = erc.resourceid and etcl.courseid = dd.courseid  and etcl.isdeleted = 0 ");
		hql.append("  where erc.isdeleted = 0 ");
		hql.append("  group by  dd.gradeid,dd.classicid,dd.schooltype,dd.majorid,dd.schoolids,dd.examclasstype,dd.facestudyhour,   ");
		hql.append("  dd.experimentperiod,erc.resourceid,erc.classesname,dd.courseid,ett.teacherid,ett.teachername,etcl.teacherid,etcl.teachername,etcl.lecturerid,etcl.lecturername,dd.dictname   ");
		hql.append("  ) cc    ");
		hql.append("  join edu_base_grade ebg on ebg.resourceid = cc.gradeid  and ebg.isdeleted = 0 ");
		hql.append("  join edu_base_classic ebcl on ebcl.resourceid = cc.classicid  and ebcl.isdeleted = 0 ");
		hql.append("  join edu_base_major ebm on ebm.resourceid = cc.majorid   and ebm.isdeleted = 0 ");
		hql.append("  join hnjk_sys_unit hsu on hsu.resourceid = cc.schoolids  and hsu.isdeleted = 0  ");
		hql.append("  join edu_base_course ebc on ebc.resourceid = cc.courseid  and ebc.isdeleted = 0 ");
		hql.append("  left join edu_base_edumanager ebe on ebe.sysuserid = cc.lecturerid  and ebe.isdeleted = 0 ");
		hql.append("  left join edu_base_edumanager ebe2 on ebe2.sysuserid = cc.recordid  and ebe2.isdeleted = 0 ");
		if (condition.containsKey("majorid")) {
			hql.append(" and ebm.resourceid =:majorid ");
			param.add(condition.get("majorid"));
		}
		if (condition.containsKey("classesid")) {
			hql.append(" and erc.resourceid =:classesid ");
			param.add(condition.get("classesid"));
		}
		if (condition.containsKey("courseId")) {
			hql.append(" and ebc.resourceid =:courseId ");
			param.add(condition.get("courseId"));
		}
		hql.append("  order by  ebg.gradename,ebcl.classicname,cc.schooltype,ebm.majorname,hsu.unitname  ");
		List<ExportCourseAndTeacherVo> list = new ArrayList<ExportCourseAndTeacherVo>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(),param.toArray(),ExportCourseAndTeacherVo.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
