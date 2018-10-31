package com.hnjk.core.foundation.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.providers.encoding.Md5PasswordEncoder;

/**
 * 基本的安全编码工具类.<p>
 * 提供主要的安全编码工具，如md5,sha1,base64等.<p>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-25下午02:53:21 
 * @version 1.0
 */
public class BaseSecurityCodeUtils {

	//base64 encode chars
	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	//MD5种子
	private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	//base 64 decode chars
	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26,
			27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
			-1, -1, -1 };

	public static Md5PasswordEncoder md5coder = null;

	static {
		md5coder = new Md5PasswordEncoder();
	}

	/**
	 * 使用Base64方式加密
	 * @param data 加密字节数组，如String类型需要使用.getByte[]方法
	 * @return 加密字符串
	 */
	public static String base64Encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	/**
	 * 还原Base64加密字符串
	 * @param str 加密过的字符串
	 * @return 还原后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] base64Decode(String str) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		byte[] data = str.getBytes("US-ASCII");
		int len = data.length;
		int i = 0;
		int b1, b2, b3, b4;
		while (i < len) {
			/* b1 */
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}
			/* b2 */
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
			/* b3 */
			do {
				b3 = data[i++];
				if (b3 == 61) {
					return sb.toString().getBytes("ISO-8859-1");
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
			/* b4 */
			do {
				b4 = data[i++];
				if (b4 == 61) {
					return sb.toString().getBytes("ISO-8859-1");
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			sb.append((char) (((b3 & 0x03) << 6) | b4));
		}
		return sb.toString().getBytes("ISO-8859-1");
	}

	/**
	 * 使用MD5+盐值方式加密用户密码
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String getMd5PasswordEncoder(String password, String salt) {
		return md5coder.encodePassword(password, salt);
	}

	/*
	 * byte to String
	 * @param digest
	 * @return
	 */
	public static String bytetoString(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		String tempStr;

		for (int i = 1; i < digest.length; i++) {
			tempStr = (Integer.toHexString(digest[i] & 0xff));
			if (tempStr.length() == 1) {
				sb.append("0" + tempStr);
			} else {
				sb.append(tempStr);
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	* 使用sha1方式加密字符串.
	* @param inStr 需要加密的字符串
	* @return 加密过的字符串
	* @throws NoSuchAlgorithmException 
	*/
	public static String SHA1Encode(String inStr) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		String outStr = null;
		md = MessageDigest.getInstance("SHA-1"); //选择SHA-1，也可以选择MD5
		byte[] digest = md.digest(inStr.getBytes()); //返回的是byet[]，要转化为String存储比较方便
		outStr = bytetoString(digest);
		return outStr;
	}

	/**
	 * 对一段String生成MD5加密信息
	 * @param message 要加密的String
	 * @return 生成的MD5信息
	 * @throws NoSuchAlgorithmException 
	 */
	public static String getMD5(String message) throws NoSuchAlgorithmException {	
		return md5coder.encodePassword(message,null);
	}
	

	public static void main(String[] args) throws Exception {
		
		
		
		System.out.println(getMD5("hnjk184"));
	}

}
