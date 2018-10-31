package com.hnjk.security.sso.client;

/**
 * SSO工具.
 * <code>SSOUtils</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-9-1 上午11:52:53
 * @see 
 * @version 1.0
 */
public class SSOUtils {
	
	 public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	
	/**
	 * 将 s 进行 BASE64 编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(byte[] s) {
		if (s == null) {
			return null;
		}
		return (new sun.misc.BASE64Encoder()).encode(s);
	}

	/**
	 * 将 s 进行 BASE64 编码,针对url的编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeForUrl(byte[] s) {
		if (s == null) {
			return null;
		}
		String standerBase64 = encode(s);
		String encodeForUrl = standerBase64;
		// 转成针对url的base64编码
		encodeForUrl = encodeForUrl.replace("=", "");
		encodeForUrl = encodeForUrl.replace("+", "*");
		encodeForUrl = encodeForUrl.replace("/", "-");
		// 去除换行
		encodeForUrl = encodeForUrl.replace("\n", "");
		encodeForUrl = encodeForUrl.replace("\r", "");

		// 转换*号为 -x-
		// 防止有违反协议的字符
		encodeForUrl = encodeSpecialLetter1(encodeForUrl);

		return encodeForUrl;

	}

	/**
	 * 转换*号为 -x-， 为了防止有违反协议的字符，-x 转换为-xx
	 * 
	 * @param str
	 * @return
	 */
	private static String encodeSpecialLetter1(String str) {
		str = str.replace("-x", "-xx");
		str = str.replace("*", "-x-");
		return str;
	}

	/**
	 * 转换 -x-号为*，-xx转换为-x
	 * 
	 * @param str
	 * @return
	 */
	private static String decodeSpecialLetter1(String str) {
		str = str.replace("-x-", "*");
		str = str.replace("-xx", "-x");
		return str;
	}

	/**
	 * 将 s 进行 BASE64 编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s) {

		if (s == null) {
			return null;
		}
		return encode(s.getBytes());
	}

	/**
	 * 将 BASE64 编码的字符串 s 进行解码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decode(String s) {
		if (s == null) {
			return null;
		}
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将 BASE64 编码的字符串 s 进行解码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decodeForUrl(String s) {
		if (s == null) {
			return null;
		}
		s = decodeSpecialLetter1(s);
		s = s.replace("*", "+");
		s = s.replace("-", "/");
		s += "=";
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	 public static byte[] addAll(byte[] array1, byte[] array2) {
	        if (array1 == null) {
	            return clone(array2);
	        } else if (array2 == null) {
	            return clone(array1);
	        }
	        byte[] joinedArray = new byte[array1.length + array2.length];
	        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
	        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
	        return joinedArray;
	    }
	 
	 public static byte[] clone(byte[] array) {
	        if (array == null) {
	            return null;
	        }
	        return (byte[]) array.clone();
	    }
	 
	 public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
	        if (array == null) {
	            return null;
	        }
	        if (startIndexInclusive < 0) {
	            startIndexInclusive = 0;
	        }
	        if (endIndexExclusive > array.length) {
	            endIndexExclusive = array.length;
	        }
	        int newSize = endIndexExclusive - startIndexInclusive;
	        if (newSize <= 0) {
	            return EMPTY_BYTE_ARRAY;
	        }

	        byte[] subarray = new byte[newSize];
	        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
	        return subarray;
	    }
}
