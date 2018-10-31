package com.hnjk.edu.netty.common;

public class StringUtil {

	public static String fillWithChar(String str, int length, char c) {
		// 使用GBK编码 中文字符占2位
		int tmpLength = strCnlength(str);
		//当字符串长度大于等于要填充的总长度时，直接返回原字符串
		if (tmpLength >= length) {
			return str;
		} else {

			if ("GBK".equalsIgnoreCase(NettyConstants.CODING)
					|| NettyConstants.CODING.contains("gb")
					|| NettyConstants.CODING.contains("GB")) {
				length = length - (tmpLength - str.length());
			}

			char[] chars = new char[length];
			for (int i = 0; i < length; i++) {
				chars[i] = c;
			}
			for (int i = 0; i < str.length(); i++) {
				chars[i] = str.charAt(i);
			}
			return new String(chars);
		}
	}

	// 返回包含中文字符串的字符串长度
	public static int strCnlength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	// 中文追加空格，截取后的字符串最后再去掉所有空格
	public static String cnfillwithSpace(String value) {
		String chinese = "[\u0391-\uFFE5]";
		StringBuffer buf = new StringBuffer();
		char c;
		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			if (String.valueOf(c).matches(chinese)) {// 如果是中文
				buf.append(c);
				buf.append(' ');
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	 * 填充字符
	 * 
	 * @param str
	 * @param length
	 *            返回的字符串的总长度
	 * @return
	 */
	public static String fillWithChar(byte[] str, int length, char c) {
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = c;
		}
		for (int i = 0; i < str.length; i++) {
			chars[i] = (char) str[i];
		}
		return new String(chars);
	}

	/**
	 * 填充字符
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillWithChar(char[] str, int length, char c) {
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = c;
		}
		for (int i = 0; i < str.length; i++) {

			chars[i] = str[i];
		}
		return new String(chars);
	}

	/**
	 * 填充空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillWithSpace(String str, int length) {
		return fillWithChar(str, length, '\0');
	}

	/**
	 * 填充空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillWithSpace(byte[] str, int length) {
		return fillWithChar(str, length, '\0');
	}

	/**
	 * 填充空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillWithSpace(char[] str, int length) {
		return fillWithChar(str, length, '\0');
	}

	/**
	 * 从前面填充字符串
	 * 
	 * @param str
	 * @param length
	 * @param c
	 * @return
	 */
	public static String fillWithHeadingChar(String str, int length, char c) {
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = c;
		}
		int sl = str.length();
		for (int i = 0; i < sl; i++) {
			chars[length - sl + i] = str.charAt(i);
		}
		return new String(chars);
	}
}
