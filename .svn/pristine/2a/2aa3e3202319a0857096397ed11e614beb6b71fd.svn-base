package com.hnjk.extend.taglib.function;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.NumberToCNUtils;
import com.hnjk.extend.taglib.BaseTagSupport;

/**
 * 自定义标签（function）函数. <p>
 * 用户通过EL输出到JSP页面，封装一些通用的处理.<p>
 * 常用的function标签，请参见 http://java.sun.com/jsp/jstl/functions.
 * @author： 广东学苑教育发展有限公司.
 * @since： Jun 30, 2009 12:00:48 PM
 * @modify: 
 * @主要功能：
 * @see 
 * @version 1.0
 */
public class JstlCustFunction extends BaseTagSupport{

	private static final long serialVersionUID = 5252129598738210363L;
	
	/**
	 * 转换日期为星期几
	 * @param dayStr
	 * @return
	 * @throws ParseException 
	 */
	public static String convertDay2Week(String dayStr) throws ParseException{
		return ExDateUtils.convertDay2Week(ExDateUtils.DATE_FORMAT.parse(dayStr));
	}
	
	/**
	 * 格式化文件大小为 KB,MB,GB
	 * @param filesize
	 * @return
	 */
	public static String formatFileSize(Long filesize) {
		return FileUtils.getHumanReadableFileSize(filesize);
	}
	
	/**
	 * 判断某个list集合中是否包含某个字符串
	 * @param str
	 * @param list
	 * @return
	 */
	public static Boolean listContains(String str, List<String> list) {
		boolean flag = false;
		if(ExCollectionUtils.isNotEmpty(list)){
			flag = list.contains(str);
		}
		
		return flag;
	}
	
	/**
	 * 判断某个字符串中是否包含某个字符串
	 * @param str
	 * @param org 用","隔开的字符串
	 * @return
	 */
	public static Boolean strContains(String str, String org) {
		boolean flag = false;
		if(ExStringUtils.isNotEmpty(org)){
			flag = Arrays.asList(org.split(",")).contains(str);
		}
		
		return flag;
	}
	
	/**
	 * 根据格式获取对应的时间
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getTimeByPattern(Date date ,String pattern) {
		String dateStr = "";
		try {
			dateStr = ExDateUtils.formatDateStr(date, pattern);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	
	/**
	 * 根据条件获取对应人民币的大写数字
	 * 
	 * @param number
	 * @param monetaryUnit
	 * @return
	 */
	public static String getCNByMonetaryUnit(BigDecimal number, String monetaryUnit) {
		return NumberToCNUtils.getCNByMonetaryUnit(number, monetaryUnit);
	}
	
	/**
	 * 获取对应人民币的大写数字
	 * 
	 * @param number
	 * @param monetaryUnit
	 * @return
	 */
	public static String getCN(BigDecimal number) {
		return NumberToCNUtils.getCN(number);
	}
}
