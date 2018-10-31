package com.hnjk.edu.teaching.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.vo.AuditExamResultsVo;
import com.hnjk.edu.teaching.vo.DegreeCourseExamVO;
import com.hnjk.edu.teaching.vo.EnterExamToTalVo;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.edu.teaching.vo.FailExamStudentVo;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.security.model.User;

/**
 * 
 * <code>学生预约考试成绩Service接口</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 下午01:37:50
 * @see 
 * @version 1.0
 */
public interface IExamResultsService extends IBaseService<ExamResults> {
	
	/**
	 * 非统考课程补考学生成绩导出统计
	 */
	
	public  List failExamStudentResultList(Map<String, Object> condition)throws ServiceException;
	
	public Page queryExamResultsInfoForFaceTeachTypeMakeup2(Map<String, Object> condition, Page page) throws ServiceException;
	public void auditExamResultsAllPassForFactMakeupCourse(
			Map<String, Object> condition, ExamInfo info, String teachType, String examSubId) throws Exception;
	/**
	 *  补考成绩审核-个别审核
	 * @param request
	 * @param info
	 * @param teachType
	 * @throws ServiceException
	 */
	public void makeupAuditExamResultsSingle(String[] idsArray,
			HttpServletRequest request, ExamInfo info, String teachType, String examSubId)throws Exception;
	/**
	 * @param condition
	 * @return
	 * throws ServiceException
	 * 非统考课程补考学生统计
	 */
	public List failExamStudentList(Map<String, Object> condition)throws ServiceException;
	/***
	 * 非统考课程补考学生导出
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<FailExamStudentVo> findFailExamResultStudentExport(Map<String,Object> condition) throws ServiceException ;
	/***
	 * 补考学生列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findFailExamResultStudentList(Page page,Map<String,Object> condition) throws ServiceException ;
	/***
	 * 申请缓考列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamDelayList(Map<String,Object> condition,Page objPage)throws ServiceException;
	/**
	 * 根据条件查询学生考试成绩记录，返回LIST
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> findExamResultsByCondition(Map<String,Object> condition)throws ServiceException;
	/**
	 * 根据条件查询学生考试成绩记录，返回PAGE
	 * @param condition
	 * @param objecPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamResultByCondition(Map<String,Object> condition,Page objecPage)throws ServiceException;
	
	/**
	 * 根据传入的ID使用IN语句查询ExamResults列表
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> findExamResultListByIds(String ids)throws ServiceException;
	/**
	 * 查找已按排座位的ExamResults列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> findHaveSeatExamResultList(Map<String,Object> condition)throws ServiceException;
	/**
	 * 查询ExamResults记录的ID
	 * @return
	 * @throws ServiceException
	 */
	public String findExamResultIdsForClearnExamRoom(Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 根据课程ID及考试批次ID获取考试成绩记录数
	 * @return
	 * @throws ServiceException
	 */
	public int findExamResultCount(Map<String,Object> param)throws ServiceException;
	
	/**
	 * 根据条件查找ExamResults列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResultsVo> findExamResultVoList(Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 根据考试批次ID及成绩状态查找成绩列表
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> findExamResultListByExamSubAndCheckStatus(String examSubId)throws ServiceException;
	
	/**
	 * 根据考试批次ID及学号、课程查找成绩对象
	 * @param examSubId
	 * @param stuNo
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	public ExamResults findExamResulByExamSubAndStuNoAndCourseId(String examSubId,String stuNo,String courseId,String classesId)throws ServiceException;
	
	/**
	 * 撤销成绩(清零)
	 * @param paramMap
	 * @return
	 */
	public int revocationImprotResults(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 查询学籍ID对应的所有成绩
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> findExamResultsByStudentInfoId(String studentId)throws ServiceException;
	
	/**
	 * 获取成绩的分布概况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> findExamResultsDistribution(Map<String,Object> condition)throws Exception;
	
	/**
	 * 发布成绩前的提示信息
	 * @return
	 * @throws Exception
	 */
	public String getPublishRemindInfoByExamSubId(Map<String,Object> condition) throws Exception;
	/**
	 * 调整成绩
	 * @param examInfoId 考试课程
	 * @param adjustLine 分数线
	 * @throws ServiceException
	 */
	void adjustExamResults(String examInfoId, String adjustLine) throws ServiceException;
	/**
	 * 提交成绩
	 * @param msg 
	 * @throws ServiceException
	 */
	void submitExamResults(List<ExamResults> list,String flag, StringBuffer msg) throws Exception;
	
	/**
	 * 提交平时成绩
	 * @param list
	 * @param flag
	 * @throws ServiceException
	 */
	void submitUsExamResults(List<ExamResults> list,String flag) throws ServiceException; 
	/**
	 * 传入成绩列表计算综合成绩
	 * @param list
	 * @param info
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> calculateExamResultsListIntegratedScore(List<ExamResults> list,ExamInfo info)throws ServiceException;
	
	/**
	 *  成绩审核-个别审核
	 * @param ids
	 * @param request
	 * @param info
	 * @param teachType
	 * @throws ServiceException
	 */
	public void auditExamResultsSingle (String [] ids ,HttpServletRequest request,ExamInfo info,String teachType)throws Exception;
	/**
	 *  成绩审核-整个考试科目全部通过
	 * @param info
	 * @param teachType
	 * @throws ServiceException
	 */
	public void auditCourseExamResultsPass(ExamInfo info,String teachType)throws Exception;
	/**
	 *  成绩审核-全部通过(面授课程、面授+网授课程)
	 * @param info
	 * @param teachType
	 * @throws ServiceException
	 */
	public void auditExamResultsAllPassForFactCourse(Map<String,Object> condition,ExamInfo info,String teachType)throws Exception;
	/**
	 * 根据条件查询成绩记录
	 * @param condition
	 * @return
	 */
	public List<ExamResults> queryExamResultsByCondition(Map<String,Object> condition);
	/**
	 * 发布考试课程的成绩
	 * @param info
	 * @param teachType
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public int publishedExamResultsByExamInfo(ExamInfo info,String teachType,Map<String,Object> condition)throws Exception;
	
	/**
	 * 给定考试课程是否允许发布
	 * @param info
	 * @param teachType
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public boolean isAllowPublishedExamInfosResults(ExamInfo info,String teachType,Map<String,Object> condition)throws Exception;
	
	/**
	 * 面授、面授+网授课程成绩审核查询
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page queryExamResultsInfoForFaceTeachType(Map<String,Object> condition,Page page);
	/**
	 * 考试统计
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	public Page examResultStatistics(Map<String, Object> condition,Page page);
	/**
	 * 获取待更正的成绩列表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findAuditExamResulstsVoByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	/**
	 * 获取成绩对应待更正成绩
	 * @param examResultsId
	 * @return
	 * @throws ServiceException
	 */
	AuditExamResultsVo findAuditExamResulstsVoByExamResultsId(String examResultsId) throws ServiceException;
	/**
	 * 卷面成绩在线录入保存
	 * @param resultsIds        成绩ID
	 * @param integratedScores  提交的总评成绩
	 * @param writtenScores     提交的卷面成绩
	 * @param examAbnormitys    提交的成绩异常状态
	 * @param writtenHandworks  考试科目为混合机考时的笔考成绩
	 * @param examCountHQL      查询选考次数的HQL
	 * @param curUserName       当前用户名
	 * @param curUserId         当前用户ID
	 * @param curTime           当前时间
	 * @param isAbnormityInput  是否是异常成绩录入用户
	 */
	public String examResultsInputSave(String[] resultsIds,String[] integratedScores,
			String[] writtenScores, String[] examAbnormitys,
			String[] writtenHandworks, String examCountHQL, String curUserName,
			String curUserId, Date curTime, boolean isAbnormityInput) throws ServiceException;
	/**
	 * 平时成绩在线录入保存
	 * @param resultsIds
	 * @param curUserName
	 * @param usuallyScores
	 * @param curUserId
	 * @param curTime
	 * @return
	 * @throws ServiceException
	 */
	public String usExamResultsInputSave(String[] resultsIds,String curUserName,String[] usuallyScores,String curUserId, Date curTime)throws ServiceException;

	/**
	 * 非统考课程补考学生统计---------old
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page failExamStudentList(Page page, Map<String, Object> condition)
			throws ServiceException;
	
	/**
	 * 非统考课程补考学生统计---------new
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page failExamStudentListNew(Page page, Map<String, Object> condition)
			throws ServiceException;
	
	public List<EnterExamToTalVo> printEnterExamList(
			Map<String, Object> condition);
	public List<Map<String, Object>> getRealExamList(
			Map<String, Object> condition) throws Exception;
	
	/**
	 * 获取正考中的缓考成绩
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public  List getZKExamResult(Map<String, Object> condition)throws ServiceException;
	
	/**
	 * 计算课程的综合成绩
	 * @param info
	 * @param usuallyScore
	 * @param writtenScore
	 * @param onlineScore
	 * @param onlineScore
	 * @param rs
	 * @param resultCalculateRuleMap
	 * @return
	 */
	public String caculateIntegrateScore(ExamInfo info, String usuallyScore, String writtenScore, String onlineScore, ExamResults rs,Map<String, String> resultCalculateRuleMap);
	
	/**
	 * 处理成绩审核时的补考逻辑
	 * @param curTime
	 * @param curUser
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public void handleMadeUp(Date curTime, User curUser, ExamResults rs)
			throws Exception;
	
	/**
	 * 根据学生学籍、课程和考试批次类型查询成绩
	 * 
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> findBySidAndCidAndExamType(Map<String, Object> params) 
			throws ServiceException;

	/**
	 * 查询需要录入的人数
	 * @param condition
	 * @throws Exception
	 */
	public void queryInputTotalNum(Map<String, Object> condition,Map<String, Object> inputTotalNum) throws Exception;
	
	/**
	 * 根据条件获取补考课程录入情况
	 * @param objpage
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page findMakeupInfoByContion(Page objpage, Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 通过教学计划，教学计划课程，教学点和年级判断是否有成绩
	 * （不包含转教学点，转专业，转学习形式的学生）
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	boolean hasExamResults(Map<String, Object> condition) throws Exception;
	
	/**
	 * 删掉某个学生某门课程补考成绩
	 * @param studentInfoId
	 * @param planCourseId
	 * @return
	 */
	public int deleteByStuIdAndPlanCourseId(String studentInfoId, String planCourseId);
	
	/**
	 * 获取某个学生某门课程某个考试批次的成绩
	 * @param studentInfoId
	 * @param planCourseId
	 * @param examSubId
	 * @return
	 */
	public ExamResults getByCondtition(String studentInfoId, String planCourseId,String examSubId);
	
	/**
	 * 根据考试批次ID及学号、课程查找成绩对象
	 * @param examSubId
	 * @param stuNo
	 * @param examInfoId
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResults> findExamResulByExamSubAndStuNoAndExamInfoId(String examSubId, String[] stuNo,String examInfoId);
	
	public List<Map<String, Object>> failExamStudentList2Excel( Map<String, Object> condition);
	
	/**
	 * 根据学籍、教学计划和代替缓考批次获取某个学生的最终成绩
	 * 
	 * @param studentId
	 * @param planId
	 * @param delayExamType
	 * @return
	 */
	public List<Map<String,Object>> findExamScore(String studentId, String planId,String delayExamType) throws Exception;
	
	/**
	 * 获取最终成绩
	 * 
	 * @param studentId
	 * @param planId
	 * @param delayExamType
	 * @return
	 */
	public List<DegreeCourseExamVO> findFinalExamForGW(String studentId,String planId,String delayExamType);
	
	/**
	 * 获取学生各科成绩平均分和论文成绩
	 * 
	 * @param studentId
	 * @param planId
	 * @param delayExamType
	 * @return
	 */
	public Map<String,String> getAvgAndThesisScore(String studentId,String planId,String delayExamType);

	/**
	 * 根据分值计算绩点—广外
	 * @param integratedScoreNum
	 * @param dictList
	 * @return
	 */
	Double getGPA_gdwy(Double integratedScoreNum, List<Dictionary> dictList);

	/**
	 * 获取统考和非统考的成绩信息 - 正考成绩并且已发布
	 * @param studentid
	 * @param openTerm
	 * @return avgScoreForFaceStudy、avgScoreForNetworkStudy
	 * <p>isAllPass、hasNetworkStudyScore、minNetworkStudyScore、maxNetworkStudyScore</p>
	 */
	Map<String, Object> getAvgScoreGroupByTeachType(String studentid, String openTerm);

	/**
	 * 汕大成绩单数据处理
	 * @param map 打印参数
	 * @param mapList 学生成绩
	 * @param classesId 班级id
	 * @param isMakeUp 是否补考
	 */
	List<Map<String, Object>> getExamResultMap_STDX(Map<String, Object> map, List<Map<String, Object>> mapList, String classesId, String isMakeUp);

	/**
	 * 将成绩Model转为Map（成绩打印和导出使用）
	 * @param list
	 * @param noexamList
	 * @param isPrint 是否打印（主要用于判断是否需要进行成绩替换）
	 * @return
	 */
	List<Map<String, Object>> getMapInfoListByExamResultListForPrint(List<ExamResults> list, List<NoExamApply> noexamList, String isPrint);
}
