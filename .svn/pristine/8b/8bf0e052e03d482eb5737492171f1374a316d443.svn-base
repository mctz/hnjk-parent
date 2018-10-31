package com.hnjk.core.support.mas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.jasson.im.api.APIClient;
import com.jasson.im.api.RPTItem;

/** 
 * MAS短信客户端
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Dec 2, 2016 4:07:48 PM 
 * 
 */
public class MasClient {

	private Logger logger = LoggerFactory.getLogger(MasClient.class);
	
	public static String MAS_STATUS_SUCCESS="success";// 成功
	public static String MAS_STATUS_FAIL="fail";// 失败
	public static String MAS_STATUS_TOOLONG="tooLong";// 消息内容太长
	private String imIP = "";// 移动代理服务器的 IP 地址
	private String loginName = "";// 接口登录名
	private String loginPWD = "";// 接口登录密码
	private String dbName = "mas";// 数据库名称，默认mas
	private String apiCode = "gdapi114";// 接口编码
	private int dbPort = 48306;
	private APIClient handler = null;// API客户端
	
	public MasClient () {
		handler = new APIClient();
		ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
		imIP = property.getProperty("imIP");
		loginName = property.getProperty("loginName");
		loginPWD = property.getProperty("loginPWD");
		dbName = property.getProperty("dbName");
		apiCode = property.getProperty("apiCode");
		dbPort = property.getProperty("dbPort")==null?48306:Integer.parseInt(property.getProperty("dbPort"));
		init();
	}
	
	/**
	 * 初始化(建立到移动代理服务器的连接，分配所属资源)
	 */
	public String init(){
		String status = MAS_STATUS_FAIL;
//		int connectRe = handler.init(imIP, loginName, loginPWD, apiCode,dbName);// 默认端口3306，广大48306
		int connectRe = handler.init(imIP, loginName, loginPWD, apiCode,dbName,dbPort);
        if(connectRe == APIClient.IMAPI_SUCC){
        	status = MAS_STATUS_SUCCESS;
        	logger.info("MAS服务初始化成功");
        }else if(connectRe == APIClient.IMAPI_CONN_ERR){
        	logger.info("MAS服务连接失败");
        }else if(connectRe == APIClient.IMAPI_API_ERR){
        	logger.info("MAS服务apiID不存在");            
        }
        
        return status;
	}
	
	/**
	 * 发送一条 MT 短信到某个手机号码
	 * <p>
	 *  1、普通短信内容，最大支持 2000 个汉字的短信，超过2000 时会被截断；
	 *  2、短信 ID，0 到 99999999 中的某一整数。确保唯一后可以用来找到对应的回执、回复
	 * </p>
	 * @param mobile 手机号码
	 * @param content 普通短信内容 
	 * @param smID 短信 ID
	 * @return
	 */
	public String sendSM(String mobile, String content , long smID){
		int resultCode = handler.sendSM(mobile, content, smID);
		String status = getResutCode(resultCode);
		 
		return status;
	}
	
	/**
	 * 发送一条 MT 短信到一个手机号码集
	 * <p>
	 *  1、普通短信内容，最大支持 2000 个汉字的短信，超过2000 时会被截断；
	 *  2、短信 ID，0 到 99999999 中的某一整数。确保唯一后可以用来找到对应的回执、回复
	 * </p>
	 * @param mobiles 手机号码的数组
	 * @param content 普通短信内容
	 * @param smID 短信 ID
	 * @return
	 */
	public String sendSM(String[] mobiles, String content, long smID){
		int resultCode = handler.sendSM(mobiles, content, smID);
		String status = getResutCode(resultCode);
		 
		return status;
	}
	
	/**
	 * 发送一条 MT 短信到一个手机号码集
	 * <p>
	 *  1、普通短信内容，最大支持 2000 个汉字的短信，超过2000 时会被截断；
	 *  2、短信 ID，0 到 99999999 中的某一整数。确保唯一后可以用来找到对应的回执；
	 *  3、终端源地址，0 到 99999999 中的某一整数。用作源地址显示在终端上，
	 *       确保唯一后可以用来找到对应的回复
	 * </p>
	 * @param mobiles 手机号码的数组
	 * @param content 普通短信内容
	 * @param smID 短信 ID
	 * @param srcID 终端源地址
	 * @return
	 */
	public String sendSM(String[] mobiles, String content, long smID , long srcID ){
		int resultCode = handler.sendSM(mobiles, content, smID, srcID);
		String status = getResutCode(resultCode);
		
		return status;
	}

	/**
	 * 定时发送一条 MT 短信到一个手机号码集
	 * 
	 * <p>
	 *  1、普通短信内容，最大支持 2000 个汉字的短信，超过2000 时会被截断；
	 *  2、短信 ID，0 到 99999999 中的某一整数。确保唯一后可以用来找到对应的回执；
	 *  3、终端源地址，0 到 99999999 中的某一整数。用作源地址显示在终端上，
	 *       确保唯一后可以用来找到对应的回复
	 *  4、定时发送时间， 格式为“yyyy-MM-dd HH-mm-ss”，为 null 时立即发送
	 * </p>
	 * @param mobiles 手机号码的数组
	 * @param content 普通短信内容
	 * @param sendTime 定时发送时间
	 * @param smID 短信 ID
	 * @param srcID 终端源地址
	 * @return
	 */
	public String sendSM(String[] mobiles, String content, String sendTime, long smID, long srcID) {
		int resultCode = handler.sendSM(mobiles, content, sendTime, smID, srcID);
		String status = getResutCode(resultCode);
		
		return status;
	}
	
	
	/**
	 * 将第三方返回的结果转为系统中结果
	 * 
	 * @param resultCode
	 * @return
	 */
	private String getResutCode( int resultCode) {
		String status = MAS_STATUS_FAIL;
		if(resultCode == APIClient.IMAPI_SUCC) {
			 status = MAS_STATUS_SUCCESS;
	     } else if(resultCode == APIClient.IMAPI_INIT_ERR){
	    	 logger.info("未初始化");
	     }else if(resultCode == APIClient.IMAPI_CONN_ERR){
	    	 logger.info("数据库连接失败");
	     }else if(resultCode == APIClient.IMAPI_DATA_ERR){
	    	 logger.info("参数错误");
	     }else if(resultCode == APIClient.IMAPI_DATA_TOOLONG){
	    	 status = MAS_STATUS_TOOLONG;
	    	 logger.info("消息内容太长");
	     }else if(resultCode == APIClient.IMAPI_INS_ERR){
	    	 logger.info("数据库插入错误");
	     }else if(resultCode == APIClient.IMAPI_IFSTATUS_INVALID){
	    	 logger.info("接口处于暂停或失效状态");
	     }else if(resultCode == APIClient.IMAPI_GATEWAY_CONN_ERR){
	    	 logger.info("短信网关未连接");
	     }else{
	            logger.info("出现其他错误,错误码");
	     }
		return status;
	}
	
	/**
	 * 接收所有已返回的短信回执
	 * 
	 * 如果短信接口不支持回执，就返回 null
	 * 
	 * @return  null表示接收失败，数组长度为 0 表示无回执
	 */
	public RPTItem[] receiveRPT(){
		return handler.receiveRPT();
	}
	
	/**
	 * 根据接收数量接收已返回的短信回执
	 * <p>
	 * 如果短信接口不支持回执，就返回 null
	 * 1、要取得的短信回执的条数。当其值为-1 时，表示接
     *      收所有数量的回执
	 * </p>
	 * @param amount 短信回执的条数
	 * @return
	 */
	public RPTItem[] receiveRPT(int amount){
		return handler.receiveRPT(amount);
	}
	
	/**
	 * 根据 smID 和接收数量接收已返回的短信回执
	 * 
	 * <p>
	 * 如果短信接口不支持回执，就返回null
	 *  1、短信 ID，0 到 99999999 中的某一整数。与发送 MT
     *       短信时的 smID 对应。当其值为-1 时，表示此参数
     *       不起作用，只按接收的条数 amount 接收回执
     *  2、要取得的短信回执的条数。当其值为-1 时，此参数
     *       不起作用，只按 smID 取短信回执。当 smID 也为-1
     *       时，此时此方法等同于无参数的 receiveRPT()方法，
     *       接收所有已返回的短信回执  
	 * </p>
	 * @param smID 短信 ID
	 * @param amount 短信回执的条数
	 * @return
	 */
	public RPTItem[] receiveRPT(long smID, int amount){
		return handler.receiveRPT(smID, amount);
	}
	
	/**
	 * 释放连接
	 */
	public void release(){
		handler.release();
	}
	
	public static void main(String[] args) {
		MasClient utils = new MasClient();
		
		utils.init();
	}
}
