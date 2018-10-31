package com.hnjk.platform.system.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.CommonUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpUrlConnectionUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.mas.MasClient;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.util.TMSStatueCode;
import com.hnjk.security.verifyimage.CaptchaServiceSingleton;

/** 
 * MAS短信
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Dec 14, 2016 6:01:19 PM 
 * 
 */
@Controller
public class MasController extends BaseSupportController {

	private static final long serialVersionUID = 8220388741432126934L;
	private static final int SMS_CONFIG_SAMEADDRMAXCNT = 10;
	private static final int SMS_CONFIG_SAMEPHONEMAXCNT = 10;
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;
	
	/**
	 * 发送短信验证码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/mas/getMsgAuthCode.html")
	public void sendMsgAuthCode(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message  = "发送成功";
//		MasClient masClient = null;
		try {
			do{
				// 获取手机号
				String phone = request.getParameter("phone");
				String authCode = ExStringUtils.trim(request.getParameter("authCode"));
				boolean isValided = CaptchaServiceSingleton.getInstance().validateAuthCode(authCode, request.getSession());
				if(!isValided){
					statusCode = 300;
					message = "验证码不正确!";
					continue;
				}
				if(ExStringUtils.isEmpty(phone)){
					statusCode = 300;
					message = "手机号码不能为空";
					continue;
				}
				 // 限制：两分钟之内只能发一条，单个IP针对一个手机号一天只最多发10条
				InetAddress 	inetAddress = InetAddress.getLocalHost();
				String ip = inetAddress.getHostAddress();
				StringBuffer ipPhone = new StringBuffer(ip);
				ipPhone.append(phone);
				Integer addrPhoneTimes = memcachedManager.get(ipPhone.toString());
				if(addrPhoneTimes==null){
					addrPhoneTimes=0;
				}
				if(addrPhoneTimes>SMS_CONFIG_SAMEADDRMAXCNT){
					statusCode = 300;
					message = "当前IP["+ip+"]今天最多发送"+SMS_CONFIG_SAMEADDRMAXCNT+"条短信!";
					continue;
				}
				// 同一号码，当天最多发送条数
				StringBuffer samePhone = new StringBuffer("samePhone");
				samePhone.append(phone);
				Integer samePhoneTimes = memcachedManager.get(samePhone.toString());
				if(samePhoneTimes==null){
					samePhoneTimes=0;
				}
				if(samePhoneTimes>SMS_CONFIG_SAMEPHONEMAXCNT){
					statusCode = 300;
					message = "当前手机号["+phone+"]今天最多发送"+SMS_CONFIG_SAMEPHONEMAXCNT+"条短信!";
					continue;
				}
				
				// 生成六位数字验证码
				String msgAuthCode = CommonUtils.createCodeByNum(6);
				// 将手机号（Key）与验证码(Value) 放到缓存中，缓存时间为15分钟，即15分钟有效（或者全局参数设置）
				int time = 15;
				String  validTime = CacheAppManager.getSysConfigurationByCode("validTime").getParamValue();
				if(ExStringUtils.isNotBlank(validTime)){
					time = Integer.parseInt(ExStringUtils.trim(validTime));
				}
				memcachedManager.put(phone, time*60, msgAuthCode);
				// 发送
//				String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
				String content = "您本次请求的验证码为"+msgAuthCode+"，"+time+"分钟内有效。";
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				String returnCode="";
				Map<String,Object> resturnMap = new HashMap<String, Object>();
				if("11078".equals(schoolCode)){//广大
					returnCode= sendSMS(content, phone);
				}else if("11846".equals(schoolCode)){//广外
					resturnMap = sendTMS(content, phone);
					returnCode= (String) resturnMap.get("result");
				}
				/*
				 * MAS短信
				 * masClient =  new MasClient();
				String returnCode = masClient.sendSM(phone, content, CommonUtils.randomInt(0, 99999999));
				*/
				if(!MasClient.MAS_STATUS_SUCCESS.equals(returnCode)){
					statusCode = 300;
					message = "发送失败,"+resturnMap.get("message");
				}else{
					request.getSession().removeAttribute("imageCaptcha");  
					addrPhoneTimes = addrPhoneTimes+1;
					memcachedManager.put(ipPhone.toString(), 60*60*24, addrPhoneTimes);
					samePhoneTimes = samePhoneTimes+1;
					memcachedManager.put(samePhone.toString(), 60*60*24, samePhoneTimes);
				}
				/*Thread.sleep(10000);
				System.out.println("10s:"+memcachedManager.get(phone));
				Thread.sleep(90000);
				System.out.println("90s:"+memcachedManager.get(phone));*/
			} while(false);
		} catch (Exception e) {
			statusCode = 300;
			message = "发送失败";
			logger.error("发送短信验证码出错", e);
		}finally {
			/*if(masClient!=null){
				masClient.release();
			}*/
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 发送短信--联通（目前广大使用）
	 * @param content
	 * @param phones
	 * @return
	 * @throws Exception
	 */
	private String sendSMS(String content, String phones) {
		String result = MasClient.MAS_STATUS_SUCCESS;
		
		try {
			do{
				ConfigPropertyUtil confProperty = ConfigPropertyUtil.getInstance();
				StringBuffer param = new StringBuffer();
				param.append("SpCode="+confProperty.getProperty("ltSpCode"));
				param.append("&LoginName="+confProperty.getProperty("ltLoginName"));
				param.append("&Password="+confProperty.getProperty("ltPassword"));
				param.append("&MessageContent="+HttpUrlConnectionUtils.urlEncode(content, "GBK"));
				param.append("&UserNumber="+phones);
				param.append("&SerialNumber=0"+ExDateUtils.formatDateStr(new Date(), "yyyyMMddHHmmssFFF"));
				param.append("&ScheduleTime=");
				param.append("&f=1");
				// 发送短信
				String returnResult = HttpUrlConnectionUtils.postRequestCharset(confProperty.getProperty("ltsmUrl"), param.toString(),"GBK");
				if(ExStringUtils.isEmpty(returnResult)){
					result = MasClient.MAS_STATUS_FAIL;
					continue;
				}
				String[] results = returnResult.split("&");
				for(String r : results){
					if("result".equals(r.split("=")[0])){
						if(Integer.parseInt(r.split("=")[1])!=0){
							logger.info("短信发送失败结果: "+returnResult);
							result = MasClient.MAS_STATUS_FAIL;
							break;
						}
					}
				}
			}while(false);
		} catch (RuntimeException e) {
			result = MasClient.MAS_STATUS_FAIL;
			logger.error("发送短信--联通失败", e);
		} catch (ParseException e) {
			result = MasClient.MAS_STATUS_FAIL;
			logger.error("发送短信--联通失败", e);
		}
		
		return result;
	}
	
	/**
	 * 发送短信-电信（目前广外使用）
	 * @param content
	 * @param phones
	 * @return
	 * @throws DocumentException 
	 */
	private Map<String,Object> sendTMS(String content, String phones) throws DocumentException {
		String result = MasClient.MAS_STATUS_SUCCESS;
		String message="发送成功";
		Map<String,Object> returnMap = new HashMap<String, Object>();
		try {
			do{
				ConfigPropertyUtil confProperty = ConfigPropertyUtil.getInstance();
				StringBuffer param = new StringBuffer();
				param.append("&username="+confProperty.getProperty("t.userName"));
				param.append("&userpwd="+confProperty.getProperty("t.userPwd"));
				param.append("&content="+HttpUrlConnectionUtils.urlEncode(content, confProperty.getProperty("t.encoding")));
				param.append("&mobiles="+phones);
				param.append("&mobilecount=1");
				param.append("&logid=");
//				param.append("&username=gdxyjy");
//				param.append("&userpwd=850518"+confProperty.getProperty("t.userPwd"));
//				param.append("&content="+HttpUrlConnectionUtils.urlEncode(content, "UTF-8"));
//				param.append("&mobiles=15920383174");
//				param.append("&mobilecount=1");
				
				// 发送短信
				String returnType = confProperty.getProperty("t.returnType");
				if("xml".equalsIgnoreCase(returnType)){
					String returnResult=HttpUrlConnectionUtils.postRequestCharset(confProperty.getProperty("t.url"), param.toString(),confProperty.getProperty("t.encoding"));
					org.dom4j.Document doc =XMLUtils.parseText(XMLUtils.convertWithBOM(returnResult,confProperty.getProperty("t.encoding")));
					Node node_Code = doc.selectSingleNode(confProperty.getProperty("t.node.result"));
					String errorCode = node_Code.getText();		
					if(!"1".equals(errorCode)){
						result = MasClient.MAS_STATUS_FAIL;
						message="原因：短信服务器发送失败，失败代码："+errorCode+":"+TMSStatueCode.getText(Integer.valueOf(errorCode))+"。请联系系统管理员处理短信服务器的问题";						
					}
					
				}else{//当前只处理xml形式的返回值，以后有其他接口再补充
					result = MasClient.MAS_STATUS_FAIL;
					message="不匹配的返回值类型";
				}
				
			}while(false);
		} catch (RuntimeException e) {
			result = MasClient.MAS_STATUS_FAIL;
			logger.error("发送短信--电信失败", e);
		} finally{
			returnMap.put("result", result);
			returnMap.put("message", message);
		}		
		return returnMap;
	}
	
	public static void main(String[] argv) throws ParserConfigurationException, SAXException, IOException{
		StringBuffer param = new StringBuffer();
		String content = "【广外】祝您快乐每一天！";
		param.append("&username=gdxyjy");
		param.append("&userpwd=850518");
		param.append("&content="+HttpUrlConnectionUtils.urlEncode(content, "UTF-8"));
		param.append("&mobiles=15920383174");
		param.append("&mobilecount=1");
		param.append("&logid=");
		String url="http://112.74.26.63/index.php?action=interface&op=sendmess";
//		String returnResult=HttpUrlConnectionUtils.postRequestCharset(url, param.toString(),"utf-8");
		String returnResult="﻿<?xml version=\"1.0\" encoding=\"utf-8\"?><sendresult><errorcode>1</errorcode><message>提交成功</message><SMSID>19315506</SMSID></sendresult>";
//		String returnResult="﻿<?xml version=\"1.0\" encoding=\"utf-8\"?><sendresult><errorcode>1</errorcode><message>提交成功</message><SMSID>19315506</SMSID></sendresult>";
		logger.debug("远程接口返回的字符串:"+returnResult);
		try {
//			System.out.println(ExStringUtils.getEncoding(returnResult));
//			String _result = new String(returnResult.getBytes("GBK"),"GBK");
//			System.out.println(ExStringUtils.getEncoding(_result));
			org.dom4j.Document doc =XMLUtils.parseText(XMLUtils.convertWithBOM(returnResult,"UTF-8"));
			Node node = doc.selectSingleNode("/sendresult/errorcode");
//			Node node = doc.selectSingleNode("/sendresult/errorcode");
//			System.out.println("节点名称："+node.getName());
//			System.out.println("节点值："+node.getText());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
