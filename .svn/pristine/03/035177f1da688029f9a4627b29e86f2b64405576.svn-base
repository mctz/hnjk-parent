package com.hnjk.edu.util;

/**
 * 工具类
 * <code>Tools</code><p>
 * 
 * @author zik, 广东学苑教育发展有限公司
 * @since： 2015-11-30 13:21:42
 * @see 
 * @version 1.0
 */
public class Toolkit {

	/**
	 * 将某个整数转化为n位字符串
	 * @param value
	 * @param num
	 * @return
	 */
	public static String convertNumberToString(Integer value,int num) throws Exception{
		if(value == null){
			throw new Exception("第一个参数值有误（不能为空）");
		}
		String valStr = String.valueOf(value);
		if(num <= 0){
			throw new Exception("第二个参数值有误（请填写不能小于1的正整数）");
		}
		char[] valArray = valStr.toCharArray();
		char[] convertArray = new char[num]; 
		for(int i=0;i<num;i++){
			convertArray[i] = '0';
		}
		System.arraycopy(valArray, 0, convertArray, convertArray.length-valArray.length, valArray.length);
		
		return new String(convertArray);
	}
	
	
	
}
