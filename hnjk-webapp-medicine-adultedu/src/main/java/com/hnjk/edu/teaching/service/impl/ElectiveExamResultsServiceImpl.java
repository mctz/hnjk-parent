package com.hnjk.edu.teaching.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import lombok.Cleanup;
import net.sf.cglib.transform.impl.AddDelegateTransformer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.cache.InitAppDataServiceImpl;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ElectiveExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IElectiveExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.security.service.IOrgUnitService;

@Transactional
@Service("electiveExamResultsService")
public class ElectiveExamResultsServiceImpl extends BaseServiceImpl<ElectiveExamResults> implements IElectiveExamResultsService{
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;

	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	private StringBuffer returnMsg = new StringBuffer();
	private Map<String,Object> validateMap;
	
	private int  count = 0;		//总条数
	private int successCount = 0;// 成功的条数
	private int startRow =3;	//第一条数据所在行数
	List<Integer> ordinalList = new ArrayList<Integer>();
	List<String> seatNumber = new ArrayList<String>();
	List<Integer> yearList = new ArrayList<Integer>();
	List<Integer> termList = new ArrayList<Integer>();
	List<String> studyNoList = new ArrayList<String>();

	
	@Override
	@Transactional(readOnly=true)
	public Page findPageByCondition(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+ElectiveExamResults.class.getSimpleName()+" c where c.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		getHqlByCondition(hql, values, condition);
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}else {
			hql.append(" order by studentInfo.branchSchool.unitCode,studentInfo.classes.classCode desc,examSub.batchName desc,course.courseCode,studentInfo.studyNo");
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public Map<String, Object> importElectiveExamResults(String filePath) {
		// TODO Auto-generated method stub
		Map<String,Object> returnMap = new HashMap<String, Object>();
		try {
			@Cleanup InputStream inputStream = new FileInputStream(filePath);
			@Cleanup Workbook workbook = WorkbookFactory.create(inputStream);
			successCount=0;
			returnMsg.setLength(0);
			studyNoList.clear();
			String brSchool = "admin";
			User user = SpringSecurityHelper.getCurrentUser();
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				brSchool = user.getOrgUnit().getResourceid();
			}else {
				brSchool = "admin";
			}

			do{
				Sheet sheet = workbook.getSheetAt(0);
				// Read the Row
				Row firstRow = sheet.getRow(0);
				if (firstRow == null) {
					returnMsg.append("<font color='red'></font>读取的文件内容为空！");
					continue;
				}
				if(ExStringUtils.isEmpty(ExStringUtils.toString(firstRow.getCell(0)))){
					returnMsg.append("<font color='red'>[第1行]</font>标题不能为空！");
					continue;
				}
				String titlt = ExStringUtils.toString(firstRow.getCell(0)).trim();
				String year = titlt.substring(0, 4);
				String term = titlt.substring(titlt.length()-3,titlt.length()-2);
				if(!ExStringUtils.isNumeric(year, 2) && !ExStringUtils.isNumeric(term, 2)){
					returnMsg.append("<font color='red'>[第1行]</font>标题格式错误，请按照正确格式填写！");
					continue;
				}
				String hql = " from "+ExamSub.class.getSimpleName()+" where yearInfo.firstYear=? and term=? and examType='N'";
				ExamSub examSub = examSubService.findUnique(hql, Long.parseLong(year),term);
				if(examSub==null){
					returnMsg.append("<font color='red'></font>请添加"+year+"年第"+term+"学期考试批次！");
					continue;
				}
				count = sheet.getLastRowNum()-2;
				for (int rowNum=startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						String unitNameStr = ExStringUtils.toString(row.getCell(0));
						String studyNoStr = ExStringUtils.toString(row.getCell(1));
						String studentNameStr = ExStringUtils.toString(row.getCell(2));
						String courseNameStr = ExStringUtils.toString(row.getCell(3));
						String usuallyScore = ExStringUtils.toString(row.getCell(4));
						String writtenScore = ExStringUtils.toString(row.getCell(5));
						String integratedScore = ExStringUtils.toString(row.getCell(6));

						validateMap = validateImportDatas(rowNum,studyNoStr,studentNameStr, unitNameStr,usuallyScore,writtenScore,integratedScore,examSub,courseNameStr,brSchool);

						boolean isPass = (Boolean)validateMap.get("isPass");
						if(!isPass){
							returnMsg.append((String)validateMap.get("msg") + "</br>");
							continue;
						}

						ElectiveExamResults electiveExamResults = (ElectiveExamResults) validateMap.get("electiveExamResults");
						saveOrUpdate(electiveExamResults);
						successCount++;
					}
				}
			}while(false);
			returnMap.put("totalCount", (count < 0 ? 0 : count));
			returnMap.put("successCount", successCount);
			returnMap.put("message", returnMsg.toString());
		} catch (FileNotFoundException e) {
			logger.error("导入成绩文件不存在", e);
			returnMap.put("message", "导入失败");
		} catch (IOException e) {
			logger.error("读取成绩文件出错", e);
			returnMap.put("message", "导入失败");
		} catch (Exception e) {
			logger.error("解析成绩文件出错", e);
			returnMap.put("message", "导入失败");
		} 
		return returnMap;
	}
	
	
	/**
	 * 根据条件获取选修课成绩信息列表
	 * 
	 * 	@param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ElectiveExamResults> findListByCondition(Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer(" from "+ElectiveExamResults.class.getSimpleName()+" c where c.isDeleted = 0 ");
		Map<String,Object> values =  new HashMap<String, Object>();
		getHqlByCondition(hql,values,condition);
		hql.append(" order by studentInfo.branchSchool.unitCode,studentInfo.classes.classCode desc,examSub.batchName desc,course.courseCode,studentInfo.studyNo");
		//hql.append(" order by studentInfo.branchSchool,studentInfo.grade,studentInfo.classic,studentInfo.teachingType,studentInfo.major desc");
		return  (List<ElectiveExamResults>) exGeneralHibernateDao.findByHql(hql.toString(), values);
	}
	
	private void getHqlByCondition(StringBuffer hql, Map<String, Object> values, Map<String, Object> condition) {
		if(condition.containsKey("resourceid")){
			String reids = condition.get("resourceid").toString();
			hql.append(" and c.resourceid in ('"+reids.replace(",", "','")+"') ");
		}
		if(condition.containsKey("examSubId")){
			hql.append(" and c.examSub.resourceid=:examSubId ");
			values.put("examSubId", condition.get("examSubId"));
		}
		if(condition.containsKey("brSchoolId")){
			hql.append(" and c.studentInfo.branchSchool.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("brSchoolId"));
		}
		if(condition.containsKey("gradeId")){
			hql.append(" and c.studentInfo.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			hql.append(" and c.studentInfo.classic.resourceid=:classicId ");
			values.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("teachingType")){
			hql.append(" and c.studentInfo.teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and c.studentInfo.major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("classesId")){
			hql.append(" and c.studentInfo.classes.resourceid=:classesId ");
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("courseId")){
			hql.append(" and c.course.resourceid=:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("studentId")){
			hql.append(" and c.studentInfo.resourceid =:studentId ");
			values.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("studyNo")){
			hql.append(" and c.studentInfo.studyNo =:studyNo ");
			values.put("studyNo", condition.get("studyNo"));
		}	
		if(condition.containsKey("studentName")){
			hql.append(" and c.studentInfo.studentName like :studentName ");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("stuStatus")){
			hql.append(" and c.studentInfo.studentStatus =:stuStatus ");
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("checkStatus") && ExStringUtils.isNotBlank(condition.get("checkStatus"))){
			if("-1".equals(condition.get("checkStatus"))){
				hql.append(" and c.checkStatus != '0' ");
			}else {
				hql.append(" and c.checkStatus =:checkStatus ");
				values.put("checkStatus", condition.get("checkStatus"));
			}
		}
		if(condition.containsKey("printDateBegin")){
			hql.append(" and c.printDate >= :printDateBegin");
			values.put("printDateBegin", ExDateUtils.convertToDate(condition.get("printDateBegin").toString()));
		}
		if(condition.containsKey("printDateEnd")){
			hql.append(" and c.printDate <= :printDateEnd");
			values.put("printDateEnd", ExDateUtils.convertToDate(condition.get("printDateEnd").toString()));
		}
	}

	private String getSqlByCondition(Map<String, Object> condition){
		StringBuilder builder = new StringBuilder();
		if(condition.containsKey("brSchoolId")){
			builder.append(" and si.branchschoolid=:brSchoolId");
		}
		if(condition.containsKey("gradeId")){
			builder.append(" and si.gradeid=:gradeId");
		}
		if(condition.containsKey("classicId")){
			builder.append(" and si.classicid=:classicId");
		}
		if(condition.containsKey("teachingType")){
			builder.append(" and si.teachingType=:teachingType");
		}
		if(condition.containsKey("majorId")){
			builder.append(" and si.majorid=:majorId");
		}
		if(condition.containsKey("classesId")){
			builder.append(" and si.classesid=:classesId");
		}
		if(condition.containsKey("studentId")){
			builder.append(" and si.resourceid=:studentId");
		}
		if(condition.containsKey("studyNo")){
			builder.append(" and si.studyNo=:studyNo");
		}	
		if(condition.containsKey("studentName")){
			builder.append(" and si.studentName=:studentName");
		}
		if(condition.containsKey("stuStatus")){
			builder.append(" and si.studentstatus=:stuStatus");
		}
		return builder.toString();
	}


	    /**
		 * 检导入成绩信息的准确性和合法性
		 * 
		 * @param
		 * @param
		 * @param rowNum
		 * @param studyNo
		 * @param studentName
		 * @param unitName
		 * @param usuallyScore
		 * @param writtenScore
		 * @param integratedScore
		 * @param examSub
		 * @param courseName
		 * @param brSchool
		 * @return
		 */
	    @SuppressWarnings("unused")
		private  Map<String, Object> validateImportDatas(int rowNum, String studyNo, String studentName, String unitName, String usuallyScore, String writtenScore, String integratedScore, ExamSub examSub, String courseName, String brSchool) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			ElectiveExamResults examResultVo = null;
			boolean isPass = false;
			String msg = "";
			do{
				//int bachelorType = 0;
				String courseScoreType = "11";
				String examAbnormity = "0";
				//判断学号
            	if(ExStringUtils.isEmpty(studyNo)){
            		returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>学号不能为空").append("</br>");
            		continue;
            	}
            	//判断姓名
                if(ExStringUtils.isEmpty(studentName) ){
                	returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生姓名不能为空").append("</br>");
                	continue;
                }
                //判断课程
                if(ExStringUtils.isEmpty(courseName) ){
                	returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生选修课程不能为空").append("</br>");
                	continue;
                }
                // 分数为数字、字符型成绩、null
                List<Dictionary> allowScore = CacheAppManager.getChildren("CodeScoreChar");
				Map<String,String> alp = new HashMap<String, String>();
				for (Dictionary dic:allowScore) {
					alp.put(ExStringUtils.trim(dic.getDictName()), ExStringUtils.trim(dic.getDictValue()));
				}

				int usuallyScore_d = 0;
				int writtenScore_d = 0;
				int integratedScore_d = 0;
				//String regex = "^(\\d|[1-9]\\d|1[0-4][0-9]|150)(\\.\\d)?$";
				if(ExStringUtils.isEmpty(integratedScore)){
					integratedScore = "";
				}else if(integratedScore.contains("缺")){
					examAbnormity = "2";
					integratedScore = "";
				}else if(integratedScore.contains("缓")){
					examAbnormity = "5";
					integratedScore = "";
				}else if(integratedScore.contains("免考")){
					examAbnormity = "6";
					integratedScore = "";
				}else if(integratedScore.contains("未修")){
					examAbnormity = "7";
					integratedScore = "";
				}else if (integratedScore.contains("免")) {
					examAbnormity = "8";
					integratedScore = "";
				}else if(allowScore.contains(integratedScore)){
					integratedScore = alp.get(integratedScore);
					courseScoreType = integratedScore.substring(0, 2);
				}else if(!ExStringUtils.isNumeric(integratedScore, 2)) {
					isPass = false;
					msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的成绩不合法";
					continue;
				}else {
					integratedScore_d = (int) ExNumberUtils.toDouble(integratedScore,0,1);
					if(ExStringUtils.isNotBlank4All(usuallyScore, writtenScore)){
						usuallyScore_d = Integer.parseInt((usuallyScore));
						writtenScore_d = Integer.parseInt((writtenScore));
					}else {
						usuallyScore_d = integratedScore_d;
						writtenScore_d = integratedScore_d;
					}
				}
				//判断学籍信息
                String hql = " from "+StudentInfo.class.getSimpleName()+" si where si.studyNo=? and si.isDeleted=0";
                StudentInfo studentInfo = studentInfoService.findUnique(hql, studyNo);
                if(studentInfo==null){
                	returnMsg.append("<font color='red'>[第"+rowNum+"行  学号:" + studyNo + "]</font>无法查询该学生学籍信息").append("</br>");
                	continue;
                }
                if(!studentName.equals(studentInfo.getStudentName())){
                	returnMsg.append("<font color='red'>[学号:" + studyNo + " 姓名："+studentName+"]</font>该学生姓名有误").append("</br>");
                	continue;
                }
                if(!unitName.equals(studentInfo.getBranchSchool().getUnitName())){
                	returnMsg.append("<font color='red'>[学号:" + studyNo + " 教学点："+unitName+"]</font>该学生教学点信息有误").append("</br>");
                	continue;
                }
                if(!studentInfo.getBranchSchool().getResourceid().equals(brSchool) && !"admin".equals(brSchool)){
					returnMsg.append("<font color='red'>[学号:" + studyNo + " 教学点："+unitName+"]</font>该学生不在您所属的教学点").append("</br>");
					continue;
				}
                //判断选修课
                Course course = null;
                hql = " from "+Course.class.getSimpleName()+" c where c.courseType='22' and c.isDeleted=0 and c.courseName=?";	
                List<Course> courseList = courseService.findByHql(hql, courseName);
                if(!(courseList!=null && courseList.size()>0)){
                	returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>请先添加选修课："+courseName).append("</br>");
                	continue;
                }
				course = courseList.get(0);
                //成绩校验
				ExamInfo examInfo = examInfoService.findExamInfoByCourseAndExamSubAndCourseType(course.getResourceid(),examSub.getResourceid(),"");
                if(examInfo!=null && "0".equals(examAbnormity)){
                	Double writtenScorePer = examInfo.getFacestudyScorePer();
                	Double usuallyScorePer = examInfo.getFacestudyScorePer2();
                	if(writtenScorePer!=null && usuallyScorePer!=null){
                		int score = (int) ((writtenScorePer*writtenScore_d + usuallyScorePer*usuallyScore_d)/100);
						if (score != integratedScore_d) {
							returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>综合成绩应该为："+score).append("</br>");
							continue;
						}
					}
				}

				// 判断数据库中是否已存在该学生数据
                int countNum = 0;
				hql = " from "+ElectiveExamResults.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and examSub.yearInfo.firstYear=?";
				List<ElectiveExamResults> examResultsList = findByHql(hql, studentInfo.getResourceid(),examSub.getYearInfo().getFirstYear());
				if(examResultsList!=null && examResultsList.size()>0){
					boolean isAbleUpdate = true;
					for (ElectiveExamResults electiveExamResults : examResultsList) {
						if(electiveExamResults.getCourse().getCourseName().equals(courseName) && electiveExamResults.getExamSub()==examSub){
							countNum--;
							if("0".equals(electiveExamResults.getCheckStatus())){
								examResultVo = electiveExamResults;//替换成绩
							}else {
								returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>成绩已提交或已发布，不允许修改！").append("</br>");
								isAbleUpdate = false;
								break;
							}
						}else {
							countNum++;
						}
					}
					if(!isAbleUpdate){
						continue;
					}
					if(examResultsList.size()+countNum>2){
						returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>一年内最多允许导入两门课程").append("</br>");
	                	continue;
					}
				}
				if(examResultVo==null){
					examResultVo = new ElectiveExamResults();
					double creditHour = studentInfo.getFinishedCreditHour()!=null?studentInfo.getFinishedCreditHour():0;
					//计算学分
					studentInfo.setFinishedCreditHour(creditHour+Double.valueOf(course.getPlanoutCreditHour()));
				}
				examResultVo.setCheckStatus("0");
				examResultVo.setExamSub(examSub);
				examResultVo.setCourse(course);
				examResultVo.setStudentInfo(studentInfo);
                examResultVo.setExamAbnormity(examAbnormity);
				if(ExStringUtils.isNotBlank(integratedScore)){
					examResultVo.setIntegratedScore(integratedScore_d+"");
				}
				if (ExStringUtils.isNotBlank(usuallyScore)){
					examResultVo.setUsuallyScore(usuallyScore_d+"");
				}
				if (ExStringUtils.isNotBlank(writtenScore)){
					examResultVo.setWrittenScore(writtenScore_d+"");
				}
                examResultVo.setCourseScoreType(courseScoreType);
                isPass = true;
			}while(false);
			resultMap.put("isPass", isPass);
			resultMap.put("msg", msg);
			if(isPass) {
				resultMap.put("electiveExamResults", examResultVo);
			}
			return resultMap;
		}

		@Override
		public List<ElectiveExamResults> studentExamResultsList(StudentInfo studentInfo) {
			StringBuffer hql = new StringBuffer(" from "+ElectiveExamResults.class.getSimpleName()+" c where c.isDeleted = 0 ");
			Map<String,Object> values =  new HashMap<String, Object>();
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("studentId", studentInfo.getResourceid());
			getHqlByCondition(hql,values,condition);
			hql.append(" order by studentInfo.branchSchool,studentInfo.grade,studentInfo.classic,studentInfo.teachingType,studentInfo.major desc");
			return  (List<ElectiveExamResults>) exGeneralHibernateDao.findByHql(hql.toString(), values);
		}

		@Override
		public List<Map<String, Object>> findElectiveExamResultsByCondition(Map<String, Object> condition) {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("select es.batchname,c.coursecode,c.coursename,u.unitname,u.unitshortname,g.gradename,m.majorname,cl.classesname,si.teachingtype,");
			sBuffer.append(" si.studyno,si.studentname,eer.writtenScore,eer.usuallyScore,eer.integratedScore,eer.examAbnormity,eer.courseScoreType");
			sBuffer.append(" from edu_teach_electiveexamresults eer ");
			sBuffer.append(" join edu_teach_examsub es on es.resourceid=eer.examsubid");
			sBuffer.append(" join edu_roll_studentinfo si on si.resourceid=eer.studentid");
			sBuffer.append(" join hnjk_sys_unit u on u.resourceid=si.branchschoolid");
			sBuffer.append(" join edu_base_grade g on g.resourceid=si.gradeid");
			sBuffer.append(" join edu_base_major m on m.resourceid=si.majorid");
			sBuffer.append(" join edu_roll_classes cl on cl.resourceid=si.classesid");
			sBuffer.append(" join edu_base_course c on c.resourceid=eer.courseid");
			sBuffer.append(" where eer.isdeleted=0 and si.isdeleted=0 and cl.isdeleted=0");
			
			if(condition.containsKey("examSubId")){
				sBuffer.append(" and es.resourceid=:examSubId");
			}
			sBuffer.append(getSqlByCondition(condition));
			if(condition.containsKey("courseId")){
				sBuffer.append(" and c.resourceid=:courseId");
			}
			if(condition.containsKey("resourceids") && ExStringUtils.isNotEmpty(condition.get("resourceids"))){
				String [] resourceid = condition.get("resourceids").toString().split("\\,");
				sBuffer.append(" and eer.resourceid in("+ExStringUtils.addSymbol(resourceid, "'", "'")+")");
			}else {
				sBuffer.append(getSqlByCondition(condition));
			}
			if(condition.containsKey("checkStatus")){
				if("-1".equals(condition.get("checkStatus"))){
					sBuffer.append(" and eer.checkStatus != '0' ");
				}else {
					sBuffer.append(" and eer.checkStatus =:checkStatus ");
				}
			}
			if(condition.containsKey("printDateBegin")){
				sBuffer.append(" and eer.printDate >= :printDateBegin");
				condition.put("printDateBegin", ExDateUtils.convertToDate(condition.get("printDateBegin").toString()));
			}
			if(condition.containsKey("printDateEnd")){
				sBuffer.append(" and eer.printDate <= :printDateEnd");
				condition.put("printDateEnd", ExDateUtils.convertToDate(condition.get("printDateEnd").toString()));
			}
			sBuffer.append(" order by u.unitcode,cl.classcode desc,es.batchname desc,c.coursecode,si.studyno");
			List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
			try {
				mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sBuffer.toString(), condition);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mapList;
		}

		@Override
		public int getCountsByExamSubAndCourse(Map<String, Object> condition) {
			Integer count = 0;
			StringBuilder sqlBuilder = new StringBuilder("select count(*) from edu_teach_electiveexamresults eer where eer.isdeleted=0");;
			if(condition.containsKey("examSubId")){
				sqlBuilder.append(" and eer.examSubId=:examSubId");
			}
			if(condition.containsKey("courseId")){
				sqlBuilder.append(" and eer.courseId=:courseId");
			}
			sqlBuilder.append(" and eer.studentid in(select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0");
			if(condition.containsKey("brSchoolId")){
				sqlBuilder.append(" and si.branchschoolid=:brSchoolId");
			}
			
			sqlBuilder.append(" )group by eer.studentid");
			try {
				List<Map<String, Object>> results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlBuilder.toString(), condition);
				if(results!=null && results.size()>0) {
					count= results.size();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public void batchUpdatePrintTime(Map<String, Object> condition) {
			StringBuffer sBuffer = new StringBuffer();
			String date = "";
			try {
				date = ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			sBuffer.append(" update edu_teach_electiveexamresults eer set eer.printDate=to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')");
			sBuffer.append(" where eer.printdate is null and eer.isdeleted=0");
			if(condition.containsKey("examSubId")){
				sBuffer.append(" and eer.examsubid=:examSubId");
			}
			if(condition.containsKey("courseId")){
				sBuffer.append(" and eer.courseid=:courseId");
			}
			if(condition.containsKey("checkStatus")){
				if("-1".equals(condition.get("checkStatus"))){
					sBuffer.append(" and eer.checkStatus != '0' ");
				}else {
					sBuffer.append(" and eer.checkStatus =:checkStatus ");
				}
			}
			if(condition.containsKey("resourceids") && ExStringUtils.isNotEmpty(condition.get("resourceids"))){
				String [] resourceid = condition.get("resourceids").toString().split("\\,");
				sBuffer.append(" and eer.resourceid in("+ExStringUtils.addSymbol(resourceid, "'", "'")+")");
			}else {
				sBuffer.append(" and eer.studentid in(select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0");
				sBuffer.append(getSqlByCondition(condition));
				sBuffer.append(")");
			}
			try {
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sBuffer.toString(), condition);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

		@Override
		public String constructCourseOptions(Map<String, Object> condition,String defaultValue) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("select c.resourceid,c.coursecode,c.coursename from edu_teach_electiveexamresults eer");
			sqlBuilder.append(" join edu_roll_studentinfo si on si.resourceid=eer.studentid and si.isdeleted=0");
			sqlBuilder.append(" join edu_base_course c on c.resourceid=eer.courseid where eer.isdeleted=0 and c.isdeleted=0");
			if(condition.containsKey("examSubId")){
				sqlBuilder.append(" and eer.examSubId=:examSubId");
			}
			sqlBuilder.append(getSqlByCondition(condition));
			sqlBuilder.append(" group by c.resourceid,c.coursecode,c.coursename  order by c.coursecode");
			List<Map<String, Object>> courseList = new ArrayList<Map<String,Object>>();
			try {
				courseList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlBuilder.toString(), condition);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			StringBuffer options = new StringBuffer("<option value=''></option>");
			if(ExCollectionUtils.isNotEmpty(courseList)){
				for(Map<String, Object> course : courseList){
					options.append("<option value='"+course.get("resourceid")+"'");
					if(ExStringUtils.isNotEmpty(defaultValue) && defaultValue.equals(course.get("resourceid"))){
						options.append(" selected ");
					}
					options.append(">");
					options.append(course.get("coursecode")+"-"+course.get("coursename"));
					options.append("</option>");
				}
			}
			return options.toString();
		}
}
