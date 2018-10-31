package com.hnjk.core.foundation.utils;

import java.util.Random;

/** 
 * 公共业务的工具类
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Dec 14, 2016 6:17:52 PM 
 * 
 */
public class CommonUtils {

	/**
	 * 获取N位数字随机编码
	 * 
	 * @param num
	 * @return
	 */
	public static String createCodeByNum(int num){
		StringBuffer code  = new StringBuffer();
		for(int i=0; i<num; i++){
			code.append(randomInt(0, 9));
		}
		return code.toString();
	}
	
	/**
	* 生成随机数
	* 
	* */
	public static int randomInt(int from, int to) {
		Random r = new Random();
		return from + r.nextInt(to - from);
	}
	
	public static void main(String[] args) {
		System.out.println(createCodeByNum(7));
	}
}
