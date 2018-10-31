package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.vo.HeadVO;
import com.hnjk.edu.finance.vo.PayOfflineVo;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.roll.model.StudentInfo;

/** 
 * 学生第一次缴费注册信息（供网上缴费使用）
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 11, 2016 4:02:27 PM 
 * 
 */
public interface ITempStudentFeeService extends IBaseService<	TempStudentFee> {

	/**
	 * 根据条件获取学生注册缴费信息列表-分页
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 */
	public Page findByContidion(Map<String, Object> condition, Page objPage);
	
	/**
	 * 根据条件获取学生注册缴费信息列表
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 */
	public List<TempStudentFee> findListByContidion(Map<String, Object> condition);
	
	/**
	 * 生成缴费订单（注册时使用）
	 * 
	 * @param enrolleeInfoList
	 * @param chargingItems
	 * @return
	 */
	public Map<String, Object> createTempStuFee(List<EnrolleeInfo> enrolleeInfoList, String chargingItems);
	
	/**
	 * 处理新增订单返回的异步信息
	 * 
	 * @param xmlData
	 */
	public void handleOrderResult(String xmlData);
	
	/**
	 * 生成学生缴费信息（供第三方使用）
	 * 
	 * @param studentPaymentList
	 * @return
	 */
	public Map<String, Object> createPayTempStuFee(List<StudentPayment> studentPaymentList);
	
	/**
	 * 根据支付状态和教育系统订单号获取实体
	 * 
	 * @param payStatus
	 * @param eduOrderNo
	 * @return
	 */
	public TempStudentFee findByPayStatusAndOrderNo(String payStatus, String eduOrderNo);
	
	/**
	 * 补缴费记录
	 * 
	 * @param resourceid
	 * @param headVO
	 * @return
	 */
	public Map<String, Object> makeRecord(String resourceid,HeadVO headVO);
	
	/**
	 * 根据长号（考生号或准考证号）和批次号获取实体类
	 * 
	 * @param examCertificateNo
	 * @param batchNo
	 * @param hasStudentInfo
	 * @param chargingItems
	 * @return
	 */
	public TempStudentFee getByExamNoAndBatchNo(String examCertificateNo, String batchNo, String hasStudentInfo,String chargingItems);
	
	/**
	 * 处理撤销订单返回的异步信息
	 * 
	 * @param xmlData
	 */
	public void handleDeleteResult(String xmlData);
	
	/**
	 * 处理修改学生返回的异步信息
	 * @param xmlData
	 */
	public void handleEditStudentInfo(String xmlData);
	
	/**
	 * 处理撤销订单返回的异步信息(第二种)
	 * 
	 * @param xmlData
	 */
	public void handleDeleteResultSecond(String xmlData);
	/**
	 * socketClient查询缴费信息
	 * @param request
	 * @return
	 */
	public String queryAssessment(String request);

	/**
	 * socketClient缴费请求处理
	 * @param request
	 * @return
	 */
	public String paymentRequest(String request);
	/**
	 * 根据教育系统订单号查询金额是否相等
	 * @param eduNum
	 * @return
	 */
	TempStudentFee isSync(String eduNum, String fee);
	
	/**
	 * 创建注册缴费信息记录
	 * 
	 * @param batchNo
	 * @param sp
	 * @param examCertificateNo
	 * @param amount
	 * @param yearInfo
	 * @return
	 */
	public TempStudentFee createTempStudentFee(String batchNo, StudentPayment sp, String examCertificateNo, double amount,YearInfo yearInfo);
	
	/**
	 * 处理导入线下缴费逻辑
	 * 
	 * @param payOfflineVoList
	 * @return
	 */
	public Map<String, Object> handlePayOfflineInfoImport(List<PayOfflineVo> payOfflineVoList);
	
	/**
	 * 根据准考证号删除未付款的预缴费订单
	 * @param examCertificateNo
	 */
	public void deleteByExamCertificateNo(String examCertificateNo);
	
	/**
	 * 生成教材费订单
	 * @param studentInfoList
	 * @return
	 */
	public Map<String, Object> createTextbookFee(List<StudentInfo> studentInfoList);
	/**
	 * 根据身份证号查询学生的订单信息
	 * @param certNum
	 * @return
	 */
	public List<TempStudentFee> findByCertnum(String certNum);

	void updatePaymentDetails(TempStudentFee tsf);
}
