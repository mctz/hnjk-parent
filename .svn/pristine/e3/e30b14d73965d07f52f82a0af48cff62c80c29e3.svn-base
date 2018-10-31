package com.hnjk.edu.teaching.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 时间工具类
 * @author fengw
 *
 */
public class CalendarUtil {
	
	/**
	 * 获取当前年
	 * 格式：2014
	 * @return
	 */
	public static String getYear() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
		int year = c.get(Calendar.YEAR);    //获取年
		
		return year + "";
	}
	
	public static String getMonth() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
		int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
		
		return month + "";
	}
	
	public static int getMonthInt() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
		int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
		
		return month;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
