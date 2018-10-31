package com.hnjk.edu.roll.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.vo.ExamResultsInfoVO;
import com.hnjk.edu.roll.vo.StudentInfoVo;
import com.hnjk.edu.roll.vo.StudentSignatureVo;
import com.hnjk.edu.roll.vo.StudentStatisticsVo;
import com.hnjk.edu.roll.vo.StudyNoImportVo;
import com.hnjk.edu.teaching.vo.StudentInfoAndDegreeCourseVO;
import com.hnjk.security.model.User;

/**
 * 学生学籍
 * <code>IStudentInfoService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-22 下午04:12:56
 * @see 
 * @version 1.0
 */
public interface IStudentInfoService extends IBaseService<StudentInfo>{

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findStuByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findStuByParam(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 启用/停用 学生账号
	 * @param ids
	 * @param isEnabled 是否允许
	 * @throws ServiceException
	 */
	void enableStudentAccount(List<String> ids,boolean isEnabled) throws ServiceException;
	/**
	 * 重置学生账户密码为原始密码
	 * @param ids
	 * @throws ServiceException
	 */
	void resetStudentAccountPassword(String[] ids, String initPassword) throws ServiceException;
	
	/**
	 * 设置学生预约学习状态
	 * @param matriculateNoticeNo
	 * @return
	 * @throws ServiceException
	 */
	boolean changeStuOrderCourseStatus(String resourceid,String configStatus,int status)throws ServiceException;
	
	/**
	 * 设置学生预约毕业论文状态
	 * @param matriculateNoticeNo
	 * @return
	 * @throws ServiceException
	 */
	@Deprecated
	boolean changeStuGraduatePaperOrderStatus(String resourceid,int status)throws ServiceException;
	
	/**
	 * 根据条件查找未预约第一学期课程的学生
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<StudentInfo> findNoFirstTermCourseStu(Map<String, Object> condition)throws ServiceException;
	/**
	 * 根据传入的多个ID查找StudentInfo列表
	 * @param ids (xxxx,xxxx,xxxx)
	 * @return
	 * @throws ServiceException
	 */
	List<StudentInfo> findStuListByIds(String ids)throws ServiceException;
	/**
	 * 获取学生默认学籍
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	StudentInfo getStudentInfoByUser(User user)throws ServiceException;
	/**
	 * 根据系统用户ID获取所有对应的学籍
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	List<StudentInfo> findStuListByUserId(String userId)throws ServiceException;
	
	/**
	 * 獲取學生的預約時間（學習、考試、畢業論文）
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> getStudentOrderTime() throws ServiceException;
	
	/**
	 * 根据学生学籍ID判断该学生是否欠费
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	boolean isSubscriberFee(String studentId) throws ServiceException; 
	
	/**
	 * 查询导出学生列表.
	 * @param parameter
	 * @return
	 * @throws ServiceException
	 */
	//List<StudentInfoVo> exportStudentList(Map<String, Object> parameter) throws ServiceException;
	
	/**
	 * 查询学生毕业条件完成情况
	 * @param studentInfo
	 * @return
	 * @throws ServiceException
	 */
	Map<String,Object> getStudentGraduateAccomplishStatus(StudentInfo studentInfo) throws ServiceException;
	
	/**
	 * 导出学籍表
	 * @param parameter
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentInfoVo> exportStudentList(Map<String, Object> parameter)	throws ServiceException ;
	
	/**
	 * 批量设置学籍状态
	 * @param map
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public void batchSetStatus(Map<String, Object> map) throws ServiceException, ParseException ;
	/**
	 * 恢复学籍状态
	 * @param stuChangeInfoId
	 * @throws ServiceException
	 */
	public void redoStudentStatus(String stuChangeInfoId,String settingDate,Map<String,Object> map) throws ServiceException, ParseException;
	/**
	 * findStuByCondition方法的重载
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentInfo> findStuByCondition(Map<String, Object> condition)throws ServiceException ;
	/**
	 * JDBC方式查询考试信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List findStuByConditionByJDBC(Map<String, Object> condition)throws ServiceException ;
	/**
	 * 学籍卡保存
	 * @param request
	 * @throws ServiceException
	 */
	void studentRollInfoCardSave(HttpServletRequest request,Map<String,Object> map, StudentInfo studentInfo)throws ServiceException;
	/**
	 * 新增功能：审核已修改的学籍
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findStuAuditResultsByCondition(Map<String, Object> condition,Page objPage)throws ServiceException;
	
	public List<StudentInfo> findStuListByRid(String rid)throws ServiceException, Exception;
	/**
	 * 退回学籍信息卡
	 * @param studenno 身份证号码
	 * @return 
	 * @throws ServiceException
	 */
	public Map<String,Object> backStudenInfoCard(String studenno)throws ServiceException;
	
	/**
	 * 根据查询条件获取班级人数
	 * @param 
	 * 参数：stuStatus-默认11；classesid
	 * @return
	 * @throws ServiceException
	 */
	public int getStudentNum(Map<String, Object> parameter)	throws ServiceException;
	/**
	 * 关联教学计划
	 * @param ids
	 * @throws ServiceException
	 */
	public void joinTeachingPlan(String[] ids) throws ServiceException;
	
	public Page findStudentByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 根据用户以及年级获取学籍信息
	 * 
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentInfo> findByUserAndGrage(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据条件获取学生学籍信息列表
	 * 
	 * 	@param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentInfo> findStudentInfoListByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据条件获取唯一学籍信息
	 * @param condition
	 * @return
	 */
	public StudentInfo findUniqueStudentInfo(Map<String, Object> condition);
	
	/**
	 * 处理成绩信息（安徽医学籍表）
	 * @param examResulsList
	 */
	public List<ExamResultsInfoVO> handleExamResultInfo(List<Map<String, Object>> examResulsList);
	
	/**
	 * 根据条件获取学籍信息（不包含其他逻辑，单纯的查询）
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentInfo> findByCondition(Map<String, Object> condition) throws ServiceException;
	
	public Page findPageByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 将学籍信息列表转为Map
	 * @param studentInfoList
	 * @return
	 */
	public Map<String, StudentInfo> transStudentInfoListToMap(List<StudentInfo> studentInfoList);
	/**
	 * 统计学生的信息
	 */
	public List<StudentStatisticsVo> CalStudentStatistics(List<String> majorList,Map<String , Object> condition) throws ServiceException;
	/**
	 * 查找考生签到表的学生人数
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentSignatureVo> findStudentSignature(Map<String, Object> condition) throws ServiceException;
	/**
	 * 查找符合结业换毕业的学生
	 */
	public Page findStudentInfoSecGraduate(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 更新学生在线时长信息
	 * @param sql
	 * @param params
	 * @throws Exception
	 */
	public void updateLoginLongInfo(String sql, Object... params) throws Exception;
	
	/**
	 * 获取某个用户当前的学籍信息
	 * @param userId
	 * @return
	 */
	public StudentInfo getByUserId(String userId) throws Exception ;
	/**
	 * 导入学生学籍相片
	 * @param filepath 
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> importPhoto(String gradeid, String filepath) throws Exception;
	
	/**
	 * 该方法只用于获取每个班级的单个学生学籍信息 2018-6-27
	 * <p>根据条件获取所有课程的学生的交集,用于导出班级成绩的标题
	 * @param condition 
	 * @return
	 * @throws Exception
	 */
	public List<StudentInfo> findAllExamResultsStudentInfo(Map<String, Object> condition);
	
	/**
	 * 提交学籍卡
	 * @param studentIds
	 * @return
	 * @throws Exception
	 */
	public int submitStuCard(String studentIds) throws Exception;
	public Map<String, Object> importStudyNo(List<StudyNoImportVo> modelList);
	
	/**
	 * 根据身份证号获取当前学籍
	 * @param certNum
	 * @return
	 */
	public StudentInfo findBycertNum(String certNum);
	
	/**
	 * 获取学籍信息
	 * @param studentList
	 * @return
	 * @throws Exception
	 */
	Map<String, Map<String, Object>> getStudentInfoMapByStuList(List<StudentInfo> studentList) throws Exception;

	/**
	 * 根据查询条件批量修改学生学籍信息
	 * @param colMap
	 * @param condition
	 */
	void batchSetStudendInfo(Map<String,Object> colMap,Map<String,Object> condition);
	
	/**
	 * 获取学生的基本信息和学位外语课程信息
	 * 
	 * @param studyNo
	 * @return
	 */
	public StudentInfoAndDegreeCourseVO findByStudyNo(String studyNo) throws Exception;

	int getStudentNumByClassid(String classesid);

	/**
	 * 导入实习材料
	 * @param filePath 文件路径
	 * @param type 类型：graduation-毕业实习材料；degree-学位申请及审定材料
	 * @return
	 */
	Map<String, Object> importPracticeMaterials(String filePath, String type);
}
