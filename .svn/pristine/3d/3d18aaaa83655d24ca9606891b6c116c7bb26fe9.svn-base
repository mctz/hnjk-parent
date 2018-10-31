package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.vo.ExamInfoVo;
/**
 * 
 * <code>考试(课程安排)信息表ServiceInf</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 上午10:13:35
 * @see 
 * @version 1.0
 */
public interface IExamInfoService extends IBaseService<ExamInfo> {
	void batchCascadeDelete(String[] c_id)throws ServiceException;
	void deleteCourse(String c_id);
	/**
	 * 根据课程ID和考试批次ID查找ExamInfo
	 * @param courseId
	 * @param examSubId
	 * @return
	 */
	public ExamInfo findExamInfoByCourseAndExamSub(String courseId,String examSubId);
	/**
	 * 根据课程名称和考试批次ID查找ExamInfo
	 * @param courseName
	 * @param examSubId
	 * @return
	 */
	public ExamInfo findExamInfoByCourseNameAndExamSubId(String courseName,String examSubId);

	/**
	 * 录入成绩时查询考试科目
	 * @param condition
	 * @param objectPage
	 * @return
	 */
	public Page findExamInfoForExamResultsInput(Map<String, Object> condition,Page objectPage);
	
	/**
	 * 设置异常成绩状态查询考试科目
	 * @param condition
	 * @param objectPage
	 * @return
	 */
	public Page findExamInfoForAbnormitConfig(Map<String, Object> condition,Page objectPage);
	/**
	 * 根据考试批次ID查找考试课程
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	public List<Course> findExamInfoCourseBySubId(String examSubId)throws ServiceException;
	
	/**
	 * 根据批次获取考试批次课程相关信息
	 * @param sub
	 * @param examInfoId
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamInfoVo> findExamResultsVOForCountExamResult(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 检查考试课程表是否被成绩关联
	 * @param resourceid
	 * @return
	 * @throws ServiceException
	 */
	public boolean isLinkedByExamResults(String resourceid) throws ServiceException;
	
	/**
	 * 查找有预约的考试课程
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamInfo> findExistsExamOrderExamInfos(String examSubId)throws ServiceException;
	/**
	 * 分页查询考试信息
	 * @param condition
	 * @param objectPage
	 * @return
	 * @throws ServiceException
	 */
	Page  findExamInfoByCondition(Map<String,Object> condition,Page objPage) throws ServiceException;
	/**
	 * 安排期末机考
	 * @param planIds
	 * @param examInfoId
	 * @param arrangeType 1-安排机考，0-取消安排
	 * @throws ServiceException
	 */
	void arrangeFinalExamInfo(String[] planIds, String examInfoId,String arrangeType) throws Exception;
	
	/**
	 * 查询有复审记录的考试课程
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamInfoForExamReview(Map<String,Object> condition,Page objPage) throws ServiceException;
	/**
	 * 考试课程设置
	 * @param ids
	 * @param studyScorePer
	 * @param courseScoreType
	 * @param msg
	 * @throws Exception
	 */
//	public void examInfoConfig(String ids, String studyScorePer,String courseScoreType, StringBuffer msg) throws Exception;
	public void examInfoConfig(String ids, ExamInfo examInfo, StringBuffer msg) throws Exception;
	
	/**
	 * 保存考试课程信息
	 * @param per
	 * @param examSub
	 * @param course
	 * @param examCourseType
	 * @param isMachineExam
	 * @return
	 * @throws Exception
	 */
	public ExamInfo saveExamInfo(Double per, ExamSub examSub, Course course,Integer examCourseType,String isMachineExam) throws Exception;
	
	/***
	 * 保存考试信息后 ,更新学生预约记录 //学院2016修改
	 * @param ei
	 * @throws Exception
	 */
	public void updateStulear(ExamInfo ei)throws Exception;
	
	/***
	 * 根据考试信息获取补考名单数据   //学院2016修改
	 * @param ei
	 * @return
	 */
	public Page getstudentmakeuplist(ExamInfo ei,Page pag,Map<String,Object> condition);
	
	/***
	 * 获取机考统计 //学院2016修改
	 * @param pag
	 * @return
	 */
	public Page getExamCount(Page pag);
	
	/**
	 * 安排/取消期末机考
	 * @param planList
	 * @param mkList
	 * @param examInfoId
	 * @param arrangeType
	 * @throws Exception
	 */
	void operateFinalExamInfo( List<StudentLearnPlan> planList,List<StudentMakeupList> mkList,String examInfoId,String arrangeType) throws Exception;
	
	/**
	 * 根据条件获取考试课程信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	List<ExamInfo> findExamInfoByCondition(Map<String,Object> condition);

	/**
	 * 查找考试信息
	 * @param courseId
	 * @param examSubId
	 * @param teachType :networkTeach/faceTeach
	 * @return
	 */
	ExamInfo findExamInfoByCourseAndExamSubAndCourseType(String courseId, String examSubId, String teachType);
	ExamInfo saveExamInfoNow(ExamSub examSub, Course course,
			Integer examCourseType, String isMachineExam) throws Exception;

	/**
	 * 根据查询条件获取符合要求的考试信息
	 * @param condition:examSubId,courseId,scoreStyle,isMachineExam,examCourseType
	 * @return
	 */
	ExamInfo getExamInfo(Map<String,Object> condition);

	/**
	 * 查找考试信息
	 * @param courseId
	 * @param examSubId
	 * @param examCourseType
	 * @return
	 */
	ExamInfo findExamInfoByCourseAndExamSubAndExamCourseType(String courseId, String examSubId,Integer examCourseType);
}
