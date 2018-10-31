package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;

/**
 * 
 * <code>招生管理模块JDBC公共service</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-8-4 上午09:20:50
 * @see
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public interface IRecruitJDBCService {
	
	/**
	 * 获得数据库中最大的学号
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public String getMaxStudentid(String prefix) throws ServiceException;

	/**
	 * 学习模式统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listStudymodel(String year, String school, String major,
			String classic);

	/**
	 * 专业统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listMajorType(String year, String school, String major,
			String classic);

	/**
	 * 统计人数
	 * 
	 * @param year
	 *            招生批次
	 * @param type
	 *            类型
	 * @param school
	 *            学习中心
	 * @param classic
	 *            层级
	 * @param major
	 *            专业
	 * @return
	 */
	public Double statisticsStuTotal(String year, String school, String major,
			String classic, String type);

	/**
	 * 获取校外学习中心申报的招生批次ID列表
	 * 
	 * @param recruitPlanId
	 *            ----招生计划ID
	 * @return
	 */
	public String findBranchSchoolPlanIdList(Map<String, Object> paramMap);

	/**
	 * 层次统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listClassicLevel(String year, String school, String major,
			String classic);

	/**
	 * 考试种类统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listExamStatistic(String year, String school, String major,
			String classic);

	/**
	 * 信息地媒体来源
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listFromMedia(String year, String school, String major,
			String classic);

	/**
	 * 报名方式
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listApplyType(String year, String school, String major,
			String classic);

	/**
	 * 录取情况统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listEnrollStatus(String year, String school, String major,
			String classic);

	/**
	 * 录取学生学习模式
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listEnrollStudy(String year, String school, String major,
			String classic);

	/**
	 * 录取学生专业统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic
	 * @param major
	 * @param school
	 * @return
	 */
	public List listEnrollMajor(String year, String school, String major,
			String classic);

	/**
	 * 录取学生层次统计
	 * 
	 * @param year
	 *            招生批次
	 * @param classic 
	 * @param major 
	 * @param school 
	 * @return
	 */
	public List listEnrollClassic(String year, String school, String major, String classic);

	/**
	 * 根据SQL统计报名学生数
	 * 
	 * @param year
	 * @param sql1
	 * @return
	 */
	public Double statisStudentBySql(String year, String sql);
	
	/**
	 * 入学成绩统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollSchool(String year, String school, String major,
			String classic);
	
	/**
	 * 招生性别统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollSex(String year, String school, String major, String classic);
	
	/**
	 * 招生籍贯统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollPlace(String year, String school, String major,
			String classic);
	
	/**
	 * 招生户口统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollResidence(String year, String school, String major,
			String classic);
	
	/**
	 * 招生民族统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollNation(String year, String school, String major,
			String classic);

	/**
	 * 政治面貌统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollPolitics(String year, String school, String major,
			String classic);
	
	/**
	 * 招生婚否统计
	 * @param year
	 * @param school
	 * @param major
	 * @param classic
	 * @return
	 */
	public List listEnrollMarry(String year, String school, String major,
			String classic);
	
	/**
	 * 招生综合统计
	 * @param condition
	 * @return
	 */
	public Map<String,Object> listEnrollStatisticComplex(Map<String,Object> condition) throws ServiceException ;
	
	/**
	 * 根据招生批次及校外学习中心查询给定批次下考场的安排情况
	 * @param recruitPlan
	 * @param branchSchool
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>>  findExamRoomAssignByRecruitPlanAndBrschool(Map<String,Object> paramMap)throws ServiceException;
	
	/**
	 * 查询入学考试座位分配列表
	 * @param paramMap
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findEntranceExamSeatList(Map<String,Object> paramMap,Page page) throws ServiceException;
	/**
	 * 查询入学考试座位分配总数
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Long findEntranceExamSeatListCount(Map<String,Object> paramMap)throws Exception;
	
	/**
	 * 查询校外学习中心的试室座位按排情况
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>>  findBrSchoolExamRoomSeatList(Map<String, Object> condition) throws Exception;
	
	/**
	 * 某个招生批次的所有考试课程
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findAllExamSubJectByRecruitPlanId(Map<String, Object> condition)throws Exception;
	
	/**
	 * 查询指定招生批次的所有考试科目组名称(字典值)
	 * @param recruitPlanId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findRecruitPlansCourseGroupName(Map<String,Object> condition);
	
	/**
	 * 查询报名信息表，供资格审核用(按W\N\Y排序)
	 * @param paramMap
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findEnrolleeInfoForAudit(Map<String,Object> paramMap,Page page) throws ServiceException;
	/**
	 * 获取期末机考统计数据
	 * @param examSubId
	 * @param courseId
	 * @param brSchool
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> statFinalExamResult(String examSubId,String courseId,String brSchool) throws ServiceException;
	/**
	 * 得到入学考试座位表
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> getRecruitExamSeatByCondition(Map<String,Object> condition) throws ServiceException;
	/**
	 * 统计机考人数
	 * @param statDate
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> statRecruitExamByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 找到和条件相似的学生
	 * @param condition
	 * @return
	 */
	public String getStudentInfoByQuery(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 查找学号是否可用
	 * @param no 学号
	 * @return
	 * @throws ServiceException
	 */
	public Boolean checkStudentInfoByNo(String no) throws ServiceException;
}
