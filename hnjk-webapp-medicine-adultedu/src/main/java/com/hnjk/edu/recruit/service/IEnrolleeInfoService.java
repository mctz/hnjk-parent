package com.hnjk.edu.recruit.service;

import java.sql.BatchUpdateException;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.security.model.OrgUnit;

/**
 * 学生报名服务接口<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-17 上午09:49:38
 * @see 
 * @version 1.0
*/
public interface IEnrolleeInfoService extends IBaseService<EnrolleeInfo> {

	/**
	   * 根据条件查找学生报名信息
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findEnrolleeByCondition(Map<String, Object> condition,  Page page) throws ServiceException; 
	/**
	   * 根据条件查找学生报名信息
	 * @param condition
	 * @param order
	 * @param page
	 * @return List
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> findEnrolleeInfoForRegisterList(Map<String, Object> condition) throws ServiceException;
	/**
	   * 根据条件查找学生报名列表
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List findListByCondition(Map<String, Object> condition) throws ServiceException; 
	
	 

	/**
	 * 查找出未安排入学考试座位且入学资格审核通过的学生-普通考
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	//public Page findNoSeatEnrollee(Map<String, Object> condition,  Page page) throws ServiceException; 
	/**
	 * 查找出未安排入学考试座位且入学资格审核通过的学生-机考
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	//public Page findNoSeatEnrolleeForExamPlan(Map<String, Object> condition,  Page page)throws ServiceException;
	
	/**
	 * 生成学号
	 */
	public String genStudentid(EnrolleeInfo ei);
	
	/**
	 * 获得学号的前缀-前12位
	 * 生成规则：
	 * 1-4位招生计划年度，第5位是季度（春-0，秋-1），
	 * 6-8位是学习中心编号，第9位是层次代码，
	 * 10-11是专业编号,第12位是学习形式
	 */
	public String getStudentidPrefix(EnrolleeInfo ei);
	
	/**
	 * 获得学号后缀-3位流水号
	 */
	public String getStudentidSuffix(String prefix);

	/**
	 * 生成准考证号
	 */
	public String genExamCertificateNo(EnrolleeInfo ei);
	
	/**
	 * 获得准考证号前缀-前11位
	 * 生成规则：
	 * 1-4位招生计划年度，第5位是季度（春-0，秋-1），
	 * 6-8位是学习中心编号，第9位是层次代码，10-11是专业编号
	 */
	public String getExamCertificateNoPrefix(EnrolleeInfo ei);
	
	/**
	 * 获得准考证后缀-3位流水号
	 */
	public String getExamCertificateNoSuffix(String prefix);
	
	/**
	 * 获得准考证号前缀-前11位
	 * 生成规则：
	 * 1-4位招生计划年度，第5位是季度（春-0，秋-1），
	 * 6-8位是学习中心编号，第9位是层次代码，10-11是专业编号
	 * @param recruitMajor 招生专业
	 * @param branchSchool 学习中心
	 * @return
	 */
	public String  genExamCertificateNoPrefix(RecruitMajor recruitMajor, OrgUnit branchSchool);
	
	/**
	 * 生成考生号
	 * @param ei
	 * @return
	 */
	public String  genEnrolleeCode(EnrolleeInfo ei);
	
	/**
	 * 生成考生号前缀
	 * 生成规则：
	 * 1-2位年度，3-9位是固定字符"W110571"
	 * 10-11位是季度（春-10，秋-11）
	 * 
	 * @param ei
	 * @return
	 */
	public String  genEnrolleeCodePrefix(EnrolleeInfo ei);
	
	/**
	 * 生成考生号后4位流水号
	 * @param prefix
	 * @return
	 */
	public String  genEnrolleeCodeSuffix(String prefix);
	
	/**
	 * 生成注册号
	 * 生成规则
     *两位入学年份（例如：2009年入学即09）
     *W1（W1即网络教育编码）+一位培养层次编码（1高起本；2高起专；3专升本；4专业硕士；5
     *本科第二学位；6研究生课程进修；7其他）+五位试点高校编码+八位随机数（数字编码）。
     *倒数第五位0表示为春季学生，1表示为秋季学生。如：10W121057100000001（2010年入学高起专春季学生）
	 * @param ei
	 * @return
	 */
	public String  genEnrolleeRegistorNo(EnrolleeInfo ei);
	
	/**
	 * 生成注册号前缀
	 * 
	 * @param ei
	 * @return
	 */
	public String  genEnrolleeRegistorNoPrefix(EnrolleeInfo ei);
	
	/**
	 * 生成注册号后缀
	 * @param season 季度（春-0，秋-1）
	 * @return
	 */
	public String  genEnrolleeRegistorNoSuffix(String season);
	
	/**
	 * 注册号是否已存在
	 * @param registorNo
	 * @return
	 */
	public boolean isContainsRegistorNo(String registorNo);
	
	/**
	 * 计算总分原始分和总分标准分-非机考
	 * @param recruitPlanId
	 * @param courseGroup
	 * @param x
	 */
	//public void calTotalpoint(String recruitPlanId,String courseGroup,String isMachineExam,double x);
	
	/**
	 * 查询已录取，待注册的学生列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findEnrolleeInfoForRegisterList(Map<String, Object> condition,Page objPage) throws ServiceException;
	
	/**
	 * 查询未安排座位列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	//public Page findNoSeatEnrolleeInfoList(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 查询给定学生是否安排座位、成绩是否为空,报名资格、入学资格是否审核
	 * @param enrolleeInfo
	 * @return
	 */
	//public boolean isAllowEidtOrDel(EnrolleeInfo enrolleeInfo) ;
	
	/**
	 * 保存或更新报名
	 * @param sbi
	 * @param ei
	 * @throws ServiceException
	 */
	public void saveOrUpdateEnrolleeinfo(StudentBaseInfo sbi,EnrolleeInfo ei )throws ServiceException;
	/**
	 * 批量保存或更新报名信息
	 * @param enrolleeInfoList
	 * @throws ServiceException
	 */
	void batchSaveOrUpdateEnrolleeInfo(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException;
	
	/**
	 * 根据3位前缀获得最大后缀集合
	 * @param enrolleeInfoList
	 * @return
	 * @throws ServiceException
	 * (1)准学号
	 */
	public List<Map<String,Object>> getMaxStudyNoPrefixMap(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException;
	
	//注册时学号生成
	public List<Map<String,Object>>	getMaxStudyNoPrefixMapRegistered(int a,int b,int c,int d) throws ServiceException;
	
	
	
	/**
	 * 根据9位前缀获得最大后缀集合
	 * @param enrolleeInfoList
	 * @return
	 * @throws ServiceException
	 * (2)准学号
	 */
	public List<Map<String,Object>> genMaxStudyNoPrefixMap(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException;

	
	
	/**
	 * 根据9位前缀获得最大后缀集合
	 * @param enrolleeInfoList
	 * @return
	 * @throws ServiceException
	 * (3)准学号
	 */
	public List<Map<String,Object>> genMaxStudyNoPrefixMap2(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException;

	
	/**
	 * 安徽医学院学号生成
	 * @param enrolleeInfoList
	 * @return
	 * @throws ServiceException
	 * (4)准学号
	 */
	public List<Map<String,Object>> genMaxStudyNoPrefixAHY() throws ServiceException;

	/**
	 * 根据1-3位，7-8位前缀获得最大后缀集合
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> getMaxStudyNoPrefixMapRegistered2(int a,int b,int c,int d,int e,int f) throws ServiceException;
	
	/**
	 * 按教学点、专业、姓名首字母排序
	 * @param enrolleeInfoIds
	 * @return
	 * @throws ServiceException
	 */
	public String[] orderByPinyinHeadChar(String[] enrolleeInfoIds) throws ServiceException;
	
	/**
	 * 获取某招生计划最大的录取编号
	 * @param recruitPlanId
	 * @return
	 * @throws ServiceException
	 */
	public Integer getMaxEnrollNO(String recruitPlanId) throws ServiceException;
	/**
	 * 通过学生的身份证号，获取学生的部分录取信息
	 * @param certNum
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>>  getEnrollInfoByCertNum(String certNum) throws ServiceException;
	
	/**
	 * 获取当前数据库中  以教学点、年级 为单位的最大注册流水号
	 * @param gradeid
	 * @param unitid
	 * @return 当前最大的流水号
	 * @throws ServiceException
	 */
	public Map<String, Object> getMaxRegistorSerialNO(String gradeid,String unitid) throws ServiceException;
}

