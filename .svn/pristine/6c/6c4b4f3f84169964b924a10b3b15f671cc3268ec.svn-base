package com.hnjk.edu.finance.util;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hnjk.core.foundation.utils.AesUtils;
import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpUrlConnectionUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.vo.HeadVO;
import com.hnjk.edu.finance.vo.OrderInfoVO;
import com.hnjk.edu.finance.vo.StudentInfoVO;

/** 
 * 通联支付工具类
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 8, 2016 11:37:06 AM 
 * 
 */
public class TonlyPayUtil {

	private static String KEY = null;
	private static String TLPayUrl =null;
	
	static {
		ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
		KEY = property.getProperty("TLKey");
		TLPayUrl = property.getProperty("TLPayUrl");
	}
	
	/**
	 * 创建请求HEAD节点
	 * 
	 * @param headVO
	 * @return
	 */
	public static Document createHead(HeadVO headVO){
		// 生成头文件
		Document document = DocumentHelper.createDocument();
		// 根节点
		Element root = document.addElement("ENVELOPE");
		// head节点
		Element head = root.addElement("HEAD");
		// 服务接口编号
		XMLUtils.addElement(head, "SERVICE_CODE", headVO.getServiceCode());
		// 接入方编号
		XMLUtils.addElement(head, "VISITOR_ID", headVO.getVisitorId());
		// 请求发送时间
		XMLUtils.addElement(head, "SEND_TIME", headVO.getSendTime());
		// 处理结果通知url,可以为空
		XMLUtils.addElement(head, "RECEIVE_URL",ExStringUtils.defaultIfEmpty(headVO.getReceiveUrl(), ""));
		
		return document;
	}
	
	/**
	 * 操作学生信息（添加和修改）
	 * 
	 * @param headVO
	 * @param studentInfoList
	 * @return
	 * @throws Exception
	 */
	public static Document operateStudentInfo(HeadVO headVO,List<StudentInfoVO> studentInfoList) throws Exception{
		Document doc = createHead(headVO);
		// 获取根节点
		Element root = doc.getRootElement();
		// 获取头节点
		Element head = XMLUtils.getChild(root, "HEAD");
		// body节点
		Element body = root.addElement("BODY");
		// dataList节点，可以有多个，最多50个
		// 新增学生信息
		Element dataList = null;
		if(ExCollectionUtils.isNotEmpty(studentInfoList)){
			for(StudentInfoVO studentInfo : studentInfoList){
				dataList = body.addElement("DATALIST");
				XMLUtils.addElement(dataList, "STUDENT_ID", studentInfo.getStudentId());
				XMLUtils.addElement(dataList, "STUDENT_NAME", studentInfo.getStudentName());
				XMLUtils.addElement(dataList, "ID_VERIFY_CODE", AesUtils.encodeBufferBase64(AesUtils.aesEncrypt(studentInfo.getIdVerifyCode(), KEY).getBytes()));// 身份证号后6位
				XMLUtils.addElement(dataList, "COLLEGE", studentInfo.getCollege());
				XMLUtils.addElement(dataList, "MAJOR", studentInfo.getMajor());
				XMLUtils.addElement(dataList, "CLASS", studentInfo.getClasses());
			}
		}
		// 报文签名
			StringBuilder sign = new StringBuilder();
			sign.append(body.asXML()).append(KEY);
			String signMsg = BaseSecurityCodeUtils.getMD5(sign.toString()).toUpperCase();
			XMLUtils.addElement(head, "SIGN_MSG",signMsg);
			// 参数
			StringBuilder param = new StringBuilder("data=");
			param.append(doc.asXML());
			// 返回的响应信息
			String results = HttpUrlConnectionUtils.postRequest(TLPayUrl, param.toString());
//			System.out.println(results);
			
			return XMLUtils.parseText(results);
	}
	
	/**
	 * 操作订单（增加或修改）
	 * 
	 * @param headVO
	 * @param projectNo
	 * @param batchNo
	 * @param orderInfoList
	 * @return
	 * @throws Exception
	 */
	public static Document operateOrderInfo(HeadVO headVO,String projectNo,String batchNo,List<OrderInfoVO> orderInfoList) throws Exception {
		Document doc = createHead(headVO);
		// 获取根节点
		Element root = doc.getRootElement();
		// 获取头节点
		Element head = XMLUtils.getChild(root, "HEAD");
		// body节点
		Element body = root.addElement("BODY");
		// dataList节点，可以有多个，最多50个
		// 新增订单
		XMLUtils.addElement(body, "PROJECT_NO", projectNo);
		XMLUtils.addElement(body, "BATCH_NO", batchNo);
		if(ExCollectionUtils.isNotEmpty(orderInfoList)){
			Element dataList = null;
			for(OrderInfoVO orderInfo : orderInfoList){
				dataList = body.addElement("DATALIST");
				XMLUtils.addElement(dataList, "ORDER_NO", orderInfo.getOrderNo());
				XMLUtils.addElement(dataList, "STUDENT_ID", orderInfo.getStudentId());
				XMLUtils.addElement(dataList, "AMOUNT", orderInfo.getAmount());
				XMLUtils.addElement(dataList, "REMARK", orderInfo.getRemark());
			}
		}
		// 报文签名
		StringBuilder sign = new StringBuilder();
		sign.append(body.asXML()).append(KEY);
		String signMsg = BaseSecurityCodeUtils.getMD5(sign.toString()).toUpperCase();
		XMLUtils.addElement(head, "SIGN_MSG",signMsg);
		StringBuilder param = new StringBuilder("data=");
		param.append(doc.asXML());
		// 返回的响应信息
		String results = HttpUrlConnectionUtils.postRequest(TLPayUrl, param.toString());
		
		return XMLUtils.parseText(results);
	}
	
	/**
	 * 查询订单处理结果
	 * 
	 * @param headVO
	 * @param orderNo
	 * @return
	 */
	public static Document queryPayment(HeadVO headVO, String orderNo) throws Exception {
		Document doc = createHead(headVO);
		// 获取根节点
		Element root = doc.getRootElement();
		// 获取头节点
		Element head = XMLUtils.getChild(root, "HEAD");
		// body节点
		Element body = root.addElement("BODY");
		// 单个dataList节点
		Element dataList = body.addElement("DATALIST");
		XMLUtils.addElement(dataList, "ORDER_NO", orderNo);
		// 报文签名
		StringBuilder sign = new StringBuilder();
		sign.append(body.asXML()).append(KEY);
		String signMsg = BaseSecurityCodeUtils.getMD5(sign.toString()).toUpperCase();
		XMLUtils.addElement(head, "SIGN_MSG",signMsg);
		StringBuilder param = new StringBuilder("data=");
		param.append(doc.asXML());
		// 返回的响应信息
		String results = HttpUrlConnectionUtils.postRequest(TLPayUrl, param.toString());
		
		return XMLUtils.parseText(results);
	}
	
	/**
	 * 注销订单信息,撤销学生注册缴费记录
	 * @param headVO
	 * @param tempStudentFeeList
	 * @return
	 * @throws Exception
	 */
	public static Document cancelOrder(HeadVO headVO,List<TempStudentFee> tempStudentFeeList) throws Exception{
		Document doc = createHead(headVO);
		// 获取根节点
		Element root = doc.getRootElement();
		// 获取头节点
		Element head = XMLUtils.getChild(root, "HEAD");
		// body节点
		Element body = root.addElement("BODY");
		// dataList节点，可以有多个，最多50个
		// 申请注销
		Element dataList = null;
		if(ExCollectionUtils.isNotEmpty(tempStudentFeeList)){
			for(TempStudentFee tempStudentFee : tempStudentFeeList){
				dataList = body.addElement("DATALIST");
				XMLUtils.addElement(dataList, "EDU_OREDER_NO", tempStudentFee.getEduOrderNo());
			}
		}
		// 报文签名
			StringBuilder sign = new StringBuilder();
			sign.append(body.asXML()).append(KEY);
			String signMsg = BaseSecurityCodeUtils.getMD5(sign.toString()).toUpperCase();
			XMLUtils.addElement(head, "SIGN_MSG",signMsg);
			// 参数
			StringBuilder param = new StringBuilder("data=");
			param.append(doc.asXML());
			// 返回的响应信息
			String results = HttpUrlConnectionUtils.postRequest(TLPayUrl, param.toString());
			
			return XMLUtils.parseText(results);
	}
	
	/**
	 * 申请修改学生信息接口
	 * @param headVO
	 * @param studentInfoList
	 * @return
	 * @throws Exception
	 */
	public static Document modifyStudentInfo(HeadVO headVO,List<Map<String,Object>> studentInfoList) throws Exception{
		Document doc = createHead(headVO);
		// 获取根节点
		Element root = doc.getRootElement();
		// 获取头节点
		Element head = XMLUtils.getChild(root, "HEAD");
		// body节点
		Element body = root.addElement("BODY");
		// dataList节点，可以有多个，最多50个
		// 修改学生信息
		Element dataList = null;
		if(ExCollectionUtils.isNotEmpty(studentInfoList)){
			for(Map<String,Object> studentInfo : studentInfoList){//有学生id才给修改,其余都是可选的
				if(studentInfo.containsKey("STUDENT_ID")){
					dataList = body.addElement("DATALIST");
					XMLUtils.addElement(dataList, "STUDENT_ID", (String)studentInfo.get("STUDENT_ID"));
				}else{
					continue;
				}
				XMLUtils.addElement(dataList, "STUDENT_NAME", ExStringUtils.defaultIfEmpty((String)studentInfo.get("STUDENT_NAME"), ""));
				XMLUtils.addElement(dataList, "ID_VERIFY_CODE", ExStringUtils.defaultIfEmpty((String)studentInfo.get("ID_VERIFY_CODE"), ""));
				XMLUtils.addElement(dataList, "COLLEGE", ExStringUtils.defaultIfEmpty((String)studentInfo.get("COLLEGE"), ""));
				XMLUtils.addElement(dataList, "MAJOR", ExStringUtils.defaultIfEmpty((String)studentInfo.get("MAJOR"), ""));
				XMLUtils.addElement(dataList, "CLASS", ExStringUtils.defaultIfEmpty((String)studentInfo.get("CLASS"), ""));
			}
		}
		// 报文签名
			StringBuilder sign = new StringBuilder();
			sign.append(body.asXML()).append(KEY);
			String signMsg = BaseSecurityCodeUtils.getMD5(sign.toString()).toUpperCase();
			XMLUtils.addElement(head, "SIGN_MSG",signMsg);
			// 参数
			StringBuilder param = new StringBuilder("data=");
			param.append(doc.asXML());
			// 返回的响应信息
			String results = HttpUrlConnectionUtils.postRequest(TLPayUrl, param.toString());
//			System.out.println(results);
			
			return XMLUtils.parseText(results);
	}
	
	/**
	 * 支付类型的转换（通联->学苑）
	 * @param payType
	 * @return
	 */
	public static String convertPayMethod(String payType){
		String payMethod = "";
		if(ExStringUtils.equals(payType, "09")){//09-网银支付
			payMethod = Constants.FEE_PAYMETHOD_ECHINABANK;
		} else if(ExStringUtils.equals(payType, "10")){// 10-POS支付
			payMethod = Constants.FEE_PAYMETHOD_POS;
		}
		
		return payMethod;
	}
}
