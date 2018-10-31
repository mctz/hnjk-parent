
/**
 * <code>IStuChangeInfo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:31:24
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.roll.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.CourseOrder;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.vo.StuChangeInfoVo;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.security.model.OrgUnit;

/**
 * <code>IStuChangeInfo</code><p>
 * 学籍异动表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:31:24
 * @see 
 * @version 1.0
 */
public interface IStuChangeInfoService extends IBaseService<StuChangeInfo> {
	
	/**
	 * 新增或保存学籍异动信息
	 * @param stuChangeInfo
	 * @param stu
	 * @throws ServiceException
	 */
	void saveOrUpdateStuChangeInfo(StuChangeInfo stuChangeInfo,StudentInfo stu) throws ServiceException;
	
	/**
	 * 根据条件查找列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 审核通过学籍异动
	 * @param stuChangeInfo
	 * @param stu
	 * @throws ServiceException
	 */
	//void auditPassStudentChangeInfo(StuChangeInfo stuChangeInfo, StudentInfo stu) throws ServiceException;
	
	/**
	 * 批量审核
	 * @param ids
	 * @param type
	 * @throws ServiceException
	 */
	String auditBatchStudentChangeInfo(String[] ids,String type) throws ServiceException;
	
	/**
	 * 批量审核学籍异动信息
	 * 
	 * @author Zik
	 * @param ids
	 * @param type
	 * @param request 
	 * @return  以map形式返回信息
	 * @throws ServiceException
	 * @throws Exception 
	 */
	Map<String,Object> auditBatchStuChangeInfo(String[] ids,String type, HttpServletRequest request) throws ServiceException, Exception;

	/**
	 *  获取指定学生的三种异动类型最新日期异动记录(转中心、转教点、其它)
	 * @param stuId
	 * @return
	 * @throws ServiceException
	 */
	Map<String,StuChangeInfo> findStudentChangeRecord(String stuId) throws ServiceException;
	
	/**
	 *  通过condition获取学生的list
	 * @param stuId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List findByCondition(Map<String, Object> condition)throws ServiceException ;
	
	
	public List<Map<String,Object>> findStuChangeInfoMapList(Map<String, Object> condition) throws ServiceException;

	public List<StuChangeInfoVo> stuChangeInfoPrint(StuChangeInfo stuChangeInfo) throws ServiceException;
	
	/**
	 * 根据系统用户ID获取所有对应的学籍
	 * @param userId
	 * @return
	 * @throws ServiceException
	 * @throws Exception 
	 */
	public List<StuChangeInfo> findStuListByUserId(String userId)throws ServiceException, Exception;
	public List<OrgUnit> findStuListByUnit(String id)throws ServiceException, Exception;
	public List<StuChangeInfo> findStuChangeInfoByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据resourceids获取学籍异动列表
	 * 
	 * @param stuChangeIds
	 * @return
	 * @throws ServiceException
	 */
	public List<StuChangeInfo> findByResourceIds(String stuChangeIds) throws ServiceException;
	
	/**
	 * 根据异动类型获取最新的异动信息
	 * @param changeTypes
	 * @return
	 * @throws ServiceException
	 */
	public StuChangeInfo findLatestByChangeType(String changeTypes) throws ServiceException;
	/**
	 * 根据学籍异动表 studentChangeInfo的resourceid删除学生的退学记录，如果并获取该生退学前的学籍状态，恢复学生退学前的状态，当前已经的有正常注册  11   休学  12
	 * @param id
	 */
	public void DeleteDropout(String id);
	/**
	 * 根据学生信息、教学计划课程信息、开课列表，执行课程成绩、补考名单替换(传入的学生信息，需要预先setTeachingGuiPlan)
	 * @param stu 传入的学生信息，需要预先setTeachingGuiPlan
	 * @param 允许为null，为null时根据学生当前学籍信息进行关联查询
	 * @param 允许为null，为null时根据学生当前学籍信息进行关联查询
	 */
	public String updateResumeSchoolResult_new(StudentInfo stu,TeachingPlanCourse plancourse,TeachingPlanCourseStatus cs) throws WebException;
	
	/**
	 * 模拟更新学生学习计划,用于复学成绩的标记删除
	 * @throws Exception 
	 */	
	public List<StudentLearnPlan> updateStudentLearnPlanForMarkDelete(StudentInfo stu,TeachingGuidePlan teachingPlan,String brSchoolid) throws Exception;
	
	public List<StuChangeInfo> getStuChangeInfoByMonth(String brschoolId) throws ServiceException;
}
