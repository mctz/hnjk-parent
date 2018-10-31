package com.hnjk.edu.roll.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IGraduationStatJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 毕业资格审核.
 * <code>GraduationQualifServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-5-16 下午04:58:15
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduationQualifService")
public class GraduationQualifServiceImpl extends BaseServiceImpl<StudentInfo> implements IGraduationQualifService {

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("graduationStatJDBCService")
	private IGraduationStatJDBCService graduationStatJDBCService;

	/* 
	 * 查找符合毕业条件的学生
	 * (non-Javadoc)
	 * @see com.hnjk.edu.roll.service.IGraduationQualifService#findGraduateionQualifByCondition(java.util.Map, com.hnjk.core.rao.dao.helper.Page)
	 */
	@Override
	public Page findGraduateionQualifByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException {
		//已修学分满足教学计划中的最低毕业学分要求
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentInfo.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		//目前允许使用的学籍状态为 在学 过期 延期 毕业四个状态 其中毕业资格筛选需要过滤掉过期和毕业两种状态
		if(condition.containsKey("isGraduateQualifer")){
			hql += " and studentStatus in ('11','21','25') ";
			/*hql += " and ( studentStatus = :studentStatus1";
			values.put("studentStatus1", "11");//在学状态
			hql += " or studentStatus = :studentStatus2) ";
			values.put("studentStatus2", "21");//延期状态*/		
			}
		if(condition.containsKey("isDegree")){
			hql += " and (studentStatus = :studentStatus3";
			values.put("studentStatus3", "16");
			hql += " or studentStatus = :studentStatus4)";
			values.put("studentStatus4", "22");//准毕业状态
		}
		//学分要求  但目前 学生学籍中的已修学分是空白的。
		if(condition.containsKey("isDegree")||condition.containsKey("isGraduateQualifer")){
		hql += " and finishedCreditHour >= teachingPlan.minResult ";//teachingPlan.minResult是最低毕业学分
		hql += " and enterAuditStatus ='Y' ";
		//是否已经完全通过必修课
		hql += " and finishedNecessCreditHour='1' ";
		//是否达到限选课学分要求
		hql += " and finishedOptionalCourseNum >= DECODE(teachingPlan.optionalCourseNum, null, 0,teachingPlan.optionalCourseNum) ";
		}
		//年级要求  and to_number(replace(substr(b.eduyear,0,3),'年',''))+to_number(c.firstyear+(d.term-1)*0.5+0.5)
		if(condition.containsKey("currentYearInfo")){
			hql += " and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5+0.5))<=(0.5+to_number(:currentYearInfo))" ;
			values.put("currentYearInfo", condition.get("currentYearInfo"));
		}
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("name")){
			hql += " and studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("gradeid")){
			hql += " and grade.resourceid= :gradeid ";
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("studyNo")){
			hql += " and studyNo= :studyNo ";
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			hql += " and studentStatus = :stuStatus ";
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("certNum")){//身份证号
			hql += " and  studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("isApplyGraduate")){
			hql += " and ( isApplyGraduate ='Y' ) ";
		}
		
		if(condition.containsKey("isUndergraduate")){
			hql += " and ( classic.resourceid='EB43B13C75A9F518E030007F0100743D' or classic.resourceid='EB43B13C75AAF518E030007F0100743D' or classic.resourceid='ff8080810b60ac12010b654bec2c1d56' )"; //高升本 专升本 EMBA硕士
		}
		if(condition.containsKey("graduateDate")){
			hql += " and ( graduateDate =to_date(:graduateDate,'yyyy-MM-dd'))";
			values.put("graduateDate", condition.get("graduateDate"));
		}
		
		return studentInfoService.findByHql(objPage, hql, values);
	}
	
	
	/*	
	private String GraduationFilterByObj(GraduationQualif graduationQualif){		
		//学员是否完成教学计划所修课程
//		String studentId=graduationQualif.getResourceid();
		String studentId=""; //学籍ID
		int planCourseCount=graduationQualif.getPlanCourseCount();//教学计划所修课程数
		int minResult =graduationQualif.getMinResult();//教学计划最低学分
		int applyPaperMinResult=graduationQualif.getApplyPaperMinResult();//教学计划毕业论文最低学分
		int thisesScore=graduationQualif.getThesisScore();
		int courseCount=(graduationQualif.getNoexamCount()+graduationQualif.getStatExamCount()+graduationQualif.getNoexamCount());
		if(planCourseCount!=0&&graduationQualif.getScore()>=planCourseCount){//学分不低于教学计划的最低学分（每门课程必须合格才有学分所以只做总学分的判断）
			studentId=graduationQualif.getResourceid();
		}
		if(graduationQualif.getIsOrderSubject()&&thisesScore<applyPaperMinResult){//有毕业论文而不合格的，不在毕业范围内
			studentId="";
		}
		return studentId;
	}
*/
	/*
	public StringBuffer queryGraduationStr() {
		// TODO Auto-generated method stub
		StringBuffer buffer=new StringBuffer();
		List list= new ArrayList();

		buffer
		.append(" select resourceid, planid,minResult,applyPaperMinResult,optionalCourseNum,planCourseCount,noexamCount,stuCoursecount,statExamCount,isdeleted ,teachPlanId ,isOrderSubject from ( ")
		.append(" select stu.resourceid,plan.resourceid as planid,plan.minResult,plan.applyPaperMinResult,plan.optionalCourseNum,pl.planCourseCount, ")
		.append(" nvl(noexam.noexamCount,0) as noexamCount,nvl(exam.stuCoursecount,0) as stuCoursecount,nvl(state.statExamCount,0)as statExamCount,stu.isdeleted,stu.teachPlanId,stu.isOrderSubject  ")
		.append(" from edu_roll_studentInfo stu")
		.append(" left join edu_base_student student on student.resourceid=stu.studentbaseinfoid ")
		.append(" left join ")
		.append(" (select count(studentid)as stuCoursecount,studentId from edu_teach_examresults group by studentid having 1=1) exam on exam.studentid=stu.resourceid ")
		.append(" left join edu_teach_plan plan on  plan.resourceid=stu.teachPlanId ")
		.append(" left join ")
		.append(" (select count(planId)as planCourseCount,planId from edu_teach_plancourse group by planId ) pl on pl.planId=plan.resourceid  ")
		.append(" left join ")
		.append(" (select count(studentid) as statExamCount,studentid from edu_teach_statexamresults group by studentid) state on state.studentid=exam.studentid ")
		.append(" left join (select count(studentid)as noexamCount ,studentid from edu_teach_noexam group by studentid) noexam on noexam.studentid=stu.resourceid ) ")
		.append(" where resourceid not in ")
		.append(" ( select t.studentid from edu_teach_graduatedata t ) ")
		.append(" and resourceid not in ")
		.append(" ( select t.studentid from edu_teach_nograduate t ) ")
		.append(" and isdeleted =0 ");
		try {
			
		
			Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
			org.hibernate.SQLQuery query = session.createSQLQuery(buffer.toString()) ;
			list=query.list();
			
			buffer.delete(0, buffer.length());
			buffer.append(" ");
			for(int i=0;i<list.size();i++){
				Object[] obj=(Object[])list.get(i);
				if(obj!=null){
					GraduationQualif graduationQualif=new GraduationQualif(); 
					graduationQualif.setResourceid(obj[0].toString());//学籍ID
					graduationQualif.setPlanid(obj[1]!=null?obj[1].toString():"");//学员对应的教学计划ID
					graduationQualif.setMinResult(obj[2]!=null?Integer.parseInt(obj[2].toString()):0); //最低学分
					graduationQualif.setApplyPaperMinResult(obj[3]!=null?Integer.parseInt(obj[3].toString()):0);//最低论文学分
					graduationQualif.setOptionalCourseNum(obj[4]!=null?Integer.parseInt(obj[4].toString()):0);// 选修修课数
					graduationQualif.setPlanCourseCount(obj[5]!=null?Integer.parseInt(obj[5].toString()):0);//教学计划课数
					graduationQualif.setNoexamCount(obj[6]!=null?Integer.parseInt(obj[6].toString()):0);//学员的免修课程数
					graduationQualif.setStuCoursecount(obj[7]!=null?Integer.parseInt(obj[7].toString()):0);//学员的参加考试数
					graduationQualif.setStatExamCount(obj[8]!=null?Integer.parseInt(obj[8].toString()):0);//学员统考通过课程数
					graduationQualif.setIsdeleted(obj[9]!=null?obj[9].toString():"");						//是否删除
					graduationQualif.setTeachPlanId(obj[10]!=null?obj[10].toString():"");				//教学计划ID
					int score[]=getStudenScore(graduationQualif.getResourceid().trim(),graduationQualif.getTeachPlanId().trim());
					graduationQualif.setScore(score[0]);//学员已获取的学分
					graduationQualif.setThesisScore(score[1]);//毕业论文学分
					graduationQualif.setIsOrderSubject(obj[10]!=null&&"Y".equals(obj[10].toString().trim())?true:false);//是否预约毕业论文
//				System.out.println("学籍ID "+graduationQualif.getResourceid()+" 教学计划ID "+graduationQualif.getTeachPlanId()+"学员已获取的学分 "+graduationQualif.getScore()+"通过的课程数 "+(graduationQualif.getNoexamCount()+graduationQualif.getStatExamCount()+graduationQualif.getNoexamCount())+"");
					
					String stuid=GraduationFilterByObj(graduationQualif);
					if(ExStringUtils.isNotEmpty(stuid)){
						buffer.append("'").append(stuid).append("'").append(",");
					}
				}
			}
			//测试数据
//			buffer.append("'").append("402880e629d5c6720129d5c9d7d20003").append("'").append(",");
			buffer.delete(buffer.length()-1,buffer.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
	*/
	/*
		public int[] getStudenScore(String stuId,String plandId){
			List<Map> list=queryExamResults();
			int[] thesisScore={0,0};//下标为0的是课程总学分，下标为1的是毕业论文学分
			int studentScore=0;
			for(Map map:list){
				//通过考试的计划课程ID获取已获取的学分
				String studentId=map.get("studentId").toString().trim();
				String pid=map.get("planid").toString().trim();
				//课程学分
				double creditHour=ExStringUtils.isNotEmpty(map.get("creditHour").toString())?Double.parseDouble(map.get("creditHour").toString()):0;
				String courseType=map.get("courseType").toString().trim(); //课程类型
				//课程综合成绩
				double score=ExStringUtils.isNotEmpty(map.get("integratedScore").toString())?Double.parseDouble(map.get("integratedScore").toString()):0;
				if(stuId.equals(studentId)&&ExStringUtils.isNotEmpty(plandId)&&plandId.equals(pid)){
					//如果该课程类型是毕业论文就获取它的毕业论文分数
					if("thesis".equals(courseType)){
						thesisScore[1]=(int)score;
					}else{
						studentScore+=(score>=60)?creditHour:0;
					}
				}
			}
			thesisScore[0]=studentScore;
			return thesisScore;
		}
	
	*/
	
	/*
	private List<Map> queryExamResults(){
		StringBuffer buffer=new StringBuffer();
		List list= new ArrayList();
		List<Map> reslut=new ArrayList<Map>();
		buffer.append("select t.studentId,t.courseid,t.majorcourseid,t.integratedScore,pc.planid,pc.creditHour," +
				"noexam.courseid as isnoexam,stat.courseid as isstateame,pc.courseType from edu_teach_examresults t" +
				" left join edu_teach_plancourse pc on pc.resourceid=t.majorcourseid " +
				" left join edu_teach_noexam noexam on noexam.studentId= t.studentId and noexam.isdeleted=0" +
				" left join edu_teach_statexamresults stat on stat.studentid=t.studentId and noexam.isdeleted=0 order by planid");
		
 		try {
			Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
			org.hibernate.SQLQuery query=session.createSQLQuery(buffer.toString());
			list=query.list();
			for(int i=0;i<list.size();i++){
				Object[] obj=(Object[])list.get(i);
				Map map=new HashMap();
				map.put("studentId", obj[0]);  //学籍号
				map.put("courseid", obj[1]);   //基础信息课程ID
				map.put("majorcourseid", obj[2]);//原教学计划课程ID
				//在当前集合中取同一门课程的最高分数 (根据学籍ID,教学计划ID,计划课程ID比较同一个课程的最高分)
//				String integratedScore=obj[3]!=null? ScoreEncryptionDecryptionUtil.getInstance().decrypt(obj[0].toString(),obj[3].toString()):"0";
				String integratedScore=getMaxScoreForCourse(obj[0]!=null?obj[0].toString():null,obj[4]!=null?obj[4].toString():null,obj[2]!=null?obj[2].toString():null,list);
				map.put("integratedScore",integratedScore);//分数
				map.put("planid", obj[4]); //教学计划ID
				map.put("creditHour", obj[5]); //课程学分
				map.put("isnoexam", obj[6]); //该课程是否免考
				map.put("isstateame", obj[7]); //该课程是否统考
				map.put("courseType", obj[8]);//课程类型（值  名称）：  44 必修课 |22选修课 |55通识课 |66 实践环节| 33 限选课 |thesis 毕业论文
				reslut.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslut;
	}
	*/
	
	/*
	private String getMaxScoreForCourse(String stuid,String planId,String majorcourseid, List list){
		double maxScore=0;
		if(stuid==null||planId==null||majorcourseid==null)
		{return "";}
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[])list.get(i);
			
			if(stuid.equals(obj[0].toString())&&planId.equals(obj[4].toString())&&majorcourseid.equals(obj[2].toString())){
				double score=Double.parseDouble(obj[3]!=null? ScoreEncryptionDecryptionUtil.getInstance().decrypt(obj[0].toString(),obj[3].toString()):"0");
				
				maxScore=score>maxScore?score:maxScore;
			}
		}
		String maxScoreStr=Double.toString(maxScore);
		return maxScoreStr;
	}
	
	*/
	
	@Override
	public String getGradeToMajor(String gradeId, String majorId, String id, String name, String click){ //年级-专业
		StringBuffer majorStr = new StringBuffer();
		majorStr.append("<select  class='flexselect' id='"+id+"' name='"+name+"' style='width:120px;' onchange='"+click+"'>");
		try {
			//处理年级-专业级联查询
			Map<String,Object> param = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(gradeId)) {
				param.put("gradeId", gradeId);
			}
			List<Map<String,Object>> listGrade = graduationStatJDBCService.cascadeGradeToMajor(param);
			if(null != listGrade && listGrade.size()>0){
				//majorStr.append("<option value=''>共"+listGrade.size()+"个专业,请选择</option>");
				majorStr.append("<option value=''></option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(majorId) ? "" : majorId).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected='selected' >"+map.get("majorName")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("majorName")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>请选择专业</option>");
			}
		} catch (Exception e) {
			logger.error("年级专业级联出错;");
			e.printStackTrace();
		}
		majorStr.append("</select>");
		return majorStr.toString();
	}
	@Override
	public String getGradeToMajor1(String gradeId, String majorId, String id, String name, String click, String classic){ //年级-专业
		StringBuffer majorStr = new StringBuffer();
		majorStr.append("<select  class='flexselect' id='"+id+"' name='"+name+"' style='width:120px;' onchange='"+click+"'>");
		try {
			//处理年级-专业级联查询
			Map<String,Object> param = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(gradeId)) {
				param.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotBlank(classic)) {
				param.put("classic", classic);
			}
			List<Map<String,Object>> listGrade = graduationStatJDBCService.cascadeGradeToMajor1(param);
			if(null != listGrade && listGrade.size()>0){
				majorStr.append("<option value=''></option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(majorId) ? "" : majorId).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected >"+map.get("MAJORCODE")+"-"+map.get("majorName")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("MAJORCODE")+"-"+map.get("majorName")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>请选择专业</option>");
			}
		} catch (Exception e) {
			logger.error("年级专业级联出错;");
			e.printStackTrace();
		}
		majorStr.append("</select>");
		return majorStr.toString();
	}
	@Override
	public String getMajors(String gradeId, String majorId, String classic){ //年级-专业
		StringBuffer majorStr = new StringBuffer();
		try {
			//处理年级-专业级联查询
			Map<String,Object> param = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(gradeId)) {
				param.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotBlank(classic)) {
				param.put("classic", classic);
			}
			List<Map<String,Object>> listGrade = graduationStatJDBCService.cascadeGradeToMajor1(param);
			if(null != listGrade && listGrade.size()>0){
				//majorStr.append("<option value=''>"+listGrade.size()+"个专业</option>");
				majorStr.append("<option value=''></option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(majorId) ? "" : majorId).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected >"+map.get("majorCode")+"-"+map.get("majorName")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("majorCode")+"-"+map.get("majorName")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>请选择专业</option>");
			}
		} catch (Exception e) {
			logger.error("年级专业级联出错;");
			e.printStackTrace();
		}
		return majorStr.toString();
	}
	
	@Override
	public String getGradeToMajorToClasses(String gradeId, String majorId, String classesId, String brschoolId, String id, String name){ //年级-专业-班级-教学站
		StringBuffer majorStr = new StringBuffer();
		majorStr.append("<select  class='flexselect' id='"+id+"' name='"+name+"' style='width:120px;'>");
		try {
			//处理年级-专业-班级级联查询
			Map<String,Object> param = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(gradeId)) {
				param.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotBlank(majorId)) {
				param.put("majorId", majorId);
			}
			if(ExStringUtils.isNotBlank(brschoolId)) {
				param.put("brschoolId", brschoolId);
			}
			List<Map<String,Object>> listGrade = graduationStatJDBCService.cascadeMajorToClasses(param);
			if(null != listGrade && listGrade.size()>0){
				//majorStr.append("<option value=''>共"+listGrade.size()+"个班级,请选择</option>");
				majorStr.append("<option value=''></option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(classesId) ? "" : classesId).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected='selected' >"+map.get("classname")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("classname")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>请选择班级</option>");
			}
		} catch (Exception e) {
			logger.error("年级专业班级级联出错;");
			e.printStackTrace();
		}
		majorStr.append("</select>");
		return majorStr.toString();
	}
	
	//学院2016修改
	@Override
	public String getGradeToMajorToCourse(String examsub, String unid, String courseid, String isMk){ //年级-专业-班级-教学站
		StringBuffer majorStr = new StringBuffer();
		majorStr.append("<select  class='flexselect' id='courseId' name='courseId' style='width:120px;'>");
		try {
			Map<String,Object> param = new HashMap<String, Object>();
			User user = SpringSecurityHelper.getCurrentUser();
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//校外学习中心人员
				if(ExStringUtils.isNotBlank(unid)) {
					param.put("unid", unid);
				}
			}
		
			//处理年级-专业-班级级联查询
			
			if(ExStringUtils.isNotBlank(examsub)) {
				param.put("examsub", examsub);
			}
			if(ExStringUtils.isNotBlank(isMk)) {
				param.put("isMk", isMk);
			}
			
			List<Map<String,Object>> listGrade =graduationStatJDBCService.cascadeMajorToCourse(param);
			if(null != listGrade && listGrade.size()>0){
				majorStr.append("<option value=''>共"+listGrade.size()+"个课程,请选择</option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(courseid) ? "" : courseid).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected='selected' >"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>请选择课程</option>");
			}
		} catch (Exception e) {
			logger.error("批次课程级联出错;");
			e.printStackTrace();
		}
		majorStr.append("</select>");
		return majorStr.toString();
	}

	
	@Override
	public String getGradeToMajorToClasses1(String gradeId, String majorId, String classesId, String brschoolId, String id, String name, String classic){ //年级-专业-班级-教学站
		StringBuffer majorStr = new StringBuffer();
		majorStr.append("<select  class='flexselect' id='"+id+"' name='"+name+"' style='width:120px;'>");
		try {
			//处理年级-专业-班级级联查询
			Map<String,Object> param = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(gradeId)) {
				param.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotBlank(majorId)) {
				param.put("majorId", majorId);
			}
			if(ExStringUtils.isNotBlank(brschoolId)) {
				param.put("brschoolId", brschoolId);
			}
			if(ExStringUtils.isNotBlank(classic)) {
				param.put("classic", classic);
			}
			User user=SpringSecurityHelper.getCurrentUser();
			if(!"administrator".equals(user.getUsername()) &&!"UNIT_ROOT".equals(user.getOrgUnit().getUnitCode())
					&&!"DEPT_TEACHING_CENTRE".equals(user.getOrgUnit().getUnitCode())){
				param.put("brschoolId", user.getUnitId());
			}
			List<Map<String,Object>> listGrade = graduationStatJDBCService.cascadeMajorToClasses1(param);
			if(null != listGrade && listGrade.size()>0){
				//majorStr.append("<option value=''>共"+listGrade.size()+"个班级,请选择</option>");
				majorStr.append("<option value=''></option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(classesId) ? "" : classesId).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected >"+map.get("classname")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("classname")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>请选择班级</option>");
			}
		} catch (Exception e) {
			logger.error("年级专业班级级联出错;");
			e.printStackTrace();
		}
		majorStr.append("</select>");
		return majorStr.toString();
	}
	@Override
	public String getClasses(String gradeId, String majorId, String classesId, String brschoolId, String classic){ //年级-专业-班级-教学站
		StringBuffer majorStr = new StringBuffer();
		try {
			//处理年级-专业-班级级联查询
			Map<String,Object> param = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(gradeId)) {
				param.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotBlank(majorId)) {
				param.put("majorId", majorId);
			}
			if(ExStringUtils.isNotBlank(brschoolId)) {
				param.put("brschoolId", brschoolId);
			}
			if(ExStringUtils.isNotBlank(classic)) {
				param.put("classic", classic);
			}
			/*User user=SpringSecurityHelper.getCurrentUser();
			if(!user.getUsername().equals("administrator")){
				param.put("brschoolId", user.getUnitId());
			}*/
			List<Map<String,Object>> listGrade = graduationStatJDBCService.cascadeMajorToClasses1(param);
			if(null != listGrade && listGrade.size()>0){
				//majorStr.append("<option value=''>"+listGrade.size()+"个班级</option>");
				majorStr.append("<option value=''></option>");
				for (Map<String,Object> map : listGrade) {
					if((ExStringUtils.isBlank(classesId) ? "" : classesId).equals(map.get("resourceid"))){
						majorStr.append("<option value='"+map.get("resourceid")+"' selected >"+map.get("classname")+"</option>");
					}else{
						majorStr.append("<option value='"+map.get("resourceid")+"'>"+map.get("classname")+"</option>");
					}
				}
			}else{
				majorStr.append("<option value=''>无班级</option>");
			}
		} catch (Exception e) {
			logger.error("年级专业班级级联出错;");
			e.printStackTrace();
		}
		return majorStr.toString();
	}
}
