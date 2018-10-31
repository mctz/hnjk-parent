package com.hnjk.edu.teaching.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.edu.roll.vo.StudentSignatureVo;
import com.hnjk.edu.teaching.vo.ExamOrderStatExportVo;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.security.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <code>教学模块公共JDBCService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-17 上午09:42:12
 * @see 
 * @version 1.0
 */
public interface ITeachingJDBCService {
	/**
	 * 统计预约教材情况
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statBookOrder(Map<String, Object> condition);

	/**
	 * 分页统计预约教材情况
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page statBookOrder(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 统计预约课程情况      version 1  (预约学习、预约考试合并同一页面时的第一个版本)
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statCourseOrder(Map<String, Object> condition);

	/**
	 * 分页统计预约课程情况    version 1   (预约学习、预约考试合并同一页面时的第一个版本)
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page statCourseOrder(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 分页统计预约考试情况        version 2  (预约学习、预约考试合并同一页面时的第二个版本)
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page statExamOrder (Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 统计预约考试情况        version 2  (预约学习、预约考试合并同一页面时的第二个版本)
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamOrderStatExportVo> statExamOrder(Map<String, Object> condition) throws ServiceException;

	/**
	 * 根据学习中心ID查询考场及座位的使用情况
	 * @param schoolid
	 * @return
	 */
	public List<Map<String,Object>> findExamRoomInfoByBranchSchoolAndExamSub(Map<String, Object> condition);
	/**
	 * 查指定用户的所有在学学籍Id
	 * @param user
	 * @return
	 */
	public List<String> findUserStudentInfoIdList(User user);

	/**
	 * 查看未预约学习学生信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findNotCourseOrderStudentList(Map<String, Object> condition, Page page)throws ServiceException;

	/**
	 * 查找给定考试批次所有考试课程的时间段(考试周时间段)
	 * @param examSubId           考试批次ID
	 * @param examMode            考试形式（机考、非机考）
	 * @param isFillterExamMode   是否过滤考试形式
	 * @return List<Map<String, Object>>
	 */
	public  List<Map<String,Object>>  findExamTimeSegment(String examSubId, String examMode, String isFillterExamMode) ;
	/**
	 * 查找有预约记录的考试课程信息(批量座位安排)
	 * @param condition
	 * @return
	 */
	public Page findExamCourseInfoByCondition(Page page, Map<String, Object> condition);

	/**
	 * 统计课程的考试人数
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> findCourseExaminationNum(Map<String, Object> condition);


	/**
	 * 统计期末考试预约人数
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statExamNum(Map<String, Object> condition);
	/**
	 * 统计入学考试人数
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statRecruitExamNum(Map<String, Object> condition) ;

	/**
	 * 打印标签时查找卷袋标签信息(按学习中心打印，按课程打印)
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamPaperBagInfoForPrint(Page page, Map<String, Object> condition);

	/**
	 * 统计座位安排情况(期末考试)
	 * @param condition
	 * @return
	 */
	public Page examSeatAssignInfo(Page page, Map<String, Object> condition);
	/**
	 * 查询面授成绩
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<ExamResultsVo> findFaceStudyExamResultsVo(Map<String, Object> condition) throws ServiceException;
	/**
	 * 面授成绩分页查询
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page findFaceStudyExamResultsVo(Page page, Map<String, Object> condition) throws ServiceException;

	/**
	 * 导出试室安排总表
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statExamRoomPlan(Map<String, Object> condition);
	/**
	 * 导出期末座位表
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getExamRoomSeat(Map<String, Object> condition);

	/**
	 * 获取课程成绩录入信息-old
	 *
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findTeachingPlanClassCourseByConditionOld(Map<String, Object> condition, Page objPage)
			throws ServiceException;

	/**
	 * 获取课程成绩录入信息-new
	 *
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findTeachingPlanClassCourseByCondition(Map<String, Object> condition, Page objPage)
			throws ServiceException;
	/**
	 * 查找有预约记录考试课程的记录(考试座位安排)
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Page findExamCourseStudentByCondition(Page page, Map<String, Object> condition) throws Exception;

	/**
	 * 查找安排座位的学生记录(考试座位安排)
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Page findStudentSeatByCondition(Page page, Map<String, Object> condition) throws Exception;

	/**
	 * 查找考试课程是否为校内考(考试座位安排)
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public String findCourseByCondition(Page page, Map<String, Object> condition) throws Exception;
	/**
	 *
	  * @description:根据年级查询教学计划
	  * @author xiangy
	  * @date 2013-11-5 上午10:16:51
	  * @version V1.0
	  * @param page
	  * @param condition
	  * @return
	  * @throws Exception
	 */
	public Page findTeachingPlanByGrade(Page page, Map<String, Object> condition) throws Exception;
	/**
	 * 查询试室的座位安排信息-试室信息_按课程分
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> findExamRoomInfoByBranchSchoolAndExamSubAndCourse(Map<String, Object> condition);
	/**
	 * 获取考试时间
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> getExamTime(Map<String, Object> condition) throws ServiceException;
	/**
	 * 分页统计统考报名考试情况
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page statStateExamOrder(Map<String, Object> condition, Page objPage)throws ServiceException;
	/**
	 * 统计统考报名考试情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamOrderStatExportVo> statStateExamOrder(Map<String, Object> condition)throws ServiceException;
	/**
	 * 统考的课程列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> courseOption(Map<String, Object> condition)throws ServiceException;

	Page findTeachingPlanClassCourseByCondition2(Map<String, Object> condition,
												 Page objPage) throws ServiceException;

	Page findNonFaceStudyExamResultsVo(Page page, Map<String, Object> condition)
			throws ServiceException;

	List<ExamResultsVo> findFailStudyExamResultsVo(
			Map<String, Object> condition) throws ServiceException;

	public StringBuffer getNonFacestudyExamResultsSql(Map<String, Object> condition);

	public Page findTeachingPlanExamresultMapByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 根据条件获取已开课课程信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> findOPenedCourseInfo(Map<String, Object> condition) throws Exception;

	/**
	 * 根据年份和建议学期获取要开课课程的有关信息
	 * @param openTerm
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> findByYearAndTerm(Set<String> openTerm) throws Exception;

	/**
	 * 根据条件获取教学计划课程的录入情况信息
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseInputInfoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 根据条件获取课程录入情况
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseExamInfoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 根据年级教学计划获取要开课的课程有关信息
	 * @param guiplanIds
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> findOpenCourseInfoByGuiplanId(String guiplanIds) throws Exception;

	/**
	 * 按查询条件获取平时成绩
	 * @return
	 * @throws ServiceException
	 */
	public String getUsualResultByStudent(Map<String, Object> params) throws ServiceException;

	/**
	 * 获取某个学生的最终成绩（安徽医学籍表）
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findFinalExamResults(String studentId) throws Exception;

	/**
	 * 获取补考签到表
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<StudentSignatureVo> findFailStudentSignature(Map<String, Object> condition) throws Exception;

	/**
	 * 获取补考考试签到表打印数据
	 * @param condition
	 * @return
	 * @throws WebException
	 */
	public List getFailStudentSignatureToPrint(Map<String, Object> condition) throws WebException;

	Page findBySql(Page page, String sql, Map<String, Object> values)
			throws ServiceException;

}
