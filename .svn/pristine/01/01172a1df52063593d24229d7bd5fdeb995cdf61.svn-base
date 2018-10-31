package com.hnjk.edu.teaching.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.vo.UsualResultsVo;
/**
 * 学生平时成绩服务接口
 * <code>IUsualResultsService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-21 下午02:15:50
 * @see 
 * @version 1.0
 */
public interface IUsualResultsService extends IBaseService<UsualResults> {
	/**
	 * 保存学生平时成绩
	 * @param list
	 * @throws ServiceException
	 */
	void batchSaveOrUpdateUsualResults(String[] planId, String[] isDealed, String[] bbsResults, String[] exerciseResults, String[] selftestResults, String[] otherResults, String[] faceResults, String[] askQuestionResults, String[] courseExamResults) throws ServiceException;
	/**
	 * 提交平时成绩
	 * @param ids
	 * @throws ServiceException
	 */
	void submitUsualResults(String[] ids) throws ServiceException;
	/**
	 * 保存更新平时成绩并更新到学习计划
	 * @param usualResults
	 * @param plan
	 * @throws ServiceException
	 */
	void saveOrUpdateUsualResultsAndLearnPlan(List<UsualResults> usualResultsList, List<StudentLearnPlan> planList) throws ServiceException;
	/**
	 * 保存课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @throws ServiceException
	 */
	void saveAllUsualResults(String yearInfoId, String term, String courseId) throws ServiceException;
	/**
	 * 提交课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @param teacherId
	 * @param unitId
	 * @param type
	 * @throws ServiceException
	 */
	int submitAllUsualResults(String yearInfoId, String term, String courseId,String teacherId,String unitId,String type) throws Exception;
	/**
	 * 查询学生平时成绩列表vo
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findUsualResultsVoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查询学生平时成绩列表vo
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<UsualResultsVo> findUsualResultsVoByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 定时计算并提交平时成绩（在学习时间结束后）
	 * @return
	 */
	public String timingCalculateAndSubmitUsualResults()throws ServiceException;
	/**
	 * 网络面授类课程学生平时成绩的计算-成绩录入过程中使用
	 * 注:2012.12.12 AB类课程成绩录入   中指出AB类课程的平时分只包括随堂练习，且不按照平时分计分规则比例计算，既随堂练习=平时分总分
	 * @param courseId     课程ID
	 * @param stuIds       学生ID
	 * @return
	 */
	Map<String,BigDecimal> calculateNetsidestudyUsualResults(String courseId,String stuIds);
	
	public int saveAllUsualResultsInt(String yearInfoId, String term, String courseId) throws ServiceException;
	
	public Map<String, BigDecimal> calculateExerciseResults(String yearInfoId, String term,String courseId,String isMaxScore)throws ServiceException;
	
	public void saveSpecificUsualResults(StudentInfo stu,String yearInfoId, String term, String courseId) throws ServiceException;
	
	/**
	 * 根据条件获取平时成绩
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<UsualResults> findByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 获取学生网上学习情况-分页
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findLearningInfoByCondition(Map<String, Object> condition, Page page) throws ServiceException;
	
	/**
	 * 获取学生网上学习情况-列表
	 * @param condition
	 * @return
	 * @throws Exception 
	 */ 
	List<UsualResultsVo> findLearningInfoListByCondition(Map<String, Object> condition) throws Exception;
}
