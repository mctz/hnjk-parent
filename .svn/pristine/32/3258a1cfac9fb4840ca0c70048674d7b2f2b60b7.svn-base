package com.hnjk.core.foundation.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数字处理函数
 * @author Administrator
 *
 */
public class ExNumberUtils {

	/**
	 * 数字转字符串
	 * @param num 数字
	 * @param bits 小数点位数
	 * @param mode 模式：1-四舍五入；2-向下取整
	 * @return
	 */
	public static String toString(double num,int bits,int mode) {
		DecimalFormat formater = new DecimalFormat();  
		 formater.setMaximumFractionDigits(bits);  
		 if(mode==1){
			 formater.setRoundingMode(RoundingMode.UP);
		 }else if (mode==2) {
			 formater.setRoundingMode(RoundingMode.FLOOR);
		}
		return formater.format(num);  
	}
	
	/**
	 * @param numstr 数字字符串
	 * @param bits 小数点位数
	 * @param mode 模式：1-四舍五入；2-向下取整
	 * @return
	 */
	public static String toString(String numstr,int bits,int mode){
		if(ExStringUtils.isNumeric(numstr, 2)){
			return toString(Double.valueOf(numstr), bits, mode);
		}else {
			throw new IllegalArgumentException(numstr+"转数字出错！");
		}
	}
	
	/**
	 * @param num 数字
	 * @param bits 小数点位数
	 * @param mode 模式：1-四舍五入；2-向下取整
	 * @return
	 */
	public static double toDouble(double num,int bits,int mode){
		String numstr = toString(num, bits, mode);
		return Double.valueOf(numstr);
	}
	
	/**
	 * @param numstr 数字字符串
	 * @param bits 小数点位数
	 * @param mode 模式：1-四舍五入；2-向下取整
	 * @return
	 */
	public static double toDouble(String numstr,int bits,int mode){
		if(ExStringUtils.isNumeric(numstr, 2)){
			return toDouble(Double.valueOf(numstr), bits, mode);
		}else {
			throw new IllegalArgumentException(numstr+"转数字出错！");
		}
	}
	
	/**
	 * 保留两位小数，四舍五入
	 * @param num
	 * @return
	 */
	public static String toString(Double num) {
		return num == null?"":String.format("%.2f", num);
	}
	
	/**
	 * 保留两位小数，四舍五入
	 * @param numstr
	 * @return
	 */
	public static String toString(String numstr) {
		if(ExStringUtils.isNumeric(numstr, 2)){
			return toString(Double.valueOf(numstr));
		}else {
			throw new IllegalArgumentException(numstr+"转数字出错！");
		}
	}
	
	/**
	 * 保留两位小数，四舍五入
	 * @param num
	 * @return
	 */
	public static double toDouble(double num){
		return Double.valueOf(toString(num));
	}
	
	/**
	 * 字符串转为double类型
	 * @param numstr
	 * @return
	 */
	public static double toDouble(String numstr){
		if (numstr.contains(".")) {
			return toDouble(Double.valueOf(numstr));
		}else {
			return toDouble(numstr,0,1);
		}

	}
	/**
	 * 取整
	 * @param num 数字
	 * @param mode 模式： 1-四舍五入；2-向下取整；3-向上取整
	 * @return
	 */
	public static double trunc(double num,int mode) {
		double result = new Double(num);
		if(mode==1){
			result = Math.round(num);
		}else if (mode==2) {
			result = Math.floor(num);
		}else if(mode==3){
			result = Math.ceil(num);
		}
		return result;
	}

	public static String toString(BigDecimal num) {
		// TODO Auto-generated method stub
		return num.toPlainString();
	}

	/**
	 * 返回最大值，null值不进行比较
	 * @param numbers
	 * @return
	 */
	public static Long getMaxNumber(Long... numbers) {
		// TODO Auto-generated method stub
		Long result = null;
		for (Long num : numbers) {
			if(num==null){
			}else if (result==null) {
				result = num;
			}else if (num>result) {
				result = num;
			}
		}
		return result;
	}
	/**
	 * 是否为纯数字
	 * @param string
	 * @return
	 */
	public static boolean isNumber(String string) {
        if (string == null) {
			return false;
		}
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }
	/**
	 * 是否包含字母
	 * @param str
	 * @return
	 */
	public static boolean isContainsStr(String str) {  
        String regex=".*[a-zA-Z]+.*";  
        Matcher m=Pattern.compile(regex).matcher(str);  
        return m.matches();  
    }
	/**
	 * 是否包含中文
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

	public static BigDecimal toBigDecimal(Object obj) {
		if (obj == null) {
			return null;
		}
		return new BigDecimal(String.valueOf(obj));
	}

	public static Double toDouble(Object obj) {
		if (obj == null) {
			return null;
		}
		return Double.valueOf(String.valueOf(obj));
	}

	public static Double toDoubleDefault0(Object obj) {
		if (obj == null || "".equals(obj)) {
			return 0.0;
		}
		return Double.valueOf(String.valueOf(obj));
	}

	public static BigDecimal toBigDecimalDefault0(Object obj) {
		if (obj == null || "".equals(obj)) {
			return BigDecimal.valueOf(0);
		}
		return new BigDecimal(String.valueOf(obj));
	}
}
