package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
/**
 * 
 * <code>学生成绩统计、列表Service接口</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-5-18 上午10:50:50
 * @see 
 * @version 1.0
 */
public interface IStudentExamResultsService extends IBaseService<ExamResults>{
	/**
	 * 获取学生的所有成绩
	 * @param studentInfo
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentExamResultsVo> studentExamResultsList(StudentInfo studentInfo,Integer examCount)throws  Exception;
	/**
	 * 获取学生的所有成绩:
	 W0519A2：【查询与打印成绩单】打印成绩单修改
	 * @param studentInfo 学生信息
	 * @param flag 自定义参数： print:打印   export:导出   audit:审核  other：其它   
	 * @param examCount 选修课次数-汕大
	 * 打印和导出成绩单，学期取教学计划原定学期，否则取课程开课学期
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentExamResultsVo> studentExamResultsList(StudentInfo studentInfo,String flag,Integer examCount)throws  Exception;
	
	/**
	 * 获取学生成绩
	 * @param condition
	 * @return
	 */
	//public List<Map<String, Object>> findExamResults(Map<String, Object> condition);
	
	/**
	 * 获取学生所有发布的有效成绩
	 * @param studentInfo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> studentPublishedExamResultsList(Map<String,Object> condition) ;
	
	/**
	 * 对查询出来的成绩列表进行过滤并取出同一科目最大成绩(通过的)
	 * @param studentInfo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> sortExamResultList(List<Map<String, Object>> resultsMap) ;
	/**
	 * 获取学生已获取的学分
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public StudentExamResultsVo getStudentFinishedCreditHour(StudentInfo stu)throws Exception;
	
	/**
	 * 获取学生已获得的学分.
	 * @param stu
	 * @param resultsMap
	 * @param scl
	 * @param ncl
	 * @param planCourseList
	 * @return
	 * @throws Exception
	 */
	public StudentExamResultsVo getStudentFinishedCreditHour(StudentInfo stu,List<Map<String, Object>>  resultsMap,
									List<Map<String,Object>> scl,List<Map<String,Object>> ncl,
									List<TeachingPlanCourse> planCourseList) throws Exception;
	/**
	 * 获取学生指定课程的最大成绩
	 * @param stu
	 * @param planCourseId
	 * @param isPass
	 * @return
	 * @throws Exception
	 */
	public ExamResults getStudentPlanCourseMaxExamResults(StudentInfo stu,String courseId,boolean isPass)throws ServiceException;
	
	/**
	 * 传入一个成绩判断该成绩是否通过
	 * @param code
	 * @param score
	 * @return
	 */
	public 	 String isPassScore(String code, double score);
	
	/**
	 * 成绩类型转换成百分制
	 * @param code
	 * @param score
	 * @return
	 */
	public String convertScore(String code, String score);
	
	/**
	 * 非百分制成绩转换成字符类型
	 * @param code
	 * @param score
	 * @return
	 */
	public String convertScoreStr(String code,String score);
	
	public List<Map<String, Object>> studentToGraduatePublishedExamResultsList(Map<String, Object> condition);
	public List<Map<String, Object>> studentToGraduateNoExamResultsList(Map<String, Object> condition); 
	/**
	 *  获学生通过的成绩(最高成绩)
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,ExamResults> getStudentPassExamResulst(String studentId) throws ServiceException;
	
	/**
	 * 计算学生已获取的总学分数等相关数据
	 * @return
	 * @throws ServiceException
	 */
	public String calculateTotalCreditHour() throws Exception;
	
	/**
	 * 毕业前计算学分
	 * @param stu
	 * @return
	 * @throws Exception
	 */
	public String graduationCalculateTotalCreditHour(List<StudentInfo> stus) throws Exception;
	/**
	 * 获取最大的补考成绩
	 * @param condition
	 * @return
	 */
	public String getMaxBKExamResult(Map<String, Object> condition);
	/**
	 * 获取结补成绩
	 * @param condition
	 * @return
	 */
	public String getJBExamResult(Map<String, Object> condition);
	/**
	 * 获取成绩
	 * 1、显示正考成绩
	 * 2、如果申请了免修免考则显示：免+成绩（默认75分）
	 * 3、如果是缓考，并且有一补成绩则显示：缓考+一补成绩；否则显示缓考
	 * @param condition
	 * @return
	 */
	public String getExamResult(Map<String, Object> condition);
	
	/**
	 * 获取正考的综合成绩（未处理和处理后）
	 * @param condition
	 * @return
	 */
	public Map<String,Object> getNormalExamResult(Map<String, Object> condition);
	
	/**
	 * 获取最大的补考成绩
	 * @param condition
	 * @return
	 */
	public Map<String,Object> getMaxMakeupExamResult(Map<String, Object> condition);
	
	/**
	 * 根据条件获取未录入成绩（已开课并且设置了登分老师）的课程
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findNoExamResultCourse(Map<String, Object> condition) throws Exception;
	Map<String, Map<String, Object>> getElectiveExamCountMapByStuList(List<StudentInfo> stuList) throws Exception;
	
}
