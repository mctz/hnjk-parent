package com.hnjk.core.foundation.utils;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 提供生成唯一ID字符串的工具类. <p>
 * 使用方法:
 * <pre>
 * 	    GUIDUtils.init();
		System.out.println(" 32位随机数1-"+GUIDUtils.buildMd5GUID(true));
		System.out.println(" 128位随机数 - "+GUIDUtils.buildSimpleGUID(true));
 * </pre>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26下午08:32:47
 * @version 1.0
 */
public class GUIDUtils {
	
	private static final Log logger = LogFactory.getLog(GUIDUtils.class);
	
	/**随机种子*/
	private static Random rand;

	/**安全随即种子*/
	private static SecureRandom secureRand;
	
	/**服务器主机名*/
	private static String localhost;
	
	
	/**
	 * 初始化.
	 *
	 */
	public static void init(){
		secureRand = new SecureRandom();
		rand = new Random(secureRand.nextLong());
		try {
			localhost = InetAddress.getLocalHost().toString();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error(e);
			}
			localhost = "localhost";
		}
	}
	
	/**
	 * 使用MD5加密方式生产GUID.
	 * @param secure 是否启用安全随即种子.
	 * @return 32位GUID字符串，如果创建失败，则返回<code>null</code>
	 */
	public static String buildMd5GUID(boolean secure) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");//MD5方式
			byte[] digest = messageDigest.digest(buildRawGUID(secure).getBytes());
			return new String(Hex.encodeHex(digest));
		} catch (Exception e) {
			logger.error("创建MD5 GUID出错："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 创建一个简单GUID.
	 * @param secure 是否启用安全随即种子.
	 * @return 128位GUID字符串.
	 */
	public static String buildSimpleGUID(boolean secure) {
		return new String(Hex.encodeHex(buildRawGUID(secure).getBytes()));
	}
	
	/**
	 * 创建一个GUID源.
	 * @param secure
	 * @return
	 */
	private static String buildRawGUID(boolean secure) {
		StringBuffer sb = new StringBuffer();
		sb.append(localhost);
		sb.append(":");
		sb.append(System.currentTimeMillis());
		sb.append(":");
		sb.append(random(secure));
		return sb.toString();
	}
	
	/**
	 * 使用随即种子得到一个随机数.
	 * @param secure
	 * @return
	 */
	private static long random(boolean secure) {
		if (secure) {
			return secureRand.nextLong();
		}
		return rand.nextLong();
	}

	public  static void main(String[] args) throws Exception{
		GUIDUtils.init();
		for(int i=0;i<1000;i++){
			System.out.println(GUIDUtils.buildMd5GUID(false));
		}
	}
}
