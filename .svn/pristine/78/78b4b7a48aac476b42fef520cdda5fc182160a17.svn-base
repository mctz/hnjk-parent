package com.hnjk.extend.plugin.excel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * excel 组件日期工具类 <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29上午11:18:27
 * @see 
 * @version 1.0
 */

public class DateUtils {
	
	private static Log logger = LogFactory.getLog(DateUtils.class);
	
	/**默认日期格式*/
	private static String defaultPattern = "yyyy-MM-dd";
	
	/**
	 * 校验日期格式是否正确
	 * @param dateStr 日期字符串
	 * @param pattern 格式
	 * @return
	 */
	public static boolean isValidDate(String dateStr, String pattern) {
		boolean isValid = false;
		if (pattern == null || pattern.length() < 1) {
			pattern = "yyyy-MM-dd";
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			//sdf.setLenient(false);
			String date = sdf.format(sdf.parse(dateStr));
			if (date.equalsIgnoreCase(dateStr)) {
				isValid = true;
			}
		} catch (Exception e) {
			isValid = false;
			logger.error(e.getMessage(),e);
		}	
		if(!isValid){
			isValid = isValidDatePatterns(dateStr,"");
		}
		return isValid;
	}
	
	/**
	 * 校验日期格式是否在制定格式中
	 * @param dateStr
	 * @param patterns
	 * @return
	 */
	public static boolean isValidDatePatterns(String dateStr,String patterns){
		if(patterns==null||patterns.length()<1){
			patterns = "yyyy-MM-dd;dd/MM/yyyy;yyyy/MM/dd;yyyy/M/d h:mm";
		}
		boolean isValid = false;
		String[] patternArr = patterns.split(";");
		for(int i=0;i<patternArr.length;i++){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(patternArr[i]);
				//sdf.setLenient(false);
				String date = sdf.format(sdf.parse(dateStr));
				if (date.equalsIgnoreCase(dateStr)) {
					isValid = true;
					DateUtils.defaultPattern = patternArr[i];
					break;
				}
			} catch (Exception e) {
				isValid = false;
				logger.error(e.getMessage(),e);
			}
		}
		return isValid;
	}
	
	/**
	 * 使用指定格式格式化日期
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getFormatDate(String dateStr, String pattern){
		if (pattern == null || pattern.length() < 1) {
			pattern = "yyyy-MM-dd";
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.defaultPattern);
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			String date = format.format(sdf.parse(dateStr));
			return date;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 获取指定日期格式的日期字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getFormatDate(Date date,String pattern){
		if (pattern == null || pattern.length() < 1) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern,java.util.Locale.CHINA);
			String strDate = sdf.format(date);
			return strDate;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	public static void main(String[] args) {		
		/*
		DateFormat df = DateFormat.getDateInstance();
		try {
			
			Date myDate = df.parse("2007/7/9 16:00");			
			System.out.println(getFormatDate("2007/7/9 16:00",""));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	/**
	 * 获取时间字符串：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date) {
		String	pattern = "yyyy-MM-dd";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern,java.util.Locale.CHINA);
			String strDate = sdf.format(date);
			return strDate;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
