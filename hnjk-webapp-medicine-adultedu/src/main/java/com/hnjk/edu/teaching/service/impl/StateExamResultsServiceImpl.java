package com.hnjk.edu.teaching.service.impl;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentCheckService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IStateExamResultsService;
import com.hnjk.edu.teaching.vo.DegreeCourseResultVo;
import com.hnjk.edu.teaching.vo.StateExamResultsVo;
import com.hnjk.edu.teaching.vo.StudentInfoAndDegreeCourseVO;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;

/**
 * 统考成绩ServiceImpl
 * <code>StateExamResultsServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-1-6 上午10:40:18
 * @see 
 * @version 1.0
 */
@Service("stateExamResultsService")
@Transactional
public class StateExamResultsServiceImpl extends BaseServiceImpl<StateExamResults> implements IStateExamResultsService {
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	
	@Autowired
	@Qualifier("studentCheckService")
	private IStudentCheckService studentCheckService;
	
	/**
	 * 通过JXL解析上传的统考成绩单
	 * @param excelFilePath
	 * @return
	 *//*
	public Map<String,Object> getStateExamResultsList(String excelFilePath){
		
		Map<String,StateExamResults>sep = new HashMap<String, StateExamResults>();    //已存在统考成绩Map
		Map<String,Object> returnMap    = new HashMap<String, Object>();              //返回解析结果Map
		Map<String,StudentInfo> sp  	= new HashMap<String, StudentInfo>();         //解析EXCEL文件中系统中存在的学籍Map
		Map<String,Course> cp 	    	= new HashMap<String, Course>();			  //统考课程Map
		Map<String,String> alp          = new HashMap<String, String>();			  //统考成绩单中允许的成绩分值项Map
		

		List<StateExamResultsVo> list   = new ArrayList<StateExamResultsVo>();        //返回允许导入的统考成绩Vo
		List<StateExamResults> serlist  = new ArrayList<StateExamResults>();		  //已存在统考成绩List
		List<StudentInfo> stuList   	= new ArrayList<StudentInfo>();				  //解析EXCEL文件中系统中存在的学籍list
		List<String> temp         		= new ArrayList<String>();					  //临时list
		
		Workbook rwb	 	      	    = null;
		InputStream is 		  	    	= null;
		try {
			is							= new FileInputStream(excelFilePath);
			rwb	 	  			  	    = Workbook.getWorkbook(is);
			Sheet sheet 		  	    = rwb.getSheet(0);
			int countRow          	    = sheet.getRows();
			int countColumn       	    = sheet.getColumns();
			
			//获取系统中的统考课
			List<Course> courses        = courseService.findByHql("from "+Course.class.getSimpleName() +" course where course.isDeleted=0 and course.isUniteExam='Y' ");
			for (Course c:courses) {
				cp.put(c.getCourseName().trim(), c);
			}
			//获取统考成绩单中允许的成绩分值项
			List<Dictionary> allowScore = CacheAppManager.getChildren("CodeAllowStateExamresultsImportSore");
			for (Dictionary dic:allowScore) {
				alp.put(ExStringUtils.trim(dic.getDictName()), ExStringUtils.trim(dic.getDictValue()));
			}
			//解析上传文件中的学号，存入临时List
			for (int i = 1; i < countRow; i++) {
				Cell studyNoCel	   	    = sheet.getCell(1,i); 
				String studyNoStr	    = ExStringUtils.trimToEmpty(studyNoCel.getContents());
				temp.add(studyNoStr);
			}
			String hql = " from "+StateExamResults.class.getSimpleName()+" s where s.isDeleted=:isDeleted and s.studentInfo.studyNo in(:stuNoList) and s.scoreType=:scoreType and s.course in(:courses)";
			returnMap.put("isDeleted", 0);
			returnMap.put("scoreType", "0");
			returnMap.put("courses", courses);
			//根据学号、统考课程获取学籍、已存在的统考课程成绩
			while(temp.size()>=999){
				returnMap.put("stuNoList", temp.subList(0, 999));
				List<StudentInfo> l1  	 = studentinfoservice.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("studyNo", temp.subList(0, 999)));
				List<StateExamResults> l2= this.findByHql(hql, returnMap);
				stuList.addAll(l1);
				serlist.addAll(l2);
				
				temp.subList(0, 999).clear();
			}
			if (temp.size()>0) {
				returnMap.put("stuNoList", temp);
				List<StudentInfo> l1   	 =  studentinfoservice.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("studyNo", temp));
				List<StateExamResults> l2= this.findByHql(hql, returnMap);
				
				stuList.addAll(l1);
				serlist.addAll(l2);
			}
			temp.clear();
			for (StudentInfo info : stuList) {
				sp.put(info.getStudyNo(), info);
			}
			for (StateExamResults ers :serlist ) {
				sep.put(ers.getStudentInfo().getStudyNo().trim()+ers.getCourse().getCourseName().trim(), ers);
			}
			
			//遍历EXCEL文件
			for (int i = 1; i < countRow; i++) {
				
				Cell certificateNoCel   = sheet.getCell(0,i); 
				Cell studyNoCel	   	    = sheet.getCell(1,i); 
				Cell nameCel 		    = sheet.getCell(2,i); 
				Cell brschoolCel 	    = sheet.getCell(4,i); 
				
				String certificateNoStr = certificateNoCel.getContents();
				String studyNoStr	    = ExStringUtils.trimToEmpty(studyNoCel.getContents());
				String nameStr		    = nameCel.getContents();
				String brschoolStr 		= brschoolCel.getContents();
				
				for (int j = 6; j <countColumn; j++) {
					
					Cell courseCel      = sheet.getCell(j,0); 
					Cell courseResultCel= sheet.getCell(j,i); 

					String courseName   = ExStringUtils.trimToEmpty(courseCel.getContents());
					String courseResult = courseResultCel.getContents();
					if (courseResult.length()>0) courseResult = courseResult.trim();
			
					Course stateCourse   = cp.get(courseName.trim()); 
					StudentInfo stuInfo  = sp.get(studyNoStr);
					boolean isAllowStatus= false;
					
					//if ("通过".equals(courseResult) || "免考".equals(courseResult) ||  "免修".equals(courseResult)|| "代修".equals(courseResult)) {
					//判断导入的成绩分值是否合法
					if (alp.containsKey(courseResult)) {
						isAllowStatus       = true;
					}
					//如果系统中已存在相应的统考课程成绩不允许导入
					if (sep.containsKey(studyNoStr+courseName)) {
						temp.add("学号：<font color='red'>"+studyNoStr+"</font> 姓名："+nameStr+"已存在《"+courseName+"》的成绩！ </br>");
						continue;
					//如果系统中找不到导入的学号对应的学籍，或导入的课程对应的统考课程不允许导入	
					}else if(null== stuInfo || null== stateCourse) {
						temp.add("学号：<font color='red'>"+studyNoStr+"</font> 姓名："+nameStr+" 课程：<font color='red'>"+courseName+"</font> 系统中找不到学生或相应的课程信息 </br>");
						continue;
					//填写在成绩分值不合法
					}else if(isAllowStatus==false&&ExStringUtils.isNotBlank(courseResult)){
						temp.add("学号：<font color='red'>"+studyNoStr+"</font> 姓名："+nameStr+" 课程：<font color='red'>"+courseName+"</font> 提供的成绩类型不合法 </br>");
						continue;
					}else if (ExStringUtils.isNotEmpty(courseResult) && isAllowStatus && null!= stuInfo &&  null!= stateCourse) {
						
						StateExamResultsVo vo   = new StateExamResultsVo();
						
						vo.setStudentInfo(stuInfo);
						vo.setExamCertificateNo(certificateNoStr);
						vo.setStudyNo(studyNoStr);
						vo.setName(nameStr);
						vo.setBrschool(brschoolStr);
						vo.setCourse(stateCourse);
						vo.setCourseResult(courseResult);
						
						list.add(vo);
					}
				}
			}

		} catch (Exception e) {
			logger.error("解析统考成绩单出错！{}"+e.fillInStackTrace());
			return returnMap;
		}finally{	
			try {
				if(null!=is) is.close();
			} catch (IOException e) {
				logger.error("",e.fillInStackTrace());
			}
			if(null!=rwb)rwb.close();
		} 
		returnMap.put("normalList",list);
		returnMap.put("errorList",temp);
		return returnMap;
	}
	
		/**
	 * 根据VO导入统考成绩

	public boolean addStateResultsByVo(StateExamResultsVo vo,Map<String,String> stateExamResults,Map<String,String> replaceCourse) throws ServiceException {
		
		try {
			Map<String,String> alp          = new HashMap<String, String>();			  //统考成绩单中允许的成绩分值项Map
			//获取统考成绩单中允许的成绩分值项
			List<Dictionary> allowScore 	= CacheAppManager.getChildren("CodeAllowStateExamresultsImportSore");
			for (Dictionary dic:allowScore) {
				alp.put(ExStringUtils.trim(dic.getDictName()), ExStringUtils.trim(dic.getDictValue()));
			}
			if (null!=vo.getCourse()&&stateExamResults.containsKey(vo.getCourse().getCourseName())) {

				//统考课程前缀
				String stateCourseRepPrefix = ExStringUtils.defaultIfEmpty(stateExamResults.get(vo.getCourse().getCourseName()), "");
				//统考课程对应的课程ID
				String replaceCourseId 		= replaceCourse.get(stateCourseRepPrefix);
				//保存导入的统考课程成绩
				StateExamResults results = new StateExamResults();
				results.setCourse(vo.getCourse());
				results.setPasstime(ExDateUtils.convertToDate(vo.getPassDate()));
				results.setStudentInfo(vo.getStudentInfo());
				if(alp.containsKey(vo.getCourseResult()))
				results.setScoreType(alp.get(vo.getCourseResult()));	
				this.save(results);
				
				//生成统考课程对应课程的成绩记录
				Set<TeachingPlanCourse>  tpcSet = vo.getStudentInfo().getTeachingPlan().getTeachingPlanCourses();
				for (TeachingPlanCourse tpc :tpcSet) {
					//如果统考成绩所属学生的教学计划中包涵了在统考课程代替课程内，则生成两条代替课程的统考成绩
					if (ExStringUtils.isNotBlank(replaceCourseId)&&replaceCourseId.indexOf(tpc.getCourse().getResourceid())>=0) {
						
						StateExamResults rc = new StateExamResults();
						rc.setCourse(courseService.load(tpc.getCourse().getResourceid()));
						rc.setPasstime(ExDateUtils.convertToDate(vo.getPassDate()));
						rc.setStudentInfo(vo.getStudentInfo());
						results.setScoreType(alp.get(vo.getCourseResult()));	
						
						this.save(rc);
					} 
				}
				
			}else {
				StateExamResults results = new StateExamResults();
				
				results.setCourse(vo.getCourse());
				results.setPasstime(ExDateUtils.convertToDate(vo.getPassDate()));
				results.setStudentInfo(vo.getStudentInfo());

				if(alp.containsKey(vo.getCourseResult()))
				results.setScoreType(alp.get(vo.getCourseResult()));	
				
				this.save(results);
			}
			

			return true;
			
		} catch (Exception e) {
			logger.error("根据VO导入统考成绩保存出错：{}"+e.fillInStackTrace());
			return false;
		}
	}
	*/

	/**
	 * 通过JXL解析上传的统考成绩单，写入临时表
	 * @param excelFilePath
	 * @return
	 */
	@Override
	public Map<String,String> analysisExamInationFile(String excelFilePath, String attid, Map<String,String> alp)throws ServiceException {
		Map<String,String> map = new HashMap<String, String>();
		List<Map<String,Object>> list   = new ArrayList<Map<String,Object>>();       
		StringBuffer returnMsg          = new StringBuffer(1024);				 
		int count = 0;
		Workbook rwb	 	      	    = null;
		InputStream is 		  	    	= null;
		try {
			is							= new FileInputStream(excelFilePath);
			rwb	 	  			  	    = Workbook.getWorkbook(is);
			Sheet sheet 		  	    = rwb.getSheet(0);
			int countRow          	    = sheet.getRows();
			count = countRow - 1; //一行是标题
			int countColumn       	    = sheet.getColumns();
			String reg = "[0-9]+.?[0-9]*";// 用来验证分数是否是数值

			String insertsql = " insert into edu_temp_statexamresults (certnum,studyno,coursename,results,attachid,memo,candidateNo,score) values(:certnum,:studyno,:coursename,:results,:attachid,:memo,:candidateNo,:score)";
			String degreeCourseName = null;
			
			//遍历EXCEL文件
			for (int i = 1; i < countRow; i++) {
				Cell candidateNoCel	   	    = sheet.getCell(0,i); //准考证号
				Cell studyNoCel	   	    = sheet.getCell(1,i); //学号
				Cell nameCel 		    = sheet.getCell(2,i); //姓名
				Cell scoreNum           = sheet.getCell(5,i); //成绩编号
//				Cell shcoolCel 		    = sheet.getCell(4,i); //教学点
				Cell certNumCel 		= sheet.getCell(3,i); //证件号码
				String candidateNo	    = ExStringUtils.trimToEmpty(candidateNoCel.getContents());
				String certNumStr	    = ExStringUtils.trimToEmpty(certNumCel.getContents());
				
				String studyNoStr	    = ExStringUtils.trimToEmpty(studyNoCel.getContents());
//				String schoolStr	    = ExStringUtils.trimToEmpty(shcoolCel.getContents());
				String nameStr		    = nameCel.getContents();
				String scoreNumStr      = ExStringUtils.trimToEmpty(scoreNum.getContents());
				// 判断合格证书编号 20180918 改为非必填
				/*if(ExStringUtils.isBlank(scoreNumStr)){
					returnMsg.append("[<font color='red'>学号：").append(studyNoStr).append("</font>],原因：成绩编号不能为空; </br>");
					continue;
				}*/
				
				// 获取学生基本信息及学位外语课程信息
//				List<StudentInfo> info = studentinfoservice.findByHql(" from "+StudentInfo.class.getSimpleName()+" where isdeleted = ? and studyNo = ? ", 0,studyNoStr);
				StudentInfoAndDegreeCourseVO info = studentinfoservice.findByStudyNo(studyNoStr);
				
				boolean flag = true;
				if(null != info){
					StringBuffer errmsg = new StringBuffer(200); 
					errmsg.append("[<font color='red'>学号：").append(studyNoStr).append("</font>],");
					degreeCourseName = info.getDegreeCourseName();
					if(ExStringUtils.isBlank(degreeCourseName)){
						errmsg.append("年级教学计划还没有设置学位外语语种</br>");
						returnMsg.append(errmsg);
						continue;
					}
					if(!nameStr.equals(info.getStudentName())){//姓名
						errmsg.append(" 名字填写错误 , ");
						flag = false;
					}
					if(!certNumStr.equals(info.getCertNum())){//证件号码
						errmsg.append(" 证件号码填写错误 ");
						flag = false;
					}
					/*if(!schoolStr.equals((null != stu.getBranchSchool() ? stu.getBranchSchool().getUnitName() : ""))){//教学点
						errmsg += " 教学点填写错误, ";
						flag = false;
					}*/
					if(!flag){
						returnMsg.append(errmsg).append("</br>");
						continue; //信息错误,跳入下一个
					}
				}else{
					returnMsg.append("[<font color='red'>学号：").append(studyNoStr).append("</font>],原因：平台没有找到这个学生; </br>");
					continue;
				}
				Double score = null;
				for (int j = 6; j <countColumn; j++) {
					Map<String, Object> m = new HashMap<String, Object>();
					Cell courseCel        = sheet.getCell(j,0);
					Cell courseResultCel  = sheet.getCell(j,i);

					String courseName     = ExStringUtils.trimToEmpty(courseCel.getContents());
					String courseResult   = ExStringUtils.trimToEmpty(courseResultCel.getContents());
					
					if(ExStringUtils.isBlank(courseResult)){
						continue;
					}else if (!courseResult.matches(reg) && !alp.containsKey(courseResult)) {
						returnMsg.append("[<font color='red'>学号：").append(studyNoStr).append("</font>] 姓名：")
						.append(nameStr).append(", 课程：").append(courseName).append(", 成绩： ").append(courseResult).append("，原因：成绩类型不对 </br>");
						continue;
					}
					
					// 判断导入的学位外语语种是否正确
					if(!degreeCourseName.equals(courseName)){
						returnMsg.append("[<font color='red'>学号：").append(studyNoStr).append("</font>] 姓名：")
						.append(nameStr).append(",原因：学位外语语种不对</br>");
						continue;
					}
					
					m.put("certnum", certNumStr);
					m.put("studyno", studyNoStr);
					m.put("coursename", courseName);
					m.put("attachid", attid);
					m.put("memo", scoreNumStr);
					m.put("candidateNo", candidateNo);
					
					if (!alp.containsKey(courseResult)) {
						score = Double.parseDouble(courseResult);
						courseResult = score.compareTo(60d)>=0?"0":"-1";
					} else {
						courseResult = alp.get(courseResult);
					}
					
					//判断导入的成绩分值是否合法()
					/*if (alp.containsKey(courseResult)) {
						//m.put("results",alp.get(courseResult));
					}else if(ExStringUtils.isNotBlank(courseResult)){
						returnMsg.append("[<font color='red'>学号："+studyNoStr+"</font>] 姓名："+nameStr+" 课程："+courseName+" 成绩类型： "+courseResult+"，原因：提供的成绩类型不合法 </br>");
						continue;
					}*/
					
					m.put("score",score);
					m.put("results",courseResult);
					list.add(m);
				}
			}
			if (!list.isEmpty()) {
				baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(insertsql,list.toArray(new Map[1]));
			}
		} catch (Exception e) {
			logger.error("解析统考成绩单出错！{}"+e.fillInStackTrace());
			throw new ServiceException("解析统考成绩单出错！"+e.fillInStackTrace());
		}finally{	
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("",e.fillInStackTrace());
			}
			if (null != rwb) {
				rwb.close();
			}
		}
		map.put("excelCount", (count < 0 ? 0 : count)+"");
		map.put("msg", returnMsg.toString());
		return map;
	}
	/**
	 * 批量插入统考记录
	 */
	@Override
	public Map<String,String> batchInputStateResults(Map<String, String> stateExamResults,Map<String, String> replaceCourse_1,Map<String, String> replaceCourse_2, String attId, String passDate)throws Exception {
		StringBuffer returnMsg = new StringBuffer(1024);
		Map<String,String> map = new HashMap<String, String>();
		int successNum1 = 0;
		int successNum2 = 0;
		map = replaceCourseInput(stateExamResults,replaceCourse_1,replaceCourse_2,attId,passDate);
		successNum1 = Integer.parseInt(map.get("successNum1"));
		returnMsg.append(map.get("msg"));
		map = stateCourseInput(stateExamResults,replaceCourse_1,attId,passDate);
		successNum2 = Integer.parseInt(map.get("successNum2"));
		returnMsg.append(map.get("msg"));
		map.clear();
		map.put("msg", returnMsg.toString());
		map.put("successNum", (successNum1+successNum2)+"");
		String clearSql = " delete from edu_temp_statexamresults t where t.attachid='"+attId+"'";
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(clearSql, null);
		return map;
	}
	/**
	 * 批量插入统考记录 - 处理有代替课程的统考记录
	 * @param stateExamResults
	 * @param replaceCourse_1
	 * @param replaceCourse_2
	 * @param attId
	 * @param passDate
	 * @return
	 * @throws Exception 
	 */
	private Map<String,String> replaceCourseInput(Map<String, String> stateExamResults,Map<String, String> replaceCourse_1,Map<String, String> replaceCourse_2, String attId, String passDate) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		StringBuffer returnMsg   = new StringBuffer();
		StringBuffer stateCourse = new StringBuffer();
		List<String> inParams    = new ArrayList<String>();
		int successNum = 0;
		for (String courseName : stateExamResults.keySet()) {
			String courseId 	 = stateExamResults.get(courseName);
			if (replaceCourse_1.containsKey(courseId)) {
				stateCourse.append(",'"+courseName+"'");
				inParams.add(courseName);
			}
		}
		
		//字典中设置了统考课程的代替课程表
		if (inParams.size()>0) {
			
			Map<String,Object> param 	    = new HashMap<String, Object>();
			List<Object[]> params   	    = new ArrayList<Object[]>();
			StringBuffer teachPlanCourseSql = new StringBuffer();
			Date passTime 				    = ExDateUtils.convertToDate(passDate);
			
			param.put("stateCourseName", inParams);
			param.put("attId", attId);
			param.put("isDeleted", 0);
			
			//插入统考成绩表SQL
			String insertSql 	     		= " insert into edu_teach_statexamresults (resourceid,studentid,courseid,scoretype,passtime,isidented,isdeleted,version,memo,candidateno,score) values (?,?,?,?,?,?,?,?,?,?,?)";
			//查询有统考代替课程的统考试成绩(导入的统考成绩系统中有相应的学籍、课程记录,并且系统中没有对应课程的统考成绩记录)

			//查询有统考代替课程的记录的教学计划课程
			teachPlanCourseSql.append(" select t.studyno,c.resourceid courseid,c.coursename from edu_temp_statexamresults t ");
			teachPlanCourseSql.append(" inner join edu_roll_studentinfo s on t.studyno = s.studyno and s.isdeleted = :isDeleted ");
			teachPlanCourseSql.append(" inner join edu_teach_plancourse pc on s.teachplanid = pc.planid and pc.isdeleted = :isDeleted ");
			teachPlanCourseSql.append(" inner join edu_base_course c  on c.resourceid = pc.courseid and c.isdeleted = :isDeleted ");
			teachPlanCourseSql.append(" where t.coursename in(:stateCourseName) ");
			teachPlanCourseSql.append("   and t.attachid = :attId");
			
			List<Map<String,Object>> nostateCoursesResults = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(getsql("no").toString(), param);
			List<Map<String,Object>> yesstateCoursesResults = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(getsql("yes").toString(), param); //已存在的数据
			if(null != yesstateCoursesResults && yesstateCoursesResults.size() > 0){
				for(Map<String,Object> m : yesstateCoursesResults){
					returnMsg.append("[<font color='red'>学号："+m.get("studyno")+"</font>],原因：课程"+m.get("coursename")+"已有统考鉴定成绩; </br>");
				}
			}
			
			List<Map<String,Object>> planCourses 		 = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(teachPlanCourseSql.toString(), param);
			Map<String,String> planCourseMap    		 = new HashMap<String, String>();
			//将查出的教学计划课程放入Map,以学号+课程ID为KEY
			for (Map<String,Object> m : planCourses) {
				planCourseMap.put(m.get("STUDYNO").toString()+m.get("COURSEID").toString(), m.get("COURSENAME").toString());
			}
			GUIDUtils.init();
			for (Map<String,Object> m : nostateCoursesResults) {
				String courseId   = m.get("COURSEID").toString();
				String stuNo      = m.get("STUDYNO").toString();
				String stuId      = m.get("STUDENTID").toString();
				String results    = m.get("RESULTS").toString();
				String classicName= m.get("CLASSICNAME").toString();
				String memo       = null==m.get("MEMO")?"":m.get("MEMO").toString();
				String candidateno= (String)m.get("CANDIDATENO");
				BigDecimal score= (BigDecimal)m.get("score");
				
				String replaceId_1= ExStringUtils.defaultIfEmpty(replaceCourse_1.get(courseId), "");
				String replaceId_2= ExStringUtils.defaultIfEmpty(replaceCourse_2.get(courseId), "");
				String []rpids_1  = replaceId_1.split(",");
				String []rpids_2  = replaceId_2.split(",");
				
				if (null!=rpids_1&&rpids_1.length>0) {
					for (int i  = 0; i < rpids_1.length; i++) {
						if(planCourseMap.containsKey(stuNo.trim()+rpids_1[i].trim())){
							List<Object> objList = new ArrayList<Object>();
							objList.add(GUIDUtils.buildMd5GUID(false));//resourceid
							objList.add(stuId);	                       //studentid
							objList.add(rpids_1[i]);				   //courseid
							objList.add(results);					   //scoretype
							objList.add(passTime);					   //passtime
							objList.add(Constants.BOOLEAN_NO);         //isidented
							objList.add(0);         				   //isdeleted
							objList.add(0);         				   //version
							objList.add(memo);                         //memo
							objList.add(candidateno);                         //candidateno
							objList.add(score!=null?score.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString():null);         				   //score
							
							params.add(objList.toArray());
						}
					}
				}
				if (null!=rpids_2&&rpids_2.length>0&&"高中起点本科".equals(classicName)) {
					for (int i  = 0; i < rpids_2.length; i++) {
						if(planCourseMap.containsKey(stuNo.trim()+rpids_2[i].trim())){
							List<Object> objList = new ArrayList<Object>();
							objList.add(GUIDUtils.buildMd5GUID(false));//resourceid
							objList.add(stuId);	                       //studentid
							objList.add(rpids_2[i]);				   //courseid
							objList.add(results);					   //scoretype
							objList.add(passTime);					   //passtime
							objList.add(Constants.BOOLEAN_NO);         //isidented
							objList.add(0);         				   //isdeleted
							objList.add(0);         				   //version
							objList.add(memo);                         //memo
							objList.add(candidateno);                         //candidateno
							objList.add(score!=null?score.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString():null);         				   //score
							
							params.add(objList.toArray());
						}
					}
				}
			}
			if (params.size()>0) {
				baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(insertSql, params);
				//returnMsg.append("* 有替代记录的课程<font color='red'>"+replaceCourse_1.size()+"</font>门:<font color='red'>"+stateCourse.substring(1)+"</font></br>");
				//returnMsg.append("* 生成统考替代课程成绩记录:<font color='red'>"+params.size()+"</font>条，在相应的统考课程成绩认定界面中完成认定后，生成的成绩即生效！</br>");
				successNum = params.size();
			}
			
		}
		map.put("successNum1",successNum+"");//成功数量
		map.put("msg", returnMsg+"");
		return map;
	}
	
	private StringBuffer getsql(String type){
		StringBuffer statResultsSql = new StringBuffer();
		statResultsSql.append(" select s.resourceid studentid,cc.classicname,t.certnum,t.studyno,c.resourceid courseid,c.coursename,t.results,t.attachid,t.memo,t.candidateno,t.score ");
		statResultsSql.append(" from edu_temp_statexamresults t ");
		statResultsSql.append(" inner join edu_base_course c      on t.coursename = c.coursename and c.isuniteexam='Y' and c.isdeleted = :isDeleted ");
		
		statResultsSql.append(" inner join edu_base_student s2 on t.certnum = s2.CERTNUM and s2.isdeleted = :isDeleted  ");
		
		statResultsSql.append(" inner join edu_roll_studentinfo s on s2.RESOURCEID = s.studentbaseinfoid and t.studyno=s.studyno and s.isdeleted = :isDeleted  ");
		
//		statResultsSql.append(" inner join edu_roll_studentinfo s on t.studyno = s.studyno and s.isdeleted = :isDeleted  ");
		statResultsSql.append(" inner join edu_base_classic cc    on cc.resourceid = s.classicid    ");
		if("no".equals(type)){
			statResultsSql.append(" and not exists ( ");
		}else if("yes".equals(type)){
			statResultsSql.append(" and exists ( ");	
		}
		statResultsSql.append("		 select t1.resourceid from edu_teach_statexamresults t1,edu_roll_studentinfo s1 ,edu_base_course c1 ");
		statResultsSql.append(" 	  where t1.isdeleted = :isDeleted  ");
		statResultsSql.append("         and s1.isdeleted = :isDeleted and c1.isdeleted = :isDeleted and c1.isuniteexam = 'Y' and t1.studentid = s1.resourceid  ");
		statResultsSql.append("         and s1.studyno   = s.studyno and t1.courseid  = c1.resourceid and c1.coursename= c.coursename ) ");
		statResultsSql.append(" where t.coursename in(:stateCourseName) ");
		statResultsSql.append(" and t.attachid   = :attId");
		return statResultsSql;
	}
	
	/**
	 * 批量插入统考记录 - 处理所有导入的统考记录
	 * @param stateExamResults
	 * @param replaceCourse
	 * @param attId
	 * @param passDate
	 * @return
	 * @throws Exception 
	 */
	private Map<String,String> stateCourseInput(Map<String, String> stateExamResults,Map<String, String> replaceCourse, String attId, String passDate) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		Date passTime 				    = ExDateUtils.convertToDate(passDate);
		Map<String,Object> param 	    = new HashMap<String, Object>();
		List<Object[]> params   	    = new ArrayList<Object[]>();
		StringBuffer returnMsg  	    = new StringBuffer(1024);
		int successNum = 0; 
		param.put("attId", attId);
		param.put("isDeleted",0);
		
		//插入统考成绩表SQL
		String insertSql = " insert into edu_teach_statexamresults (resourceid,studentid,courseid,scoretype,passtime,isidented,isdeleted,version,memo,candidateno,score) values (?,?,?,?,?,?,?,?,?,?,?)";
		
		List<Map<String,Object>> nostateCoursesResults = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(getStatResultsSql("no").toString(), param);
		List<Map<String,Object>> yesstateCoursesResults = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(getStatResultsSql("yes").toString(), param);
		if(null != yesstateCoursesResults && yesstateCoursesResults.size() > 0){
			for(Map<String,Object> m : yesstateCoursesResults){
				returnMsg.append("[<font color='red'>学号：").append(m.get("studyno")).append("</font>],原因：课程").append(m.get("coursename")).append("已有统考鉴定成绩; </br>");
			}
		}
		GUIDUtils.init();
		for (Map<String,Object> m : nostateCoursesResults) {
			String courseId = m.get("COURSEID").toString();
//			String stuNo    = m.get("STUDYNO").toString();
			String stuId    = m.get("STUDENTID").toString();
			String results  = m.get("RESULTS").toString();
			String memo     = null==m.get("MEMO")?"":m.get("MEMO").toString();
			String candidateNo  = (String)m.get("CANDIDATENO");
			BigDecimal score  = (BigDecimal)m.get("SCORE");
			
			List<Object> objList = new ArrayList<Object>();
			objList.add(GUIDUtils.buildMd5GUID(false));//resourceid
			objList.add(stuId);	                       //studentid
			objList.add(courseId);					   //courseid
			objList.add(results);					   //scoretype
			objList.add(passTime);					   //passtime
			objList.add(Constants.BOOLEAN_NO);         //isidented
			objList.add(0);         				   //isdeleted
			objList.add(0);         				   //version
			objList.add(memo);         				   //memo
			objList.add(candidateNo);         				   //candidateNo
			objList.add(score!=null?score.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString():null);         				   //score
			
			params.add(objList.toArray());

		}
		if (params.size()>0) {
			baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(insertSql, params);
			//returnMsg.append("* 导入统考课程成绩:<font color='red'>"+params.size()+"</font>条，在相应的统考课程成绩认定界面可查看、认定成绩,成绩认定后即生效！（如有未导入数据，该学生已导入过统考课程成绩或则该数据填写错误）</br>");
			successNum = params.size();
		}
		map.put("successNum2",successNum+"");//成功数量
		map.put("msg", returnMsg.toString());
		return map;
	}
	
	private StringBuffer getStatResultsSql(String type){
		StringBuffer statResultsSql     = new StringBuffer();
		statResultsSql.append(" select s.resourceid studentid,t.certnum,t.studyno,c.resourceid courseid,c.coursename,t.results,t.attachid,t.memo,t.candidateno,t.score ");
		statResultsSql.append(" from edu_temp_statexamresults t ");
//		statResultsSql.append(" inner join edu_base_course c  on t.coursename = c.coursename and c.isuniteexam='Y' and c.isdeleted = :isDeleted ");
		statResultsSql.append(" inner join edu_base_course c  on t.coursename = c.coursename and c.isdegreeunitexam='Y' and c.isdeleted = :isDeleted ");
		
		statResultsSql.append(" inner join edu_base_student s2 on t.certnum = s2.CERTNUM and s2.isdeleted = :isDeleted  ");
		
		statResultsSql.append(" inner join edu_roll_studentinfo s on s2.RESOURCEID = s.studentbaseinfoid and t.studyno=s.studyno  and s.isdeleted = :isDeleted  ");
//		statResultsSql.append(" inner join edu_roll_studentinfo s on t.studyno = s.studyno and s.isdeleted = :isDeleted  ");
		if("no".equals(type)){
			statResultsSql.append(" and not exists ( ");
		}else if("yes".equals(type)){
			statResultsSql.append(" and exists ( ");	
		}
		statResultsSql.append("		 select * from edu_teach_statexamresults t1,edu_roll_studentinfo s1 ,edu_base_course c1 ");
		statResultsSql.append(" 	  where t1.isdeleted = :isDeleted  ");
//		statResultsSql.append("         and s1.isdeleted = :isDeleted and c1.isdeleted = :isDeleted and c1.isuniteexam = 'Y' and t1.studentid = s1.resourceid  ");
		statResultsSql.append("         and s1.isdeleted = :isDeleted and c1.isdeleted = :isDeleted and c1.isdegreeunitexam = 'Y' and t1.studentid = s1.resourceid  ");
		statResultsSql.append("         and s1.studyno   = s.studyno and t1.courseid  = c1.resourceid and c1.coursename= c.coursename ) ");
		statResultsSql.append(" and t.attachid   = :attId");
		return statResultsSql;
	}
	
	/**
	 * 根据条件查询统考成绩
	 */
	@Override
	public Page findStateResultsByCondition(Map<String,Object> condition , Page objPage) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append("from "+StateExamResults.class.getSimpleName()+" results where results.isDeleted=0");
		if (condition.containsKey("stuName")) {
			hql.append(" and results.studentInfo.studentName like '%" + condition.get("stuName") + "%'");
		}
		if (condition.containsKey("scoreType")){
			hql.append(" and results.scoreType =:scoreType");}
		if (condition.containsKey("passtime")){
			hql.append(" and results.passtime <= to_date('"+condition.get("passtime")+"','yyyy-MM-dd')");}
		if (condition.containsKey("courseId")){
			hql.append(" and results.course.resourceid =:courseId");}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and results.studentInfo.branchSchool.resourceid=:branchSchool");
		}
		if (condition.containsKey("gradeid")) {
			hql.append(" and results.studentInfo.grade.resourceid=:gradeid");
		}
		if (condition.containsKey("classic")) {
			hql.append(" and results.studentInfo.classic.resourceid=:classic");
		}
		if (condition.containsKey("major")) {
			hql.append(" and results.studentInfo.major.resourceid=:major");
		}
		if (condition.containsKey("classes")) {
			hql.append(" and results.studentInfo.classes.resourceid=:classes");
		}
		if (condition.containsKey("studyNo")) {
			hql.append(" and results.studentInfo.studyNo=:studyNo");
		}
		if (condition.containsKey("studentStatus")) {
			hql.append(" and results.studentInfo.studentStatus=:studentStatus");
		}
		if (condition.containsKey("learningStyle")) {
			hql.append(" and results.studentInfo.learningStyle=:learningStyle");
		}
		if (condition.containsKey("isIdented")) {
			hql.append(" and results.isIdented=:isIdented");
		}
		
		hql.append(" order by results."+objPage.getOrderBy()+ " "+objPage.getOrder());
			
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), condition);
	}

	@Override
	public List<StateExamResults> findStateExamResultsByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(500);
			hql.append("from "+StateExamResults.class.getSimpleName()+" results where results.isDeleted=0");
		
		if (condition.containsKey("stuName")){
			hql.append(" and results.studentInfo.studentName like '%"+condition.get("stuName")+"%'");}
		
		if (condition.containsKey("studyNo")){
			hql.append(" and results.studentInfo.studyNo=:studyNo");
			values.put("studyNo", condition.get("studyNo"));
		}
		if (condition.containsKey("studentStatus")){
			hql.append(" and results.studentInfo.studentStatus=:studentStatus");
			values.put("studentStatus", condition.get("studentStatus"));
		}
			
		if (condition.containsKey("studentId")){
			hql.append(" and results.studentInfo.resourceid=:studentId");
			values.put("studentId", condition.get("studentId"));
		}
			
		if (condition.containsKey("scoreType")){
			hql.append(" and results.scoreType =:scoreType");
			values.put("scoreType", condition.get("scoreType"));
		} 
			
		if (condition.containsKey("passtime")){
			hql.append(" and results.passtime <= to_date('"+condition.get("passtime")+"','yyyy-MM-dd')");}
		
		if (condition.containsKey("courseId")){
			hql.append(" and results.course.resourceid =:courseId");
			values.put("courseId", condition.get("courseId"));
		} 
			
		
		if (condition.containsKey("branchSchool")){
			hql.append(" and results.studentInfo.branchSchool.resourceid=:branchSchool");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		
		if (condition.containsKey("gradeid")){
			hql.append(" and results.studentInfo.grade.resourceid=:gradeid");
			values.put("gradeid", condition.get("gradeid"));
		}
			
		
		if (condition.containsKey("classic")){
			hql.append(" and results.studentInfo.classic.resourceid=:classic");
			values.put("classic", condition.get("classic"));
		}
			
		
		if (condition.containsKey("major")){
			hql.append(" and results.studentInfo.major.resourceid=:major");
			values.put("major", condition.get("major"));
		}
			
		
		if (condition.containsKey("learningStyle")){
			hql.append(" and results.studentInfo.learningStyle=:learningStyle");
			values.put("learningStyle", condition.get("learningStyle"));
		}
			
		
		if (condition.containsKey("isIdented")) {
			hql.append(" and results.isIdented=:isIdented");
			values.put("isIdented", condition.get("isIdented"));
		}
		if (condition.containsKey("uniteExam")) {
			hql.append(" and results.course.isUniteExam=:uniteExam");
			values.put("uniteExam", condition.get("uniteExam"));
		}
		
		return (List<StateExamResults>) exGeneralHibernateDao.findByHql(hql.toString(), values);
		
	}
	/**
	 * 保存统考成绩认定
	 * @param stateExamResultsIds
	 * @param courseId
	 * @throws Exception
	 */
	@Override
	public void maintainExamInationCourseSave(List<String> stateExamResultsIds, String courseId)throws Exception {

		Map<String,String> stateCourse      = new HashMap<String, String>(); 
		Map<String,String> replaceCourse    = new HashMap<String, String>(); 
		
		Course course  			 			=  courseService.get(courseId);
		//获取统考课程   可以替代哪些课程
		List<Dictionary> dict  			    = CacheAppManager.getChildren("CodeStateExamResultsReplaceCourse");
		//从数据字典中获取统考课程 课程名，用于跟导入的成绩单中做匹配(字典中排序为-1的选项为 统考课程)
		for (Dictionary d:dict ) {
			if (-1==d.getShowOrder()) {
				String  stateCourseName     = d.getDictName();   //统考课程名       
				String  stateCourseId       = d.getDictValue();  //统考课程ID    
				stateCourse.put(stateCourseName, stateCourseId);
			}else {      
				String  replaceCourseId     = d.getDictValue();  //统考课程代替的课程ID 
				String [] rci               = replaceCourseId.split("_");
				String oldReplaceCourseId   = ExStringUtils.defaultIfEmpty(replaceCourse.get(rci[0]), "");
				String stateCourseId        = rci[0];
				replaceCourse.put(stateCourseId, oldReplaceCourseId+","+rci[1]);
			}
		}
		
		List<StateExamResults> list1    	= this.findByCriteria(Restrictions.in("resourceid",stateExamResultsIds.toArray()));
		String studentIds               	= "";
		String replaceCourseId         	    = ExStringUtils.defaultIfEmpty(replaceCourse.get(course.getResourceid()), "");
		
		for (StateExamResults rs : list1) {
			 studentIds          	   	   +=  ","+rs.getStudentInfo().getResourceid();
			 rs.setIsIdented(Constants.BOOLEAN_YES);
		}
		this.batchSaveOrUpdate(list1);
		
		//如果当前认定的是有代替课程的统考课程，则将代替的课程也操行认定操作
		if (stateCourse.containsKey(course.getCourseName())) {
			List<StateExamResults> list2    = this.findByCriteria(Restrictions.in("studentInfo.resourceid",studentIds.split(",")),Restrictions.in("course.resourceid",replaceCourseId.split(",")));
			for (StateExamResults rs : list2) {
				 rs.setIsIdented(Constants.BOOLEAN_YES);
			}
			this.batchSaveOrUpdate(list2);
		}
		
		List<StudentCheck> checks      = new ArrayList<StudentCheck>();
		for (StateExamResults rs:list1) {
			StudentCheck check = new StudentCheck();
			check.setStudentId(rs.getStudentInfo().getResourceid());
			checks.add(check);
		}
		studentCheckService.batchSaveOrUpdate(checks);
	}

	@Override
	public List<StateExamResultsVo> findStateExamResultsVoByHql(Map<String,Object> condition) throws ServiceException {
		StringBuffer sql  = new StringBuffer();
		Map<String,Object> params = new HashMap<String, Object>();
		sql.append(" select ebc.courseName,ets.passtime,ets.scoretype,ers.studentstatus,hsu.unitname,ebg.gradename,ebcl.classicname,ebm.majorname,cl.classesname classname,ers.studentname,ers.studyno,ebs.gender,ers.teachingtype,ets.memo ");
		sql.append(" from edu_teach_statexamresults ets ");
		sql.append(" left join edu_roll_studentinfo ers on ers.resourceid = ets.studentid ");
		sql.append(" left join edu_base_course ebc on ebc.resourceid = ets.courseid ");
		sql.append(" left join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
		sql.append(" left join edu_base_classic ebcl on ebcl.resourceid = ers.classicid ");
		sql.append(" left join edu_base_major ebm on ebm.resourceid = ers.majorid ");
		sql.append(" left join edu_roll_classes cl on cl.resourceid=ers.classesid");
		sql.append(" left join hnjk_sys_unit hsu on hsu.resourceid = ers.branchschoolid ");
		sql.append(" left join edu_base_student ebs on ebs.resourceid = ers.studentbaseinfoid ");
		sql.append(" where ets.isdeleted = 0  ");		
		if (condition.containsKey("gradeid")) {
			sql.append(" and ers.gradeid=:gradeid");
			params.put("gradeid", condition.get("gradeid"));
		}
		if (condition.containsKey("classic")) {
			sql.append(" and ers.classicid=:classic");
			params.put("classic", condition.get("classic"));
		}
		if (condition.containsKey("learningStyle")) {
			sql.append(" and ers.teachingtype=:learningStyle");
			params.put("learningStyle", condition.get("learningStyle"));
		}
		if (condition.containsKey("major")) {
			sql.append(" and ers.majorid=:major");
			params.put("major", condition.get("major"));
		}
		if (condition.containsKey("classes")) {
			sql.append(" and ers.classesid=:classes");
			params.put("classes", condition.get("classes"));
		}
		if (condition.containsKey("branchSchool")) {
			sql.append(" and ers.branchschoolid=:branchSchool");
			params.put("branchSchool", condition.get("branchSchool"));
		}
		sql.append(" order by hsu.unitname,ebg.gradename,ebcl.classicname,ers.teachingtype,ebm.majorname,ers.classesid,ers.studyno ");
		List<StateExamResultsVo> list;
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StateExamResultsVo.class, params);
			return list;
		} catch (Exception e) {
			logger.error("查询下载统考成绩单列表出错");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<StateExamResultsVo> findStateExamResultsMismatching(Map<String,Object> condition) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ets.resourceid stateExamid,ers.studyNo,ers.studentName,ebc.courseName from edu_teach_statexamresults ets ");
		sql.append(" join edu_roll_studentinfo ers on ers.resourceid = ets.studentid ");
		
		sql.append(" join edu_teach_guiplan etg on etg.planid = ers.teachplanid and etg.gradeid=ers.gradeid and etg.degreeforeignlanguage<>ets.courseid ");
		sql.append(" join edu_base_course ebc on ebc.resourceid = etg.degreeforeignlanguage ");
		sql.append(" where 1=1 ");		
		if(condition.containsKey("studentid")){
			sql.append(" and ets.studentid in (:studentid)");
		}
		sql.append(" order by ers.studyNo");
		List<StateExamResultsVo> list;
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StateExamResultsVo.class, condition);
			return list;
		} catch (Exception e) {
			logger.error("认定学位外语，查询导入的学位外语与教学计划中的学位外语是否一致出错");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据学籍、年级和教学计划获取学位外语课程成绩信息
	 * @param studentId
	 * @param gradeId
	 * @param planId
	 * @return
	 */
	@Override
	public StateExamResults findDegreeForeignLanguage(String studentId,String gradeId,String planId) {
		StateExamResults stateExamResults =  null;
		 try {
			Map<String, Object> params = new HashMap<String, Object>(5);
			 params.put("studentId", studentId);
			 params.put("gradeId", gradeId);
			 params.put("planId", planId);
			 
			StringBuffer sql = new StringBuffer(500);
			sql.append("select sr.*,c.courseName from edu_teach_statexamresults sr inner join edu_base_course c on c.resourceid=sr.courseid ")
			.append("where sr.isdeleted=0 and sr.scoretype='0' and sr.isidented='Y' and sr.studentid=:studentId ")
			.append("and sr.courseid=(select max(gp.degreeforeignlanguage) from edu_teach_guiplan gp where gp.isdeleted=0 and gp.ispublished='Y' and gp.gradeid=:gradeId and gp.planid=:planId) ")
			.append("order by sr.passtime desc ");
			
			List<StateExamResults> stateExamResultsList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StateExamResults.class, params);
			if(ExCollectionUtils.isNotEmpty(stateExamResultsList)){
				stateExamResults = stateExamResultsList.get(0);
			}
		} catch (Exception e) {
			logger.error("根据学籍、年级和教学计划获取学位外语课程成绩信息出错", e);
		}
		
		return stateExamResults;
	}
	
	/**
	 * 处理导入学位外语成绩
	 * @param degreeCourseResultList
	 * @param passDate
	 * @return
	 */
	@Override
	public Map<String, Object> handleDegreeCourseResultImport(List<DegreeCourseResultVo> degreeCourseResultList, String passDate) {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		// 失败记录
		List<DegreeCourseResultVo> failList = new ArrayList<DegreeCourseResultVo>(50);
		try {
			do {
				if (ExCollectionUtils.isEmpty(degreeCourseResultList)) {
					statusCode = 300;
					msg.append("没有学位外语成绩数据");
					continue;
				}
				List<String> existList = new ArrayList<String>(500);
				Map<String, Object> m = null;
				List<Map<String,Object>> list   = new ArrayList<Map<String,Object>>(500);       
				
				String reg = "[0-9]+.?[0-9]*";// 用来验证分数是否是数值
				String insertResultSql = " insert into edu_teach_statexamresults (resourceid,studentid,courseid,scoretype,passtime,isidented,isdeleted,version,memo,candidateno,score) "
						+ "values(:resourceid,:studentid,:courseid,:scoretype,:passtime,:isidented,:isdeleted,:version,:memo,:candidateNo,:score)";
				String degreeCourseName = null;
				String studyNo = null;
				String scoreStr = null;
				
				Map<String,String> alp = new HashMap<String, String>(5);
				//获取统考成绩单中允许的成绩分值项
				List<Dictionary> allowScore = CacheAppManager.getChildren("CodeAllowStateExamresultsImportSore");
				for (Dictionary dic:allowScore) {
					alp.put(ExStringUtils.trim(dic.getDictName()), ExStringUtils.trim(dic.getDictValue()));
				}
				// 处理学位外语成绩
				String existKey = null;
				for(DegreeCourseResultVo dcr : degreeCourseResultList){
					studyNo = dcr.getStudyNo();
					existKey = studyNo+dcr.getDegreeCourseName();
					if(existList.contains(existKey)){
						msg.append("[<font color='red'>学号：").append(studyNo).append("</font>],课程：").append(dcr.getDegreeCourseName()).append(",原因：有重复的记录; </br>");
						dcr.setErrorMsg("有重复的记录");
						failList.add(dcr);
						continue;
					}
					existList.add(existKey);
					// 判断合格证书号
					if(ExStringUtils.isBlank(dcr.getCertificateNo())){
						msg.append("[<font color='red'>学号：").append(studyNo).append("</font>],原因：合格证书号不能为空; </br>");
						dcr.setErrorMsg("合格证书号不能为空");
						failList.add(dcr);
						continue;
					}
					// 获取学生信息及学位外语课程
					StudentInfoAndDegreeCourseVO info = studentinfoservice.findByStudyNo(studyNo);
					if(info == null){
						msg.append("[<font color='red'>学号：").append(studyNo).append("</font>],原因：不存在该学生</br>");
						dcr.setErrorMsg("不存在该学生");
						failList.add(dcr);
						continue;
					}
					// 存在学生的逻辑
					StringBuffer errmsg = new StringBuffer(200); 
					errmsg.append("[<font color='red'>学号：").append(studyNo).append("</font>],");
					degreeCourseName = info.getDegreeCourseName();
					// 判断年级教学计划是否设置语种
					if(ExStringUtils.isBlank(degreeCourseName)){
						errmsg.append("没有关联教学计划或年级教学计划还没有设置学位外语语种</br>");
						msg.append(errmsg);
						dcr.setErrorMsg("没有关联教学计划或年级教学计划还没有设置学位外语语种");
						failList.add(dcr);
						continue;
					}
					// 判断名字是否正确
					if(!dcr.getStudentName().equals(info.getStudentName())){
						errmsg.append("名字填写错误</br>");
						msg.append(errmsg);
						dcr.setErrorMsg("名字填写错误");
						failList.add(dcr);
						continue;
					}
					// 判断证件号码是否正确
					if(!dcr.getCertNum().equals(info.getCertNum())){
						errmsg.append("证件号码填写错误</br>");
						msg.append(errmsg);
						dcr.setErrorMsg("证件号码填写错误");
						failList.add(dcr);
						continue;
					}
					
					// 判断导入的学位外语语种是否正确
					if(!degreeCourseName.equals(dcr.getDegreeCourseName())){
						msg.append("[<font color='red'>学号：").append(studyNo).append("</font>],原因：学位外语课程名称不对</br>");
						dcr.setErrorMsg("学位外语课程名称不对");
						failList.add(dcr);
						continue;
					}
					// 成绩处理
					scoreStr = dcr.getScore();
					if (!dcr.getScore().matches(reg) && !alp.containsKey(scoreStr)) {
						msg.append("[<font color='red'>学号：").append(studyNo).append("</font>], 成绩： ").append(scoreStr).append("，原因：成绩填写有误 </br>");
						dcr.setErrorMsg("成绩填写有误");
						failList.add(dcr);
						continue;
					}
					// 判断该学生的学位外语是否有成绩
					if(ExStringUtils.isNotBlank(info.getResultId())){
						msg.append("[<font color='red'>学号：").append(studyNo).append("</font>],原因：已有学位外语成绩 </br>");
						dcr.setErrorMsg("已有学位外语成绩 ");
						failList.add(dcr);
						continue;
					}
					BigDecimal score = null;
					if (!alp.containsKey(scoreStr)) {
						score = BigDecimal.valueOf(Double.parseDouble(scoreStr)).setScale(1, BigDecimal.ROUND_HALF_UP);
						scoreStr = score.compareTo(BigDecimal.valueOf(60d))>=0?"0":"-1";
					} else {
						scoreStr = alp.get(scoreStr);
					}
					
					m = new HashMap<String, Object>(11);
					GUIDUtils.init();
					m.put("resourceid", GUIDUtils.buildMd5GUID(false));
					m.put("studentid", info.getStudentId());
					m.put("courseid", info.getCourseId());
					m.put("scoretype", scoreStr);
					m.put("passtime",ExDateUtils.convertToDate(passDate));
					m.put("isidented",Constants.BOOLEAN_NO);
					m.put("isdeleted",0);
					m.put("version",0);
					m.put("memo", dcr.getCertificateNo());
					m.put("candidateNo", dcr.getCandidateNo());
					m.put("score",score!=null?score.toPlainString():null);
					// 加入列表
					list.add(m);
				}
				
				if (!list.isEmpty()) {
					baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(insertResultSql,list.toArray(new Map[1]));
				}
				
			} while (false);
			
			if(msg.length() >0) {
				statusCode = 400;
			}
		} catch (Exception e) {
			logger.error("处理导入学位外语成绩出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("处理导入学位外语成绩失败！");
		} finally {
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
			retrunMap.put("failList", failList);
		}
		
		return retrunMap;
	}
	
	

}
