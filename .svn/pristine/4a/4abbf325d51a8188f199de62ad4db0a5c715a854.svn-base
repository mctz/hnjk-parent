package com.hnjk.core.support.context;

import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;

import java.io.File;


/**
 * <code>Constants</code>系统全局常量定义.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午11:13:55
 * @see 
 * @version 1.0
 */
public final class Constants {

	public static final String TEMPLATES_PATH = "WEB-INF" + File.separator + "templates"+File.separator+"excel"+File.separator;

	private Constants(){}
	
	public static final String BOOLEAN_YES = "Y"; // boolean值：yes
	
	public static final String BOOLEAN_NO = "N"; // boolean值：no
	
	public static final String BOOLEAN_WAIT = "W";//boolean 等待
	
	public static final String BOOLEAN_MOVE = "M";//boolean 移除
	
	public static final int BOOLEAN_TRUE = 1;    // 布尔值：true
	
	public static final int BOOLEAN_FALSE = 0;   // 布尔值：false
	
	public static final String REQUEST_REDIRECT = "redirect:"; // 请求重转发
	
	//D:\xy\datas\
	public static final String EDU3_DATAS_LOCALROOTPATH = ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath");//网院平台v3文件本地存放路径
	
	public static final String EDU3_CACHE_WEBROOTPATH = System.getProperty("xy.root");//缓存根目录
	
	public static final int LEARNBOOKINGFLAG = 1;//学习预约的标识代号	
	
	public static final String FROM_EDU = "edu";//网络来源，公网
	
	public static final String FROM_PUB = "pub";//网络来源，公网
	
    public final static String ACHIEVEMENTLIST_FILENAME = "AchievementList.xml";// 成绩列表文件名称

    public final static String ZIP_FILE_DIR = "achievement"; // 成绩路径

    public final static String ZIP_FILE_NAME = "achievement.zip"; // 成绩zip文件名称

    public final static String ACHIEVEMENTXML_ENCODING = "GB18030";

    public final static String COURSE_SCORE_TYPE_NUMBER = "10";          //成绩类型-数值型
    public final static String COURSE_SCORE_TYPE_ONEHUNHRED = "11";		 //成绩类型-百分制
    public final static String COURSE_SCORE_TYPE_ONEHUNHRED_FIFTY = "12";//成绩类型-150分制
    public final static String COURSE_SCORE_TYPE_CHAR = "20";			 //成绩类型-字符型
    public final static String COURSE_SCORE_TYPE_TWO = "22";			 //成绩类型-二分制
    public final static String COURSE_SCORE_TYPE_THREE = "23";			 //成绩类型-三分制
    public final static String COURSE_SCORE_TYPE_FOUR = "24";			 //成绩类型-四分制	
    public final static String COURSE_SCORE_TYPE_FIVE = "25";			 //成绩类型-五分制
    
    
	public final static String EXAMRESULT_CHECKSTATUS_INIT     = "-1"; //成绩状态 -1     初始状态
	public final static String EXAMRESULT_CHECKSTATUS_SAVE     = "0";  //成绩状态 0        保存
	public final static String EXAMRESULT_CHECKSTATUS_SUBMIT   = "1";  //成绩状态 1        提交
	public final static String EXAMRESULT_CHECKSTATUS_WAIT     = "2";  //成绩状态 2        待审核
	public final static String EXAMRESULT_CHECKSTATUS_PASS     = "3";  //成绩状态 3        审核通过
	public final static String EXAMRESULT_CHECKSTATUS_PUBLISH  = "4";  //成绩状态 4        发布
	 
	public final static String EXAMRESULT_ABNORAMITY_0     	   = "0";  //成绩异常状态 0 正常
	public final static String EXAMRESULT_ABNORAMITY_1     	   = "1";  //成绩异常状态 1 作弊	
	public final static String EXAMRESULT_ABNORAMITY_2     	   = "2";  //成绩异常状态 2 缺考	
	public final static String EXAMRESULT_ABNORAMITY_3     	   = "3";  //成绩异常状态 3 无卷
	public final static String EXAMRESULT_ABNORAMITY_4     	   = "4";  //成绩异常状态 4 其它
	
	public final static String EXAMRESULT_TYPE_0 = "N";//考试类型-正考
	public final static String EXAMRESULT_TYPE_1 = "Y";//考试类型-一补
	public final static String EXAMRESULT_TYPE_2 = "T";//考试类型-二补
	public final static String EXAMRESULT_TYPE_3 = "Q";//考试类型-结补
	public final static String EXAMRESULT_TYPE_4 = "R";//考试类型-返校一补
	public final static String EXAMRESULT_TYPE_5 = "G";//考试类型-返校二补
	public final static String LINE_BREAK = "\n";

    public static enum msgReceiveType {  //消息的接收类型
    	org,role,user 
    } 
    public static enum msgType {  //消息类型
    	tips,sysmsg,usermsg
    } 
    // 通联支付接口有关信息
    public static final String RETURN_STATUS_SUCESS = "0000";// 成功
    public static final String EDU_STU_INFO_ADD = "EDU_STU_INFO_ADD";// 学生信息新增接口
    public static final String EDU_STU_INFO_MODIFY = "EDU_STU_INFO_MODIFY";// 学生信息修改接口
    public static final String EDU_STU_INFO_CANCEL = "EDU_STU_INFO_CANCEL";// 学生信息注销接口
    public static final String EDU_ORDER_INFO_ADD = "EDU_ORDER_INFO_ADD";// 订单信息新增接口
    public static final String EDU_ORDER_RESULT_NOTICE = "EDU_ORDER_RESULT_NOTICE";// 订单处理结果通知接口
    public static final String EDU_BATCH_QUERY = "EDU_BATCH_QUERY";// 请求处理状态查询接口
    public static final String EDU_RESULT_QUERY = "EDU_RESULT_QUERY";// 请求处理结果查询接口
    public static final String EDU_PAYMENT_QUERY = "EDU_PAYMENT_QUERY";// 订单处理结果查询接口
    public static final String EDU_ORDER_INFO_CLOSE = "EDU_ORDER_INFO_CLOSE";// 订单信息失效接口
    
    // 通联支付商户号
//    public static final String TONGLIAN_VISITOR_ID = "";
    
    // 付款方式
    public static final String FEE_PAYMETHOD_CASH = "1";// 现金
    public static final String FEE_PAYMETHOD_BANKOFWITHHOLD= "2";// 银行代扣
    public static final String FEE_PAYMETHOD_ECHINABANK = "3";// 网银支付
    public static final String FEE_PAYMETHOD_POS = "4";// POS支付
    
    // 支付状态
    public static final String FEE_PAYSTATUS_UNPAY = "1";// 未付款
    public static final String FEE_PAYSTATUS_PAYED = "2";// 已付款
    
    // 学位申请状态
    public static final String DEGREEAPPLYSTATUS_APPLYING = "applying";// 待审核
    public static final String DEGREEAPPLYSTATUS_PASS = "pass";// 审核通过
    public static final String DEGREEAPPLYSTATUS_NOPASS = "nopass";// 审核不通过
}
