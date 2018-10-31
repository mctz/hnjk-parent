package com.hnjk.edu.teaching.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import lombok.Cleanup;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.UniversalExam;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.IUniversalExamService;
import com.hnjk.edu.teaching.vo.UniversalExamCountVO;
import com.hnjk.edu.teaching.vo.UniversalExamDetailsVO;
import com.hnjk.edu.teaching.vo.UniversalExamVO;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 统考成绩表-教学计划内课程
 * <code>UniversalExamServiceImpl</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-7-9 上午 10:55:20
 * @see
 * @version 1.0
 */
@Transactional
@Service("universalExamService")
public class UniversalExamServiceImpl extends BaseServiceImpl<UniversalExam> implements
		IUniversalExamService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	/**
	 * 根据查询条件获取统考成绩列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page findUniversalExamList(Map<String, Object> condition, Page UEpage)
			throws ServiceException {
		try {
			StringBuffer universalExam_sql = new StringBuffer(""); 
			universalExam_sql.append("SELECT ue.resourceid UEID,so.studyno,so.studentname,c.coursename,so.resourceid studentId, ");
			universalExam_sql.append("u.unitname,g.gradename,m.majorname,cl.classesname,ue.score,ue.checkstatus,cc.classicname classicName, ");
			universalExam_sql.append("ue.certificateno,ue.examdate,ue.whichtime,pc.examclasstype,ue.operatorname,er.integratedscore  currentScore ");
			universalExam_sql.append(" FROM edu_teach_universalexam ue, ");
			universalExam_sql.append(" edu_roll_studentinfo so, ");
			universalExam_sql.append(" edu_base_course c, ");
			universalExam_sql.append(" edu_teach_plancourse pc, ");
			universalExam_sql.append(" edu_teach_examresults er, ");
			universalExam_sql.append(" edu_base_major m, ");
			universalExam_sql.append(" edu_base_grade g, ");
			universalExam_sql.append(" edu_base_classic cc, ");
			universalExam_sql.append(" edu_roll_classes cl, ");
			universalExam_sql.append(" hnjk_sys_unit u ");
			universalExam_sql.append("WHERE ue.studentid=so.resourceid ");
			universalExam_sql.append("AND so.branchschoolid=u.resourceid ");
			universalExam_sql.append("AND so.gradeid=g.resourceid ");
			universalExam_sql.append("AND so.classicid=cc.resourceid ");
			universalExam_sql.append("AND so.classesid=cl.resourceid ");
			universalExam_sql.append("AND so.majorid=m.resourceid ");
			universalExam_sql.append("AND ue.plancourseid=pc.resourceid ");
			universalExam_sql.append("AND pc.courseid=c.resourceid ");
			universalExam_sql.append("AND ue.isdeleted=0 AND so.isdeleted=0 AND u.isdeleted=0 ");
			universalExam_sql.append("AND g.isdeleted=0 AND m.isdeleted=0 AND cc.isdeleted=0 ");
			universalExam_sql.append("AND cl.isdeleted=0 AND pc.isdeleted=0 AND c.isdeleted=0 and er.isdeleted=0 ");
			
			if(condition.containsKey("UEStudentName")){
				universalExam_sql.append(" AND so.studentname like  '%"+condition.get("UEStudentName")+"%' ");
			}
			if(condition.containsKey("UEStudyNo")){
				universalExam_sql.append(" AND so.studyno like '%"+condition.get("UEStudyNo")+"%' ");
			}
			if(condition.containsKey("UEGradeid")){
				universalExam_sql.append(" and  g.resourceid=:UEGradeid ");
			}
			if(condition.containsKey("UEClassic")){
				universalExam_sql.append(" and  cc.resourceid=:UEClassic ");
			}
			if(condition.containsKey("UEMajor")){
				universalExam_sql.append(" and  m.resourceid=:UEMajor ");
			}
			if(condition.containsKey("UEClassId")){
				universalExam_sql.append(" and  cl.resourceid=:UEClassId ");
			}
			if(condition.containsKey("certificateNo")){
				universalExam_sql.append(" and ue.certificateNo=:certificateNo ");
			}
			if(condition.containsKey("examDate")){
				universalExam_sql.append(" and ue.examDate=to_date('"+condition.get("examDate")+"','yyyy-MM-dd')");
			}
			if(condition.containsKey("UECourseId")){
				universalExam_sql.append(" and c.resourceid=:UECourseId ");
			}
			if(condition.containsKey("UESchool")){
				universalExam_sql.append(" and  u.resourceid=:UESchool ");
			}
			universalExam_sql.append(" ORDER BY  ue.examDate desc, so.studyno ");
			List<UniversalExamVO> universalExamList = baseSupportJdbcDao.getBaseJdbcTemplate()
										.findList(universalExam_sql.toString(), UniversalExamVO.class, condition);
			UEpage.setResult(universalExamList);
		} catch (Exception e) {
			logger.error("根据查询条件获取统考成绩列表出错", e);
		}
		return UEpage;
	}

	/**
	 * 根据查询条件获取统考成绩列表--HQL
	 */
	@Override
	public Page findUniversalExamListByHQL(Map<String, Object> condition, Page UEpage)
			throws ServiceException {
		StringBuffer universalExam_sql = new StringBuffer(""); 
		universalExam_sql.append("from "+UniversalExam.class.getSimpleName()+" ue where ue.isDeleted=0 ");
		
		if(condition.containsKey("UEStudentName")){
			universalExam_sql.append(" AND ue.studentInfo.studentName like  '%"+condition.get("UEStudentName")+"%' ");
		}
		if(condition.containsKey("UEStudyNo")){
			universalExam_sql.append(" AND ue.studentInfo.studyNo like '%"+condition.get("UEStudyNo")+"%'  ");
		}
		if(condition.containsKey("UEGradeid")){
			universalExam_sql.append(" and  ue.studentInfo.grade.resourceid=:UEGradeid ");
		}
		if(condition.containsKey("UEClassic")){
			universalExam_sql.append(" and  ue.studentInfo.classic.resourceid=:UEClassic ");
		}
		if(condition.containsKey("UEMajor")){
			universalExam_sql.append(" and  ue.studentInfo.major.resourceid=:UEMajor ");
		}
		if(condition.containsKey("UEClassId")){
			universalExam_sql.append(" and  ue.studentInfo.classes.resourceid=:UEClassId ");
		}
		if(condition.containsKey("certificateNo")){
			universalExam_sql.append(" and ue.certificateNo=:certificateNo ");
		}
		if(condition.containsKey("examDate")){
			universalExam_sql.append(" and ue.examDate=to_date('"+condition.get("examDate")+"','yyyy-MM-dd')");
		}
		if(condition.containsKey("UECourseId")){
			universalExam_sql.append(" and ue.teachingPlanCourse.course.resourceid=:UECourseId ");
		}
		if(condition.containsKey("UESchool")){
			universalExam_sql.append(" and  ue.studentInfo.branchSchool.resourceid=:UESchool ");
		}
		//当前是否通过
		if(condition.containsKey("isPass")){
			if(condition.get("isPass").equals(Constants.BOOLEAN_YES)){
				universalExam_sql.append(" and to_number(ue.score)>=60 ");
			}else{
				universalExam_sql.append(" and to_number(ue.score)<60 ");
			}
		}
		//最终是否通过
		if(condition.containsKey("finalPass")){
			universalExam_sql.append(" and ue.studentInfo.resourceid "+(Constants.BOOLEAN_YES.equals(condition.get("finalPass"))?"":"not")+" in(");
			universalExam_sql.append(" select uexam.studentInfo.resourceid from "+UniversalExam.class.getSimpleName()+" uexam where uexam.isDeleted=0");
			universalExam_sql.append(" and uexam.studentInfo=ue.studentInfo and uexam.course=ue.course and to_number(ue.score)>=60)");
		}
		universalExam_sql.append(" order by ue.examDate desc, ue.studentInfo.studyNo");
		
		return findByHql(UEpage, universalExam_sql.toString(), condition);
	}
	
	/**
	 * 解析统考成绩文件
	 */
	@Override
	public Map<String, Object> analysisUniversalExamFile(String filePath, Course course, User user, String examType) 
			throws ServiceException {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer returnMsg = new StringBuffer();		
		// 总条数
		int count = 0;
		// 成功的条数
		int successCount = 0;
		try {
			@Cleanup InputStream is		= new FileInputStream(filePath);
			@Cleanup Workbook rwb	    = Workbook.getWorkbook(is);
			Sheet sheet 		  	    = rwb.getSheet(0);
			int countRow          	    = sheet.getRows();
			count = countRow - 3; //前3行分别是标题和备注
//			int countColumn       	    = sheet.getColumns();
			// 获取统考规定导入的次数
			String UETimes = CacheAppManager.getSysConfigurationByCode("unifiedExam_times").getParamValue();
			// 统考进行补考的批次
			String UEMakeup = CacheAppManager.getSysConfigurationByCode("unifiedExam_makeup").getParamValue();
			
			//遍历EXCEL文件
			for (int i = 3; i < countRow; i++) {
				String school = ExStringUtils.trimToEmpty(sheet.getCell(0,i).getContents()); // 教学点
				String studentName = ExStringUtils.trimToEmpty(sheet.getCell(1,i).getContents()); // 姓名
				String idCardNo = ExStringUtils.trimToEmpty(sheet.getCell(2,i).getContents()); // 身份证号码
				String studentNo = ExStringUtils.trimToEmpty(sheet.getCell(4,i).getContents()); // 学号
				String score = ExStringUtils.trimToEmpty(sheet.getCell(5,i).getContents()); // 成绩
				String certificateNo = ExStringUtils.trimToEmpty(sheet.getCell(6,i).getContents()); // 证书编号
				Cell examDateType = sheet.getCell(7,i);
				CellType cellType = examDateType.getType();
				if(CellType.DATE  !=cellType ){
					returnMsg.append("<font color='red'>[学号：" + studentNo + "]</font>考试日期不是日期类型</br>");
					continue;
				}
				String examDate = ExStringUtils.trimToEmpty(sheet.getCell(7,i).getContents()); // 考试日期
				Date _examDate =((DateCell)examDateType).getDate();
				String whichTime = ExStringUtils.trimToEmpty(sheet.getCell(8,i).getContents()); // 第几次
				
				// 检验数据的准确性和合法性
				Map<String,Object> validateMap = validateImportDatas(score,examDate,whichTime,course, 
																		school, studentName, idCardNo,studentNo, examType, UEMakeup);
				
				boolean isPass = (Boolean)validateMap.get("isPass");
				if(!isPass){
					returnMsg.append((String)validateMap.get("msg") + "</br>");
					continue;
				}
				
				if("Y".equals(examType)){// 补考的次数都为-1
					whichTime = "-1";
				}
				
				TeachingPlanCourse tpc = (TeachingPlanCourse)validateMap.get("planCourse");
				ExamSub es = (ExamSub)validateMap.get("examSub");
				ExamSub es_one = (ExamSub)validateMap.get("examSub_one");
				ExamSub es_two = (ExamSub)validateMap.get("examSub_two");
				StudentInfo so = (StudentInfo)validateMap.get("studentInfo");
//				SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
//				Date _examDate = sdf.parse(examDate);
				Date nowTime = new Date();
				// 统考成绩处理
				Map<String, Object> params = new HashMap<String, Object>();
				
				// 获取该次统考成绩
				params.clear();
				params.put("planCourse", tpc.getResourceid());
				params.put("studentInfo", so.getResourceid());
				//params.put("examDate", _examDate);
				
				UniversalExam saveUE = null;
				UniversalExam mkUE = null;
				List<UniversalExam> _UEList = findByCondition(params,"");
				if(ExCollectionUtils.isNotEmpty(_UEList) ){
					//_UEList.remove(ue)  导致 ConcurrentModificationException
//					for(UniversalExam ue : _UEList){
//						// 没导入过该次统考成绩
//						if("N".equals(examType)) {// 正考
//							if(whichTime.equals(ExStringUtils.trim(ue.getWhichTime())) 
//									&& (DateFormatUtils.format(_examDate, "yyyy-MM-dd")).equals(DateFormatUtils.format(ue.getExamDate(), "yyyy-MM-dd"))){
//								saveUE = ue;
//								_UEList.remove(ue);
//								continue;
//							}
//							if("-1".equals(ue.getWhichTime())){
//								mkUE = ue;
//							}
//						} else if("Y".equals(examType) && "-1".equals(ue.getWhichTime())){// 补考
//							saveUE = ue;
//							break;
//						}
//					}
					for(int k=0;k<_UEList.size();k++){
						UniversalExam ue = _UEList.get(k);
						// 没导入过该次统考成绩
						if("N".equals(examType)) {// 正考
							if(whichTime.equals(ExStringUtils.trim(ue.getWhichTime())) 
									&& (DateFormatUtils.format(_examDate, "yyyy-MM-dd")).equals(DateFormatUtils.format(ue.getExamDate(), "yyyy-MM-dd"))){
								saveUE = ue;
								_UEList.remove(ue);
								k--;
								continue;
							}
							if("-1".equals(ue.getWhichTime())){
								mkUE = ue;
							}
						} else if("Y".equals(examType) && "-1".equals(ue.getWhichTime())){// 补考
							saveUE = ue;
							break;
						}
					}
					if("N".equals(examType) && saveUE==null ){
						// 判断导入统考成绩的次数是否超过规定次数
						if(_UEList.size()>=Integer.valueOf(UETimes)){
							returnMsg.append("<font color='red'>[学号：" + studentNo + "]</font>该门课程导入成绩的次数不能超过"+UETimes+"次</br>");
							continue;
						}
					}
				}
				
				// 成绩记录的处理
				ExamResults examResult = null;
				if("N".equals(examType)){// 处理正考的成绩
					examResult = handleExamResults(tpc,es,so,user,params,score,course,nowTime,_examDate);
				} else if ("Y".equals(examType)){// 处理补考的成绩
					examResult = handMKResults(tpc,es,es_one,es_two,so,user,params,score,course,nowTime,_examDate,UEMakeup,saveUE);
					if(examResult == null){
						returnMsg.append("<font color='red'>[学号：" + studentNo + "]</font>该门课程已经及格，不需补考</br>");
						continue;
					}
				}
				
				// 1、保存统考成绩记录
				/*if(_UEList != null && _UEList.size() > 0){
					saveUE = _UEList.get(0);
				} else {*/
				if(saveUE==null){
					saveUE = new UniversalExam();
					saveUE.setStudentInfo(so);
					saveUE.setTeachingPlanCourse(tpc);
					saveUE.setCourse(course);
					saveUE.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
					saveUE.setExamDate(_examDate);
					saveUE.setWhichTime(whichTime);
				}
				saveUE.setCertificateNo(certificateNo);
				saveUE.setOperateDate(nowTime);
				saveUE.setOperator(user);
				saveUE.setOperatorName(user.getCnName());
				saveUE.setScore(score);
				saveUE.setExamResults(examResult);
				saveOrUpdate(saveUE);
				// 如果是正考并且成绩及格，则删除统考补考和更新统考的当前成绩
				if("N".equals(examType) && Double.valueOf(examResult.getIntegratedScore())>=60){
					if(mkUE!=null){
						delete(mkUE);
						if(ExCollectionUtils.isNotEmpty(_UEList)){
							updateExamResult(examResult.getResourceid(), so.getResourceid(), course.getResourceid(), tpc.getResourceid());
						}
					}
				}
				// 如果是补考，则更新所有的当前成绩
				if("Y".equals(examType) && ExCollectionUtils.isNotEmpty(_UEList)){
					updateExamResult(examResult.getResourceid(), so.getResourceid(), course.getResourceid(), tpc.getResourceid());
				}
				//最新导入的成绩，不管是正考还是补考成绩，都更新到StudentLearnPlan 
				StudentLearnPlan plan = studentLearnPlanService.findUnique(" from "+StudentLearnPlan.class.getSimpleName()+" where isDeleted = 0 and studentInfo.resourceid=? and teachingPlanCourse.resourceid=? ", so.getResourceid(),tpc.getResourceid());
				if(plan!=null ){
					plan.setExamResults(examResult);
					plan.setExamInfo(examResult.getExamInfo());
					plan.setFinalScore(score);
					if(Integer.parseInt(score)>=60){
						plan.setIsPass("Y");
					}else{
						plan.setIsPass("N");
					}
					plan.setStatus(3);//已发布
					studentLearnPlanService.saveOrUpdate(plan);
				}else{
					isPass = false;
					if(!isPass){
						returnMsg.append("学号 "+studentNo+":该学生没有相应的学习计划" + "</br>");
						continue;
					}
				}
				++successCount;
			}
		} catch (FileNotFoundException e) {
			logger.error("导入统考成绩文件不存在", e);
		} catch (IOException e) {
			logger.error("读取统考成绩文件出错", e);
		} catch (Exception e) {
			logger.error("解析统考成绩文件出错", e);
		}
		
		map.put("totalCount", (count < 0 ? 0 : count));
		map.put("successCount", successCount);
		map.put("message", returnMsg.toString());
		
		return map;
	}
	
	/**
	 * 处理统考正考成绩
	 * @param tpc
	 * @param es
	 * @param so
	 * @param user
	 * @param params
	 * @param score
	 * @param course
	 * @param nowTime
	 * @param _examDate
	 * @return
	 * @throws Exception
	 */
	private ExamResults handleExamResults(TeachingPlanCourse tpc,ExamSub es,StudentInfo so,User user,
			Map<String, Object> params,String score,Course course,Date nowTime,Date _examDate) throws Exception {
		// 最高分数
		String maxScore = score;
		// 2、该门课程的正考是否已经有成绩
		String examResultHql = "from " + ExamResults.class.getSimpleName() + " where isDeleted=0 and majorCourseId=? "
				+ " and studentInfo.resourceid=? and examsubId=? ";
		List<ExamResults> examResultsList = examResultsService.findByHql(examResultHql, tpc.getResourceid(), 
																	so.getResourceid(), es.getResourceid());
		ExamResults _examResults = null;
		if(examResultsList != null && examResultsList.size() > 0){
			_examResults = examResultsList.get(0);
			// 3、有，则判断该成绩是否是已发布
			if(!Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(_examResults.getCheckStatus())){
				// 4、非已发布，则替换，并且将状态修改为已发布
				_examResults.setIntegratedScore(score);
				_examResults.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
			} else {
				// 5、已发布，该学生的该门课程是否已经导入过统考成绩，否，则直接替换成绩
				params.clear();
				params.put("planCourse", tpc.getResourceid());
				params.put("studentInfo", so.getResourceid());
				params.put("ne_examDate", _examDate);
				
				List<UniversalExam> _universalExamList = findByCondition(params, "ue.score desc");
				if(_universalExamList == null || _universalExamList.isEmpty()){
					_examResults.setIntegratedScore(score);
				} else {
					// 如果是重复导，则取最高的分数(除了这次)
					BigDecimal ueScore = new BigDecimal(_universalExamList.get(0).getScore());
					BigDecimal _score = new BigDecimal(score);
					// 6、是，则之前导入统考成绩中最高分数跟导入的成绩对比哪个高,获取最高分数替换成绩表的成绩
					if(ueScore.compareTo(_score) > 0){
						maxScore = ueScore.toString();
					}
					_examResults.setIntegratedScore(maxScore);
				}
			}
		} else {
			// 7、没有则新增（已发布）
			_examResults = createExamResults(tpc, es, 1L, so, user, score, course,nowTime,1);
		}
		_examResults.setExamAbnormity("0");
		_examResults.setWrittenScore("0");
		_examResults.setUsuallyScore("0");
		_examResults.setOnlineScore("0");
		examResultsService.saveOrUpdate(_examResults);
		// 如果总分是及格的，则删除补考名单和补考成绩
		if(Integer.valueOf(_examResults.getIntegratedScore()==null?"0":_examResults.getIntegratedScore()) >= 60){
			// 删除该学生该门课程的所有补考名单
			studentMakeupListService.deleteByStuIdAndCourseId(so.getResourceid(), course.getResourceid(), tpc.getResourceid());
			// 删除该学生该门课程的所有补考成绩
			examResultsService.deleteByStuIdAndPlanCourseId(so.getResourceid(), tpc.getResourceid());
		}
		
		return _examResults;
	}

	/**
	 * 生成成绩记录
	 * @param tpc
	 * @param es
	 * @param so
	 * @param user
	 * @param score
	 * @param course
	 * @param nowTime
	 * @return
	 * @throws Exception
	 */
	private ExamResults createExamResults(TeachingPlanCourse tpc, ExamSub es,long examCount,
			StudentInfo so, User user, String score, Course course, Date nowTime,Integer examForm) throws Exception {
		ExamResults _examResults;
		//Integer examCourseType = "facestudy".equals(tpc.getTeachType())?1:2;
		Integer examCourseType = 1;
		String isMachineExam = Constants.BOOLEAN_NO;
		if(examForm!=null && examForm==3){
			//examCourseType =3;
			isMachineExam = Constants.BOOLEAN_YES;
		}
		ExamInfo examInfo = examInfoService.saveExamInfo(60D, es, course, examCourseType,isMachineExam);
		_examResults = new ExamResults();
		_examResults.setCourse(course);	
		_examResults.setExamInfo(examInfo);
		_examResults.setMajorCourseId(tpc.getResourceid());
		_examResults.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
		_examResults.setExamsubId(es.getResourceid());
		_examResults.setCreditHour(tpc.getCreditHour()!=null?tpc.getCreditHour():null);
		_examResults.setStydyHour(tpc.getStydyHour()!=null?tpc.getStydyHour().intValue():null);
		_examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
		_examResults.setIsMakeupExam(es.getExamType());//考试类型
		_examResults.setIsMachineExam(isMachineExam);
		_examResults.setCourseType(tpc.getCourseType());														
		_examResults.setStudentInfo(so);
		_examResults.setCourseScoreType(examInfo.getCourseScoreType());
		_examResults.setExamCount(examCount);	
		_examResults.setPlanCourseTeachType(tpc.getTeachType());
		_examResults.setIntegratedScore(score);
		_examResults.setFillinDate(nowTime);
		_examResults.setFillinMan(user.getCnName());
		_examResults.setFillinManId(user.getResourceid());
		return _examResults;
	}
	
	/**
	 * 统考补考成绩处理
	 * @param tpc
	 * @param es
	 * @param es_one
	 * @param es_two
	 * @param so
	 * @param user
	 * @param params
	 * @param score
	 * @param course
	 * @param nowTime
	 * @param _examDate
	 * @param UEMakeup
	 * @param saveUE
	 * @return
	 * @throws Exception
	 */
	private ExamResults handMKResults(TeachingPlanCourse tpc,ExamSub es,ExamSub es_one,ExamSub es_two,StudentInfo so,User user,
			Map<String, Object> params,String score,Course course,Date nowTime,Date _examDate,String UEMakeup,UniversalExam saveUE) throws Exception {
		ExamResults _examResults = null;
		
		// 该门课程的正考是否已经有成绩
		/*String examResultHql = "from " + ExamResults.class.getSimpleName() + " where isDeleted=0 and majorCourseId=? "
				+ " and studentInfo.resourceid=? and examsubId=? ";
		List<ExamResults> examResultsList = examResultsService.findByHql(examResultHql, tpc.getResourceid(), 
																	so.getResourceid(), es.getResourceid());*/
		ExamResults er = examResultsService.getByCondtition(so.getResourceid(), tpc.getResourceid(), es.getResourceid());
		// 没有正考成绩就创建一条缺考的正考成绩
		if(er==null){
			er = createExamResults(tpc, es, 1L, so, user, "0", course, nowTime,1);
			er.setExamAbnormity("2");// 缺考
			er.setWrittenScore("0");
			er.setUsuallyScore("0");
			er.setOnlineScore("0");
			examResultsService.save(er);
		}
		// 有正考成绩，则判断正考成绩是否及格，不及格就生成补考名单
		if(("0".equals(er.getExamAbnormity()) && Integer.valueOf(er.getIntegratedScore()) < 60)
				|| !("0".equals(er.getExamAbnormity()))){
			String _isPass = "N";
			if(Double.valueOf(score)>=60){
				_isPass = "Y";
			}
			// 生成补考名单
			if("Y".equals(UEMakeup)){
				createMakeupList(tpc, es_one, so, course, er, _isPass,saveUE,Double.valueOf(score));
				if(saveUE==null){
					_examResults = createExamResults(tpc, es_one, 2L, so, user, score, course, nowTime,1);
					_examResults.setExamAbnormity("0");
					_examResults.setWrittenScore("0");
					_examResults.setUsuallyScore("0");
					_examResults.setOnlineScore("0");
					examResultsService.saveOrUpdate(_examResults);
				} else if(!Double.valueOf(score).equals(Double.valueOf(saveUE.getScore()))){
					_examResults = examResultsService.getByCondtition(so.getResourceid(), tpc.getResourceid(), es_one.getResourceid());
					_examResults.setIntegratedScore(score);
					examResultsService.saveOrUpdate(_examResults);
				}
			} else if("T".equals(UEMakeup)){
				// 一补
				ExamResults _erOne = null;
				if(saveUE==null){
					createMakeupList(tpc, es_one, so, course, er, "N",saveUE,Double.valueOf(score));
					_erOne = createExamResults(tpc, es_one, 2L, so, user, "0", course, nowTime,1);
					_erOne.setExamAbnormity("2");// 缺考
					_erOne.setWrittenScore("0");
					_erOne.setUsuallyScore("0");
					_erOne.setOnlineScore("0");
					examResultsService.saveOrUpdate(_erOne);
				}
				// 二补
				createMakeupList(tpc, es_two, so, course, _erOne, _isPass,saveUE,Double.valueOf(score));
				if(saveUE==null){
					_examResults = createExamResults(tpc, es_two, 3L, so, user, score, course, nowTime,1);
					_examResults.setExamAbnormity("0");
					_examResults.setWrittenScore("0");
					_examResults.setUsuallyScore("0");
					_examResults.setOnlineScore("0");
					examResultsService.saveOrUpdate(_examResults);
				}else  if(!Double.valueOf(score).equals(Double.valueOf(saveUE.getScore()))){
					_examResults = examResultsService.getByCondtition(so.getResourceid(), tpc.getResourceid(), es_two.getResourceid());
					_examResults.setIntegratedScore(score);
					examResultsService.saveOrUpdate(_examResults);
				}
			}
		}
		
		return _examResults;
	}

	/**
	 * 创建补考名单
	 * @param tpc
	 * @param es_one
	 * @param so
	 * @param course
	 * @param er
	 * @param _isPass
	 * @param saveUE
	 * @param score
	 */
	private void createMakeupList(TeachingPlanCourse tpc, ExamSub es_one,StudentInfo so,
			 Course course, ExamResults er, String _isPass,UniversalExam saveUE,double score) {
		StudentMakeupList sml = null;
		if(saveUE!=null){
			if(!((Double.valueOf(saveUE.getScore())>=60 && score>=60) 
					|| (Double.valueOf(saveUE.getScore())<60 && score<60))){
				sml = studentMakeupListService.getByCondition(so.getResourceid(), tpc.getResourceid(), es_one.getResourceid());
				sml.setIsPass(_isPass);
				studentMakeupListService.saveOrUpdate(sml);
			}
		}else{
			sml = new StudentMakeupList();
			sml.setCourse(course);
			sml.setExamResults(er);
			sml.setIsPass(_isPass);
			sml.setTeachingPlanCourse(tpc);
			sml.setStudentInfo(so);
			sml.setNextExamSubId(es_one.getResourceid());
			studentMakeupListService.saveOrUpdate(sml);
		}
	}

	/**
	 * 检导入统考成绩信息的准确性和合法性
	 * 
	 * @param course
	 * @param school
	 * @param studentName
	 * @param idCardNo
	 * @param studentNo
	 * @param examType
	 * @param UEMakeup
	 * @return
	 */
	private Map<String, Object> validateImportDatas(String score,String examDate,String whichTime,Course course,
			String school, String studentName, String idCardNo, String studentNo, String examType, String UEMakeup) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isPass = true;
		String msg = "";
		TeachingPlanCourse planCourse = null;
		ExamSub examSub = null;// 正考批次
		ExamSub examSub_one = null;// 一补批次
		ExamSub examSub_two = null;// 二补批次
		StudentInfo studentInfo = null;
		for(int k=0; k<1; k++){
			// 分数不能为空且为数字
			String regex = "^(\\d|[1-9]\\d|1[0-4][0-9]|150)(\\.\\d)?$";
			if(ExStringUtils.isEmpty(score)){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的成绩不能为空";
				continue;
			}
			if(!Pattern.matches(regex, score)) {
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的成绩不合法";
				continue;
			}
			// 日期不能为空且符合规定格式
			if(ExStringUtils.isEmpty(examDate)){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的考试日期不能为空";
				continue;
			}
			
			// 第几次不能为空且为数字
			regex = "^(\\d+)$";
			if(ExStringUtils.isEmpty(whichTime)){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的第几次考试不能为空";
				continue;
			}
			if("N".equals(examType) && !Pattern.matches(regex, whichTime)) {
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的第几次考试应为正整数";
				continue;
			}
			if("Y".equals(examType) && !"-1".equals(whichTime)){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生补考的第几次考试应为-1";
				continue;
			}
			// 判断该学生是否存在
			studentInfo = studentInfoService.findUniqueByProperty("studyNo", studentNo);
			if(studentInfo == null) {
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生不存在";
				continue;
			}
			// 判断名字是否有误
			if(!studentName.equals(studentInfo.getStudentName())) {
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的名字有误";
				continue;
			}
			// 判断是否有对应的基本信息
			if(studentInfo.getStudentBaseInfo() == null){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生没有对应的基本信息";
				continue;
			}
			// 判断身份证号是否正确
			if(!idCardNo.equalsIgnoreCase(studentInfo.getStudentBaseInfo().getCertNum())) {
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生的身份证号有误";
				continue;
			}
			// 判断该学生是否有教学计划
			TeachingPlan teachingPlan = studentInfo.getTeachingPlan();
			if(teachingPlan == null){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生没有教学计划";
				continue;
			}
			// 判断该学生的教学计划中是否有该门统考课程
			String tpHql = "from " + TeachingPlanCourse.class.getSimpleName() +" pc "
					+ "where pc.isDeleted=0 and pc.course.resourceid=? and pc.teachingPlan.resourceid=?";
			List<TeachingPlanCourse> tpCourseList =  teachingPlanCourseService.findByHql(tpHql, course.getResourceid(),
																														teachingPlan.getResourceid());
			if(tpCourseList == null || tpCourseList.isEmpty()){
				isPass = false;
				msg = "<font color='red'>[学号：" + studentNo + "]</font>该学生没有《"+course.getCourseName()+"》该门统考课程";
				continue;
			}
			//TODO:以后会判断该课程是否是统考类型（目前为了能从课程成绩录入那里录入成绩，类型为非统考）
			planCourse = tpCourseList.get(0);
			// 判断该课程是否已开课
			String guidePlanHql = "from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and teachingPlan.resourceid=? and grade.resourceid=?";
			List<TeachingGuidePlan> guidePlanList = teachingGuidePlanService.findByHql(guidePlanHql, teachingPlan.getResourceid()
																					,studentInfo.getGrade().getResourceid());
			if(guidePlanList != null && guidePlanList.size() > 0) {
				String courseStatusHql = "from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingPlanCourse.resourceid=?"
						+" and teachingGuidePlan.resourceid=? and schoolIds=? and isOpen='Y' and checkStatus!='cancelY' ";
				List<TeachingPlanCourseStatus> courseStatusList = teachingPlanCourseStatusService.findByHql(courseStatusHql,planCourse.getResourceid(),
						guidePlanList.get(0).getResourceid(), studentInfo.getBranchSchool().getResourceid());
				if(courseStatusList == null || courseStatusList.isEmpty()) {
					isPass = false;
					msg = "<font color='red'>[学号：" + studentNo + "]</font>教学点："+studentInfo.getBranchSchool().getUnitName()+"，专业："
							+studentInfo.getMajor().getMajorName()+"，《"+course.getCourseName()+"》该门课程还未开课";
					continue;
				}
				// 判断是否有对应的考试批次（正考）
				String term = courseStatusList.get(0).getTerm();
				String[] year_term = term.split("_");
				// 该课程开课年份
				Long _year = Long.parseLong(year_term[0]);
				// 开课学期
				String _term = year_term[1];
				_term = _term.substring(_term.length()-1);
//				if("N".equals(examType)){// 正考
				examSub = examSubService.findExamSubBycondition("N", _year, _term);
				if(examSub == null) {
					isPass = false;
					msg = "<font color='red'>[学号：" + studentNo + "]</font>没有\""+_year + "年第" + _term + "学期正考\"这个考试批次";
					continue;
				}
//				} else if("Y".equals(examType)){// 补考
				if("Y".equals(examType)){// 补考
					// 一补
					Long firstYear = studentInfo.getGrade().getYearInfo().getFirstYear();
					Long fy_year = _year-firstYear;
					Long t_year = 0L;
					String t_term = "1";
					if("2".equals(_term) && fy_year<2){
						t_year = _year + 1;
						t_term = "1";
					}else if(("2".equals(_term) && fy_year==2) || "1".equals(_term)){
						t_year = _year;
						t_term = "2";
					}
					examSub_one = examSubService.findExamSubBycondition("Y", t_year, t_term);
					if(examSub_one == null ){
						isPass = false;
						msg = "<font color='red'>[学号：" + studentNo + "]</font>没有\""+t_year + "年第" + t_term + "学期一补\"这个考试批次";
						continue;
					}
					if("T".equals(UEMakeup)){
						// 二补 TODO: 这是广大的考试规则，其他学校看情况而定
						if(fy_year<2){// 1-4学期
							t_year = firstYear + 2;
							t_term = "1";
						}else if(fy_year==2){
							t_year = _year;
							t_term = "2";
						}
						examSub_two = examSubService.findExamSubBycondition("T", t_year, t_term);
						if(examSub_two == null ){
							isPass = false;
							msg = "<font color='red'>[学号：" + studentNo + "]</font>没有\""+t_year + "年第" + t_term + "学期二补\"这个考试批次";
							continue;
						}
					}
				}
			}
		}
		resultMap.put("isPass", isPass);
		resultMap.put("msg", msg);
		if(isPass) {
			resultMap.put("studentInfo", studentInfo);
			resultMap.put("planCourse", planCourse);
			resultMap.put("examSub", examSub);
			resultMap.put("examSub_one", examSub_one);
			resultMap.put("examSub_two", examSub_two);
		}
		return resultMap;
	}

	@Override
	public List<UniversalExam> findByCondition(Map<String, Object> condition, String orderBy)
			throws ServiceException {
		StringBuffer singleUE_HQL = new StringBuffer("");
		singleUE_HQL.append("from " +UniversalExam.class.getSimpleName() + " ue where ue.isDeleted=0 ");
		if(condition.containsKey("planCourse")){
			singleUE_HQL.append(" and ue.teachingPlanCourse.resourceid=:planCourse ");
		}
		if(condition.containsKey("studentInfo")){
			singleUE_HQL.append(" and ue.studentInfo.resourceid=:studentInfo ");
		}
		if(condition.containsKey("examDate")){
			singleUE_HQL.append(" and ue.examDate=:examDate ");
		}
		if(condition.containsKey("whichTime")){
			singleUE_HQL.append(" and ue.whichTime=:whichTime ");
		}
		if(condition.containsKey("ne_examDate")){
			singleUE_HQL.append(" and ue.examDate !=:ne_examDate ");
		}
		if(ExStringUtils.isNotEmpty(orderBy)){
			singleUE_HQL.append(" order by "+orderBy+" ");
		} else {
			singleUE_HQL.append(" order by ue.examDate desc ");
		}
		
		return  findByHql(singleUE_HQL.toString(), condition);
	}
	
	@Override
	public List<UniversalExamVO>  findUniversalExamVO(Map<String,Object> condition) throws ServiceException{
		StringBuffer sql = new StringBuffer(""); 
		sql.append(" select ers.studyno,ers.studentname,ebco.coursename,hsu.unitname ");
		sql.append(" ,ebg.gradename,ebm.majorname,ebc.classicname classicName,erc.classesname ");
		sql.append(" ,etu.score,etu.certificateno,etu.whichtime ");
		sql.append(" from edu_teach_universalexam etu ");
		sql.append(" join edu_roll_studentinfo ers on ers.resourceid = etu.studentid ");
		sql.append(" join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
		sql.append(" join edu_base_classic ebc on ebc.resourceid = ers.classicid ");
		sql.append(" join edu_base_major ebm on ebm.resourceid = ers.majorid ");
		sql.append(" join edu_roll_classes erc on erc.resourceid = ers.classesid ");
		sql.append(" join edu_base_course ebco on ebco.resourceid = etu.courseid ");
		sql.append(" join hnjk_sys_unit hsu on hsu.resourceid = ers.branchschoolid ");
		sql.append(" where  etu.isDeleted=0 ");
		if(condition.containsKey("UEStudentName")){
			sql.append(" AND ers.studentname like  '%"+condition.get("UEStudentName")+"%' ");
		}
		if(condition.containsKey("UEStudyNo")){
			sql.append(" AND ers.studyno like '%"+condition.get("UEStudyNo")+"%' ");
		}
		if(condition.containsKey("UEGradeid")){
			sql.append(" and  ebg.resourceid=:UEGradeid ");
		}
		if(condition.containsKey("UEClassic")){
			sql.append(" and  ebc.resourceid=:UEClassic ");
		}
		if(condition.containsKey("UEMajor")){
			sql.append(" and  ebm.resourceid=:UEMajor ");
		}
		if(condition.containsKey("UEClassId")){
			sql.append(" and  erc.resourceid=:UEClassId ");
		}
		if(condition.containsKey("certificateNo")){
			sql.append(" and etu.certificateNo=:certificateNo ");
		}
		if(condition.containsKey("examDate")){
			sql.append(" and etu.examDate=to_date('"+condition.get("examDate")+"','yyyy-MM-dd')");
		}
		if(condition.containsKey("UECourseId")){
			sql.append(" and ebc.resourceid=:UECourseId ");
		}
		if(condition.containsKey("UESchool")){
			sql.append(" and  hsu.resourceid=:UESchool ");
		}
		if(condition.containsKey("isPass")){
			if(condition.get("isPass").equals(Constants.BOOLEAN_YES)){
				sql.append(" and to_number(etu.score)>=60 ");
			}else{
				sql.append(" and to_number(etu.score)<60 ");
			}
		}
		sql.append(" group by ers.studyno,ers.studentname,ebco.coursename,hsu.unitname,ebg.gradename,ebm.majorname,ebc.classicname,erc.classesname ");
		sql.append(" ,etu.score,etu.certificateno,etu.whichtime ");
		
		
		sql.append(" ORDER BY  hsu.unitname,ebg.gradename,ebc.classicname,ebm.majorname,erc.classesname, ers.studyno ");
		try {
			@SuppressWarnings("unchecked")
			List<UniversalExamVO> universalExamList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), UniversalExamVO.class, condition);
			return universalExamList;
		} catch (Exception e) {
			logger.error("根据查询条件获取统考成绩列表出错", e);
		}
		return null;
	}

	/**
	 * 更新统考成绩的当前成绩（正考）
	 * @param examResultId
	 * @param studentInfoId
	 * @param courseId
	 * @param planCourseId
	 * @return
	 */
	@Override
	public int updateExamResult(String examResultId, String studentInfoId, String courseId, String planCourseId) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("examResultId", examResultId);
			param.put("studentInfoId", studentInfoId);
			param.put("courseId", courseId);
			param.put("planCourseId", planCourseId);
			String sql = "update edu_teach_universalexam ue set ue.examresultsid=:examResultId  where ue.studentid=:studentInfoId and ue.courseid=:courseId and ue.plancourseid=:planCourseId and ue.whichtime!=-1 ";
			return baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, param);
		} catch (Exception e) {
			logger.error("删掉某个学生某门课程补考成绩出错", e);
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Page getUniversalExamList(Map<String,Object> condition,Page objPage) throws ServiceException{
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		if("N".equalsIgnoreCase(condition.get("examType").toString())){
			universalExamSQLN(condition, sql, param);
		}else{
			universalExamSQLNN(condition, sql, param);
		}
		
		Page page = baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(),param.toArray() , UniversalExamCountVO.class) ;
		//在查询时，如果该批次还没有创建课程比例，就给它创建
		//这样在导入时，就可以跳过创建了
		//TODO 这里直接只处理面授课程，网络课程以后有这个需求再改进
//		CreateExamInfoThread thread = new CreateExamInfoThread(page,condition);
//		Thread t1 = new Thread(thread);
//		t1.start();
		List<UniversalExamCountVO> voList = new ArrayList<UniversalExamCountVO>();
		voList.addAll((List<UniversalExamCountVO>)page.getResult());//注意不能用赋值
		String hql = "from "+ExamSub.class.getSimpleName()+" where isDeleted=0 and yearInfo.firstYear=? and term=? and examType=?";
		ExamSub es = examSubService.findUnique(hql, new Object[]{Long.parseLong(condition.get("_term").toString().substring(0, 4)),condition.get("_term").toString().substring(6, 7),condition.get("examType")});
		if(ExStringUtils.isNotBlank(es)){
			
//			String sql1 = "select ef.* from edu_teach_examinfo ef where ef.isdeleted=0 and ef.examsubid=?  and ef.examCourseType=? and ef.isMachineExam=?";
			String hql1 = " from "+ExamInfo.class.getSimpleName()+" where isDeleted=0 and examSub.resourceid=? and examCourseType=? and isMachineExam=?";
			List<Object> param1 = new ArrayList<Object>();
			param1.add(es.getResourceid());			
			param1.add(1);
			param1.add("N");
			try {
//				List<ExamInfo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql1,param1.toArray() , ExamInfo.class);
				List<ExamInfo> list =examInfoService.findByHql(hql1, param1.toArray());
				String _examSubId = es.getResourceid();
				if(ExCollectionUtils.isNotEmpty(list)){
					for(Iterator<UniversalExamCountVO> it = voList.iterator();it.hasNext();){
						UniversalExamCountVO vo = it.next();
						String _courseId=vo.getCourseid();
						for(ExamInfo ef:list){
							if(ef.getExamSub().getResourceid().equals(_examSubId)&&ef.getCourse().getResourceid().equals(_courseId)){
								it.remove();
								break;
							}
						}
						
					}
					if(ExCollectionUtils.isNotEmpty(voList)){//添加到examIno表
						List<ExamInfo> _list = new ArrayList<ExamInfo>();
						for(UniversalExamCountVO vo:voList){
							ExamInfo ef = new ExamInfo();
							ef.setCourse(courseService.get(vo.getCourseid()));	
							ef.setIsOutplanCourse("0");
							ef.setExamSub(es);					
							ef.setExamCourseType(1);
							ef.setIsMachineExam("N");
							ef.setCourseScoreType(es.getCourseScoreType());
							ef.setStudyScorePer(es.getFacestudyScorePer());
							ef.setFacestudyScorePer(es.getFacestudyScorePer());
							ef.setFacestudyScorePer2(es.getFacestudyScorePer2());
//							ef.setFacestudyScorePer3(es.getFacestudyScorePer3());
							_list.add(ef);
						}
						if(ExCollectionUtils.isNotEmpty(_list)){
							examInfoService.batchSaveOrUpdate(_list);
						}
					}
				}
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
		}
		return page;
	}

	private void universalExamSQLNN(Map<String, Object> condition,
			StringBuffer sql, List<Object> param) {
		sql.append(" select y.firstyear || '_0' || es.term term, ");
		sql.append("        es.examtype, ");
		sql.append("        c.coursename, ");
		sql.append("        pc.courseid, ");
		sql.append("        count(s.resourceid) stuCount, ");
		sql.append("        decode(count(s.resourceid) - count(r.resourceid), ");
		sql.append("               0, ");
		sql.append("               '-', ");
		sql.append("               count(s.resourceid) - count(r.resourceid)) wCount ");
		sql.append("   from edu_teach_makeuplist m ");
		sql.append("   join edu_roll_studentinfo s ");
		sql.append("     on s.resourceid = m.studentid ");
		sql.append("     and s.studentstatus = '11' ");
		if(condition.containsKey("stuGrade")){
			sql.append("    and s.gradeid=?               ");
			param.add(condition.get("stuGrade").toString());
		}
		if(condition.containsKey("stuClassic")){
			sql.append("    and s.classicid=?               ");
			param.add(condition.get("stuClassic").toString());
		}
		sql.append("  join hnjk_sys_unit u                          ");
		sql.append("    on u.resourceid = s.branchschoolid              ");
		if(condition.containsKey("unitId")){
			sql.append("    and u.resourceid=?               ");
			param.add(condition.get("unitId").toString());
		}
		sql.append("   join edu_teach_plancourse pc ");
		sql.append("     on pc.resourceid = m.plansourceid ");
		sql.append("    and pc.examclasstype = '3' ");
		sql.append("   join edu_base_course c ");
		sql.append("     on c.resourceid = pc.courseid ");
		if(condition.containsKey("courseId")){
			sql.append("    and c.resourceid=?               ");
			param.add(condition.get("courseId").toString());
		}
		sql.append("   join edu_teach_examsub es ");
		sql.append("     on es.resourceid = m.nextexamsubid ");
		sql.append("    and es.term = ? ");
		sql.append("    and es.examtype = ? ");
		sql.append("   join edu_base_year y ");
		sql.append("     on y.resourceid = es.yearid ");
		sql.append("    and y.firstyear = ? ");
		sql.append("   left join edu_teach_examresults r ");
		sql.append("     on r.studentid = s.resourceid ");
		sql.append("    and r.majorcourseid = pc.resourceid ");
		sql.append("    and r.ismakeupexam = es.examtype ");
		sql.append("    and r.isdeleted = 0 ");
		sql.append("    and r.checkstatus = '4' ");
		sql.append("  where m.isdeleted = 0 ");
		sql.append("  group by y.firstyear || '_0' || es.term, ");
		sql.append("           es.examtype, ");
		sql.append("           c.coursename, ");
		sql.append("           pc.courseid ");
		String year = condition.get("_term").toString().split("_0")[0];
		String term = condition.get("_term").toString().split("_0")[1];
		param.add(term);
		param.add(condition.get("examType").toString());
		param.add(Integer.valueOf(year));
	}

	private void universalExamSQLN(Map<String, Object> condition,
			StringBuffer sql, List<Object> param) {
		sql.append("select cs.term,                                 ");
		sql.append("       c.resourceid courseid,                   ");
		sql.append("       c.coursename,                            ");
		sql.append("       'N' examType,                ");
		sql.append("        decode(count(ne.resourceid), 0, '-', count(ne.resourceid)) neCount, ");
		sql.append("        decode(count(ae.resourceid), 0, '-', count(ae.resourceid)) aeCount, ");
		sql.append("        decode(count(s.studyno) - count(ne.resourceid) - ");
		sql.append("                count(r.resourceid), ");
		sql.append("               0, ");
		sql.append("               '-', ");
		sql.append("               count(s.studyno) - count(ne.resourceid)  - ");
		sql.append("               count(r.resourceid)) wCount, ");
		sql.append("        count(s.studyno) stuCount ");
		sql.append("  from edu_teach_coursestatus cs                ");
		sql.append("  join edu_teach_plancourse pc                  ");
		sql.append("    on pc.resourceid = cs.plancourseid          ");
		sql.append("   and pc.examclasstype = '3'                   ");
		sql.append("   and pc.isdeleted = 0                         ");
		sql.append("  join edu_base_course c                        ");
		sql.append("    on c.resourceid = pc.courseid               ");
		if(condition.containsKey("courseId")){
			sql.append("    and c.resourceid=?               ");
			param.add(condition.get("courseId").toString());
		}
		sql.append("  join hnjk_sys_unit u                          ");
		sql.append("    on u.resourceid = cs.schoolids              ");
		if(condition.containsKey("unitId")){
			sql.append("    and u.resourceid=?               ");
			param.add(condition.get("unitId").toString());
		}
		sql.append("  join edu_roll_studentinfo s                   ");
		sql.append("    on s.teachplanid = pc.planid                ");
		sql.append("   and s.branchschoolid = cs.schoolids          ");
		sql.append("   and s.isdeleted = 0                          ");
		sql.append("     and s.studentstatus = '11' ");
		sql.append("  join edu_base_grade g                         ");
		sql.append("    on g.resourceid = s.gradeid                 ");
		if(condition.containsKey("stuGrade")){
			sql.append("    and g.resourceid=?               ");
			param.add(condition.get("stuGrade").toString());
		}
		if(condition.containsKey("stuClassic")){
			sql.append("    and s.classicid=?               ");
			param.add(condition.get("stuClassic").toString());
		}
		sql.append("  join edu_base_major m                         ");
		sql.append("    on m.resourceid = s.majorid                 ");
		sql.append("  join edu_roll_classes cl                      ");
		sql.append("    on cl.resourceid = s.classesid              ");
		sql.append("  join edu_teach_guiplan gp                     ");
		sql.append("    on gp.planid = pc.planid                    ");
		sql.append("   and cs.guiplanid = gp.resourceid             ");
		sql.append("   and gp.isdeleted = 0                         ");
		sql.append("   and gp.gradeid = s.gradeid                   ");
		sql.append("   left join edu_teach_noexam ne ");
		sql.append("     on ne.studentid = s.resourceid ");
		sql.append("    and ne.courseid = c.resourceid ");
		sql.append("    and ne.checkstatus = '1' ");
		sql.append("    and ne.isdeleted = 0 ");
		sql.append("   left join edu_teach_abnormalexam ae ");
		sql.append("     on ae.studentid = s.resourceid ");
		sql.append("    and ae.plancourseid = pc.resourceid ");
		sql.append("    and ae.checkstatus = '1' ");
		sql.append("    and ae.isdeleted = 0 ");
		sql.append("   left join edu_teach_examresults r ");
		sql.append("     on r.studentid = s.resourceid ");
		sql.append("    and r.majorcourseid = pc.resourceid ");
		sql.append("    and r.checkstatus = '4' ");
		sql.append("    and r.ismakeupexam = 'N' ");
		sql.append("    and r.isdeleted = 0 ");
		sql.append(" where cs.openstatus = 'Y'                      ");
		sql.append("   and cs.isdeleted = 0                         ");
		sql.append("   and cs.isopen = 'Y'                          ");
		sql.append("   and cs.term = ?                      ");
		sql.append(" group by c.resourceid, cs.term, c.coursename   ");
		sql.append(" order by c.coursename                          ");
		
		param.add(condition.get("_term").toString());
	}
	@Override
	public Page getUniversalExamDetails(Map<String,Object> condition,Page objPage) throws ServiceException{
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		if("N".equalsIgnoreCase(condition.get("examType").toString())){
			universalExamDetailsN(condition, sql, param);
		}else{
			universalExamDetailsNN(condition, sql, param);
		}
		
		Page page = baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(),param.toArray() , UniversalExamDetailsVO.class) ;
		return page;
	}

	private void universalExamDetailsNN(Map<String, Object> condition,
			StringBuffer sql, List<Object> param) {
		sql.append(" select y.firstyear || '_0' || es.term term, ");
		sql.append("        es.examtype, ");
		sql.append("        c.coursename, ");
		sql.append("        s.studyno, ");
		sql.append("        s.studentname, ");
		sql.append("        s.resourceid studentid, ");
		sql.append("        g.gradename, ");
		sql.append("        u.unitname, ");
		sql.append("        ci.classicname, ");
		sql.append("        cl.classesname, ");
		sql.append("        mj.majorname, ");
		sql.append("        r.usuallyscore, ");
		sql.append("        r.integratedscore, ");
		sql.append("        r.writtenscore, ");
		sql.append("        c.resourceid courseId, ");
		sql.append("        pc.resourceid planCourseId, ");
		sql.append("        sp.resourceid studentLearnPlanId, ");
		sql.append("        ef.resourceid examInfoId, ");
		sql.append("        es.resourceid examSubId, ");
		sql.append("        us.resourceid universalExamId, ");
		sql.append("        r.resourceid examResultId, ");
		sql.append("        r.examcount ");
		sql.append("   from edu_teach_makeuplist m ");
		sql.append("   join edu_roll_studentinfo s ");
		sql.append("     on s.resourceid = m.studentid ");
		sql.append("     and s.studentstatus = '11' ");
		if(condition.containsKey("studyNo")){
			sql.append("    and s.studyNo like ?               ");
			param.add("%"+condition.get("studyNo").toString()+"%");
		}
		if(condition.containsKey("studentName")){
			sql.append("    and s.studentName like ?               ");
			param.add("%"+condition.get("studentName").toString()+"%");
		}
		sql.append("   join edu_teach_plancourse pc ");
		sql.append("     on pc.resourceid = m.plansourceid ");
		sql.append("    and pc.examclasstype = '3' ");
		sql.append("   join edu_base_course c ");
		sql.append("     on c.resourceid = pc.courseid");
		if(condition.containsKey("courseId")){
			sql.append("    and c.resourceid=?               ");
			param.add(condition.get("courseId").toString());
		}		
		sql.append("   join edu_teach_examsub es ");
		sql.append("     on es.resourceid = m.nextexamsubid ");
		sql.append("    and es.term = ? ");
		sql.append("    and es.examtype = ? ");
		sql.append("   join edu_base_year y ");
		sql.append("     on y.resourceid = es.yearid ");
		sql.append("    and y.firstyear = ? ");
		sql.append("   join edu_base_grade g ");
		sql.append("     on g.resourceid = s.gradeid ");
		if(condition.containsKey("stuGrade")){
			sql.append("    and g.resourceid=?               ");
			param.add(condition.get("stuGrade").toString());
		}
		sql.append("   join edu_base_classic ci ");
		sql.append("     on ci.resourceid = s.classicid ");
		if(condition.containsKey("stuClassic")){
			sql.append("    and s.classicid=?               ");
			param.add(condition.get("stuClassic").toString());
		}
		sql.append("   join edu_base_major mj ");
		sql.append("     on mj.resourceid = s.majorid ");
		sql.append("   join hnjk_sys_unit u ");
		sql.append("     on u.resourceid = s.branchschoolid ");
		String year = condition.get("_term").toString().split("_0")[0];
		String term = condition.get("_term").toString().split("_0")[1];
		param.add(term);
		param.add(condition.get("examType").toString());
		param.add(Integer.valueOf(year));
		if(condition.containsKey("unitId")){
			sql.append("    and u.resourceid=?               ");
			param.add(condition.get("unitId").toString());
		}
		sql.append("   join edu_roll_classes cl ");
		sql.append("     on cl.resourceid = s.classesid ");
		sql.append("   left join edu_teach_examresults r ");
		sql.append("     on r.studentid = s.resourceid ");
		sql.append("    and r.majorcourseid = pc.resourceid ");
		sql.append("    and r.ismakeupexam = es.examtype ");
		sql.append("    and r.isdeleted = 0 ");
		sql.append("    and r.checkstatus = '4' ");
		sql.append("   left join edu_teach_universalexam us ");
		sql.append("     on us.studentid = s.resourceid ");
		sql.append("    and us.plancourseid = pc.resourceid ");
		sql.append("    and us.isdeleted = 0 ");
		sql.append("   left join edu_teach_examinfo ef ");
		sql.append("     on ef.resourceid = r.examinfoid ");
		sql.append("    and ef.isdeleted = 0 ");
		sql.append("   left join edu_learn_stuplan sp ");
		sql.append("     on s.resourceid = sp.studentid ");
		sql.append("    and sp.plansourceid = pc.resourceid ");
		sql.append("    and sp.isdeleted = 0 ");
		sql.append("  where m.isdeleted = 0 ");
//		if(condition.containsKey("courseid")) param.add(condition.get("courseid").toString());
		
	}

	private void universalExamDetailsN(Map<String, Object> condition,
			StringBuffer sql, List<Object> param) {
		sql.append(" select cs.term, ");
		sql.append("        c.coursename, ");
		sql.append("        s.studyno, ");
		sql.append("        g.gradename, ");
		sql.append("        m.majorname, ");
		sql.append("        cl.classesname, ");
		sql.append("        cs.resourceid, ");
		sql.append("        u.unitname, ");
		sql.append("        ci.classicname, ");
		sql.append("        s.studentname, ");
		sql.append("        s.resourceid studentid, ");
		sql.append("        'N' examType, ");
		sql.append("        r.usuallyscore, ");
		sql.append("        r.integratedscore, ");
		sql.append("        r.writtenscore, ");
		sql.append("        ae.abnormaltype, ");
		sql.append("        ne.unScore, ");
		sql.append("        c.resourceid courseId, ");
		sql.append("        pc.resourceid planCourseId, ");
		sql.append("        sp.resourceid studentLearnPlanId, ");
		sql.append("        ef.resourceid examInfoId, ");
		sql.append("        es.resourceid examSubId, ");
		sql.append("        ae.resourceid abnormalExamId, ");
		sql.append("        ne.resourceid noExamId, ");
		sql.append("        us.resourceid universalExamId, ");
		sql.append("        r.resourceid examResultId, ");
		sql.append("        r.examcount ");
		sql.append("   from edu_teach_coursestatus cs ");
		sql.append("   join edu_teach_plancourse pc ");
		sql.append("     on pc.resourceid = cs.plancourseid ");
		sql.append("    and pc.examclasstype = '3' ");
		sql.append("    and pc.isdeleted = 0 ");
		sql.append("   join edu_base_course c ");
		sql.append("     on c.resourceid = pc.courseid ");
		if(condition.containsKey("courseId")){
			sql.append("    and c.resourceid=?               ");
			param.add(condition.get("courseId").toString());
		}		
		sql.append("   join hnjk_sys_unit u ");
		sql.append("     on u.resourceid = cs.schoolids ");
		if(condition.containsKey("unitId")){
			sql.append("    and u.resourceid=?               ");
			param.add(condition.get("unitId").toString());
		}
		sql.append("   join edu_roll_studentinfo s ");
		sql.append("     on s.teachplanid = pc.planid ");
		sql.append("    and s.branchschoolid = cs.schoolids ");
		sql.append("    and s.isdeleted = 0 ");
		sql.append("     and s.studentstatus = '11' ");
		if(condition.containsKey("studyNo")){
			sql.append("    and s.studyNo like ?               ");
			param.add("%"+condition.get("studyNo").toString()+"%");
		}
		if(condition.containsKey("studentName")){
			sql.append("    and s.studentName like ?               ");
			param.add("%"+condition.get("studentName").toString()+"%");
		}
		sql.append("   join edu_base_grade g ");
		sql.append("     on g.resourceid = s.gradeid ");
		if(condition.containsKey("stuGrade")){
			sql.append("    and g.resourceid=?               ");
			param.add(condition.get("stuGrade").toString());
		}
		sql.append("   join edu_base_classic ci ");
		sql.append("     on ci.resourceid = s.classicid ");
		if(condition.containsKey("stuClassic")){
			sql.append("    and s.classicid=?               ");
			param.add(condition.get("stuClassic").toString());
		}
		sql.append("   join edu_base_major m ");
		sql.append("     on m.resourceid = s.majorid ");
		sql.append("   join edu_roll_classes cl ");
		sql.append("     on cl.resourceid = s.classesid ");
		sql.append("   join edu_teach_guiplan gp ");
		sql.append("     on gp.planid = pc.planid ");
		sql.append("    and cs.guiplanid = gp.resourceid ");
		sql.append("    and gp.isdeleted = 0 ");
		sql.append("    and gp.gradeid = s.gradeid ");
		sql.append("   left join edu_teach_universalexam us ");
		sql.append("     on us.studentid = s.resourceid ");
		sql.append("    and us.plancourseid = pc.resourceid ");
		sql.append("    and us.isdeleted =0 ");
		sql.append("   left join edu_teach_examresults r ");
		sql.append("     on r.studentid = s.resourceid ");
		sql.append("    and r.majorcourseid = pc.resourceid ");
		sql.append("    and r.ismakeupexam = 'N' ");
		sql.append("    and r.checkstatus = '4' ");
		sql.append("    and r.isdeleted = 0 ");
		sql.append("   left join edu_teach_examsub es ");
		sql.append("     on es.resourceid = r.examsubid ");
		sql.append("     and es.isdeleted = 0 ");
		sql.append("   left join edu_teach_examinfo ef ");
		sql.append("     on ef.resourceid = r.examinfoid ");
		sql.append("     and ef.isdeleted = 0 ");
		sql.append("   left join edu_teach_noexam ne ");
		sql.append("     on ne.studentid = s.resourceid ");
		sql.append("    and ne.courseid = c.resourceid ");
		sql.append("    and ne.checkstatus = '1' ");
		sql.append("    and ne.isdeleted = 0 ");
		sql.append("   left join edu_teach_abnormalexam ae ");
		sql.append("     on ae.studentid = s.resourceid ");
		sql.append("    and ae.plancourseid = pc.resourceid ");
		sql.append("    and ae.checkstatus = '1' ");
		sql.append("    and ae.isdeleted = 0 ");
		sql.append("   left join edu_learn_stuplan sp ");
		sql.append("     on s.resourceid = sp.studentid ");
		sql.append("    and sp.plansourceid = pc.resourceid ");
		sql.append("    and sp.isdeleted =0 ");
		sql.append("   left join edu_base_year y ");
		sql.append("     on y.resourceid = sp.yearid ");
		sql.append("    and y.firstyear = substr(cs.term, 0, 4) ");
		sql.append("    and sp.term = substr(cs.term, 7, 1) ");
		sql.append("  where cs.openstatus = 'Y' ");
		sql.append("    and cs.isdeleted = 0 ");
		sql.append("    and cs.isopen = 'Y' ");
		sql.append("    and cs.term =? ");
		sql.append("  order by c.coursename, s.studyno  ");		
		param.add(condition.get("_term").toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UniversalExamDetailsVO> getUniversalExamDetails(Map<String,Object> condition) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		if("N".equalsIgnoreCase(condition.get("examType").toString())){
			universalExamDetailsN(condition, sql, param);
		}else{
			universalExamDetailsNN(condition, sql, param);
		}
		
		List<UniversalExamDetailsVO> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), param.toArray(), UniversalExamDetailsVO.class) ;
		return list;
	}
	@Override
	public List<UniversalExamDetailsVO> handleUniversalExamDetails(Map<String,Object> condition,List<UniversalExamDetailsVO> voList) throws Exception{
		List<UniversalExamDetailsVO> failList = new ArrayList<UniversalExamDetailsVO>();
		try {
			final String EmptyScore="成绩为空，跳过不导入";
			List<UniversalExamDetailsVO> queries=getUniversalExamDetails(condition);//通过查询，把所有当前批次要导入的数据都查询出来
			//查询出来考试批次
			String hql = "from "+ExamSub.class.getSimpleName()+" where isDeleted=0 and yearInfo.firstYear=? and term=? and examType=?";
			ExamSub es = examSubService.findUnique(hql, new Object[]{Long.parseLong(condition.get("_term").toString().substring(0, 4)),condition.get("_term").toString().substring(6, 7),condition.get("examType")});
			User _user = SpringSecurityHelper.getCurrentUser();
			List<UniversalExam> saveUEList = new ArrayList<UniversalExam>();
			List<ExamResults> saveEList = new ArrayList<ExamResults>();
			List<ExamResults> makeupList = new ArrayList<ExamResults>();
//			List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
			List<StudentLearnPlan> stuLearnList = new ArrayList<StudentLearnPlan>();
			if(ExCollectionUtils.isNotEmpty(queries)){
				for(UniversalExamDetailsVO query:queries){
					String studyNo = query.getStudyNo();
					String courseName = query.getCourseName();
					for(Iterator<UniversalExamDetailsVO> it = voList.iterator();it.hasNext();){//遍历要导入的数据
						UniversalExamDetailsVO vo = it.next();
						String input_s= vo.getStudyNo();
						String input_c =vo.getCourseName();
						if(studyNo.equals(input_s)&&courseName.equals(input_c)){//按照课程名称、学号进行查找对比
							if(ExStringUtils.isNotBlank(query.getExamResultId())){//已有成绩
								vo.setMemo(UniversalExamDetailsVO.MEMO_EXAMRESULT);
								failList.add(vo);
								it.remove();
								break;
							}else if(ExStringUtils.isNotBlank(query.getNoExamId())){//已有免考
								vo.setMemo(UniversalExamDetailsVO.MEMO_NOEXAM);
								failList.add(vo);
								it.remove();
								break;
							}else if(ExStringUtils.isNotBlank(query.getAbnormalExamId())){//已申请缓考
								vo.setMemo(UniversalExamDetailsVO.MEMO_ABNORMALEXAM);
								failList.add(vo);
								it.remove();
								break;
							}else{//插入成绩：
								StudentLearnPlan sl = new StudentLearnPlan();
								if(ExStringUtils.isNotBlank(query.getStudentLearnPlanId())){
									sl = studentLearnPlanService.get(query.getStudentLearnPlanId());
								}else{//学生未创建学习计划
									vo.setMemo(UniversalExamDetailsVO.MEMO_NOSTUDYPLAN);
									failList.add(vo);
									it.remove();
									break;
								}							
								//1 成绩表
								ExamResults er = new ExamResults();
								StudentInfo _stu = studentInfoService.get(query.getStudentId());
								Course course = courseService.get(query.getCourseId());
								TeachingPlanCourse planCourse = teachingPlanCourseService.get(query.getPlanCourseId());
								String isMachineExam = Constants.BOOLEAN_NO;
								Integer examCourseType = "facestudy".equals(planCourse.getTeachType())?1:2;
								
								ExamInfo examInfo = examInfoService.saveExamInfo(es.getFacestudyScorePer(), es, course, examCourseType, isMachineExam);
								//增加 100：0 成绩比例0列允许为空
								double writePer = examInfo.getExamCourseType()==0?examInfo.getStudyScorePer():examInfo.getFacestudyScorePer();
								double usuallyPer = examInfo.getExamCourseType()==0?examInfo.getNetsidestudyScorePer():examInfo.getFacestudyScorePer2();
								if(writePer!=0.0 && ExStringUtils.isBlank(vo.getWrittenScore())){
									vo.setMemo(EmptyScore);
									failList.add(vo);
									it.remove();
									break;
								}
								if(usuallyPer!=0.0 && ExStringUtils.isBlank(vo.getUsuallyScore())){
									vo.setMemo(EmptyScore);
									failList.add(vo);
									it.remove();
									break;
								}
								List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
								Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
								for(Dictionary d : resultCalculateRuleList) {
									resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
								}
								String integratedScore = vo.getWrittenScore();
								String writtenScore = vo.getWrittenScore();
								String usuallyScore = vo.getUsuallyScore();
								String abnormity ="0";//0 正常   2-缺考
								er.setStudentInfo(_stu);
								er.setCourse(course);
								er.setCourseScoreType("11");
								er.setMajorCourseId(query.getPlanCourseId());
								er.setExamInfo(examInfo);
								Long examCount = 1L;
								er.setCheckStatus("4");
								if("N".equals(condition.get("examType"))){//正考跟补考有区别，补考不需要平时成绩，卷面成绩即综合成绩
									if(integratedScore.trim().startsWith("缺")){
										writtenScore="0";
										integratedScore="0";
										usuallyScore="0";
										abnormity = "2";
									}else{
										integratedScore=examResultsService.caculateIntegrateScore(examInfo, vo.getUsuallyScore(), vo.getWrittenScore(), "0", er,resultCalculateRuleMap);
									}
									er.setUsuallyScore(usuallyScore);
									
								}else{
									if(integratedScore.trim().startsWith("缺")){
										integratedScore ="0";
										abnormity = "2";
										writtenScore="0";
									}else{
										integratedScore=vo.getWrittenScore();
									}
									examCount = ExStringUtils.isBlank(query.getExamCount())?1L+1:query.getExamCount()+1L;
								}
								er.setExamAbnormity(abnormity);
								er.setWrittenScore(writtenScore);
								er.setExamCount(examCount);
								er.setIntegratedScore(integratedScore);//通过计算得出
								er.setExamsubId(es.getResourceid());
								er.setAuditDate(new Date());
								er.setAuditMan(_user.getCnName());
								er.setAuditManId(_user.getResourceid());
								er.setExamType(0);
								er.setStydyHour(planCourse.getFaceStudyHour().intValue());
								er.setIsMakeupExam(query.getExamType());
								er.setPlanCourseTeachType("facestudy");
								saveEList.add(er);
								String isPass="";
								if(ExNumberUtils.isNumber(integratedScore)){
									isPass=Double.parseDouble(integratedScore)>=60D?"Y":"N";
								}else{
									if("2".equals(abnormity)){
										isPass="N";
									}								
								}
								
								sl.setFinalScore(integratedScore);
								sl.setExamResults(er);
								sl.setExamInfo(examInfo);
								sl.setIsPass(isPass);
								stuLearnList.add(sl);
								UniversalExam ue = new UniversalExam();
								ue.setCheckStatus("4");
								ue.setOperateDate(new Date());
								ue.setOperatorName(_user.getCnName());				
								ue.setOperator(_user);
								ue.setStudentInfo(_stu);
								ue.setCourse(course);
								ue.setTeachingPlanCourse(planCourse);
								ue.setExamResults(er);
								saveUEList.add(ue);
								if("N".equalsIgnoreCase(isPass)){//不及格，生成补考名单
									makeupList.add(er);//这里不能直接生成补考名单，必须要先保存成绩表之后，再生成补考名单
//									examResultsService.handleMadeUp(new Date(),_user,er);
								}
								it.remove();
								break;
							}
						}
					}
				}
			}
			examResultsService.batchSaveOrUpdate(saveEList);
			batchSaveOrUpdate(saveUEList);
			studentLearnPlanService.batchSaveOrUpdate(stuLearnList);
			if(ExCollectionUtils.isNotEmpty(makeupList)){
				for(ExamResults e:makeupList){
					examResultsService.handleMadeUp(new Date(),_user,e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("导入成绩业务逻辑出错，请联系管理员");
		}
		return failList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UniversalExamCountVO> getUniversalExamCount(Map<String,Object> condition) throws ServiceException{
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<UniversalExamCountVO> returnList = new ArrayList<UniversalExamCountVO>();
		if("N".equalsIgnoreCase(condition.get("examType").toString())){
			universalExamCountSQLN(condition, sql, param);
		}else{
			universalExamCountSQLNN(condition, sql, param);
		}
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), param.toArray(), UniversalExamCountVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}
	
	private void universalExamCountSQLN(Map<String, Object> condition,
			StringBuffer sql, List<Object> param) {
		sql.append(" select cs.term, ");
		sql.append("        c.resourceid courseid, ");
		sql.append("        g.gradename, ");
		sql.append("        cc.classicname, ");
		sql.append("        c.coursename, ");
		sql.append("        u.unitname,        ");
		sql.append("        m.majorname,        ");
		sql.append("       'N' examType,                ");
		sql.append("        count(s.studyno) - count(ne.resourceid) - count(ae.resourceid) - ");
		sql.append("        count(r.resourceid) wCount ");
		sql.append("  from edu_teach_coursestatus cs                ");
		sql.append("  join edu_teach_plancourse pc                  ");
		sql.append("    on pc.resourceid = cs.plancourseid          ");
		sql.append("   and pc.examclasstype = '3'                   ");
		sql.append("   and pc.isdeleted = 0                         ");
		sql.append("  join edu_base_course c                        ");
		sql.append("    on c.resourceid = pc.courseid               ");
		if(condition.containsKey("courseId")){
			sql.append("    and c.resourceid=?               ");
			param.add(condition.get("courseId").toString());
		}
		sql.append("  join hnjk_sys_unit u                          ");
		sql.append("    on u.resourceid = cs.schoolids              ");
		if(condition.containsKey("unitId")){
			sql.append("    and u.resourceid=?               ");
			param.add(condition.get("unitId").toString());
		}
		sql.append("  join edu_roll_studentinfo s                   ");
		sql.append("    on s.teachplanid = pc.planid                ");
		sql.append("   and s.branchschoolid = cs.schoolids          ");
		sql.append("   and s.isdeleted = 0                          ");
		sql.append("     and s.studentstatus = '11' ");
		sql.append("  join edu_base_grade g                         ");
		sql.append("    on g.resourceid = s.gradeid                 ");
		if(condition.containsKey("stuGrade")){
			sql.append("    and g.resourceid=?               ");
			param.add(condition.get("stuGrade").toString());
		}
		sql.append("  join edu_base_classic cc                      ");
		sql.append("    on cc.resourceid = s.classicid              ");
		if(condition.containsKey("stuClassic")){
			sql.append("    and s.classicid=?               ");
			param.add(condition.get("stuClassic").toString());
		}
		sql.append("  join edu_base_major m                         ");
		sql.append("    on m.resourceid = s.majorid                 ");
		sql.append("  join edu_roll_classes cl                      ");
		sql.append("    on cl.resourceid = s.classesid              ");
		
		sql.append("  join edu_teach_guiplan gp                     ");
		sql.append("    on gp.planid = pc.planid                    ");
		sql.append("   and cs.guiplanid = gp.resourceid             ");
		sql.append("   and gp.isdeleted = 0                         ");
		sql.append("   and gp.gradeid = s.gradeid                   ");
		sql.append("   left join edu_teach_noexam ne ");
		sql.append("     on ne.studentid = s.resourceid ");
		sql.append("    and ne.courseid = c.resourceid ");
		sql.append("    and ne.checkstatus = '1' ");
		sql.append("    and ne.isdeleted = 0 ");
		sql.append("   left join edu_teach_abnormalexam ae ");
		sql.append("     on ae.studentid = s.resourceid ");
		sql.append("    and ae.plancourseid = pc.resourceid ");
		sql.append("    and ae.checkstatus = '1' ");
		sql.append("    and ae.isdeleted = 0 ");
		sql.append("   left join edu_teach_examresults r ");
		sql.append("     on r.studentid = s.resourceid ");
		sql.append("    and r.majorcourseid = pc.resourceid ");
		sql.append("    and r.checkstatus = '4' ");
		sql.append("    and r.ismakeupexam = 'N' ");
		sql.append("    and r.isdeleted = 0 ");
		sql.append(" where cs.openstatus = 'Y'                      ");
		sql.append("   and cs.isdeleted = 0                         ");
		sql.append("   and cs.isopen = 'Y'                          ");
		sql.append("   and cs.term = ?                      ");
		sql.append("  group by  c.resourceid, cs.term, g.gradename,cc.classicname, m.majorname,c.coursename,u.unitcode,u.unitname ");
		sql.append("  order by  cs.term, g.gradename,cc.classicname,c.coursename, m.majorname,u.unitcode, u.unitname ");
		
		param.add(condition.get("_term").toString());
	}
	
	private void universalExamCountSQLNN(Map<String, Object> condition,
			StringBuffer sql, List<Object> param) {
		sql.append(" select y.firstyear || '_0' || es.term term, ");
		sql.append("        es.examtype, ");
		sql.append("        g.gradename, ");
		sql.append("        cc.classicname, ");
		sql.append("        mj.majorname, ");
		sql.append("        u.unitname, ");
		sql.append("        c.coursename, ");
		sql.append("        pc.courseid, ");
		sql.append("        count(s.resourceid) - count(r.resourceid) wCount ");
		sql.append("   from edu_teach_makeuplist m ");
		sql.append("   join edu_roll_studentinfo s ");
		sql.append("     on s.resourceid = m.studentid ");
		sql.append("     and s.studentstatus = '11' ");
		sql.append("   join edu_base_grade g ");
		sql.append("     on g.resourceid = s.gradeid ");
		if(condition.containsKey("stuGrade")){
			sql.append("    and g.resourceid=?               ");
			param.add(condition.get("stuGrade").toString());
		}
		sql.append("   join edu_base_classic cc ");
		sql.append("     on cc.resourceid = s.classicid ");
		if(condition.containsKey("stuClassic")){
			sql.append("    and s.classicid=?               ");
			param.add(condition.get("stuClassic").toString());
		}
		sql.append("   join hnjk_sys_unit u ");
		sql.append("     on u.resourceid = s.branchschoolid ");
		if(condition.containsKey("unitId")){
			sql.append("    and u.resourceid=?               ");
			param.add(condition.get("unitId").toString());
		}
		sql.append("   join edu_base_major mj ");
		sql.append("     on mj.resourceid = s.majorid ");
		sql.append("   join edu_teach_plancourse pc ");
		sql.append("     on pc.resourceid = m.plansourceid ");
		sql.append("    and pc.examclasstype = '3' ");
		sql.append("   join edu_base_course c ");
		sql.append("     on c.resourceid = pc.courseid ");
		if(condition.containsKey("courseId")){
			sql.append("    and c.resourceid=?               ");
			param.add(condition.get("courseId").toString());
		}
		sql.append("   join edu_teach_examsub es ");
		sql.append("     on es.resourceid = m.nextexamsubid ");
		sql.append("    and es.term = ? ");
		sql.append("    and es.examtype = ? ");
		sql.append("   join edu_base_year y ");
		sql.append("     on y.resourceid = es.yearid ");
		sql.append("    and y.firstyear = ? ");
		sql.append("   left join edu_teach_examresults r ");
		sql.append("     on r.studentid = s.resourceid ");
		sql.append("    and r.majorcourseid = pc.resourceid ");
		sql.append("    and r.ismakeupexam = es.examtype ");
		sql.append("    and r.isdeleted = 0 ");
		sql.append("    and r.checkstatus = '4' ");
		sql.append("  where m.isdeleted = 0 ");
		sql.append("  group by y.firstyear || '_0' || es.term, ");
		sql.append("           es.examtype, ");
		sql.append("           c.coursename, ");
		sql.append("           pc.courseid, ");
		sql.append("           es.resourceid, ");
		sql.append("           g.gradename,cc.classicname,mj.majorname,u.unitcode, ");
		sql.append("           u.unitname ");
		sql.append("  order by y.firstyear || '_0' || es.term, ");
		sql.append("           es.examtype, ");
		sql.append("           g.gradename,cc.classicname,c.coursename，mj.majorname,u.unitcode, ");
		sql.append("           u.unitname ");
		
		
		String year = condition.get("_term").toString().split("_0")[0];
		String term = condition.get("_term").toString().split("_0")[1];
		param.add(term);
		param.add(condition.get("examType").toString());
		param.add(Integer.valueOf(year));
	}
}
