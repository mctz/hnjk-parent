package com.hnjk.edu.finance.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.StuReturnFeeCommissionInfoVo;
import com.hnjk.edu.finance.vo.StudentPaymentVo;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.security.model.User;
/**
 * 学生缴费标准服务接口.
 * <code>IStudentPaymentService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-12 下午01:07:36
 * @see 
 * @version 1.0
 */
public interface IStudentPaymentService extends IBaseService<StudentPayment> {
	/**
	 * 根据录取信息生成学生缴费标准
	 * @param enrolleeInfoList 录取的学生信息
	 * @throws ServiceException
	 */
	void createStudentPayment(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException;
	/**
	 * 根据录取信息删除学生缴费标准
	 * @param enrolleeInfoList
	 * @throws ServiceException
	 */
	void deleteStudentPayment(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException;
	/**
	 * 分页查询学生缴费标准
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findStudentPaymentByContidion(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查询列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<StudentPayment> findStudentPaymentByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 查找多次缓缴记录
	 * @param studentPayment
	 * @return
	 * @throws ServiceException
	 */
	List<StudentPayment> findDeferStudentPayment(StudentPayment studentPayment) throws ServiceException;
	/**
	 * 缴费情况统计
	 * @param year
	 * @param brSchool
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> statStudentPayment(Map<String, Object> condition, String byType) throws ServiceException;
	Page findStudentPaymentStuByContidion(Map<String, Object> condition,Page objPage) throws ServiceException;
	List findStudentPaymentStu1ByContidion(Map<String, Object> condition)throws ServiceException;
	
	/**
	 * 给考生生成缴费记录
	 * @param studentInfoList
	 * @throws ServiceException
	 */
	Map<String, Object>generateStudentFeeRecord(List<StudentInfo>studentInfoList) throws ServiceException;
	
	/**
	 * 处理学生缴费信息导入
	 * @param modelList
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> handleStudentPaymentImport(List<StudentPaymentVo> modelList, User user) throws ServiceException;
	/**
	 * 处理学生缴费信息导入
	 * @param modelList
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> handleStudentPaymentImport_new(List<StudentPaymentVo> modelList, User user) throws ServiceException;
	
	/**
	 * 处理学生缴费信息导入(右江医)
	 * @param modelList
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> handleStudentPaymentImport1(List<StudentPaymentVo> modelList, User user) throws ServiceException;
	
	/**
	 * 缴费
	 * @param studentPayment
	 * @param payDetailsVO
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> payFee(StudentPayment studentPayment, PayDetailsVO payDetailsVO,User user) throws ServiceException;
	
	/**
	 * 验证缴费金额
	 * @param facepay
	 * @param studentPayment
	 * @return
	 */
	public Map<String, Object> validateFacepay(Double facepay,StudentPayment studentPayment) ;
	
	/**
	 * 网上缴费注册
	 * 
	 * @param majorKey
	 * @param uniqueId
	 * @param user
	 * @param payDetailsVO
	 * @param tempStudentFee
	 * @return
	 */
	public Map<String, Object> payOnline(String majorKey,int uniqueId,User user, PayDetailsVO payDetailsVO,TempStudentFee tempStudentFee);
	
	/**
	 * 获取对账单（通过通联接口）
	 * @param serverUrl
	 * @param tradeDate
	 * @param merNo
	 * @param key
	 * @return
	 */
	public Map<String, Object> getStatementByDate(String serverUrl,String tradeDate, String merNo, String key);
	
	/**
	 * 获取退费信息
	 * @param map
	 * @return
	 */
	public List<StuReturnFeeCommissionInfoVo> getStuFeeCommissionInfo(Map<String, Object> map);
	
	/**
	 * 按日期查询已缴费的学生缴费信息
	 * @param startDate
	 * @param endDate
	 */
	public void queryBatchForGW(String startDate,String endDate);
	
	/**
	 * 根据身份证号获取学生缴费记录
	 * @param certNum
	 */
	public void queryForGW(String certNum);
	
	/**
	 * 访问第三方缴费查询接口
	 * @param serverUrlAndParam
	 * @param queryParamMap
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public StringBuffer queryPaymentForGW(StringBuffer serverUrlAndParam, Map<String, Object> queryParamMap)
			throws UnsupportedEncodingException, ParseException;
	
	/**
	 * 缴费--教材费
	 * @param tempStudentFee
	 * @param payDetailsVO
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> payFeeForTextbook(TempStudentFee tempStudentFee,PayDetailsVO payDetailsVO, User user) throws ServiceException;
}
