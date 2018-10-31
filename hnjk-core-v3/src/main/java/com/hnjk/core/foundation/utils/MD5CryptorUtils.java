package com.hnjk.core.foundation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import lombok.Cleanup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * 
 * 32标准MD5加密方法，使用java类库的security包的MessageDigest类处理
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2017年2月17日 上午11:58:00 
 * 
 * 广东学苑教育发展有限公司版权所有,未经授权严禁查看传抄
 */
public class MD5CryptorUtils {
	
	public static final Log log = LogFactory.getLog(MD5CryptorUtils.class);
	// 加密算法
	public static final String ALGORITHM = "MD5";// SHA

	/**
	 * 
	 * 获得MD5加密密码的方法
	 */
	public static String getMD5ofStr(String origString) {
		String origMD5 = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
			byte[] result = md5.digest(origString.getBytes());
			origMD5 = byteArray2HexStr(result);
		} catch (Exception e) {
			log.error("获得MD5加密密码的方法异常!");
			e.printStackTrace();
		}
		return origMD5;
	}

	/**
	 * 
	 * 获得MD5加密密码的方法
	 */
	public static String getMD5ofByte(byte[] data) {
		String origMD5 = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] result = md5.digest(data);
			origMD5 = byteArray2HexStr(result);
		} catch (Exception e) {
			log.error("获得MD5加密密码的方法异常!");
			e.printStackTrace();
		}
		return origMD5;
	}

	/**
	 * 文件进行数字签名
	 * 
	 * @param file
	 * @return
	 */
	public static String getMD5ofFile(File file) {
		byte[] buffer = new byte[1024];
		int len;
		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			@Cleanup FileInputStream in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] b = digest.digest();
			return byteArray2HexStr(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * MD5多次加密方法
	 */
	public static String getMD5ofStr(String origString, int times) {
		String md5 = getMD5ofStr(origString);// 至少加密一次
		for (int i = 0; i < times - 1; i++) {
			md5 = getMD5ofStr(md5);
		}
		return md5;
	}

	/**
	 * 
	 * 密码验证方法
	 */
	public static boolean verifyPassword(String inputStr, String MD5Code) {
		return getMD5ofStr(inputStr).equals(MD5Code);
	}

	/**
	 * 
	 * 重载一个多次加密时的密码验证方法
	 */

	public static boolean verifyPassword(String inputStr, String MD5Code, int times) {
		return getMD5ofStr(inputStr, times).equals(MD5Code);
	}

	/**
	 * 
	 * 处理字节数组得到MD5密码的方法
	 */
	private static String byteArray2HexStr(byte[] bs) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append(byte2HexStr(b));
		}
		return sb.toString();
	}

	/**
	 * 
	 * 字节标准移位转十六进制方法
	 */
	private static String byte2HexStr(byte b) {
		String hexStr = null;
		int n = b;
		if (n < 0) {
			// 定义移位算法 
			n = b & 0x7F + 128;
		}
		hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
		return hexStr.toUpperCase();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("32位加密算法测试......");
//		System.out.println("JesseyHu: "
//				+ getMD5ofStr("<VnbMessage><MessageHead><MessageCode>VNB3PARTY_PAYVOUCHER</MessageCode><MessageID>201507091609318471</MessageID><SenderID>TEST</SenderID><SendTime>2015-07-0916:09:31</SendTime><Sign></Sign></MessageHead><MessageBodyList><MessageBody><customICP>PTE51571312250000002</customICP><orderNo>20150706000014</orderNo><payTransactionNo>13256143</payTransactionNo><payChnlID>03</payChnlID><payTime>2015-07-0814:12:14</payTime><payGoodsAmount>781.73</payGoodsAmount><payTaxAmount>0.0</payTaxAmount><freight>22.23</freight><payCurrency>142</payCurrency><payerName>陈俊伟</payerName><payerDocumentType>01</payerDocumentType><payerDocumentNumber>440583198505304517</payerDocumentNumber></MessageBody></MessageBodyList></VnbMessage>3D7D15BA3FEF491F9096AC4CB7BB0893"));
//		// System.out.println("test: " +
		// getMD5ofStr("A0000002|2015030637222|20150306155415|33600|0000|123456"));
//		String s = "<VnbMessage><MessageHead><MessageCode>VNB3PARTY_PAYVOUCHER</MessageCode><MessageID>201507091709296594</MessageID><SenderID>TEST</SenderID><SendTime>2015-07-09 17:09:29</SendTime><Sign></Sign></MessageHead><MessageBodyList><MessageBody><customICP>PTE51571312250000002</customICP><orderNo>20150706000014</orderNo><payTransactionNo>13256143</payTransactionNo><payChnlID>03</payChnlID><payTime>2015-07-08 14:12:14</payTime><payGoodsAmount>781.73</payGoodsAmount><payTaxAmount>0.0</payTaxAmount><freight>22.23</freight><payCurrency>142</payCurrency><payerName>chenjunwei</payerName><payerDocumentType>01</payerDocumentType><payerDocumentNumber>440583198505304517</payerDocumentNumber></MessageBody></MessageBodyList></VnbMessage>3D7D15BA3FEF491F9096AC4CB7BB0893";
//		String s = "A1234567|123456|1|成功|20150828143805|3D7D15BA3FEF491F9096AC4CB7BB0893";	
//		String s = "<VnbMessage><MessageHead><MessageCode>VNB3PARTY_TRANSINFO</MessageCode><MessageID>201509111744086470</MessageID><SenderID>TEST</SenderID><SendTime>2015-09-11 17:44:08</SendTime><Sign></Sign></MessageHead><MessageBodyList><MessageBody><merName>测试商户</merName><orderNo>A123456</orderNo><orderAmount>0.11</orderAmount><payTransactionNo>1456325</payTransactionNo><payAmount>0.11</payAmount><tradeType>01</tradeType><payName>小明</payName><tradeTime>2015-09-11 13:30:40</tradeTime><paymentAccount>10324325235223</paymentAccount><cardBank>招商银行</cardBank><productName>A商品</productName><receiptGoodsName>小明</receiptGoodsName><receiptGoodsContact>13255336855</receiptGoodsContact><logisticsCompany>XX物流公司</logisticsCompany><logisticsMessage>test</logisticsMessage><logisticsAmount>0.00</logisticsAmount><remarks></remarks></MessageBody></MessageBodyList></VnbMessage>3D7D15BA3FEF491F9096AC4CB7BB0893";
//		String s="<VnbMessage><MessageHead><MessageCode>VNB3PARTY_PAYVOUCHER</MessageCode><MessageID>201508061414368038</MessageID><SenderID>TEST</SenderID><SendTime>2015-08-06 14:14:36</SendTime><Sign></Sign></MessageHead><MessageBodyList><MessageBody><customICP>TEST000000001</customICP><orderNo>NO201508060001</orderNo><payTransactionNo>201508061059110001</payTransactionNo><payChnlID>1</payChnlID><payTime>2015-08-06 14:14:36</payTime><payGoodsAmount>100.00</payGoodsAmount><payTaxAmount>0.00</payTaxAmount><freight>0.00</freight><payCurrency>142</payCurrency><payerName>小明</payerName><payerDocumentType>01</payerDocumentType><payerDocumentNumber>445226198912184536</payerDocumentNumber></MessageBody></MessageBodyList></VnbMessage>3D7D15BA3FEF491F9096AC4CB7BB0893";
		//String s = "A0000001|2015030637222|20150306155344|||123456";
		String s = "TEST2017-02-03f2fd3a5859984dce";
		//		System.out.println("gbk："+getMD5ofByte(s.getBytes("GBK")));
		System.out.println("待签名串-->"+s);
		System.out.println("md5-->"+getMD5ofByte(s.getBytes("utf-8")));
//		System.out.println("GB2312："+getMD5ofByte(s.getBytes("GB2312")));
//		System.out.println("md5："+getMD5ofStr(s));
		
//		System.out.println("md5："+getMD5ofStr(s));
//			getMD5ofByte("黄万值".getBytes("UTF-8"));
		
	}
}
