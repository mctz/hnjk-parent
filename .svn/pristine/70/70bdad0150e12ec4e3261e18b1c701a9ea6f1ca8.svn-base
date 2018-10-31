package com.hnjk.edu.teaching.service.impl;

import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseExamination;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseExaminationService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import lombok.Cleanup;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * 排考服务接口实现
 * @author msl
 * @since 2018-05-21
 */
@Transactional
@Service("teachingPlanCourseExaminationService")
public class TeachingPlanCourseExaminationServiceImpl extends BaseServiceImpl<TeachingPlanCourseExamination> implements ITeachingPlanCourseExaminationService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;

	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;

	@Override
	public Page findPageByCondition(Map<String, Object> condition,Page objPage) {
		StringBuilder builder = new StringBuilder("from "+TeachingPlanCourseExamination.class.getSimpleName()+" where isDeleted=0");
		if (condition.containsKey("branchSchool")) {
			builder.append(" and classes.brSchool.resourceid=:branchSchool");
		} else if (condition.containsKey("branchSchoolId")) {
			builder.append(" and classes.brSchool.resourceid=:branchSchoolId");
		}
		if (condition.containsKey("grade")) {
			builder.append(" and classes.grade.resourceid=:grade");
		}else if (condition.containsKey("gradeid")) {
			builder.append(" and classes.grade.resourceid=:gradeid");
		}
		if (condition.containsKey("classic")) {
			builder.append(" and classes.classic.resourceid=:classic");
		}else if (condition.containsKey("classicid")) {
			builder.append(" and classes.classic.resourceid=:classicid");
		}
		if (condition.containsKey("teachingType")) {
			builder.append(" and classes.teachingType=:teachingType");
		}
		if (condition.containsKey("major")) {
			builder.append(" and classes.major.resourceid=:major");
		}else if (condition.containsKey("majorid")) {
			builder.append(" and classes.major.resourceid=:majorid");
		}
		if (condition.containsKey("classes")) {
			builder.append(" and classes.resourceid=:classes");
		} else if (condition.containsKey("classesid")) {
			builder.append(" and classes.resourceid=:classesid");
		}
		if (condition.containsKey("plancourseid")) {
			builder.append(" and teachingPlanCourse.resourceid=:plancourseid");
		}
		if (condition.containsKey("courseid")) {
			builder.append(" and course.resourceid=:courseid");
		}
		if (condition.containsKey("resIds")) {
			builder.append(" and resourceid in(:resIds)");
		}
		return  findByHql(objPage,builder.toString(),condition);
	}

	@Override
	public Map<String, Object> importExaminationResult(Map<String, Object> condition, String filePath) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			@Cleanup InputStream inputStream = new FileInputStream(filePath);
			@Cleanup Workbook workbook = WorkbookFactory.create(inputStream);
			StringBuffer returnMsg = new StringBuffer();
			Map<String,Object> validateMap = new HashMap<String, Object>();
			int count = 0;		//总条数
			int successCount = 0;// 成功的条数
			// Read the Sheet
			//for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			do{
				String userName = ExStringUtils.toString(condition.get("userName"));
				String brSchoolid = ExStringUtils.toString(condition.get("brSchoolid"));
				Classes classes =  null;
				TeachingPlanCourse planCourse = null;
				//TeachingPlanCourse planCourse = teachingPlanCourseService.get(ExStringUtils.toString(condition.get("plancourseid")));
				//只读取第一张sheet表
				Sheet sheet = workbook.getSheetAt(0);
				//getLastRowNum:下表从0开始读取
				count = sheet.getLastRowNum();
				String[] parseDate = {ExDateUtils.PATTREN_DATE,ExDateUtils.PATTREN_DATE_CN,ExDateUtils.PATTREN_DATE_EN};
				String[] parseTime = {ExDateUtils.PATTREN_SIMPLETIME,ExDateUtils.PATTREN_TIME};
				List<TeachingPlanCourseExamination> examinationList = new ArrayList<TeachingPlanCourseExamination>();
				// 从第2行开始读取数据
				for (int rowNum=1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					//专业
					String majorName = ExStringUtils.toString(row.getCell(0));
					//层次
					String classicName = ExStringUtils.toString(row.getCell(1));
					//年级
					String gradeName = ExStringUtils.toString(row.getCell(2));
					//班级名称
					String classname = ExStringUtils.toString(row.getCell(3));
					//课程名称
					String courseName = ExStringUtils.toString(row.getCell(5));
					//教师编号
					String teacherCode = ExStringUtils.toString(row.getCell(6));
					//监考教师
					String teacher = ExStringUtils.toString(row.getCell(7));
					//开始日期
					String startExamDate = ExStringUtils.toString(row.getCell(8));
					//结束日期
					String endExamDate = ExStringUtils.toString(row.getCell(9));
					//开始时间
					String startTimePeriod = ExStringUtils.toString(row.getCell(10));
					//结束时间
					String endTimePeriod = ExStringUtils.toString(row.getCell(11));
					//地点
					String location = ExStringUtils.toString(row.getCell(12));
					//课室
					String classroom = ExStringUtils.toString(row.getCell(13));
					//人数
					String studentNum = ExStringUtils.toString(row.getCell(14));
					//备注
					String memo = ExStringUtils.toString(row.getCell(15));

					Map<String,Object> tempMap = new HashMap<String,Object>();

					//如果是教学点导入则添加教学点查询条件
					if(condition.containsKey("brSchoolid") && ExStringUtils.isNotBlank(condition.get("brSchoolid"))){
						tempMap.put("brSchoolid",condition.get("brSchoolid"));
					}
					tempMap.put("gradeName",gradeName);
					tempMap.put("classicName",classicName);
					tempMap.put("majorName",majorName);
					tempMap.put("classname",classname);
					tempMap.put("courseName",courseName);
					List<Map<String,Object>> results = findClassesAndPlanCourseByCondition(tempMap);
					if(results!=null && results.size()==1){
						Map<String,Object> residInfo = results.get(0);
						classes =  classesService.get(residInfo.get("classesid").toString());
						planCourse = teachingPlanCourseService.get(residInfo.get("plancourseid").toString());
					}else if(results!=null && results.size()>0){
						returnMsg.append("所在行【"+rowNum+"】:匹配到多个信息，班级课程必须唯一！<br>");
						continue;
					}else {
						returnMsg.append("所在行【"+rowNum+"】:无法查询到该班级和课程信息！<br>");
						continue;
					}
					TeachingPlanCourseExamination examination = new TeachingPlanCourseExamination();
					try {
						Date startExamDate_d = ExDateUtils.parseDate(startExamDate, parseDate);
						Date endExamDate_d = ExDateUtils.parseDate(endExamDate, parseDate);
						Date startTimePeriod_d = ExDateUtils.parseDate(startTimePeriod, parseTime);
						Date endTimePeriod_d = ExDateUtils.parseDate(endTimePeriod, parseTime);
						if (startExamDate_d.after(endExamDate_d) || startTimePeriod_d.after(endTimePeriod_d)) {
							returnMsg.append("所在行【"+rowNum+"】:开始时间不能大于结束时间！<br>");
							continue;
						}

						examination.setStartExamDate(startExamDate_d);
						examination.setEndExamDate(endExamDate_d);
						examination.setStartTimePeriod(startTimePeriod_d);
						examination.setEndTimePeriod(endTimePeriod_d);
						//examination.setStartTimePeriod(ExDateUtils.formatDateStr(startTimePeriod_d,ExDateUtils.PATTREN_TIME));
						//examination.setEndTimePeriod(ExDateUtils.formatDateStr(endTimePeriod_d,ExDateUtils.PATTREN_TIME));
						examination.setStudentNum(Integer.parseInt(studentNum)+"");
					} catch (ParseException e) {
						returnMsg.append("所在行【" + rowNum + "】日期或时间格式无法解析！<br>");
						continue;
					} catch (NumberFormatException e) {
						returnMsg.append("所在行【" + rowNum + "】学生人数格式不正确！<br>");
						continue;
					}
					examination.setClasses(classes);
					examination.setCourse(planCourse.getCourse());
					examination.setOperatorName(userName);
					examination.setTeachingPlanCourse(planCourse);
					examination.setTeacherCode(teacherCode);
					examination.setTeacher(teacher);
					//examination.setExamDate(examDate);
					//examination.setTimePeriod(timePeriod);
					examination.setLocation(location);
					examination.setClassroom(classroom);
					//examination.setStudentNum(studentNum);
					examination.setMemo(memo);

					successCount++;
					examinationList.add(examination);
				}
				batchSaveOrUpdate(examinationList);
			}while(false);
			map.put("totalCount", count);
			map.put("successCount", successCount);
			map.put("message", returnMsg.toString());
		} catch (FileNotFoundException e) {
			logger.error("导入文件不存在", e);
			map.put("message", "导入失败");
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			map.put("message", "导入失败");
		} catch (IOException e) {
			logger.error("读取文件出错", e);
			map.put("message", "导入失败");
		} catch (Exception e) {
			logger.error("解析文件出错", e);
			map.put("message", "导入失败");
		}
		return map;
	}

	/**
	 * 根据查询条件查询班级id和教学计划课程id
	 * @param condition
	 * @return classesid,plancourseid
	 */
	private List<Map<String,Object>> findClassesAndPlanCourseByCondition(Map<String,Object> condition) {
		StringBuilder builder = new StringBuilder();
		List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
		builder.append("select cl.RESOURCEID classesid,pc.RESOURCEID plancourseid from EDU_ROLL_CLASSES cl");
		builder.append(" join EDU_ROLL_STUDENTINFO si on si.CLASSESID=cl.RESOURCEID");
		builder.append(" join EDU_BASE_GRADE g on g.RESOURCEID=cl.GRADEID and g.ISDELETED=0");
		builder.append(" join EDU_BASE_CLASSIC ci on ci.RESOURCEID=cl.CLASSICID and ci.ISDELETED=0");
		builder.append(" join EDU_BASE_MAJOR m on m.RESOURCEID=cl.MAJORID and m.ISDELETED=0");
		builder.append(" join EDU_TEACH_PLANCOURSE pc on pc.PLANID=si.TEACHPLANID and pc.ISDELETED=0");
		builder.append(" join EDU_BASE_COURSE c on c.RESOURCEID=pc.COURSEID where cl.isdeleted=0");
		if(condition.containsKey("brSchoolid")){
			builder.append(" and cl.ORGUNITID=:brSchoolid");
		}
		if(condition.containsKey("gradeName")){
			builder.append(" and g.GRADENAME=:gradeName");
		}
		if(condition.containsKey("classicName")){
			builder.append(" and ci.CLASSICNAME=:classicName");
		}
		if(condition.containsKey("majorName")){
			builder.append(" and m.MAJORNAME=:majorName");
		}
		if(condition.containsKey("classname")){
			builder.append(" and cl.CLASSESNAME=:classname");
		}
		if(condition.containsKey("calssesid")){
			builder.append(" and cl.resourceid=:calssesid");
		}
		if(condition.containsKey("courseName")){
			builder.append(" and c.COURSENAME=:courseName");
		}
		builder.append(" group by cl.RESOURCEID,pc.RESOURCEID");
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(builder.toString(),condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}

}

